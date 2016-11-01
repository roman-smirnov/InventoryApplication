package roman.com.inventoryapplication.Presenters;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import roman.com.inventoryapplication.adapters.ItemsCursorAdapter;
import roman.com.inventoryapplication.contracts.EditorContract;
import roman.com.inventoryapplication.data.DatabaseContract;
import roman.com.inventoryapplication.dataobjects.CompleteInventoryItem;
import roman.com.inventoryapplication.fragments.EditorFragment;
import roman.com.inventoryapplication.utils.MyApplication;

import static roman.com.inventoryapplication.utils.Preconditions.checkNotNull;

/**
 * Created by roman on 10/27/16.
 */

public class EditorPresenter implements EditorContract.Presenter, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 1207;
    private EditorContract.View mView;
    private LoaderManager mLoaderManager;

    private CompleteInventoryItem mCompleteInventoryItem = null;

    public EditorPresenter(@NonNull EditorContract.View view, @NonNull LoaderManager loaderManager) {
        checkNotNull(view);
        checkNotNull(loaderManager);
        mView = view;
        mLoaderManager = loaderManager;
    }

    @Override
    public void setItem(int itemId) {
        Bundle bundle = new Bundle();
        bundle.putInt(EditorFragment.KEY_ITEM_ID, itemId);
        mLoaderManager.initLoader(LOADER_ID, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                DatabaseContract.TableInventory.COLUMN_ID,
                DatabaseContract.TableInventory.COLUMN_NAME,
                DatabaseContract.TableInventory.COLUMN_PRICE,
                DatabaseContract.TableInventory.COLUMN_QUANTITY,
                DatabaseContract.TableInventory.COLUMN_ORDER_CONTACT_EMAIL,
                DatabaseContract.TableInventory.COLUMN_PICTURE
        };

        String selection = "_id=?";
        String[] selectionArgs = {String.valueOf(args.getInt(EditorFragment.KEY_ITEM_ID))};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(MyApplication.getContext(),   // Application activity context
                DatabaseContract.TableInventory.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                selection,                   // No selection clause
                selectionArgs,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCompleteInventoryItem = ItemsCursorAdapter.getCompleteItemFromCursor(data);

        //if the cursor  becomes empty (e.g when deleted) check the returned value is not null
        if (mCompleteInventoryItem == null) {
            return;
        }
        mView.showItem(mCompleteInventoryItem);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void decreaseQuantity() {
        mCompleteInventoryItem.decreaseQuantity();
        mView.showItem(mCompleteInventoryItem);
    }

    @Override
    public void increaseQunatity() {
        mCompleteInventoryItem.increaseQuantity();
        mView.showItem(mCompleteInventoryItem);
    }

    @Override
    public void decreasePrice() {
        mCompleteInventoryItem.decreasePrice();
        mView.showItem(mCompleteInventoryItem);
    }

    @Override
    public void increasePrice() {
        mCompleteInventoryItem.increasePrice();
        mView.showItem(mCompleteInventoryItem);
    }

    @Override
    public void emailContact() {
        mView.showEmailContact(mCompleteInventoryItem.getContactEmail());
    }

    @Override
    public void deleteItem() {
        mCompleteInventoryItem.deleteInventoryItem();
        mView.removeFromView();
    }
}
