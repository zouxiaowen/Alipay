package com.sj.huakr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sj.huakr.bean.apliben;
import com.sj.huakr.bean.logingben;
import com.sj.huakr.bean.wexben;
import com.sj.huakr.utils.SharedPreferencesHelper;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button1, button2, button3, button4;
    private TextView textView2;
    String URL = "http://www.huakr.com/";
    String LOGIN = URL + "user/loginNew";
    /**
     * 获取订单签名信息.
     */
    String ORDER_SIGN = URL + "order/getSign";
    wexben wb;

    private SharedPreferencesHelper sharedPreferencesHelper;


    private IWXAPI api;

    // 应用id
    public static final String APP_ID = "wx7d8371c3e46f3c9d";

    // 商户号
    public static final String MCH_ID = "1319350901";

    // 秘钥
    public static final String API_KEY = "f8g327muCXGjV7qG2znvlspLx2Qw068m";

    public static final int ALI_PAY_FLAG = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button2.setOnClickListener(this);
        button1.setOnClickListener(this);
        textView2 = findViewById(R.id.textView2);
        sharedPreferencesHelper = new SharedPreferencesHelper(
                MainActivity.this, "anhua");


        String token = sharedPreferencesHelper.getSharedPreference("token", "").toString();
        if (token != null) {
            textView2.setText(token);
        }

        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(APP_ID);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                login("18964057803", "123qwe");
                break;
            case R.id.button2:
                getShaping();
                break;

            case R.id.button3:
                if (wb != null) {
                    sendPayRequest(wb);
                }
                break;
            case R.id.button4:
                senAlipay();
                break;
        }
    }

    private void senAlipay() {
//        //老的本地签名
//        myDialog.showLoadingDialog();
        int intmoneys = (int) (Double.valueOf(1) * 100);
//        RequestInterpolator interpolator = new RequestInterpolator(IConstants.ORDER_SIGN);
//        interpolator.addParam("token", GlobalApplication.getInstance().getToken());
//        interpolator.addParam("type", "1");
//        interpolator.addParam("amount", String.valueOf(intmoneys));
//        interpolator.addParam("orderCode", mOrderCode);
//        NetUtils.doHttpGetData(interpolator, new NetOperatorListener<String>() {
//            @Override
//            public void onFail(String msg) {
//                super.onFail(msg);
//                ToastUtils.show(msg);
//                myDialog.dismissLoadingDialog();
//            }
//
//            @Override
//            public void onSuccess(String s) {
//                super.onSuccess(s);
//                myDialog.dismissLoadingDialog();
//                try {
//                    JSONObject object = new JSONObject(s);
//                    int code = object.getInt("code");
//                    String payInfo = object.getString("data");
//
//                    if (code == IConstants.SUCCESSCODE) {
//                        AlipayUtils.payToAlipay2(PayFrontMoneyActivity.this, mHandler, payInfo);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
        String token = sharedPreferencesHelper.getSharedPreference("token", "").toString();
        OkGo.<String>post(ORDER_SIGN)//
                .tag(this)//
                .params("token", token)
                .params("type", "1")
                .params("amount", String.valueOf(intmoneys))
                .params("orderCode", "20161222131856828")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(final Response<String> response) {
                        Gson gson = new Gson();
                        final apliben ap = gson.fromJson(response.body(), apliben.class);
                        //注意这里已经是在主线程了
                        final String data = response.body();//这个就是返回来的结果
                        textView2.setText(ap.getData());
                        try {
                            JSONObject object = new JSONObject(response.body());
                            int code = object.getInt("code");
                            final String payInfo = object.getString("data");

                            if (code == 12000) {
                                Runnable payRunnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        // 构造PayTask 对象
                                        PayTask alipay = new PayTask( MainActivity.this);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });

    }


    /**
     * 调用微信支付
     */
    public void sendPayRequest(wexben wb) {
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


    private void login(String s, String s1) {
        OkGo.<String>post(LOGIN)//
                .tag(this)//
                .params("phone", s)
                .params("password", s1)
                .params("loginStatus", "0")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        logingben logingben = gson.fromJson(response.body(), logingben.class);
                        //注意这里已经是在主线程了
                        String data = response.body();//这个就是返回来的结果
                        textView2.setText(logingben.getData().getToken());
                        sharedPreferencesHelper.put("token", logingben.getData().getToken());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });

    }

    public void getShaping() {
        String token = sharedPreferencesHelper.getSharedPreference("token", "").toString();
        if (token != null) {

            int intmoneys = (int) (Double.valueOf(1) * 100);
            OkGo.<String>post(ORDER_SIGN)//
                    .tag(this)//
                    .params("token", token)
                    .params("type", "2")
                    .params("amount", String.valueOf(intmoneys))
                    .params("orderCode", "20161222131856828")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Gson gson = new Gson();
                            wb = gson.fromJson(response.body(), wexben.class);
                            //注意这里已经是在主线程了
                            String data = response.body();//这个就是返回来的结果
                            textView2.setText(data);
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                        }
                    });
        }

    }
    public static int parseInt(String str) {
        int ret = 0;
        try {
            ret = Integer.parseInt(str);
        } catch (Exception e) {

        }
        return ret;
    }


    @SuppressLint("HandlerLeak")
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
