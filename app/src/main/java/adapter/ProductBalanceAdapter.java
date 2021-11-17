package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import common.SystemInfo;
import model.ProductModel;

public class ProductBalanceAdapter extends BaseAdapter {
    private Context context;
    List<ProductModel> lstProduct;
    SystemInfo systemInfo=new SystemInfo();

    public ProductBalanceAdapter(Context context, List<ProductModel> lstProduct){
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
        TextView tvProduct,tvQuantity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_product_balance, parent,false);
            holder = new ViewHolder();
            holder.tvProduct =row.findViewById(R.id.tvProduct);
            holder.tvQuantity =row.findViewById(R.id.tvQuantity);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        holder.tvProduct.setText(lstProduct.get(position).getProductName());
        if(lstProduct.get(position).getSelectUnitKeyword() == null)
            holder.tvQuantity.setText(context.getResources().getString(R.string.balance_my)+": "+systemInfo.df2d.format(lstProduct.get(position).getBalQuantity()));
        else holder.tvQuantity.setText(context.getResources().getString(R.string.balance_my)+": "+systemInfo.df2d.format(lstProduct.get(position).getBalQuantity())+"("+lstProduct.get(position).getSelectUnitKeyword()+")");

        return row;
    }
}
