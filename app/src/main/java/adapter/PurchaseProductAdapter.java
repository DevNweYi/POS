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
import listener.PurchaseAdjustListener;
import model.TranPurchaseModel;

public class PurchaseProductAdapter extends BaseAdapter {
    private Context context;
    List<TranPurchaseModel> lstTranPurchase;
    PurchaseAdjustListener purchaseListener;
    SystemInfo systemInfo=new SystemInfo();

    public PurchaseProductAdapter(Context context, List<TranPurchaseModel> lstTranPurchase){
        this.context=context;
        this.lstTranPurchase = lstTranPurchase;
    }

    public void setOnEventListener(PurchaseAdjustListener purchaseListener){
        this.purchaseListener=purchaseListener;
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
        TextView tvProductName,tvPrice,tvQuantity,tvProductUnit;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_purchase_adjust_product, parent,false);
            holder = new ViewHolder();
            holder.tvProductName =row.findViewById(R.id.tvProductName);
            holder.tvQuantity =row.findViewById(R.id.tvQuantity);
            holder.tvProductUnit =row.findViewById(R.id.tvProductUnit);
            holder.tvPrice =row.findViewById(R.id.tvPrice);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        holder.tvProductName.setText(lstTranPurchase.get(position).getProductName());
        holder.tvQuantity.setText(String.valueOf(lstTranPurchase.get(position).getQuantity()));
        if(lstTranPurchase.get(position).getUnitKeyword() != null) holder.tvProductUnit.setText(lstTranPurchase.get(position).getUnitKeyword());
        else holder.tvProductUnit.setText("");
        holder.tvPrice.setText(String.valueOf(systemInfo.df.format(lstTranPurchase.get(position).getPurPrice())));

        holder.tvQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(purchaseListener !=null){
                    purchaseListener.onQuantityClickListener(position,holder.tvQuantity);
                }
            }
        });

        holder.tvPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(purchaseListener !=null){
                    purchaseListener.onPriceClickListener(position,holder.tvPrice);
                }
            }
        });

        return row;
    }
}
