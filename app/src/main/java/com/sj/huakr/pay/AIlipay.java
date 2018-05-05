package com.sj.huakr.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;

import org.json.JSONObject;

public class AIlipay implements Ipay{
    public static final int ALI_PAY_FLAG = 0;

    @Override
    public void pay(final Activity context, final String info) {
        try{
            //支付宝支付
            JSONObject object = new JSONObject(info);
            int code = object.getInt("code");
            final String payInfo = object.getString("data");
            if (code == 12000) {
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask( context);
                        // 调用支付接口，获取支付结果
                        //alipay.pay(arg0, arg1)
                        String result = alipay.pay(payInfo,true);
                        Message msg = new Message();
                        msg.what = ALI_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }
        }catch (Exception e){

        }

    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALI_PAY_FLAG:
//                    Map<String, String> result = (Map<String, String>) (msg.obj);
//                        int status =  parseInt("" + result.get("resultStatus"));
//                        String tostMsg = result.get("memo");
//                        if (status == 9000) {
//                            tostMsg = "支付成功";
//                        }
//                        Toast.makeText(MainActivity.this, tostMsg, Toast.LENGTH_LONG).show();
//                        if (status == 9000) {
//                        }
                    break;
            }

        }
    };
}
