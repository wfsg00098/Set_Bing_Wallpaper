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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    String queryUrl = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=ROW";
    String imageUrlBase = "https://www.bing.com";
    String imageQuality = "_1080x1920.jpg";
    String data_path;
    File file;

    public void setBingWallpaper() {
        try {
            //Context context = getApplicationContext();
            WallpaperManager wm = WallpaperManager.getInstance(this);

            //wm.setBitmap(bitmap, null, false, WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
            FileInputStream fis = new FileInputStream(file);
            wm.setStream(fis,null,false,WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
            runOnUiThread(this::finish);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class getBingWallpaper extends Thread {
        @Override
        public void run() {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(queryUrl).openConnection();
                conn.setConnectTimeout(1000);
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
                FileOutputStream fos = new FileOutputStream(file);
                byte[] temp = new byte[16*1024];
                int len;
                while ((len = is.read(temp)) != -1) {
                    fos.write(temp, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
                setBingWallpaper();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data_path = getFilesDir().getAbsolutePath() + "/";
        file = new File(data_path + "temp.jpg");
        new getBingWallpaper().start();
        finish();
    }
}