package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import model.PrinterModel;

/**
 * Created by NweYiAung on 14-02-2017.
 */
public class PrinterListAdapter extends BaseAdapter{

    private Context context;
    List<PrinterModel> lstPrinterData;

    public PrinterListAdapter(Context context, List<PrinterModel> lstPrinterData){
        this.context=context;
        this.lstPrinterData = lstPrinterData;
    }

    public int getCount(){
        return lstPrinterData.size();
    }

    public Object getItem(int position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    public void updateResults(List<PrinterModel> list) {
        lstPrinterData = list;
        notifyDataSetChanged();
    }

    public View getView(final int position,View convertView,ViewGroup parent){
        View vi=convertView;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi=inflater.inflate(R.layout.list_printer, null);
        }
        TextView tvPrinterName=vi.findViewById(R.id.tvPrinterName);
        TextView tvPrinterInterface=vi.findViewById(R.id.tvPrinterInterface);

        tvPrinterName.setText(lstPrinterData.get(position).getPrinterName());
        tvPrinterInterface.setText(lstPrinterData.get(position).getPrinterInterfaceName());

        return vi;
    }
}
