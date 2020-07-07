package com.yiyun.rmj.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.yiyun.rmj.R;
import com.yiyun.rmj.activity.ControlActivity;
import com.yiyun.rmj.adapter.CustomBaseAdapter;
import com.yiyun.rmj.bean.EditTimeBean;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
//定时fragment
public class TimingFragment extends Fragment implements View.OnClickListener {

    private ArrayList<EditTimeBean> list_arry;
    private CustomBaseAdapter<EditTimeBean> adapter;
    private boolean isedit = true;

    public TimingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timing, container, false);
        ListView list_timing = (ListView) view.findViewById(R.id.list_timing);
        ImageView iv_add = (ImageView) view.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(this);

        list_arry = new ArrayList<>();

        adapter = new CustomBaseAdapter<EditTimeBean>(list_arry, R.layout.item_list_time) {
            @Override
            public void bindView(ViewHolder holder, EditTimeBean obj, final int position) {
                ImageView iv_delete = holder.getView(R.id.iv_delete);
                TextView tv_shiduan = holder.getView(R.id.tv_shiduan);
                TextView tv_chongfu = holder.getView(R.id.tv_chongfu);
                TextView tv_specific_time = holder.getView(R.id.tv_specific_time);
                tv_specific_time.setText(obj.getStartTime() + "-" + obj.getEndTime());
                tv_shiduan.setText("时段" + position);
                tv_chongfu.setText(obj.getChongfu());
                if (obj.iseditor()) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_delete.setVisibility(View.GONE);
                }
                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list_arry.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                Switch swit = holder.getView(R.id.switcher);
                if (obj.isopen()) {
                    swit.setChecked(false);
                } else {
                    swit.setChecked(true);
                }

                swit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            Log.e("Pan", "syq1" + isChecked);
                            list_arry.get(position).setIsopen(true);
                        } else {
                            Log.e("Pan", "syq2" + isChecked);
                            list_arry.get(position).setIsopen(false);
                        }
                    }
                });
            }
        };
        list_timing.setAdapter(adapter);
        ControlActivity.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isedit) {
                    ControlActivity.tv_edit.setText("取消");
                    for (int i = 0; i < list_arry.size(); i++) {
                        list_arry.get(i).setIseditor(true);

                    }
                    isedit = false;
                } else {
                    ControlActivity.tv_edit.setText("编辑");
                    for (int i = 0; i < list_arry.size(); i++) {
                        list_arry.get(i).setIseditor(false);

                    }
                    isedit = true;
                }
                adapter.notifyDataSetChanged();
            }
        });
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:

                Intent intent = new Intent();
                intent.setClassName(getActivity(), "com.yiyun.rmj.activity.TimeFrameActivity");
                startActivityForResult(intent, 22);
//                getActivity().overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 66) {
            String starttime = data.getStringExtra("starttime");
            String endtime = data.getStringExtra("endtime");
            String reception = data.getStringExtra("reception");
            EditTimeBean editTimeBean = new EditTimeBean();
            editTimeBean.setStartTime(starttime);
            editTimeBean.setEndTime(endtime);
            editTimeBean.setChongfu(reception);
            list_arry.add(editTimeBean);
            adapter.notifyDataSetChanged();


        }

    }
}
