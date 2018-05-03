package com.sj.huakr.bean;

public class wexben {

    /**
     * msg : 请求成功
     * code : 12000
     * data : {"timeStamp":"1525330150","packageValue":"Sign=WXPay","appId":"wx7d8371c3e46f3c9d","sign":"7540FFCB4B2CDBF19783B8F34482E6F0","partnerId":"1319350901","prepayId":"wx03145538877472c62f4e79f40024720472","nonceStr":"m8s9diygdxlve7n1bg85ikbb"}
     */

    private String msg;
    private String code;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * timeStamp : 1525330150
         * packageValue : Sign=WXPay
         * appId : wx7d8371c3e46f3c9d
         * sign : 7540FFCB4B2CDBF19783B8F34482E6F0
         * partnerId : 1319350901
         * prepayId : wx03145538877472c62f4e79f40024720472
         * nonceStr : m8s9diygdxlve7n1bg85ikbb
         */

        private String timeStamp;
        private String packageValue;
        private String appId;
        private String sign;
        private String partnerId;
        private String prepayId;
        private String nonceStr;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getPackageValue() {
            return packageValue;
        }

        public void setPackageValue(String packageValue) {
            this.packageValue = packageValue;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public void setPartnerId(String partnerId) {
            this.partnerId = partnerId;
        }

        public String getPrepayId() {
            return prepayId;
        }

        public void setPrepayId(String prepayId) {
            this.prepayId = prepayId;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }
    }
}
