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

public class SaleByCategoryAdapter extends RecyclerView.Adapter<SaleByCategoryAdapter.SaleByDateViewHolder>{

    List<MasterSaleModel> list = Collections.emptyList();
    Context context;
    SystemInfo systemInfo=new SystemInfo();
    private View.OnClickListener mOnItemClickListener;

    public SaleByCategoryAdapter(List<MasterSaleModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public SaleByDateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rp_sale_by_category, parent, false);
        SaleByDateViewHolder holder = new SaleByDateViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SaleByDateViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.tvDate.setText(list.get(position).getCategoryName());
        holder.tvTotalQuantity.setText(String.valueOf(list.get(position).getTotalQuantity()));
        holder.tvTotalAmt.setText(String.valueOf(systemInfo.df.format(list.get(position).getTotalAmount())));
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

        public TextView tvDate,tvTotalQuantity,tvTotalAmt;

        public SaleByDateViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTotalQuantity = itemView.findViewById(R.id.tvTotalQuantity);
            tvTotalAmt = itemView.findViewById(R.id.tvTotalAmt);
        }
    }

}
