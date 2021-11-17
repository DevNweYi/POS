package model;

import java.io.Serializable;

public class TranSaleModel implements Serializable {

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

    public int getSalePrice() {
        return salePrice;
    }

    public int getIsProductUnit() {
        return isProductUnit;
    }

    public String getUnitKeyword() {
        return unitKeyword;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public void setIsProductUnit(int isProductUnit) {
        this.isProductUnit = isProductUnit;
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

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getdQuantity() {
        return dQuantity;
    }

    public void setdQuantity(double dQuantity) {
        this.dQuantity = dQuantity;
    }

    public int getMasterSaleId() {
        return masterSaleId;
    }

    public void setMasterSaleId(int masterSaleId) {
        this.masterSaleId = masterSaleId;
    }

    public int getOpenBillId() {
        return openBillId;
    }

    public void setOpenBillId(int openBillId) {
        this.openBillId = openBillId;
    }

    int productId;
    int id;
    int quantity;
    int salePrice;
    int amount;
    int unitId;
    int isProductUnit;
    String productName;
    String unitKeyword;
    String unitType;
    String productCode;
    String categoryName;
    double dQuantity;
    int masterSaleId;
    int openBillId;

}
