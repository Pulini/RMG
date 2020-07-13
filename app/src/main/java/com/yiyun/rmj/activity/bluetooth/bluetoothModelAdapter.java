package com.yiyun.rmj.activity.bluetooth;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.bean.BluetoothSettingBean;

import java.util.List;

public class bluetoothModelAdapter  extends CommonRecyclerViewAdapter<BluetoothSettingBean> {
    String[] strStrenth = new String[]{"弱", "中", "强", "MAX"};
    String[] strStrenthNewMode = new String[]{"阅读模式", "电竞模式", "美容模式 ", "MAX"};
    /**
     * 构造函数
     *
     * @param context 上下文
     * @param data    显示的数据
     */
    public bluetoothModelAdapter(Context context, List<BluetoothSettingBean> data) {
        super(context, data);
    }

    @Override
    public void convert(CommonRecyclerViewHolder h, BluetoothSettingBean entity, int position) {
        final TextView tv_remarks = h.getView(R.id.tv_remarks);
        TextView tv_modify = h.getView(R.id.tv_modify);
        TextView tv_strength = h.getView(R.id.tv_strength);
        TextView tv_time = h.getView(R.id.tv_time);
        TextView txt_time = h.getView(R.id.txt_time);
        TextView txt_qd = h.getView(R.id.txt_qd);
        final ImageView iv_leftgraytag = h.getView(R.id.iv_leftgraytag);
        final ImageView iv_leftbluetag = h.getView(R.id.iv_leftbluetag);
        final ImageView iv_leftgraytag_intelligent = h.getView(R.id.iv_leftgraytag_intelligent);
        final ImageView iv_leftbluetag_intelligent = h.getView(R.id.iv_leftbluetag_intelligent);

        if (("智能模式").equals(entity.getRemarks())) {
            tv_modify.setVisibility(View.GONE);
            tv_time.setVisibility(View.GONE);
            iv_leftgraytag.setVisibility(View.GONE);
            iv_leftbluetag.setVisibility(View.GONE);
            iv_leftgraytag_intelligent.setVisibility(View.VISIBLE);
            txt_time.setVisibility(View.GONE);
            tv_strength.setText(strStrenthNewMode[entity.getStrength() - 1]);
        } else {
            tv_modify.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.VISIBLE);
            iv_leftgraytag_intelligent.setVisibility(View.GONE);
            iv_leftbluetag_intelligent.setVisibility(View.GONE);
            txt_time.setVisibility(View.VISIBLE);
            tv_strength.setText(strStrenth[entity.getStrength() - 1]);
        }
    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item_bluetooth_setting;
    }
}
