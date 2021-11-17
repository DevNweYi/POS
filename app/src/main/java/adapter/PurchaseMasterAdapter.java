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
import model.MasterPurchaseModel;

public class PurchaseMasterAdapter extends BaseAdapter {
    private Context context;
    List<MasterPurchaseModel> lstMasterPurchase;
    SystemInfo systemInfo=new SystemInfo();

    public PurchaseMasterAdapter(Context context, List<MasterPurchaseModel> lstMasterPurchase){
        this.context=context;
        this.lstMasterPurchase = lstMasterPurchase;
    }

    @Override
    public int getCount(){
        return lstMasterPurchase.size();
    }

    @Override
    public String getItem(int position){
        return lstMasterPurchase.get(position).getDate();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    public void updateResults(List<MasterPurchaseModel> list) {
        lstMasterPurchase = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvVoucherNo,tvTime,tvNetAmt,tvSupplier;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_purchase_master, parent,false);
            holder = new ViewHolder();
            holder.tvVoucherNo =row.findViewById(R.id.tvVoucherNo);
            holder.tvTime =row.findViewById(R.id.tvTime);
            holder.tvNetAmt =row.findViewById(R.id.tvNetAmt);
            holder.tvSupplier =row.findViewById(R.id.tvSupplier);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        holder.tvVoucherNo.setText("#" + lstMasterPurchase.get(position).getVoucherNumber());
        holder.tvTime.setText(lstMasterPurchase.get(position).getDate() + context.getResources().getString(R.string.space) + lstMasterPurchase.get(position).getTime());
        holder.tvNetAmt.setText(String.valueOf(systemInfo.df.format(lstMasterPurchase.get(position).getNetAmount())));
        holder.tvSupplier.setText(lstMasterPurchase.get(position).getSupplierName());

        return row;
    }
}
