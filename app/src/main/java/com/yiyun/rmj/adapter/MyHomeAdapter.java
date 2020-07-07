package com.yiyun.rmj.adapter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.yiyun.rmj.R;
import com.yiyun.rmj.activity.ProductDetailActivity;
import com.yiyun.rmj.bean.PictureInfo;
import com.yiyun.rmj.bean.apibean.ProductBean;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.view.indicatorseekbar.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据不同的 ViewType 返回不同的 ViewHolder
 * 通过 setter 方法将不同的 View 注入进 Adapter
 */

public class MyHomeAdapter extends RecyclerView.Adapter<MyHomeAdapter.MyViewHolder> {

    private int playIndex = -1;
    public static final int TYPE_HEADER = 0;

    public static final int TYPE_NOMAL = 1;

    private View mHeaderView;

    private Activity mContext;

    private List<ProductBean.Data> mDatas;
    private List<VideoView> videoList;

    private OnItemClickListener mListener;

    public View getmHeaderView() {
        return mHeaderView;
    }

    public MyHomeAdapter(Activity context, List<ProductBean.Data> mDatas) {
        mContext = context;
        this.mDatas = mDatas;
        videoList = new ArrayList<>();
    }

    public void setmHeaderView(View mHeaderView) {
        this.mHeaderView = mHeaderView;
        notifyItemInserted(0);//插入下标0位置
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null) {
            return TYPE_NOMAL;
        }
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_NOMAL;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new MyViewHolder(mHeaderView);
        }

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }

        final int pos = getRealPosition(holder);//这里的 position 实际需要不包括 header
        final ProductBean.Data product = mDatas.get(pos);

        LogUtils.LogE("VideoUrl=" + product.VideoUrl);
        if (product.VideoUrl != null && product.VideoUrl.length() > 0) {
            holder.rl_product.setVisibility(View.GONE);
            holder.rl_video.setVisibility(View.VISIBLE);

//            Uri uri = Uri.parse("http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4");
//            holder.vv_player.setMediaController(new MediaController(mContext));
            holder.vv_player.setVideoURI(Uri.parse(product.VideoUrl));
//            if(!product.isReady){
//               holder.rl_loading.setVisibility(View.GONE);
//               holder.vv_player.requestFocus();
//           }

//            Glide.with(mContext)
//                    .load(uri)
//                    .into(holder.iv_videoBKG);
//            if(product.VideoImage!=null){
//                holder.iv_videoBKG.setImageBitmap(product.VideoImage);
//                holder.iv_videoBKG.setVisibility(View.VISIBLE);
//                holder.iv_videoPreloadBKG.setVisibility(View.GONE);
//            }else{
//                holder.iv_videoBKG.setVisibility(View.GONE);
//                holder.iv_videoPreloadBKG.setVisibility(View.VISIBLE);
//            }
            holder.iv_videoBKG.setVisibility(View.GONE);
            holder.iv_videoPreloadBKG.setVisibility(View.VISIBLE);
            holder.rl_loading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "视频加载中...", Toast.LENGTH_SHORT).show();
                }
            });
            holder.vv_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    product.isReady = true;
                    holder.rl_loading.setVisibility(View.GONE);
                    holder.iv_play.setVisibility(View.VISIBLE);

                }
            });
            holder.vv_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //视频播放完成后的回调
                    holder.iv_play.setBackgroundResource(R.drawable.ic_play);
                    holder.iv_play.setVisibility(View.VISIBLE);

                    if (product.VideoImage != null) {
//                        holder.iv_videoBKG.setImageBitmap(product.VideoImage);
                        holder.iv_videoBKG.setVisibility(View.VISIBLE);
                        holder.iv_videoPreloadBKG.setVisibility(View.GONE);
                    } else {
                        holder.iv_videoBKG.setVisibility(View.GONE);
                        holder.iv_videoPreloadBKG.setVisibility(View.VISIBLE);
                    }
                }
            });

            holder.iv_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.vv_player.isPlaying()) {
                        holder.vv_player.pause();
                        holder.iv_play.setBackgroundResource(R.drawable.ic_play);
                        LogUtils.LogE(getRealPosition(holder) + "stop");
                    } else {
                        holder.vv_player.start();
                        holder.iv_play.setVisibility(View.GONE);
                        LogUtils.LogE(getRealPosition(holder) + "start");
                    }
                    holder.iv_videoBKG.setVisibility(View.GONE);
                    holder.iv_videoPreloadBKG.setVisibility(View.GONE);
                }
            });
            holder.vv_player.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.iv_play.getVisibility() == View.GONE) {
                        holder.iv_play.setBackgroundResource(R.drawable.ic_pause);
                        holder.iv_play.setVisibility(View.VISIBLE);
                    } else {
                        if (holder.vv_player.isPlaying()) {
                            holder.iv_play.setVisibility(View.GONE);
                        }
                    }
                }
            });
            holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(mContext, 200)));


