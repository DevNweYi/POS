package model;

import java.io.Serializable;

public class ProductModel implements Serializable {

    public int getProductId() {
        return productId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getTrackStock() {
        return trackStock;
    }

    public int getIsTrackStock() {
        return isTrackStock;
    }

    public int getIsProductUnit() {
        return isProductUnit;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public int getPurPrice() {
        return purPrice;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setTrackStock(int trackStock) {
        this.trackStock = trackStock;
    }

    public void setIsTrackStock(int isTrackStock) {
        this.isTrackStock = isTrackStock;
    }

    public void setIsProductUnit(int isProductUnit) {
        this.isProductUnit = isProductUnit;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public void setPurPrice(int purPrice) {
        this.purPrice = purPrice;
    }

    public int getSelectUnitId() {
        return selectUnitId;
    }

    public void setSelectUnitId(int selectUnitId) {
        this.selectUnitId = selectUnitId;
    }

    public String getSelectUnitKeyword() {
        return selectUnitKeyword;
    }

    public void setSelectUnitKeyword(String selectUnitKeyword) {
        this.selectUnitKeyword = selectUnitKeyword;
    }

    public double getBalQuantity() {
        return balQuantity;
    }

    public void setBalQuantity(double balQuantity) {
        this.balQuantity = balQuantity;
    }

    public int getStandardUnitId() {
        return standardUnitId;
    }

    public void setStandardUnitId(int standardUnitId) {
        this.standardUnitId = standardUnitId;
    }

    public int getSaleUnitId() {
        return saleUnitId;
    }

    public void setSaleUnitId(int saleUnitId) {
        this.saleUnitId = saleUnitId;
    }

    public int getSaleUnitQty() {
        return saleUnitQty;
    }

    public void setSaleUnitQty(int saleUnitQty) {
        this.saleUnitQty = saleUnitQty;
    }

    public int getStandardSaleUnitQty() {
        return standardSaleUnitQty;
    }

    public void setStandardSaleUnitQty(int standardSaleUnitQty) {
        this.standardSaleUnitQty = standardSaleUnitQty;
    }

    public int getSaleUnitSalePrice() {
        return saleUnitSalePrice;
    }

    public void setSaleUnitSalePrice(int saleUnitSalePrice) {
        this.saleUnitSalePrice = saleUnitSalePrice;
    }

    public int getSaleUnitPurPrice() {
        return saleUnitPurPrice;
    }

    public void setSaleUnitPurPrice(int saleUnitPurPrice) {
        this.saleUnitPurPrice = saleUnitPurPrice;
    }

    public int getPurUnitId() {
        return purUnitId;
    }

    public void setPurUnitId(int purUnitId) {
        this.purUnitId = purUnitId;
    }

    public int getPurUnitQty() {
        return purUnitQty;
    }

    public void setPurUnitQty(int purUnitQty) {
        this.purUnitQty = purUnitQty;
    }

    public int getStandardPurUnitQty() {
        return standardPurUnitQty;
    }

    public void setStandardPurUnitQty(int standardPurUnitQty) {
        this.standardPurUnitQty = standardPurUnitQty;
    }

    public int getPurUnitSalePrice() {
        return purUnitSalePrice;
    }

    public void setPurUnitSalePrice(int purUnitSalePrice) {
        this.purUnitSalePrice = purUnitSalePrice;
    }

    public int getPurUnitPurPrice() {
        return purUnitPurPrice;
    }

    public void setPurUnitPurPrice(int purUnitPurPrice) {
        this.purUnitPurPrice = purUnitPurPrice;
    }

    public String getStandardUnitKeyword() {
        return standardUnitKeyword;
    }

    public void setStandardUnitKeyword(String standardUnitKeyword) {
        this.standardUnitKeyword = standardUnitKeyword;
    }

    public String getSaleUnitKeyword() {
        return saleUnitKeyword;
    }

    public void setSaleUnitKeyword(String saleUnitKeyword) {
        this.saleUnitKeyword = saleUnitKeyword;
    }

    public String getPurUnitKeyword() {
        return purUnitKeyword;
    }

    public void setPurUnitKeyword(String purUnitKeyword) {
        this.purUnitKeyword = purUnitKeyword;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public int getOpeningQuantity() {
        return openingQuantity;
    }

    public void setOpeningQuantity(int openingQuantity) {
        this.openingQuantity = openingQuantity;
    }

    public int getTotalSaleQtyByCategory() {
        return totalSaleQtyByCategory;
    }

    public void setTotalSaleQtyByCategory(int totalSaleQtyByCategory) {
        this.totalSaleQtyByCategory = totalSaleQtyByCategory;
    }

    public double getdOpeningQuantity() {
        return dOpeningQuantity;
    }

    public void setdOpeningQuantity(double dOpeningQuantity) {
        this.dOpeningQuantity = dOpeningQuantity;
    }

    public double getdPurQuantity() {
        return dPurQuantity;
    }

    public void setdPurQuantity(double dPurQuantity) {
        this.dPurQuantity = dPurQuantity;
    }

    public double getdSaleQuantity() {
        return dSaleQuantity;
    }

    public void setdSaleQuantity(double dSaleQuantity) {
        this.dSaleQuantity = dSaleQuantity;
    }

    public double getdAdjustQuantity() {
        return dAdjustQuantity;
    }

    public void setdAdjustQuantity(double dAdjustQuantity) {
        this.dAdjustQuantity = dAdjustQuantity;
    }

    int productId;
    int categoryId;
    int trackStock;
    int isTrackStock;
    int isProductUnit;
    int selectUnitId;
    int openingQuantity;
    String productCode;
    String productName;
    String categoryName;
    String selectUnitKeyword;
    int salePrice;
    int purPrice;
    double balQuantity;
    int standardUnitId;
    int saleUnitId;
    int saleUnitQty;
    int standardSaleUnitQty;
    int saleUnitSalePrice;
    int saleUnitPurPrice;
    int purUnitId;
    int purUnitQty;
    int standardPurUnitQty;
    int purUnitSalePrice;
    int purUnitPurPrice;
    String standardUnitKeyword;
    String saleUnitKeyword;
    String purUnitKeyword;
    String unitType;
    int totalSaleQtyByCategory;
    double dOpeningQuantity;
    double dPurQuantity;
    double dSaleQuantity;
    double dAdjustQuantity;

}
