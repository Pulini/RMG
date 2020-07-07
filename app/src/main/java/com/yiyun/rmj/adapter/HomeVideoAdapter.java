package com.yiyun.rmj.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yiyun.rmj.R;
import com.yiyun.rmj.bean.VideoModel;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.view.indicatorseekbar.SizeUtils;

import java.util.List;

/**
 * File Name : HomeProductAdapter
 * Created by : PanZX on  2020/6/2 21:14
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class HomeVideoAdapter extends RecyclerView.Adapter<HomeVideoAdapter.HomeVideoViewHolder> {
    List<VideoModel> dataList;
    Context context;

    public HomeVideoAdapter(Context context, List<VideoModel> list) {
        dataList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HomeVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeVideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeVideoViewHolder holder, int position) {
        setHolder(holder, dataList.get(position));
    }

    public void setHolder(final HomeVideoViewHolder holder, final VideoModel data) {
//        Glide.with(context).load(entity.getProduct_picture().get(0).getUrl()).apply(new RequestOptions()).into(iv_product_image);

        Glide.with(context)
                .asBitmap()
                .load(dataList.get(holder.getAdapterPosition()).VideoUrl)
                .listener(new RequestListener<Bitmap>() {

                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pb_loading.setVisibility(View.GONE);
                        holder.iv_play.setBackgroundResource(R.drawable.ic_play);
                        holder.iv_play.setVisibility(View.VISIBLE);
                        dataList.get(holder.getAdapterPosition()).VideoImage = resource;
                        return false;
                    }
                })
                .into(holder.iv_bkg);



//        if (data.VideoImage == null) {
//            new LoadVideoImageTask(new LoadVideoImageTask.OnLoadVideoImageListener() {
//                @Override
//                public void onLoadImage(Bitmap bitmap) {
//                    LogUtils.LogE("------------" + holder.getAdapterPosition());
//                    dataList.get(holder.getAdapterPosition()).VideoImage = bitmap;
//                    holder.iv_bkg.setImageBitmap(dataList.get(holder.getAdapterPosition()).VideoImage);
////                    holder.vv_player.setBackground(new BitmapDrawable(dataList.get(holder.getAdapterPosition()).VideoImage));
//                    holder.rl_loading.setVisibility(View.GONE);
//                    holder.iv_play.setBackgroundResource(R.drawable.ic_play);
//                    holder.iv_play.setVisibility(View.VISIBLE);
//                }
//            }).execute(data.VideoUrl);
//        }
        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.LogE("play----------------" + holder.getAdapterPosition());
                if (holder.vv_player.isPlaying()) {
                    holder.vv_player.pause();
                    holder.iv_play.setBackgroundResource(R.drawable.ic_play);
                } else {
                    if (!data.isReady) {
                        holder.pb_loading.setVisibility(View.VISIBLE);
                        holder.iv_play.setVisibility(View.GONE);
//                        holder.vv_player.setVideoURI(Uri.parse(data.VideoUrl));
                        holder.vv_player.setVideoPath(data.VideoUrl);
                    } else {
                        holder.pb_loading.setVisibility(View.GONE);
                        holder.iv_play.setVisibility(View.GONE);
                        holder.iv_bkg.setVisibility(View.GONE);
                    }
                    holder.vv_player.start();
                }
            }
        });
        holder.vv_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.LogE("player----------------" + holder.vv_player.isPlaying());
                if (holder.iv_play.getVisibility() == View.VISIBLE) {
                    holder.iv_play.setVisibility(View.GONE);
                    return;
                }
                if (holder.vv_player.isPlaying()) {
                    holder.iv_play.setBackgroundResource(R.drawable.ic_pause);
                    holder.iv_play.setVisibility(View.VISIBLE);
                }
            }
        });
//        holder.rl_loading.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "视频加载中...", Toast.LENGTH_SHORT).show();
//            }
//        });
        holder.vv_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            //视频加载完后的回调
            @Override
            public void onPrepared(MediaPlayer mp) {
                LogUtils.LogE("加载完成" + holder.vv_player.isPlaying());
                if (!data.isReady) {
                    data.isReady = true;
                    holder.iv_play.setVisibility(View.GONE);
                    holder.iv_bkg.setVisibility(View.GONE);
                } else {
                    holder.iv_play.setVisibility(View.VISIBLE);
                    holder.iv_bkg.setVisibility(View.VISIBLE);
                }
                holder.pb_loading.setVisibility(View.GONE);

            }
        });

//        holder.vv_player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                // 视频帧开始渲染时设置背景透明
//                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
//                    holder.vv_player.setBackgroundColor(Color.TRANSPARENT);
//                }
//                return true;
//            }
//        });

        holder.vv_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            //视频播放完成后的回调
            @Override
            public void onCompletion(MediaPlayer mp) {
                holder.iv_play.setBackgroundResource(R.drawable.ic_play);
                holder.iv_play.setVisibility(View.VISIBLE);
                holder.iv_bkg.setVisibility(View.VISIBLE);
            }
        });


        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(context, 180)));
//        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

    }

    @Override
    public void onViewRecycled(@NonNull HomeVideoViewHolder holder) {
        LogUtils.LogE("*************");

        if (holder.vv_player.isPlaying()) {
            LogUtils.LogE("暂停播放");
            holder.vv_player.pause();
            holder.iv_play.setBackgroundResource(R.drawable.ic_play);
            holder.iv_play.setVisibility(View.VISIBLE);
            holder.iv_bkg.setVisibility(View.VISIBLE);
        }
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class HomeVideoViewHolder extends RecyclerView.ViewHolder {
        public VideoView vv_player;
        public ImageView iv_bkg;
        //        public ImageView iv_videoPreloadBKG;
        public ImageView iv_play;
        public ProgressBar pb_loading;

        public HomeVideoViewHolder(View itemView) {
            super(itemView);
            vv_player = itemView.findViewById(R.id.vv_player);
            iv_bkg = itemView.findViewById(R.id.iv_bkg);
            iv_play = itemView.findViewById(R.id.iv_play);
            pb_loading = itemView.findViewById(R.id.pb_loading);
        }


    }
}
