package roman.com.inventoryapplication.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import roman.com.inventoryapplication.R;
import roman.com.inventoryapplication.dataobjects.InventoryItem;

/**
 * Created by skyfishjy on 10/31/14.
 */
public class ItemsCursorRecyclerViewAdaper extends CursorRecyclerViewAdapter<ItemsCursorRecyclerViewAdaper.ViewHolder> {

    public ItemsCursorRecyclerViewAdaper(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_items_list_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
        InventoryItem inventoryItem = InventoryItem.fromCursor(cursor);
        viewHolder.mNameTextView.setText(inventoryItem.getName());
        viewHolder.mPriceTextView.setText(inventoryItem.getPrice());
        viewHolder.mQuantityTextView.setText(inventoryItem.getQuantity());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTextView;
        public TextView mPriceTextView;
        public TextView mQuantityTextView;

        public ViewHolder(View view) {
            super(view);
            mNameTextView = (TextView) view.findViewById(R.id.textview_name);
            mPriceTextView = (TextView) view.findViewById(R.id.textview_price);
            mQuantityTextView = (TextView) view.findViewById(R.id.textview_quantity);

        }
    }
}