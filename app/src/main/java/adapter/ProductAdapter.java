package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import model.ProductModel;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    List<ProductModel> lstProduct;

    public ProductAdapter(Context context, List<ProductModel> lstProduct){
        this.context=context;
        this.lstProduct = lstProduct;
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

    public void updateResults(List<ProductModel> list) {
        lstProduct = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvProductName,tvProductCode,tvQuantity,tvCategory;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_product, parent,false);
            holder = new ViewHolder();
            holder.tvProductName =row.findViewById(R.id.tvProductName);
            holder.tvProductCode =row.findViewById(R.id.tvProductCode);
            holder.tvQuantity =row.findViewById(R.id.tvQuantity);
            holder.tvCategory =row.findViewById(R.id.tvCategory);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        holder.tvProductName.setText(lstProduct.get(position).getProductName());
        holder.tvProductCode.setText(lstProduct.get(position).getProductCode());
        /*if(lstProduct.get(position).getIsProductUnit() == 0) holder.tvQuantity.setText(String.valueOf(lstProduct.get(position).getQuantity()+ " Opening Stock"));
        else holder.tvQuantity.setText("-");*/
        holder.tvCategory.setText(lstProduct.get(position).getCategoryName());

        return row;
    }
}
