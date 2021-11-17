package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.CustomerListActivity;
import com.developerstar.pos.R;

import java.util.List;

import common.SystemInfo;
import listener.CustomerListListener;
import model.CustomerModel;

public class CustomerListAdapter extends BaseAdapter {
    private Context context;
    List<CustomerModel> lstCustomer;
    CustomerListListener customerListListener;

    public CustomerListAdapter(Context context, List<CustomerModel> lstCustomer){
        this.context=context;
        this.lstCustomer = lstCustomer;
    }

    public void setOnEventListener(CustomerListListener customerListListener){
        this.customerListListener=customerListListener;
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
        TextView tvCustomer,tvMobileNumber;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_customer, parent,false);
            holder = new ViewHolder();
            holder.tvCustomer =row.findViewById(R.id.tvCustomer);
            holder.tvMobileNumber =row.findViewById(R.id.tvMobileNumber);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        holder.tvCustomer.setText(lstCustomer.get(position).getCustomerName());
        if(lstCustomer.get(position).getMobileNumber().length() != 0) holder.tvMobileNumber.setText(lstCustomer.get(position).getMobileNumber());
        else holder.tvMobileNumber.setText("-");

        holder.tvMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(customerListListener !=null){
                    customerListListener.onMobileClickListener(position);
                }
            }
        });

        return row;
    }
}
