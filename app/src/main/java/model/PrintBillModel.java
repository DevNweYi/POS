package model;

import java.util.List;

/**
 * Created by User on 7/30/2018.
 */
public class PrintBillModel {
    public String shopName;
    public String description;
    public String address;
    public String phone;
    public String printDate;
    public String voucherNo;
    public String supplier;
    public String customer;
    public String user;
    public String payType;
    public String footerMessage1;
    public String footerMessage2;
    public String remark;
    public int totalAmount;
    public int discount;
    public int netAmount;

    public int getLastDebtAmount() {
        return lastDebtAmount;
    }

    public void setLastDebtAmount(int lastDebtAmount) {
        this.lastDebtAmount = lastDebtAmount;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(int changeAmount) {
        this.changeAmount = changeAmount;
    }

    public int getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(int debtAmount) {
        this.debtAmount = debtAmount;
    }

    public int lastDebtAmount;
    public int paidAmount;
    public int changeAmount;
    public int debtAmount;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int customerId;

    public int getIsDebtAmount() {
        return isDebtAmount;
    }

    public void setIsDebtAmount(int isDebtAmount) {
        this.isDebtAmount = isDebtAmount;
    }

    public int isDebtAmount;
    public List<TranSaleModel> lstTranSale;
    public List<TranPurchaseModel> lstTranPurchase;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrintDate() {
        return printDate;
    }

    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getFooterMessage1() {
        return footerMessage1;
    }

    public void setFooterMessage1(String footerMessage1) {
        this.footerMessage1 = footerMessage1;
    }

    public String getFooterMessage2() {
        return footerMessage2;
    }

    public void setFooterMessage2(String footerMessage2) {
        this.footerMessage2 = footerMessage2;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(int netAmount) {
        this.netAmount = netAmount;
    }

    public List<TranSaleModel> getLstTranSale() {
        return lstTranSale;
    }

    public void setLstTranSale(List<TranSaleModel> lstTranSale) {
        this.lstTranSale = lstTranSale;
    }

    public List<TranPurchaseModel> getLstTranPurchase() {
        return lstTranPurchase;
    }

    public void setLstTranPurchase(List<TranPurchaseModel> lstTranPurchase) {
        this.lstTranPurchase = lstTranPurchase;
    }
}
