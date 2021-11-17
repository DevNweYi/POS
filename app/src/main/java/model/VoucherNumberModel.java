package model;

public class VoucherNumberModel {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSaleVoucherNo() {
        return saleVoucherNo;
    }

    public void setSaleVoucherNo(int saleVoucherNo) {
        this.saleVoucherNo = saleVoucherNo;
    }

    public int getPurchaseVoucherNo() {
        return purchaseVoucherNo;
    }

    public void setPurchaseVoucherNo(int purchaseVoucherNo) {
        this.purchaseVoucherNo = purchaseVoucherNo;
    }

    int id;
    int saleVoucherNo;
    int purchaseVoucherNo;

}
