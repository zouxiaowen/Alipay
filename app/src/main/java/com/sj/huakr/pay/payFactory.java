package com.sj.huakr.pay;

public class payFactory {

    public static final int TYPE_WX = 0;
    public static final int TYPE_ALI = 1;

    /**
     * 工厂模式
     * @param type
     * @return
     */
    public static Ipay cread(int type) {
        switch (type) {
            case TYPE_WX:
                return new WxPay();
            case TYPE_ALI:
                return  new AIlipay();
        }
        return null;
    }
}
