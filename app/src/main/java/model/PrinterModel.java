package model;

/**
 * Created by User on 8/23/2018.
 */
public class PrinterModel {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrinterInterfaceId() {
        return printerInterfaceId;
    }

    public void setPrinterInterfaceId(int printerInterfaceId) {
        this.printerInterfaceId = printerInterfaceId;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getPrinterInterfaceName() {
        return printerInterfaceName;
    }

    public void setPrinterInterfaceName(String printerInterfaceName) {
        this.printerInterfaceName = printerInterfaceName;
    }

    public String getNetPrinterIp() {
        return netPrinterIp;
    }

    public void setNetPrinterIp(String netPrinterIp) {
        this.netPrinterIp = netPrinterIp;
    }

    public String getBtPrinterAddress() {
        return btPrinterAddress;
    }

    public void setBtPrinterAddress(String btPrinterAddress) {
        this.btPrinterAddress = btPrinterAddress;
    }

    public String getPaperWidth() {
        return paperWidth;
    }

    public void setPaperWidth(String paperWidth) {
        this.paperWidth = paperWidth;
    }

    int printerInterfaceId;
    String printerName,printerInterfaceName,netPrinterIp,btPrinterAddress,paperWidth;
}
