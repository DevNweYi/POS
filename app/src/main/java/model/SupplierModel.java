package model;

public class SupplierModel {

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

    int supplierId;

    public int getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(int debtAmount) {
        this.debtAmount = debtAmount;
    }

    int debtAmount;
    String mobileNumber;
    String otherMobileNumber;

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setOtherMobileNumber(String otherMobileNumber) {
        this.otherMobileNumber = otherMobileNumber;
    }

    public void setIsAllowCredit(int isAllowCredit) {
        this.isAllowCredit = isAllowCredit;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    int isAllowCredit;
    String supplierName;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getOtherMobileNumber() {
        return otherMobileNumber;
    }

    public int getIsAllowCredit() {
        return isAllowCredit;
    }

    public String getAddress() {
        return address;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    String address;
    String contactName;

}
