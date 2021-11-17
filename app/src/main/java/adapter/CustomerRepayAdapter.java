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
import model.ReceivableModel;

public class CustomerRepayAdapter extends BaseAdapter {
    private Context context;
    List<ReceivableModel> lstReceivable;
    SystemInfo systemInfo=new SystemInfo();

    public CustomerRepayAdapter(Context context, List<ReceivableModel> lstReceivable){
        this.context=context;
        this.lstReceivable = lstReceivable;
    }

    @Override
    public int getCount(){
        return lstReceivable.size();
    }

    @Override
    public String getItem(int position){
        return lstReceivable.get(position).getCustomerName();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    public void updateResults(List<ReceivableModel> list) {
        lstReceivable = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvName,tvDate,tvDebtAmt,tvPaidAmt;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_repayment_history, parent,false);
            holder = new ViewHolder();
            holder.tvDate =row.findViewById(R.id.tvDate);
            holder.tvName =row.findViewById(R.id.tvName);
            holder.tvDebtAmt =row.findViewById(R.id.tvDebtAmt);
            holder.tvPaidAmt =row.findViewById(R.id.tvPaidAmt);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        holder.tvDate.setText(lstReceivable.get(position).getDate());
        holder.tvName.setText(lstReceivable.get(position).getCustomerName());
        holder.tvDebtAmt.setText(context.getResources().getString(R.string.receivable)+": "+String.valueOf(systemInfo.df.format(lstReceivable.get(position).getDebtAmount())));
        holder.tvPaidAmt.setText(context.getResources().getString(R.string.paid_amount)+": "+String.valueOf(systemInfo.df.format(lstReceivable.get(position).getPaidAmount())));
        return row;
    }
}
