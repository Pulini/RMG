package com.yiyun.rmj.utils;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yiyun.rmj.R;

public class UMShareHelper {

    /**
     * 分享
     */
    public static void toShare(Activity context,String title, String link,String content, UMShareListener shareListener){

        UMWeb web = new UMWeb(link);
        web.setTitle(title);//标题
        UMImage umImage = new UMImage(context, R.mipmap.ic_launcher);
        web.setThumb(umImage);  //缩略图
        web.setDescription(content);//描述


        new ShareAction(context).withText(title)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).open();
    }
}
