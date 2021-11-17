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

/**
 * Created by User on 6/21/2017.
 */
public class SpCategoryAdapter extends BaseAdapter {

    private Context context;
    List<CategoryModel> lstCategoryData;

    public SpCategoryAdapter(Context context, List<CategoryModel> lstCategoryData){
        this.context=context;
        this.lstCategoryData =lstCategoryData;
    }

    public int getCount(){
        return lstCategoryData.size();
    }

    public Object getItem(int position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        View vi=convertView;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi=inflater.inflate(R.layout.spinner, null);
        }
        TextView tvData = vi.findViewById(R.id.tvData);

        tvData.setText(lstCategoryData.get(position).getCategoryName());

        return vi;
    }
}
