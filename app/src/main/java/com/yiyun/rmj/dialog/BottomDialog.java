package com.yiyun.rmj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.yiyun.rmj.R;
import com.yiyun.rmj.bean.apibean.ProductDetailBean;
import com.yiyun.rmj.utils.DisplayUtils;
import com.yiyun.rmj.view.WrapLayout;

import java.util.ArrayList;

public class BottomDialog{

    public static final int TYPE_ADD_TO_CART = 1;//添加到购物车
    public static final int TYPE_BUY_NOW = 2;    //立即购买
    private static int showType = TYPE_ADD_TO_CART;

    static Context mContext;
    static Dialog  bottomDialog;
    static TextView tv_count;
    static int count;
    static int currentStone; //当前库存
    static String currentSelect;
    static int currentSelectPosition = -1;
    private static CommitLisener lisener_1;
    private static ProductDetailBean.Data productData;
    private static TextView[]  textViews;
    private static Integer[] selectCountNum;
    private static TextView tv_store;
    private static TextView tv_money;
    private static ImageView iv_product_icon;
    private static ArrayList<ProductDetailBean.Classification> classification;



    public static void show(){
        bottomDialog.show();
    }

    public static Dialog createDialog(Context context, int type, ProductDetailBean.Data data, CommitLisener lisener){
        showType = type;
        mContext = context;
        count = 1;
        currentSelect = null;
        lisener_1 = lisener;
        productData = data;
        currentSelectPosition = -1;
        currentStone = 0;

        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_bottom_buy, null);
        bottomDialog.setContentView(contentView);
        initView(contentView,type);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        params.bottomMargin = 0;
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.setCanceledOnTouchOutside(true);
        return bottomDialog;
    }

    private  static void initView(View view , final int type){
        ImageView iv_close = view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
            }
        });

        tv_store = view.findViewById(R.id.tv_store);
        tv_store.setText("库存" + productData.getInventory() + "件");

        TextView tv_choice_product_name = view.findViewById(R.id.tv_choice_product_name);
        tv_choice_product_name.setText("已选择：" + productData.getProductName());

        final TextView tv_money = view.findViewById(R.id.tv_money);
        tv_money.setText("" + productData.getProductPrice());

        iv_product_icon = view.findViewById(R.id.iv_product_icon);
        Glide.with(mContext).load(productData.getProductPicture().get(0).getUrl()).apply(new RequestOptions()).into(iv_product_icon);

        WrapLayout vg_contant = view.findViewById(R.id.vg_contant);
        classification = productData.getClassification();
        if(classification != null && classification.size() != 0){
            textViews = new TextView[classification.size()];
            selectCountNum = new Integer[classification.size()];

            for(int i=0; i< classification.size(); i++){
                currentStone = currentStone + classification.get(i).getInventory();
                selectCountNum[i] = 1;
                TextView textView = new TextView(mContext);
                textView.setGravity(17);
                textView.setPadding(DisplayUtils.dp2px(mContext,15),DisplayUtils.dp2px(mContext,8),DisplayUtils.dp2px(mContext,15),DisplayUtils.dp2px(mContext,8));
                textViews[i] = textView;
                textViews[i].setBackgroundResource(R.drawable.unselector_color);
                textViews[i].setText(classification.get(i).getProductModel());
                textViews[i].setId(i);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2,-2);
                layoutParams.setMargins(0,DisplayUtils.dp2px(mContext,10),DisplayUtils.dp2px(mContext,10),0);
                vg_contant.addView(textViews[i],layoutParams);
            }

            for(TextView tv: textViews){
                tv.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        if(currentSelectPosition != -1){
                            textViews[currentSelectPosition].setBackgroundResource(R.drawable.unselector_color);
                        }
                        currentSelectPosition = view.getId();
                        currentStone = classification.get(currentSelectPosition).getInventory();
                        tv_store.setText("库存" + currentStone + "件");
                        tv_count.setText("" + selectCountNum[currentSelectPosition]);
                        tv_money.setText("" +classification.get(currentSelectPosition).getProductPrice() );

                        Glide.with(mContext).load(classification.get(currentSelectPosition).getProductPicture().getUrl()).apply(new RequestOptions()).into(iv_product_icon);

                        TextView textView = (TextView)view;
                        currentSelect = textView.getText().toString();
                        view.setBackgroundResource(R.drawable.selector_color);
                    }
                });
            }

        }

        tv_count = (TextView) view.findViewById(R.id.tv_product_num);
        tv_count.setText("" + 1);

        Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(currentSelect)){
                    ToastUtils.show("请先选择颜色");
                    return;
                }

                lisener_1.onCommit(currentSelect,classification.get(currentSelectPosition).getProductId(), selectCountNum[currentSelectPosition],type);
                bottomDialog.dismiss();

            }
        });



        ImageView iv_less = view.findViewById(R.id.iv_less);
        iv_less.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (currentSelectPosition < 0){
                    return;
                }
                if(currentSelectPosition != -1){
                    if(selectCountNum[currentSelectPosition] > 1){
                        selectCountNum[currentSelectPosition]--;
                        tv_count.setText("" + selectCountNum[currentSelectPosition]);
                    }

                }

            }
        });

        ImageView iv_add = view.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (currentSelectPosition < 0){
                    return;
                }
                if (selectCountNum[currentSelectPosition] < currentStone) {
                    selectCountNum[currentSelectPosition]++;
                    tv_count.setText("" + selectCountNum[currentSelectPosition]);
                }

            }
        });

    }

    public interface CommitLisener{
       void onCommit(String color,int productId, int num, int type);
    }

}
