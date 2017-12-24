package com.dmytrosheiko.crispy.photo;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmytrosheiko.crispy.R;
import com.dmytrosheiko.crispy.network.ApiFactory;
import com.dmytrosheiko.crispy.photo.adapter.PhotosAdapter;
import com.dmytrosheiko.crispy.photo.data.Photo;
import com.dmytrosheiko.crispy.photo.data.SearchResponse;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewPhotosActivity extends AppCompatActivity {

    @BindView(R.id.main_recycler)
    RecyclerViewPager mRecyclerView;
    @BindView(R.id.main_progress)
    View mProgress;
    @BindView(R.id.main_progress_txt)
    TextView mProgressTxt;

    private PhotosAdapter mAdapter;
    private ArrayList<Photo> mPhotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        ButterKnife.bind(this);

        initStatusBar();
        initPager();
        loadPhotos();

        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("first_launch", true)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(NewPhotosActivity.this);
                    dialogBuilder.setTitle(getString(R.string.welcome_title));
                    dialogBuilder.setMessage(getString(R.string.welcome_desc));
                    dialogBuilder.setPositiveButton("Got it", null);
                    dialogBuilder.create().show();
                    PreferenceManager.getDefaultSharedPreferences(NewPhotosActivity.this).edit().putBoolean("first_launch", false).apply();
                }
            }, 2000);
        }
    }

    /**
     * Makes the status bar transparent.
     */
    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    private void initPager() {
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layout);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        mAdapter = new PhotosAdapter(this, Glide.with(this), mPhotos, width > 1080);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadPhotos() {
        retrofit2.Call<SearchResponse> call = ApiFactory.getApiService().getNewPhotos();
        call.enqueue(new retrofit2.Callback<SearchResponse>() {
            @Override
            public void onResponse(retrofit2.Call<SearchResponse> call,
                                   retrofit2.Response<SearchResponse> response) {
                if (!response.isSuccessful()) return;

                int oldSize = mPhotos.size();

                ArrayList<Photo> newPhotos = response.body().getResults();

                mPhotos.addAll(newPhotos);
                mAdapter.notifyItemRangeInserted(oldSize, newPhotos.size());
            }

            @Override
            public void onFailure(retrofit2.Call<SearchResponse> call, Throwable t) {
                Toast.makeText(NewPhotosActivity.this,
                        "Can't get wallpapers. Please try again later :(",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setProgressActive(boolean active, boolean downloading) {
        mProgress.setVisibility(active ? View.VISIBLE : View.GONE);
        if (active) {
            mProgressTxt.setText(downloading ? R.string.downloading_wallpaper
                    : R.string.setting_wallpaper);
        }
    }
}
