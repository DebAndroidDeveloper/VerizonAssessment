package com.mycompany.newswebsitephoto.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.mycompany.newswebsitephoto.R;
import com.mycompany.newswebsitephoto.activities.MainActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Debasis on 9/28/2015.
 */
public class PhotoListAdapter extends BaseAdapter {

    private Context mContext;

    private ImageLoader mImageLoader;
    private MainActivity.AnimateFirstDisplayListener mAnimator;

    private List<String> mPhotoList;

    private int mWidth;
    private int mHeight;
    private static final String TAG = PhotoListAdapter.class.getCanonicalName();

    public PhotoListAdapter(Context context){
        mContext = context;

        DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .considerExifParams(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .writeDebugLogs()
                .defaultDisplayImageOptions(displayOptions)
                .build();

        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(config);

        mAnimator  = new MainActivity.AnimateFirstDisplayListener();
    }
    @Override
    public int getCount() {
        return mPhotoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPhotoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mPhotoList.indexOf(mPhotoList.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageIv;
        if (convertView == null) {
            imageIv = new ImageView(mContext);

            imageIv.setLayoutParams(new GridView.LayoutParams(mWidth, mHeight));
            imageIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageIv.setPadding(0, 0, 0, 0);
        }  else {
            imageIv = (ImageView) convertView;
        }

        mImageLoader.displayImage(mPhotoList.get(position), imageIv, mAnimator);

        return imageIv;
    }

    public void setLayoutParam(int width, int height) {
        mWidth 	= width;
        mHeight = height;
    }

    public void setData(List<String> imageData) {
        mPhotoList = imageData;
    }

}
