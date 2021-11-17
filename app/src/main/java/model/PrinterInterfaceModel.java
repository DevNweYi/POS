package model;

/**
 * Created by User on 8/23/2018.
 */
public class PrinterInterfaceModel {
    public int getInterfaceId() {
        return interfaceId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    int interfaceId;
    String interfaceName;
}
