package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import common.SystemInfo;
import listener.OpenBillListener;
import model.MasterSaleModel;

public class OpenBillAdapter extends BaseAdapter {
    private Context context;
    List<MasterSaleModel> lstMasterSale;
    OpenBillListener openBillListener;
    SystemInfo systemInfo=new SystemInfo();

    public OpenBillAdapter(Context context, List<MasterSaleModel> lstMasterSale){
        this.context=context;
        this.lstMasterSale = lstMasterSale;
    }

    public void setOnEventListener(OpenBillListener openBillListener){
        this.openBillListener=openBillListener;
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
        TextView tvVoucherNo,tvDateTime,tvNetAmt,tvCustomer,tvRemark;
        Button btnAddToPOS,btnDetail;
        ImageButton btnDelete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_open_bill, parent,false);
            holder = new ViewHolder();
            holder.tvVoucherNo =row.findViewById(R.id.tvVoucherNo);
            holder.tvDateTime =row.findViewById(R.id.tvDateTime);
            holder.tvNetAmt =row.findViewById(R.id.tvNetAmt);
            holder.tvCustomer =row.findViewById(R.id.tvCustomer);
            holder.tvRemark =row.findViewById(R.id.tvRemark);
            holder.btnAddToPOS =row.findViewById(R.id.btnAddToPOS);
            holder.btnDetail =row.findViewById(R.id.btnDetail);
            holder.btnDelete =row.findViewById(R.id.btnDelete);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        holder.tvVoucherNo.setText("#" + String.valueOf(lstMasterSale.get(position).getVoucherNumber()));
        holder.tvDateTime.setText(lstMasterSale.get(position).getDate() + " " + lstMasterSale.get(position).getTime());
        holder.tvNetAmt.setText(String.valueOf(systemInfo.df.format(lstMasterSale.get(position).getNetAmount())));
        holder.tvCustomer.setText(lstMasterSale.get(position).getCustomerName());
        holder.tvRemark.setText(lstMasterSale.get(position).getOpenBillRemark());

        holder.btnAddToPOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(openBillListener !=null){
                    openBillListener.onAddToPOSClickListener(position);
                }
            }
        });

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(openBillListener !=null){
                    openBillListener.onDetailClickListener(position);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(openBillListener !=null){
                    openBillListener.onDeleteClickListener(position);
                }
            }
        });

        return row;
    }
}
