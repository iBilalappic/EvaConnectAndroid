package com.hypernym.evaconnect.view.ui.fragments;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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
        init();
        showBackButton();
        setPageTitle("Preview Pdf");
        listener();
        return view;
    }

    private void init() {
        if (getArguments() != null) {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setTitle("PDF");
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            wv_cv.setWebViewClient(new WebViewClient());
            wv_cv.getSettings().setSupportZoom(true);
            wv_cv.getSettings().setJavaScriptEnabled(true);
            cv_url = getArguments().getString("applicant_cv");

            wv_cv.loadUrl("https://docs.google.com/gview?embedded=true&url=" + cv_url);
            wv_cv.setDownloadListener(new DownloadListener() {

                @Override
                public void onDownloadStart(String url, String userAgent,
                                            String contentDisposition, String mimetype,
                                            long contentLength) {
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(url));

                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "pdf");
                    DownloadManager dm = (DownloadManager) getContext().getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
                            Toast.LENGTH_LONG).show();
                    wv_cv.loadUrl("https://docs.google.com/gview?embedded=true&url=" + cv_url);
                }
            });

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
                super.onPageFinished(view, url);
                pDialog.dismiss();
            }
        });
    }


}

