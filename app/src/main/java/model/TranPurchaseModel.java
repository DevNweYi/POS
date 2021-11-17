package model;

import java.io.Serializable;

public class TranPurchaseModel implements Serializable {
    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAmount() {
        return amount;
    }

    public int getUnitId() {
        return unitId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsProductUnit() {
        return isProductUnit;
    }

    public String getUnitKeyword() {
        return unitKeyword;
    }

    public void setIsProductUnit(int isProductUnit) {
        this.isProductUnit = isProductUnit;
    }

    public void setUnitKeyword(String unitKeyword) {
        this.unitKeyword = unitKeyword;
    }

    public int getPurPrice() {
        return purPrice;
    }

    public void setPurPrice(int purPrice) {
        this.purPrice = purPrice;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public double getdQuantity() {
        return dQuantity;
    }

    public void setdQuantity(double dQuantity) {
        this.dQuantity = dQuantity;
    }

    public int getMasterPurchaseId() {
        return masterPurchaseId;
    }

    public void setMasterPurchaseId(int masterPurchaseId) {
        this.masterPurchaseId = masterPurchaseId;
    }

    int productId;
    int id;
    int quantity;
    int purPrice;
    int amount;
    int unitId;
    int isProductUnit;
    String productName,unitKeyword;
    String unitType;
    double dQuantity;
    int masterPurchaseId;

}
