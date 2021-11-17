package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import model.UnitModel;

public class UnitListAdapter extends BaseAdapter {
    private Context context;
    List<UnitModel> lstUnit;

    public UnitListAdapter(Context context, List<UnitModel> lstUnit){
        this.context=context;
        this.lstUnit = lstUnit;
    }

    @Override
    public int getCount(){
        return lstUnit.size();
    }

    @Override
    public String getItem(int position){
        return lstUnit.get(position).getUnitName();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    public void updateResults(List<UnitModel> list) {
        lstUnit = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvUnitName,tvUnitKeyword;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_unit, parent,false);
            holder = new ViewHolder();
            holder.tvUnitName =row.findViewById(R.id.tvUnitName);
            holder.tvUnitKeyword =row.findViewById(R.id.tvUnitKeyword);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        holder.tvUnitName.setText(lstUnit.get(position).getUnitName());
        holder.tvUnitKeyword.setText(lstUnit.get(position).getUnitKeyword());

        return row;
    }
}
