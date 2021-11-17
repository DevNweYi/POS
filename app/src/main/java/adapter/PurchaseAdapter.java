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
import model.TranPurchaseModel;

public class PurchaseAdapter extends BaseAdapter {
    private Context context;
    List<TranPurchaseModel> lstTranPurchase;
    SystemInfo systemInfo=new SystemInfo();

    public PurchaseAdapter(Context context, List<TranPurchaseModel> lstTranPurchase){
        this.context=context;
        this.lstTranPurchase = lstTranPurchase;
    }

    @Override
    public int getCount(){
        return lstTranPurchase.size();
    }

    @Override
    public String getItem(int position){
        return lstTranPurchase.get(position).getProductName();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    static class ViewHolder {
        TextView tvProductName,tvQuantity,tvAmount,tvPurPrice;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_purchase, parent,false);
            holder = new ViewHolder();
            holder.tvProductName =row.findViewById(R.id.tvProductName);
            holder.tvQuantity =row.findViewById(R.id.tvQuantity);
            holder.tvAmount =row.findViewById(R.id.tvAmount);
            holder.tvPurPrice =row.findViewById(R.id.tvPurPrice);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        holder.tvProductName.setText(lstTranPurchase.get(position).getProductName());
        holder.tvPurPrice.setText(String.valueOf(systemInfo.df.format(lstTranPurchase.get(position).getPurPrice())));
        if(lstTranPurchase.get(position).getUnitId() != 0)
            holder.tvQuantity.setText(lstTranPurchase.get(position).getQuantity()+ "(" + lstTranPurchase.get(position).getUnitKeyword() + ")");
        else holder.tvQuantity.setText(String.valueOf(lstTranPurchase.get(position).getQuantity()));
        holder.tvAmount.setText(String.valueOf(systemInfo.df.format(lstTranPurchase.get(position).getQuantity()*lstTranPurchase.get(position).getPurPrice())));

        return row;
    }
}
