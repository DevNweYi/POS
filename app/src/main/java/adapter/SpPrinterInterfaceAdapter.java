package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import model.PrinterInterfaceModel;

/**
 * Created by NweYiAung on 15-02-2017.
 */
public class SpPrinterInterfaceAdapter extends BaseAdapter{
    private Context context;
    List<PrinterInterfaceModel> lstInterfaceData;

    public SpPrinterInterfaceAdapter(Context context, List<PrinterInterfaceModel> lstInterfaceData){
        this.context=context;
        this.lstInterfaceData = lstInterfaceData;
    }

    public int getCount(){
        return lstInterfaceData.size();
    }

    public Object getItem(int position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(final int position,View convertView,ViewGroup parent){
        View vi=convertView;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi=inflater.inflate(R.layout.spinner, null);
        }
        TextView tvSpinnerItem=vi.findViewById(R.id.tvData);

        tvSpinnerItem.setText(lstInterfaceData.get(position).getInterfaceName());

        return vi;
    }

}
