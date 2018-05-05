package com.sj.huakr.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @function
 * @author fengjing:
 * @date ：2016年4月12日 上午9:17:29
 * @mail 164303256@qq.com
 */
public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
	private IWXAPI api;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, "wx7d8371c3e46f3c9d");
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode==BaseResp.ErrCode.ERR_OK) {
				Toast.makeText(this,"支付成功" ,Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(this,"支付失败" ,Toast.LENGTH_SHORT).show();
			}
			finish();
		}else {
			finish();
		}
	}
	}


