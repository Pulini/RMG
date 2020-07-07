package com.yiyun.rmj.fragment;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseFragment;
import com.yiyun.rmj.bean.apibean.ProductDetailBean;

import java.util.ArrayList;

public class ProductDetailFragment extends BaseFragment {

    RecyclerView rv_product_detail;
    ArrayList<ProductDetailBean.ProductPicture> pictureList;
    CommonRecyclerViewAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_detail;
    }

    @Override
    protected void initView(View view) {
        pictureList = new ArrayList<>();
        rv_product_detail = view.findViewById(R.id.rv_product_detail);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv_product_detail.setLayoutManager(manager);
        adapter = new CommonRecyclerViewAdapter<ProductDetailBean.ProductPicture>(getActivity(), pictureList) {
            @Override
            public void convert(CommonRecyclerViewHolder h, ProductDetailBean.ProductPicture entity, int position) {
                ImageView iv_product_detail_img = h.getView(R.id.iv_product_detail_img);
                Glide.with(getActivity()).load(entity.getUrl()).apply(new RequestOptions()).into(iv_product_detail_img);
            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_product_detail_img;
            }
        };
        rv_product_detail.setAdapter(adapter);

    }

    public void refreshData(ArrayList<ProductDetailBean.ProductPicture> data){
        pictureList.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }
}
