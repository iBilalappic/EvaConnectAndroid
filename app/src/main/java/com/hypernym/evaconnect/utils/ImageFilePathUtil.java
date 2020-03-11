package com.hypernym.evaconnect.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

public class ImageFilePathUtil {
    /**
     * Method for return file path of Gallery image
     *
     * @param context
     * @param uri
     * @return path of the selected image file from gallery
     */
    static String nopath = "File path not found";

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        try {
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/"
                                + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    try {
                        final String id = DocumentsContract.getDocumentId(uri);
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"),
                                Long.valueOf(id));

                        return getDataColumn(context, contentUri, null, null);
                    } catch (Exception ex) {
                        Log.e("FILE", ex.getMessage());
                        return nopath;

                    }
                }

                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type) || "pdf".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }


                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] { split[1] };

                    return getDataColumn(context, contentUri, selection,
                            selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        catch (Exception ex)
        {
            return nopath;
        }
        return nopath;
    }

    public static String getFilePath(final Context context, final Uri imageuri) {
        String tempID= "", id ="",actualfilepath="";
        try {
            if (imageuri.getAuthority().equals("media")){
                tempID =   imageuri.toString();
                tempID = tempID.substring(tempID.lastIndexOf("/")+1);
                id = tempID;
                Uri contenturi = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String selector = MediaStore.Images.Media._ID+"=?";
                actualfilepath = getColunmData( context,contenturi, selector, new String[]{id}  );
            }else if (imageuri.getAuthority().equals("com.android.providers.media.documents")){
                tempID = DocumentsContract.getDocumentId(imageuri);
                String[] split = tempID.split(":");
                String type = split[0];
                id = split[1];
                Uri contenturi = null;
                if (type.equals("image")){
                    contenturi = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }else if (type.equals("video")){
                    contenturi = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }else if (type.equals("audio")){
                    contenturi = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selector = "_id=?";
                actualfilepath = getColunmData( context,contenturi, selector, new String[]{id}  );
            } else if (imageuri.getAuthority().equals("com.android.providers.downloads.documents")){
                tempID =   imageuri.toString();
                tempID = tempID.substring(tempID.lastIndexOf("/")+1);
                id = tempID;
                Uri contenturi = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                // String selector = MediaStore.Images.Media._ID+"=?";
                actualfilepath = getColunmData(context, contenturi, null, null  );
            }else if (imageuri.getAuthority().equals("com.android.externalstorage.documents")){
                tempID = DocumentsContract.getDocumentId(imageuri);
                String[] split = tempID.split(":");
                String type = split[0];
                id = split[1];
                Uri contenturi = null;
                if (type.equals("primary")){
                    actualfilepath=  Environment.getExternalStorageDirectory()+"/"+id;
                }
            }
            return actualfilepath;
        }
        catch (Exception ex)
        {
            return  nopath;
        }


    }
    public static String getColunmData(Context context, Uri uri, String selection, String[] selectarg){
        String filepath ="";
        Cursor cursor = null;
        String colunm = "_data";
        String[] projection = {colunm};
        cursor =  context.getContentResolver().query( uri, projection, selection, selectarg, null);
        if (cursor!= null){
            cursor.moveToFirst();
          //  Log.e(TAG, " file path is "+  cursor.getString(cursor.getColumnIndex(colunm)));
            filepath = cursor.getString(cursor.getColumnIndex(colunm));
        }
        if (cursor!= null)
            cursor.close();
        return  filepath;
    }
    /**
     * Get the value of the data column for this Uri. This is <span id="IL_AD2"
     * class="IL_AD">useful</span> for MediaStore Uris, and other file-based
     * ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return nopath;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }
}
