package com.hypernym.evaconnect.view.ui.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hypernym.evaconnect.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class WebviewCvFragment extends BaseFragment {

    @BindView(R.id.wv_cv)
    WebView wv_cv;
    String cv_url;
    ProgressDialog pDialog;


    public WebviewCvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cv_webview, container, false);
        ButterKnife.bind(this, view);



        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      //  showBackButton();
       // setPageTitle("Preview Pdf");
//        listener();

        try {



            init();

        }
        catch (Exception e)
        {
            init();
        }






    }

    private void init() {
        if (getArguments() != null) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("PDF");
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);









            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

//
//                    wv_cv.setDownloadListener(new DownloadListener() {
//
//                        @Override
//                        public void onDownloadStart(String url, String userAgent,
//                                                    String contentDisposition, String mimetype,
//                                                    long contentLength) {
//                            DownloadManager.Request request = new DownloadManager.Request(
//                                    Uri.parse(url));
//
//                            request.allowScanningByMediaScanner();
//                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
//                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "pdf");
//                            DownloadManager dm = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
//                            dm.enqueue(request);
//                            Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
//                                    Toast.LENGTH_LONG).show();
//                            wv_cv.loadUrl("https://docs.google.com/gview?embedded=true&url=" + cv_url);
//                        }
//                    });




                    LoadPdf();
                }
            }, 600);



        }
    }

    private void listener() {
        wv_cv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                pDialog.dismiss();


                String u=url;

//                if(url.equalsIgnoreCase("https://docs.google.com/gview?embedded=true&url=" + cv_url)) {



//                }

                super.onPageFinished(view, url);


//                Log.e("erroroccuredhere", u);

            }



            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                super.onReceivedError(view, request, error);
//                Log.e("erroroccuredhere",error.toString());

                LoadPdf();

//                wv_cv.reload();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

//                Log.e("erroroccuredhere",errorCode+"");
                LoadPdf();





            }
        });
    }


    public void LoadPdf() {
        wv_cv.setWebViewClient(new AppWebViewClient());
        wv_cv.getSettings().setSupportZoom(true);
        wv_cv.getSettings().setJavaScriptEnabled(true);
        wv_cv.getSettings().setDomStorageEnabled(true);
        wv_cv.getSettings().getAllowFileAccess();
        wv_cv.getSettings().getAllowFileAccessFromFileURLs();
        wv_cv.getSettings().getAllowUniversalAccessFromFileURLs();
        cv_url = getArguments().getString("applicant_cv");




        wv_cv.loadUrl("https://docs.google.com/gview?embedded=true&url=" + cv_url);

    }






    public class AppWebViewClient extends WebViewClient {



        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

//            Log.e("erroroccuredhere",error.toString());

            LoadPdf();

//                wv_cv.reload();
        }




        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);


//            Log.e("erroroccuredhere",errorCode+"");
            LoadPdf();
        }



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {


            view.loadUrl("https://docs.google.com/gview?embedded=true&url=" + cv_url);


            return super.shouldOverrideUrlLoading(view, request);
        }



        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            pDialog.show();
        }



        @Override
        public void onPageFinished(WebView view, String url) {
            //Page load finished


            if (wv_cv.getContentHeight() > 0) {

                pDialog.dismiss();
            }
            else
            {
                wv_cv.reload();
            }




//            Log.e("erroris",url);





//            Log.e("erroris","t1");

            super.onPageFinished(view, url);



//            Log.e("erroris","t2");






        }



    }

}

