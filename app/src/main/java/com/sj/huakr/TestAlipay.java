package com.sj.huakr;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sj.huakr.utils.AlipayUtils;

public class TestAlipay extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_alipay);
        button=findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                AlipayUtils.payToAlipay(TestAlipay.this,handler,"1","201506101035040000001");
                break;
        }
    }
}
