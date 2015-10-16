package com.mycompany.newswebsitephoto.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mycompany.newswebsitephoto.R;
import com.mycompany.newswebsitephoto.adapter.PhotoListAdapter;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String URL_HTML_PAGE = "http://www.thehindu.com/";
    private GridView imageGridView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageGridView = (GridView) findViewById(R.id.gridView);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading);
        if(isNetworkAvailable(this)) {
            new ParseUrlTask().execute(URL_HTML_PAGE);
        }else{
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this,"Network connnecton is not available!!",Toast.LENGTH_LONG).show();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    private class ParseUrlTask extends AsyncTask<String,Void,List<String>>{

        @Override
        protected List<String> doInBackground(String... params) {
            List<String> imageUrlList = new ArrayList<String>();
            try {
                String url = params[0];
                Log.d(TAG, "Connecting to " + url);
                Document document = Jsoup.connect(url).get();
                // Get document (HTML page) title
                String title = document.title();
                Log.d(TAG, "Title ["+title+"]");
                Elements elements = document.select("img");//need to set proper tag
                for(Element element : elements){
                    String imageUrl = element.attr("src");
                    imageUrlList.add(imageUrl);
                }
            }catch (Exception ex){
                ex.printStackTrace();
                Log.e(TAG,ex.getMessage());
            }
            return imageUrlList;
        }

        @Override
        protected void onPostExecute(List<String> imageUrlList) {
            progressBar.setVisibility(View.GONE);
                DisplayMetrics dm = new DisplayMetrics();

                getWindowManager().getDefaultDisplay().getMetrics(dm);

                int width 	= (int) Math.ceil((double) dm.widthPixels / 2);
                width=width-40;
                int height	= width;

            PhotoListAdapter adapter = new PhotoListAdapter(MainActivity.this);
            adapter.setLayoutParam(width,height);
            adapter.setData(imageUrlList);
            imageGridView.setAdapter(adapter);
            //super.onPostExecute(imageUrlList);
        }
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            Log.d(TAG, " isOnline : True");
            return true;
        } else {
            Log.d(TAG, " isOnline : False");
            return false;
        }
    }
}
