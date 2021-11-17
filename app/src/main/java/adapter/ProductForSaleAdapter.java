package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import common.SystemInfo;
import listener.ProductForSaleListener;
import model.ProductModel;

public class ProductForSaleAdapter extends BaseAdapter {
    private Context context;
    List<ProductModel> lstProduct;
    SystemInfo systemInfo=new SystemInfo();
    ProductForSaleListener productForSaleListener;

    public ProductForSaleAdapter(Context context, List<ProductModel> lstProduct){
        this.context=context;
        this.lstProduct = lstProduct;
    }

    public void setOnEventListener(ProductForSaleListener productForSaleListener){
        this.productForSaleListener=productForSaleListener;
    }

    @Override
    public int getCount(){
        return lstProduct.size();
    }

    @Override
    public String getItem(int position){
        return lstProduct.get(position).getProductCode();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    static class ViewHolder {
        TextView tvProductName,tvSalePrice,tvProductUnit;
        ImageButton btnAdd;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_product_for_sale, parent,false);
            holder = new ViewHolder();
            holder.tvProductName =row.findViewById(R.id.tvProductName);
            holder.tvProductUnit =row.findViewById(R.id.tvProductUnit);
            holder.tvSalePrice =row.findViewById(R.id.tvSalePrice);
            holder.btnAdd =row.findViewById(R.id.btnAdd);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        holder.tvProductName.setText(lstProduct.get(position).getProductName());
        holder.tvProductUnit.setText(lstProduct.get(position).getSelectUnitKeyword());
        holder.tvSalePrice.setText(String.valueOf(systemInfo.df.format(lstProduct.get(position).getSalePrice())));
        if(lstProduct.get(position).getIsProductUnit() == 0) {
            holder.tvProductUnit.setVisibility(View.GONE);
        } else if(lstProduct.get(position).getIsProductUnit() == 1) {
            holder.tvProductUnit.setVisibility(View.VISIBLE);
        }

        holder.tvProductUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productForSaleListener !=null){
                    productForSaleListener.onUnitSelectListener(position,holder.tvProductUnit,holder.tvSalePrice);
                }
            }
        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productForSaleListener !=null){
                    productForSaleListener.onAddListener(position);
                }
            }
        });

        return row;
    }
}
