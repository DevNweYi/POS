package adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.developerstar.pos.R;

import java.util.Collections;
import java.util.List;

import common.SystemInfo;
import model.CategoryModel;

public class CategoryFilterAdapter extends RecyclerView.Adapter<CategoryFilterAdapter.CategoryFilterViewHolder>{

    List<CategoryModel> list = Collections.emptyList();
    Context context;
    SystemInfo systemInfo=new SystemInfo();
    private View.OnClickListener mOnItemClickListener;

    public CategoryFilterAdapter(List<CategoryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public CategoryFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category_filter, parent, false);
        CategoryFilterViewHolder holder = new CategoryFilterViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CategoryFilterViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.tvCategory.setText(list.get(position).getCategoryName());
        holder.chkCategory.setChecked(list.get(position).getSelected());

        holder.chkCategory.setTag(position);

        holder.chkCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer pos = (Integer) holder.chkCategory.getTag();
                if (list.get(pos).getSelected()) {
                    list.get(pos).setSelected(false);
                } else {
                    list.get(pos).setSelected(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    public class CategoryFilterViewHolder extends RecyclerView.ViewHolder {

        public CheckBox chkCategory;
        public TextView tvCategory;

        public CategoryFilterViewHolder(View itemView) {
            super(itemView);
            chkCategory = itemView.findViewById(R.id.chkCategory);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }

}
