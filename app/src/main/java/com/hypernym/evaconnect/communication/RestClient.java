package com.hypernym.evaconnect.communication;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypernym.evaconnect.BuildConfig;
import com.hypernym.evaconnect.communication.api.AppApi;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static RestClient clientInstance = null;
    private static Retrofit retrofit;
    private static Retrofit retrofit_onesignal;
    private AtomicBoolean reset = new AtomicBoolean(false);
    private static final long TIMEOUT = 2;
    private AppApi appApi, appApi_onesignal;

    private RestClient() {
    }

    public static RestClient get() {
        if (clientInstance == null) {
            return _get();
        }
        return clientInstance;
    }


    private static synchronized RestClient _get() {
        if (clientInstance == null) {
            clientInstance = new RestClient();
        }
        return clientInstance;
    }

//       .baseUrl(BuildConfig.BASE_SERVER_URL)

    private synchronized Retrofit retrofit() {
        if (retrofit == null || reset.get()) {
//            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .setVersion(1.0)
                    .create();

            Retrofit.Builder retrofitBuilder =
                    new Retrofit.Builder()
                            .baseUrl(BuildConfig.BASE_SERVER_URL)           /*BuildConfig.BASE_SERVER_URL*/
                            .client(getUnsafeOkHttpClient(AppUtils.getApplicationContext(),AppConstants.SIMPLE_BASEURL))
                            .addConverterFactory(GsonConverterFactory.create(gson));

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            retrofit = retrofitBuilder.client(getUnsafeOkHttpClient(AppUtils.getApplicationContext(),AppConstants.SIMPLE_BASEURL)).build();
        }
        return retrofit;
    }

    private synchronized Retrofit retrofit_onesignal() {
        if (retrofit_onesignal == null || reset.get()) {
//            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .setVersion(1.0)
                    .create();

            Retrofit.Builder retrofitBuilder =
                    new Retrofit.Builder()
                            .baseUrl("http:/67.205.178.219:8000/eva/") /*BuildConfig.BASE_SERVER_URL*/
                            .client(getUnsafeOkHttpClient(AppUtils.getApplicationContext(), AppConstants.ONESIGNAL_BASEURL))
                            .addConverterFactory(GsonConverterFactory.create(gson));

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            retrofit_onesignal = retrofitBuilder.client(getUnsafeOkHttpClient(AppUtils.getApplicationContext(), AppConstants.ONESIGNAL_BASEURL)).build();
        }
        return retrofit_onesignal;
    }

    private static OkHttpClient getUnsafeOkHttpClient(final Context context, String name) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder().readTimeout(TIMEOUT, TimeUnit.MINUTES);
//            .addInterceptor(new NetworkConnectionInterceptor(context)).readTimeout(20, TimeUnit.MINUTES);

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);

            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder _builder = chain.request().newBuilder();
                    Request original = chain.request();
                    //todo: fill auth header
                    _builder.header("Cache-Control", "no-cache");
                    _builder.header("Content-Type", "application/json")
                     .method(original.method(), original.body());

                    if (LoginUtils.isUserLogin()) {
                        if (name.equals(AppConstants.SIMPLE_BASEURL)) {
                            _builder.header("Authorization", "token " + LoginUtils.getAuthToken(AppUtils.getApplicationContext()));
                            Log.d("TAG", "intercept: "+LoginUtils.getAuthToken(AppUtils.getApplicationContext()));
                        } else {
                            _builder.header("Authorization", "Basic OGY1MDlkNzItMzljNS00ZGY2LTg5MmItMmEwOGQ2YzMwZWU4");
                        }
                        Log.d("Token", "token " + LoginUtils.getAuthToken(AppUtils.getApplicationContext()));
                        Log.d("name", name);
                    }

                    _builder.header("OS", AppConstants.OS);
                    Request request = _builder.build();
                    return chain.proceed(request);
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AppApi appApi() {
        if (appApi == null) {
            appApi = retrofit().create(AppApi.class);
        }
        return appApi;
    }

    public AppApi appApi_onesignal() {
        if (appApi_onesignal == null) {
            appApi_onesignal = retrofit_onesignal().create(AppApi.class);
        }
        return appApi_onesignal;
    }
}
