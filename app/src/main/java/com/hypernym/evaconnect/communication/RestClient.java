package com.hypernym.evaconnect.communication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypernym.evaconnect.communication.api.AppApi;
import com.hypernym.evaconnect.constants.APIConstants;
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
    private AtomicBoolean reset = new AtomicBoolean(false);
    private static final long TIMEOUT = 2;
    private AppApi appApi;

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

    private synchronized Retrofit retrofit() {
        if (retrofit == null || reset.get()) {
//            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .setVersion(1.0)
                    .create();

            Retrofit.Builder retrofitBuilder =
                    new Retrofit.Builder()
                            .baseUrl(APIConstants.BASE_SERVER_URL)
                            .client(getUnsafeOkHttpClient(AppUtils.getApplicationContext()))
                            .addConverterFactory(GsonConverterFactory.create(gson));

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            retrofit = retrofitBuilder.client(getUnsafeOkHttpClient(AppUtils.getApplicationContext())).build();
        }
        return retrofit;
    }

    private static OkHttpClient getUnsafeOkHttpClient(final Context context) {
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
                    //todo: fill auth header

                    if(LoginUtils.isUserLogin()){
                        _builder.header("Authorization", "token "+LoginUtils.getAuthToken(AppUtils.getApplicationContext()));
                    Log.d("Token","token "+LoginUtils.getAuthToken(AppUtils.getApplicationContext()));
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
}
