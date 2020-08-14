package com.yiyun.rmj.activity.bluetooth;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.HomeAdapter;
import com.yiyun.rmj.bluetooth.NewBleBluetoothUtil;
import com.yiyun.rmj.view.indicatorseekbar.SizeUtils;

import java.util.List;


public class BluetoothModelAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<SettingListModel> list;
    String[] strStrenth = new String[]{"弱", "中", "强", "MAX"};

    private OnItemClickListener listener;

    public BluetoothModelAdapter2(Context context, List<SettingListModel> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);

        void SettingAutoModel(boolean isChecked);

        void SelectModel(int position, boolean isChecked);

        void OnItemModifyClick(int position);
    }

    public static final int TYPE1 = 0;
    public static final int TYPE2 = 1;

    public int getItemViewType(int position) {
        if (list.get(position).getModel() == NewBleBluetoothUtil.modeNull) {
            return TYPE2;
        }
        return TYPE1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE2) {
            return new Holder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_setting_null, parent, false));
        }
        return new Holder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_setting2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof Holder1){
            setHolder1((Holder1) holder,position);
        }else{
            holder.itemView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(context, 15)));
        }
    }



    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    private void setHolder1(Holder1 holder, int position) {
        holder.itemView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(context, 80)));
        holder.itemView.setOnClickListener(view -> {
//            if (position > 0) {
                listener.OnItemClick(holder.getAdapterPosition());
//            }
        });

        holder.tv_modify.setOnClickListener(view -> listener.OnItemModifyClick(position));


        holder.iv_model.setVisibility(View.GONE);
        holder.iv_auto.setVisibility(View.GONE);
        holder.tv_modify.setVisibility(View.GONE);
        holder.tv_short.setVisibility(View.GONE);
        holder.tv_long.setVisibility(View.GONE);
        holder.tv_auto.setVisibility(View.GONE);

        holder.tv_remark.setText(list.get(position).getModelName());

        holder.sw_choice.setTrackResource(R.drawable.switch_custom_track_selector);
        switch (list.get(position).getModel()) {
            case NewBleBluetoothUtil.mode_auto_mild:
                //智能模式 阅读
                holder.iv_auto.setVisibility(View.VISIBLE);
                holder.tv_auto.setVisibility(View.VISIBLE);
//                holder.iv_auto.setBackgroundResource(list.get(position).isSelected() ? R.mipmap.intelligent_model_select : R.mipmap.intelligent_model_unselect);
                holder.iv_auto.setBackgroundResource(R.mipmap.intelligent_model_select);
                holder.tv_auto.setText("阅读模式");
                break;
            case NewBleBluetoothUtil.mode_auto_middle:
                //智能模式 电竞
                holder.iv_auto.setVisibility(View.VISIBLE);
                holder.tv_auto.setVisibility(View.VISIBLE);
//                holder.iv_auto.setBackgroundResource(list.get(position).isSelected() ? R.mipmap.intelligent_model_select : R.mipmap.intelligent_model_unselect);
                holder.iv_auto.setBackgroundResource(R.mipmap.intelligent_model_select);
                holder.tv_auto.setText("电竞模式");
                break;
            case NewBleBluetoothUtil.mode_auto_strength:
                //智能模式 美容
                holder.iv_auto.setVisibility(View.VISIBLE);
                holder.tv_auto.setVisibility(View.VISIBLE);
//                holder.iv_auto.setBackgroundResource(list.get(position).isSelected() ? R.mipmap.intelligent_model_select : R.mipmap.intelligent_model_unselect);
                holder.iv_auto.setBackgroundResource(R.mipmap.intelligent_model_select);
               holder.tv_auto.setText("美容模式");
                break;
/*            case NewBleBluetoothUtil.mode_short_long:
                //（短+长）模式
                holder.iv_model.setVisibility(View.VISIBLE);
                holder.tv_short.setVisibility(View.VISIBLE);
                holder.tv_long.setVisibility(View.VISIBLE);
                holder.tv_modify.setVisibility(View.VISIBLE);
//                holder.iv_model.setBackgroundColor(list.get(position).isSelected() ? Color.parseColor("#24F7A8") : Color.parseColor("#dddddd"));
                holder.iv_model.setBackgroundColor(Color.parseColor("#24F7A8"));
                holder.tv_short.setText("短喷： 强度：" + strStrenth[list.get(position).getShortStrength() - 1] + "  时间：" + list.get(position).getShortTime() + "秒");
                holder.tv_long.setText("长喷： 强度：" + strStrenth[list.get(position).getLongStrength() / 2 - 1] + "  时间：" + list.get(position).getLongTime() + "分");
                holder.sw_choice.setTrackResource(R.drawable.switch_track_green_selector);
                break;*/
            case NewBleBluetoothUtil.mode_short:
                //（短）模式
                holder.iv_model.setVisibility(View.VISIBLE);
                holder.tv_short.setVisibility(View.VISIBLE);
                holder.tv_modify.setVisibility(View.VISIBLE);
//                holder.iv_model.setBackgroundColor(list.get(position).isSelected() ? Color.parseColor("#029DF9") : Color.parseColor("#dddddd"));
//                holder.iv_model.setBackgroundColor(Color.parseColor("#FFC81E"));
                holder.iv_model.setBackgroundColor(Color.parseColor("#F98802"));
                holder.tv_short.setText("短喷： 强度：" + strStrenth[list.get(position).getShortStrength() - 1] + "  时间：" + list.get(position).getShortTime() + "秒");
                holder.sw_choice.setTrackResource(R.drawable.switch_track_yellow_selector);
                break;
            case NewBleBluetoothUtil.mode_long:
                //（长）模式
                holder.iv_model.setVisibility(View.VISIBLE);
                holder.tv_long.setVisibility(View.VISIBLE);
                holder.tv_modify.setVisibility(View.VISIBLE);
//                holder.iv_model.setBackgroundColor(list.get(position).isSelected() ? Color.parseColor("#24F7A8") : Color.parseColor("#dddddd"));
                holder.iv_model.setBackgroundColor(Color.parseColor("#24F7A8"));
                holder.tv_long.setText("长喷： 强度：" + strStrenth[list.get(position).getLongStrength() / 2 - 1] + "  时间：" + list.get(position).getLongTime() + "分");
                holder.sw_choice.setTrackResource(R.drawable.switch_track_green_selector);
                break;
        }
        holder.sw_choice.setOnCheckedChangeListener(null);
        holder.sw_choice.setChecked(list.get(position).isSelected());
        holder.sw_choice.setOnCheckedChangeListener((compoundButton, b) -> {
            if (position == 0) {
                listener.SettingAutoModel(b);
            } else {
                listener.SelectModel(holder.getAdapterPosition(), b);
            }
//            holder.iv_auto.setBackgroundResource(list.get(position).isSelected() ? R.mipmap.intelligent_model_select : R.mipmap.intelligent_model_unselect);
            holder.iv_model.setBackgroundColor(
                    list.get(position).isSelected() ?
                            NewBleBluetoothUtil.mode_short_long == list.get(position).getModel() ?
                                    Color.parseColor("#24F7A8") : Color.parseColor("#029DF9")
                            : Color.parseColor("#dddddd")
            );
        });
    }

    public class Holder1 extends RecyclerView.ViewHolder {
        ImageView iv_model;
        ImageView iv_auto;
        TextView tv_remark;
        TextView tv_modify;
        TextView tv_short;
        TextView tv_long;
        TextView tv_auto;
        Switch sw_choice;

        public Holder1(View itemView) {
            super(itemView);
            iv_model = itemView.findViewById(R.id.iv_model);
            iv_auto = itemView.findViewById(R.id.iv_auto);
            tv_remark = itemView.findViewById(R.id.tv_remark);
            tv_modify = itemView.findViewById(R.id.tv_modify);
            tv_short = itemView.findViewById(R.id.tv_short);
            tv_long = itemView.findViewById(R.id.tv_long);
            tv_auto = itemView.findViewById(R.id.tv_auto);
            sw_choice = itemView.findViewById(R.id.sw_choice);
        }
    }

    public class Holder2 extends RecyclerView.ViewHolder {

        public Holder2(View itemView) {
            super(itemView);
        }
    }

}
