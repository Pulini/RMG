package com.yiyun.rmj.api;

import com.yiyun.rmj.bean.apibase.BaseBean;
import com.yiyun.rmj.bean.apibean.AddressBean;
import com.yiyun.rmj.bean.apibean.BeforePayBean;
import com.yiyun.rmj.bean.apibean.BondRegistBean;
import com.yiyun.rmj.bean.apibean.BuyAgainBean;
import com.yiyun.rmj.bean.apibean.CollectionBean;
import com.yiyun.rmj.bean.apibean.CreateOrderBean;
import com.yiyun.rmj.bean.apibean.CustomServiceBean;
import com.yiyun.rmj.bean.apibean.DistributionBean;
import com.yiyun.rmj.bean.apibean.EvaluationBean;
import com.yiyun.rmj.bean.apibean.GetCodeBean;
import com.yiyun.rmj.bean.apibean.GetVersionBean;
import com.yiyun.rmj.bean.apibean.IntegrationBean;
import com.yiyun.rmj.bean.apibean.MoneyDetailBean;
import com.yiyun.rmj.bean.apibean.OneZfbAccountBean;
import com.yiyun.rmj.bean.apibean.OrderDetailBean;
import com.yiyun.rmj.bean.apibean.OrderListBean;
import com.yiyun.rmj.bean.apibean.OrderProgressBean;
import com.yiyun.rmj.bean.apibean.PersonCenterBean;
import com.yiyun.rmj.bean.apibean.ProductBean;
import com.yiyun.rmj.bean.apibean.ProductDetailBean;
import com.yiyun.rmj.bean.apibean.ProductDetailEvaluationBean;
import com.yiyun.rmj.bean.apibean.RecordListBean;
import com.yiyun.rmj.bean.apibean.RotationBean;
import com.yiyun.rmj.bean.apibean.ShareBean;
import com.yiyun.rmj.bean.apibean.ShoppingCartBean;
import com.yiyun.rmj.bean.apibean.UpHeadImageBean;
import com.yiyun.rmj.bean.apibean.UpdateShopCartNumBean;
import com.yiyun.rmj.bean.apibean.ZfbAccountBean;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface Api {

    // 账号注册
    @POST("/AppLogin/register")
    Observable<BondRegistBean>register(@Query("desParams") String desParams);

    // 登录前获取验证码
    @POST("/AppLogin/getCode")
    Observable<GetCodeBean> getCode(@Query("desParams") String desParams);

    // 修改密码校验验证码
    @POST("/AppLogin/updatePasswordCodeContrast")
    Observable<BaseBean> updatePasswordCodeContrast(@Query("desParams") String desParams);

    // 修改密码
    @POST("/AppLogin/updatePassword")
    Observable<BaseBean> updatePassword(@Query("desParams") String desParams);

    // 忘记密码
    @POST("/AppLogin/forgotPassword")
    Observable<BaseBean> forgotPassword(@Query("desParams") String desParams);

    // 登录
    @POST("/AppLogin/login")
    Observable<BaseBean> login(@Query("desParams") String desParams);

    // 首页 -- 商城（商品简介和图片）
    @POST("/AppHome/getCommodityList")
    Observable<ProductBean> getCommodityList(@Query("desParams") String desParams);

    // 首页 -- 轮播图
    @POST("/AppHome/getRotation")
    Observable<RotationBean> getRotation(@Query("desParams") String desParams);

    // 购物车
    @POST("/AppHome/getShoppingCart")
    Observable<ShoppingCartBean> getShoppingCart(@Query("desParams") String desParams);

    // 我的订单
    @POST("/AppHome/myOrderList")
    Observable<OrderListBean> myOrderList(@Query("desParams") String desParams);

    // 商品详情
    @POST("/AppHome/getCommodityDetail")
    Observable<ProductDetailBean> getCommodityDetail(@Query("desParams") String desParams);

    // 获取全部收货地址
    @POST("/personal/getAddressList")
    Observable<AddressBean> getAddressList(@Query("desParams") String desParams);

    // 更改默认收货地址
    @POST("/personal/updateDefaultAddress")
    Observable<BaseBean> updateDefaultAddress(@Query("desParams") String desParams);

    // 删除收货地址
    @POST("/personal/deleteAddress")
    Observable<BaseBean> deleteAddress(@Query("desParams") String desParams);


    // 添加修改地址
    @POST("/personal/address")
    Observable<BaseBean> address(@Query("desParams") String desParams);

    // 添加修改支付宝账号
    @POST("/personal/addAliPayNum")
    Observable<BaseBean> addAliPayNum(@Query("desParams") String desParams);

    // 设置为默认支付宝账号
    @POST("/personal/defaultAliPayNum")
    Observable<BaseBean> defaultAliPayNum(@Query("desParams") String desParams);

    // 删除支付宝账号
    @POST("/personal/delAliPayNum")
    Observable<BaseBean> delAliPayNum(@Query("desParams") String desParams);

    // 获取全部支付宝账号
    @POST("/personal/AliPayNumList")
    Observable<ZfbAccountBean> AliPayNumList(@Query("desParams") String desParams);

    // 获取单个支付宝账号
    @POST("/personal/getAliPayNum")
    Observable<OneZfbAccountBean> getAliPayNum(@Query("desParams") String desParams);

    // 我的分销
    @POST("/personal/getMyDistribution")
    Observable<DistributionBean> getMyDistribution(@Query("desParams") String desParams);

    // 我的收藏
    @POST("/personal/getMyCollection")
    Observable<CollectionBean> getMyCollection(@Query("desParams") String desParams);

    // 我的评价
    @POST("/personal/getMyEvaluate")
    Observable<EvaluationBean> getMyEvaluate(@Query("desParams") String desParams);

    // 积分明细
    @POST("/personal/getMyIntegral")
    Observable<IntegrationBean> getMyIntegral(@Query("desParams") String desParams);

    // 金钱明细
    @POST("/personal/moneyDetail")
    Observable<MoneyDetailBean> moneyDetail(@Query("desParams") String desParams);

    // 加入购物车
    @POST("/AppHome/addToCart")
    Observable<BaseBean> addToCart(@Query("desParams") String desParams);

    // 获取商品评价
    @POST("/AppHome/getCommodityEvaluate")
    Observable<ProductDetailEvaluationBean> getCommodityEvaluate(@Query("desParams") String desParams);

    // 收藏商品/取消商品
    @POST("/AppHome/collection")
    Observable<BaseBean> collection(@Query("desParams") String desParams);

    // 创建订单前(有结算按钮的页面)
    @POST("/AppHome/beforeCreatOrder")
    Observable<BeforePayBean> beforeCreatOrder(@Query("desParams") String desParams);

    // 创建订单
    @POST("/AppHome/creatOrder")
    Observable<CreateOrderBean> creatOrder(@Query("desParams") String desParams);

    // 订单详情
    @POST("/AppHome/orderDetail")
    Observable<OrderDetailBean> orderDetail(@Query("desParams") String desParams);

    // 修改购物车里商品数量
    @POST("/AppHome/updateShoppingCartProductNum")
    Observable<UpdateShopCartNumBean> updateShoppingCartProductNum(@Query("desParams") String desParams);

    // 删除购物车商品
    @POST("/AppHome/delShoppingCart")
    Observable<BaseBean> delShoppingCart(@Query("desParams") String desParams);

    // 添加商品评价
    @POST("/AppHome/addEvaluation")
    Observable<BaseBean> addEvaluation(@Query("desParams") String desParams);

    //上传文件
    @Multipart
    @POST("/backgroundController/uploadfile")
    Observable<UpHeadImageBean> uploadfile(@Part("file" + "\";filename=\"" + "test.jpg") RequestBody file);

    // 添加商品评价
    @POST("/personal/updatePersonalMes")
    Observable<BaseBean> updatePersonalMes(@Query("desParams") String desParams);

    // 个人中心
    @POST("/personal/home")
    Observable<PersonCenterBean> home(@Query("desParams") String desParams);

    // 邀请战绩
    @POST("/personal/getMyInvitationRecord")
    Observable<RecordListBean> getMyInvitationRecord(@Query("desParams") String desParams);

    // 删除订单
    @POST("/AppHome/delOrder")
    Observable<BaseBean> delOrder(@Query("desParams") String desParams);

    // 物流查询
    @POST("/personal/logisticsQuery")
    Observable<OrderProgressBean> logisticsQuery(@Query("desParams") String desParams);

    // 取消订单
    @POST("/AppHome/cancel")
    Observable<BaseBean> cancel(@Query("desParams") String desParams);

    // 客服信息
    @POST("/AppHome/customerService")
    Observable<CustomServiceBean> customerService(@Query("desParams") String desParams);

    // 再次购买
    @POST("/AppHome/repurchase")
    Observable<BuyAgainBean> repurchase(@Query("desParams") String desParams);

    // 确认收货
    @POST("/AppHome/confirmReceipt")
    Observable<BaseBean> confirmReceipt(@Query("desParams") String desParams);

    // 分享
    @POST("/AppHome/share")
    Observable<ShareBean> share(@Query("desParams") String desParams);

    // 提现
    @POST("/personal/withdraw")
    Observable<ShareBean> withdraw(@Query("desParams") String desParams);

    //获取版本号
    @POST("/AppHome/getVersion")
    Observable<GetVersionBean> getVersion(@Query("desParams") String desParams);


    //设备绑定
    @POST("/AppHome/addDeviceRecord")
    Observable<String> addDeviceRecord(@Query("desParams") String desParams);



























}
