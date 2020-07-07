package com.yiyun.rmj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.yiyun.rmj.R;
import com.yiyun.rmj.bean.PictureInfo;
import com.yiyun.rmj.bean.apibean.ProductBean;
import com.yiyun.rmj.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * File Name : HomeProductAdapter
 * Created by : PanZX on  2020/6/2 21:14
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.HomeProductViewHolder> {
    List<ProductBean.Data> data;
    Context context;
    int Width = 0;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void click(int ID);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public HomeProductAdapter(Context context, List<ProductBean.Data> data, int Width) {
        this.context = context;
        this.data = data;
        this.Width = Width / 2 - QMUIDisplayHelper.dp2px(context, 20);
        LogUtils.LogE("Width=" + this.Width);
    }

    @NonNull
    @Override
    public HomeProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeProductViewHolder holder, int position) {
        setHolder(holder, data.get(position));
    }

    public void setHolder(final HomeProductViewHolder holder, final ProductBean.Data data) {
        ArrayList<PictureInfo> pictureUrls = data.getProductPicture();
        String pictureUrl = "";
        if (pictureUrls != null && pictureUrls.size() > 0) {
            pictureUrl = pictureUrls.get(0).getUrl();
        }
        LogUtils.LogE("pictureUrl=" + pictureUrl);
        Glide.with(context).load(pictureUrl).into(holder.iv_photo);
        holder.tv_title.setText(data.getProductName());
        holder.tv_price.setText(data.getProductPrice());
        holder.iv_photo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Width));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.click(data.getProductId());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class HomeProductViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_photo;
        public TextView tv_title;
        public TextView tv_price;

        public HomeProductViewHolder(View itemView) {
            super(itemView);
            iv_photo = itemView.findViewById(R.id.iv_photo);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}
