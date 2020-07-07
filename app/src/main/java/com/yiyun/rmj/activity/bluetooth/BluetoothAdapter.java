package com.yiyun.rmj.activity.bluetooth;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.bean.BluetoothSettingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * File Name : BluetoothAdapter
 * Created by : PanZX on 2020/05/27
 * Email : 644173944@qq.com
 * Github : https://github.com/Pulini
 * Remark：
 */
public class BluetoothAdapter extends CommonRecyclerViewAdapter<BluetoothSettingBean> {
    String[] strStrenth = new String[]{"弱", "中", "强", "MAX"};
    String[] strStrenthNewMode = new String[]{"阅读模式", "电竞模式", "美容模式 ", "MAX"};

    ArrayList<BluetoothSettingBean> list;
    Context context;
    BluetoothAdapterClickListener listener;

    public interface BluetoothAdapterClickListener {
        void itemModify(BluetoothSettingBean data);
        void itemSelect(BluetoothSettingBean data1, boolean isChecked, BluetoothSettingBean data2);
    }

    public void setListener(BluetoothAdapterClickListener listener) {
        this.listener = listener;
    }

    /**
     * 构造函数
     *
     * @param context 上下文
     * @param data    显示的数据
     */
    public BluetoothAdapter(Context context, List data) {
        super(context, data);
        this.context = context;
        this.list = (ArrayList<BluetoothSettingBean>) data;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private int getCurrentSelectPosition() {
        for (int i = 0; i < list.size(); i++) {
            BluetoothSettingBean beann = list.get(i);
            if (beann.isSelected()) {
                return i;
            }
        }
        return -999;
    }

    public void setData(List data) {
        this.list = (ArrayList<BluetoothSettingBean>) data;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void convert(final CommonRecyclerViewHolder h, final BluetoothSettingBean entity, final int position) {
        if (("智能模式").equals(entity.getRemarks())) {
            setItem1(h, entity, position);
        } else {
            setItem2(h, entity, position);
        }

        Switch sw_choice = h.getView(R.id.sw_choice);
        sw_choice.setChecked(entity.isSelected());
        sw_choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              int index=  getCurrentSelectPosition();
                if(index!=-999){
                    listener.itemSelect(entity,isChecked,list.get(index));
                }else{
                    listener.itemSelect(entity,isChecked,null);
                }
            }
        });

//        sw_choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                int currentSelectPosition = getCurrentSelectPosition();
//                if (b) {
//                    if (currentSelectPosition != -2 && currentSelectPosition != position) {
//                        listData.get(currentSelectPosition).setSelected(false);
//                        listData.get(currentSelectPosition).save();
//                        if (currentSelectPosition == 0) {
//                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_normal, 0);
//                        }
//                    }
//                    for (int i = 0; i < listData.size(); i++) {
//                        BluetoothSettingBean bluetoothSettingBean = listData.get(i);
//                        if (i == position) {
//                            bluetoothSettingBean.setSelected(true);
//                            bluetoothSettingBean.save();
//                            bluetoothSettingBean.setDeviceId(-2);
//                        } else {
//                            bluetoothSettingBean.setSelected(false);
//                            bluetoothSettingBean.save();
//                            bluetoothSettingBean.setDeviceId(-1);
//                        }
//                    }
//                    if (("智能模式").equals(entity.getRemarks())) {
//                        iv_leftgraytag.setVisibility(View.GONE);
//                        iv_leftbluetag.setVisibility(View.GONE);
//                        iv_leftgraytag_intelligent.setVisibility(View.GONE);
//                        iv_leftbluetag_intelligent.setVisibility(View.VISIBLE);
//                        dialog.show();
//                    } else {
//                        iv_leftgraytag_intelligent.setVisibility(View.GONE);
//                        iv_leftbluetag_intelligent.setVisibility(View.GONE);
//                        iv_leftgraytag.setVisibility(View.GONE);
//                        iv_leftbluetag.setVisibility(View.VISIBLE);
//                        //发送操作指令
////                            startExcute(position);
//                    }
//                    startExcute(position);
//                    Message message = new Message();
//                    message.what = 2;
//                    message.arg1 = currentSelectPosition;
//                    handler.sendMessage(message);
//                } else {
//                    if (getCurrentSelectPosition() == position) {
//
//                        if (("智能模式").equals(entity.getRemarks())) {
//                            iv_leftgraytag.setVisibility(View.GONE);
//                            iv_leftbluetag.setVisibility(View.GONE);
//                            iv_leftgraytag_intelligent.setVisibility(View.VISIBLE);
//                            iv_leftbluetag_intelligent.setVisibility(View.GONE);
//                            //设置普通模式
//                            bluetoothUtil.addOrderToQuee(NewBleBluetoothUtil.mode_normal, 0);
////                                readStatusToQuee();
//                            bluetoothUtil.sendOrder();
//                        } else {
//                            iv_leftgraytag_intelligent.setVisibility(View.GONE);
//                            iv_leftbluetag_intelligent.setVisibility(View.GONE);
//                            iv_leftgraytag.setVisibility(View.VISIBLE);
//                            iv_leftbluetag.setVisibility(View.GONE);
//                        }
//                        entity.setSelected(false);
//                        entity.save();
//                    }
//                }
//
//
//            }
//        });


    }

    private void setItem1(final CommonRecyclerViewHolder h, final BluetoothSettingBean entity, final int position) {
        ImageView iv_leftbluetag = h.getView(R.id.iv_leftbluetag);
        ImageView iv_leftgraytag = h.getView(R.id.iv_leftgraytag);
        ImageView iv_leftgraytag_intelligent = h.getView(R.id.iv_leftgraytag_intelligent);
        ImageView iv_leftbluetag_intelligent = h.getView(R.id.iv_leftbluetag_intelligent);
        TextView tv_remarks = h.getView(R.id.tv_remarks);
        TextView tv_modify = h.getView(R.id.tv_modify);
        TextView tv_strength = h.getView(R.id.tv_strength);
        TextView txt_time = h.getView(R.id.txt_time);
        TextView tv_time = h.getView(R.id.tv_time);

        tv_modify.setVisibility(View.GONE);
        tv_time.setVisibility(View.GONE);
        iv_leftgraytag.setVisibility(View.GONE);
        iv_leftbluetag.setVisibility(View.GONE);
        iv_leftgraytag_intelligent.setVisibility(View.VISIBLE);
        txt_time.setVisibility(View.GONE);

        tv_remarks.setText(entity.getRemarks());
        tv_strength.setText(strStrenthNewMode[entity.getStrength() - 1]);

        if (entity.isSelected()) {
            iv_leftgraytag_intelligent.setVisibility(View.GONE);
            iv_leftbluetag_intelligent.setVisibility(View.VISIBLE);
        } else {
            iv_leftgraytag_intelligent.setVisibility(View.VISIBLE);
            iv_leftbluetag_intelligent.setVisibility(View.GONE);
        }
    }

    private void setItem2(final CommonRecyclerViewHolder h, final BluetoothSettingBean entity, final int position) {
        ImageView iv_leftbluetag = h.getView(R.id.iv_leftbluetag);
        ImageView iv_leftgraytag = h.getView(R.id.iv_leftgraytag);
        ImageView iv_leftgraytag_intelligent = h.getView(R.id.iv_leftgraytag_intelligent);
        ImageView iv_leftbluetag_intelligent = h.getView(R.id.iv_leftbluetag_intelligent);
        TextView tv_remarks = h.getView(R.id.tv_remarks);
        TextView tv_modify = h.getView(R.id.tv_modify);
        TextView tv_strength = h.getView(R.id.tv_strength);
        TextView txt_time = h.getView(R.id.txt_time);
        TextView tv_time = h.getView(R.id.tv_time);

        tv_modify.setVisibility(View.VISIBLE);
        tv_time.setVisibility(View.VISIBLE);
        iv_leftgraytag_intelligent.setVisibility(View.GONE);
        iv_leftbluetag_intelligent.setVisibility(View.GONE);
        txt_time.setVisibility(View.VISIBLE);
        tv_strength.setText(strStrenth[entity.getStrength() - 1]);

        if (entity.isSelected()) {
            iv_leftgraytag.setVisibility(View.GONE);
            iv_leftbluetag.setVisibility(View.VISIBLE);
        } else {
            iv_leftgraytag.setVisibility(View.VISIBLE);
            iv_leftbluetag.setVisibility(View.GONE);
        }

        tv_time.setText(entity.getTime() + "秒");
        String remark = entity.getRemarks();
        if (remark == null || remark.isEmpty()) {
            entity.setRemarks("设置");
            entity.save();
        }
        tv_remarks.setText(entity.getRemarks());

        if (listener != null) {
            tv_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemModify(entity);
                }
            });
        }
    }

    @Override
    public int getLayoutViewId(int viewType) {
        return R.layout.item_bluetooth_setting;
    }

}