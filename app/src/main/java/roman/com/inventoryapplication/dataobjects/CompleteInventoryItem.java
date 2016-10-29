package roman.com.inventoryapplication.dataobjects;

import android.content.ContentValues;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import roman.com.inventoryapplication.data.DatabaseContract;
import roman.com.inventoryapplication.utils.MyApplication;

import static roman.com.inventoryapplication.utils.Preconditions.checkNotNull;

/**
 *
 */
public class CompleteInventoryItem extends InventoryItem {
    private Drawable mDrawable;
    private String mContact;

    public CompleteInventoryItem(InventoryItem inventoryItem, String contactEmail, Drawable drawable) {
        super(inventoryItem.getName(), inventoryItem.getPrice(), inventoryItem.getQuantity(), inventoryItem.getId());
        mDrawable = drawable;
        mContact = contactEmail;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public String getContactEmail() {
        return mContact;
    }

    public void increasePrice() {
        mPrice++;
        new DatabaseUpdateThread(mId, getContentValues()).start();
    }

    public void decreasePrice() {
        mPrice--;
        new DatabaseUpdateThread(mId, getContentValues()).start();
    }

    public void increaseQuantity() {
        mQuantity++;
        new DatabaseUpdateThread(mId, getContentValues()).start();
    }

    public void decreaseQuantity() {
        mQuantity--;
        new DatabaseUpdateThread(mId, getContentValues()).start();
    }

    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableInventory.COLUMN_PRICE, mPrice);
        values.put(DatabaseContract.TableInventory.COLUMN_QUANTITY, mQuantity);
        return values;
    }

    private static class DatabaseUpdateThread extends Thread {
        private ContentValues mContentValues;
        private int mId;

        public DatabaseUpdateThread(int id, @NonNull ContentValues contentValues) {
            mId = id;
            mContentValues = checkNotNull(contentValues);
        }

        @Override
        public void run() {
            String selection = "_id=?";
            String[] selectionArgs = {String.valueOf(mId)};
            // update a row in the database
            MyApplication.getContext().getContentResolver().update(DatabaseContract.TableInventory.CONTENT_URI, mContentValues, selection, selectionArgs);
        }
    }


}
