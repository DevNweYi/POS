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
import model.SupplierModel;

public class PayableListAdapter extends BaseAdapter {
    private Context context;
    List<SupplierModel> lstSupplier;
    SystemInfo systemInfo=new SystemInfo();

    public PayableListAdapter(Context context, List<SupplierModel> lstSupplier){
        this.context=context;
        this.lstSupplier = lstSupplier;
    }

    @Override
    public int getCount(){
        return lstSupplier.size();
    }

    @Override
    public String getItem(int position){
        return lstSupplier.get(position).getSupplierName();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    public void updateResults(List<SupplierModel> list) {
        lstSupplier = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvName,tvDebtAmount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_receivable_payable, parent,false);
            holder = new ViewHolder();
            holder.tvName =row.findViewById(R.id.tvName);
            holder.tvDebtAmount =row.findViewById(R.id.tvDebtAmount);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        holder.tvName.setText(lstSupplier.get(position).getSupplierName());
        holder.tvDebtAmount.setText(String.valueOf(systemInfo.df.format(lstSupplier.get(position).getDebtAmount())));
        return row;
    }
}
