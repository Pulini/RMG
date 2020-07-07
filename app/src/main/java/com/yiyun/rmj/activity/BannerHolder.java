package com.yiyun.rmj.activity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.yiyun.rmj.R;
import com.yiyun.rmj.bean.apibean.RotationBean;

/**
 * File Name : BannerHolder
 * Created by : PanZX on  2020/6/2 21:18
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class BannerHolder implements Holder<RotationBean.SlidePicture> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_header_img, null, false);
        imageView = view.findViewById(R.id.iv_head);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, RotationBean.SlidePicture data) {
        Log.d("imgUrl", "UpdateUI: " + data.getSlideshowPicture());
        Glide.with(context).load(data.getSlideshowPicture()).into(imageView);
    }

}
