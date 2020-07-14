package com.yiyun.rmj.activity.bluetooth;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.view.indicatorseekbar.SizeUtils;

import java.util.List;

public class BluetoothModelAdapter extends CommonRecyclerViewAdapter<SettingListModel> {
    String[] strStrenth = new String[]{"弱", "中", "强", "MAX"};

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void OnItemClick(int position);
        void SettingAutoModel();
        void OnItemModifyClick(int position);
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param data    显示的数据
     */
    public BluetoothModelAdapter(Context context, List<SettingListModel> data, OnItemClickListener listener) {
        super(context, data);
        this.listener = listener;
    }

    @Override
    public void convert(CommonRecyclerViewHolder h, SettingListModel data, int position) {
        h.itemView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(context, 80)));
        h.itemView.setOnClickListener(view -> {
            if (position>0) {
                listener.OnItemClick(position);
            }
        });
        TextView tv_remark = h.getView(R.id.tv_remark);
        Switch sw_choice = h.getView(R.id.sw_choice);

        ImageView iv_model = h.getView(R.id.iv_model);
        ImageView iv_auto = h.getView(R.id.iv_auto);
        TextView tv_modify = h.getView(R.id.tv_modify);
        TextView tv_short = h.getView(R.id.tv_short);
        TextView tv_long = h.getView(R.id.tv_long);
        TextView tv_auto = h.getView(R.id.tv_auto);

        tv_modify.setOnClickListener(view -> listener.OnItemModifyClick(position));

        sw_choice.setOnCheckedChangeListener(null);
        sw_choice.setChecked(data.isSelected());
        sw_choice.setOnCheckedChangeListener((compoundButton, b) -> {
            if(position==0&&b){
                listener.SettingAutoModel();
            }
        });

        iv_model.setVisibility(View.GONE);
        iv_auto.setVisibility(View.GONE);
        tv_modify.setVisibility(View.GONE);
        tv_short.setVisibility(View.GONE);
        tv_long.setVisibility(View.GONE);
        tv_auto.setVisibility(View.GONE);

        tv_remark.setText(data.getModelName());


        switch (data.getModel()) {
            case NewBleBluetoothUtil.mode_mild:
                //智能模式 阅读
                iv_auto.setVisibility(View.VISIBLE);
                tv_auto.setVisibility(View.VISIBLE);
                iv_auto.setBackgroundResource(data.isSelected() ? R.mipmap.intelligent_model_select : R.mipmap.intelligent_model_unselect);
                tv_auto.setText("阅读模式");
                break;
            case NewBleBluetoothUtil.mode_middle:
                //智能模式 电竞
                iv_auto.setVisibility(View.VISIBLE);
                tv_auto.setVisibility(View.VISIBLE);
                iv_auto.setBackgroundResource(data.isSelected() ? R.mipmap.intelligent_model_select : R.mipmap.intelligent_model_unselect);
                tv_auto.setText("电竞模式");
                break;
            case NewBleBluetoothUtil.mode_strength:
                //智能模式 美容
                iv_auto.setVisibility(View.VISIBLE);
                tv_auto.setVisibility(View.VISIBLE);
                iv_auto.setBackgroundResource(data.isSelected() ? R.mipmap.intelligent_model_select : R.mipmap.intelligent_model_unselect);
                tv_auto.setText("美容模式");
                break;
            case NewBleBluetoothUtil.mode_short_long:
                //（短+长）模式
                iv_model.setVisibility(View.VISIBLE);
                tv_short.setVisibility(View.VISIBLE);
                tv_long.setVisibility(View.VISIBLE);
                tv_modify.setVisibility(View.VISIBLE);
                iv_model.setBackgroundColor(data.isSelected() ? Color.parseColor("#24F7A8") : Color.parseColor("#dddddd"));
                tv_short.setText("短喷： 强度：" + strStrenth[data.getShortStrength()] + "  时间：" + data.getShortTime() + "秒");
                tv_long.setText("长喷： 强度：" + strStrenth[data.getLongStrength()/2-1] + "  时间：" + data.getLongTime() + "分");
                break;
            case NewBleBluetoothUtil.mode_short:
                //（短）模式
                iv_model.setVisibility(View.VISIBLE);
                tv_short.setVisibility(View.VISIBLE);
                tv_modify.setVisibility(View.VISIBLE);
                iv_model.setBackgroundColor(data.isSelected() ? Color.parseColor("#029DF9") : Color.parseColor("#dddddd"));
                tv_short.setText("短喷： 强度：" + strStrenth[data.getShortStrength()] + "  时间：" + data.getShortTime() + "秒");
                break;
            case NewBleBluetoothUtil.mode_long:
                //（长）模式
                iv_model.setVisibility(View.VISIBLE);
                tv_long.setVisibility(View.VISIBLE);
                tv_modify.setVisibility(View.VISIBLE);
                iv_model.setBackgroundColor(data.isSelected() ? Color.parseColor("#029DF9") : Color.parseColor("#dddddd"));
                tv_long.setText("长喷： 强度：" + strStrenth[data.getLongStrength()/2-1] + "  时间：" + data.getLongTime() + "分");
                break;
        }
    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item_bluetooth_setting2;
    }
}
