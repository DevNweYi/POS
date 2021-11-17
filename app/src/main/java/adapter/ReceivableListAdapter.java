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
import model.CustomerModel;

public class ReceivableListAdapter extends BaseAdapter {
    private Context context;
    List<CustomerModel> lstCustomer;
    SystemInfo systemInfo=new SystemInfo();

    public ReceivableListAdapter(Context context, List<CustomerModel> lstCustomer){
        this.context=context;
        this.lstCustomer = lstCustomer;
    }

    @Override
    public int getCount(){
        return lstCustomer.size();
    }

    @Override
    public String getItem(int position){
        return lstCustomer.get(position).getCustomerName();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    public void updateResults(List<CustomerModel> list) {
        lstCustomer = list;
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

        holder.tvName.setText(lstCustomer.get(position).getCustomerName());
        holder.tvDebtAmount.setText(String.valueOf(systemInfo.df.format(lstCustomer.get(position).getDebtAmount())));
        return row;
    }
}
