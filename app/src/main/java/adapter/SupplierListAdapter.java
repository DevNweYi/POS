package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import listener.SupplierListListener;
import model.SupplierModel;

public class SupplierListAdapter extends BaseAdapter {
    private Context context;
    List<SupplierModel> lstSupplier;
    SupplierListListener supplierListListener;

    public SupplierListAdapter(Context context, List<SupplierModel> lstSupplier){
        this.context=context;
        this.lstSupplier = lstSupplier;
    }

    public void setOnEventListener(SupplierListListener supplierListListener){
        this.supplierListListener=supplierListListener;
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
        TextView tvSupplier,tvMobileNumber;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_supplier, parent,false);
            holder = new ViewHolder();
            holder.tvSupplier =row.findViewById(R.id.tvSupplier);
            holder.tvMobileNumber =row.findViewById(R.id.tvMobileNumber);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        holder.tvSupplier.setText(lstSupplier.get(position).getSupplierName());
        if(lstSupplier.get(position).getMobileNumber().length() != 0) holder.tvMobileNumber.setText(lstSupplier.get(position).getMobileNumber());
        else holder.tvMobileNumber.setText("-");

        holder.tvMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(supplierListListener !=null){
                    supplierListListener.onMobileClickListener(position);
                }
            }
        });

        return row;
    }
}
