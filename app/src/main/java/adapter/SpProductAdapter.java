package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import model.ProductModel;

/**
 * Created by User on 6/21/2017.
 */
public class SpProductAdapter extends BaseAdapter {

    private Context context;
    List<ProductModel> lstProductData;

    public SpProductAdapter(Context context, List<ProductModel> lstProductData){
        this.context=context;
        this.lstProductData =lstProductData;
    }

    public int getCount(){
        return lstProductData.size();
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

        tvData.setText(lstProductData.get(position).getProductName());

        return vi;
    }
}
