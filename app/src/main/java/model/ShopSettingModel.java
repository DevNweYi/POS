package model;

import java.sql.Blob;

public class ShopSettingModel {
    public byte[] getLogo() {
        return logo;
    }

    public String getShopName() {
        return shopName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    byte[] logo;
    String shopName;
    String mobile;
    String address;
    String currencySymbol;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
    String otherMobile;

    public String getOtherMobile() {
        return otherMobile;
    }

    public void setOtherMobile(String otherMobile) {
        this.otherMobile = otherMobile;
    }
}