//            holder.vv_player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.setVolume(0f, 0f);
//                    mp.start();
//                    mp.setLooping(true);
//                }
//            });
//            holder.vv_player.setVideoPath(product.VideoUrl);
//            holder.vv_player.requestFocus();
//            holder.vv_player.start();
        } else {
            holder.rl_product.setVisibility(View.VISIBLE);
            holder.rl_video.setVisibility(View.GONE);
            if (pos == 0) {
                holder.txt_shop.setVisibility(View.VISIBLE);
            } else {
                holder.txt_shop.setVisibility(View.GONE);
            }

            holder.tv_product_name.setText(product.getProductName());
            ArrayList<PictureInfo> pictureUrls = product.getProductPicture();
            String pictureUrl = "";
            if (pictureUrls != null && pictureUrls.size() > 0) {
                pictureUrl = pictureUrls.get(0).getUrl();
            }

            Glide.with(mContext).load(pictureUrl).into(holder.iv_product_img);
            holder.tv_product_introduction.setText(product.getComposition());

            holder.tv_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("productId", product.getProductId());
                    intent.putExtra("showFlag", 2); //立即购买
                    mContext.startActivity(intent);
//                    BottomDialog.createDialog(HomeActivity.this, BottomDialog.TYPE_ADD_TO_CART,productData, addOrBuyLisenner).show();
                }
            });

            holder.tv_learn_more_about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProductDetailActivity.class);
                    intent.putExtra("productId", product.getProductId());
                    intent.putExtra("showFlag", 1); //了解更多
                    mContext.startActivity(intent);
//                    Toast.makeText(HomeActivity.this, "点击了解更多", Toast.LENGTH_SHORT).show();
                }
            });
            holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * 添加头部布局后的位置
     * headerView 不为空则 position - 1
     */
    private int getRealPosition(MyViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderView == null ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        //header 不为空，则 rv 的总 Count 需要 +1（把 Header 加上算一个 item）
        return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
      public  TextView tv_product_name;
      public  TextView tv_product_introduction;
      public  TextView tv_learn_more_about;
      public  TextView tv_buy;
      public  ImageView iv_product_img;
      public  TextView txt_shop;
      public  RelativeLayout rl_product;
      public  VideoView vv_player;
      public  RelativeLayout rl_video;
      public  ImageView iv_videoBKG;
      public  ImageView iv_play;
      public  ImageView iv_videoPreloadBKG;
      public  RelativeLayout rl_loading;

        public MyViewHolder(View itemView) {
            super(itemView);
//            if (itemView == mHeaderView) {
//                return;
//            }

            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_introduction = itemView.findViewById(R.id.tv_product_introduction);
            tv_learn_more_about = itemView.findViewById(R.id.tv_learn_more_about);
            tv_buy = itemView.findViewById(R.id.tv_buy);
            iv_product_img = itemView.findViewById(R.id.iv_product_image);
            txt_shop = itemView.findViewById(R.id.txt_shop);
            rl_product = itemView.findViewById(R.id.rl_product);
            vv_player = itemView.findViewById(R.id.vv_player);
            rl_video = itemView.findViewById(R.id.rl_video);
            iv_videoBKG = itemView.findViewById(R.id.iv_videoBKG);
            iv_play = itemView.findViewById(R.id.iv_play);
            iv_videoPreloadBKG = itemView.findViewById(R.id.iv_videoPreloadBKG);
            rl_loading = itemView.findViewById(R.id.rl_loading);


        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {//item 点击事件接口

        void onItemClick(int position, String data);

    }
}
