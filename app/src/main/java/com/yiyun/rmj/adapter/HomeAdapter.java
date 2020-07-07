package com.yiyun.rmj.adapter;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.yiyun.rmj.R;
import com.yiyun.rmj.activity.BannerHolder;
import com.yiyun.rmj.bean.VideoModel;
import com.yiyun.rmj.bean.apibean.ProductBean;
import com.yiyun.rmj.bean.apibean.RotationBean;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.view.indicatorseekbar.SizeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据不同的 ViewType 返回不同的 ViewHolder
 * 通过 setter 方法将不同的 View 注入进 Adapter
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_BANNER = 0;
    public static final int TYPE_PRODUCT = 1;
    public static final int TYPE_VIDEO = 2;
    public static final int TYPE_FOOT = 3;
    public List<VideoModel> videoData = new ArrayList<>();
    public List<RotationBean.SlidePicture> bannerData = new ArrayList<>();
    public List<ProductBean.Data> productData = new ArrayList<>();

    int Width = 0;
    int Height = 0;
    Context context;

    HomeVideoAdapter hva;
    HomeProductAdapter hpa;

    public HomeAdapter(Context context, int Width, int Height) {
        this.context = context;
        this.Width = Width;
        this.Height = Height;

    }

    OnProductClickListener ProductListener;
    OnImageClickListener ImageListener;

    public interface OnProductClickListener {
        void ProductItemClick(int id);
    }

    public interface OnImageClickListener {
        void ImageItemClick(int id);
    }

    public void setBannerAndVideoData(List<VideoModel> videoData, List<RotationBean.SlidePicture> bannerData, final OnImageClickListener lis) {
        this.videoData.clear();
        this.videoData.addAll(videoData);
        this.bannerData.clear();
        this.bannerData.addAll(bannerData);
        if (videoData.size() > 0) {
            hva = new HomeVideoAdapter(context, videoData);
            notifyItemChanged(2);
        }
        if (this.bannerData.size() > 0) {
            ImageListener = lis;
            notifyItemChanged(0);
        }
    }

    public void setProductData(List<ProductBean.Data> productData, OnProductClickListener lis) {
        this.productData.clear();
        this.productData.addAll(productData);
        if (productData.size() > 0) {
            hpa = new HomeProductAdapter(context, productData, Width);
            ProductListener = lis;
            hpa.setOnItemClickListener(new HomeProductAdapter.OnItemClickListener() {
                @Override
                public void click(int ID) {
                    ProductListener.ProductItemClick(ID);
                }
            });
            notifyItemChanged(1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        }
        if (position == 1) {
            return TYPE_PRODUCT;
        }
        if (position == 2) {
            return TYPE_VIDEO;
        }
        return TYPE_FOOT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            return new BannerImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_rv_banner, parent, false));
        }
        if (viewType == TYPE_PRODUCT) {
            return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_rv_product, parent, false));
        }
        if (viewType == TYPE_VIDEO) {
            return new VideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_rv_video, parent, false));
        }
        return new FootHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_rv_foot, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                setBanner((BannerImageHolder) holder);
                break;
            case 1:
                setProduct((ProductHolder) holder);
                break;
            case 2:
                setVideo((VideoHolder) holder);
                break;
            case 3:
                setFoot((FootHolder) holder);
                break;
        }


    }

    private void setBanner(final BannerImageHolder holder) {
        holder.banner.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, Height / 4));
        holder.banner.setPages(new CBViewHolderCreator<com.yiyun.rmj.activity.BannerHolder>() {
            @Override
            public BannerHolder createHolder() {
                return new BannerHolder();
            }
        }, bannerData).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                .setPageIndicator(new int[]{R.mipmap.home_lunbo_button01, R.mipmap.home_lunbo_button02})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setScrollDuration(1500);
        holder.banner.startTurning(5000);
        holder.banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                LogUtils.LogE("click=========");
                if (ImageListener != null) {
                    ImageListener.ImageItemClick(bannerData.get(position).getProductId());
                }
            }
        });
        holder.itemView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
        );

    }

    private void setProduct(final ProductHolder holder) {
        holder.product.setLayoutManager(new GridLayoutManager(context, 2));
        holder.product.setAdapter(hpa);
        holder.product.addItemDecoration(new SpacesItemDecoration(QMUIDisplayHelper.dp2px(context, 2)));
        holder.itemView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
        );

    }

    private void setVideo(VideoHolder holder) {
        holder.video.setLayoutManager(new LinearLayoutManager(context));
        holder.video.setAdapter(hva);
        holder.video.addItemDecoration(new SpacesItemDecoration(QMUIDisplayHelper.dp2px(context, 2)));
        holder.itemView.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT)
        );
    }

    public void setFoot(FootHolder holder) {
        holder.itemView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                SizeUtils.dp2px(context, 100))
        );
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class BannerImageHolder extends RecyclerView.ViewHolder {
        public ConvenientBanner banner;

        public BannerImageHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.cb_home);
        }
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        public RecyclerView product;

        public ProductHolder(View itemView) {
            super(itemView);
            product = itemView.findViewById(R.id.rv_product);
        }
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        public RecyclerView video;

        public VideoHolder(View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.rv_video);
        }
    }

    public class FootHolder extends RecyclerView.ViewHolder {

        public FootHolder(View itemView) {
            super(itemView);
        }
    }

}
