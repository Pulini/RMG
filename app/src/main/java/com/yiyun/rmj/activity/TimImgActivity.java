package com.yiyun.rmj.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CustomBaseAdapter;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.EditTimeBean;

import java.util.ArrayList;

public class TimImgActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_edit;
    private CustomBaseAdapter<EditTimeBean> adapter;
    private boolean isedit = true;
    private ArrayList<EditTimeBean> list_arry;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_tim_img;
    }

    @Override
    protected void initView() {
        TextView tv_control = (TextView) findViewById(R.id.tv_control);
        TextView tv_timing = (TextView) findViewById(R.id.tv_timing);
        TextView tv_clean = (TextView) findViewById(R.id.tv_clean);
        tv_clean.setOnClickListener(this);
        tv_control.setOnClickListener(this);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(this);
        ImageView iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_add.setOnClickListener(this);
        ListView list_timing = (ListView) findViewById(R.id.list_timing);
        list_arry = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            list_arry.add(new EditTimeBean());
        }
        adapter = new CustomBaseAdapter<EditTimeBean>(list_arry, R.layout.item_list_time) {
            @Override
            public void bindView(ViewHolder holder, EditTimeBean obj, int position) {
                ImageView iv_delete = holder.getView(R.id.iv_delete);
                if (obj.iseditor()) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_delete.setVisibility(View.GONE);
                }
                Switch swit = holder.getView(R.id.switcher);
            }
        };
        list_timing.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit:
                if (isedit) {
                    tv_edit.setText("取消");
                    for (int i = 0; i < list_arry.size(); i++) {
                        list_arry.get(i).setIseditor(true);

                    }
                    isedit = false;
                } else {
                    tv_edit.setText("编辑");
                    for (int i = 0; i < list_arry.size(); i++) {
                        list_arry.get(i).setIseditor(false);

                    }
                    isedit = true;
                }
                adapter.notifyDataSetChanged();

                break;

            case R.id.iv_add:
                // 跳转到时段页面
                Intent intent = new Intent();
                intent.setClassName(this, "com.yiyun.rmj.activity.TimeFrameActivity");
                startActivity(intent);
                this.overridePendingTransition(R.anim.activity_open, 0);
                break;
            case R.id.tv_clean:
                Intent intentclean = new Intent();
                intentclean.setClassName(this, "com.yiyun.rmj.activity.CleanActivity");
                startActivity(intentclean);
                this.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                break;
            case R.id.tv_control:
                Intent intentcontrol = new Intent();
                intentcontrol.setClassName(this, "com.yiyun.rmj.activity.ControlActivity");
                startActivity(intentcontrol);
                this.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;




        }
    }
}
