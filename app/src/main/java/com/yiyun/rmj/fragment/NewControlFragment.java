package com.yiyun.rmj.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.yiyun.rmj.R;
import com.yiyun.rmj.activity.bluetooth.BluetoothControlDetailActivity;
import com.yiyun.rmj.activity.bluetooth.BluetoothMainActivity;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.base.BaseFragment;
import com.yiyun.rmj.bean.BluetoothSettingBean;
import com.yiyun.rmj.view.MyNewArcProgressBar;
import com.yiyun.rmj.view.WheelView;

import java.util.ArrayList;

public class NewControlFragment extends BaseFragment {

    WheelView wheelView;
    MyNewArcProgressBar progressBar;
    ArrayList<String> list;
    int lastindex = 0;
    private static String timeUnit = "\t秒";
    BluetoothControlDetailActivity activity;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_control_new;
    }

    @Override
    protected void initView(View view) {
        progressBar = (MyNewArcProgressBar) view.findViewById(R.id.progressBar);
        activity = (BluetoothControlDetailActivity)getActivity();

        wheelView = (WheelView) view.findViewById(R.id.wheel);
        list = new ArrayList<>();
        for (int i = 5; i < 61; i++) {
            if(i%5 == 0) {
                list.add(i + timeUnit);
            }
        }
        wheelView.lists(list).select(lastindex).showCount(5)
                .listener(new WheelView.OnWheelViewItemSelectListener() {
                    @Override
                    public void onItemSelect(int index) {
                        lastindex = index;
                    }
                });


        Button btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                BluetoothSettingBean bean = activity.getSettingBean();
                bean.setStrength(progressBar.getCurrentStep() + 1);
                bean.setTime(getLasttime(lastindex));
                Intent intent = new Intent();
                intent.putExtra("setting",bean);
                activity.setResult(BaseActivity.RESULT_OK,intent);
                activity.setIsFinishActivityFlag(true);
                activity.finish();
                activity.overridePendingTransition(R.anim.activity_leftclick_in,R.anim.activity_leftclick_out);
            }
        });

        if(activity.getType() == BluetoothMainActivity.TYPE_MODIFY){
            BluetoothSettingBean settingBean = activity.getSettingBean();
            lastindex = list.indexOf(settingBean.getTime() + timeUnit);
            wheelView.select(lastindex);
            progressBar.setCurrentStep(settingBean.getStrength() - 1);
        }

    }

    private int getLasttime(int index){
        String str = list.get(index);
        String str2 = str.substring(0,str.length()-1).trim();
        return Integer.parseInt(str2);
    }

    @Override
    protected void initData() {

    }
}
