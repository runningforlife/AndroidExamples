package com.github.jason.android.animationsamples;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AnimationMainFragment extends Fragment implements View.OnClickListener{
    public static final String TAG = "AnimationMainFragment";

    private TextView mTvImageTitle = null;
    private ImageView mIvImageIcon = null;
    private Button mBtnFromLeft = null;
    private Button mBtnFromRight = null;
    private Button mBtnFromTop = null;
    private Button mBtnFromBottom = null;
    private Button mBtnFadeIn = null;

    public AnimationMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG,"onCreateView()");
        View rootView = inflater.inflate(R.layout.fragment_animation_main, container, false);

        mTvImageTitle = (TextView)rootView.findViewById(R.id.image_title);
        mIvImageIcon = (ImageView)rootView.findViewById(R.id.image_icon);
        //mIvImageIcon.setImageResource(R.drawable.puppy);
        mIvImageIcon.setImageBitmap(LargeImageDecodeUtil.decodeBitmapFromResource(getResources(),
                R.drawable.puppy, 250, 170));

        mBtnFromLeft = (Button)rootView.findViewById(R.id.from_left);
        mBtnFromRight = (Button)rootView.findViewById(R.id.from_right);
        mBtnFromTop = (Button)rootView.findViewById(R.id.from_top);
        mBtnFromBottom = (Button)rootView.findViewById(R.id.from_bottom);
        mBtnFadeIn = (Button) rootView.findViewById(R.id.fade_in);

        mBtnFromLeft.setOnClickListener(this);
        mBtnFromBottom.setOnClickListener(this);
        mBtnFromRight.setOnClickListener(this);
        mBtnFromTop.setOnClickListener(this);
        mBtnFadeIn.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        Intent intent = new Intent(getActivity(), FullImageActivity.class);
        ActivityOptionsCompat optionsCompat = null;
        switch (id) {
            case R.id.from_left:
                optionsCompat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(), R.anim.enter_from_left, R.anim.fade_out);
                break;
            case R.id.from_right:
                optionsCompat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(), R.anim.enter_from_right, R.anim.fade_out);
                break;
            case R.id.from_top:
                optionsCompat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(), R.anim.enter_from_top, R.anim.fade_out);
                break;
            case R.id.from_bottom:
                optionsCompat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(), R.anim.enter_from_bottom, R.anim.fade_out);
                break;
            case R.id.fade_in:
                optionsCompat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(),R.anim.fade_in,R.anim.fade_out);
                break;
            default:
                optionsCompat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(), R.anim.fade_in, R.anim.fade_out);
                break;
        }
        startActivity(intent, optionsCompat.toBundle());
    }
}


