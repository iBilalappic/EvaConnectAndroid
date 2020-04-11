package com.hypernym.evaconnect.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.AccountCheck;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.onesignal.OneSignal;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class LinkedinActivity extends BaseActivity {

    /*CONSTANT FOR THE AUTHORIZATION PROCESS*/

    /****FILL THIS WITH YOUR INFORMATION*********/
//This is the public api key of our application
    private static final String API_KEY = "81nb3i17mke494";
    //This is the private api key of our application
    private static final String SECRET_KEY = "aHpwMPXwy6zJn7Zh";
    //This is any string we want to use. This will be used for avoiding CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
    private static final String STATE = "E3ZYKC1T6H2yP4z";
    //This is the url that LinkedIn Auth process will redirect to. We can put whatever we want that starts with http:// or https:// .
//We use a made up url that we will intercept when redirecting. Avoid Uppercases.
    private static final String REDIRECT_URI = "https://www.google.com";
    private static final String SCOPES = "r_liteprofile%20r_emailaddress%20w_member_social";
    /*********************************************/

//These are constants used for build the urls
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    String profileUrl = "https://api.linkedin.com/v2/me?projection=(id,firstName,lastName,profilePicture(displayImage~:playableStreams))";
    String emailAddress = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";
    private WebView webView;
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE = "code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    private static final String SCOPE_PARAM = "scope";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    String accessToken;
    String linkedInUserEmailAddress;
    private ProgressDialog pd;
    String linkedInUserId, linkedInUserFirstName, linkedInUserLastName, linkedInUserProfile;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //get the webView from the layout
        webView = (WebView) findViewById(R.id.main_activity_web_view);

        //Request focus for the webview
        webView.requestFocus(View.FOCUS_DOWN);

        //Show a progress dialog to the user
        pd = ProgressDialog.show(this, "", "Loading...", true);

        //Set a custom web view client
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //This method will be executed each time a page finished loading.
                //The only we do is dismiss the progressDialog, in case we are showing any.
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                //This method will be called when the Auth proccess redirect to our RedirectUri.
                //We will check the url looking for our RedirectUri.
                if (authorizationUrl.startsWith(REDIRECT_URI)) {
                    Log.i("Authorize", "");
                    Uri uri = Uri.parse(authorizationUrl);
                    //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                    //If not, that means the request may be a result of CSRF and must be rejected.
                    String stateToken = uri.getQueryParameter(STATE_PARAM);
                    if (stateToken == null || !stateToken.equals(STATE)) {
                        Log.e("Authorize", "State token doesn't match");
                        return true;
                    }

                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null) {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }
                    Log.i("Authorize", "Auth token received: " + authorizationToken);

                    //Generate URL for requesting Access Token
                    String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                    //We make the request in a AsyncTask
                    new PostRequestAsyncTask().execute(accessTokenUrl);

                } else {
                    //Default behaviour
                    Log.i("Authorize", "Redirecting to: " + authorizationUrl);
                    webView.loadUrl(authorizationUrl);
                }
                return true;
            }
        });

        //Get the authorization Url
        String authUrl = getAuthorizationUrl();
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        //Load the authorization URL into the webView
        webView.loadUrl(authUrl);
    }

    private void init() {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication(), this)).get(UserViewModel.class);

    }

    /**
     * Method that generates the url for get the access token from the Service
     *
     * @return Url
     */
    private static String getAccessTokenUrl(String authorizationToken) {
        String URL = ACCESS_TOKEN_URL
                + QUESTION_MARK
                + GRANT_TYPE_PARAM + EQUALS + GRANT_TYPE
                + AMPERSAND
                + RESPONSE_TYPE_VALUE + EQUALS + authorizationToken
                + AMPERSAND
                + CLIENT_ID_PARAM + EQUALS + API_KEY
                + AMPERSAND
                + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI
                + AMPERSAND
                + SECRET_KEY_PARAM + EQUALS + SECRET_KEY;
        Log.i("accessToken URL", "" + URL);
        return URL;
    }

    /**
     * Method that generates the url for get the authorization token from the Service
     *
     * @return Url
     */
    private static String getAuthorizationUrl() {
        String URL = AUTHORIZATION_URL
                + QUESTION_MARK + RESPONSE_TYPE_PARAM + EQUALS + RESPONSE_TYPE_VALUE
                + AMPERSAND + CLIENT_ID_PARAM + EQUALS + API_KEY
                + AMPERSAND + SCOPE_PARAM + EQUALS + SCOPES
                + AMPERSAND + STATE_PARAM + EQUALS + STATE
                + AMPERSAND + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI;
        Log.i("authorization URL", "" + URL);
        return URL;
    }


    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(LinkedinActivity.this, "", "Loading...", true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            if (urls.length > 0) {
                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpost = new HttpPost(url);
                try {
                    HttpResponse response = httpClient.execute(httpost);
                    if (response != null) {
                        //If status is OK 200
                        if (response.getStatusLine().getStatusCode() == 200) {
                            String result = EntityUtils.toString(response.getEntity());
                            //Convert the string result to a JSON Object
                            JSONObject resultJson = new JSONObject(result);
                            //Extract data from JSON Response
                            int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;

                            String accessToken = resultJson.has("access_token") ? resultJson.getString("access_token") : null;
                            Log.e("Tokenm", "" + accessToken);
                            if (expiresIn > 0 && accessToken != null) {
                                Log.i("Authorize", "This is the access Token: " + accessToken + ". It will expires in " + expiresIn + " secs");

                                //Calculate date of expiration
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.SECOND, expiresIn);
                                long expireDate = calendar.getTimeInMillis();

                                ////Store both expires in and access token in shared preferences
                                SharedPreferences preferences = LinkedinActivity.this.getSharedPreferences("user_info", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("expires", expireDate);
                                editor.putString("accessToken", accessToken);
                                editor.commit();

                                return true;
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (status) {
                //If everything went Ok, change to another activity.
                SharedPreferences preferences = LinkedinActivity.this.getSharedPreferences("user_info", 0);
                accessToken = preferences.getString("accessToken", null);
                try {
                    if (accessToken != null) {
                        new GetProfileRequestAsyncTask().execute(profileUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    ;

    private class GetProfileRequestAsyncTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(LinkedinActivity.this, "", "Loading..", true);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            if (urls.length > 0) {
                try {
                    sendGetRequest(profileUrl, accessToken);
                    sendGetRequestForEmail(emailAddress, accessToken);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject data) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (data != null) {

                try {
                    String welcomeTextString = String.format("Welcome %1$s %2$s, You are a %3$s", data.getString("firstName"), data.getString("lastName"), data.getString("headline"));

                } catch (JSONException e) {
                    Log.e("Authorize", "Error Parsing json " + e.getLocalizedMessage());
                }
            }
        }


    }

    public void sendGetRequest(String urlString, String accessToken) throws Exception {
        URL url = new URL(urlString);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + accessToken);
        con.setRequestProperty("cache-control", "no-cache");
        con.setRequestProperty("X-Restli-Protocol-Version", "2.0.0");
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder jsonString = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            jsonString.append(line);
        }
        JSONObject jsonObject = new JSONObject(jsonString.toString());
        Log.d("Complete json object", jsonObject.toString());
        try {
            linkedInUserId = jsonObject.getString("id");
            String country = jsonObject.getJSONObject("firstName").getJSONObject("preferredLocale").getString("country");
            String language = jsonObject.getJSONObject("firstName").getJSONObject("preferredLocale").getString("language");
            String getFirstnameKey = language + "_" + country;
            linkedInUserFirstName = jsonObject.getJSONObject("firstName").getJSONObject("localized").getString(getFirstnameKey);
            linkedInUserLastName = jsonObject.getJSONObject("firstName").getJSONObject("localized").getString(getFirstnameKey);
            linkedInUserProfile = jsonObject.getJSONObject("profilePicture").getJSONObject("displayImage~").getJSONArray("elements").getJSONObject(0).getJSONArray("identifiers").getJSONObject(0).getString("identifier");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void sendGetRequestForEmail(String urlString, String accessToken) throws Exception {
        URL url = new URL(urlString);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + accessToken);
        con.setRequestProperty("cache-control", "no-cache");
        con.setRequestProperty("X-Restli-Protocol-Version", "2.0.0");
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder jsonString = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            jsonString.append(line);
        }
        JSONObject jsonObject = new JSONObject(jsonString.toString());
        linkedInUserEmailAddress = jsonObject.getJSONArray("elements").getJSONObject(0).getJSONObject("handle~").getString("emailAddress");
        Log.d("email json object", linkedInUserEmailAddress);
        Intent intent = new Intent(LinkedinActivity.this, BlankActivity.class);
        intent.putExtra("Email", linkedInUserEmailAddress);
        startActivity(intent);
        pd.dismiss();
       // CheckUserExist(linkedInUserEmailAddress);

    }
//    private void CheckUserExist(String linkedInUserEmailAddress) {
//        if (NetworkUtils.isNetworkConnected(this)) {
////            showDialog();
//            userViewModel.isEmailExist_linkedin(linkedInUserEmailAddress).observe(this, new Observer<BaseModel<List<AccountCheck>>>() {
//                @Override
//                public void onChanged(BaseModel<List<AccountCheck>> listBaseModel) {
//                    if (listBaseModel != null && !listBaseModel.isError()) {
//                      //  if (listBaseModel.getData().get(0).getIsLinkedin() != null && listBaseModel.getData().get(0).getIsLinkedin() == 1) {
//                         //   JustLoginApiCall();
//                      //  }
//
//                    }
//                }
//            });
//        }
//    }

    private void JustLoginApiCall() {

        if (NetworkUtils.isNetworkConnected(this)) {
//            showDialog();
            userViewModel.linkedin_login(linkedInUserEmailAddress).observe(this, new Observer<BaseModel<List<User>>>() {
                @Override
                public void onChanged(BaseModel<List<User>> listBaseModel) {
              //      LoginUtils.userLoggedIn();

                    User userData = listBaseModel.getData().get(0);
                    userData.setUser_id(userData.getId());
                    LoginUtils.saveUser(listBaseModel.getData().get(0));
                    OneSignal.sendTag("email", userData.getEmail());
                    UserDetails.username = userData.getFirst_name();
                    if (listBaseModel.getData().get(0) != null) {
                        LoginUtils.saveUserToken(listBaseModel.getData().get(0).getToken());
                    }

//                    Intent intent = new Intent(LinkedinActivity.this, HomeActivity.class);
//                    // set the new task and clear flags
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
                }
            });
        }

    }


}