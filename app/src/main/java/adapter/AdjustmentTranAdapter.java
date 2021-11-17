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
import model.TranAdjustmentModel;

public class AdjustmentTranAdapter extends BaseAdapter {
    private Context context;
    List<TranAdjustmentModel> lstTranAdjustment;
    SystemInfo systemInfo=new SystemInfo();

    public AdjustmentTranAdapter(Context context, List<TranAdjustmentModel> lstTranAdjustment){
        this.context=context;
        this.lstTranAdjustment = lstTranAdjustment;
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
        TextView tvProductName,tvQuantity,tvAmount,tvPurPrice;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_purchase_adjust_tran, parent,false);
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
        holder.tvProductName.setText(lstTranAdjustment.get(position).getProductName());
        holder.tvPurPrice.setText(String.valueOf(systemInfo.df.format(lstTranAdjustment.get(position).getPurPrice())));
        if(lstTranAdjustment.get(position).getUnitId() != 0)
            holder.tvQuantity.setText(lstTranAdjustment.get(position).getQuantity()+ "(" + lstTranAdjustment.get(position).getUnitKeyword() + ")");
        else holder.tvQuantity.setText(String.valueOf(lstTranAdjustment.get(position).getQuantity()));
        holder.tvAmount.setText(String.valueOf(systemInfo.df.format(lstTranAdjustment.get(position).getAmount())));

        return row;
    }
}
