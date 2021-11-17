package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import common.SystemInfo;
import model.TranSaleModel;

public class SaleTranAdapter extends BaseAdapter {
    private Context context;
    List<TranSaleModel> lstTranSale;
    SystemInfo systemInfo=new SystemInfo();

    public SaleTranAdapter(Context context, List<TranSaleModel> lstTranSale){
        this.context=context;
        this.lstTranSale = lstTranSale;
    }

    @Override
    public int getCount(){
        return lstTranSale.size();
    }

    @Override
    public String getItem(int position){
        return lstTranSale.get(position).getProductName();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    static class ViewHolder {
        TextView tvProductName,tvQtySPrice,tvAmount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_sale_tran, parent,false);
            holder = new ViewHolder();
            holder.tvProductName =row.findViewById(R.id.tvProductName);
            holder.tvQtySPrice =row.findViewById(R.id.tvQtySPrice);
            holder.tvAmount =row.findViewById(R.id.tvAmount);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        if(lstTranSale.get(position).getIsProductUnit() != 0)
            holder.tvProductName.setText(lstTranSale.get(position).getProductName() + "(" + lstTranSale.get(position).getUnitKeyword() + ")");
        else holder.tvProductName.setText(lstTranSale.get(position).getProductName());
        holder.tvQtySPrice.setText(String.valueOf(lstTranSale.get(position).getQuantity() + " * " + systemInfo.df.format(lstTranSale.get(position).getSalePrice())));
        holder.tvAmount.setText(String.valueOf(systemInfo.df.format(lstTranSale.get(position).getAmount())));

        return row;
    }
}
