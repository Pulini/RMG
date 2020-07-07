package com.yiyun.rmj.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yiyun.rmj.R;
import com.yiyun.rmj.adapter.DragDetailFragmentPagerAdapter;
import com.yiyun.rmj.base.BaseActivity;
import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apibean.ProductDetailBean;
import com.yiyun.rmj.bean.apiparm.AddShppingCartParm;
import com.yiyun.rmj.bean.apiparm.ProductDetailParm;
import com.yiyun.rmj.dialog.BottomDialog;
import com.yiyun.rmj.fragment.ProductDetailFragment;
import com.yiyun.rmj.fragment.ProductEvaluationFragment;
import com.yiyun.rmj.utils.DESHelper;
import com.yiyun.rmj.utils.PermissionUtil;
import com.yiyun.rmj.utils.StatusBarUtil;
import com.yiyun.rmj.utils.UMShareHelper;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//商品详情2
public class ProductDetailActivity extends BaseActivity implements View.OnClickListener{
    ViewPager detailPager;
    ProductDetailActivity.ViewPagerAdapter detalAdapter;
    String[] tabNames = new String[]{"商品详情","评价"};
    private MagicIndicator indicator;

    TextView tv_product_detail_collection;
    ShineButton po_shinebutton;
    ArrayList<Fragment> fragments;
    private boolean isCollectioned = false;
    public XBanner banner;
    private int productId;//产品Id
    private int showFlag; //区分是立即购买或了解更多点进来的 ：1为了解更多，2为购买
    private List<String> imgesUrl;
    private TextView tv_product_name, tv_product_des, tv_price, tv_freight;
    private ProductDetailBean.Data productData;
    private TextView tv_sale_num;

    private BottomDialog.CommitLisener addOrBuyLisenner = new BottomDialog.CommitLisener() {
        @Override
        public void onCommit(String color,int mproductId ,int num, int type) {

            if(type == BottomDialog.TYPE_ADD_TO_CART){
                AddShppingCartParm parm = new AddShppingCartParm();
                parm.setNum(num);
                parm.setProductId(mproductId);
                String parmDes = DESHelper.encrypt(gson.toJson(parm));
                addToCart(parmDes);
            }else{
                //到确认订单界面
                Intent intent = new Intent(ProductDetailActivity.this, SettlementActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",1);
                bundle.putInt("productId",mproductId);
                bundle.putInt("num",num);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

    /**
     * 分享的回调
     */
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            Log.e("bcz","ProductDescriptionActivity-UMShareListener-onStart" );
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Log.e("bcz","ProductDescriptionActivity-UMShareListener-onResult:name==" + share_media.getName() + "==getauthstyle==" + share_media.getauthstyle(true) + "===getsharestyle==" + share_media.getsharestyle(true));

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Log.e("bcz","ProductDescriptionActivity-UMShareListener-onError:" + throwable.getMessage() );
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Log.e("bcz","ProductDescriptionActivity-UMShareListener-onCancel" );
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    public void initTitleView(){
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setVisibility(View.VISIBLE);
        iv_back.setOnClickListener(this);

        ImageView iv_shopping_cart = findViewById(R.id.iv_shopping_cart);
        iv_shopping_cart.setVisibility(View.VISIBLE);
        iv_shopping_cart.setOnClickListener(this);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.product_detial));
    }

    @Override
    protected void initView() {
        initTitleView();
        isCollectioned = false;

        productId = getIntent().getIntExtra("productId",0);
        showFlag = getIntent().getIntExtra("showFlag",1);
        fragments = new ArrayList<>();
        fragments.add(new ProductDetailFragment());
        Fragment evaluationFragment = new ProductEvaluationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("productId", productId);
        evaluationFragment.setArguments(bundle);
        fragments.add(evaluationFragment);
        TextView tv_share = findViewById(R.id.tv_share);
        tv_share.setOnClickListener(this);

        tv_sale_num = findViewById(R.id.tv_sale_num);

        detailPager = findViewById(R.id.viewpager);

        indicator = findViewById(R.id.magic_indicator );
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter(){
            @Override
            public int getCount() {
                return tabNames == null ? 0 : tabNames.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);

                colorTransitionPagerTitleView.setNormalColor(getResources().getColor(R.color.color99));
                colorTransitionPagerTitleView.setSelectedColor(getResources().getColor(R.color.textcolor_black));
                colorTransitionPagerTitleView.setText(tabNames[index]);

                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        detailPager.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(getResources().getColor(R.color.light_blue));
                return indicator;
            }
        });
        indicator.setNavigator(commonNavigator);

        tv_product_detail_collection = findViewById(R.id.tv_product_detail_collection);
        po_shinebutton = findViewById(R.id.po_shinebutton);
        po_shinebutton.init(this);
        po_shinebutton.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {

                //收藏操作
                ProductDetailParm productDetailParm = new ProductDetailParm();
                productDetailParm.setProductId(productId);
                String strDes = DESHelper.encrypt(gson.toJson(productDetailParm));
                collection(strDes);

                if(checked){
                    tv_product_detail_collection.setText(getString(R.string.collectioned));
                }else{
                    tv_product_detail_collection.setText(getString(R.string.collection));
                }
            }
        });


        tv_product_name = findViewById(R.id.tv_product_name);
        tv_product_des = findViewById(R.id.tv_product_des);
        tv_price = findViewById(R.id.tv_price);
        tv_freight = findViewById(R.id.tv_freight);

