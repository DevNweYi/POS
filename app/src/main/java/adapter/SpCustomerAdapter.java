package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import model.CustomerModel;

/**
 * Created by User on 6/21/2017.
 */
public class SpCustomerAdapter extends BaseAdapter {

    private Context context;
    List<CustomerModel> lstCustomerData;

    public SpCustomerAdapter(Context context, List<CustomerModel> lstCustomerData){
        this.context=context;
        this.lstCustomerData =lstCustomerData;
    }

    public int getCount(){
        return lstCustomerData.size();
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

        tvData.setText(lstCustomerData.get(position).getCustomerName());

        return vi;
    }
}
