package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import common.SystemInfo;
import database.DatabaseAccess;
import model.PrintBillModel;
import model.TranSaleModel;

/**
 * Created by User on 7/30/2018.
 */
public class PrintSaleVoucherAdapter extends BaseAdapter {
    private Context context;
    List<PrintBillModel> lstPrintBillModel;
    DatabaseAccess db;
    SystemInfo systemInfo=new SystemInfo();

    public PrintSaleVoucherAdapter(Context context, List<PrintBillModel> lstPrintBillModel){
        this.context=context;
        db=new DatabaseAccess(this.context);
        this.lstPrintBillModel = lstPrintBillModel;
    }

    @Override
    public int getCount(){
        return lstPrintBillModel.size();
    }

    @Override
    public String getItem(int position){
        return lstPrintBillModel.get(position).getShopName();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    static class ViewHolder {
        TextView tvShopName, tvShopDesc, tvAddress, tvPhone, tvDate,tvVoucherNo, tvCustomer,
                tvPayType, tvTotalAmt, tvDiscount, tvNetAmt, tvFooterMessage1, tvFooterMessage2,
                tvRemark,tvLastDebtAmt,tvPaidAmt,tvChangeAmt,tvDebtAmt;
        ImageView imgLogo;
        LinearLayout layoutPrintList,layoutTotalAmt,layoutDiscount,layoutNetAmt,layoutLastDebtAmt,layoutPaidAmt,layoutChangeAmt,layoutDebtAmt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_print_sale_voucher, parent,false);
            holder = new ViewHolder();
            holder.imgLogo = row.findViewById(R.id.imgLogo);
            holder.tvShopName = row.findViewById(R.id.tvShopName);
            holder.tvShopDesc = row.findViewById(R.id.tvShopDesc);
            holder.tvAddress = row.findViewById(R.id.tvAddress);
            holder.tvPhone = row.findViewById(R.id.tvPhone);
            holder.tvDate = row.findViewById(R.id.tvDate);
            holder.tvVoucherNo = row.findViewById(R.id.tvVoucherNo);
            holder.tvCustomer = row.findViewById(R.id.tvCustomer);
            holder.tvPayType = row.findViewById(R.id.tvPayType);
            holder.tvTotalAmt = row.findViewById(R.id.tvTotalAmt);
            holder.tvDiscount = row.findViewById(R.id.tvDiscount);
            holder.tvNetAmt = row.findViewById(R.id.tvNetAmt);
            holder.tvLastDebtAmt = row.findViewById(R.id.tvLastDebtAmt);
            holder.tvPaidAmt = row.findViewById(R.id.tvPaidAmt);
            holder.tvChangeAmt = row.findViewById(R.id.tvChangeAmt);
            holder.tvDebtAmt = row.findViewById(R.id.tvDebtAmt);
            holder.tvFooterMessage1 = row.findViewById(R.id.tvFooterMessage1);
            holder.tvFooterMessage2 = row.findViewById(R.id.tvFooterMessage2);
            holder.tvRemark = row.findViewById(R.id.tvRemark);
            holder.layoutPrintList = row.findViewById(R.id.layoutPrintList);
            holder.layoutTotalAmt = row.findViewById(R.id.layoutTotalAmt);
            holder.layoutDiscount = row.findViewById(R.id.layoutDiscount);
            holder.layoutNetAmt = row.findViewById(R.id.layoutNetAmt);
            holder.layoutLastDebtAmt = row.findViewById(R.id.layoutLastDebtAmt);
            holder.layoutPaidAmt = row.findViewById(R.id.layoutPaidAmt);
            holder.layoutChangeAmt = row.findViewById(R.id.layoutChangeAmt);
            holder.layoutDebtAmt = row.findViewById(R.id.layoutDebtAmt);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        if(lstPrintBillModel.get(position).getShopName()!=null) {
            if (lstPrintBillModel.get(position).getShopName().trim().length() == 0)
                holder.tvShopName.setVisibility(View.GONE);
            else holder.tvShopName.setText(lstPrintBillModel.get(position).getShopName());
        }else{
            holder.tvShopName.setVisibility(View.GONE);
        }

        if(lstPrintBillModel.get(position).getDescription()!=null) {
            if (lstPrintBillModel.get(position).getDescription().trim().length() == 0)
                holder.tvShopDesc.setVisibility(View.GONE);
            else holder.tvShopDesc.setText(lstPrintBillModel.get(position).getDescription());
        }else{
            holder.tvShopDesc.setVisibility(View.GONE);
        }

        if(lstPrintBillModel.get(position).getAddress()!=null) {
            if (lstPrintBillModel.get(position).getAddress().trim().length() == 0)
                holder.tvAddress.setVisibility(View.GONE);
            else holder.tvAddress.setText(lstPrintBillModel.get(position).getAddress());
        }else{
            holder.tvAddress.setVisibility(View.GONE);
        }

        if(lstPrintBillModel.get(position).getPhone()!=null) {
            if (lstPrintBillModel.get(position).getPhone().trim().length() == 0)
                holder.tvPhone.setVisibility(View.GONE);
            else holder.tvPhone.setText(lstPrintBillModel.get(position).getPhone());
        }else{
            holder.tvPhone.setVisibility(View.GONE);
        }

        if(lstPrintBillModel.get(position).getFooterMessage1()!=null) {
            if (lstPrintBillModel.get(position).getFooterMessage1().trim().length() == 0)
                holder.tvFooterMessage1.setVisibility(View.GONE);
            else holder.tvFooterMessage1.setText(lstPrintBillModel.get(position).getFooterMessage1());
        }else{
            holder.tvFooterMessage1.setVisibility(View.GONE);
        }

        if(lstPrintBillModel.get(position).getFooterMessage2()!=null) {
            if (lstPrintBillModel.get(position).getFooterMessage2().trim().length() == 0)
                holder.tvFooterMessage2.setVisibility(View.GONE);
            else holder.tvFooterMessage2.setText(lstPrintBillModel.get(position).getFooterMessage2());
        }else{
            holder.tvFooterMessage2.setVisibility(View.GONE);
        }

        if(lstPrintBillModel.get(position).getRemark()!=null) {
            if (lstPrintBillModel.get(position).getRemark().trim().length() == 0)
                holder.tvRemark.setVisibility(View.GONE);
            else holder.tvRemark.setText(lstPrintBillModel.get(position).getRemark());
        }else{
            holder.tvRemark.setVisibility(View.GONE);
        }

        holder.tvVoucherNo.setText(lstPrintBillModel.get(position).getVoucherNo());
        holder.tvDate.setText(lstPrintBillModel.get(position).getPrintDate());
        holder.tvCustomer.setText(lstPrintBillModel.get(position).getCustomer());
        holder.tvPayType.setText(lstPrintBillModel.get(position).getPayType());

        if(lstPrintBillModel.size()-1 == position) {
            holder.tvTotalAmt.setText(String.valueOf(systemInfo.df.format(lstPrintBillModel.get(position).getTotalAmount())));
            holder.tvDiscount.setText(String.valueOf(systemInfo.df.format(lstPrintBillModel.get(position).getDiscount())));
            holder.tvNetAmt.setText(String.valueOf(systemInfo.df.format(lstPrintBillModel.get(position).getNetAmount())));
            if(lstPrintBillModel.get(position).getCustomerId()!=0){
                holder.layoutLastDebtAmt.setVisibility(View.VISIBLE);
                holder.layoutPaidAmt.setVisibility(View.VISIBLE);
                if(lstPrintBillModel.get(position).getIsDebtAmount()==1) {
                    holder.layoutDebtAmt.setVisibility(View.VISIBLE);
                    holder.tvDebtAmt.setText(String.valueOf(systemInfo.df.format(lstPrintBillModel.get(position).getDebtAmount())));
                }
                else {
                    holder.layoutChangeAmt.setVisibility(View.VISIBLE);
                    holder.tvChangeAmt.setText(String.valueOf(systemInfo.df.format(lstPrintBillModel.get(position).getChangeAmount())));
                }
                holder.tvLastDebtAmt.setText(String.valueOf(systemInfo.df.format(lstPrintBillModel.get(position).getLastDebtAmount())));
                holder.tvPaidAmt.setText(String.valueOf(systemInfo.df.format(lstPrintBillModel.get(position).getPaidAmount())));
            }
        }else{
            holder.tvTotalAmt.setVisibility(View.GONE);
            holder.tvDiscount.setVisibility(View.GONE);
            holder.tvNetAmt.setVisibility(View.GONE);
        }

        for (int i = 0; i< lstPrintBillModel.get(position).getLstTranSale().size(); i++) {
            TranSaleModel data = lstPrintBillModel.get(position).getLstTranSale().get(i);
            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row1 = layoutInflater.inflate(R.layout.list_print_sale_data, null);
            TextView tvItemProduct = row1.findViewById(R.id.tvItemProduct);
            TextView tvItemQty = row1.findViewById(R.id.tvItemQty);
            TextView tvItemPrice = row1.findViewById(R.id.tvItemPrice);
            TextView tvItemAmount = row1.findViewById(R.id.tvItemAmount);

            tvItemProduct.setText(data.getProductName());
            if(data.getUnitId() != 0) tvItemQty.setText(data.getQuantity()+ "(" + data.getUnitKeyword() + ")");
            else tvItemQty.setText(String.valueOf(data.getQuantity()));
            tvItemPrice.setText(String.valueOf(systemInfo.df.format(data.getSalePrice())));
            tvItemAmount.setText(String.valueOf(systemInfo.df.format(data.getAmount())));
            holder.layoutPrintList.addView(row1);
        }

        return row;
    }

}
