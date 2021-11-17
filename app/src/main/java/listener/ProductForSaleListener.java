package listener;

import android.widget.TextView;

public interface ProductForSaleListener {
    void onUnitSelectListener(int position,TextView tvUnit,TextView tvSalePrice);
    void onAddListener(int position);
}
