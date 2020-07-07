package com.yiyun.rmj.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.CommonRecyclerViewAdapter;
import com.yiyun.rmj.adapter.CommonRecyclerViewHolder;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.base.MyApplication;
import com.yiyun.rmj.bean.AddressInfo;
import com.yiyun.rmj.bean.ProductInfo;
import com.yiyun.rmj.bean.ProductSampleInfo;
import com.yiyun.rmj.bean.apibean.BeforePayBean;
import com.yiyun.rmj.bean.apibean.CreateOrderBean;
import com.yiyun.rmj.bean.apiparm.PayParm;
import com.yiyun.rmj.bean.apiparm.SettlementParm;
import com.yiyun.rmj.setting.UrlContact;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.LogUtils;
import com.yiyun.rmj.utils.PayResult;
import com.yiyun.rmj.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//结算界面-确认订单
public class SettlementActivity extends BaseActivity implements View.OnClickListener{


    private SettlementParm parm;
    private TextView tv_province,tv_city,tv_address,tv_name ,tv_phone_number,tv_kd, tv_user_total_integral,tv_less_money, tv_product_num, tv_count_product_price, tv_total_num,money_num;
    private CheckBox cb_choice_use_integral, cb_choice_paytype_weixin,cb_choice_paytype_zhifubao;
    private EditText et_ly,et_need_integral;
    private int integralRatio;//积分换算比例
    private BeforePayBean.Data mData;
    private int useintegralNum = 0;
    private ArrayList<ProductInfo> productList;
    private CommonRecyclerViewAdapter adapter;
    private AddressInfo defaultAddress;
    private IWXAPI wxapi;
    private boolean isFirstStart;
    private int paytype = -2;
    private static final int SDK_PAY_FLAG = 6666;
    private int orderId;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.e("bcz","resultStatus:" + resultStatus);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Intent intentPaySuccess = new Intent(SettlementActivity.this, PayResultActivity.class);
                        intentPaySuccess.putExtra("payresult",0);
                        intentPaySuccess.putExtra("orderId",orderId);
                        startActivity(intentPaySuccess);
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Intent intentPaySuccess = new Intent(SettlementActivity.this, PayResultActivity.class);
                        intentPaySuccess.putExtra("payresult",-1);
                        intentPaySuccess.putExtra("orderId",orderId);
                        startActivity(intentPaySuccess);
                        finish();

                    }
                    break;
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settlement;
    }

    @Override
    protected void initView() {
        initTitleView();
        isFirstStart = true;
        parm = new SettlementParm();
        productList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        String favoriteIdsStr = bundle.getString("favoriteIdsStr");
//        wxapi = WXAPIFactory.createWXAPI(this, UrlContact.WXAPPID, false);

        wxapi = WXAPIFactory.createWXAPI(this,null);

        // 将该app注册到微信
        wxapi.registerApp(UrlContact.WXAPPID);

        if(!TextUtils.isEmpty(favoriteIdsStr)){
            parm.setFavoriteIdsStr(favoriteIdsStr);
        }
        parm.setType(bundle.getInt("type",1));
        parm.setProductId(bundle.getInt("productId",0));
        parm.setNum(bundle.getInt("num",0));
        orderId = bundle.getInt("orderId",0);
        parm.setOrderId(orderId);

        LinearLayout ll_address = findViewById(R.id.ll_address);
        ll_address.setOnClickListener(this);

        tv_province = findViewById(R.id.tv_province);
        tv_city = findViewById(R.id.tv_city);
        tv_address = findViewById(R.id.tv_address);
        tv_name = findViewById(R.id.tv_name);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_kd = findViewById(R.id.tv_kd);
        et_need_integral = findViewById(R.id.et_need_integral);
        tv_user_total_integral = findViewById(R.id.tv_user_total_integral);
        tv_less_money = findViewById(R.id.tv_less_money);
        tv_total_num = findViewById(R.id.tv_total_num);
        money_num = findViewById(R.id.money_num);

        cb_choice_use_integral = findViewById(R.id.cb_choice_use_integral);
        Button btn_commit = findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);

        et_ly = findViewById(R.id.et_ly);
        tv_product_num = findViewById(R.id.tv_product_num);
        tv_count_product_price = findViewById(R.id.tv_count_product_price);
        cb_choice_paytype_weixin = findViewById(R.id.cb_choice_paytype_weixin);
        cb_choice_paytype_zhifubao = findViewById(R.id.cb_choice_paytype_zhifubao);

        cb_choice_paytype_weixin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cb_choice_paytype_zhifubao.setClickable(true);
                    cb_choice_paytype_zhifubao.setChecked(false);
                    cb_choice_paytype_weixin.setClickable(false);
                }
            }
        });

        cb_choice_paytype_zhifubao.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cb_choice_paytype_zhifubao.setClickable(false);
                    cb_choice_paytype_weixin.setChecked(false);
                    cb_choice_paytype_weixin.setClickable(true);
                }
            }
        });

        cb_choice_use_integral.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if( useintegralNum == 0 ){
                        ToastUtils.show("请先输入使用积分");
                        cb_choice_use_integral.toggle();
                    }else{
                        refreshBottomView();
                    }
                }else{
                    refreshBottomView();
                }
            }
        });

        et_need_integral.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();





            }

            @Override
            public void afterTextChanged(Editable editable) {
                String temp = editable.toString();
                int intger = 0;
                try{
                    intger = Integer.parseInt(temp);
                }catch (NumberFormatException ex){
                }
                useintegralNum = intger;

                if(integralRatio != 0){
                    tv_less_money.setText("-" + ((double)useintegralNum)/integralRatio);
                }


                int posDot = temp.indexOf(".");
                if (posDot > 0){
                    //保留小数点后两位
                    if (temp.length() - posDot - 1 > 2) {
                        editable.delete(posDot + 3, posDot + 4);
                    }
                }

                //输入的金额超出最大还款金额则设置为最大还款金额
                if(!TextUtils.isEmpty(temp)){
                    if(temp.equals(".")){
                        temp = "0.";
                        et_need_integral.setText(temp);
                        et_need_integral.setSelection(temp.length());
                    }
                    double tempDouble = Double.parseDouble(temp);
                    if(tempDouble > mData.getIntegral()){
                        et_need_integral.setText("" + mData.getIntegral());
                        et_need_integral.setSelection(("" + mData.getIntegral()).length());
                    }
                }
            }
        });

        RecyclerView rv_product = findViewById(R.id.rv_product);
        LinearLayoutManager manager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_product.setLayoutManager(manager);

        adapter = new CommonRecyclerViewAdapter<ProductInfo>(this,productList) {
            @Override
            public void convert(CommonRecyclerViewHolder h, ProductInfo entity, int position) {
                ImageView imageView = h.getView(R.id.iv_product_image);
                TextView tv_product_name = h.getView(R.id.tv_product_name);
                TextView tv_price = h.getView(R.id.tv_price);
                TextView tv_product_num = h.getView(R.id.tv_product_num);

                tv_product_name.setText(entity.getProduct_name());
                tv_price.setText("" + entity.getProduct_price());
                tv_product_num.setText("x" + entity.getNum());
                Glide.with(SettlementActivity.this).load(entity.getProduct_picture().get(0).getUrl()).apply(new RequestOptions()).into(imageView);

            }

            @Override
            public int getLayoutViewId(int viewType) {
                return R.layout.item_order_productinfo;
            }

        };

        rv_product.setAdapter(adapter);


    }

    private void initTitleView(){
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        TextView iv_title = (TextView) findViewById(R.id.tv_title);
        iv_title.setVisibility(View.VISIBLE);
        iv_title.setText(getString(R.string.confirm_order));

    }

    @Override
    protected void initData() {
        String strDes = DESHelper.encrypt(gson.toJson(parm));
        LogUtils.LogE("initData---beforeCreatOrder:" + strDes);
        beforeCreatOrder(strDes);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_address:
                //进入地址选择界面
                Intent selectAddressIntent = new Intent(SettlementActivity.this, SelectOrShowAddressActivity.class);
                startActivityForResult(selectAddressIntent,101);

                break;

            case R.id.btn_commit:
                if(TextUtils.isEmpty(tv_address.getText().toString().trim())){
                    ToastUtils.show("请先选择收货地址");
                    return;
                }

                if(!cb_choice_paytype_weixin.isChecked() && !cb_choice_paytype_zhifubao.isChecked()){
                    ToastUtils.show("请先选择支付方式");
                    return;
                }

                PayParm payParm = new PayParm();
                payParm.setOrderId(orderId);
                payParm.setAddressId(defaultAddress.getReceiv_address_id());
                payParm.setDeduction(useintegralNum);
                payParm.setMessage(et_ly.getText().toString().trim());
                paytype = 0;
                if(cb_choice_paytype_weixin.isChecked()){
                    paytype = 1;
                }
                payParm.setPayType(paytype);
                payParm.setType(parm.getType());
                ArrayList<ProductSampleInfo> list = new ArrayList<>();

                for(ProductInfo info:mData.getProductItem()){
                    ProductSampleInfo sampleInfo = new ProductSampleInfo();
                    sampleInfo.setNum(info.getNum());
                    sampleInfo.setProductId(info.getProduct_id());
                    list.add(sampleInfo);
                }
                payParm.setProductJson(list);
                String strDes = DESHelper.encrypt(gson.toJson(payParm));
                LogUtils.LogE("payPam:" + strDes);
                creatOrder(strDes);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 101) {
            //选择地址返回

            int addressId =data.getIntExtra("addressId",0);
            String address = data.getStringExtra("address");
            String province = data.getStringExtra("province");
            String city = data.getStringExtra("city");
            String phone = data.getStringExtra("phone");
            String area = data.getStringExtra("area");
            String name = data.getStringExtra("name");

            tv_province.setText(province);
            tv_city.setText(city);
            tv_address.setText(area + address);
            tv_name.setText(name);
            tv_phone_number.setText(phone);

            defaultAddress.setAddress(address);
            defaultAddress.setArea(area);
            defaultAddress.setCity(city);
            defaultAddress.setConsignee_name(name);
            defaultAddress.setPhone(phone);
            defaultAddress.setProvince(province);
            defaultAddress.setReceiv_address_id(addressId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MyApplication.wxpayResltCode == 0){
            Intent intentPaySuccess = new Intent(SettlementActivity.this, PayResultActivity.class);
            intentPaySuccess.putExtra("payresult",MyApplication.wxpayResltCode);
            intentPaySuccess.putExtra("orderId",orderId);
            startActivity(intentPaySuccess);
            setResult(RESULT_OK);
            finish();
        }else if(MyApplication.wxpayResltCode == -1){

            Intent intentPaySuccess = new Intent(SettlementActivity.this, PayResultActivity.class);
            intentPaySuccess.putExtra("payresult",MyApplication.wxpayResltCode);
            intentPaySuccess.putExtra("orderId",orderId);
            startActivity(intentPaySuccess);
            finish();
        }else if(MyApplication.wxpayResltCode == -2){

        }
        MyApplication.wxpayResltCode = 999;
    }

    /**
     * 创建订单前的操作
     * @param desparms
     */
    public void beforeCreatOrder(String desparms) {
        api.beforeCreatOrder(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BeforePayBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(BeforePayBean obj) {
                        LogUtils.LogE("beforeCreatOrder:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            refreshData(obj.getInfo().getData());
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }
                    }
                });

    }

    /**
     * 收藏
     * @param desparms
     */
    public void creatOrder(String desparms) {
        api.creatOrder(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrderBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(CreateOrderBean obj) {

                        LogUtils.LogE("creatOrder:" + gson.toJson(obj));
                        if(obj.getState() == 1){
                            orderId = obj.getInfo().getData().getOrderId();
                            if(paytype == 0){
                                aliPay(obj.getInfo());
                            }else if(paytype == 1){
                                wxPay(obj.getInfo());
                            }
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }
                    }
                });

    }

    private void wxPay(CreateOrderBean.Info info){
        PayReq request = new PayReq();
        request.appId = info.getData().getMchPayApp().getAppid();
        request.partnerId = info.getData().getMchPayApp().getPartnerid();
        request.prepayId= info.getData().getMchPayApp().getPrepayid();
        request.packageValue = info.getData().getMchPayApp().getPackage_();
        request.nonceStr= info.getData().getMchPayApp().getNoncestr();
        request.timeStamp= info.getData().getMchPayApp().getTimestamp();
        request.sign= info.getData().getMchPayApp().getSign();
        wxapi.sendReq(request);
    }

    private void aliPay(CreateOrderBean.Info info){
        final String orderInfo = info.getData().getOrderString();   // 订单信息

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(SettlementActivity.this);
                Map<String,String> result = alipay.payV2(orderInfo,true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
//        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    public void refreshData(BeforePayBean.Data data){
        mData = data;
        defaultAddress = data.getDefaultAddress();
        productList.addAll(data.getProductItem());
        adapter.notifyDataSetChanged();
        tv_kd.setText("快递 ￥" + data.getExpressMoney());
        int integral = data.getIntegral();
        tv_user_total_integral.setText("（可用" + integral + "积分）");
        tv_less_money.setText("-0.00");
        tv_product_num.setText("共计" + data.getAllNum() + "件商品");
        tv_count_product_price.setText("" + data.getAllPrice());

        tv_total_num.setText("共" + data.getAllNum() + "件");
        money_num.setText("" + (data.getAllPrice() + data.getExpressMoney()));
        integralRatio = data.getIntegralRatio();

        if(defaultAddress == null){
            defaultAddress = new AddressInfo();
            tv_province.setText("");
            tv_city.setText("");
            tv_address.setText("");
            tv_name.setText("");
            tv_phone_number.setText("");
        }else{
            tv_province.setText(defaultAddress.getProvince());
            tv_city.setText(defaultAddress.getCity());
            tv_address.setText(defaultAddress.getArea() + defaultAddress.getAddress());
            tv_name.setText(defaultAddress.getConsignee_name());
            tv_phone_number.setText(defaultAddress.getPhone());
        }

    }

    private void refreshBottomView(){
        if(cb_choice_use_integral.isChecked()){
            double less = 0;
            if(integralRatio != 0 && useintegralNum != 0){
                less = ((double)useintegralNum)/integralRatio;
            }
            money_num.setText("" + (mData.getAllPrice() + mData.getExpressMoney() - less));
        }else{
            money_num.setText("" + mData.getAllPrice() + mData.getExpressMoney());
        }

    }





    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
