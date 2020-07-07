package com.yiyun.rmj.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apibase.BaseParm;
import com.yiyun.rmj.bean.apibean.AddressBean;
import com.yiyun.rmj.bean.apiparm.AddressParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.SpfUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import java.util.ArrayList;

import cn.jake.share.frdialog.dialog.FRDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//显示选择地址
public class SelectOrShowAddressActivity extends BaseActivity implements View.OnClickListener{

    public static final int TYPE_SELECT_ADREESS = 1;  //选择地址
    public static final int TYPE_JUST_SHOW_ADREESS = 2; //我的地址
    private RelativeLayout rl_no_one_address;
    private RelativeLayout rl_have_address;
    private int defaultItemPosition = -1; //默认地址的item
    private FRDialog deleteDialog;

    public int type;
    public ArrayList<AddressBean.AddressDetail> addressList;
    private RecyclerView rv_adress;
    private CommonRecyclerViewAdapter adapter;
    private int currentDeletePosition = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_address;
    }

    private void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView iv_title = findViewById(R.id.tv_title);
        iv_title.setVisibility(View.VISIBLE);
//        iv_title.setText(getString(R.string.modify_shipping_address));


        if(type == TYPE_JUST_SHOW_ADREESS){
            iv_title.setText(getString(R.string.my_address));
        }else if(type == TYPE_SELECT_ADREESS){
            iv_title.setText(getString(R.string.select_shipping_address));
        }

    }

    @Override
    protected void initView() {

        addressList = new ArrayList<>();
        type = getIntent().getIntExtra("type",1);
        initTitleView();

        Button btn_no_address_goto_add = findViewById(R.id.btn_no_address_goto_add);
        btn_no_address_goto_add.setOnClickListener(this);

        Button btn_add_address = findViewById(R.id.btn_add_address);
        btn_add_address.setOnClickListener(this);

        rl_no_one_address = findViewById(R.id.rl_no_one_address);
        rl_have_address = findViewById(R.id.rl_have_address);

        deleteDialog =new FRDialog.CommonBuilder(this).setContentView(R.layout.dialog_delete).create();
        TextView dialog_tv_delete = deleteDialog.getView(R.id.tv_sure);
        TextView dialog_tv_cancel = deleteDialog.getView(R.id.tv_cancel);
        dialog_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

        dialog_tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddressParm addressParm = new AddressParm();
                addressParm.setAddressId(addressList.get(currentDeletePosition).getReceiv_address_id());
                String parmDes = DESHelper.encrypt(gson.toJson(addressParm));
                deleteAddress(parmDes);
                deleteDialog.dismiss();
            }
        });

        rv_adress = findViewById(R.id.rv_adress);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_adress.setLayoutManager(manager);
        adapter = new CommonRecyclerViewAdapter<AddressBean.AddressDetail>(this,addressList){

            @Override
            public void convert(CommonRecyclerViewHolder h, final AddressBean.AddressDetail entity, final int position) {

                TextView tv_phone = h.getView(R.id.tv_phone);
                TextView tv_name = h.getView(R.id.tv_name);
                TextView tv_addressDetail = h.getView(R.id.tv_address_detail);

                CheckBox cb_choice =  h.getView(R.id.cb_choice);
                TextView tv_edit = h.getView(R.id.tv_edit);
                TextView tv_delete = h.getView(R.id.tv_delete);

                tv_phone.setText(entity.getPhone());
                tv_name.setText(entity.getConsignee_name());
                tv_addressDetail.setText(entity.getAddressStr());

                if(entity.getIf_default_address() == 1){
                    cb_choice.setChecked(true);
                    defaultItemPosition = position;
                }else{
                    cb_choice.setChecked(false);
                }

                cb_choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            if(entity.getIf_default_address() == 0){
                                addressList.get(position).setIf_default_address(1);
                                AddressParm addressParm = new AddressParm();
                                addressParm.setAddressId(entity.getReceiv_address_id());
                                String parmDes = DESHelper.encrypt(gson.toJson(addressParm));
                                updateDefaultAddress(parmDes);
                            }
                        }
                    }
                });

                tv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent addAddressIntent = new Intent(SelectOrShowAddressActivity.this, EditAddAdressActivity.class);
                        addAddressIntent.putExtra("type",EditAddAdressActivity.OPTION_TYPE_MODIFY);
                        addAddressIntent.putExtra("address", entity.getAddress());
                        addAddressIntent.putExtra("addressstr", entity.getAddressStr());
                        addAddressIntent.putExtra("area", entity.getArea());
                        addAddressIntent.putExtra("city", entity.getCity());
                        addAddressIntent.putExtra("name", entity.getConsignee_name());
                        addAddressIntent.putExtra("phone", entity.getPhone());
                        addAddressIntent.putExtra("province", entity.getProvince());
                        addAddressIntent.putExtra("addressid", entity.getReceiv_address_id());


                        startActivity(addAddressIntent);
                    }
                });

                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentDeletePosition = position;
                        deleteDialog.show();
                    }
                });

            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_address;
            }
        };

        adapter.setOnRecyclerViewItemClickListener(new CommonRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if(type == TYPE_SELECT_ADREESS){
                    AddressBean.AddressDetail addressDetail = addressList.get(position);
                    Intent intent = new Intent();
                    intent.putExtra("addressId",addressDetail.getReceiv_address_id());
                    intent.putExtra("address", addressDetail.getAddress());
                    intent.putExtra("province", addressDetail.getProvince());
                    intent.putExtra("city", addressDetail.getCity());
                    intent.putExtra("phone", addressDetail.getPhone());
                    intent.putExtra("area", addressDetail.getArea());
                    intent.putExtra("name", addressDetail.getConsignee_name());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

        rv_adress.setAdapter(adapter);

        if(addressList.size() == 0){
            rl_no_one_address.setVisibility(View.VISIBLE);
            rl_have_address.setVisibility(View.INVISIBLE);
        }else
        {
            rl_no_one_address.setVisibility(View.INVISIBLE);
            rl_have_address.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                Intent intent = new Intent();
                intent.putExtra("addressId",0);
                intent.putExtra("address", "");
                intent.putExtra("province", "");
                intent.putExtra("city", "");
                intent.putExtra("phone", "");
                intent.putExtra("area", "");
                intent.putExtra("name", "");

                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.btn_no_address_goto_add:
                //地址缺省页添加新地址
                Intent addAddressIntent = new Intent(SelectOrShowAddressActivity.this, EditAddAdressActivity.class);
                addAddressIntent.putExtra("type",EditAddAdressActivity.OPTION_TYPE_ADD);
                startActivity(addAddressIntent);
                break;
            case R.id.btn_add_address:
                Intent addAddressIntent1 = new Intent(SelectOrShowAddressActivity.this, EditAddAdressActivity.class);
                addAddressIntent1.putExtra("type",EditAddAdressActivity.OPTION_TYPE_ADD);
                startActivity(addAddressIntent1);
                break;

        }
    }

    public void updateSavedDefaultAddressId(){
        for(int i=0; i < addressList.size(); i++){
            AddressBean.AddressDetail addressDetail = addressList.get(i);
            if(addressDetail.getIf_default_address() == 1){
                SpfUtils.getSpfUtils(SelectOrShowAddressActivity.this).setDefaultAddressId(addressDetail.getReceiv_address_id());
                return;
            }
        }
        SpfUtils.getSpfUtils(SelectOrShowAddressActivity.this).setDefaultAddressId(-1);
    }

    @Override
    public void onResume() {
        super.onResume();
        String parmDes = DESHelper.encrypt(gson.toJson(new BaseParm()));
        getAddressList(parmDes);
    }

    /**
     * 获取收货地址列表
     * @param desparms
     */
    private void getAddressList(String desparms){


        api.getAddressList(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddressBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(AddressBean obj) {
                        LogUtils.LogE(SelectOrShowAddressActivity.class.getName() + "--defaultAliPayNum:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            addressList.clear();
                            if(obj.getInfo().getData().size() == 0){
                                rl_no_one_address.setVisibility(View.VISIBLE);
                                rl_have_address.setVisibility(View.GONE);
                            }else{
                                rl_no_one_address.setVisibility(View.GONE);
                                rl_have_address.setVisibility(View.VISIBLE);
                                addressList.addAll(obj.getInfo().getData());
                                updateSavedDefaultAddressId();
                            }

                            adapter.notifyDataSetChanged();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

    /**
     * 设置默认收货地址
     * @param desparms
     */
    private void updateDefaultAddress(String desparms){


        api.updateDefaultAddress(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean obj) {
                        LogUtils.LogE(SelectOrShowAddressActivity.class.getName() + "--updateDefaultAddress:" + gson.toJson(obj));

                        if(obj.getState() == 1){
//                            String parmDes = DESHelper.encrypt(gson.toJson(new BaseParm()));
//                            getAddressList(parmDes);

                            if(defaultItemPosition != -1){
                                addressList.get(defaultItemPosition).setIf_default_address(0);
                                adapter.notifyDataSetChanged();
                            }
                            updateSavedDefaultAddressId();

                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }

                    }
                });

    }

    /**
     * 删除收货地址
     * @param desparms
     */
    private void deleteAddress(String desparms){


        api.deleteAddress(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                        LogUtils.LogE("error:" + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseBean obj) {
                        LogUtils.LogE(SelectOrShowAddressActivity.class.getName() + "--deleteAddress:" + gson.toJson(obj));
                        showConnectError(obj.getInfo().getMessage());
                        if(obj.getState() == 1){
                            String parmDes = DESHelper.encrypt(gson.toJson(new BaseParm()));
                            getAddressList(parmDes);
                        }else if(obj.getState() == -1){
                            startlogin();
                        }


                    }
                });

    }



    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
