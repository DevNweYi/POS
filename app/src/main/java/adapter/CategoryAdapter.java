package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import model.CategoryModel;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    List<CategoryModel> lstCategory;

    public CategoryAdapter(Context context, List<CategoryModel> lstCategory){
        this.context=context;
        this.lstCategory = lstCategory;
    }

    @Override
    public int getCount(){
        return lstCategory.size();
    }

    @Override
    public String getItem(int position){
        return lstCategory.get(position).getCategoryName();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    public void updateResults(List<CategoryModel> list) {
        lstCategory = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvCategory,tvItem;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_category, parent,false);
            holder = new ViewHolder();
            holder.tvCategory =row.findViewById(R.id.tvCategory);
            holder.tvItem =row.findViewById(R.id.tvItem);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        holder.tvCategory.setText(lstCategory.get(position).getCategoryName());
        holder.tvItem.setText(lstCategory.get(position).getTotalItem() + " Item");

        return row;
    }
}
