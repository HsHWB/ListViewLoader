package com.example.root.listviewloader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private ListView mListView;
    private static String URL = "http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview);
        new MyAsyncTask().execute(URL);
    }

    private List<MyItem> getJsonDate(String url) {

        List<MyItem> myItems = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(url).openStream());
            Log.d("test", jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myItems;
    }


    private String readStream (InputStream inputStream){

        InputStreamReader inputStreamReader;
        String result = "";
        try {
            String line = "";
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            while ( (line = br.readLine()) != null ){
                result +=line;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <url, Void, return>
     */
    class MyAsyncTask extends AsyncTask<String, Void, List<MyItem>>{

        @Override
        protected List<MyItem> doInBackground(String... params) {

            String url = params[0];
            return getJsonDate(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action b ar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
