package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    String URLs = "https://meme-api.herokuapp.com/gimme";
    ImageView ig;
    String sharememe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchWallpaper();

    }


    public void fetchWallpaper() {
        ProgressDialog pro = new ProgressDialog(this);
        pro.setMessage("Loding....");
        pro.show();
        String url = "https://meme-api.herokuapp.com/gimme";
        //RequestQueue queue = Volley.newRequestQueue(this);
        ig = findViewById(R.id.memeImageView);
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try {

                            String ur = response.getString("url");
//                            Glide.with(MainActivity.this).load(ur).into(ig);
                            Glide.with(MainActivity.this).load(ur)
                                    .placeholder(R.drawable.loading_pro).into(ig);
                            sharememe = ur;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        finally {
                            pro.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Loding Failed!!", Toast.LENGTH_SHORT).show();
                        pro.dismiss();


                    }
                });
//        queue.add(jsonObjectRequest);

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }


    public void showNextMeme(View view) {
        fetchWallpaper();
    }

    public void shareMeme(View view) {
        Intent myintent = new Intent(Intent.ACTION_SEND);
        myintent.setType("text/plan");

        String shereBoday = "Your Apps are Here";

        String shereSub = sharememe;
        myintent.putExtra(Intent.EXTRA_SUBJECT, shereBoday);
        myintent.putExtra(Intent.EXTRA_TEXT, shereSub);
        startActivity(Intent.createChooser(myintent, "Share Using"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.download) {

            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(sharememe);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            downloadManager.enqueue(request);
            Toast.makeText(this, "Downloading Start", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}