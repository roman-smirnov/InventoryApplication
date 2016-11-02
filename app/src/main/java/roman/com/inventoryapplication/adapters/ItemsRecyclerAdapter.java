package roman.com.inventoryapplication.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import roman.com.inventoryapplication.R;
import roman.com.inventoryapplication.dataobjects.InventoryItem;
import roman.com.inventoryapplication.listeners.RecyclerClickListener;

import static roman.com.inventoryapplication.utils.Preconditions.checkNotNull;


public class ItemsRecyclerAdapter extends RecyclerView.Adapter<ItemsRecyclerAdapter.ItemViewHolder> {
    private List<InventoryItem> mItemList;
    private RecyclerClickListener mRecyclerClickListener;

    public ItemsRecyclerAdapter(List<InventoryItem> itemList, RecyclerClickListener recyclerClickListener) {
        mItemList = itemList;
        mRecyclerClickListener = recyclerClickListener;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_items_list_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
        InventoryItem item = mItemList.get(position);
        viewHolder.mNameTextView.setText(item.getName());
        //use String.valueOf() to avoid bug with the setText(int) method being called - which is not what we want
        viewHolder.mPriceTextView.setText(String.valueOf(item.getPrice()));
        viewHolder.mQuantityTextView.setText(String.valueOf(item.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    /**
     * get the note at specified position in the list
     *
     * @param position
     * @return
     */
    public InventoryItem getItem(int position) {
        return mItemList.get(position);
    }

    /**
     * replace the list in the adapter and call notifydatasetchanged
     *
     * @param notesList
     */
    public void replaceData(@NonNull List<InventoryItem> notesList) {
        mItemList = checkNotNull(notesList);
        notifyDataSetChanged();
    }

    public int getPosition(@NonNull InventoryItem note) {
        return mItemList.indexOf(note);
    }


    /**
     * the view holder class
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mNameTextView;
        public TextView mPriceTextView;
        public TextView mQuantityTextView;
        public Button mSaleButton;

        public ItemViewHolder(View view) {
            super(view);
            mNameTextView = (TextView) view.findViewById(R.id.textview_name);
            mPriceTextView = (TextView) view.findViewById(R.id.textview_price);
            mQuantityTextView = (TextView) view.findViewById(R.id.textview_quantity);
            mSaleButton = (Button) view.findViewById(R.id.button_sale_made);
            // set the internal listeners
            view.setOnClickListener(this);
            mSaleButton.setOnClickListener(this);
        }

        /**
         * internal onclick method passes the event up
         * @param view
         */
        @Override
        public void onClick(View view) {
            // check whether the whole row was clicked or just the button
            if(view.getId() == mSaleButton.getId()){
                mRecyclerClickListener.onClickRowItem(view,getAdapterPosition());
            }else{
                mRecyclerClickListener.onClickRow(view, getAdapterPosition());
            }
        }
    }



}
