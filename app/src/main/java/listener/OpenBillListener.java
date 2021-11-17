package listener;

public interface OpenBillListener {
    void onAddToPOSClickListener(int position);
    void onDetailClickListener(int position);
    void onDeleteClickListener(int position);
}
