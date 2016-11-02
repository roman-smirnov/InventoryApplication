package roman.com.inventoryapplication.listeners;

import android.view.View;

/**
 * click listener for whole row of recyclerview and for a single item inside a row
 */
public interface RecyclerClickListener {

    public void onClickRow(View view, int position);

    public void onClickRowItem(View view, int position);

}
