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
import model.ProductUnitModel;

public class ProductUnitAdapter extends BaseAdapter {
    private Context context;
    List<ProductUnitModel> lstProductUnit;
    SystemInfo systemInfo=new SystemInfo();
    int moduleType;

    public ProductUnitAdapter(Context context, List<ProductUnitModel> lstProductUnit,int moduleType){
        this.context=context;
        this.lstProductUnit = lstProductUnit;
        this.moduleType=moduleType;
    }

    @Override
    public int getCount(){
        return lstProductUnit.size();
    }

    @Override
    public String getItem(int position){
        return lstProductUnit.get(position).getUnitKeyword();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    static class ViewHolder {
        TextView tvUnitKeyword,tvPrice,tvUnitType;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_product_unit, parent,false);
            holder = new ViewHolder();
            holder.tvUnitKeyword =row.findViewById(R.id.tvUnitKeyword);
            holder.tvPrice =row.findViewById(R.id.tvPrice);
            holder.tvUnitType =row.findViewById(R.id.tvUnitType);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        holder.tvUnitType.setText(lstProductUnit.get(position).getUnitType());
        holder.tvUnitKeyword.setText(lstProductUnit.get(position).getUnitKeyword());
        if(moduleType==systemInfo.SaleModule) holder.tvPrice.setText(context.getResources().getString(R.string.sale_price) + ": " + systemInfo.df.format(lstProductUnit.get(position).getPuSalePrice()));
        else if(moduleType==systemInfo.PurchaseModule) holder.tvPrice.setText(context.getResources().getString(R.string.pur_price) + ": " + systemInfo.df.format(lstProductUnit.get(position).getPuPurPrice()));

        return row;
    }
}
