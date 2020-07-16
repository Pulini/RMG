package com.yiyun.rmj.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.yiyun.rmj.R;
import com.yiyun.rmj.bean.VideoModel;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.view.SampleCoverVideo;
import com.yiyun.rmj.view.indicatorseekbar.SizeUtils;

import java.util.List;

/**
 * File Name : HomeProductAdapter
 * Created by : PanZX on  2020/6/2 21:14
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class HomeVideo2Adapter extends RecyclerView.Adapter<HomeVideo2Adapter.HomeVideoViewHolder> {

    List<VideoModel> dataList;
    Context context;


    public HomeVideo2Adapter(Context context, List<VideoModel> list) {
        dataList = list;
        this.context = context;

    }

    @NonNull
    @Override
    public HomeVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeVideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_video2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeVideoViewHolder holder, int position) {
        setHolder(holder, position);
    }

    public void setHolder(HomeVideoViewHolder holder, int position) {

        holder.gsyvp_player.getBackButton().setVisibility(View.VISIBLE);
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.video_bkg);
        Glide.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop()
                                .error(R.mipmap.video_bkg)
                                .placeholder(R.mipmap.video_bkg))
                .load(dataList.get(position).VideoUrl)
                .into(imageView);
        holder.gsyvp_player.setThumbImageView(imageView);

        holder.gsyvp_player.setUpLazy(dataList.get(position).VideoUrl, true, null, null, " ");
        //增加title
        holder.gsyvp_player.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        holder.gsyvp_player.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        holder.gsyvp_player.getFullscreenButton().setOnClickListener(v -> holder.gsyvp_player.startWindowFullscreen(context, false, true));
        //防止错位设置
        holder.gsyvp_player.setPlayTag(dataList.get(position).VideoUrl);
        holder.gsyvp_player.setPlayPosition(position);
        //是否根据视频尺寸，自动选择竖屏全屏或者横屏全屏
        holder.gsyvp_player.setAutoFullWithSize(true);
        //音频焦点冲突时是否释放
        holder.gsyvp_player.setReleaseWhenLossAudio(false);
        //全屏动画
        holder.gsyvp_player.setShowFullAnimation(true);
        //小屏时不触摸滑动
        holder.gsyvp_player.setIsTouchWiget(false);
        holder.gsyvp_player.isInPlayingState();
        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(context, 180)));
//        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class HomeVideoViewHolder extends RecyclerView.ViewHolder {
        public SampleCoverVideo gsyvp_player;

        public HomeVideoViewHolder(View itemView) {
            super(itemView);
            gsyvp_player = itemView.findViewById(R.id.gsyvp_player);
        }
    }
}