//        LinearLayout rl_collection = findViewById(R.id.rl_collection);
//        rl_collection.setOnClickListener(this);

        LinearLayout ll_join_collection = findViewById(R.id.ll_join_collection);
        ll_join_collection.setOnClickListener(this);

        TextView tv_buy_imidiately = findViewById(R.id.tv_buy_imidiately);
        tv_buy_imidiately.setOnClickListener(this);

        detalAdapter = new ProductDetailActivity.ViewPagerAdapter(getSupportFragmentManager(),fragments);
        detailPager.setAdapter(detalAdapter);
        detailPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                indicator.onPageSelected(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                indicator.onPageScrollStateChanged(state);
            }
        });

        banner = findViewById(R.id.banner_1);
        imgesUrl = new ArrayList<>();
        imgesUrl.add("http://www.seeege.com/app/defaultpicture.png");
        // 为XBanner绑定数据
        banner .setData(imgesUrl,null);//第二个参数为提示文字资源集合
        // XBanner适配数据
        banner.setmAdapter(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                Glide.with(ProductDetailActivity.this).load(imgesUrl.get(position)).into((ImageView) view);
            }
        });
        // 设置XBanner的页面切换特效，选择一个即可，总的大概就这么多效果啦，欢迎使用
        banner.setPageTransformer(Transformer.Default);//横向移动
        // 设置XBanner页面切换的时间，即动画时长
        banner.setPageChangeDuration(1000);

    }

    public void refreshBannerData(){
        banner.setData(imgesUrl,null);//第二个参数为提示文字资源集合
    }

    @Override
    protected void initData() {
        ProductDetailParm productDetailParm = new ProductDetailParm();
        productDetailParm.setProductId(productId);
        String parmDes = DESHelper.encrypt(gson.toJson(productDetailParm));
        getCommodityDetail(parmDes);
    }

    public void changeCollectionBtnState(){
        if(isCollectioned){
            tv_product_detail_collection.setText(getString(R.string.collectioned));
        }else{
            tv_product_detail_collection.setText(getString(R.string.collection));
        }
        po_shinebutton.setChecked(isCollectioned);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                //返回按钮
                finish();
                break;
            case R.id.iv_shopping_cart:
                //购物车按钮
                Intent intentCart = new Intent(ProductDetailActivity.this,ShoppingCartActivity.class);
                startActivity(intentCart);
                break;

            case R.id.rl_collection:
                //收藏按钮
//                ProductDetailParm productDetailParm = new ProductDetailParm();
//                productDetailParm.setProductId(productId);
//                String strDes = DESHelper.encrypt(gson.toJson(productDetailParm));
//                collection(strDes);
                break;

            case R.id.ll_join_collection:
                //加入购物车按钮

                BottomDialog.createDialog(ProductDetailActivity.this, BottomDialog.TYPE_ADD_TO_CART,productData, addOrBuyLisenner).show();
                break;

            case R.id.tv_buy_imidiately:
                //立即购买按钮
                BottomDialog.createDialog(ProductDetailActivity.this, BottomDialog.TYPE_BUY_NOW, productData, addOrBuyLisenner).show();
                break;
            case R.id.tv_share:
                PermissionUtil.requestStoragePermission(ProductDetailActivity.this,new PermissionUtil.IRequestPermissionCallBack(){
                    @Override
                    public void permissionSuccess() {
                        UMShareHelper.toShare(ProductDetailActivity.this, productData.getProductName(), productData.getProductShare(), productData.getComposition(),shareListener);
                    }
                });
                break;
        }

    }

    /**
     *
     * @param desparms
     */
    public void getCommodityDetail(String desparms) {
        api.getCommodityDetail(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProductDetailBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(ProductDetailBean obj) {
                        if(obj.getState() == 1){
                            productData = obj.getInfo().getData();
                            refreshData(obj.getInfo().getData());
                            if(showFlag == 2){
                                //立即购买直接弹窗口
                                BottomDialog.createDialog(ProductDetailActivity.this, BottomDialog.TYPE_BUY_NOW, productData, addOrBuyLisenner).show();
                            }
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }
                    }
                });
    }


    /**
     * 加入购物车
     * @param desparms
     */
    public void addToCart(String desparms) {
        api.addToCart(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(BaseBean obj) {
                        showConnectError(obj.getInfo().getMessage());
                        if(obj.getState() == 1){
//                            refreshData(obj.getInfo().getData());
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{

                        }
                    }
                });
    }



    /**
     * 收藏
     * @param desparms
     */
    public void collection(String desparms) {
        api.collection(desparms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showConnectError();
                    }

                    @Override
                    public void onNext(BaseBean obj) {
                        if(obj.getState() == 1){
//                            isCollectioned = !isCollectioned;
//                            changeCollectionBtnState();
//                            refreshData(obj.getInfo().getData());
                        }else if(obj.getState() == -1){
                            startlogin();
                        }else{
                            showConnectError(obj.getInfo().getMessage());
                        }
                    }
                });

    }

    public void refreshData(ProductDetailBean.Data data){

        ArrayList<ProductDetailBean.ProductPicture> pictures = data.getProductPicture();
        imgesUrl.clear();
        for(ProductDetailBean.ProductPicture productPicture:pictures){
            imgesUrl.add(productPicture.getUrl());
        }
        refreshBannerData();

        tv_product_name.setText(data.getProductName());
        tv_product_des.setText(data.getComposition());
        tv_price.setText(data.getProductPrice() + "");
        tv_freight.setText("运费：￥" + data.getExpressMoney());
        tv_sale_num.setText( data.getSalesVolume() + "人已付款");

        ProductDetailFragment productDetailFragment = (ProductDetailFragment)fragments.get(0);
        productDetailFragment.refreshData(data.getGraphic());

        if(data.getIsCollection() == 0){
            //没有收藏
            isCollectioned = false;
        }else{
            isCollectioned = true;
        }
        changeCollectionBtnState();

    }


    private class ViewPagerAdapter extends DragDetailFragmentPagerAdapter {
        List<Fragment> fragments;
        private View mCurrentView;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.fragments = list;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (object instanceof View) {
                mCurrentView = (View) object;
            } else if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                mCurrentView = fragment.getView();
            }
        }

        @Override
        public Fragment getItem(int num) {
            return fragments.get(num);
        }

        public View getPrimaryItem() {
            return mCurrentView;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setImmersiveStatusBar(this,true);
    }
}
