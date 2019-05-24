package com.ting.a.livehome.bean;

/**
 * @Explain: 配送单位也就是后台的商家
 */
public class MerchantInfo {
    //商家ID
    public String MerchantId;
    //商家名字
    public String MerchantName;
    //商家地址
    public String Adds;
    //商家电话
    public String PhoneNum;
    //商家秒速
    public String MerchantIntro;
    //商家GpsLng
    public double GpsLng;
    //商家GpsLat
    public double GpsLat;

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String merchantName) {
        MerchantName = merchantName;
    }

    public String getAdds() {
        return Adds;
    }

    public void setAdds(String adds) {
        Adds = adds;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public String getMerchantIntro() {
        return MerchantIntro;
    }

    public void setMerchantIntro(String merchantIntro) {
        MerchantIntro = merchantIntro;
    }

    public double getGpsLng() {
        return GpsLng;
    }

    public void setGpsLng(double gpsLng) {
        GpsLng = gpsLng;
    }

    public double getGpsLat() {
        return GpsLat;
    }

    public void setGpsLat(double gpsLat) {
        GpsLat = gpsLat;
    }
}
