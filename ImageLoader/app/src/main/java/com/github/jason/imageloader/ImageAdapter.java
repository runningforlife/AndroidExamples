package com.github.jason.imageloader;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.github.jason.imageloader.ImageAdapter.ImageViewHolder;
import com.github.jason.imageloader.loader.GlideLoader;
import com.github.jason.imageloader.loader.PicassoLoader;
import com.github.jason.imageloader.stats.LoaderStats;
import com.github.jason.imageloader.stats.StatsItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jason.github.com.imageloader.R;

import static com.github.jason.imageloader.ImageAdapter.Loader.GLIDE;
import static com.github.jason.imageloader.ImageAdapter.Loader.PICASSO;

/**
 * Created by jason on 4/20/17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder>{
    private static final String TAG = "ImageAdapter";

    public enum Loader {
        PICASSO("picasso"),
        VOLLEY("volley"),
        GLIDE("glide");

        String val;

        Loader(String value){
            val = value;
        }
    }
    /** image source */
    private final static String[] IMAGE_SOURCE = {
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/40/8/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/40/2/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/41/5/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/41/3/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/42/10/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/42/4/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/42/8/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/42/1/thumb.jpg",
            "http://albumarium.com/media/W1siZiIsIjU2YTBhYmJlNzY3MDczNGIxYzlhMTUwMCJdLFsicCIsInRodW1iIiwiMTkyMHgxOTIwXHUwMDNFIl1d?sha=02ddaf72",
            "http://tupian.g312.com/uploadimg/ent/20150913_c0mpbi5ntzq.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/42/3/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/43/9/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/43/11/thumb.jpg",
            "http://images.freeimages.com/images/previews/5eb/pyramids-1442106.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/43/1/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/43/3/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/43/4/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/43/6/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/43/2/thumb.jpg",
            "http://ppe.oss-cn-shenzhen.aliyuncs.com/collections/43/5/thumb.jpg",
    };

    private static List<String> imgList = new ArrayList<>(Arrays.asList(IMAGE_SOURCE));

    private Context mContext;
    private Loader mLoader = PICASSO;
    private LoaderStats mStats;

    public ImageAdapter(Context context){
        mContext = context;
        mStats = new LoaderStats();
    }

    public void setLoader(Loader loader){
        mLoader = loader;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_image,parent,false);
        return new ImageViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Log.v(TAG,"onBindViewHolder(): position = " + position);
        switch (mLoader){
            case VOLLEY:
                break;
            case GLIDE:
                GlideLoader.load(mContext,imgList.get(position),holder.image,R.drawable.ic_flower_48dp,
                        new GlideLoaderListener(holder.image,new StatsItem()));
                break;
            default:
/*                PicassoLoader.load(mContext,imgList.get(position),
                        holder.image,R.drawable.ic_flower_48dp,new ImageLoaderListener());*/
                PicassoLoader.load(mContext,imgList.get(position),new ImageTarget(holder.image, new StatsItem()),
                        R.drawable.ic_flower_48dp);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public void dumpStats(){
        Log.v(TAG,"dumpStats()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mStats.dumpStats();
            }
        }).start();
    }

    final class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public ImageViewHolder(View root) {
            super(root);

            image = (ImageView)root;

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v(TAG,"onClick(): adapter position = " + getAdapterPosition());

                }
            });
        }
    }

    private class ImageTarget implements Target{
        private ImageView image;
        private StatsItem item;

        public ImageTarget(ImageView view, StatsItem item){
            this.image = view;
            this.item = item;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.v(TAG,"onBitmapLoaded(): bitmap size " + bitmap.getByteCount() + ",loaded from " + from);
            image.setImageBitmap(bitmap);
            mStats.addStat(PICASSO,item);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.v(TAG,"onBitmapFailed()");
            image.setImageDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }

    // for debug purpose
    private class ImageLoaderListener implements Picasso.Listener{

        @Override
        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
            Log.v("PicassoLoader","onImageLoadFailed(): url = " + uri + ",exception = " + exception);
        }
    }

    // for monitoring glide loading complete
    private class GlideLoaderListener implements RequestListener<String, GlideDrawable>{

        private ImageView image;
        private StatsItem item;

        public GlideLoaderListener(ImageView view, StatsItem item){
            this.image = view;
            this.item = item;
        }

        @Override
        public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.v(TAG,"onResourceReady()");
            item.setStartTime(System.currentTimeMillis());
            mStats.addStat(GLIDE,item);
            //image.setImageDrawable(resource);
            return false;
        }
    }


}
