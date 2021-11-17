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

public class PosAdapter extends BaseAdapter {
    private Context context;
    List<TranSaleModel> lstTranSale;
    SystemInfo systemInfo=new SystemInfo();

    public PosAdapter(Context context, List<TranSaleModel> lstTranSale){
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
        TextView tvProductName,tvQuantity,tvAmount,tvSalePrice;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_pos, parent,false);
            holder = new ViewHolder();
            holder.tvProductName =row.findViewById(R.id.tvProductName);
            holder.tvQuantity =row.findViewById(R.id.tvQuantity);
            holder.tvAmount =row.findViewById(R.id.tvAmount);
            holder.tvSalePrice =row.findViewById(R.id.tvSalePrice);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        holder.tvProductName.setText(lstTranSale.get(position).getProductName());
        holder.tvSalePrice.setText(String.valueOf(systemInfo.df.format(lstTranSale.get(position).getSalePrice())));
        if(lstTranSale.get(position).getUnitId() != 0)
            holder.tvQuantity.setText(lstTranSale.get(position).getQuantity()+ "(" + lstTranSale.get(position).getUnitKeyword() + ")");
        else holder.tvQuantity.setText(String.valueOf(lstTranSale.get(position).getQuantity()));
        holder.tvAmount.setText(String.valueOf(systemInfo.df.format(lstTranSale.get(position).getAmount())));

        return row;
    }
}
