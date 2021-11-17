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
import model.TranAdjustmentModel;

public class AdjustmentProductAdapter extends BaseAdapter {
    private Context context;
    List<TranAdjustmentModel> lstTranAdjustment;
    PurchaseAdjustListener adjustmentListener;
    SystemInfo systemInfo=new SystemInfo();

    public AdjustmentProductAdapter(Context context, List<TranAdjustmentModel> lstTranAdjustment){
        this.context=context;
        this.lstTranAdjustment = lstTranAdjustment;
    }

    public void setOnEventListener(PurchaseAdjustListener adjustmentListener){
        this.adjustmentListener=adjustmentListener;
    }

    @Override
    public int getCount(){
        return lstTranAdjustment.size();
    }

    @Override
    public String getItem(int position){
        return lstTranAdjustment.get(position).getProductName();
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
        holder.tvProductName.setText(lstTranAdjustment.get(position).getProductName());
        holder.tvQuantity.setText(String.valueOf(lstTranAdjustment.get(position).getQuantity()));
        if(lstTranAdjustment.get(position).getUnitKeyword() != null) holder.tvProductUnit.setText(lstTranAdjustment.get(position).getUnitKeyword());
        else holder.tvProductUnit.setText("");
        holder.tvPrice.setText(String.valueOf(systemInfo.df.format(lstTranAdjustment.get(position).getPurPrice())));

        holder.tvQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adjustmentListener !=null){
                    adjustmentListener.onQuantityClickListener(position,holder.tvQuantity);
                }
            }
        });

        holder.tvPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adjustmentListener !=null){
                    adjustmentListener.onPriceClickListener(position,holder.tvPrice);
                }
            }
        });

        return row;
    }
}
