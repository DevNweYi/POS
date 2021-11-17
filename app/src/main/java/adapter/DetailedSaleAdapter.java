package adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import common.SystemInfo;
import model.MasterSaleModel;

public class DetailedSaleAdapter extends RecyclerView.Adapter<DetailedSaleAdapter.DetailedSaleViewHolder> {

    private Context context;
    List<MasterSaleModel> lstMasterSale;
    SystemInfo systemInfo=new SystemInfo();
    private View.OnClickListener mOnItemClickListener;

    public DetailedSaleAdapter(Context context, List<MasterSaleModel> lstMasterSale){
        this.context=context;
        this.lstMasterSale = lstMasterSale;
    }

    @Override
    public DetailedSaleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rp_detailed_sale, parent, false);
        DetailedSaleViewHolder holder = new DetailedSaleViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(DetailedSaleViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.tvVoucherNo.setText("#" + lstMasterSale.get(position).getVoucherNumber());
        holder.tvDateTime.setText(lstMasterSale.get(position).getDate()+" "+lstMasterSale.get(position).getTime());
        holder.tvNetAmt.setText(String.valueOf(systemInfo.df.format(lstMasterSale.get(position).getNetAmount())));
        holder.tvCustomer.setText(lstMasterSale.get(position).getCustomerName());
        holder.tvUser.setText(lstMasterSale.get(position).getUserName());
        holder.tvDiscount.setText(String.valueOf(systemInfo.df.format(lstMasterSale.get(position).getTotalDisAmount())));
        holder.tvTotalAmt.setText(String.valueOf(systemInfo.df.format(lstMasterSale.get(position).getTotalAmount())));
        holder.tvLastDebtAmt.setText(String.valueOf(systemInfo.df.format(lstMasterSale.get(position).getLastDebtAmount())));
        holder.tvPaidAmt.setText(String.valueOf(systemInfo.df.format(lstMasterSale.get(position).getPaidAmount())));
        holder.tvChangeAmt.setText(String.valueOf(systemInfo.df.format(lstMasterSale.get(position).getChangeAmount())));
        holder.tvDebtAmt.setText(String.valueOf(systemInfo.df.format(lstMasterSale.get(position).getDebtAmount())));
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return lstMasterSale.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public class DetailedSaleViewHolder extends RecyclerView.ViewHolder {

        public TextView tvVoucherNo,tvDateTime,tvNetAmt,tvCustomer,tvTotalAmt,tvDiscount,tvUser,tvLastDebtAmt,tvPaidAmt,tvChangeAmt,tvDebtAmt;

        public DetailedSaleViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
            tvVoucherNo = itemView.findViewById(R.id.tvVoucherNo);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvTotalAmt = itemView.findViewById(R.id.tvTotalAmt);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvNetAmt = itemView.findViewById(R.id.tvNetAmt);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvLastDebtAmt = itemView.findViewById(R.id.tvLastDebtAmt);
            tvPaidAmt = itemView.findViewById(R.id.tvPaidAmt);
            tvChangeAmt = itemView.findViewById(R.id.tvChangeAmt);
            tvDebtAmt = itemView.findViewById(R.id.tvDebtAmt);
        }
    }
}
