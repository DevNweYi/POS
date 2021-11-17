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
import model.MasterAdjustmentModel;

public class AdjustmentMasterAdapter extends BaseAdapter {
    private Context context;
    List<MasterAdjustmentModel> lstMasterAdjustment;
    SystemInfo systemInfo=new SystemInfo();

    public AdjustmentMasterAdapter(Context context, List<MasterAdjustmentModel> lstMasterAdjustment){
        this.context=context;
        this.lstMasterAdjustment = lstMasterAdjustment;
    }

    @Override
    public int getCount(){
        return lstMasterAdjustment.size();
    }

    @Override
    public String getItem(int position){
        return lstMasterAdjustment.get(position).getDate();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    public void updateResults(List<MasterAdjustmentModel> list) {
        lstMasterAdjustment = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvDamageID,tvTime,tvTotalAmt,tvUser;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_adjustment_master, parent,false);
            holder = new ViewHolder();
            holder.tvDamageID =row.findViewById(R.id.tvDamageID);
            holder.tvTime =row.findViewById(R.id.tvTime);
            holder.tvTotalAmt =row.findViewById(R.id.tvTotalAmt);
            holder.tvUser =row.findViewById(R.id.tvUser);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }
        holder.tvDamageID.setText("damage # " + lstMasterAdjustment.get(position).getMasterAdjustmentId());
        holder.tvTime.setText(lstMasterAdjustment.get(position).getDate() + context.getResources().getString(R.string.space) + lstMasterAdjustment.get(position).getTime());
        holder.tvTotalAmt.setText(String.valueOf(systemInfo.df.format(lstMasterAdjustment.get(position).getTotalAmount())));
        holder.tvUser.setText(lstMasterAdjustment.get(position).getUserName());

        return row;
    }
}
