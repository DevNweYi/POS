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
import model.TranPurchaseModel;

/**
 * Created by User on 7/30/2018.
 */
public class PrintPurVoucherAdapter extends BaseAdapter {
    private Context context;
    List<PrintBillModel> lstPrintBillModel;
    DatabaseAccess db;
    SystemInfo systemInfo=new SystemInfo();

    public PrintPurVoucherAdapter(Context context, List<PrintBillModel> lstPrintBillModel){
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
        TextView tvShopName, tvShopDesc, tvAddress, tvPhone, tvDate, tvSupplier;
        ImageView imgLogo;
        LinearLayout layoutPrintList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_print_pur_voucher, parent,false);
            holder = new ViewHolder();
            holder.imgLogo = row.findViewById(R.id.imgLogo);
            holder.tvShopName = row.findViewById(R.id.tvShopName);
            holder.tvShopDesc = row.findViewById(R.id.tvShopDesc);
            holder.tvAddress = row.findViewById(R.id.tvAddress);
            holder.tvPhone = row.findViewById(R.id.tvPhone);
            holder.tvDate = row.findViewById(R.id.tvDate);
            holder.tvSupplier = row.findViewById(R.id.tvSupplier);
            holder.layoutPrintList = row.findViewById(R.id.layoutPrintList);

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

        holder.tvDate.setText(lstPrintBillModel.get(position).getPrintDate());
        holder.tvSupplier.setText(lstPrintBillModel.get(position).getSupplier());

        for (int i = 0; i< lstPrintBillModel.get(position).getLstTranPurchase().size(); i++) {
            TranPurchaseModel data = lstPrintBillModel.get(position).getLstTranPurchase().get(i);
            LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row1 = layoutInflater.inflate(R.layout.list_print_pur_data, null);
            TextView tvItemProduct = row1.findViewById(R.id.tvItemProduct);
            TextView tvItemQty = row1.findViewById(R.id.tvItemQty);

            tvItemProduct.setText(data.getProductName());
            if(data.getUnitId() != 0) tvItemQty.setText(data.getQuantity()+ "(" + data.getUnitKeyword() + ")");
            else tvItemQty.setText(String.valueOf(data.getQuantity()));
            holder.layoutPrintList.addView(row1);
        }

        return row;
    }

}
