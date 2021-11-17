package model;

public class BillSettingModel {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    String footerMessage1;
    String footerMessage2;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    String remark;

}
