package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.List;

import model.UserModel;

public class UserAdapter extends BaseAdapter {
    private Context context;
    List<UserModel> lstUser;

    public UserAdapter(Context context, List<UserModel> lstUser){
        this.context=context;
        this.lstUser = lstUser;
    }

    @Override
    public int getCount(){
        return lstUser.size();
    }

    @Override
    public String getItem(int position){
        return lstUser.get(position).getUserName();
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    public void updateResults(List<UserModel> list) {
        lstUser = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvUser;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.list_general_item, parent,false);
            holder = new ViewHolder();
            holder.tvUser =row.findViewById(R.id.tvGeneralItem);

            row.setTag(holder);
        } else {
            row = convertView;
            holder = (ViewHolder) row.getTag();
        }

        holder.tvUser.setText(lstUser.get(position).getUserName());

        return row;
    }
}
