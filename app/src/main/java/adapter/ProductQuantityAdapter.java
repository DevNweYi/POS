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
import model.ProductModel;

public class ProductQuantityAdapter extends RecyclerView.Adapter<ProductQuantityAdapter.ProductQuantityViewHolder>{

    List<ProductModel> list = Collections.emptyList();
    Context context;
    SystemInfo systemInfo=new SystemInfo();
    private View.OnClickListener mOnItemClickListener;

    public ProductQuantityAdapter(List<ProductModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ProductQuantityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_rp_product_quantity, parent, false);
        ProductQuantityViewHolder holder = new ProductQuantityViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductQuantityViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.tvCode.setText(list.get(position).getProductCode());
        holder.tvProduct.setText(list.get(position).getProductName());
        holder.tvUnit.setText(list.get(position).getStandardUnitKeyword());
        holder.tvCategory.setText(list.get(position).getCategoryName());
        holder.tvOpeningStock.setText(String.valueOf(systemInfo.df2d.format(list.get(position).getdOpeningQuantity())));
        holder.tvPurchaseQty.setText(String.valueOf(systemInfo.df2d.format(list.get(position).getdPurQuantity())));
        holder.tvSaleQty.setText(String.valueOf(systemInfo.df2d.format(list.get(position).getdSaleQuantity())));
        holder.tvAdjustQty.setText(String.valueOf(systemInfo.df2d.format(list.get(position).getdAdjustQuantity())));
        holder.tvBalance.setText(String.valueOf(systemInfo.df2d.format(list.get(position).getBalQuantity())));
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public class ProductQuantityViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCode,tvProduct,tvOpeningStock,tvPurchaseQty,tvCategory,tvUnit,tvSaleQty,tvAdjustQty,tvBalance;

        public ProductQuantityViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            tvOpeningStock = itemView.findViewById(R.id.tvOpeningStock);
            tvPurchaseQty = itemView.findViewById(R.id.tvPurchaseQty);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvSaleQty = itemView.findViewById(R.id.tvSaleQty);
            tvAdjustQty = itemView.findViewById(R.id.tvAdjustQty);
            tvBalance = itemView.findViewById(R.id.tvBalance);
        }
    }

}
