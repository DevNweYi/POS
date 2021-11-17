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

public class UnitOnlyAdapter extends BaseAdapter {
    private Context context;
    List<UnitModel> lstUnit;

    public UnitOnlyAdapter(Context context, List<UnitModel> lstUnit){
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

    static class ViewHolder {
        TextView tvGeneralItem;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_general_item, parent,false);
            holder = new ViewHolder();
            holder.tvGeneralItem =row.findViewById(R.id.tvGeneralItem);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        holder.tvGeneralItem.setText(lstUnit.get(position).getUnitKeyword());

        return row;
    }
}
