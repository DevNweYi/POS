package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import common.SystemInfo;
import model.CategoryModel;
import model.CustomerModel;
import model.MasterAdjustmentModel;
import model.MasterPurchaseModel;
import model.MasterSaleModel;
import model.BillSettingModel;
import model.PayableModel;
import model.PrinterModel;
import model.ProductModel;
import model.ProductBalanceModel;
import model.ProductUnitModel;
import model.ReceivableModel;
import model.ShopSettingModel;
import model.SupplierModel;
import model.TranAdjustmentModel;
import model.TranPurchaseModel;
import model.TranSaleModel;
import model.UnitModel;
import model.UserModel;
import model.VoucherNumberModel;

public class DatabaseAccess {
    private static SQLiteOpenHelper openHelper;
    private static SQLiteDatabase database;
    private static DatabaseAccess instance;
    SystemInfo systemInfo=new SystemInfo();

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * truncate
     */
    public void truncateDataTable(){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM Category");
        database.execSQL("DELETE FROM Customer");
        database.execSQL("DELETE FROM Supplier");
        database.execSQL("DELETE FROM Unit");
        database.execSQL("DELETE FROM User");
        database.execSQL("DELETE FROM Product");
        database.execSQL("DELETE FROM ProductBalance");
        database.execSQL("DELETE FROM ShopSetting");
        database.execSQL("DELETE FROM BillSetting");
    }
    public void truncateTransactionTable(){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TMasterSale");
        database.execSQL("DELETE FROM TTranSale");
        database.execSQL("DELETE FROM TMasterPurchase");
        database.execSQL("DELETE FROM TTranPurchase");
        database.execSQL("DELETE FROM TMasterAdjustment");
        database.execSQL("DELETE FROM TTranAdjustment");
        database.execSQL("DELETE FROM TMasterOpenBill");
        database.execSQL("DELETE FROM TTranOpenBill");
        database.execSQL("DELETE FROM VoucherNumber");
    }
    /**
     * App Registration function
     */
    public void setRegister(){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("IsRegistered", 1);
        database.insert("AppRegistration", null, cv);
    }
    public boolean checkIsRegister(){
        boolean isRegister=false;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT IsRegistered FROM AppRegistration",null);
        if(cur.moveToFirst()) isRegister=true;
        return isRegister;
    }
    /**
     * shop setting function
     */
    public boolean insertUpdateShopSetting(byte[] logo,String shopName,String mobile,String address,String currencySymbol,String description,String otherMobile){
        boolean isExist=false;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ID FROM ShopSetting",null);
        if(cur.moveToFirst()) isExist=true;
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Logo", logo);
        cv.put("ShopName", shopName);
        cv.put("Mobile", mobile);
        cv.put("Address", address);
        cv.put("CurrencySymbol", currencySymbol);
        cv.put("Description", description);
        cv.put("OtherMobile", otherMobile);
        if(!isExist) database.insert("ShopSetting", null, cv);
        else database.update("ShopSetting", cv, null, null);
        return true;
    }
    public boolean updateShopLogo(byte[] logo){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Logo", logo);
        database.update("ShopSetting", cv, null, null);
        return true;
    }
    public boolean importShopSetting(int id,byte[] logo,String shopName,String mobile,String address,String currencySymbol,String description,String otherMobile){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("Logo", logo);
        cv.put("ShopName", shopName);
        cv.put("Mobile", mobile);
        cv.put("Address", address);
        cv.put("CurrencySymbol", currencySymbol);
        cv.put("Description", description);
        cv.put("OtherMobile", otherMobile);
        database.insert("ShopSetting", null, cv);
        return true;
    }
    public ShopSettingModel getShopSettingTableData(){
        ShopSettingModel data=new ShopSettingModel();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ID,Logo,ShopName,Mobile,Address,CurrencySymbol,Description,OtherMobile FROM ShopSetting",null);
        if(cur.moveToFirst()){
            data.setId(cur.getInt(0));
            data.setLogo(cur.getBlob(1));
            data.setShopName(cur.getString(2));
            data.setMobile(cur.getString(3));
            data.setAddress(cur.getString(4));
            data.setCurrencySymbol(cur.getString(5));
            data.setDescription(cur.getString(6));
            data.setOtherMobile(cur.getString(7));
        }
        return data;
    }
    public String getShopName(){
        String shopName="";
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ShopName FROM ShopSetting",null);
        if(cur.moveToFirst()) shopName = cur.getString(0);
        return shopName;
    }
    public boolean insertUpdateTempShopLogo(byte[] logo){
        boolean isExist=false;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ID FROM TempShopLogo",null);
        if(cur.moveToFirst()) isExist=true;
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Logo", logo);
        if(!isExist) database.insert("TempShopLogo", null, cv);
        else database.update("TempShopLogo", cv, null, null);
        return true;
    }
    public byte[] getTempShopLogo(){
        byte[] shopLogo=null;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Logo FROM TempShopLogo",null);
        if(cur.moveToFirst()) shopLogo = cur.getBlob(0);
        return shopLogo;
    }
    public boolean isExistShopSetting(){
        boolean result=false;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ID FROM ShopSetting",null);
        if(cur.moveToFirst()) result=true;
        return result;
    }
    /**
     * bill setting function
     */
    public boolean insertUpdateBillSetting(String footerMessage1,String footerMessage2,String remark){
        boolean isExist=false;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ID FROM BillSetting",null);
        if(cur.moveToFirst()) isExist=true;
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("FooterMessage1", footerMessage1);
        cv.put("FooterMessage2", footerMessage2);
        cv.put("Remark", remark);
        if(!isExist) database.insert("BillSetting", null, cv);
        else database.update("BillSetting", cv, null, null);
        return true;
    }
    public boolean importBillSetting(int id,String footerMessage1,String footerMessage2,String remark){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("FooterMessage1", footerMessage1);
        cv.put("FooterMessage2", footerMessage2);
        cv.put("Remark", remark);
        database.insert("BillSetting", null, cv);
        return true;
    }
    public BillSettingModel getBillSettingTableData(){
        BillSettingModel data=new BillSettingModel();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ID,FooterMessage1,FooterMessage2,Remark FROM BillSetting",null);
        if(cur.moveToFirst()){
            data.setId(cur.getInt(0));
            data.setFooterMessage1(cur.getString(1));
            data.setFooterMessage2(cur.getString(2));
            data.setRemark(cur.getString(3));
        }
        return data;
    }
    /**
     * category function
     */
    public boolean insertCategory(String categoryName){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CategoryName", categoryName);
        database.insert("Category", null, cv);
        return true;
    }
    public boolean importCategory(int categoryId,String categoryName){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CategoryID", categoryId);
        cv.put("CategoryName", categoryName);
        database.insert("Category", null, cv);
        return true;
    }
    public boolean updateCategory(int categoryId,String categoryName) {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CategoryName", categoryName);
        database.update("Category", cv, "CategoryID=" + categoryId, null);
        return true;
    }
    public boolean deleteCategory(int categoryId) {
        boolean result;
        database = openHelper.getWritableDatabase();
        if(isExistProductByCategory(categoryId) == 0){
            database.execSQL("DELETE FROM Category WHERE CategoryID=" + categoryId);
            result=true;
        }else{
            result=false;
        }
        return result;
    }
    private int isExistProductByCategory(int categoryId){
        int count=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Count(ProductID) FROM Product WHERE CategoryID="+categoryId,null);
        if(cur.moveToNext()) count=cur.getInt(0);
        return count;
    }
    public List<CategoryModel> getCategoryTableData(){
        List<CategoryModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT CategoryID,CategoryName FROM Category",null);
        while(cur.moveToNext()){
            CategoryModel data=new CategoryModel();
            data.setCategoryId(cur.getInt(0));
            data.setCategoryName(cur.getString(1));
            list.add(data);
        }
        return list;
    }
    public List<CategoryModel> getCategory(){
        List<CategoryModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT c.CategoryID,CategoryName,COUNT(ProductID) FROM Category c LEFT JOIN Product p ON c.CategoryID=p.CategoryID GROUP BY c.CategoryID,CategoryName",null);
        while(cur.moveToNext()){
            CategoryModel data=new CategoryModel();
            data.setCategoryId(cur.getInt(0));
            data.setCategoryName(cur.getString(1));
            data.setTotalItem(cur.getInt(2));
            data.setSelected(true);
            list.add(data);
        }
        return list;
    }
    public List<CategoryModel> getCategoryWithDefault(String defaultName){
        List<CategoryModel> list=new ArrayList<>();
        CategoryModel data=new CategoryModel();
        database = openHelper.getReadableDatabase();
        data.setCategoryId(0);
        data.setCategoryName(defaultName);
        list.add(data);
        Cursor cur=database.rawQuery("SELECT CategoryID,CategoryName FROM Category",null);
        while(cur.moveToNext()){
            data=new CategoryModel();
            data.setCategoryId(cur.getInt(0));
            data.setCategoryName(cur.getString(1));
            list.add(data);
        }
        return list;
    }
    public List<CategoryModel> getCategoryByFilter(String categoryName){
        List<CategoryModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT CategoryID,CategoryName FROM Category WHERE CategoryName LIKE '%"+categoryName+"%'",null);
        while(cur.moveToNext()){
            CategoryModel data=new CategoryModel();
            data.setCategoryId(cur.getInt(0));
            data.setCategoryName(cur.getString(1));
            list.add(data);
        }
        return list;
    }
    /**
     * user function
     */
    public boolean insertUser(String userName,String mobileNumber,String password){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UserName", userName);
        cv.put("MobileNumber", mobileNumber);
        cv.put("Password", password);
        database.insert("User", null, cv);
        return true;
    }
    public boolean importUser(int userId,String userName,String mobileNumber,String password){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UserID", userId);
        cv.put("UserName", userName);
        cv.put("MobileNumber", mobileNumber);
        cv.put("Password", password);
        database.insert("User", null, cv);
        return true;
    }
    public boolean updateUser(int userId,String userName,String mobileNumber,String password) {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UserName", userName);
        cv.put("MobileNumber", mobileNumber);
        cv.put("Password", password);
        database.update("User", cv, "UserID=" + userId, null);
        return true;
    }
    public boolean deleteUser(int userId) {
        if(checkUserCount()==1)return false;
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM User WHERE UserID=" + userId);
        return true;
    }
    public int isExistUser(int userId,String password){
        int count=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Count(UserID) FROM User WHERE UserID="+userId +" AND Password='"+password+"'",null);
        if(cur.moveToFirst()) count=cur.getInt(0);
        return count;
    }
    public int checkUserCount(){
        int count=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Count(UserID) FROM User",null);
        if(cur.moveToFirst()) count=cur.getInt(0);
        return count;
    }
    public List<UserModel> getUserTableData(){
        List<UserModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT UserID,UserName,MobileNumber,Password FROM User",null);
        while(cur.moveToNext()){
            UserModel data=new UserModel();
            data.setUserId(cur.getInt(0));
            data.setUserName(cur.getString(1));
            data.setMobileNumber(cur.getString(2));
            data.setPassword(cur.getString(3));
            list.add(data);
        }
        return list;
    }
    public List<UserModel> getUserByFilter(String userName){
        List<UserModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT UserID,UserName,MobileNumber,Password FROM User WHERE UserName LIKE '%"+userName+"%'",null);
        while(cur.moveToNext()){
            UserModel data=new UserModel();
            data.setUserId(cur.getInt(0));
            data.setUserName(cur.getString(1));
            data.setMobileNumber(cur.getString(2));
            data.setPassword(cur.getString(3));
            list.add(data);
        }
        return list;
    }
    public UserModel getUserBySignUp(){
        UserModel data=new UserModel();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Max(UserID),UserName FROM User",null);
        if(cur.moveToFirst()){
            data.setUserId(cur.getInt(0));
            data.setUserName(cur.getString(1));
        }
        return data;
    }
    /**
     * unit function
     */
    public boolean insertUnit(String unitName,String unitKeyword){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UnitName", unitName);
        cv.put("UnitKeyword", unitKeyword);
        database.insert("Unit", null, cv);
        return true;
    }
    public boolean importUnit(int unitId,String unitName,String unitKeyword){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UnitID", unitId);
        cv.put("UnitName", unitName);
        cv.put("UnitKeyword", unitKeyword);
        database.insert("Unit", null, cv);
        return true;
    }
    public boolean updateUnit(int unitId,String unitName,String unitKeyword) {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UnitName", unitName);
        cv.put("UnitKeyword", unitKeyword);
        database.update("Unit", cv, "UnitID=" + unitId, null);
        return true;
    }
    public boolean deleteUnit(int unitId) {
        boolean result;
        database = openHelper.getWritableDatabase();
        if(isExistProductByUnit(unitId) == 0){
            database.execSQL("DELETE FROM Unit WHERE UnitID=" + unitId);
            result=true;
        }else{
            result=false;
        }
        return result;
    }
    private int isExistProductByUnit(int unitId){
        int count=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Count(PUID) FROM ProductUnit WHERE UnitID="+unitId,null);
        if(cur.moveToNext()) count=cur.getInt(0);
        return count;
    }
    public List<UnitModel> getUnitTableData(){
        List<UnitModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT UnitID,UnitName,UnitKeyword FROM Unit",null);
        while(cur.moveToNext()){
            UnitModel data=new UnitModel();
            data.setUnitId(cur.getInt(0));
            data.setUnitName(cur.getString(1));
            data.setUnitKeyword(cur.getString(2));
            list.add(data);
        }
        return list;
    }
    public List<UnitModel> getUnitByFilter(String unitKeyword){
        List<UnitModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT UnitID,UnitName,UnitKeyword FROM Unit WHERE UnitKeyword LIKE '%"+unitKeyword+"%'",null);
        while(cur.moveToNext()){
            UnitModel data=new UnitModel();
            data.setUnitId(cur.getInt(0));
            data.setUnitName(cur.getString(1));
            data.setUnitKeyword(cur.getString(2));
            list.add(data);
        }
        return list;
    }
    /**
     * customer function
     */
    public boolean insertCustomer(String customerName,String mobileNumber,String otherMobileNumber,String address,int isAllowCredit,String contactName){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CustomerName", customerName);
        cv.put("ContactName", contactName);
        cv.put("MobileNumber", mobileNumber);
        cv.put("OtherMobileNumber", otherMobileNumber);
        cv.put("Address", address);
        cv.put("IsAllowCredit", isAllowCredit);
        cv.put("DebtAmount", 0);
        database.insert("Customer", null, cv);
        return true;
    }
    public boolean importCustomer(int customerId,String customerName,String mobileNumber,String otherMobileNumber,String address,int isAllowCredit,int debtAmount,String contactName){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CustomerID", customerId);
        cv.put("CustomerName", customerName);
        cv.put("ContactName", contactName);
        cv.put("MobileNumber", mobileNumber);
        cv.put("OtherMobileNumber", otherMobileNumber);
        cv.put("Address", address);
        cv.put("IsAllowCredit", isAllowCredit);
        cv.put("DebtAmount", debtAmount);
        database.insert("Customer", null, cv);
        return true;
    }
    public boolean updateCustomer(int customerId,String customerName,String mobileNumber,String otherMobileNumber,String address,int isAllowCredit,String contactName) {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CustomerName", customerName);
        cv.put("ContactName", contactName);
        cv.put("MobileNumber", mobileNumber);
        cv.put("OtherMobileNumber", otherMobileNumber);
        cv.put("Address", address);
        cv.put("IsAllowCredit", isAllowCredit);
        database.update("Customer", cv, "CustomerID=" + customerId, null);
        return true;
    }
    public boolean deleteCustomer(int customerId) {
        if(!isExistTranByCustomer(customerId)) {
            database = openHelper.getWritableDatabase();
            database.execSQL("DELETE FROM Customer WHERE CustomerID=" + customerId);
            return true;
        }else{
            return false;
        }
    }
    private boolean isExistTranByCustomer(int customerId){
        boolean result=false;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT * FROM TMasterSale WHERE CustomerID="+customerId,null);
        if(cur.moveToFirst()) result=true;
        return result;
    }
    public int isAllowCreditForCustomer(int customerId){
        int result=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT IsAllowCredit FROM Customer WHERE CustomerID="+customerId,null);
        if(cur.moveToFirst())result = cur.getInt(0);
        return result;
    }
    public List<CustomerModel> getCustomerTableData(){
        List<CustomerModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT CustomerID,CustomerName,MobileNumber,OtherMobileNumber,Address,IsAllowCredit,DebtAmount,ContactName FROM Customer",null);
        while(cur.moveToNext()){
            CustomerModel data=new CustomerModel();
            data.setCustomerId(cur.getInt(0));
            data.setCustomerName(cur.getString(1));
            data.setMobileNumber(cur.getString(2));
            data.setOtherMobileNumber(cur.getString(3));
            data.setAddress(cur.getString(4));
            data.setIsAllowCredit(cur.getInt(5));
            data.setDebtAmount(cur.getInt(6));
            data.setContactName(cur.getString(7));
            list.add(data);
        }
        return list;
    }
    public List<CustomerModel> getCustomerWithDefault(){
        List<CustomerModel> list=new ArrayList<>();
        CustomerModel data=new CustomerModel();
        database = openHelper.getReadableDatabase();
        data.setCustomerId(0);
        data.setCustomerName("Walk in Client");
        list.add(data);
        Cursor cur=database.rawQuery("SELECT CustomerID,CustomerName,MobileNumber,OtherMobileNumber,Address,IsAllowCredit,DebtAmount,ContactName FROM Customer",null);
        while(cur.moveToNext()){
            data=new CustomerModel();
            data.setCustomerId(cur.getInt(0));
            data.setCustomerName(cur.getString(1));
            data.setMobileNumber(cur.getString(2));
            data.setOtherMobileNumber(cur.getString(3));
            data.setAddress(cur.getString(4));
            data.setIsAllowCredit(cur.getInt(5));
            data.setDebtAmount(cur.getInt(6));
            data.setContactName(cur.getString(7));
            list.add(data);
        }
        return list;
    }
    public CustomerModel getCustomerByCustomerID(int customerId){
        CustomerModel data=new CustomerModel();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT CustomerID,CustomerName,MobileNumber,OtherMobileNumber,Address,IsAllowCredit,DebtAmount,ContactName FROM Customer WHERE CustomerID=" + customerId,null);
        if(cur.moveToNext()){
            data=new CustomerModel();
            data.setCustomerId(cur.getInt(0));
            data.setCustomerName(cur.getString(1));
            data.setMobileNumber(cur.getString(2));
            data.setOtherMobileNumber(cur.getString(3));
            data.setAddress(cur.getString(4));
            data.setIsAllowCredit(cur.getInt(5));
            data.setDebtAmount(cur.getInt(6));
            data.setContactName(cur.getString(7));
        }
        return data;
    }
    public List<CustomerModel> getCustomerByFilter(String customerName){
        List<CustomerModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT CustomerID,CustomerName,MobileNumber,OtherMobileNumber,Address,IsAllowCredit,DebtAmount,ContactName FROM Customer WHERE CustomerName LIKE '%"+customerName+"%'",null);
        while(cur.moveToNext()){
            CustomerModel data=new CustomerModel();
            data.setCustomerId(cur.getInt(0));
            data.setCustomerName(cur.getString(1));
            data.setMobileNumber(cur.getString(2));
            data.setOtherMobileNumber(cur.getString(3));
            data.setAddress(cur.getString(4));
            data.setIsAllowCredit(cur.getInt(5));
            data.setDebtAmount(cur.getInt(6));
            data.setContactName(cur.getString(7));
            list.add(data);
        }
        return list;
    }
    public int getDebtAmountByCustomer(int customerId){
        int debtAmount=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT DebtAmount FROM Customer WHERE CustomerID=" + customerId,null);
        if(cur.moveToFirst()) debtAmount=cur.getInt(0);
        return debtAmount;
    }
    public void updateDebtAmountByCustomer(int customerId,int debtAmount){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("DebtAmount", debtAmount);
        database.update("Customer", cv, "CustomerID=" + customerId, null);
    }
    public List<CustomerModel> getCustomerDebtAmount(String customerName){
        List<CustomerModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur;
        if(!customerName.equals("")) cur=database.rawQuery("SELECT CustomerID,CustomerName,DebtAmount FROM Customer WHERE DebtAmount!=0 AND CustomerName LIKE '%"+customerName+"%'",null);
        else cur=database.rawQuery("SELECT CustomerID,CustomerName,DebtAmount FROM Customer WHERE DebtAmount!=0",null);
        while(cur.moveToNext()){
            CustomerModel data=new CustomerModel();
            data.setCustomerId(cur.getInt(0));
            data.setCustomerName(cur.getString(1));
            data.setDebtAmount(cur.getInt(2));
            list.add(data);
        }
        return list;
    }
    public int getCustomerTotalDebtAmount(){
        int totalDebtAmount=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Sum(DebtAmount) FROM Customer",null);
        if(cur.moveToFirst())totalDebtAmount=cur.getInt(0);
        return totalDebtAmount;
    }
    /**
     * supplier function
     */
    public boolean insertSupplier(String supplierName,String mobileNumber,String otherMobileNumber,String address,int isAllowCredit,String contactName){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("SupplierName", supplierName);
        cv.put("MobileNumber", mobileNumber);
        cv.put("OtherMobileNumber", otherMobileNumber);
        cv.put("Address", address);
        cv.put("IsAllowCredit", isAllowCredit);
        cv.put("DebtAmount", 0);
        cv.put("ContactName", contactName);
        database.insert("Supplier", null, cv);
        return true;
    }
    public boolean importSupplier(int supplierId,String supplierName,String mobileNumber,String otherMobileNumber,String address,int isAllowCredit,int debtAmount,String contactName){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("SupplierID", supplierId);
        cv.put("SupplierName", supplierName);
        cv.put("MobileNumber", mobileNumber);
        cv.put("OtherMobileNumber", otherMobileNumber);
        cv.put("Address", address);
        cv.put("IsAllowCredit", isAllowCredit);
        cv.put("DebtAmount", debtAmount);
        cv.put("ContactName", contactName);
        database.insert("Supplier", null, cv);
        return true;
    }
    public boolean updateSupplier(int supplierId,String supplierName,String mobileNumber,String otherMobileNumber,String address,int isAllowCredit,String contactName) {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("SupplierName", supplierName);
        cv.put("MobileNumber", mobileNumber);
        cv.put("OtherMobileNumber", otherMobileNumber);
        cv.put("Address", address);
        cv.put("IsAllowCredit", isAllowCredit);
        cv.put("ContactName", contactName);
        database.update("Supplier", cv, "SupplierID=" + supplierId, null);
        return true;
    }
    public boolean deleteSupplier(int supplierId) {
        if(!isExistTranBySupplier(supplierId)) {
            database = openHelper.getWritableDatabase();
            database.execSQL("DELETE FROM Supplier WHERE SupplierID=" + supplierId);
            return true;
        }else{
            return false;
        }
    }
    private boolean isExistTranBySupplier(int supplierId){
        boolean result=false;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT * FROM TMasterPurchase WHERE SupplierID="+supplierId,null);
        if(cur.moveToFirst()) result=true;
        return result;
    }
    public int isAllowCreditForSupplier(int supplierId){
        int result=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT IsAllowCredit FROM Supplier WHERE SupplierID="+supplierId,null);
        if(cur.moveToFirst())result = cur.getInt(0);
        return result;
    }
    public List<SupplierModel> getSupplierTableData(){
        List<SupplierModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT SupplierID,SupplierName,MobileNumber,OtherMobileNumber,Address,IsAllowCredit,DebtAmount,ContactName FROM Supplier",null);
        while(cur.moveToNext()){
            SupplierModel data=new SupplierModel();
            data.setSupplierId(cur.getInt(0));
            data.setSupplierName(cur.getString(1));
            data.setMobileNumber(cur.getString(2));
            data.setOtherMobileNumber(cur.getString(3));
            data.setAddress(cur.getString(4));
            data.setIsAllowCredit(cur.getInt(5));
            data.setDebtAmount(cur.getInt(6));
            data.setContactName(cur.getString(7));
            list.add(data);
        }
        return list;
    }
    public List<SupplierModel> getSupplierWithDefault(String defaultSupplier){
        List<SupplierModel> list=new ArrayList<>();
        SupplierModel data=new SupplierModel();
        database = openHelper.getReadableDatabase();
        data.setSupplierId(0);
        data.setSupplierName(defaultSupplier);
        list.add(data);
        Cursor cur=database.rawQuery("SELECT SupplierID,SupplierName,MobileNumber,OtherMobileNumber,Address,IsAllowCredit,DebtAmount,ContactName FROM Supplier",null);
        while(cur.moveToNext()){
            data=new SupplierModel();
            data.setSupplierId(cur.getInt(0));
            data.setSupplierName(cur.getString(1));
            data.setMobileNumber(cur.getString(2));
            data.setOtherMobileNumber(cur.getString(3));
            data.setAddress(cur.getString(4));
            data.setIsAllowCredit(cur.getInt(5));
            data.setDebtAmount(cur.getInt(6));
            data.setContactName(cur.getString(7));
            list.add(data);
        }
        return list;
    }
    public SupplierModel getSupplierBySupplierID(int supplierId){
        SupplierModel data=new SupplierModel();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT SupplierID,SupplierName,MobileNumber,OtherMobileNumber,Address,IsAllowCredit,DebtAmount,ContactName FROM Supplier WHERE SupplierID=" + supplierId,null);
        if(cur.moveToNext()){
            data=new SupplierModel();
            data.setSupplierId(cur.getInt(0));
            data.setSupplierName(cur.getString(1));
            data.setMobileNumber(cur.getString(2));
            data.setOtherMobileNumber(cur.getString(3));
            data.setAddress(cur.getString(4));
            data.setIsAllowCredit(cur.getInt(5));
            data.setDebtAmount(cur.getInt(6));
            data.setContactName(cur.getString(7));
        }
        return data;
    }
    public List<SupplierModel> getSupplierByFilter(String supplierName){
        List<SupplierModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT SupplierID,SupplierName,MobileNumber,OtherMobileNumber,Address,IsAllowCredit,DebtAmount,ContactName FROM Supplier WHERE SupplierName LIKE '%"+supplierName+"%'",null);
        while(cur.moveToNext()){
            SupplierModel data=new SupplierModel();
            data.setSupplierId(cur.getInt(0));
            data.setSupplierName(cur.getString(1));
            data.setMobileNumber(cur.getString(2));
            data.setOtherMobileNumber(cur.getString(3));
            data.setAddress(cur.getString(4));
            data.setIsAllowCredit(cur.getInt(5));
            data.setDebtAmount(cur.getInt(6));
            data.setContactName(cur.getString(7));
            list.add(data);
        }
        return list;
    }
    public int getDebtAmountBySupplier(int supplierId){
        int debtAmount=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT DebtAmount FROM Supplier WHERE SupplierID=" + supplierId,null);
        if(cur.moveToFirst()) debtAmount=cur.getInt(0);
        return debtAmount;
    }
    public void updateDebtAmountBySupplier(int supplierId,int debtAmount){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("DebtAmount", debtAmount);
        database.update("Supplier", cv, "SupplierID=" + supplierId, null);
    }
    public List<SupplierModel> getSupplierDebtAmount(String supplierName){
        List<SupplierModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur;
        if(!supplierName.equals("")) cur=database.rawQuery("SELECT SupplierID,SupplierName,DebtAmount FROM Supplier WHERE DebtAmount!=0 AND SupplierName LIKE '%"+supplierName+"%'",null);
        else cur=database.rawQuery("SELECT SupplierID,SupplierName,DebtAmount FROM Supplier WHERE DebtAmount!=0",null);
        while(cur.moveToNext()){
            SupplierModel data=new SupplierModel();
            data.setSupplierId(cur.getInt(0));
            data.setSupplierName(cur.getString(1));
            data.setDebtAmount(cur.getInt(2));
            list.add(data);
        }
        return list;
    }
    public int getSupplierTotalDebtAmount(){
        int totalDebtAmount=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Sum(DebtAmount) FROM Supplier",null);
        if(cur.moveToFirst())totalDebtAmount=cur.getInt(0);
        return totalDebtAmount;
    }
    /**
     * product function
     */
    public boolean insertProduct(String productCode,String productName,int categoryId,int salePrice,int purPrice,int quantity,int isTrackStock,int trackStock,int isProductUnit,int stanUnitId,int saleUnitId,int saleUnitQty,int stanSaleUnitQty,int saleUnitSalePrice,int saleUnitPurPrice,int purUnitId,int purUnitQty,int stanPurUnitQty,int purUnitSalePrice,int purUnitPurPrice){
        int maxProductId=0;
        SQLiteDatabase dbWrite = openHelper.getWritableDatabase();
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("ProductCode", productCode);
        cv.put("ProductName", productName);
        cv.put("CategoryID", categoryId);
        cv.put("IsProductUnit", isProductUnit);
        cv.put("SalePrice", salePrice);
        cv.put("PurPrice", purPrice);
        cv.put("Quantity", quantity);
        cv.put("IsTrackStock", isTrackStock);
        cv.put("TrackStock", trackStock);
        cv.put("StandardUnitID",stanUnitId);
        cv.put("SaleUnitID",saleUnitId);
        cv.put("SaleUnitQty",saleUnitQty);
        cv.put("StandardSaleUnitQty",stanSaleUnitQty);
        cv.put("SaleUnitSalePrice",saleUnitSalePrice);
        cv.put("SaleUnitPurPrice",saleUnitPurPrice);
        cv.put("PurUnitID",purUnitId);
        cv.put("PurUnitQty",purUnitQty);
        cv.put("StandardPurUnitQty",stanPurUnitQty);
        cv.put("PurUnitSalePrice",purUnitSalePrice);
        cv.put("PurUnitPurPrice",purUnitPurPrice);
        dbWrite.insert("Product", null, cv);

        Cursor cur=dbRead.rawQuery("SELECT MAX(ProductID) FROM Product",null);
        if(cur.moveToFirst())maxProductId=cur.getInt(0);

        if(isProductUnit == 1){
            cur=dbRead.rawQuery("SELECT ProductID,StandardUnitID,Quantity FROM Product WHERE ProductID="+maxProductId,null);
            while(cur.moveToNext()){
                insertProductBalance(cur.getInt(0),cur.getInt(1),cur.getInt(2));
            }
        }else{
            cur=dbRead.rawQuery("SELECT ProductID,Quantity FROM Product WHERE ProductID="+maxProductId,null);
            if(cur.moveToFirst()){
                insertProductBalance(cur.getInt(0),0,cur.getInt(1));
            }
        }
        return true;
    }
    public void insertProductBalance(int productId,int unitId,double productQuantity){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ProductID", productId);
        cv.put("UnitID", unitId);
        cv.put("Quantity", productQuantity);
        database.insert("ProductBalance", null, cv);
    }
    private void updateProductBalance(int productId,double productQuantity){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Quantity", productQuantity);
        database.update("ProductBalance", cv, "ProductID=" + productId, null);
    }
    private void updateProductBalance(int productId, int unitId){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UnitID", unitId);
        database.update("ProductBalance", cv, "ProductID=" + productId, null);
    }
    private boolean deleteProductBalance(int productId) {
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM ProductBalance WHERE ProductID=" + productId);
        return true;
    }
    public boolean importProduct(int productId,String productCode,String productName,int categoryId,int salePrice,int purPrice,int quantity,int isTrackStock,int trackStock,int isProductUnit,int stanUnitId,int saleUnitId,int saleUnitQty,int stanSaleUnitQty,int saleUnitSalePrice,int saleUnitPurPrice,int purUnitId,int purUnitQty,int stanPurUnitQty,int purUnitSalePrice,int purUnitPurPrice){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ProductID", productId);
        cv.put("ProductCode", productCode);
        cv.put("ProductName", productName);
        cv.put("CategoryID", categoryId);
        cv.put("IsProductUnit", isProductUnit);
        cv.put("SalePrice", salePrice);
        cv.put("PurPrice", purPrice);
        cv.put("Quantity", quantity);
        cv.put("IsTrackStock", isTrackStock);
        cv.put("TrackStock", trackStock);
        cv.put("StandardUnitID",stanUnitId);
        cv.put("SaleUnitID",saleUnitId);
        cv.put("SaleUnitQty",saleUnitQty);
        cv.put("StandardSaleUnitQty",stanSaleUnitQty);
        cv.put("SaleUnitSalePrice",saleUnitSalePrice);
        cv.put("SaleUnitPurPrice",saleUnitPurPrice);
        cv.put("PurUnitID",purUnitId);
        cv.put("PurUnitQty",purUnitQty);
        cv.put("StandardPurUnitQty",stanPurUnitQty);
        cv.put("PurUnitSalePrice",purUnitSalePrice);
        cv.put("PurUnitPurPrice",purUnitPurPrice);
        database.insert("Product", null, cv);
        return true;
    }
    public boolean importProductBalance(int id, int productId, int unitId, double productQuantity){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("ProductID", productId);
        cv.put("UnitID", unitId);
        cv.put("Quantity", productQuantity);
        database.insert("ProductBalance", null, cv);
        return true;
    }
    public boolean updateProduct(int productId,String productCode,String productName,int categoryId,int salePrice,int purPrice,int quantity,int isTrackStock,int trackStock,int isProductUnit,int stanUnitId,int saleUnitId,int saleUnitQty,int stanSaleUnitQty,int saleUnitSalePrice,int saleUnitPurPrice,int purUnitId,int purUnitQty,int stanPurUnitQty,int purUnitSalePrice,int purUnitPurPrice) {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ProductCode", productCode);
        cv.put("ProductName", productName);
        cv.put("CategoryID", categoryId);
        cv.put("IsProductUnit", isProductUnit);
        cv.put("SalePrice", salePrice);
        cv.put("PurPrice", purPrice);
        cv.put("Quantity", quantity);
        cv.put("IsTrackStock", isTrackStock);
        cv.put("TrackStock", trackStock);
        cv.put("StandardUnitID",stanUnitId);
        cv.put("SaleUnitID",saleUnitId);
        cv.put("SaleUnitQty",saleUnitQty);
        cv.put("StandardSaleUnitQty",stanSaleUnitQty);
        cv.put("SaleUnitSalePrice",saleUnitSalePrice);
        cv.put("SaleUnitPurPrice",saleUnitPurPrice);
        cv.put("PurUnitID",purUnitId);
        cv.put("PurUnitQty",purUnitQty);
        cv.put("StandardPurUnitQty",stanPurUnitQty);
        cv.put("PurUnitSalePrice",purUnitSalePrice);
        cv.put("PurUnitPurPrice",purUnitPurPrice);
        database.update("Product", cv, "ProductID=" + productId, null);
        updateProductBalance(productId,stanUnitId);
        return true;
    }
    public boolean deleteProduct(int productId) {
        boolean result;
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM Product WHERE ProductID=" + productId);
        database.execSQL("DELETE FROM ProductBalance WHERE ProductID=" + productId);
        result=true;
        return result;
    }
    public boolean isAlreadyExistProductCode(String productCode){
        int count=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT count(ProductID) FROM Product WHERE ProductCode="+productCode,null);
        if(cur.moveToFirst()) count=cur.getInt(0);
        if(count == 0)return false;
        else return true;
    }
    public boolean isAlreadyExistProductCodeForEdit(int productId,String productCode){
        int count=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT count(ProductID) FROM Product WHERE ProductCode =" + productCode + " AND ProductID !=" + productId,null);
        if(cur.moveToFirst()) count=cur.getInt(0);
        if(count == 0)return false;
        else return true;
    }
    public List<ProductModel> getProductTableData(){
        List<ProductModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ProductID,ProductCode,ProductName,CategoryID,SalePrice,PurPrice,IsTrackStock,TrackStock,IsProductUnit,Quantity,StandardUnitID,SaleUnitID,SaleUnitQty,StandardSaleUnitQty,SaleUnitSalePrice,SaleUnitPurPrice,PurUnitID,PurUnitQty,StandardPurUnitQty,PurUnitSalePrice,PurUnitPurPrice FROM Product",null);
        while(cur.moveToNext()){
            ProductModel data=new ProductModel();
            data.setProductId(cur.getInt(0));
            data.setProductCode(cur.getString(1));
            data.setProductName(cur.getString(2));
            data.setCategoryId(cur.getInt(3));
            data.setSalePrice(cur.getInt(4));
            data.setPurPrice(cur.getInt(5));
            data.setIsTrackStock(cur.getInt(6));
            data.setTrackStock(cur.getInt(7));
            data.setIsProductUnit(cur.getInt(8));
            data.setOpeningQuantity(cur.getInt(9));
            data.setStandardUnitId(cur.getInt(10));
            data.setSaleUnitId(cur.getInt(11));
            data.setSaleUnitQty(cur.getInt(12));
            data.setStandardSaleUnitQty(cur.getInt(13));
            data.setSaleUnitSalePrice(cur.getInt(14));
            data.setSaleUnitPurPrice(cur.getInt(15));
            data.setPurUnitId(cur.getInt(16));
            data.setPurUnitQty(cur.getInt(17));
            data.setStandardPurUnitQty(cur.getInt(18));
            data.setPurUnitSalePrice(cur.getInt(19));
            data.setPurUnitPurPrice(cur.getInt(20));
            list.add(data);
        }
        return list;
    }
    public List<ProductBalanceModel> getProductBalanceTableData(){
        List<ProductBalanceModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ID,ProductID,UnitID,Quantity FROM ProductBalance",null);
        while(cur.moveToNext()){
            ProductBalanceModel data=new ProductBalanceModel();
            data.setId(cur.getInt(0));
            data.setProductId(cur.getInt(1));
            data.setUnitId(cur.getInt(2));
            data.setQuantity(cur.getDouble(3));
            list.add(data);
        }
        return list;
    }
    public List<ProductModel> getProduct(){
        List<ProductModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ProductID,ProductCode,ProductName,p.CategoryID,SalePrice,PurPrice,IsTrackStock,TrackStock,IsProductUnit,Quantity,c.CategoryName,StandardUnitID,SaleUnitID,SaleUnitQty,StandardSaleUnitQty,SaleUnitSalePrice,SaleUnitPurPrice,PurUnitID,PurUnitQty,StandardPurUnitQty,PurUnitSalePrice,PurUnitPurPrice FROM Product p INNER JOIN Category c ON p.CategoryID=c.CategoryID",null);
        while(cur.moveToNext()){
            ProductModel data=new ProductModel();
            data.setProductId(cur.getInt(0));
            data.setProductCode(cur.getString(1));
            data.setProductName(cur.getString(2));
            data.setCategoryId(cur.getInt(3));
            data.setSalePrice(cur.getInt(4));
            data.setPurPrice(cur.getInt(5));
            data.setIsTrackStock(cur.getInt(6));
            data.setTrackStock(cur.getInt(7));
            data.setIsProductUnit(cur.getInt(8));
            data.setOpeningQuantity(cur.getInt(9));
            data.setCategoryName(cur.getString(10));
            data.setStandardUnitId(cur.getInt(11));
            data.setSaleUnitId(cur.getInt(12));
            data.setSaleUnitQty(cur.getInt(13));
            data.setStandardSaleUnitQty(cur.getInt(14));
            data.setSaleUnitSalePrice(cur.getInt(15));
            data.setSaleUnitPurPrice(cur.getInt(16));
            data.setPurUnitId(cur.getInt(17));
            data.setPurUnitQty(cur.getInt(18));
            data.setStandardPurUnitQty(cur.getInt(19));
            data.setPurUnitSalePrice(cur.getInt(20));
            data.setPurUnitPurPrice(cur.getInt(21));
            list.add(data);
        }
        return list;
    }
    public ProductModel getProductByProductID(int productId){
        ProductModel data=new ProductModel();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ProductCode,ProductName,CategoryID,SalePrice,PurPrice,IsTrackStock,TrackStock,IsProductUnit,Quantity,StandardUnitID,SaleUnitID,SaleUnitQty,StandardSaleUnitQty,SaleUnitSalePrice,SaleUnitPurPrice,PurUnitID,PurUnitQty,StandardPurUnitQty,PurUnitSalePrice,PurUnitPurPrice FROM Product WHERE ProductID=" + productId,null);
        if(cur.moveToFirst()){
            data=new ProductModel();
            data.setProductCode(cur.getString(0));
            data.setProductName(cur.getString(1));
            data.setCategoryId(cur.getInt(2));
            data.setSalePrice(cur.getInt(3));
            data.setPurPrice(cur.getInt(4));
            data.setIsTrackStock(cur.getInt(5));
            data.setTrackStock(cur.getInt(6));
            data.setIsProductUnit(cur.getInt(7));
            data.setOpeningQuantity(cur.getInt(8));
            data.setStandardUnitId(cur.getInt(9));
            data.setSaleUnitId(cur.getInt(10));
            data.setSaleUnitQty(cur.getInt(11));
            data.setStandardSaleUnitQty(cur.getInt(12));
            data.setSaleUnitSalePrice(cur.getInt(13));
            data.setSaleUnitPurPrice(cur.getInt(14));
            data.setPurUnitId(cur.getInt(15));
            data.setPurUnitQty(cur.getInt(16));
            data.setStandardPurUnitQty(cur.getInt(17));
            data.setPurUnitSalePrice(cur.getInt(18));
            data.setPurUnitPurPrice(cur.getInt(19));

            if(cur.getInt(7)==1) {
                Cursor cUnitKeyword = database.rawQuery("SELECT UnitKeyword FROM Unit WHERE UnitID=" + cur.getInt(9), null);
                if (cUnitKeyword.moveToFirst()) data.setStandardUnitKeyword(cUnitKeyword.getString(0));
                cUnitKeyword = database.rawQuery("SELECT UnitKeyword FROM Unit WHERE UnitID=" + cur.getInt(10), null);
                if (cUnitKeyword.moveToFirst()) data.setSaleUnitKeyword(cUnitKeyword.getString(0));
                cUnitKeyword = database.rawQuery("SELECT UnitKeyword FROM Unit WHERE UnitID=" + cur.getInt(15), null);
                if (cUnitKeyword.moveToFirst()) data.setPurUnitKeyword(cUnitKeyword.getString(0));
            }
        }
        return data;
    }
    public List<ProductUnitModel> getProductUnitByProductID(int productId,int moduleType){
        String standardUnitKeyword="",saleUnitKeyword="",purUnitKeyword="";
        List<ProductUnitModel> lstProductUnit=new ArrayList<>();
        ProductUnitModel productUnitModel;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT SalePrice,PurPrice,StandardUnitID,SaleUnitID,SaleUnitSalePrice,SaleUnitPurPrice,PurUnitID,PurUnitSalePrice,PurUnitPurPrice FROM Product WHERE ProductID=" + productId,null);
        if(cur.moveToFirst()){
            int salePrice=cur.getInt(0);
            int purPrice=cur.getInt(1);
            int standardUnitId=cur.getInt(2);
            int saleUnitId=cur.getInt(3);
            int saleUnitSalePrice=cur.getInt(4);
            int saleUnitPurPrice=cur.getInt(5);
            int purUnitId=cur.getInt(6);
            int purUnitSalePrice=cur.getInt(7);
            int purUnitPurPrice=cur.getInt(8);

            Cursor cUnitKeyword = database.rawQuery("SELECT UnitKeyword FROM Unit WHERE UnitID=" + standardUnitId, null);
            if (cUnitKeyword.moveToFirst()) standardUnitKeyword=cUnitKeyword.getString(0);
            cUnitKeyword = database.rawQuery("SELECT UnitKeyword FROM Unit WHERE UnitID=" + saleUnitId, null);
            if (cUnitKeyword.moveToFirst())saleUnitKeyword=cUnitKeyword.getString(0);
            cUnitKeyword = database.rawQuery("SELECT UnitKeyword FROM Unit WHERE UnitID=" + purUnitId, null);
            if (cUnitKeyword.moveToFirst()) purUnitKeyword=cUnitKeyword.getString(0);

            if(moduleType==systemInfo.SaleModule) {
                if (saleUnitId != 0) {
                    productUnitModel=new ProductUnitModel();
                    productUnitModel.setUnitId(saleUnitId);
                    productUnitModel.setUnitKeyword(saleUnitKeyword);
                    productUnitModel.setPuSalePrice(saleUnitSalePrice);
                    productUnitModel.setPuPurPrice(saleUnitPurPrice);
                    productUnitModel.setUnitType(systemInfo.SaleUnit);
                    lstProductUnit.add(productUnitModel);
                }
                if (standardUnitId != 0) {
                    productUnitModel = new ProductUnitModel();
                    productUnitModel.setUnitId(standardUnitId);
                    productUnitModel.setUnitKeyword(standardUnitKeyword);
                    productUnitModel.setPuSalePrice(salePrice);
                    productUnitModel.setPuPurPrice(purPrice);
                    productUnitModel.setUnitType(systemInfo.StandardUnit);
                    lstProductUnit.add(productUnitModel);
                }
                if (purUnitId != 0) {
                    productUnitModel = new ProductUnitModel();
                    productUnitModel.setUnitId(purUnitId);
                    productUnitModel.setUnitKeyword(purUnitKeyword);
                    productUnitModel.setPuSalePrice(purUnitSalePrice);
                    productUnitModel.setPuPurPrice(purUnitPurPrice);
                    productUnitModel.setUnitType(systemInfo.PurchaseUnit);
                    lstProductUnit.add(productUnitModel);
                }
            }

            if(moduleType==systemInfo.PurchaseModule) {
                if (purUnitId != 0) {
                    productUnitModel = new ProductUnitModel();
                    productUnitModel.setUnitId(purUnitId);
                    productUnitModel.setUnitKeyword(purUnitKeyword);
                    productUnitModel.setPuSalePrice(purUnitSalePrice);
                    productUnitModel.setPuPurPrice(purUnitPurPrice);
                    productUnitModel.setUnitType(systemInfo.PurchaseUnit);
                    lstProductUnit.add(productUnitModel);
                }
                if(standardUnitId!=0){
                    productUnitModel = new ProductUnitModel();
                    productUnitModel.setUnitId(standardUnitId);
                    productUnitModel.setUnitKeyword(standardUnitKeyword);
                    productUnitModel.setPuSalePrice(salePrice);
                    productUnitModel.setPuPurPrice(purPrice);
                    productUnitModel.setUnitType(systemInfo.StandardUnit);
                    lstProductUnit.add(productUnitModel);
                }
                if (saleUnitId != 0) {
                    productUnitModel=new ProductUnitModel();
                    productUnitModel.setUnitId(saleUnitId);
                    productUnitModel.setUnitKeyword(saleUnitKeyword);
                    productUnitModel.setPuSalePrice(saleUnitSalePrice);
                    productUnitModel.setPuPurPrice(saleUnitPurPrice);
                    productUnitModel.setUnitType(systemInfo.SaleUnit);
                    lstProductUnit.add(productUnitModel);
                }
            }
        }
        return lstProductUnit;
    }
    public List<ProductModel> getProductByFilter(String productName,int categoryId){
        List<ProductModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur = null;
        if(productName.length() == 0 && categoryId != 0)
            cur=database.rawQuery("SELECT ProductID,ProductCode,ProductName,p.CategoryID,SalePrice,PurPrice,IsTrackStock,TrackStock,IsProductUnit,Quantity,c.CategoryName,StandardUnitID,SaleUnitID,SaleUnitQty,StandardSaleUnitQty,SaleUnitSalePrice,SaleUnitPurPrice,PurUnitID,PurUnitQty,StandardPurUnitQty,PurUnitSalePrice,PurUnitPurPrice FROM Product p INNER JOIN Category c ON p.CategoryID=c.CategoryID WHERE p.CategoryID=" + categoryId,null);
        else if(categoryId == 0 && productName.length() != 0)
            cur=database.rawQuery("SELECT ProductID,ProductCode,ProductName,p.CategoryID,SalePrice,PurPrice,IsTrackStock,TrackStock,IsProductUnit,Quantity,c.CategoryName,StandardUnitID,SaleUnitID,SaleUnitQty,StandardSaleUnitQty,SaleUnitSalePrice,SaleUnitPurPrice,PurUnitID,PurUnitQty,StandardPurUnitQty,PurUnitSalePrice,PurUnitPurPrice FROM Product p INNER JOIN Category c ON p.CategoryID=c.CategoryID WHERE ProductName LIKE '%"+productName+"%'",null);
        else if(categoryId != 0 && productName.length() != 0)
            cur=database.rawQuery("SELECT ProductID,ProductCode,ProductName,p.CategoryID,SalePrice,PurPrice,IsTrackStock,TrackStock,IsProductUnit,Quantity,c.CategoryName,StandardUnitID,SaleUnitID,SaleUnitQty,StandardSaleUnitQty,SaleUnitSalePrice,SaleUnitPurPrice,PurUnitID,PurUnitQty,StandardPurUnitQty,PurUnitSalePrice,PurUnitPurPrice FROM Product p INNER JOIN Category c ON p.CategoryID=c.CategoryID WHERE ProductName LIKE '%"+productName+"%' AND p.CategoryID=" + categoryId,null);
        //else
            //cur=database.rawQuery("SELECT ProductID,ProductCode,ProductName,p.CategoryID,SalePrice,PurPrice,IsTrackStock,TrackStock,IsProductUnit,Quantity,c.CategoryName,StandardUnitID,SaleUnitID,SaleUnitQty,StandardSaleUnitQty,SaleUnitSalePrice,SaleUnitPurPrice,PurUnitID,PurUnitQty,StandardPurUnitQty,PurUnitSalePrice,PurUnitPurPrice FROM Product p INNER JOIN Category c ON p.CategoryID=c.CategoryID WHERE ProductName LIKE '%"+productName+"%' OR p.CategoryID=" + categoryId,null);
        if(cur != null) {
            while (cur.moveToNext()) {
                ProductModel data = new ProductModel();
                data.setProductId(cur.getInt(0));
                data.setProductCode(cur.getString(1));
                data.setProductName(cur.getString(2));
                data.setCategoryId(cur.getInt(3));
                data.setSalePrice(cur.getInt(4));
                data.setPurPrice(cur.getInt(5));
                data.setIsTrackStock(cur.getInt(6));
                data.setTrackStock(cur.getInt(7));
                data.setIsProductUnit(cur.getInt(8));
                data.setOpeningQuantity(cur.getInt(9));
                data.setCategoryName(cur.getString(10));
                data.setStandardUnitId(cur.getInt(11));
                data.setSaleUnitId(cur.getInt(12));
                data.setSaleUnitQty(cur.getInt(13));
                data.setStandardSaleUnitQty(cur.getInt(14));
                data.setSaleUnitSalePrice(cur.getInt(15));
                data.setSaleUnitPurPrice(cur.getInt(16));
                data.setPurUnitId(cur.getInt(17));
                data.setPurUnitQty(cur.getInt(18));
                data.setStandardPurUnitQty(cur.getInt(19));
                data.setPurUnitSalePrice(cur.getInt(20));
                data.setPurUnitPurPrice(cur.getInt(21));
                list.add(data);
            }
        }
        return list;
    }
    public List<ProductModel> getProductByCategoryOrKeyword(int categoryId,String keyword,boolean isSearch,boolean isWithDefault,int moduleType){
        List<ProductModel> lstProduct=new ArrayList<>();
        int standardUnitId,saleUnitId,saleUnitQty,standardSaleUnitQty,saleUnitSalePrice,saleUnitPurPrice,purUnitId,purUnitQty,standardPurUnitQty,purUnitSalePrice,purUnitPurPrice;
        String standardUnitKeyword="",saleUnitKeyword="",purUnitKeyword="";
        ProductModel data=new ProductModel();
        database = openHelper.getReadableDatabase();
        if(isWithDefault) {
            data.setProductId(0);
            data.setProductName("ကုန်ပစ္စည်းရွေးရန်");
            lstProduct.add(data);
        }
        Cursor cur;
        if(isSearch)
            cur=database.rawQuery("SELECT ProductID,ProductCode,ProductName,CategoryID,SalePrice,PurPrice,IsTrackStock,TrackStock,IsProductUnit,Quantity,StandardUnitID,SaleUnitID,SaleUnitQty,StandardSaleUnitQty,SaleUnitSalePrice,SaleUnitPurPrice,PurUnitID,PurUnitQty,StandardPurUnitQty,PurUnitSalePrice,PurUnitPurPrice FROM Product WHERE ProductName LIKE '%"+keyword+"%'",null);
        else
            cur=database.rawQuery("SELECT ProductID,ProductCode,ProductName,CategoryID,SalePrice,PurPrice,IsTrackStock,TrackStock,IsProductUnit,Quantity,StandardUnitID,SaleUnitID,SaleUnitQty,StandardSaleUnitQty,SaleUnitSalePrice,SaleUnitPurPrice,PurUnitID,PurUnitQty,StandardPurUnitQty,PurUnitSalePrice,PurUnitPurPrice FROM Product WHERE CategoryID=" + categoryId,null);
        while(cur.moveToNext()){
            data=new ProductModel();
            data.setProductId(cur.getInt(0));
            data.setProductCode(cur.getString(1));
            data.setProductName(cur.getString(2));
            data.setCategoryId(cur.getInt(3));
            data.setSalePrice(cur.getInt(4));
            data.setPurPrice(cur.getInt(5));
            data.setIsTrackStock(cur.getInt(6));
            data.setTrackStock(cur.getInt(7));
            data.setIsProductUnit(cur.getInt(8));
            data.setOpeningQuantity(cur.getInt(9));
            if(cur.getInt(8) == 1){
                standardUnitId=cur.getInt(10);
                saleUnitId=cur.getInt(11);
                saleUnitQty=cur.getInt(12);
                standardSaleUnitQty=cur.getInt(13);
                saleUnitSalePrice=cur.getInt(14);
                saleUnitPurPrice=cur.getInt(15);
                purUnitId=cur.getInt(16);
                purUnitQty=cur.getInt(17);
                standardPurUnitQty=cur.getInt(18);
                purUnitSalePrice=cur.getInt(19);
                purUnitPurPrice=cur.getInt(20);

                Cursor cUnitKeyword=database.rawQuery("SELECT UnitKeyword FROM Unit WHERE UnitID=" + standardUnitId,null);
                if(cUnitKeyword.moveToFirst())standardUnitKeyword=cUnitKeyword.getString(0);
                cUnitKeyword=database.rawQuery("SELECT UnitKeyword FROM Unit WHERE UnitID=" + saleUnitId,null);
                if(cUnitKeyword.moveToFirst())saleUnitKeyword=cUnitKeyword.getString(0);
                cUnitKeyword=database.rawQuery("SELECT UnitKeyword FROM Unit WHERE UnitID=" + purUnitId,null);
                if(cUnitKeyword.moveToFirst())purUnitKeyword=cUnitKeyword.getString(0);

                if(moduleType==systemInfo.SaleModule) {         // for sale module
                    if (saleUnitId != 0) {
                        data.setSelectUnitId(saleUnitId);
                        data.setSelectUnitKeyword(saleUnitKeyword);
                        data.setSalePrice(saleUnitSalePrice);
                        data.setUnitType(systemInfo.SaleUnit);
                    }else if (standardUnitId != 0) {
                        data.setSelectUnitId(standardUnitId);
                        data.setSelectUnitKeyword(standardUnitKeyword);
                        data.setSalePrice(cur.getInt(4));
                        data.setUnitType(systemInfo.StandardUnit);
                    }else if (purUnitId != 0) {
                        data.setSelectUnitId(purUnitId);
                        data.setSelectUnitKeyword(purUnitKeyword);
                        data.setSalePrice(purUnitSalePrice);
                        data.setUnitType(systemInfo.PurchaseUnit);
                    }
                }

                if(moduleType==systemInfo.PurchaseModule) {         // for purchase module
                    if (purUnitId != 0) {
                        data.setSelectUnitId(purUnitId);
                        data.setSelectUnitKeyword(purUnitKeyword);
                        data.setPurPrice(purUnitPurPrice);
                        data.setUnitType(systemInfo.PurchaseUnit);
                    } else if(standardUnitId!=0){
                        data.setSelectUnitId(standardUnitId);
                        data.setSelectUnitKeyword(standardUnitKeyword);
                        data.setPurPrice(cur.getInt(5));
                        data.setUnitType(systemInfo.StandardUnit);
                    } else if (saleUnitId != 0) {
                        data.setSelectUnitId(saleUnitId);
                        data.setSelectUnitKeyword(saleUnitKeyword);
                        data.setPurPrice(saleUnitPurPrice);
                        data.setUnitType(systemInfo.SaleUnit);
                    }
                }
            }
            lstProduct.add(data);
        }
        return lstProduct;
    }
    public List<ProductModel> getProductBalance(String productName,int categoryId){
        List<ProductModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur = null;
        if(productName.length() == 0 && categoryId != 0)
            cur=database.rawQuery("SELECT p.ProductName,u.UnitKeyword,bal.Quantity FROM ProductBalance bal INNER JOIN Product p ON bal.ProductID=p.ProductID LEFT JOIN Unit u ON bal.UnitID=u.UnitID WHERE p.CategoryID=" + categoryId + " ORDER BY p.ProductID",null);
        else if(categoryId == 0 && productName.length() != 0)
            cur=database.rawQuery("SELECT p.ProductName,u.UnitKeyword,bal.Quantity FROM ProductBalance bal INNER JOIN Product p ON bal.ProductID=p.ProductID LEFT JOIN Unit u ON bal.UnitID=u.UnitID WHERE p.ProductName LIKE '%"+productName+"%'" + " ORDER BY p.ProductID",null);
        else if(categoryId != 0 && productName.length() != 0)
            cur=database.rawQuery("SELECT p.ProductName,u.UnitKeyword,bal.Quantity FROM ProductBalance bal INNER JOIN Product p ON bal.ProductID=p.ProductID LEFT JOIN Unit u ON bal.UnitID=u.UnitID WHERE p.ProductName LIKE '%"+productName+"%' AND p.CategoryID=" + categoryId + " ORDER BY p.ProductID",null);
        //else
            //cur=database.rawQuery("SELECT p.ProductName,u.UnitKeyword,bal.Quantity FROM ProductBalance bal INNER JOIN Product p ON bal.ProductID=p.ProductID LEFT JOIN Unit u ON bal.UnitID=u.UnitID WHERE p.ProductName LIKE '%"+productName+"%' OR p.CategoryID=" + categoryId + " ORDER BY p.ProductID",null);
        if(cur != null){
            while(cur.moveToNext()){
                ProductModel data=new ProductModel();
                data.setProductName(cur.getString(0));
                data.setSelectUnitKeyword(cur.getString(1));
                data.setBalQuantity(cur.getDouble(2));
                list.add(data);
            }
        }
        return list;
    }
    public List<ProductModel> getTrackProduct(String productName,int categoryId){
        int productId,unitId;
        List<ProductModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        SQLiteDatabase dbWrite = openHelper.getWritableDatabase();

        dbWrite.execSQL("DELETE FROM TempTrackProduct");

        Cursor cur=database.rawQuery("SELECT ProductID,StandardUnitID,TrackStock FROM Product WHERE IsTrackStock = 1",null);
        while(cur.moveToNext()){
            ContentValues cv = new ContentValues();
            cv.put("ProductID", cur.getInt(0));
            cv.put("UnitID", cur.getInt(1));
            cv.put("TrackQty", cur.getInt(2));
            cv.put("BalQty", 0);
            dbWrite.insert("TempTrackProduct", null, cv);
        }

        cur=database.rawQuery("SELECT ProductID,UnitID,Quantity FROM ProductBalance",null);
        while(cur.moveToNext()){
            productId=cur.getInt(0);
            unitId=cur.getInt(1);
            ContentValues cv = new ContentValues();
            cv.put("BalQty", cur.getDouble(2));
            database.update("TempTrackProduct", cv, "ProductID=" + productId + " AND " + "UnitID=" + unitId, null);
        }

        cur=database.rawQuery("SELECT tp.ProductID,tp.UnitID,TrackQty,BalQty,p.ProductName,u.UnitKeyword,c.CategoryName FROM TempTrackProduct tp INNER JOIN Product p ON tp.ProductID=p.ProductID LEFT JOIN Unit u ON tp.UnitID=u.UnitID INNER JOIN Category c ON p.CategoryID=c.CategoryID ORDER BY p.ProductID",null);
        while(cur.moveToNext()){
            if(cur.getInt(3)<=cur.getInt(2)) {
                ProductModel data = new ProductModel();
                data.setProductId(cur.getInt(0));
                data.setSelectUnitId(cur.getInt(1));
                data.setTrackStock(cur.getInt(2));
                data.setBalQuantity(cur.getDouble(3));
                data.setProductName(cur.getString(4));
                data.setSelectUnitKeyword(cur.getString(5));
                data.setCategoryName(cur.getString(6));
                list.add(data);
            }
        }
        return list;
    }
    /**
     * temp sale function
     */
   /* public void insertMasterSaleTemp(int voucherNo,int customerId,int totalQuantity,int totalAmount){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TempMasterSale");
        ContentValues cv = new ContentValues();
        cv.put("VoucherNo", voucherNo);
        cv.put("CustomerID", customerId);
        cv.put("TotalQuantity", totalQuantity);
        cv.put("TotalAmount", totalAmount);
        database.insert("TempMasterSale", null, cv);
    }
    public void updateMasterSaleTempQtyAndAmt(int totalQuantity,int totalAmount){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TotalQuantity", totalQuantity);
        cv.put("TotalAmount", totalAmount);
        database.update("TempMasterSale", cv, null, null);
    }*/
    public void insertSaleItemTemp(int productId,String productName,int salePrice,int quantity,int isProductUnit,int unitId,String unitKeyword,String unitType,int amount){
        int currentQty=0;
        boolean isExistProduct=false;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Quantity FROM TempSaleItem WHERE ProductID="+productId +" AND UnitID="+unitId,null);
        if(cur.moveToFirst()){
            isExistProduct=true;
            currentQty=cur.getInt(0);
        }
        database = openHelper.getWritableDatabase();
        if(!isExistProduct) {
            ContentValues cv = new ContentValues();
            cv.put("ProductID", productId);
            cv.put("ProductName", productName);
            cv.put("SalePrice", salePrice);
            cv.put("Quantity", quantity);
            cv.put("IsProductUnit", isProductUnit);
            cv.put("UnitID", unitId);
            cv.put("UnitKeyword", unitKeyword);
            cv.put("UnitType",unitType);
            cv.put("Amount",amount);
            database.insert("TempSaleItem", null, cv);
        }
        else {
            ContentValues cv = new ContentValues();
            cv.put("Quantity", currentQty + quantity);
            database.update("TempSaleItem", cv, "ProductID=" + productId + " AND " + "UnitID=" + unitId, null);
        }
    }
    public void updateSaleItemTempQty(int productId,int quantity,int unitId,int amount){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Quantity", quantity);
        cv.put("Amount", amount);
        database.update("TempSaleItem", cv, "ProductID=" + productId + " AND " + "UnitID=" + unitId, null);
    }
    public void deleteSaleItemTemp(int productId,int unitId) {
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TempSaleItem WHERE ProductID="+productId +" AND UnitID="+unitId);
    }
  /*  public List<MasterSaleModel> getMasterSaleTemp(){
        List<MasterSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT VoucherNo,CustomerID,TotalQuantity,TotalAmount FROM TempMasterSale",null);
        if(cur.moveToFirst()){
            MasterSaleModel data=new MasterSaleModel();
            data.setVoucherNumber(cur.getInt(0));
            data.setCustomerId(cur.getInt(1));
//            data.setTotalQuantity(cur.getInt(2));
            data.setTotalAmount(cur.getInt(3));
            list.add(data);
        }
        return list;
    }*/
    public List<TranSaleModel> getSaleItemTemp(){
        List<TranSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ProductID,ProductName,SalePrice,SUM(Quantity),IsProductUnit,UnitID,UnitKeyword,UnitType,Amount FROM TempSaleItem GROUP BY ProductID,ProductName,SalePrice,IsProductUnit,UnitID,UnitKeyword",null);
        while(cur.moveToNext()){
            TranSaleModel data=new TranSaleModel();
            data.setProductId(cur.getInt(0));
            data.setProductName(cur.getString(1));
            data.setSalePrice(cur.getInt(2));
            data.setQuantity(cur.getInt(3));
            data.setIsProductUnit(cur.getInt(4));
            data.setUnitId(cur.getInt(5));
            data.setUnitKeyword(cur.getString(6));
            data.setUnitType(cur.getString(7));
            data.setAmount(cur.getInt(8));
            list.add(data);
        }
        return list;
    }
  /*  public int isExistMasterSaleTemp(){
        int result=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT * FROM TempMasterSale",null);
        if(cur.moveToFirst()) result=1;
        return result;
    }*/
    public int isExistTranSaleTemp(){
        int result=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT * FROM TempSaleItem",null);
        if(cur.moveToFirst()) result=1;
        return result;
    }
  /*  public int getCountSaleItemTemp(){
        int result=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Count(ProductID) FROM TempSaleItem",null);
        if(cur.moveToFirst()) result=cur.getInt(0);
        return result;
    }*/
    public void deleteSaleTemp() {
        database = openHelper.getWritableDatabase();
//        database.execSQL("DELETE FROM TempMasterSale");
        database.execSQL("DELETE FROM TempSaleItem");
    }
  /*  public int totalQtySaleItemTemp() {
        int totalQuantity = 0;
        database = openHelper.getReadableDatabase();
        Cursor cur = database.rawQuery("SELECT SUM(Quantity) FROM TempSaleItem", null);
        if (cur.moveToFirst()) totalQuantity = cur.getInt(0);
        return totalQuantity;
    }
    public int totalAmtSaleItemTemp() {
        int totalAmount = 0;
        database = openHelper.getReadableDatabase();
        Cursor cur = database.rawQuery("SELECT SUM(Quantity * SalePrice) FROM TempSaleItem", null);
        if (cur.moveToFirst()) totalAmount = cur.getInt(0);
        return totalAmount;
    }*/
    /**
     * temp purchase function
     */
    public void insertPurchaseItemTemp(int productId,String productName,int purPrice,int quantity,int isProductUnit,int unitId,String unitKeyword,String unitType){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ProductID", productId);
        cv.put("ProductName", productName);
        cv.put("PurPrice", purPrice);
        cv.put("Quantity", quantity);
        cv.put("IsProductUnit", isProductUnit);
        cv.put("UnitID", unitId);
        cv.put("UnitKeyword", unitKeyword);
        cv.put("UnitType", unitType);
        database.insert("TempPurchaseItem", null, cv);
    }
    public void deletePurchaseItemTemp() {
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TempPurchaseItem");
    }
    public List<TranPurchaseModel> getPurchaseItemTemp(){
        List<TranPurchaseModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ProductID,ProductName,PurPrice,Quantity,IsProductUnit,UnitID,UnitKeyword,UnitType FROM TempPurchaseItem",null);
        while(cur.moveToNext()){
            TranPurchaseModel data=new TranPurchaseModel();
            data.setProductId(cur.getInt(0));
            data.setProductName(cur.getString(1));
            data.setPurPrice(cur.getInt(2));
            data.setQuantity(cur.getInt(3));
            data.setIsProductUnit(cur.getInt(4));
            data.setUnitId(cur.getInt(5));
            data.setUnitKeyword(cur.getString(6));
            data.setUnitType(cur.getString(7));
            list.add(data);
        }
        return list;
    }
    /**
     * voucher number function
     */
    public void insertSalePurVoucherNumber(int saleVoucherNo,int purVoucherNo){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("SaleVoucherNo", saleVoucherNo);
        cv.put("PurchaseVoucherNo", purVoucherNo);
        database.insert("VoucherNumber", null, cv);
    }
    public boolean increaseSaleVoucherNumber() {
        int saleVoucherNo=getSaleVoucherNumber();
        saleVoucherNo=saleVoucherNo+1;
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("SaleVoucherNo", saleVoucherNo);
        database.update("VoucherNumber", cv, null, null);
        return true;
    }
    public int getSaleVoucherNumber(){
        int saleVoucherNumber=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT SaleVoucherNo FROM VoucherNumber",null);
        if(cur.moveToNext()){
            saleVoucherNumber=cur.getInt(0);
        }
        return saleVoucherNumber;
    }
    public boolean increasePurVoucherNumber() {
        int purVoucherNo=getPurVoucherNumber();
        purVoucherNo=purVoucherNo+1;
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PurchaseVoucherNo", purVoucherNo);
        database.update("VoucherNumber", cv, null, null);
        return true;
    }
    public int getPurVoucherNumber(){
        int purVoucherNo=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT PurchaseVoucherNo FROM VoucherNumber",null);
        if(cur.moveToNext()){
            purVoucherNo=cur.getInt(0);
        }
        return purVoucherNo;
    }
    public VoucherNumberModel getVoucherNoTableData(){
        VoucherNumberModel data=new VoucherNumberModel();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ID,SaleVoucherNo,PurchaseVoucherNo FROM VoucherNumber",null);
        if(cur.moveToFirst()){
            data.setId(cur.getInt(0));
            data.setSaleVoucherNo(cur.getInt(1));
            data.setPurchaseVoucherNo(cur.getInt(2));
        }
        return data;
    }
    public void importVoucherNumber(int id,int saleVoucherNo,int purVoucherNo){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("SaleVoucherNo", saleVoucherNo);
        cv.put("PurchaseVoucherNo", purVoucherNo);
        database.insert("VoucherNumber", null, cv);
    }
    /**
     * sale function
     */
    public void insertSale(int voucherNo,String date,String time,int userId,String userName,int customerId,String customerName,int totalAmt,int payDisPercent,int payDisAmount,int totalDisAmount,int netAmount,int isCredit,String creditRemark,int lastDebtAmount,int paidAmount,int changeAmount,int debtAmount,int isDebtAmount) {
        int id=0;
        database = openHelper.getWritableDatabase();
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("VoucherNo", voucherNo);
        cv.put("SaleDate", date);
        cv.put("SaleTime", time);
        cv.put("UserID", userId);
        cv.put("UserName", userName);
        cv.put("CustomerID", customerId);
        cv.put("CustomerName", customerName);
        cv.put("TotalAmount", totalAmt);
        cv.put("PayDisPercent", payDisPercent);
        cv.put("PayDisAmount", payDisAmount);
        cv.put("TotalDisAmount", totalDisAmount);
        cv.put("NetAmount", netAmount);
        cv.put("IsCredit", isCredit);
        cv.put("CreditRemark", creditRemark);
        cv.put("LastDebtAmount", lastDebtAmount);
        cv.put("PaidAmount", paidAmount);
        cv.put("ChangeAmount", changeAmount);
        cv.put("DebtAmount", debtAmount);
        cv.put("IsDebtAmount", isDebtAmount);
        database.insert("TMasterSale", null, cv);

        Cursor cur=dbRead.rawQuery("SELECT Max(MasterSaleID) FROM TMasterSale",null);
        if(cur.moveToFirst()) id=cur.getInt(0);

        List<TranSaleModel> list = getSaleItemTemp();
        for (int i = 0; i < list.size(); i++) {
            ContentValues cvTran = new ContentValues();
            cvTran.put("MasterSaleID", id);
            cvTran.put("ProductID", list.get(i).getProductId());
            cvTran.put("ProductName", list.get(i).getProductName());
            cvTran.put("SalePrice", list.get(i).getSalePrice());
            cvTran.put("Quantity", list.get(i).getQuantity());
            cvTran.put("IsProductUnit", list.get(i).getIsProductUnit());
            cvTran.put("UnitID", list.get(i).getUnitId());
            cvTran.put("UnitKeyword", list.get(i).getUnitKeyword());
            cvTran.put("Amount", list.get(i).getAmount());
            cvTran.put("UnitType", list.get(i).getUnitType());
            database.insert("TTranSale", null, cvTran);

            int productId = list.get(i).getProductId();
            int posQuantity = list.get(i).getQuantity();
            decreaseProductBalance(list.get(i).getIsProductUnit(),list.get(i).getUnitType(),productId,posQuantity);
        }
    }
    private void decreaseProductBalance(int isProductUnit,String unitType,int productId,int posQuantity){
        double curQuantity=0;
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        Cursor cur;
        if(isProductUnit == 1){
            int convertQty,stanConvertQty;
            cur=dbRead.rawQuery("SELECT Quantity FROM ProductBalance WHERE ProductID="+productId,null);
            if(cur.moveToFirst()) curQuantity=cur.getDouble(0);

            if(unitType.equals(systemInfo.StandardUnit)){
                updateProductBalance(productId,curQuantity-posQuantity);
            }else{
                if(unitType.equals(systemInfo.SaleUnit))cur=dbRead.rawQuery("SELECT SaleUnitQty,StandardSaleUnitQty FROM Product WHERE ProductID="+productId,null);
                else if(unitType.equals(systemInfo.PurchaseUnit))cur=dbRead.rawQuery("SELECT PurUnitQty,StandardPurUnitQty FROM Product WHERE ProductID="+productId,null);
                if(cur.moveToFirst()){
                    convertQty=cur.getInt(0);
                    stanConvertQty=cur.getInt(1);

                    if(convertQty==1){
                        int val=posQuantity*stanConvertQty;
                        double bal=curQuantity-val;
                        updateProductBalance(productId,bal);
                    }else{
                        double val=curQuantity*convertQty;
                        double bal=val-posQuantity;
                        double result=bal/convertQty;
                        updateProductBalance(productId,result);
                    }
                }
            }
        }else{
            cur=dbRead.rawQuery("SELECT Quantity FROM ProductBalance WHERE ProductID="+productId,null);
            if(cur.moveToFirst()){
                curQuantity=cur.getDouble(0);
                updateProductBalance(productId,curQuantity-posQuantity);
            }
        }
    }
    public List<MasterSaleModel> getMasterSaleByFilter(String fromDate, String toDate, String searchKeyword){
        List<MasterSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT MasterSaleID,VoucherNo,CustomerName,NetAmount,SaleTime,SaleDate,TotalAmount,PayDisPercent,PayDisAmount,TotalDisAmount,IsCredit,CreditRemark,UserName,LastDebtAmount,PaidAmount,ChangeAmount,DebtAmount,IsDebtAmount,CustomerID FROM TMasterSale WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "' AND (VoucherNo LIKE '%" + searchKeyword + "%' OR CustomerName LIKE '%" + searchKeyword + "%') ORDER BY VoucherNo",null);
        while(cur.moveToNext()){
            MasterSaleModel data=new MasterSaleModel();
            data.setid(cur.getInt(0));
            data.setVoucherNumber(cur.getInt(1));
            data.setCustomerName(cur.getString(2));
            data.setNetAmount(cur.getInt(3));
            data.setTime(cur.getString(4));
            data.setDate(cur.getString(5));
            data.setTotalAmount(cur.getInt(6));
            data.setPayDisPercent(cur.getInt(7));
            data.setPayDisAmount(cur.getInt(8));
            data.setTotalDisAmount(cur.getInt(9));
            data.setIsCredit(cur.getInt(10));
            data.setCreditRemark(cur.getString(11));
            data.setUserName(cur.getString(12));
            data.setLastDebtAmount(cur.getInt(13));
            data.setPaidAmount(cur.getInt(14));
            data.setChangeAmount(cur.getInt(15));
            data.setDebtAmount(cur.getInt(16));
            data.setIsDebtAmount(cur.getInt(17));
            data.setCustomerId(cur.getInt(18));
            list.add(data);
        }
        return list;
    }
    public List<TranSaleModel> getTranSaleByMaster(int id){
        List<TranSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ProductName,SalePrice,Quantity,IsProductUnit,UnitKeyword,Amount,ProductID,UnitID,TranSaleID FROM TTranSale WHERE MasterSaleID=" + id,null);
        while(cur.moveToNext()){
            TranSaleModel data=new TranSaleModel();
            data.setProductName(cur.getString(0));
            data.setSalePrice(cur.getInt(1));
            data.setQuantity(cur.getInt(2));
            data.setIsProductUnit(cur.getInt(3));
            data.setUnitKeyword(cur.getString(4));
            data.setAmount(cur.getInt(5));
            data.setProductId(cur.getInt(6));
            data.setUnitId(cur.getInt(7));
            data.setId(cur.getInt(8));
            list.add(data);
        }
        return list;
    }
    public boolean deleteSale(int voucherNo) {
        int id,productId,unitId,quantity,isProductUnit;
        String unitType;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT MasterSaleID FROM TMasterSale WHERE VoucherNo="+voucherNo,null);
        if(cur.moveToFirst()){
            id=cur.getInt(0);

            Cursor c=database.rawQuery("SELECT ProductID,UnitID,Quantity,IsProductUnit,UnitType FROM TTranSale WHERE MasterSaleID="+id,null);
            while(c.moveToNext()){
                productId=c.getInt(0);
                unitId=c.getInt(1);
                quantity=c.getInt(2);
                isProductUnit=c.getInt(3);
                unitType=c.getString(4);

                increaseProductBalance(isProductUnit,unitType,productId,quantity);
            }

            database = openHelper.getWritableDatabase();
            database.execSQL("DELETE FROM TTranSale WHERE MasterSaleID=" + id);
            database.execSQL("DELETE FROM TMasterSale WHERE MasterSaleID=" + id);
        }
        return true;
    }
    public void updateTranSaleQtyAmt(int tranSaleId,int productId,int unitId,int oldQuantity,int updateQuantity,int amount){
        int isProductUnit;
        String unitType;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT IsProductUnit,UnitType FROM TTranSale WHERE TranSaleID="+tranSaleId,null);
        if(cur.moveToFirst()){
            isProductUnit=cur.getInt(0);
            unitType=cur.getString(1);

            if(oldQuantity > updateQuantity) increaseProductBalance(isProductUnit,unitType,productId,oldQuantity-updateQuantity);
            else if(oldQuantity < updateQuantity)decreaseProductBalance(isProductUnit,unitType,productId,updateQuantity-oldQuantity);
        }
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Quantity", updateQuantity);
        cv.put("Amount", amount);
        database.update("TTranSale", cv, "TranSaleID=" + tranSaleId, null);
    }
    public void deleteTranSale(int tranSaleId,int productId,int unitId) {
        int quantity,isProductUnit;
        String unitType;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Quantity,IsProductUnit,UnitType FROM TTranSale WHERE TranSaleID="+tranSaleId,null);
        if(cur.moveToFirst()){
            quantity=cur.getInt(0);
            isProductUnit=cur.getInt(1);
            unitType=cur.getString(2);
            increaseProductBalance(isProductUnit,unitType,productId,quantity);
        }
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TTranSale WHERE TranSaleID="+tranSaleId);
    }
    public String updateMasterSaleAmt(int voucherNo,int totalAmount,int customerId,int lastDebtAmount,int paidAmount){
        int payDisPercent,payDisAmount,percentAmt,discountAmt=0,netAmount=0,isDebtAmount=0,changeAmount=0,debtAmount=0;
        String result;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT PayDisPercent,PayDisAmount FROM TMasterSale WHERE VoucherNo="+voucherNo,null);
        if(cur.moveToFirst()){
            payDisPercent=cur.getInt(0);
            payDisAmount=cur.getInt(1);
            percentAmt=payDisPercent*totalAmount/100;
            discountAmt=percentAmt+payDisAmount;
            netAmount=totalAmount-discountAmt;

            if(customerId!=0) {
                int curAmount = netAmount + lastDebtAmount;
                if (paidAmount >= curAmount) {
                    changeAmount = paidAmount - curAmount;
                } else {
                    debtAmount = curAmount - paidAmount;
                    isDebtAmount = 1;
                }
            }
        }
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TotalDisAmount", discountAmt);
        cv.put("TotalAmount", totalAmount);
        cv.put("NetAmount", netAmount);
        cv.put("LastDebtAmount", lastDebtAmount);
        cv.put("PaidAmount", paidAmount);
        cv.put("ChangeAmount", changeAmount);
        cv.put("DebtAmount", debtAmount);
        cv.put("IsDebtAmount", isDebtAmount);
        database.update("TMasterSale", cv, "VoucherNo=" + voucherNo, null);
        updateDebtAmountByCustomer(customerId,debtAmount);
        result=discountAmt+","+netAmount+","+isDebtAmount+","+changeAmount+","+debtAmount;
        return result;
    }
    public List<MasterSaleModel> getMasterSaleTableData(){
        List<MasterSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT MasterSaleID,VoucherNo,SaleDate,SaleTime,UserID,UserName,CustomerID,CustomerName,TotalAmount,PayDisPercent,PayDisAmount,TotalDisAmount,NetAmount,LastDebtAmount,PaidAmount,ChangeAmount,DebtAmount,IsDebtAmount,IsCredit,CreditRemark FROM TMasterSale",null);
        while(cur.moveToNext()){
            MasterSaleModel data=new MasterSaleModel();
            data.setId(cur.getInt(0));
            data.setVoucherNumber(cur.getInt(1));
            data.setDate(cur.getString(2));
            data.setTime(cur.getString(3));
            data.setUserId(cur.getInt(4));
            data.setUserName(cur.getString(5));
            data.setCustomerId(cur.getInt(6));
            data.setCustomerName(cur.getString(7));
            data.setTotalAmount(cur.getInt(8));
            data.setPayDisPercent(cur.getInt(9));
            data.setPayDisAmount(cur.getInt(10));
            data.setTotalDisAmount(cur.getInt(11));
            data.setNetAmount(cur.getInt(12));
            data.setLastDebtAmount(cur.getInt(13));
            data.setPaidAmount(cur.getInt(14));
            data.setChangeAmount(cur.getInt(15));
            data.setDebtAmount(cur.getInt(16));
            data.setIsDebtAmount(cur.getInt(17));
            data.setIsCredit(cur.getInt(18));
            data.setCreditRemark(cur.getString(19));
            list.add(data);
        }
        return list;
    }
    public List<TranSaleModel> getTranSaleTableData(){
        List<TranSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT TranSaleID,MasterSaleID,ProductID,ProductName,SalePrice,Quantity,IsProductUnit,UnitID,UnitKeyword,Amount,UnitType FROM TTranSale",null);
        while(cur.moveToNext()){
            TranSaleModel data=new TranSaleModel();
            data.setId(cur.getInt(0));
            data.setMasterSaleId(cur.getInt(1));
            data.setProductId(cur.getInt(2));
            data.setProductName(cur.getString(3));
            data.setSalePrice(cur.getInt(4));
            data.setQuantity(cur.getInt(5));
            data.setIsProductUnit(cur.getInt(6));
            data.setUnitId(cur.getInt(7));
            data.setUnitKeyword(cur.getString(8));
            data.setAmount(cur.getInt(9));
            data.setUnitType(cur.getString(10));
            list.add(data);
        }
        return list;
    }
    public void importMasterSale(int masterSaleId,int voucherNo,String date,String time,int userId,String userName,int customerId,String customerName,int totalAmt,int payDisPercent,int payDisAmount,int totalDisAmount,int netAmount,int lastDebtAmount,int paidAmount,int changeAmount,int debtAmount,int isDebtAmount,int isCredit,String creditRemark) {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("MasterSaleID", masterSaleId);
        cv.put("VoucherNo", voucherNo);
        cv.put("SaleDate", date);
        cv.put("SaleTime", time);
        cv.put("UserID", userId);
        cv.put("UserName", userName);
        cv.put("CustomerID", customerId);
        cv.put("CustomerName", customerName);
        cv.put("TotalAmount", totalAmt);
        cv.put("PayDisPercent", payDisPercent);
        cv.put("PayDisAmount", payDisAmount);
        cv.put("TotalDisAmount", totalDisAmount);
        cv.put("NetAmount", netAmount);
        cv.put("LastDebtAmount", lastDebtAmount);
        cv.put("PaidAmount", paidAmount);
        cv.put("ChangeAmount", changeAmount);
        cv.put("DebtAmount", debtAmount);
        cv.put("IsDebtAmount", isDebtAmount);
        cv.put("IsCredit", isCredit);
        cv.put("CreditRemark", creditRemark);
        database.insert("TMasterSale", null, cv);
    }
    public void importTranSale(int tranSaleId,int masterSaleId,int productId,String productName,int salePrice,int quantity,int isProductUnit,int unitId,String unitKeyword,int amount,String unitType) {
        database = openHelper.getWritableDatabase();
        ContentValues cvTran = new ContentValues();
        cvTran.put("TranSaleID", tranSaleId);
        cvTran.put("MasterSaleID", masterSaleId);
        cvTran.put("ProductID", productId);
        cvTran.put("ProductName", productName);
        cvTran.put("SalePrice", salePrice);
        cvTran.put("Quantity", quantity);
        cvTran.put("IsProductUnit", isProductUnit);
        cvTran.put("UnitID", unitId);
        cvTran.put("UnitKeyword", unitKeyword);
        cvTran.put("Amount", amount);
        cvTran.put("UnitType", unitType);
        database.insert("TTranSale", null, cvTran);
    }
    /**
     * opened bills function
     */
    public void insertOpenedBill(int voucherNo,String date,String time,int userId,String userName,int customerId,String customerName,int totalAmt,String remark,int disPercent,int disAmt,int totalDisAmt,int netAmt) {
        int openBillId=0;
        database = openHelper.getWritableDatabase();
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("VoucherNo", voucherNo);
        cv.put("OpenDate", date);
        cv.put("OpenTime", time);
        cv.put("UserID", userId);
        cv.put("UserName", userName);
        cv.put("CustomerID", customerId);
        cv.put("CustomerName", customerName);
        cv.put("TotalAmount", totalAmt);
        cv.put("Remark", remark);
        cv.put("PayDisPercent", disPercent);
        cv.put("PayDisAmount", disAmt);
        cv.put("TotalDisAmount", totalDisAmt);
        cv.put("NetAmount", netAmt);
        database.insert("TMasterOpenBill", null, cv);

        Cursor cur=dbRead.rawQuery("SELECT Max(OpenBillID) FROM TMasterOpenBill",null);
        if(cur.moveToFirst()) openBillId=cur.getInt(0);

        List<TranSaleModel> list = getSaleItemTemp();
        for (int i = 0; i < list.size(); i++) {
            ContentValues cvTran = new ContentValues();
            cvTran.put("OpenBillID", openBillId);
            cvTran.put("ProductID", list.get(i).getProductId());
            cvTran.put("ProductName", list.get(i).getProductName());
            cvTran.put("SalePrice", list.get(i).getSalePrice());
            cvTran.put("Quantity", list.get(i).getQuantity());
            cvTran.put("IsProductUnit", list.get(i).getIsProductUnit());
            cvTran.put("UnitID", list.get(i).getUnitId());
            cvTran.put("UnitKeyword", list.get(i).getUnitKeyword());
            cvTran.put("UnitType", list.get(i).getUnitType());
            cvTran.put("Amount", list.get(i).getAmount());
            database.insert("TTranOpenBill", null, cvTran);
        }
    }
    public List<MasterSaleModel> getMasterOpenBill(){
        List<MasterSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT OpenBillID,VoucherNo,OpenDate,OpenTime,UserID,UserName,CustomerID,CustomerName,TotalAmount,Remark,PayDisPercent,PayDisAmount,TotalDisAmount,NetAmount FROM TMasterOpenBill",null);
        while(cur.moveToNext()){
            MasterSaleModel data=new MasterSaleModel();
            data.setOpenBillId(cur.getInt(0));
            data.setVoucherNumber(cur.getInt(1));
            data.setDate(cur.getString(2));
            data.setTime(cur.getString(3));
            data.setUserId(cur.getInt(4));
            data.setUserName(cur.getString(5));
            data.setCustomerId(cur.getInt(6));
            data.setCustomerName(cur.getString(7));
            data.setTotalAmount(cur.getInt(8));
            data.setOpenBillRemark(cur.getString(9));
            data.setPayDisPercent(cur.getInt(10));
            data.setPayDisAmount(cur.getInt(11));
            data.setTotalDisAmount(cur.getInt(12));
            data.setNetAmount(cur.getInt(13));
            list.add(data);
        }
        return list;
    }
    public List<TranSaleModel> getTranOpenBillByMaster(int openBillId){
        List<TranSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ProductName,SalePrice,Quantity,IsProductUnit,UnitID,UnitKeyword,Amount FROM TTranOpenBill WHERE OpenBillID=" + openBillId,null);
        while(cur.moveToNext()){
            TranSaleModel data=new TranSaleModel();
            data.setProductName(cur.getString(0));
            data.setSalePrice(cur.getInt(1));
            data.setQuantity(cur.getInt(2));
            data.setIsProductUnit(cur.getInt(3));
            data.setUnitId(cur.getInt(4));
            data.setUnitKeyword(cur.getString(5));
            data.setAmount(cur.getInt(6));
            list.add(data);
        }
        return list;
    }
    public void setTranOpenBillToTemp(int openBillId) {
        int productId, salePrice, quantity, isProductUnit, unitId, amount;
        String productName, unitKeyword,unitType;
        database = openHelper.getReadableDatabase();
        deleteSaleTemp();

        Cursor cur = database.rawQuery("SELECT ProductID,ProductName,SalePrice,Quantity,IsProductUnit,UnitID,UnitKeyword,UnitType,Amount FROM TTranOpenBill WHERE OpenBillID=" + openBillId, null);
        while (cur.moveToNext()) {
            productId = cur.getInt(0);
            productName = cur.getString(1);
            salePrice = cur.getInt(2);
            quantity = cur.getInt(3);
            isProductUnit = cur.getInt(4);
            unitId = cur.getInt(5);
            unitKeyword = cur.getString(6);
            unitType=cur.getString(7);
            amount=cur.getInt(8);
            insertSaleItemTemp(productId, productName, salePrice, quantity, isProductUnit, unitId, unitKeyword,unitType,amount);
        }
    }
    public void deleteOpenBill(int openBillId) {
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TMasterOpenBill WHERE OpenBillID="+openBillId);
        database.execSQL("DELETE FROM TTranOpenBill WHERE OpenBillID="+openBillId);
    }
    public void deleteOpenBillByVoucher(int voucherNo) {
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        Cursor cur = dbRead.rawQuery("SELECT OpenBillID FROM TMasterOpenBill WHERE VoucherNo=" + voucherNo, null);
        if(cur.moveToFirst()){
            int openBillId=cur.getInt(0);
            database = openHelper.getWritableDatabase();
            database.execSQL("DELETE FROM TMasterOpenBill WHERE OpenBillID="+openBillId);
            database.execSQL("DELETE FROM TTranOpenBill WHERE OpenBillID="+openBillId);
        }
    }
    public void updateTranOpenBillQty(int productId,int quantity,int unitId,int amount){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Quantity", quantity);
        cv.put("Amount", amount);
        database.update("TTranOpenBill", cv, "ProductID=" + productId + " AND " + "UnitID=" + unitId, null);
    }
    public void deleteTranOpenBill(int productId,int unitId) {
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TTranOpenBill WHERE ProductID="+productId +" AND UnitID="+unitId);
    }
    public void updateMasterOpenBillAmt(int voucherNo,int totalAmount,int totalDisAmount,int netAmount){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TotalAmount", totalAmount);
        cv.put("TotalDisAmount", totalDisAmount);
        cv.put("NetAmount", netAmount);
        database.update("TMasterOpenBill", cv, "VoucherNo=" + voucherNo, null);
    }
    public List<MasterSaleModel> getMasterOpenBillTableData(){
        List<MasterSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT OpenBillID,VoucherNo,OpenDate,OpenTime,UserID,UserName,CustomerID,CustomerName,TotalAmount,Remark,PayDisPercent,PayDisAmount,TotalDisAmount,NetAmount FROM TMasterOpenBill",null);
        while(cur.moveToNext()){
            MasterSaleModel data=new MasterSaleModel();
            data.setOpenBillId(cur.getInt(0));
            data.setVoucherNumber(cur.getInt(1));
            data.setDate(cur.getString(2));
            data.setTime(cur.getString(3));
            data.setUserId(cur.getInt(4));
            data.setUserName(cur.getString(5));
            data.setCustomerId(cur.getInt(6));
            data.setCustomerName(cur.getString(7));
            data.setTotalAmount(cur.getInt(8));
            data.setOpenBillRemark(cur.getString(9));
            data.setPayDisPercent(cur.getInt(10));
            data.setPayDisAmount(cur.getInt(11));
            data.setTotalDisAmount(cur.getInt(12));
            data.setNetAmount(cur.getInt(13));
            list.add(data);
        }
        return list;
    }
    public List<TranSaleModel> getTranOpenBillTableData(){
        List<TranSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ID,OpenBillID,ProductID,ProductName,SalePrice,Quantity,IsProductUnit,UnitID,UnitKeyword,Amount,UnitType FROM TTranOpenBill",null);
        while(cur.moveToNext()){
            TranSaleModel data=new TranSaleModel();
            data.setId(cur.getInt(0));
            data.setOpenBillId(cur.getInt(1));
            data.setProductId(cur.getInt(2));
            data.setProductName(cur.getString(3));
            data.setSalePrice(cur.getInt(4));
            data.setQuantity(cur.getInt(5));
            data.setIsProductUnit(cur.getInt(6));
            data.setUnitId(cur.getInt(7));
            data.setUnitKeyword(cur.getString(8));
            data.setAmount(cur.getInt(9));
            data.setUnitType(cur.getString(10));
            list.add(data);
        }
        return list;
    }
    public void importMasterOpenBill(int openBillId,int voucherNo,String date,String time,int userId,String userName,int customerId,String customerName,int totalAmt,String remark,int payDisPercent,int payDisAmount,int totalDisAmount,int netAmount) {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("OpenBillID", openBillId);
        cv.put("VoucherNo", voucherNo);
        cv.put("OpenDate", date);
        cv.put("OpenTime", time);
        cv.put("UserID", userId);
        cv.put("UserName", userName);
        cv.put("CustomerID", customerId);
        cv.put("CustomerName", customerName);
        cv.put("TotalAmount", totalAmt);
        cv.put("Remark", remark);
        cv.put("PayDisPercent", payDisPercent);
        cv.put("PayDisAmount", payDisAmount);
        cv.put("TotalDisAmount", totalDisAmount);
        cv.put("NetAmount", netAmount);
        database.insert("TMasterOpenBill", null, cv);
    }
    public void importTranOpenBill(int id,int openBillId,int productId,String productName,int salePrice,int quantity,int isProductUnit,int unitId,String unitKeyword,int amount,String unitType) {
        database = openHelper.getWritableDatabase();
        ContentValues cvTran = new ContentValues();
        cvTran.put("ID", id);
        cvTran.put("OpenBillID", openBillId);
        cvTran.put("ProductID", productId);
        cvTran.put("ProductName", productName);
        cvTran.put("SalePrice", salePrice);
        cvTran.put("Quantity", quantity);
        cvTran.put("IsProductUnit", isProductUnit);
        cvTran.put("UnitID", unitId);
        cvTran.put("UnitKeyword", unitKeyword);
        cvTran.put("Amount", amount);
        cvTran.put("UnitType", unitType);
        database.insert("TTranOpenBill", null, cvTran);
    }
    /**
     * purchase function
     */
    public void insertPurchase(int voucherNo,String date,String time,int userId,String userName,int supplierId,String supplierName,int totalAmt,int payDisPercent,int payDisAmount,int totalDisAmount,int netAmount,int isCredit,String creditRemark,List<TranPurchaseModel> lstPurProduct,int lastDebtAmount,int paidAmount,int changeAmount,int debtAmount,int isDebtAmount) {
        int masterPurId=0;
        SQLiteDatabase dbWrite = openHelper.getWritableDatabase();
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("VoucherNo", voucherNo);
        cv.put("PurchaseDate", date);
        cv.put("PurchaseTime", time);
        cv.put("UserID", userId);
        cv.put("UserName", userName);
        cv.put("SupplierID", supplierId);
        cv.put("SupplierName", supplierName);
        cv.put("TotalAmount", totalAmt);
        cv.put("PayDisPercent", payDisPercent);
        cv.put("PayDisAmount", payDisAmount);
        cv.put("TotalDisAmount", totalDisAmount);
        cv.put("NetAmount", netAmount);
        cv.put("IsCredit", isCredit);
        cv.put("CreditRemark", creditRemark);
        cv.put("LastDebtAmount", lastDebtAmount);
        cv.put("PaidAmount", paidAmount);
        cv.put("ChangeAmount", changeAmount);
        cv.put("DebtAmount", debtAmount);
        cv.put("IsDebtAmount", isDebtAmount);
        dbWrite.insert("TMasterPurchase", null, cv);

        Cursor cur=dbRead.rawQuery("SELECT Max(MasterPurchaseID) FROM TMasterPurchase",null);
        if(cur.moveToFirst()) masterPurId=cur.getInt(0);

        for (int i = 0; i < lstPurProduct.size(); i++) {
            ContentValues cvTran = new ContentValues();
            cvTran.put("MasterPurchaseID", masterPurId);
            cvTran.put("ProductID", lstPurProduct.get(i).getProductId());
            cvTran.put("ProductName", lstPurProduct.get(i).getProductName());
            cvTran.put("PurchasePrice", lstPurProduct.get(i).getPurPrice());
            cvTran.put("Quantity", lstPurProduct.get(i).getQuantity());
            cvTran.put("IsProductUnit", lstPurProduct.get(i).getIsProductUnit());
            cvTran.put("UnitID", lstPurProduct.get(i).getUnitId());
            cvTran.put("UnitKeyword", lstPurProduct.get(i).getUnitKeyword());
            cvTran.put("Amount", lstPurProduct.get(i).getQuantity()*lstPurProduct.get(i).getPurPrice());
            cvTran.put("UnitType", lstPurProduct.get(i).getUnitType());
            dbWrite.insert("TTranPurchase", null, cvTran);

            int productId = lstPurProduct.get(i).getProductId();
            int purQuantity = lstPurProduct.get(i).getQuantity();
            increaseProductBalance(lstPurProduct.get(i).getIsProductUnit(),lstPurProduct.get(i).getUnitType(),productId,purQuantity);
        }
    }
    private void increaseProductBalance(int isProductUnit,String unitType,int productId,int purQuantity){
        double curQuantity=0;
        Cursor cur;
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        if(isProductUnit == 1){
            int convertQty,stanConvertQty;
            cur=dbRead.rawQuery("SELECT Quantity FROM ProductBalance WHERE ProductID="+productId,null);
            if(cur.moveToFirst()) curQuantity=cur.getDouble(0);

            if(unitType.equals(systemInfo.StandardUnit)){
                updateProductBalance(productId,curQuantity+purQuantity);
            }else{
                if(unitType.equals(systemInfo.SaleUnit))cur=dbRead.rawQuery("SELECT SaleUnitQty,StandardSaleUnitQty FROM Product WHERE ProductID="+productId,null);
                else if(unitType.equals(systemInfo.PurchaseUnit))cur=dbRead.rawQuery("SELECT PurUnitQty,StandardPurUnitQty FROM Product WHERE ProductID="+productId,null);
                if(cur.moveToFirst()){
                    convertQty=cur.getInt(0);
                    stanConvertQty=cur.getInt(1);

                    if(convertQty==1){
                        int val=purQuantity*stanConvertQty;
                        double bal=curQuantity+val;
                        updateProductBalance(productId,bal);
                    }else{
                        double val=curQuantity*convertQty;
                        double bal=val+purQuantity;
                        double result=bal/convertQty;
                        updateProductBalance(productId,result);
                    }
                }
            }
        }else{
            cur=dbRead.rawQuery("SELECT Quantity FROM ProductBalance WHERE ProductID="+productId,null);
            if(cur.moveToFirst()){
                curQuantity=cur.getDouble(0);
                updateProductBalance(productId,curQuantity+purQuantity);
            }
        }
    }
    public List<MasterPurchaseModel> getMasterPurchaseByFilter(String fromDate,String toDate, String searchKeyword){
        List<MasterPurchaseModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT MasterPurchaseID,VoucherNo,SupplierName,NetAmount,PurchaseTime,PurchaseDate,TotalAmount,PayDisPercent,PayDisAmount,TotalDisAmount,IsCredit,CreditRemark,UserName,LastDebtAmount,PaidAmount,ChangeAmount,DebtAmount,IsDebtAmount,SupplierID FROM TMasterPurchase WHERE PurchaseDate BETWEEN '" + fromDate + "' AND '" + toDate + "' AND (VoucherNo LIKE '%" + searchKeyword + "%' OR SupplierName LIKE '%" + searchKeyword + "%')",null);
        while(cur.moveToNext()){
            MasterPurchaseModel data=new MasterPurchaseModel();
            data.setId(cur.getInt(0));
            data.setVoucherNumber(cur.getInt(1));
            data.setSupplierName(cur.getString(2));
            data.setNetAmount(cur.getInt(3));
            data.setTime(cur.getString(4));
            data.setDate(cur.getString(5));
            data.setTotalAmount(cur.getInt(6));
            data.setPayDisPercent(cur.getInt(7));
            data.setPayDisAmount(cur.getInt(8));
            data.setTotalDisAmount(cur.getInt(9));
            data.setIsCredit(cur.getInt(10));
            data.setCreditRemark(cur.getString(11));
            data.setUserName(cur.getString(12));
            data.setLastDebtAmount(cur.getInt(13));
            data.setPaidAmount(cur.getInt(14));
            data.setChangeAmount(cur.getInt(15));
            data.setDebtAmount(cur.getInt(16));
            data.setIsDebtAmount(cur.getInt(17));
            data.setSupplierId(cur.getInt(18));
            list.add(data);
        }
        return list;
    }
    public List<TranPurchaseModel> getTranPurchaseByMaster(int masterPurId){
        List<TranPurchaseModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ProductName,PurchasePrice,Quantity,IsProductUnit,UnitKeyword,Amount,TranPurchaseID,ProductID,UnitID FROM TTranPurchase WHERE MasterPurchaseID=" + masterPurId,null);
        while(cur.moveToNext()){
            TranPurchaseModel data=new TranPurchaseModel();
            data.setProductName(cur.getString(0));
            data.setPurPrice(cur.getInt(1));
            data.setQuantity(cur.getInt(2));
            data.setIsProductUnit(cur.getInt(3));
            data.setUnitKeyword(cur.getString(4));
            data.setAmount(cur.getInt(5));
            data.setId(cur.getInt(6));
            data.setProductId(cur.getInt(7));
            data.setUnitId(cur.getInt(8));
            list.add(data);
        }
        return list;
    }
    public boolean deletePurchase(int voucherNo) {
        int masterPurId,productId,unitId,quantity,curQuantity,isProductUnit;
        String unitType;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT MasterPurchaseID FROM TMasterPurchase WHERE VoucherNo="+voucherNo,null);
        if(cur.moveToFirst()){
            masterPurId=cur.getInt(0);

            Cursor c=database.rawQuery("SELECT ProductID,UnitID,Quantity,IsProductUnit,UnitType FROM TTranPurchase WHERE MasterPurchaseID="+masterPurId,null);
            while(c.moveToNext()){
                productId=c.getInt(0);
                unitId=c.getInt(1);
                quantity=c.getInt(2);
                isProductUnit=c.getInt(3);
                unitType=c.getString(4);

                decreaseProductBalance(isProductUnit,unitType,productId,quantity);

               /* cur=database.rawQuery("SELECT Quantity FROM ProductBalance WHERE ProductID="+productId + " AND UnitID="+unitId,null);
                if(cur.moveToFirst()){
                    curQuantity=cur.getInt(0);
                    updateProductBalance(productId,curQuantity-quantity);
                }*/
            }

            database = openHelper.getWritableDatabase();
            database.execSQL("DELETE FROM TTranPurchase WHERE MasterPurchaseID=" + masterPurId);
            database.execSQL("DELETE FROM TMasterPurchase WHERE MasterPurchaseID=" + masterPurId);
        }
        return true;
    }
    public void deleteTranPurchase(int tranPurchaseId,int productId,int unitId) {
        int curQuantity,quantity,isProductUnit;
        String unitType;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Quantity,IsProductUnit,UnitType FROM TTranPurchase WHERE TranPurchaseID="+tranPurchaseId,null);
        if(cur.moveToFirst()){
            quantity=cur.getInt(0);
            isProductUnit=cur.getInt(1);
            unitType=cur.getString(2);
            decreaseProductBalance(isProductUnit,unitType,productId,quantity);
            /*cur=database.rawQuery("SELECT Quantity FROM ProductBalance WHERE ProductID="+productId + " AND UnitID="+unitId,null);
            if(cur.moveToFirst()){
                curQuantity=cur.getInt(0);
                updateProductBalance(productId,curQuantity-quantity);
            }*/
        }
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TTranPurchase WHERE TranPurchaseID="+tranPurchaseId);
    }
    public void updateTranPurchaseQtyAmt(int tranPurchaseId,int productId,int unitId,int oldQuantity,int updateQuantity,int amount){
        int isProductUnit;
        String unitType;
        database = openHelper.getReadableDatabase();
//        Cursor cur=database.rawQuery("SELECT Quantity FROM ProductBalance WHERE ProductID="+productId + " AND UnitID="+unitId,null);
        Cursor cur=database.rawQuery("SELECT IsProductUnit,UnitType FROM TTranPurchase WHERE TranPurchaseID="+tranPurchaseId,null);
        if(cur.moveToFirst()){
            isProductUnit=cur.getInt(0);
            unitType=cur.getString(1);

            if(oldQuantity > updateQuantity) decreaseProductBalance(isProductUnit,unitType,productId,oldQuantity-updateQuantity);
            else if(oldQuantity < updateQuantity)increaseProductBalance(isProductUnit,unitType,productId,updateQuantity-oldQuantity);
//            updateProductBalance(productId,(curQuantity-oldQuantity)+updateQuantity);
        }
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Quantity", updateQuantity);
        cv.put("Amount", amount);
        database.update("TTranPurchase", cv, "TranPurchaseID=" + tranPurchaseId, null);
    }
    public String updateMasterPurchaseAmt(int voucherNo,int totalAmount,int supplierId,int lastDebtAmount,int paidAmount){
        int payDisPercent,payDisAmount,percentAmt,discountAmt=0,netAmount=0,isDebtAmount=0,changeAmount=0,debtAmount=0;
        String result;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT PayDisPercent,PayDisAmount FROM TMasterPurchase WHERE VoucherNo="+voucherNo,null);
        if(cur.moveToFirst()){
            payDisPercent=cur.getInt(0);
            payDisAmount=cur.getInt(1);
            percentAmt=payDisPercent*totalAmount/100;
            discountAmt=percentAmt+payDisAmount;
            netAmount=totalAmount-discountAmt;

            if(supplierId!=0) {
                int curAmount = netAmount + lastDebtAmount;
                if (paidAmount >= curAmount) {
                    changeAmount = paidAmount - curAmount;
                } else {
                    debtAmount = curAmount - paidAmount;
                    isDebtAmount = 1;
                }
            }
        }
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TotalDisAmount", discountAmt);
        cv.put("TotalAmount", totalAmount);
        cv.put("NetAmount", netAmount);
        cv.put("LastDebtAmount", lastDebtAmount);
        cv.put("PaidAmount", paidAmount);
        cv.put("ChangeAmount", changeAmount);
        cv.put("DebtAmount", debtAmount);
        cv.put("IsDebtAmount", isDebtAmount);
        database.update("TMasterPurchase", cv, "VoucherNo=" + voucherNo, null);
        updateDebtAmountBySupplier(supplierId,debtAmount);
        result=discountAmt+","+netAmount+","+isDebtAmount+","+changeAmount+","+debtAmount;
        return result;
    }
    public List<MasterPurchaseModel> getMasterPurchaseTableData(){
        List<MasterPurchaseModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT MasterPurchaseID,VoucherNo,PurchaseDate,PurchaseTime,UserID,UserName,SupplierID,SupplierName,TotalAmount,PayDisPercent,PayDisAmount,TotalDisAmount,NetAmount,LastDebtAmount,PaidAmount,ChangeAmount,DebtAmount,IsDebtAmount,IsCredit,CreditRemark FROM TMasterPurchase",null);
        while(cur.moveToNext()){
            MasterPurchaseModel data=new MasterPurchaseModel();
            data.setId(cur.getInt(0));
            data.setVoucherNumber(cur.getInt(1));
            data.setDate(cur.getString(2));
            data.setTime(cur.getString(3));
            data.setUserId(cur.getInt(4));
            data.setUserName(cur.getString(5));
            data.setSupplierId(cur.getInt(6));
            data.setSupplierName(cur.getString(7));
            data.setTotalAmount(cur.getInt(8));
            data.setPayDisPercent(cur.getInt(9));
            data.setPayDisAmount(cur.getInt(10));
            data.setTotalDisAmount(cur.getInt(11));
            data.setNetAmount(cur.getInt(12));
            data.setLastDebtAmount(cur.getInt(13));
            data.setPaidAmount(cur.getInt(14));
            data.setChangeAmount(cur.getInt(15));
            data.setDebtAmount(cur.getInt(16));
            data.setIsDebtAmount(cur.getInt(17));
            data.setIsCredit(cur.getInt(18));
            data.setCreditRemark(cur.getString(19));
            list.add(data);
        }
        return list;
    }
    public List<TranPurchaseModel> getTranPurchaseTableData(){
        List<TranPurchaseModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT TranPurchaseID,MasterPurchaseID,ProductID,ProductName,PurchasePrice,Quantity,IsProductUnit,UnitID,UnitKeyword,Amount,UnitType FROM TTranPurchase",null);
        while(cur.moveToNext()){
            TranPurchaseModel data=new TranPurchaseModel();
            data.setId(cur.getInt(0));
            data.setMasterPurchaseId(cur.getInt(1));
            data.setProductId(cur.getInt(2));
            data.setProductName(cur.getString(3));
            data.setPurPrice(cur.getInt(4));
            data.setQuantity(cur.getInt(5));
            data.setIsProductUnit(cur.getInt(6));
            data.setUnitId(cur.getInt(7));
            data.setUnitKeyword(cur.getString(8));
            data.setAmount(cur.getInt(9));
            data.setUnitType(cur.getString(10));
            list.add(data);
        }
        return list;
    }
    public void importMasterPurchase(int masterPurchaseId,int voucherNo,String date,String time,int userId,String userName,int supplierId,String supplierName,int totalAmt,int payDisPercent,int payDisAmount,int totalDisAmount,int netAmount,int lastDebtAmount,int paidAmount,int changeAmount,int debtAmount,int isDebtAmount,int isCredit,String creditRemark) {
        SQLiteDatabase dbWrite = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("MasterPurchaseID", masterPurchaseId);
        cv.put("VoucherNo", voucherNo);
        cv.put("PurchaseDate", date);
        cv.put("PurchaseTime", time);
        cv.put("UserID", userId);
        cv.put("UserName", userName);
        cv.put("SupplierID", supplierId);
        cv.put("SupplierName", supplierName);
        cv.put("TotalAmount", totalAmt);
        cv.put("PayDisPercent", payDisPercent);
        cv.put("PayDisAmount", payDisAmount);
        cv.put("TotalDisAmount", totalDisAmount);
        cv.put("NetAmount", netAmount);
        cv.put("LastDebtAmount", lastDebtAmount);
        cv.put("PaidAmount", paidAmount);
        cv.put("ChangeAmount", changeAmount);
        cv.put("DebtAmount", debtAmount);
        cv.put("IsDebtAmount", isDebtAmount);
        cv.put("IsCredit", isCredit);
        cv.put("CreditRemark", creditRemark);
        dbWrite.insert("TMasterPurchase", null, cv);
    }
    public void importTranPurchase(int tranPurchaseId,int masterPurchaseId,int productId,String productName,int purchasePrice,int quantity,int isProductUnit,int unitId,String unitKeyword,int amount,String unitType) {
        database = openHelper.getWritableDatabase();
        ContentValues cvTran = new ContentValues();
        cvTran.put("TranPurchaseID", tranPurchaseId);
        cvTran.put("MasterPurchaseID", masterPurchaseId);
        cvTran.put("ProductID", productId);
        cvTran.put("ProductName", productName);
        cvTran.put("PurchasePrice", purchasePrice);
        cvTran.put("Quantity", quantity);
        cvTran.put("IsProductUnit", isProductUnit);
        cvTran.put("UnitID", unitId);
        cvTran.put("UnitKeyword", unitKeyword);
        cvTran.put("Amount", amount);
        cvTran.put("UnitType", unitType);
        database.insert("TTranPurchase", null, cvTran);
    }
    /**
     * printer function
     */
    public boolean insertPrinter(String printerName,int printerInterfaceId,String printerInterfaceName,String netPrinterIp,String btPrinterAddress,String paperWidth){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PrinterName", printerName);
        cv.put("PrinterInterfaceID", printerInterfaceId);
        cv.put("PrinterInterfaceName", printerInterfaceName);
        cv.put("NetPrinterIP", netPrinterIp);
        cv.put("BTPrinterAddress", btPrinterAddress);
        cv.put("PaperWidth", paperWidth);
        database.insert("Printer", null, cv);
        return true;
    }
    public boolean updatePrinter(int id,String printerName,int printerInterfaceId,String printerInterfaceName,String netPrinterIp,String btPrinterAddress,String paperWidth) {
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PrinterName", printerName);
        cv.put("PrinterInterfaceID", printerInterfaceId);
        cv.put("PrinterInterfaceName", printerInterfaceName);
        cv.put("NetPrinterIP", netPrinterIp);
        cv.put("BTPrinterAddress", btPrinterAddress);
        cv.put("PaperWidth", paperWidth);
        database.update("Printer", cv, "ID=" + id, null);
        return true;
    }
    public boolean deletePrinter(int id) {
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM Printer WHERE ID=" + id);
        return true;
    }
    public List<PrinterModel> getPrinter(){
        List<PrinterModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ID,PrinterName,PrinterInterfaceID,PrinterInterfaceName,NetPrinterIP,BTPrinterAddress,PaperWidth FROM Printer",null);
        while(cur.moveToNext()){
            PrinterModel data=new PrinterModel();
            data.setId(cur.getInt(0));
            data.setPrinterName(cur.getString(1));
            data.setPrinterInterfaceId(cur.getInt(2));
            data.setPrinterInterfaceName(cur.getString(3));
            data.setNetPrinterIp(cur.getString(4));
            data.setBtPrinterAddress(cur.getString(5));
            data.setPaperWidth(cur.getString(6));
            list.add(data);
        }
        return list;
    }
    public PrinterModel getPrinterByID(int id){
        PrinterModel data=new PrinterModel();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT PrinterName,PrinterInterfaceID,PrinterInterfaceName,NetPrinterIP,BTPrinterAddress,PaperWidth FROM Printer WHERE ID=" + id,null);
        if(cur.moveToFirst()){
            data=new PrinterModel();
            data.setPrinterName(cur.getString(0));
            data.setPrinterInterfaceId(cur.getInt(1));
            data.setPrinterInterfaceName(cur.getString(2));
            data.setNetPrinterIp(cur.getString(3));
            data.setBtPrinterAddress(cur.getString(4));
            data.setPaperWidth(cur.getString(5));
        }
        return data;
    }
    public boolean isExistNetPrinter(){
        boolean result=false;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ID FROM Printer WHERE PrinterInterfaceID=1",null);
        if(cur.moveToFirst()) result=true;
        return result;
    }
    /**
     * adjustment function
     */
    public void insertAdjustment(String date, String time, int userId, String userName, int totalAmt, String remark, List<TranAdjustmentModel> lstAdjustProduct) {
        int masterAdjustId=0;
        SQLiteDatabase dbWrite = openHelper.getWritableDatabase();
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("AdjustmentDate", date);
        cv.put("AdjustmentTime", time);
        cv.put("UserID", userId);
        cv.put("UserName", userName);
        cv.put("TotalAmount", totalAmt);
        cv.put("Remark", remark);
        dbWrite.insert("TMasterAdjustment", null, cv);

        Cursor cur=dbRead.rawQuery("SELECT Max(MasterAdjustmentID) FROM TMasterAdjustment",null);
        if(cur.moveToFirst()) masterAdjustId=cur.getInt(0);

        for (int i = 0; i < lstAdjustProduct.size(); i++) {
            ContentValues cvTran = new ContentValues();
            cvTran.put("MasterAdjustmentID", masterAdjustId);
            cvTran.put("ProductID", lstAdjustProduct.get(i).getProductId());
            cvTran.put("ProductName", lstAdjustProduct.get(i).getProductName());
            cvTran.put("PurchasePrice", lstAdjustProduct.get(i).getPurPrice());
            cvTran.put("Quantity", lstAdjustProduct.get(i).getQuantity());
            cvTran.put("IsProductUnit", lstAdjustProduct.get(i).getIsProductUnit());
            cvTran.put("UnitID", lstAdjustProduct.get(i).getUnitId());
            cvTran.put("UnitKeyword", lstAdjustProduct.get(i).getUnitKeyword());
            cvTran.put("Amount", lstAdjustProduct.get(i).getQuantity()*lstAdjustProduct.get(i).getPurPrice());
            cvTran.put("UnitType", lstAdjustProduct.get(i).getUnitType());
            dbWrite.insert("TTranAdjustment", null, cvTran);

            int productId = lstAdjustProduct.get(i).getProductId();
            int posQuantity = lstAdjustProduct.get(i).getQuantity();
            decreaseProductBalance(lstAdjustProduct.get(i).getIsProductUnit(),lstAdjustProduct.get(i).getUnitType(),productId,posQuantity);
        }
    }
    public List<MasterAdjustmentModel> getMasterAdjustmentByFilter(String fromDate,String toDate, String searchKeyword){
        List<MasterAdjustmentModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT MasterAdjustmentID,AdjustmentTime,AdjustmentDate,TotalAmount,UserName,Remark FROM TMasterAdjustment WHERE AdjustmentDate BETWEEN '" + fromDate + "' AND '" + toDate + "' AND (MasterAdjustmentID LIKE '%" + searchKeyword + "%' OR UserName LIKE '%" + searchKeyword + "%') ORDER BY MasterAdjustmentID DESC",null);
        while(cur.moveToNext()){
            MasterAdjustmentModel data=new MasterAdjustmentModel();
            data.setMasterAdjustmentId(cur.getInt(0));
            data.setTime(cur.getString(1));
            data.setDate(cur.getString(2));
            data.setTotalAmount(cur.getInt(3));
            data.setUserName(cur.getString(4));
            data.setRemark(cur.getString(5));
            list.add(data);
        }
        return list;
    }
    public List<TranAdjustmentModel> getTranAdjustmentByMaster(int masterAdjustId){
        List<TranAdjustmentModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT ProductName,PurchasePrice,Quantity,IsProductUnit,UnitKeyword,Amount,TranAdjustmentID,ProductID,UnitID FROM TTranAdjustment WHERE MasterAdjustmentID=" + masterAdjustId,null);
        while(cur.moveToNext()){
            TranAdjustmentModel data=new TranAdjustmentModel();
            data.setProductName(cur.getString(0));
            data.setPurPrice(cur.getInt(1));
            data.setQuantity(cur.getInt(2));
            data.setIsProductUnit(cur.getInt(3));
            data.setUnitKeyword(cur.getString(4));
            data.setAmount(cur.getInt(5));
            data.setTranAdjustmentId(cur.getInt(6));
            data.setProductId(cur.getInt(7));
            data.setUnitId(cur.getInt(8));
            list.add(data);
        }
        return list;
    }
    public boolean deleteAdjustment(int masterAdjustId) {
        int productId, unitId, quantity, isProductUnit;
        String unitType;
        database = openHelper.getReadableDatabase();

        Cursor c = database.rawQuery("SELECT ProductID,UnitID,Quantity,IsProductUnit,UnitType FROM TTranAdjustment WHERE MasterAdjustmentID=" + masterAdjustId, null);
        while (c.moveToNext()) {
            productId = c.getInt(0);
            unitId = c.getInt(1);
            quantity = c.getInt(2);
            isProductUnit = c.getInt(3);
            unitType = c.getString(4);

            increaseProductBalance(isProductUnit, unitType, productId, quantity);
        }

        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TTranAdjustment WHERE MasterAdjustmentID=" + masterAdjustId);
        database.execSQL("DELETE FROM TMasterAdjustment WHERE MasterAdjustmentID=" + masterAdjustId);

        return true;
    }
    public void deleteTranAdjustment(int tranAdjustId,int productId) {
        int quantity,isProductUnit;
        String unitType;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Quantity,IsProductUnit,UnitType FROM TTranAdjustment WHERE TranAdjustmentID="+tranAdjustId,null);
        if(cur.moveToFirst()){
            quantity=cur.getInt(0);
            isProductUnit=cur.getInt(1);
            unitType=cur.getString(2);
            increaseProductBalance(isProductUnit,unitType,productId,quantity);
        }
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TTranAdjustment WHERE TranAdjustmentID="+tranAdjustId);
    }
    public void updateTranAdjustmentQtyAmt(int tranAdjustId,int productId,int unitId,int oldQuantity,int updateQuantity,int amount){
        int isProductUnit;
        String unitType;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT IsProductUnit,UnitType FROM TTranAdjustment WHERE TranAdjustmentID="+tranAdjustId,null);
        if(cur.moveToFirst()){
            isProductUnit=cur.getInt(0);
            unitType=cur.getString(1);

            if(oldQuantity > updateQuantity) increaseProductBalance(isProductUnit,unitType,productId,oldQuantity-updateQuantity);
            else if(oldQuantity < updateQuantity)decreaseProductBalance(isProductUnit,unitType,productId,updateQuantity-oldQuantity);
        }
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Quantity", updateQuantity);
        cv.put("Amount", amount);
        database.update("TTranAdjustment", cv, "TranAdjustmentID=" + tranAdjustId, null);
    }
    public void updateMasterAdjustmentAmt(int masterAdjustId,int totalAmount){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TotalAmount", totalAmount);
        database.update("TMasterAdjustment", cv, "MasterAdjustmentID=" + masterAdjustId, null);
    }
    public List<MasterAdjustmentModel> getMasterAdjustmentTableData(){
        List<MasterAdjustmentModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT MasterAdjustmentID,AdjustmentDate,AdjustmentTime,UserID,UserName,TotalAmount,Remark FROM TMasterAdjustment",null);
        while(cur.moveToNext()){
            MasterAdjustmentModel data=new MasterAdjustmentModel();
            data.setMasterAdjustmentId(cur.getInt(0));
            data.setDate(cur.getString(1));
            data.setTime(cur.getString(2));
            data.setUserId(cur.getInt(3));
            data.setUserName(cur.getString(4));
            data.setTotalAmount(cur.getInt(5));
            data.setRemark(cur.getString(6));
            list.add(data);
        }
        return list;
    }
    public List<TranAdjustmentModel> getTranAdjustmentTableData(){
        List<TranAdjustmentModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT TranAdjustmentID,MasterAdjustmentID,ProductID,ProductName,PurchasePrice,Quantity,IsProductUnit,UnitID,UnitKeyword,Amount,UnitType FROM TTranAdjustment",null);
        while(cur.moveToNext()){
            TranAdjustmentModel data=new TranAdjustmentModel();
            data.setTranAdjustmentId(cur.getInt(0));
            data.setMasterAdjustmentId(cur.getInt(1));
            data.setProductId(cur.getInt(2));
            data.setProductName(cur.getString(3));
            data.setPurPrice(cur.getInt(4));
            data.setQuantity(cur.getInt(5));
            data.setIsProductUnit(cur.getInt(6));
            data.setUnitId(cur.getInt(7));
            data.setUnitKeyword(cur.getString(8));
            data.setAmount(cur.getInt(9));
            data.setUnitType(cur.getString(10));
            list.add(data);
        }
        return list;
    }
    public void importMasterAdjustment(int masterAdjustmentId,String date,String time,int userId,String userName,int totalAmt,String remark) {
        SQLiteDatabase dbWrite = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("MasterAdjustmentID", masterAdjustmentId);
        cv.put("AdjustmentDate", date);
        cv.put("AdjustmentTime", time);
        cv.put("UserID", userId);
        cv.put("UserName", userName);
        cv.put("TotalAmount", totalAmt);
        cv.put("Remark", remark);
        dbWrite.insert("TMasterAdjustment", null, cv);
    }
    public void importTranAdjustment(int tranAdjustmentId,int masterAdjustmentId,int productId,String productName,int purchasePrice,int quantity,int isProductUnit,int unitId,String unitKeyword,int amount,String unitType) {
        database = openHelper.getWritableDatabase();
        ContentValues cvTran = new ContentValues();
        cvTran.put("TranAdjustmentID", tranAdjustmentId);
        cvTran.put("MasterAdjustmentID", masterAdjustmentId);
        cvTran.put("ProductID", productId);
        cvTran.put("ProductName", productName);
        cvTran.put("PurchasePrice", purchasePrice);
        cvTran.put("Quantity", quantity);
        cvTran.put("IsProductUnit", isProductUnit);
        cvTran.put("UnitID", unitId);
        cvTran.put("UnitKeyword", unitKeyword);
        cvTran.put("Amount", amount);
        cvTran.put("UnitType", unitType);
        database.insert("TTranAdjustment", null, cvTran);
    }
    /**
     * report function
     */
    public int getTotalSaleCountByDate(String date){
        int count=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Count(MasterSaleID) FROM TMasterSale WHERE SaleDate='" + date +"'",null);
        if(cur.moveToFirst())count=cur.getInt(0);
        return count;
    }
    public int getSaleTotalAmountByDate(String date){
        int amount=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Sum(TotalAmount) FROM TMasterSale WHERE SaleDate='" + date +"'",null);
        if(cur.moveToFirst())amount=cur.getInt(0);
        return amount;
    }
    public int getSaleNetAmountByDate(String date){
        int amount=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Sum(NetAmount) FROM TMasterSale WHERE SaleDate='" + date +"'",null);
        if(cur.moveToFirst())amount=cur.getInt(0);
        return amount;
    }
    public int getSaleDiscountByDate(String date){
        int amount=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Sum(TotalDisAmount) FROM TMasterSale WHERE SaleDate='" + date +"'",null);
        if(cur.moveToFirst())amount=cur.getInt(0);
        return amount;
    }
    public int getTotalQuantityByDate(String date){
        int totalQty=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Sum(Quantity) FROM TTranSale ts INNER JOIN TMasterSale ms ON ts.MasterSaleID=ms.MasterSaleID WHERE SaleDate='" + date +"'",null);
        if(cur.moveToFirst())totalQty=cur.getInt(0);
        return totalQty;
    }
    public int getSaleTotalDebtByDate(String date){
        int amount=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Sum(DebtAmount) FROM TMasterSale WHERE SaleDate='" + date +"'",null);
        if(cur.moveToFirst())amount=cur.getInt(0);
        return amount;
    }
    public List<MasterSaleModel> getMasterSaleByFromToDate(String fromDate, String toDate){
        List<MasterSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT MasterSaleID,VoucherNo,CustomerName,NetAmount,SaleTime,SaleDate,TotalAmount,PayDisPercent,PayDisAmount,TotalDisAmount,IsCredit,CreditRemark,UserName,LastDebtAmount,PaidAmount,ChangeAmount,DebtAmount,IsDebtAmount,CustomerID FROM TMasterSale WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "' ORDER BY VoucherNo",null);
        while(cur.moveToNext()){
            MasterSaleModel data=new MasterSaleModel();
            data.setid(cur.getInt(0));
            data.setVoucherNumber(cur.getInt(1));
            data.setCustomerName(cur.getString(2));
            data.setNetAmount(cur.getInt(3));
            data.setTime(cur.getString(4));
            data.setDate(cur.getString(5));
            data.setTotalAmount(cur.getInt(6));
            data.setPayDisPercent(cur.getInt(7));
            data.setPayDisAmount(cur.getInt(8));
            data.setTotalDisAmount(cur.getInt(9));
            data.setIsCredit(cur.getInt(10));
            data.setCreditRemark(cur.getString(11));
            data.setUserName(cur.getString(12));
            data.setLastDebtAmount(cur.getInt(13));
            data.setPaidAmount(cur.getInt(14));
            data.setChangeAmount(cur.getInt(15));
            data.setDebtAmount(cur.getInt(16));
            data.setIsDebtAmount(cur.getInt(17));
            data.setCustomerId(cur.getInt(18));
            list.add(data);
        }
        return list;
    }
    public int getTotalAmtByFromToDate(String fromDate, String toDate){
        int totalAmt=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Sum(TotalAmount) FROM TMasterSale WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "'",null);
        if(cur.moveToFirst())totalAmt=cur.getInt(0);
        return totalAmt;
    }
    public int getDiscountAmtByFromToDate(String fromDate, String toDate){
        int totalDisAmt=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Sum(TotalDisAmount) FROM TMasterSale WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "'",null);
        if(cur.moveToFirst())totalDisAmt=cur.getInt(0);
        return totalDisAmt;
    }
    public int getNetAmtByFromToDate(String fromDate, String toDate){
        int netAmt=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Sum(NetAmount) FROM TMasterSale WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "'",null);
        if(cur.moveToFirst())netAmt=cur.getInt(0);
        return netAmt;
    }
    public List<ProductModel> getTopSaleCategory(String fromDate,String toDate,int number){
        List<ProductModel> list=new ArrayList<>();
        ProductModel data;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT p.CategoryID,c.CategoryName,SUM(ts.Quantity) FROM TMasterSale ms INNER JOIN TTranSale ts ON ms.MasterSaleID=ts.MasterSaleID INNER JOIN Product p ON ts.ProductID=p.ProductID INNER JOIN Category c ON p.CategoryID=c.CategoryID WHERE ms.SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY p.CategoryID,c.CategoryName ORDER BY SUM(ts.Quantity) DESC LIMIT "+number,null);
        while (cur.moveToNext()){
            data=new ProductModel();
            data.setCategoryId(cur.getInt(0));
            data.setCategoryName(cur.getString(1));
            data.setTotalSaleQtyByCategory(cur.getInt(2));
            list.add(data);
        }
        return list;
    }
    public List<MasterSaleModel> getSaleByDate(String fromDate, String toDate){
        List<MasterSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT SaleDate,Count(MasterSaleID),Sum(TotalAmount),Sum(TotalDisAmount),Sum(NetAmount) FROM TMasterSale WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY SaleDate ORDER BY SaleDate",null);
        while(cur.moveToNext()){
            MasterSaleModel data=new MasterSaleModel();
            data.setDate(cur.getString(0));
            data.setTotalSale(cur.getInt(1));
            data.setTotalAmount(cur.getInt(2));
            data.setTotalDisAmount(cur.getInt(3));
            data.setNetAmount(cur.getInt(4));
            Cursor curQty=database.rawQuery("SELECT SaleDate,Sum(Quantity) FROM TMasterSale ms INNER JOIN TTranSale ts ON ms.MasterSaleID=ts.MasterSaleID WHERE SaleDate='" + cur.getString(0) +"' GROUP BY SaleDate",null);
            if(curQty.moveToFirst())data.setTotalQuantity(curQty.getInt(1));
            list.add(data);
        }
        return list;
    }
    public int getTotalSaleByFromToDate(String fromDate, String toDate){
        int totalSale=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Count(MasterSaleID) FROM TMasterSale WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "'",null);
        if(cur.moveToFirst())totalSale=cur.getInt(0);
        return totalSale;
    }
    public int getTotalQuantityByFromToDate(String fromDate, String toDate){
        int totalQty=0;
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT Sum(Quantity) FROM TTranSale ts INNER JOIN TMasterSale ms ON ts.MasterSaleID=ms.MasterSaleID WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "'",null);
        if(cur.moveToFirst())totalQty=cur.getInt(0);
        return totalQty;
    }
    public List<MasterSaleModel> getSaleByMonthly(int year){
        List<MasterSaleModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        deleteTempSaleByMonthly();
        setMonth(list);
        Cursor cur=database.rawQuery("SELECT Count(MasterSaleID),Sum(TotalAmount),Sum(TotalDisAmount),Sum(NetAmount),substr(SaleDate,6,2),substr(SaleDate,1,4) FROM TMasterSale GROUP BY substr(SaleDate,6,2),substr(SaleDate,1,4)",null);
        Cursor curQty=database.rawQuery("SELECT Sum(Quantity),substr(SaleDate,6,2),substr(SaleDate,1,4) FROM TMasterSale ms INNER JOIN TTranSale ts ON ms.MasterSaleID=ts.MasterSaleID GROUP BY substr(SaleDate,6,2),substr(SaleDate,1,4)",null);
        while(cur.moveToNext()){
            insertTempSaleByMonthly(cur.getInt(0),cur.getInt(1),cur.getInt(2),cur.getInt(3),cur.getString(4),cur.getString(5));
        }
        while (curQty.moveToNext()){
            updateTempSaleByMonthly(curQty.getInt(0),curQty.getString(1),curQty.getString(2));
        }
        cur=database.rawQuery("SELECT TotalSale,TotalQuantity,TotalAmount,TotalDisAmount,NetAmount,Month FROM TempSaleByMonthly WHERE Year='" + year + "' ORDER BY Month",null);
        while(cur.moveToNext()){
            for(int i=0;i<list.size();i++){
                if(list.get(i).getMonth().equals(cur.getString(5))){
                    MasterSaleModel data=new MasterSaleModel();
                    data.setTotalSale(cur.getInt(0));
                    data.setTotalQuantity(cur.getInt(1));
                    data.setTotalAmount(cur.getInt(2));
                    data.setTotalDisAmount(cur.getInt(3));
                    data.setNetAmount(cur.getInt(4));
                    data.setMonth(cur.getString(5));
                    if(list.get(i).getMonth().equals("01"))data.setMonthName("January");
                    else if(list.get(i).getMonth().equals("02"))data.setMonthName("February");
                    else if(list.get(i).getMonth().equals("03"))data.setMonthName("March");
                    else if(list.get(i).getMonth().equals("04"))data.setMonthName("April");
                    else if(list.get(i).getMonth().equals("05"))data.setMonthName("May");
                    else if(list.get(i).getMonth().equals("06"))data.setMonthName("June");
                    else if(list.get(i).getMonth().equals("07"))data.setMonthName("July");
                    else if(list.get(i).getMonth().equals("08"))data.setMonthName("August");
                    else if(list.get(i).getMonth().equals("09"))data.setMonthName("September");
                    else if(list.get(i).getMonth().equals("10"))data.setMonthName("October");
                    else if(list.get(i).getMonth().equals("11"))data.setMonthName("November");
                    else if(list.get(i).getMonth().equals("12"))data.setMonthName("December");
                    list.set(i,data);
                }
            }
        }
        return list;
    }
    private void setMonth(List<MasterSaleModel> list){
        MasterSaleModel data=new MasterSaleModel();
        data.setMonth("01");
        data.setMonthName("January");
        list.add(data);
        data=new MasterSaleModel();
        data.setMonth("02");
        data.setMonthName("February");
        list.add(data);
        data=new MasterSaleModel();
        data.setMonth("03");
        data.setMonthName("March");
        list.add(data);
        data=new MasterSaleModel();
        data.setMonth("04");
        data.setMonthName("April");
        list.add(data);
        data=new MasterSaleModel();
        data.setMonth("05");
        data.setMonthName("May");
        list.add(data);
        data=new MasterSaleModel();
        data.setMonth("06");
        data.setMonthName("June");
        list.add(data);
        data=new MasterSaleModel();
        data.setMonth("07");
        data.setMonthName("July");
        list.add(data);
        data=new MasterSaleModel();
        data.setMonth("08");
        data.setMonthName("August");
        list.add(data);
        data=new MasterSaleModel();
        data.setMonth("09");
        data.setMonthName("September");
        list.add(data);
        data=new MasterSaleModel();
        data.setMonth("10");
        data.setMonthName("October");
        list.add(data);
        data=new MasterSaleModel();
        data.setMonth("11");
        data.setMonthName("November");
        list.add(data);
        data=new MasterSaleModel();
        data.setMonth("12");
        data.setMonthName("December");
        list.add(data);
    }
    private void deleteTempSaleByMonthly(){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TempSaleByMonthly");
    }
    private void insertTempSaleByMonthly(int totalSale,int totalAmount,int totalDisAmount,int netAmount,String month,String year){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TotalSale", totalSale);
        cv.put("TotalAmount", totalAmount);
        cv.put("TotalDisAmount", totalDisAmount);
        cv.put("NetAmount", netAmount);
        cv.put("Month", month);
        cv.put("Year", year);
        database.insert("TempSaleByMonthly", null, cv);
    }
    private void updateTempSaleByMonthly(int totalQuantity,String month,String year){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TotalQuantity", totalQuantity);
        database.update("TempSaleByMonthly", cv, "Month='" + month + "' AND " + "Year='" + year+"'", null);
    }
    private void deleteTempSaleByCategory(){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TempSaleByCategory");
    }
    private void updateTempSaleByCategory(int totalQuantity,int totalAmount,int categoryId){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TotalQuantity", totalQuantity);
        cv.put("TotalAmount", totalAmount);
        database.update("TempSaleByCategory", cv, "CategoryID=" + categoryId, null);
    }
    public List<MasterSaleModel> getSaleByCategory(String fromDate, String toDate){
        List<MasterSaleModel> list=new ArrayList<>();
        SQLiteDatabase dbWrite = openHelper.getWritableDatabase();
        database = openHelper.getReadableDatabase();
        deleteTempSaleByCategory();
        dbWrite.execSQL("INSERT INTO TempSaleByCategory (CategoryID,CategoryName) SELECT CategoryID,CategoryName FROM Category");

        Cursor cur=database.rawQuery("SELECT Sum(ts.Quantity),Sum(ts.Amount),p.CategoryID FROM TTranSale ts INNER JOIN Product p on ts.ProductID=p.ProductID INNER JOIN TMasterSale ms on ts.MasterSaleID=ms.MasterSaleID WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY p.CategoryID",null);
        while(cur.moveToNext()){
            updateTempSaleByCategory(cur.getInt(0),cur.getInt(1),cur.getInt(2));
        }

        cur=database.rawQuery("SELECT CategoryName,TotalQuantity,TotalAmount FROM TempSaleByCategory",null);
        while(cur.moveToNext()){
            MasterSaleModel data=new MasterSaleModel();
            data.setCategoryName(cur.getString(0));
            data.setTotalQuantity(cur.getInt(1));
            data.setTotalAmount(cur.getInt(2));
            list.add(data);
        }
        return list;
    }
    public List<TranSaleModel> getSaleByProduct(String fromDate, String toDate, String selectedCategoryLst){
        List<TranSaleModel> list=new ArrayList<>();
        Cursor cur=database.rawQuery("SELECT Sum(ts.Quantity),Sum(ts.Amount),p.ProductCode,p.ProductName,ts.UnitKeyword,c.CategoryName,UnitType FROM TTranSale ts INNER JOIN Product p on ts.ProductID=p.ProductID INNER JOIN Category c on p.CategoryID=c.CategoryID INNER JOIN TMasterSale ms on ts.MasterSaleID=ms.MasterSaleID WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "' AND c.CategoryID IN (" + selectedCategoryLst + ") GROUP BY p.ProductCode,p.ProductName,ts.UnitKeyword,c.CategoryName,UnitType ORDER BY c.CategoryID",null);
        while(cur.moveToNext()){
            TranSaleModel data=new TranSaleModel();
            data.setQuantity(cur.getInt(0));
            data.setAmount(cur.getInt(1));
            data.setProductCode(cur.getString(2));
            data.setProductName(cur.getString(3));
            data.setUnitKeyword(cur.getString(4));
            data.setCategoryName(cur.getString(5));
            data.setUnitType(cur.getString(6));
            list.add(data);
        }
        return list;
    }
    public List<TranSaleModel> getTopSaleProduct(String fromDate,String toDate,int number){
        String stanUnitKeyword;
        double quantity;
        List<TranSaleModel> list=new ArrayList<>();
        TranSaleModel data;
        database = openHelper.getReadableDatabase();
        deleteTempTopSaleProduct();
        Cursor cur=database.rawQuery("SELECT Sum(ts.Quantity),Sum(ts.Amount),p.ProductCode,p.ProductName,ts.UnitKeyword,c.CategoryName,UnitType,ts.IsProductUnit,ts.ProductID FROM TTranSale ts INNER JOIN Product p on ts.ProductID=p.ProductID INNER JOIN Category c on p.CategoryID=c.CategoryID INNER JOIN TMasterSale ms on ts.MasterSaleID=ms.MasterSaleID WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY p.ProductCode,p.ProductName,ts.UnitKeyword,c.CategoryName,UnitType,ts.IsProductUnit,ts.ProductID",null);
        while (cur.moveToNext()){
            data=new TranSaleModel();
            if(cur.getInt(7)==1 && !cur.getString(6).equals(systemInfo.StandardUnit)) {
                stanUnitKeyword = getStandardUnitKeyword(cur.getInt(8));
                quantity = changeStandardUnit(cur.getString(6), cur.getInt(8), cur.getDouble(0));
                data.setdQuantity(quantity);
                data.setUnitKeyword(stanUnitKeyword);
            }else{
                data.setdQuantity(cur.getDouble(0));
                data.setUnitKeyword(cur.getString(4));
            }

            data.setAmount(cur.getInt(1));
            data.setProductCode(cur.getString(2));
            data.setProductName(cur.getString(3));
            data.setCategoryName(cur.getString(5));
            insertTempTopSaleProduct(data.getProductCode(),data.getProductName(),data.getCategoryName(),data.getUnitKeyword(),data.getdQuantity(),data.getAmount());
        }

        cur=database.rawQuery("SELECT ProductCode,ProductName,CategoryName,UnitKeyword,Sum(Quantity),Sum(Amount) FROM TempTopSaleProduct GROUP BY ProductCode,ProductName,CategoryName,UnitKeyword ORDER BY Sum(Quantity) DESC LIMIT " + number,null);
        while (cur.moveToNext()){
            data=new TranSaleModel();
            data.setProductCode(cur.getString(0));
            data.setProductName(cur.getString(1));
            data.setCategoryName(cur.getString(2));
            data.setUnitKeyword(cur.getString(3));
            data.setdQuantity(cur.getDouble(4));
            data.setAmount(cur.getInt(5));
            list.add(data);
        }
        return list;
    }
    private void deleteTempTopSaleProduct(){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TempTopSaleProduct");
    }
    private void insertTempTopSaleProduct(String productCode,String productName,String categoryName,String unitKeyword,double quantity,int amount){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ProductCode", productCode);
        cv.put("ProductName", productName);
        cv.put("CategoryName", categoryName);
        cv.put("UnitKeyword", unitKeyword);
        cv.put("Quantity", quantity);
        cv.put("Amount", amount);
        database.insert("TempTopSaleProduct", null, cv);
    }
    private String getStandardUnitKeyword(int productId){
        String result="";
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        Cursor cur=dbRead.rawQuery("SELECT UnitKeyword FROM Product p INNER JOIN Unit u ON p.StandardUnitID=u.UnitID WHERE ProductID="+productId,null);
        if(cur.moveToFirst())result=cur.getString(0);
        return result;
    }
    private int getStandardUnitID(int productId){
        int result=0;
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        Cursor cur=dbRead.rawQuery("SELECT p.StandardUnitID FROM Product p INNER JOIN Unit u ON p.StandardUnitID=u.UnitID WHERE ProductID="+productId,null);
        if(cur.moveToFirst())result=cur.getInt(0);
        return result;
    }
    private double changeStandardUnit(String unitType,int productId,double quantity){
        double result=0;
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        Cursor cur=null;
        int convertQty,stanConvertQty;

        if(unitType.equals(systemInfo.SaleUnit))cur=dbRead.rawQuery("SELECT SaleUnitQty,StandardSaleUnitQty FROM Product WHERE ProductID="+productId,null);
        else if(unitType.equals(systemInfo.PurchaseUnit))cur=dbRead.rawQuery("SELECT PurUnitQty,StandardPurUnitQty FROM Product WHERE ProductID="+productId,null);
        if(cur.moveToFirst()){
            convertQty=cur.getInt(0);
            stanConvertQty=cur.getInt(1);

            if(convertQty>stanConvertQty){
                result=quantity/convertQty;
            }else if(convertQty<=stanConvertQty){
                result=quantity*convertQty;
            }
        }
        return result;
    }
    public List<MasterPurchaseModel> getMasterPurchaseByFromToDate(String fromDate, String toDate){
        List<MasterPurchaseModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur=database.rawQuery("SELECT MasterPurchaseID,VoucherNo,SupplierName,NetAmount,PurchaseTime,PurchaseDate,TotalAmount,PayDisPercent,PayDisAmount,TotalDisAmount,IsCredit,CreditRemark,UserName,LastDebtAmount,PaidAmount,ChangeAmount,DebtAmount,IsDebtAmount,SupplierID FROM TMasterPurchase WHERE PurchaseDate BETWEEN '" + fromDate + "' AND '" + toDate + "' ORDER BY VoucherNo",null);
        while(cur.moveToNext()){
            MasterPurchaseModel data=new MasterPurchaseModel();
            data.setId(cur.getInt(0));
            data.setVoucherNumber(cur.getInt(1));
            data.setSupplierName(cur.getString(2));
            data.setNetAmount(cur.getInt(3));
            data.setTime(cur.getString(4));
            data.setDate(cur.getString(5));
            data.setTotalAmount(cur.getInt(6));
            data.setPayDisPercent(cur.getInt(7));
            data.setPayDisAmount(cur.getInt(8));
            data.setTotalDisAmount(cur.getInt(9));
            data.setIsCredit(cur.getInt(10));
            data.setCreditRemark(cur.getString(11));
            data.setUserName(cur.getString(12));
            data.setLastDebtAmount(cur.getInt(13));
            data.setPaidAmount(cur.getInt(14));
            data.setChangeAmount(cur.getInt(15));
            data.setDebtAmount(cur.getInt(16));
            data.setIsDebtAmount(cur.getInt(17));
            data.setSupplierId(cur.getInt(18));
            list.add(data);
        }
        return list;
    }
    private void deleteTempQtyRpProBal(){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TempQtyRpProBal");
    }
    private void insertTempQtyRpProBal(int productId,int unitId,int quantity){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ProductID", productId);
        cv.put("UnitID", unitId);
        cv.put("Quantity", quantity);
        database.insert("TempQtyRpProBal", null, cv);
    }
    private void deleteTempQtyRpPurTran(){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TempQtyRpPurTran");
    }
    private void insertUpdateTempQtyRpPurTran(int productId,int unitId,double quantity){
        double curQuantity;
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        database = openHelper.getWritableDatabase();
        Cursor cur=dbRead.rawQuery("SELECT ProductID,UnitID,Quantity FROM TempQtyRpPurTran WHERE ProductID="+productId+" AND UnitID="+unitId,null);
        if(cur.moveToFirst()){
            curQuantity=cur.getDouble(2);

            ContentValues cv = new ContentValues();
            cv.put("Quantity", curQuantity+quantity);
            database.update("TempQtyRpPurTran", cv, "ProductID=" + productId + " AND " + "UnitID=" + unitId, null);
        }else{
            ContentValues cv = new ContentValues();
            cv.put("ProductID", productId);
            cv.put("UnitID", unitId);
            cv.put("Quantity", quantity);
            database.insert("TempQtyRpPurTran", null, cv);
        }
    }
    private void deleteTempQtyRpSaleTran(){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TempQtyRpSaleTran");
    }
    private void insertUpdateTempQtyRpSaleTran(int productId,int unitId,double quantity){
        double curQuantity;
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        database = openHelper.getWritableDatabase();
        Cursor cur=dbRead.rawQuery("SELECT ProductID,UnitID,Quantity FROM TempQtyRpSaleTran WHERE ProductID="+productId+" AND UnitID="+unitId,null);
        if(cur.moveToFirst()){
            curQuantity=cur.getDouble(2);

            ContentValues cv = new ContentValues();
            cv.put("Quantity", curQuantity+quantity);
            database.update("TempQtyRpSaleTran", cv, "ProductID=" + productId + " AND " + "UnitID=" + unitId, null);
        }else {
            ContentValues cv = new ContentValues();
            cv.put("ProductID", productId);
            cv.put("UnitID", unitId);
            cv.put("Quantity", quantity);
            database.insert("TempQtyRpSaleTran", null, cv);
        }
    }
    private void deleteTempQtyRpAdjustTran(){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TempQtyRpAdjustTran");
    }
    private void insertUpdateTempQtyRpAdjustTran(int productId,int unitId,double quantity){
        double curQuantity;
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        database = openHelper.getWritableDatabase();
        Cursor cur=dbRead.rawQuery("SELECT ProductID,UnitID,Quantity FROM TempQtyRpAdjustTran WHERE ProductID="+productId+" AND UnitID="+unitId,null);
        if(cur.moveToFirst()){
            curQuantity=cur.getDouble(2);

            ContentValues cv = new ContentValues();
            cv.put("Quantity", curQuantity+quantity);
            database.update("TempQtyRpAdjustTran", cv, "ProductID=" + productId + " AND " + "UnitID=" + unitId, null);
        }else {
            ContentValues cv = new ContentValues();
            cv.put("ProductID", productId);
            cv.put("UnitID", unitId);
            cv.put("Quantity", quantity);
            database.insert("TempQtyRpAdjustTran", null, cv);
        }
    }
    private void deleteTempProductQuantity(){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM TempProductQuantity");
    }
    private void insertTempProductQuantity(int productId,int unitId,double curOpeningStock){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ProductID", productId);
        cv.put("UnitID", unitId);
        cv.put("CurOpeningStock", curOpeningStock);
        database.insert("TempProductQuantity", null, cv);
    }
    public List<ProductModel> getProductQuantityByFromToDate(String fromDate, String toDate){
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        int stanUnitId;
        double quantity,curOpeningStock;
        List<ProductModel> lstProduct=new ArrayList<>();

        deleteTempQtyRpProBal();
        deleteTempQtyRpPurTran();
        deleteTempQtyRpSaleTran();
        deleteTempQtyRpAdjustTran();
        deleteTempProductQuantity();

        Cursor cur=dbRead.rawQuery("SELECT ProductID,StandardUnitID,Quantity FROM Product",null);
        while(cur.moveToNext()) {
            insertTempQtyRpProBal(cur.getInt(0),cur.getInt(1),cur.getInt(2));
        }

        cur=dbRead.rawQuery("SELECT ProductID,UnitID,IsProductUnit,UnitType,Sum(Quantity) FROM TTranPurchase t INNER JOIN TMasterPurchase m ON t.MasterPurchaseID=m.MasterPurchaseID WHERE PurchaseDate < '"+fromDate+"' GROUP BY ProductID,UnitID,IsProductUnit,UnitType",null);
        while (cur.moveToNext()){
            TranPurchaseModel data=new TranPurchaseModel();
            if(cur.getInt(2)==1 && !cur.getString(3).equals(systemInfo.StandardUnit)) {
                stanUnitId = getStandardUnitID(cur.getInt(0));
                quantity = changeStandardUnit(cur.getString(3), cur.getInt(0), cur.getDouble(4));
                data.setdQuantity(quantity);
                data.setUnitId(stanUnitId);
            }else{
                data.setdQuantity(cur.getDouble(4));
                data.setUnitId(cur.getInt(1));
            }
            data.setProductId(cur.getInt(0));
            insertUpdateTempQtyRpPurTran(data.getProductId(),data.getUnitId(),data.getdQuantity());
        }

        cur=dbRead.rawQuery("SELECT ProductID,UnitID,IsProductUnit,UnitType,Sum(Quantity) FROM TTranSale t INNER JOIN TMasterSale m ON t.MasterSaleID=m.MasterSaleID WHERE SaleDate < '"+fromDate+"' GROUP BY ProductID,UnitID,IsProductUnit,UnitType",null);
        while (cur.moveToNext()){
            TranSaleModel data=new TranSaleModel();
            if(cur.getInt(2)==1 && !cur.getString(3).equals(systemInfo.StandardUnit)) {
                stanUnitId = getStandardUnitID(cur.getInt(0));
                quantity = changeStandardUnit(cur.getString(3), cur.getInt(0), cur.getDouble(4));
                data.setdQuantity(quantity);
                data.setUnitId(stanUnitId);
            }else{
                data.setdQuantity(cur.getDouble(4));
                data.setUnitId(cur.getInt(1));
            }
            data.setProductId(cur.getInt(0));
            insertUpdateTempQtyRpSaleTran(data.getProductId(),data.getUnitId(),data.getdQuantity());
        }

        cur=dbRead.rawQuery("SELECT ProductID,UnitID,IsProductUnit,UnitType,Sum(Quantity) FROM TTranAdjustment t INNER JOIN TMasterAdjustment m ON t.MasterAdjustmentID=m.MasterAdjustmentID WHERE AdjustmentDate < '"+fromDate+"' GROUP BY ProductID,UnitID,IsProductUnit,UnitType",null);
        while (cur.moveToNext()){
            TranAdjustmentModel data=new TranAdjustmentModel();
            if(cur.getInt(2)==1 && !cur.getString(3).equals(systemInfo.StandardUnit)) {
                stanUnitId = getStandardUnitID(cur.getInt(0));
                quantity = changeStandardUnit(cur.getString(3), cur.getInt(0), cur.getDouble(4));
                data.setdQuantity(quantity);
                data.setUnitId(stanUnitId);
            }else{
                data.setdQuantity(cur.getDouble(4));
                data.setUnitId(cur.getInt(1));
            }
            data.setProductId(cur.getInt(0));
            insertUpdateTempQtyRpAdjustTran(data.getProductId(),data.getUnitId(),data.getdQuantity());
        }

        cur=dbRead.rawQuery("SELECT bal.ProductID,bal.UnitID,bal.Quantity,pur.Quantity,sale.Quantity,adj.Quantity FROM TempQtyRpProBal bal LEFT JOIN TempQtyRpPurTran pur ON bal.ProductID=pur.ProductID LEFT JOIN TempQtyRpSaleTran sale ON bal.ProductID=sale.ProductID LEFT JOIN TempQtyRpAdjustTran adj ON bal.ProductID=adj.ProductID",null);
        while(cur.moveToNext()) {
            curOpeningStock=(cur.getInt(2)+cur.getDouble(3))-(cur.getDouble(4)+cur.getDouble(5));
            insertTempProductQuantity(cur.getInt(0),cur.getInt(1),curOpeningStock);
        }

        deleteTempQtyRpPurTran();
        cur=dbRead.rawQuery("SELECT ProductID,UnitID,IsProductUnit,UnitType,Sum(Quantity) FROM TTranPurchase t INNER JOIN TMasterPurchase m ON t.MasterPurchaseID=m.MasterPurchaseID WHERE PurchaseDate BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY ProductID,UnitID,IsProductUnit,UnitType",null);
        while (cur.moveToNext()){
            TranPurchaseModel data=new TranPurchaseModel();
            if(cur.getInt(2)==1 && !cur.getString(3).equals(systemInfo.StandardUnit)) {
                stanUnitId = getStandardUnitID(cur.getInt(0));
                quantity = changeStandardUnit(cur.getString(3), cur.getInt(0), cur.getDouble(4));
                data.setdQuantity(quantity);
                data.setUnitId(stanUnitId);
            }else{
                data.setdQuantity(cur.getDouble(4));
                data.setUnitId(cur.getInt(1));
            }
            data.setProductId(cur.getInt(0));
            insertUpdateTempQtyRpPurTran(data.getProductId(),data.getUnitId(),data.getdQuantity());
        }

        deleteTempQtyRpSaleTran();
        cur=dbRead.rawQuery("SELECT ProductID,UnitID,IsProductUnit,UnitType,Sum(Quantity) FROM TTranSale t INNER JOIN TMasterSale m ON t.MasterSaleID=m.MasterSaleID WHERE SaleDate BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY ProductID,UnitID,IsProductUnit,UnitType",null);
        while (cur.moveToNext()){
            TranSaleModel data=new TranSaleModel();
            if(cur.getInt(2)==1 && !cur.getString(3).equals(systemInfo.StandardUnit)) {
                stanUnitId = getStandardUnitID(cur.getInt(0));
                quantity = changeStandardUnit(cur.getString(3), cur.getInt(0), cur.getDouble(4));
                data.setdQuantity(quantity);
                data.setUnitId(stanUnitId);
            }else{
                data.setdQuantity(cur.getDouble(4));
                data.setUnitId(cur.getInt(1));
            }
            data.setProductId(cur.getInt(0));
            insertUpdateTempQtyRpSaleTran(data.getProductId(),data.getUnitId(),data.getdQuantity());
        }

        deleteTempQtyRpAdjustTran();
        cur=dbRead.rawQuery("SELECT ProductID,UnitID,IsProductUnit,UnitType,Sum(Quantity) FROM TTranAdjustment t INNER JOIN TMasterAdjustment m ON t.MasterAdjustmentID=m.MasterAdjustmentID WHERE AdjustmentDate BETWEEN '" + fromDate + "' AND '" + toDate + "' GROUP BY ProductID,UnitID,IsProductUnit,UnitType",null);
        while (cur.moveToNext()){
            TranAdjustmentModel data=new TranAdjustmentModel();
            if(cur.getInt(2)==1 && !cur.getString(3).equals(systemInfo.StandardUnit)) {
                stanUnitId = getStandardUnitID(cur.getInt(0));
                quantity = changeStandardUnit(cur.getString(3), cur.getInt(0), cur.getDouble(4));
                data.setdQuantity(quantity);
                data.setUnitId(stanUnitId);
            }else{
                data.setdQuantity(cur.getDouble(4));
                data.setUnitId(cur.getInt(1));
            }
            data.setProductId(cur.getInt(0));
            insertUpdateTempQtyRpAdjustTran(data.getProductId(),data.getUnitId(),data.getdQuantity());
        }

        cur=dbRead.rawQuery("SELECT qty.ProductID,qty.UnitID,qty.CurOpeningStock,pur.Quantity,sale.Quantity,p.ProductCode,p.ProductName,c.CategoryName,u.UnitKeyword,adj.Quantity FROM TempProductQuantity qty LEFT JOIN TempQtyRpPurTran pur ON qty.ProductID=pur.ProductID LEFT JOIN TempQtyRpSaleTran sale ON qty.ProductID=sale.ProductID LEFT JOIN TempQtyRpAdjustTran adj ON qty.ProductID=adj.ProductID INNER JOIN Product p ON qty.ProductID=p.ProductID INNER JOIN Category c ON p.CategoryID=c.CategoryID LEFT JOIN Unit u ON qty.UnitID=u.UnitID ORDER BY c.CategoryID,p.ProductCode",null);
        while(cur.moveToNext()) {
            ProductModel data=new ProductModel();
            data.setProductId(cur.getInt(0));
            data.setStandardUnitId(cur.getInt(1));
            data.setdOpeningQuantity(cur.getDouble(2));
            data.setdPurQuantity(cur.getDouble(3));
            data.setdSaleQuantity(cur.getDouble(4));
            data.setBalQuantity((cur.getDouble(2)+cur.getDouble(3))-(cur.getDouble(4)+cur.getDouble(9)));
            data.setProductCode(cur.getString(5));
            data.setProductName(cur.getString(6));
            data.setCategoryName(cur.getString(7));
            data.setStandardUnitKeyword(cur.getString(8));
            data.setdAdjustQuantity(cur.getDouble(9));
            lstProduct.add(data);
        }

        return lstProduct;
    }
    /**
     * trial function
     */
    public boolean insertTrial(int isTrial,int trialAllowDay,String startDate){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("IsTrial", isTrial);
        cv.put("TrialAllowDay", trialAllowDay);
        cv.put("startDate", startDate);
        database.insert("Trial", null, cv);
        return true;
    }
    public boolean updateNotTrial(){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("IsTrial", 0);
        database.update("Trial", cv, null, null);
        return true;
    }
    public int getIsTrial(){
        int result=0;
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        Cursor cur=dbRead.rawQuery("SELECT IsTrial FROM Trial",null);
        if(cur.moveToFirst())result=cur.getInt(0);
        return result;
    }
    public boolean isTrialExpired(){
        int dayDiff;
        boolean isTrialFinish=false;
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        Cursor cur=dbRead.rawQuery("SELECT IsTrial,TrialAllowDay,StartDate FROM Trial",null);
        if(cur.moveToFirst()){
            int trialAllowDay=cur.getInt(1);
//            int trialAllowDay=-1;
            String strStartDate=cur.getString(2);
            String strTodayDate=systemInfo.getTodayDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat(systemInfo.DATE_FORMAT);
            try{
                Date startDate = dateFormat.parse(strStartDate);
                Date todayDate = dateFormat.parse(strTodayDate);
                dayDiff=getDaysDifference(startDate,todayDate);
                if(dayDiff > trialAllowDay) isTrialFinish=true;
            }catch (ParseException ex){

            }
        }
        return isTrialFinish;
    }
    public static int getDaysDifference(Date startDate,Date todayDate)
    {
        if(startDate==null||todayDate==null) return 0;

        return (int)( (todayDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }
    /**
     * end user setting function
     */
    public boolean insertEndUserSetting(String macAddress,String mobileNumber,int isAppPurchase,String name,String purchasePlan,String amount,String orderDate,String startDate,String endDate){
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM EndUserSetting");
        ContentValues cv = new ContentValues();
        cv.put("MacAddress", macAddress);
        cv.put("MobileNumber", mobileNumber);
        cv.put("IsAppPurchase", isAppPurchase);
        cv.put("Name", name);
        cv.put("PurchasePlan", purchasePlan);
        cv.put("Amount", amount);
        cv.put("OrderDate", orderDate);
        cv.put("StartDate", startDate);
        cv.put("EndDate", endDate);
        database.insert("EndUserSetting", null, cv);
        return true;
    }
    public boolean updateNotAppPurchase(){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("IsAppPurchase", 0);
        database.update("EndUserSetting", cv, null, null);
        return true;
    }
    public int getIsAppPurchase(){
        int result=0;
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        Cursor cur=dbRead.rawQuery("SELECT IsAppPurchase FROM EndUserSetting",null);
        if(cur.moveToFirst())result=cur.getInt(0);
        return result;
    }
    public boolean isAppPurchaseExpired(){
        int startEndDayDiff,startTodayDayDiff;
        boolean isAppPurchaseFinish=false;
        SQLiteDatabase dbRead = openHelper.getReadableDatabase();
        Cursor cur=dbRead.rawQuery("SELECT StartDate,EndDate FROM EndUserSetting",null);
        if(cur.moveToFirst()){
            String strStartDate=cur.getString(0);
            String strEndDate=cur.getString(1);
            String strTodayDate=systemInfo.getTodayDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat(systemInfo.DATE_FORMAT);
            try{
                Date startDate = dateFormat.parse(strStartDate);
                Date endDate = dateFormat.parse(strEndDate);
                Date todayDate = dateFormat.parse(strTodayDate);
                startEndDayDiff=getDaysDifference(startDate,endDate);
                startTodayDayDiff=getDaysDifference(startDate,todayDate);
                if(startEndDayDiff < startTodayDayDiff) isAppPurchaseFinish=true;
//                isAppPurchaseFinish=true;
            }catch (ParseException ex){

            }
        }
        return isAppPurchaseFinish;
    }
    /**
     * payable function
     */
    public boolean insertPayable(int supplierId,int debtAmount,int paidAmount,String date,String remark){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("SupplierID", supplierId);
        cv.put("DebtAmount", debtAmount);
        cv.put("PaidAmount", paidAmount);
        cv.put("Date", date);
        cv.put("Remark", remark);
        database.insert("Payable", null, cv);
        return true;
    }
    public List<PayableModel> getPayable(int supplierId, String fromDate, String toDate){
        List<PayableModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur;
        if(supplierId==0) cur=database.rawQuery("SELECT ID,r.SupplierID,r.DebtAmount,PaidAmount,Date,Remark,SupplierName FROM Payable r INNER JOIN Supplier c ON r.SupplierID=c.SupplierID WHERE Date BETWEEN '" + fromDate + "' AND '" + toDate + "'",null);
        else cur=database.rawQuery("SELECT ID,r.SupplierID,r.DebtAmount,PaidAmount,Date,Remark,SupplierName FROM Payable r INNER JOIN Supplier c ON r.SupplierID=c.SupplierID WHERE r.SupplierID=" + supplierId + " AND Date BETWEEN '" + fromDate + "' AND '" + toDate + "'",null);
        while(cur.moveToNext()){
            PayableModel data=new PayableModel();
            data.setId(cur.getInt(0));
            data.setSupplierId(cur.getInt(1));
            data.setDebtAmount(cur.getInt(2));
            data.setPaidAmount(cur.getInt(3));
            data.setDate(cur.getString(4));
            data.setRemark(cur.getString(5));
            data.setSupplierName(cur.getString(6));
            list.add(data);
        }
        return list;
    }
    public void deletePayable(int id) {
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM Payable WHERE ID="+id);
    }
    /**
     * receivable function
     */
    public boolean insertReceivable(int customerId,int debtAmount,int paidAmount,String date,String remark){
        database = openHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CustomerID", customerId);
        cv.put("DebtAmount", debtAmount);
        cv.put("PaidAmount", paidAmount);
        cv.put("Date", date);
        cv.put("Remark", remark);
        database.insert("Receivable", null, cv);
        return true;
    }
    public List<ReceivableModel> getReceivable(int customerId,String fromDate, String toDate){
        List<ReceivableModel> list=new ArrayList<>();
        database = openHelper.getReadableDatabase();
        Cursor cur;
        if(customerId==0) cur=database.rawQuery("SELECT ID,r.CustomerID,r.DebtAmount,PaidAmount,Date,Remark,CustomerName FROM Receivable r INNER JOIN Customer c ON r.CustomerID=c.CustomerID WHERE Date BETWEEN '" + fromDate + "' AND '" + toDate + "'",null);
        else cur=database.rawQuery("SELECT ID,r.CustomerID,r.DebtAmount,PaidAmount,Date,Remark,CustomerName FROM Receivable r INNER JOIN Customer c ON r.CustomerID=c.CustomerID WHERE r.CustomerID=" + customerId + " AND Date BETWEEN '" + fromDate + "' AND '" + toDate + "'",null);
        while(cur.moveToNext()){
            ReceivableModel data=new ReceivableModel();
            data.setId(cur.getInt(0));
            data.setCustomerId(cur.getInt(1));
            data.setDebtAmount(cur.getInt(2));
            data.setPaidAmount(cur.getInt(3));
            data.setDate(cur.getString(4));
            data.setRemark(cur.getString(5));
            data.setCustomerName(cur.getString(6));
            list.add(data);
        }
        return list;
    }
    public void deleteReceivable(int id) {
        database = openHelper.getWritableDatabase();
        database.execSQL("DELETE FROM Receivable WHERE ID="+id);
    }
}
