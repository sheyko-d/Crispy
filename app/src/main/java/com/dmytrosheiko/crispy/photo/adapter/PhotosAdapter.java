package com.dmytrosheiko.crispy.photo.adapter;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dmytrosheiko.crispy.R;
import com.dmytrosheiko.crispy.photo.NewPhotosActivity;
import com.dmytrosheiko.crispy.photo.data.Photo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private final NewPhotosActivity mActivity;
    private final RequestManager mGlide;
    private final boolean mRaw;
    private ArrayList<Photo> mPhotos;

    public PhotosAdapter(NewPhotosActivity activity, RequestManager glide, ArrayList<Photo> photos,
                         boolean raw) {
        mActivity = activity;
        mGlide = glide;
        mPhotos = photos;
        mRaw = raw;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = mPhotos.get(position);

        mGlide.load(mRaw ? photo.getFullUrl() : photo.getRegularUrl()).placeholder(new ColorDrawable
                (Color.parseColor(photo.getColor()))).centerCrop().into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.photo_img)
        ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            img.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mActivity.setProgressActive(true, true);

            Glide.with(mActivity)
                    .load(mRaw ? mPhotos.get(getAdapterPosition()).getRawUrl()
                            : mPhotos.get(getAdapterPosition()).getFullUrl())
                    .asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(final Bitmap resource,
                                            GlideAnimation<? super Bitmap> glideAnimation) {
                    mActivity.setProgressActive(true, false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                WallpaperManager myWallpaperManager
                                        = WallpaperManager.getInstance(mActivity);
                                myWallpaperManager.setBitmap(resource);
                                mActivity.finish();

                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mActivity, "Wallpaper is set!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (final Exception e) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mActivity, "Error, try again later :(\n\n"
                                                        + e.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mActivity.setProgressActive(false, false);
                                }
                            });
                        }
                    }).start();
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    Toast.makeText(mActivity, "Error, try again later :(\n\n" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    mActivity.setProgressActive(false, false);
                }
            });
        }
    }
}