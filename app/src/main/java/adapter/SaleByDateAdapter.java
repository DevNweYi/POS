package adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.Collections;
import java.util.List;

import common.SystemInfo;
import model.MasterSaleModel;

public class SaleByDateAdapter extends RecyclerView.Adapter<SaleByDateAdapter.SaleByDateViewHolder>{

    List<MasterSaleModel> list = Collections.emptyList();
    Context context;
    SystemInfo systemInfo=new SystemInfo();
    private View.OnClickListener mOnItemClickListener;

    public SaleByDateAdapter(List<MasterSaleModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public SaleByDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rp_sale_by_date_month, parent, false);
        SaleByDateViewHolder holder = new SaleByDateViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SaleByDateViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.tvDate.setText(list.get(position).getDate());
        holder.tvTotalSale.setText(String.valueOf(list.get(position).getTotalSale()));
        holder.tvTotalQuantity.setText(String.valueOf(list.get(position).getTotalQuantity()));
        holder.tvTotalAmt.setText(String.valueOf(systemInfo.df.format(list.get(position).getTotalAmount())));
        holder.tvDiscount.setText(String.valueOf(systemInfo.df.format(list.get(position).getTotalDisAmount())));
        holder.tvNetAmt.setText(String.valueOf(systemInfo.df.format(list.get(position).getNetAmount())));
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public class SaleByDateViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDate,tvTotalSale,tvTotalQuantity,tvTotalAmt,tvDiscount,tvNetAmt;

        public SaleByDateViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotalSale = itemView.findViewById(R.id.tvTotalSale);
            tvTotalQuantity = itemView.findViewById(R.id.tvTotalQuantity);
            tvTotalAmt = itemView.findViewById(R.id.tvTotalAmt);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvNetAmt = itemView.findViewById(R.id.tvNetAmt);
        }
    }

}
