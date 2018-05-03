package com.sj.huakr.bean;

public class apliben {

    /**
     * msg : 请求成功
     * code : 12000
     * data : app_id=2016030901197557&biz_content=%7B%22out_trade_no%22%3A%2220161222131856828%22%2C%22total_amount%22%3A%221.0%22%2C%22subject%22%3A%22%E8%8A%B1%E9%9F%A9%E6%94%AF%E4%BB%98%E5%AE%9D%E5%85%85%E5%80%BC%22%2C%22timeout_express%22%3A%2230m%22%2C%22body%22%3A%22%E8%8A%B1%E9%9F%A9%E6%94%AF%E4%BB%98%E5%AE%9D%E5%85%85%E5%80%BC%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22seller_id%22%3Anull%7D&charset=utf-8&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fwww.huakr.com%2Forder%2FAlipayNotyfy2&sign_type=RSA&timestamp=2018-05-03+19%3A41%3A00&version=1.0&sign=TdXqY1P8Hf9tkrsCIudGtecpajN1kocWxkauO0PdjVQ7Ei0F4I6mg5AVaA%2BqIyZia0X2cxDl4t2lXhONT0uX9QOFDNnu%2FxtTCuzcoCejL%2Bj%2F%2BexG0nW%2BpCNbAq2fOpJipK5di2R9s2AsL6VYjJPQCBZIOtwQLG3Qpj8HbjqsaBw%3D&sign_type=RSA
     */

    private String msg;
    private String code;
    private String data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
