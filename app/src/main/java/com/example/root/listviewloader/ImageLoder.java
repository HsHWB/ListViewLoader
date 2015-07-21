package com.example.root.listviewloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 15-7-17.
 */
public class ImageLoder {

    private ImageView mImageView;
    private String mUrl;
    private LruCache<String, Bitmap> mCaches;
    private ListView mListView;
    private Set<NewsAsyncTask> mTask;

    public ImageLoder(ListView listView){

        mListView = listView;
        mTask = new HashSet<>();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 4;
        mCaches = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

    }


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
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap == null){
            imageView.setImageResource(R.mipmap.ic_launcher);

        }else{
            imageView.setImageBitmap(bitmap);
        }
        new NewsAsyncTask(url).execute(url);
    }

    public void addBitmapToCache(String url, Bitmap bitmap){
        if (getBitmapFromCache(url) == null){
            mCaches.put(url, bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String url){
        return mCaches.get(url);
    }

    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap>{

        private ImageView mImageView;
        private String mUrl;

        public NewsAsyncTask(String url){
//            mImageView = imageView;
            mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bitmap = getBitmap(url);
            if (bitmap != null){
                addBitmapToCache(url, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) mListView.findViewWithTag(mUrl);
            if (imageView != null && bitmap != null){
                imageView.setImageBitmap(bitmap);
            }
            mTask.remove(this);
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

    public void loadImages(int start, int end){
        for (int i = start; i < end; i++){
            String url = MyAdapater.URLS[i];
            Bitmap bitmap = getBitmapFromCache(url);
            if (bitmap == null){
                NewsAsyncTask task = new NewsAsyncTask(url);
                task.execute(url);
                mTask.add(task);
            }else{
                ImageView imageView = (ImageView) mListView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            }
        }

    }

    public void cancelAllTask(){
        if (mTask != null){
            for (NewsAsyncTask task : mTask){
                task.cancel(false);
            }
        }
    }
}
