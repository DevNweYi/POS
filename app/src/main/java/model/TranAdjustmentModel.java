package model;

public class TranAdjustmentModel {

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getTranAdjustmentId() {
        return tranAdjustmentId;
    }

    public void setTranAdjustmentId(int tranAdjustmentId) {
        this.tranAdjustmentId = tranAdjustmentId;
    }

    public int getMasterAdjustmentId() {
        return masterAdjustmentId;
    }

    public void setMasterAdjustmentId(int masterAdjustmentId) {
        this.masterAdjustmentId = masterAdjustmentId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPurPrice() {
        return purPrice;
    }

    public void setPurPrice(int purPrice) {
        this.purPrice = purPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getIsProductUnit() {
        return isProductUnit;
    }

    public void setIsProductUnit(int isProductUnit) {
        this.isProductUnit = isProductUnit;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnitKeyword() {
        return unitKeyword;
    }

    public void setUnitKeyword(String unitKeyword) {
        this.unitKeyword = unitKeyword;
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

    int productId;
    int tranAdjustmentId;
    int masterAdjustmentId;
    int quantity;
    int purPrice;
    int amount;
    int unitId;
    int isProductUnit;
    String productName,unitKeyword;
    String unitType;
    double dQuantity;

}
