package model;

public class MasterAdjustmentModel {

    public int getMasterAdjustmentId() {
        return masterAdjustmentId;
    }

    public void setMasterAdjustmentId(int masterAdjustmentId) {
        this.masterAdjustmentId = masterAdjustmentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    int masterAdjustmentId;
    String date;
    String time;
    int userId;
    String userName;
    int totalAmount;
    String remark;
}
