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
import model.TranSaleModel;

public class TopSaleProductAdapter extends RecyclerView.Adapter<TopSaleProductAdapter.TopSaleProductViewHolder>{

    List<TranSaleModel> list = Collections.emptyList();
    Context context;
    SystemInfo systemInfo=new SystemInfo();
    private View.OnClickListener mOnItemClickListener;

    public TopSaleProductAdapter(List<TranSaleModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public TopSaleProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rp_top_sale_product, parent, false);
        TopSaleProductViewHolder holder = new TopSaleProductViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(TopSaleProductViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.tvCode.setText(list.get(position).getProductCode());
        holder.tvProduct.setText(list.get(position).getProductName());
        holder.tvUnit.setText(list.get(position).getUnitKeyword());
        holder.tvCategory.setText(list.get(position).getCategoryName());
        holder.tvTotalQuantity.setText(String.valueOf(systemInfo.df2d.format(list.get(position).getdQuantity())));
        holder.tvTotalAmt.setText(String.valueOf(systemInfo.df.format(list.get(position).getAmount())));
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public class TopSaleProductViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCode,tvProduct,tvTotalQuantity,tvTotalAmt,tvCategory,tvUnit;

        public TopSaleProductViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            tvTotalQuantity = itemView.findViewById(R.id.tvTotalQuantity);
            tvTotalAmt = itemView.findViewById(R.id.tvTotalAmt);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }

}
