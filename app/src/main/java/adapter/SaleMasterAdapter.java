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
import model.MasterSaleModel;

public class SaleMasterAdapter extends BaseAdapter {
    private Context context;
    List<MasterSaleModel> lstMasterSale;
    SystemInfo systemInfo=new SystemInfo();

    public SaleMasterAdapter(Context context, List<MasterSaleModel> lstMasterSale){
        this.context=context;
        this.lstMasterSale = lstMasterSale;
    }

    @Override
    public int getCount(){
        return lstMasterSale.size();
    }

    @Override
    public String getItem(int position){
        return lstMasterSale.get(position).getDate();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    public void updateResults(List<MasterSaleModel> list) {
        lstMasterSale = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvVoucherNo,tvTime,tvNetAmt,tvCustomer;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_sale_master, parent,false);
            holder = new ViewHolder();
            holder.tvVoucherNo =row.findViewById(R.id.tvVoucherNo);
            holder.tvTime =row.findViewById(R.id.tvTime);
            holder.tvNetAmt =row.findViewById(R.id.tvNetAmt);
            holder.tvCustomer =row.findViewById(R.id.tvCustomer);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        holder.tvVoucherNo.setText("#" + lstMasterSale.get(position).getVoucherNumber());
        holder.tvTime.setText(lstMasterSale.get(position).getDate() + context.getResources().getString(R.string.space) + lstMasterSale.get(position).getTime());
        holder.tvNetAmt.setText(String.valueOf(systemInfo.df.format(lstMasterSale.get(position).getNetAmount())));
        holder.tvCustomer.setText(lstMasterSale.get(position).getCustomerName());

        return row;
    }
}
