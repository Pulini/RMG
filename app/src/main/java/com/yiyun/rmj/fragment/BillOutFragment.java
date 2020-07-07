package com.yiyun.rmj.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseFragment;

import java.util.ArrayList;

public class BillOutFragment extends BaseFragment {
    RecyclerView rv_list;
    ArrayList<String> listData;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bill;
    }

    @Override
    protected void initView(View view) {
        listData = new ArrayList<>();
        listData.add("1");
        listData.add("2");

        rv_list = (RecyclerView) view.findViewById(R.id.rv_list);
        rv_list.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_list.setAdapter(new CommonRecyclerViewAdapter<String>(getContext(),listData) {
            @Override
            public void convert(CommonRecyclerViewHolder h, String entity, int position) {

            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_bill;
            }

        });

    }

    @Override
    protected void initData() {

    }
}
