package com.example.root.listviewloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by root on 15-7-17.
 */
public class ImageLoder {

    private ImageView mImageView;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mImageView.setImageBitmap((Bitmap) msg.obj);
        }
    };

    public void showImageByThread(ImageView imageView, final String url){

        mImageView = imageView;

        new Thread(){

            @Override
            public void run() {
                super.run();
                Bitmap bitmap = getBitmap(url);
                Message message = Message.obtain();
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }
        }.start();

    }

    public void showImageByAsyncTask(ImageView imageView, String url){
        new NewsAsyncTask(imageView).execute(url);
    }

    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap>{

        public NewsAsyncTask(ImageView imageView){
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mImageView.setImageBitmap(bitmap);
        }
    }

    public Bitmap getBitmap(String url){
        Bitmap bitmap;
        InputStream inputStream = null;
        try {
            URL mUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            inputStream = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(inputStream);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
