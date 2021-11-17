package adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.developerstar.pos.R;

import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    int[] images;
    LayoutInflater mLayoutInflater;

    public ViewPagerAdapter(Context context,int[] images){
        this.context=context;
        this.images=images;
        mLayoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view,@NonNull Object object){
        return view ==((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container,final int position){
        View itemView=mLayoutInflater.inflate(R.layout.item_info_slide,container,false);
        ImageView imageView=(ImageView) itemView.findViewById(R.id.imgSlideImage);
        imageView.setImageResource(images[position]);
        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeView((LinearLayout)object);
    }
}
