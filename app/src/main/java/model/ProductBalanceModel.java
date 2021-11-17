package model;

public class ProductBalanceModel {

    public void setId(int id) {
        this.id = id;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    int id;

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getUnitId() {
        return unitId;
    }

    public double getQuantity() {
        return quantity;
    }

    int productId;
    int unitId;
    double quantity;

}
