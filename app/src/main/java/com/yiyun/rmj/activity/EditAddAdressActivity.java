package com.yiyun.rmj.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.lljjcoder.citypickerview.widget.CityPicker;
import com.yiyun.rmj.R;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apiparm.AddressParm;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.StatusBarUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//新增修改地址
public class EditAddAdressActivity extends BaseActivity implements View.OnClickListener{

    public static final int OPTION_TYPE_ADD = 0;  //新增
    public static final int OPTION_TYPE_MODIFY = 1;//修改

    private EditText tv_name, tv_phone_number, tv_address_detail;
    private TextView tv_city;
    private CityPicker cityPicker;
    private AddressParm addressParm;

    private int type; //是修改还是新增

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_add_adress;
    }

    public void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        if(type == OPTION_TYPE_ADD) {
            tv_title.setText(R.string.add_shipping_address);
        }else if(type == OPTION_TYPE_MODIFY){
            tv_title.setText(R.string.modify_shipping_address);
        }
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
        initTitleView();

        tv_name = findViewById(R.id.tv_name);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_address_detail = findViewById(R.id.tv_address_detail);
        tv_city = findViewById(R.id.tv_city);

        addressParm = new AddressParm();
        if(type == OPTION_TYPE_ADD){
            initCityPicker("北京市","北京市","朝阳区");
        } else if(type == OPTION_TYPE_MODIFY){
            addressParm.setAddressId(intent.getIntExtra("addressid",0));
            addressParm.setAddress(intent.getStringExtra("address"));
            addressParm.setProvince(intent.getStringExtra("province"));
            addressParm.setCity(intent.getStringExtra("city"));
            addressParm.setArea(intent.getStringExtra("area"));
            addressParm.setPhone(intent.getStringExtra("phone"));
            addressParm.setName(intent.getStringExtra("name"));

            tv_name.setText(addressParm.getName());
            tv_phone_number.setText(addressParm.getPhone());
            tv_address_detail.setText(addressParm.getAddress());
            tv_city.setText(addressParm.getProvince() + addressParm.getCity() + addressParm.getArea());
            initCityPicker(addressParm.getProvince(), addressParm.getCity(),addressParm.getArea());
        }

        addressParm.setType(type);

        if(type == OPTION_TYPE_ADD){
//            tv_name.setFocusableInTouchMode(true);//可编辑
//            tv_phone_number.setFocusableInTouchMode(true);
//            tv_address_detail.setFocusableInTouchMode(true);

//            tv_name.setText("");
//            tv_phone_number.setText("");
//            tv_address_detail.setText("");
        }else if(type == OPTION_TYPE_MODIFY){
//            tv_name.setFocusableInTouchMode(false);//不可编辑
//            tv_phone_number.setFocusableInTouchMode(false);
//            tv_address_detail.setFocusableInTouchMode(false);
        }

        RelativeLayout rl_city = findViewById(R.id.rl_city);
        rl_city.setOnClickListener(this);

        Button btn_save_info = findViewById(R.id.btn_save_info);
        btn_save_info.setOnClickListener(this);

    }

    public void initCityPicker(String province, String city, String district){
        cityPicker = new CityPicker.Builder(EditAddAdressActivity.this)

                .textSize(14)

                .title("地址选择")

                .titleBackgroundColor("#FFFFFF")

//                .titleTextColor("#696969")

                .confirTextColor("#696969")

                .cancelTextColor("#696969")

                .province(province)

                .city(city)

                .district(district)

                .textColor(Color.parseColor("#000000"))

                .provinceCyclic(true)

                .cityCyclic(false)

                .districtCyclic(false)

                .visibleItemsCount(7)

                .itemPadding(15)

                .onlyShowProvinceAndCity(false)

                .build();

        //监听方法，获取选择结果

        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {

            @Override

            public void onSelected(String... citySelected) {

                //省份

                String province = citySelected[0];

                //城市

                String city = citySelected[1];

                //区县（如果设定了两级联动，那么该项返回空）

                String district = citySelected[2];

                //邮编

                String code = citySelected[3];

                //为TextView赋值
                tv_city.setText(province.trim()+ city.trim()+ district.trim());

                addressParm.setProvince(province);
                addressParm.setCity(city);
                addressParm.setArea(district);

            }

        });


    }

    /**
     * 添加修改地址
     */
    private void addModifyAddress(String desparms){


        api.address(desparms)
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
                        LogUtils.LogE("EditAddressActivity--address:" + obj.getState());
                        showConnectError(obj.getInfo().getMessage());
                        if(obj.getState() == 1){
                            finish();
                        }else if(obj.getState() == -1){
                            startlogin();
                        }

                    }
                });

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                //后退
                finish();
                break;

            case R.id.rl_city:
                //所在城市选择
                cityPicker.show();
                break;

            case R.id.btn_save_info:
                //保存地址信息
                if(checkInput()){
                    addressParm.setName(tv_name.getText().toString().trim());
                    addressParm.setPhone(tv_phone_number.getText().toString().trim());
                    addressParm.setAddress(tv_address_detail.getText().toString().trim());
                    LogUtils.LogE("addressparm:" + gson.toJson(addressParm));
                    String strDes = DESHelper.encrypt(gson.toJson(addressParm));
                    addModifyAddress(strDes);
                }

                break;

        }
    }

    private boolean checkInput(){

        if(TextUtils.isEmpty(tv_name.getText().toString().trim())){
            ToastUtils.show("收货人姓名不能为空");
            return false;
        }

        if(TextUtils.isEmpty(tv_phone_number.getText().toString().trim())){
            ToastUtils.show("手机号码不能为空");
            return false;
        }

        if(TextUtils.isEmpty(tv_city.getText().toString().trim())){
            ToastUtils.show("所在城市不能为空");
            return false;
        }

        if(TextUtils.isEmpty(tv_address_detail.getText().toString().trim())){
            ToastUtils.show("详细地址不能为空");
            return false;
        }

        return true;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
