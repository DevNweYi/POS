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
import model.MasterPurchaseModel;

public class DetailedPurchaseAdapter extends RecyclerView.Adapter<DetailedPurchaseAdapter.DetailedPurchaseViewHolder> {

    private Context context;
    List<MasterPurchaseModel> lstMasterPurchase;
    SystemInfo systemInfo=new SystemInfo();
    private View.OnClickListener mOnItemClickListener;

    public DetailedPurchaseAdapter(Context context, List<MasterPurchaseModel> lstMasterPurchase){
        this.context=context;
        this.lstMasterPurchase = lstMasterPurchase;
    }

    @Override
    public DetailedPurchaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rp_detailed_purchase, parent, false);
        DetailedPurchaseViewHolder holder = new DetailedPurchaseViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(DetailedPurchaseViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.tvVoucherNo.setText("#" + lstMasterPurchase.get(position).getVoucherNumber());
        holder.tvDateTime.setText(lstMasterPurchase.get(position).getDate()+" "+lstMasterPurchase.get(position).getTime());
        holder.tvNetAmt.setText(String.valueOf(systemInfo.df.format(lstMasterPurchase.get(position).getNetAmount())));
        holder.tvSupplier.setText(lstMasterPurchase.get(position).getSupplierName());
        holder.tvUser.setText(lstMasterPurchase.get(position).getUserName());
        holder.tvDiscount.setText(String.valueOf(systemInfo.df.format(lstMasterPurchase.get(position).getTotalDisAmount())));
        holder.tvTotalAmt.setText(String.valueOf(systemInfo.df.format(lstMasterPurchase.get(position).getTotalAmount())));
        holder.tvLastDebtAmt.setText(String.valueOf(systemInfo.df.format(lstMasterPurchase.get(position).getLastDebtAmount())));
        holder.tvPaidAmt.setText(String.valueOf(systemInfo.df.format(lstMasterPurchase.get(position).getPaidAmount())));
        holder.tvChangeAmt.setText(String.valueOf(systemInfo.df.format(lstMasterPurchase.get(position).getChangeAmount())));
        holder.tvDebtAmt.setText(String.valueOf(systemInfo.df.format(lstMasterPurchase.get(position).getDebtAmount())));
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return lstMasterPurchase.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public class DetailedPurchaseViewHolder extends RecyclerView.ViewHolder {

        public TextView tvVoucherNo,tvDateTime,tvNetAmt,tvSupplier,tvTotalAmt,tvDiscount,tvUser,tvLastDebtAmt,tvPaidAmt,tvChangeAmt,tvDebtAmt;

        public DetailedPurchaseViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
            tvVoucherNo = itemView.findViewById(R.id.tvVoucherNo);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvSupplier = itemView.findViewById(R.id.tvSupplier);
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
