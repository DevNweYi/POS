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
import model.ProductUnitModel;

/**
 * Created by User on 6/21/2017.
 */
public class SpProductUnitAdapter extends BaseAdapter {

    private Context context;
    List<ProductUnitModel> lstProductUnitData;
    SystemInfo systemInfo=new SystemInfo();

    public SpProductUnitAdapter(Context context, List<ProductUnitModel> lstProductUnitData){
        this.context=context;
        this.lstProductUnitData =lstProductUnitData;
    }

    public int getCount(){
        return lstProductUnitData.size();
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
            vi=inflater.inflate(R.layout.list_product_unit, null);
        }
        TextView tvUnitKeyword = vi.findViewById(R.id.tvUnitKeyword);
        TextView tvPrice = vi.findViewById(R.id.tvPrice);

        tvUnitKeyword.setText(lstProductUnitData.get(position).getUnitKeyword());
        tvPrice.setText(String.valueOf(systemInfo.df.format(lstProductUnitData.get(position).getPuSalePrice())));

        return vi;
    }
}
