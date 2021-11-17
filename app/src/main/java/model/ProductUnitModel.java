package model;

public class ProductUnitModel {
    public int getPuId() {
        return puId;
    }

    public int getProductId() {
        return productId;
    }

    public int getUnitId() {
        return unitId;
    }

    public int getPuTrackStock() {
        return puTrackStock;
    }

    public int getPuSalePrice() {
        return puSalePrice;
    }

    public int getPuPurPrice() {
        return puPurPrice;
    }

    public void setPuId(int puId) {
        this.puId = puId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public void setPuTrackStock(int puTrackStock) {
        this.puTrackStock = puTrackStock;
    }

    public void setPuSalePrice(int puSalePrice) {
        this.puSalePrice = puSalePrice;
    }

    public void setPuPurPrice(int puPurPrice) {
        this.puPurPrice = puPurPrice;
    }

    int puId;
    int productId;
    int unitId;
    int puTrackStock;

    public int getPuQuantity() {
        return puQuantity;
    }

    public void setPuQuantity(int puQuantity) {
        this.puQuantity = puQuantity;
    }

    int puQuantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    int quantity;
    int puSalePrice,puPurPrice;

    public String getUnitKeyword() {
        return unitKeyword;
    }

    public void setUnitKeyword(String unitName) {
        this.unitKeyword = unitName;
    }

    String unitKeyword;

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    String unitType;
}
