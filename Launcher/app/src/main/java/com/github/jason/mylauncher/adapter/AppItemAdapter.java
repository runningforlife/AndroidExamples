package com.github.jason.mylauncher.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jason.mylauncher.R;

import java.util.Collections;
import java.util.List;

/**
 * an app item adapter to bind data to grid view
 */
public class AppItemAdapter extends BaseAdapter{
    private static final String TAG = "AppItemAdapter";

    private Context mContext;
    private List<AppItem> mAppList = Collections.EMPTY_LIST;
    private ItemClickListener mListener;

    public interface ItemClickListener{
        void onItemClick(int position);
    }

    public AppItemAdapter(Context context){
        mContext = context;
    }

    public void setListener(ItemClickListener listener){
        mListener = listener;
    }

    public void setData(List<AppItem> data){
        mAppList = data;
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public AppItem getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = convertView;
        if(root == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            root = inflater.inflate(R.layout.layout_app_item,parent,false);

            ViewHolder vh = new ViewHolder(root,position);
            //TODO: icon size may be different for different apps
            // keep the size the same
            vh.ivIcon.setImageDrawable(mAppList.get(position).getAppIcon());
            vh.tvName.setText(mAppList.get(position).getAppName());
        }

        return root;
    }

    private class ViewHolder{
        ImageView ivIcon;
        TextView tvName;

        public ViewHolder(View root, final int pos){
            ImageView icon = (ImageView)root.findViewById(R.id.iv_app_icon);
            TextView name = (TextView)root.findViewById(R.id.tv_app_name);
            ivIcon = icon;
            tvName = name;

            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos);
                }
            });
        }

        public ViewHolder(ImageView icon,TextView name){
            ivIcon = icon;
            tvName = name;
        }
    }
}
