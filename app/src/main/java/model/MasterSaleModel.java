package model;

public class MasterSaleModel {
    public int getid() {
        return id;
    }

    public int getVoucherNumber() {
        return voucherNumber;
    }

    public int getCustomerId() {
        return customerId;
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

    public String getCustomerName() {
        return customerName;
    }

    public String getCreditRemark() {
        return creditRemark;
    }

    public void setid(int id) {
        this.id = id;
    }

    public void setVoucherNumber(int voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCreditRemark(String creditRemark) {
        this.creditRemark = creditRemark;
    }

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

    public int getOpenBillId() {
        return openBillId;
    }

    public void setOpenBillId(int openBillId) {
        this.openBillId = openBillId;
    }

    public String getOpenBillRemark() {
        return openBillRemark;
    }

    public void setOpenBillRemark(String openBillRemark) {
        this.openBillRemark = openBillRemark;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(int totalSale) {
        this.totalSale = totalSale;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    int id;
    int voucherNumber;
    int customerId;
    int totalAmount;
    int payDisPercent;
    int payDisAmount;
    int TotalDisAmount;
    int netAmount;
    int isCredit;
    int userId;
    int openBillId;
    String date;
    String customerName;
    String creditRemark;
    String userName;
    String openBillRemark;
    String time;
    int lastDebtAmount;
    int paidAmount;
    int changeAmount;
    int debtAmount;
    int isDebtAmount;
    int totalQuantity;
    int totalSale;
    String month;
    String year;
    String monthName;
    String categoryName;
    String productName;
    String productCode;

}
