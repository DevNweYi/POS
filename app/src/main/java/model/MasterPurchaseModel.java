package model;

public class MasterPurchaseModel {
    public int getId() {
        return id;
    }

    public int getVoucherNumber() {
        return voucherNumber;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getPayDisPercent() {
        return payDisPercent;
    }

    public int getPayDisAmount() {
        return payDisAmount;
    }

    public int getTotalDisAmount() {
        return TotalDisAmount;
    }

    public int getNetAmount() {
        return netAmount;
    }

    public int getIsCredit() {
        return isCredit;
    }

    public String getDate() {
        return date;
    }

    public String getCreditRemark() {
        return creditRemark;
    }

    int id;

    public void setId(int id) {
        this.id = id;
    }

    public void setVoucherNumber(int voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPayDisPercent(int payDisPercent) {
        this.payDisPercent = payDisPercent;
    }

    public void setPayDisAmount(int payDisAmount) {
        this.payDisAmount = payDisAmount;
    }

    public void setTotalDisAmount(int totalDisAmount) {
        TotalDisAmount = totalDisAmount;
    }

    public void setNetAmount(int netAmount) {
        this.netAmount = netAmount;
    }

    public void setIsCredit(int isCredit) {
        this.isCredit = isCredit;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCreditRemark(String creditRemark) {
        this.creditRemark = creditRemark;
    }

    int voucherNumber;
    int supplierId;
    int totalAmount;
    int payDisPercent;
    int payDisAmount;
    int TotalDisAmount;
    int netAmount;
    int isCredit;

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    int totalQuantity;

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    int userId;

    public int getOpenBillId() {
        return openBillId;
    }

    public void setOpenBillId(int openBillId) {
        this.openBillId = openBillId;
    }

    int openBillId;
    String date;

    public int getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    String supplierName;
    String creditRemark;
    String userName;

    public String getOpenBillRemark() {
        return openBillRemark;
    }

    public void setOpenBillRemark(String openBillRemark) {
        this.openBillRemark = openBillRemark;
    }

    String openBillRemark;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;
    int lastDebtAmount;
    int paidAmount;
    int changeAmount;
    int debtAmount;

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

    public int getIsDebtAmount() {
        return isDebtAmount;
    }

    public void setIsDebtAmount(int isDebtAmount) {
        this.isDebtAmount = isDebtAmount;
    }

    int isDebtAmount;
}
