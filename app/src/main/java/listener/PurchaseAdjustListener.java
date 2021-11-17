package listener;

import android.widget.TextView;

public interface PurchaseAdjustListener {
    void onQuantityClickListener(int position, TextView tvQuantity);
    void onPriceClickListener(int position, TextView tvPrice);
}
