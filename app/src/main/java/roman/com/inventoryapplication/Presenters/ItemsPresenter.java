package roman.com.inventoryapplication.Presenters;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.List;

import roman.com.inventoryapplication.adapters.ItemsCursorAdapter;
import roman.com.inventoryapplication.contracts.ItemsContract;
import roman.com.inventoryapplication.data.DatabaseContract;
import roman.com.inventoryapplication.dataobjects.InventoryItem;
import roman.com.inventoryapplication.utils.MyApplication;

/**
 *
 */
public class ItemsPresenter implements ItemsContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    // id of the article loader
    private static final int LOADER_ID = 2701;
    private ItemsContract.View mView;
    private LoaderManager mLoaderManager;

    public ItemsPresenter(ItemsContract.View view, LoaderManager loaderManager) {
        mView = view;
        mLoaderManager = loaderManager;
    }

    @Override
    public void start() {
        // Kick off the loader
        mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public void editItem(int itemId) {
        mView.showEditItem(itemId);
    }

    @Override
    public void newItem() {
        mView.showNewItem();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                DatabaseContract.TableInventory.COLUMN_ID,
                DatabaseContract.TableInventory.COLUMN_NAME,
                DatabaseContract.TableInventory.COLUMN_PRICE,
                DatabaseContract.TableInventory.COLUMN_QUANTITY};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(MyApplication.getContext(),   // Application activity context
                DatabaseContract.TableInventory.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<InventoryItem> inventoryItemList = ItemsCursorAdapter.getItemListFromCursor(cursor);
        if (inventoryItemList.isEmpty()) {
            //show empty list
            mView.showItems(new ArrayList<InventoryItem>(0));
            mView.showEmpty();
        }else {
            mView.showItems(inventoryItemList);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mView.showItems(new ArrayList<InventoryItem>(0));
    }

}
