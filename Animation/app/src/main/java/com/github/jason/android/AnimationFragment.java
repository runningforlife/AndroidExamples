package com.github.jason.android;

import android.content.Intent;
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

import com.github.jason.android.layout.LayoutAnimationActivity;
import com.github.jason.android.view.FullImageActivity;
import com.github.jason.android.view.LargeImageDecodeUtil;
import com.github.jason.android.view.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AnimationFragment extends Fragment implements View.OnClickListener{
    public static final String TAG = "AnimationFragment";

    private TextView mTvImageTitle = null;
    private ImageView mIvImageIcon = null;
    private Button mBtnFromLeft = null;
    private Button mBtnFromRight = null;
    private Button mBtnFromTop = null;
    private Button mBtnFromBottom = null;
    private Button mBtnFadeIn = null;
    private Button mBtnLayoutAnim = null;

    public AnimationFragment() {
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
        mBtnLayoutAnim = (Button)rootView.findViewById(R.id.layout_anim);

        mBtnFromLeft.setOnClickListener(this);
        mBtnFromBottom.setOnClickListener(this);
        mBtnFromRight.setOnClickListener(this);
        mBtnFromTop.setOnClickListener(this);
        mBtnFadeIn.setOnClickListener(this);
        mBtnLayoutAnim.setOnClickListener(this);

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
                startActivity(intent, optionsCompat.toBundle());
                break;
            case R.id.from_right:
                optionsCompat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(), R.anim.enter_from_right, R.anim.fade_out);
                startActivity(intent, optionsCompat.toBundle());
                break;
            case R.id.from_top:
                optionsCompat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(), R.anim.enter_from_top, R.anim.fade_out);
                startActivity(intent, optionsCompat.toBundle());
                break;
            case R.id.from_bottom:
                optionsCompat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(), R.anim.enter_from_bottom, R.anim.fade_out);
                startActivity(intent, optionsCompat.toBundle());
                break;
            case R.id.fade_in:
                optionsCompat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(),R.anim.fade_in,R.anim.fade_out);
                startActivity(intent, optionsCompat.toBundle());
                break;
            case R.id.layout_anim:
                Intent i = new Intent(getContext(), LayoutAnimationActivity.class);
                startActivity(i);
                break;
            default:
                optionsCompat = ActivityOptionsCompat.makeCustomAnimation(
                        getContext(), R.anim.fade_in, R.anim.fade_out);
                startActivity(intent, optionsCompat.toBundle());
                break;
        }
    }
}


