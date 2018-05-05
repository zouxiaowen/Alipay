package com.sj.huakr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sj.huakr.bean.logingben;
import com.sj.huakr.pay.payFactory;
import com.sj.huakr.utils.SharedPreferencesHelper;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button1, button2, button3, button4;
    private TextView textView2;
    String URL = "http://www.huakr.com/";
    String LOGIN = URL + "user/loginNew";
    /**
     * 获取订单签名信息.
     */
    String ORDER_SIGN = URL + "order/getSign";

    private SharedPreferencesHelper sharedPreferencesHelper;
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;
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



        String ACTION_PAYSUCCESS = "com.sjing.huakr.action.PAYSUCCESS";
        mFilter = new IntentFilter(ACTION_PAYSUCCESS);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getIntExtra("errorCode", -1) == 0) {
                    finish();
//                    startActivity(new Intent(PayFrontMoneyActivity.this, PlaySuccessActivity.class));
                }
            }
        };
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
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

                break;
            case R.id.button4:
                senAlipay();
                break;
        }
    }

    private void senAlipay() {
//        //老的本地签名
        int intmoneys = (int) (Double.valueOf(1) * 100);
        String token = sharedPreferencesHelper.getSharedPreference("token", "").toString();
        OkGo.<String>post(ORDER_SIGN)//
                .tag(this)//
                .params("token", token)
                .params("type", "1")
                .params("amount", String.valueOf(intmoneys))
                .params("orderCode", "20161222131856827")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(final Response<String> response) {

                        payFactory.cread(payFactory.TYPE_ALI).pay(MainActivity.this, response.body());

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });

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
                    .params("orderCode", "20161222131856827")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            if (!response.body().isEmpty()) {
                                payFactory.cread(payFactory.TYPE_WX).pay(MainActivity.this, response.body());
                            }
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


}
