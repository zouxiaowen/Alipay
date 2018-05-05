package com.sj.huakr.pay;

import android.app.Activity;

import com.google.gson.Gson;
import com.sj.huakr.bean.wexben;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WxPay implements Ipay{
    public static final String APP_ID = "wx7d8371c3e46f3c9d";

    @Override
    public void pay(Activity context, String info) {
        //支付
        IWXAPI api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(APP_ID);
        Gson gson = new Gson();
        wexben wb = gson.fromJson(info, wexben.class);
        PayReq req = new PayReq();
        req.appId = wb.getData().getAppId();
        req.partnerId = wb.getData().getPartnerId();
        req.prepayId = wb.getData().getPrepayId();
        req.nonceStr = wb.getData().getNonceStr();
        req.timeStamp = wb.getData().getTimeStamp();
        req.packageValue = wb.getData().getPackageValue();
        req.sign = wb.getData().getSign();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        //3.调用微信支付sdk支付方法
        api.sendReq(req);
    }
}
