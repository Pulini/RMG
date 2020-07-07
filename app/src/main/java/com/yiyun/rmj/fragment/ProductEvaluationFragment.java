package com.yiyun.rmj.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseFragment;
import com.yiyun.rmj.bean.apibean.ProductDetailEvaluationBean;
import com.yiyun.rmj.bean.apiparm.ProductDetailEvaluationParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.view.CircleImageView;
import com.yiyun.rmj.view.RatingBar;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//商品详情-评价
public class ProductEvaluationFragment extends BaseFragment {

    RecyclerView rv_evaluation_list;
    List<ProductDetailEvaluationBean.ProductEvaluation> evaluationList;
    private int page = 1;
    private int productId;
    CommonRecyclerViewAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_evaluation;
    }

    @Override
    protected void initView(View view) {
        evaluationList = new ArrayList<>();
        productId = getArguments().getInt("productId");
        rv_evaluation_list = view.findViewById(R.id.rv_evaluation_list);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_evaluation_list.setLayoutManager(manager);

        adapter = new CommonRecyclerViewAdapter<ProductDetailEvaluationBean.ProductEvaluation>(getContext(),evaluationList) {

            @Override
            public void convert(CommonRecyclerViewHolder h, ProductDetailEvaluationBean.ProductEvaluation entity, int position) {
                TextView tv = h.getView(R.id.tv_ll_num);
                tv.setText("浏览1280次");

                final ImageView iv_un_like = h.getView(R.id.iv_un_like);
                final ImageView iv_like = h.getView(R.id.iv_like);
                iv_un_like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        iv_un_like.setVisibility(View.GONE);
                        iv_like.setVisibility(View.VISIBLE);

                    }
                });

                CircleImageView iv_buyer_icon = h.getView(R.id.iv_buyer_icon);

                TextView tv_buyer_name = h.getView(R.id.tv_buyer_name);
                TextView tv_date = h.getView(R.id.tv_date);
                RatingBar rb_star = h.getView(R.id.rb_star);
                TextView tv_evaluation_content = h.getView(R.id.tv_evaluation_content);
                TextView tv_ll_num = h.getView(R.id.tv_ll_num);
                TextView tv_like_num = h.getView(R.id.tv_like_num);
                RecyclerView recyclerView = h.getView(R.id.rv_img);

                Glide.with(getActivity()).load(entity.getHead()).apply(new RequestOptions()).into(iv_buyer_icon);
                tv_buyer_name.setText(entity.getUsername());
                tv_date.setText(entity.getEvaluate_time());
                rb_star.setStar(entity.getEvaluate_stars());
                tv_evaluation_content.setText(entity.getEvaluate_content());
                tv_ll_num.setText("浏览" + entity.getBrowse_count());
                tv_like_num.setText("" + entity.getLikes());

                LinearLayoutManager manager1 = new LinearLayoutManager(getActivity());
                manager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(manager1);

                ArrayList<String> pictureList = entity.getEvaluate_pictures();
                if(pictureList == null){
                    pictureList = new ArrayList<>();
                }

                recyclerView.setAdapter(new CommonRecyclerViewAdapter<String>(getActivity(),pictureList) {
                    @Override
                    public void convert(CommonRecyclerViewHolder h, String entity, int position) {
                        ImageView iv_picture = h.getView(R.id.iv_picture);
                        Glide.with(getActivity()).load(entity).apply(new RequestOptions()).into(iv_picture);
                    }

                    @Override
                    public int getLayoutViewId(int viewType) {
                        return R.layout.item_picture_show;
                    }
                });



            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_product_evaluation;
            }
        };

        rv_evaluation_list.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        ProductDetailEvaluationParm parm = new ProductDetailEvaluationParm();
        parm.setPage(page);
        parm.setProductId(productId);
        String strDes = DESHelper.encrypt(gson.toJson(parm));
        getCommodityEvaluate(strDes);
    }

    private void getCommodityEvaluate(String desparms){


        api.getCommodityEvaluate(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductDetailEvaluationBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(ProductDetailEvaluationBean obj) {
                        LogUtils.LogE(ProductEvaluationFragment.class.getName() + "--getCommodityEvaluate:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            evaluationList.addAll(obj.getInfo().getData());
                            adapter.notifyDataSetChanged();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

}
