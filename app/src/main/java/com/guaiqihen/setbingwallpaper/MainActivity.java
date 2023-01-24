package com.guaiqihen.setbingwallpaper;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    String queryUrl = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=ROW";
    String imageUrlBase = "https://www.bing.com";
    String imageQuality = "_UHD.jpg";


    public void setBingWallpaper(Bitmap bitmap) {
        try {
            //Context context = getApplicationContext();
            WallpaperManager wm = WallpaperManager.getInstance(this);
            if (bitmap != null) {
                wm.setBitmap(bitmap, null, false, WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class getBingWallpaper extends Thread {
        @Override
        public void run() {
            Bitmap bmp = null;
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(queryUrl).openConnection();
                conn.setConnectTimeout(6000);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                JSONObject json = new JSONObject(br.readLine());
                br.close();
                JSONArray json1 = json.getJSONArray("images");
                json = json1.getJSONObject(0);
                String urlBase = json.getString("urlbase");
                URL url = new URL(imageUrlBase + urlBase + imageQuality);
                conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                bmp = BitmapFactory.decodeStream(is);
                is.close();
                setBingWallpaper(bmp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        new getBingWallpaper().start();
        finish();
    }
}