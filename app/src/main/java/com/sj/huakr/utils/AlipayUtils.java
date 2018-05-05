package com.sj.huakr.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AlipayUtils {
	// 商户PID
	public static final String PARTNER = "2088221318506695";
	// 商户收款账号
	public static final String SELLER = "18601779203@163.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKqzCdjeIuKWz1/+j8VEs5upaUwUI3W4YqJAwwqW+tk88ctWOksnN2LksDG0h+iG6e9mw8b7OVi8OKQhRk6BiOIRlWsUOoNvs8+J4vl1UaKrW6IxX8CbhLJtlYqSDz5dTjcEYQEG2yGpZTI8+UCXv2IMBJTFOEGHkKekCDyXcu+RAgMBAAECgYBOXN7aZi/RobYVtYE+6GkjvsQOHvigZEMzhVOEmwcRj37Y//7G3Xv5tE6Gov/5aWYfdyfNQytyZwYBoxzpkWHK1nw8118oVtIiJlFlCBP/8Y8aO3hVw9bm/dvrxNSsJk2vgeWejgwNWieNrwcwN/GimiJS7CPlbVuhq5kgBL/IAQJBANmzz+73UPKV/uK+wV0hkV5RuFEDVWMv6SzXAyHbwBhVg7XJAW0OGtIQtVKCe3/pDi6XyxjETU6eXHIqYHyRi8ECQQDIunQgewBWVCUXf1ahCWt3JipADDhVyJGZE4la22IhbCsZdE7fd4iz7NVaVQNIqasD9mtfpO9lTZX+tEEbcZfRAkBBlTC73ZduLQ+nAS5BPx6K2nV7OlTb15AYKQHRjmDVM9XEyrP6RXiA9fWZ0KMhynSyRpoxcNmVKJHS9r8wZ6xBAkEAkLQBdUzWsGVWm/rba5oL08g6mxN0JTtC4vR3h30x3+BHrcDv/x7/+J1aLRWuGVrfAytIupl/TytAX254pR0dkQJBAJzUTLiX3Lx3nNmJLEN+kEDqPGMV8hO/DSPlTYU65Czok2CWVPT2i7K4y/VaI0XadUFGEwiaeTKI82yfBfRZYwk=";
	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	

	public static String getOrderInfo(String subject, String out_trade_no, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + "body" + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径androidAlipayNotyfy.do
		orderInfo += "&notify_url=" + "\"" + "http://www.huakr.com/order/AlipayNotyfy"  + "\"";
		// http://app.zuzuapp.com/androidAlipayNotyfy.do

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	public static void payToAlipay(final Context context, final Handler mHandler, String price, String out_trade_no) {
		String subject = "花韩订单支付";

		String orderInfo = AlipayUtils.getOrderInfo(subject, out_trade_no, price);
		// 对订单做RSA 签名
		String sign = SignUtils.sign(orderInfo, RSA_PRIVATE);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity) context);
				// 调用支付接口，获取支付结果
				//alipay.pay(arg0, arg1)
				String result = alipay.pay(payInfo,true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();

	}

	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/**
	 * 后台请求签名
	 */
	public static void payToAlipay2(final Context context, final Handler mHandler, final String payInfo){
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity) context);
				// 调用支付接口，获取支付结果
				//alipay.pay(arg0, arg1)
				String result = alipay.pay(payInfo,true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
}
