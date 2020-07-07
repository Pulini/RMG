package com.yiyun.rmj.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CustomBaseAdapter;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.RecetitionBean;

import java.util.ArrayList;

public class RepetitionActivity extends BaseActivity {

    private Context mContext;
    private ArrayList<RecetitionBean> list_item;
    private ListView list_repetition;
    String str = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_repetition;
    }

    @Override
    protected void initView() {
        mContext = this;
        list_repetition = (ListView) findViewById(R.id.list_repetition);
        ImageView back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                for (int i = 0; i < list_item.size(); i++) {
                    if (list_item.get(i).isOnclick()) {
                        str += list_item.get(i).getText().substring(1);
                        str += ",";
                    }
                }
                if (str.equals("")) {
                    str = "从不";
                } else {
                    Log.e("syq", str);
                    str = str.substring(0, str.length() - 1);
                }
                intent.putExtra("reception", str);
                setResult(101, intent);
                finish();
//                Intent intent = new Intent();
//                intent.setClassName(mContext, "com.yiyun.rmj.activity.LabelActivity");
//                mContext.startActivity(intent);
//                RepetitionActivity.this.overridePendingTransition(R.anim.activity_open, 0);
            }
        });

        list_item = new ArrayList<>();
        list_item.add(new RecetitionBean("每周日"));
        list_item.add(new RecetitionBean("每周一"));
        list_item.add(new RecetitionBean("每周二"));
        list_item.add(new RecetitionBean("每周三"));
        list_item.add(new RecetitionBean("每周四"));
        list_item.add(new RecetitionBean("每周五"));
        list_item.add(new RecetitionBean("每周六"));
        CustomBaseAdapter<RecetitionBean> adapter = new CustomBaseAdapter<RecetitionBean>(list_item, R.layout.itme_recepetition) {

            @Override
            public void bindView(ViewHolder holder, RecetitionBean obj, final int position) {
                TextView tv_text = holder.getView(R.id.tv_text);
                final RelativeLayout rll_main = holder.getView(R.id.rll_main);
                final ImageView iv_right = holder.getView(R.id.iv_right);
                tv_text.setText(obj.getText());
                rll_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!list_item.get(position).isOnclick()) {
                            list_item.get(position).setOnclick(true);
                            Log.e("syq", position + "");
                            rll_main.setBackgroundColor(mContext.getResources().getColor(R.color.colora8));
                            iv_right.setVisibility(View.VISIBLE);

                        } else {
                            list_item.get(position).setOnclick(false);
                            rll_main.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                            iv_right.setVisibility(View.GONE);
                        }
                    }
                });


            }
        };
        list_repetition.setAdapter(adapter);

    }

    @Override
    protected void initData() {

    }
}
