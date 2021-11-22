package com.example.memesharing;

import static android.content.Intent.createChooser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    private void displaySetting(){
        Button nextButton = findViewById(R.id.nextButton);
        Button shareButton = findViewById(R.id.shareButton);


        nextButton.setTextColor(Color.WHITE);
        nextButton.setBackgroundColor(Color.GRAY);

        shareButton.setTextColor(Color.WHITE);
        shareButton.setBackgroundColor(Color.GRAY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displaySetting();

        loadMeme();

    }
    private String currenturl = null;
    private ImageView memeImageView,bitmap;
    private void loadMeme(){
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://meme-api.herokuapp.com/gimme";

        // Request a string response from the provided URL.
        JsonObjectRequest JsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            currenturl = response.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        memeImageView = (ImageView) findViewById(R.id.imageView);
                        Glide.with(MainActivity.this).load(currenturl).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                        Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                           DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);

                                return false;
                            }
                        }).into(memeImageView);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(JsonObjectRequest);
    }

    public void nextMeme(View view) {
        loadMeme();
    }

    public void shareMeme(View view) throws URISyntaxException {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"look at this funny meme "+currenturl);
        intent.setType("image/*");
        final Intent chooser = createChooser(intent, "Share using");
        startActivity(chooser);
    }

}