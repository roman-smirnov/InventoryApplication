package roman.com.inventoryapplication.dataobjects;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;

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

    public CompleteInventoryItem(String name, int price, int quantity, int id, Drawable drawable, String contact) {
        super(name, price, quantity, id);
        mDrawable = drawable;
        mContact = contact;
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

    /**
     * delete the inventory item from the db
     * this method will NOT get rid of the actual object - this is your responsibility
     */
    public void deleteInventoryItem() {
        System.out.println("deleteInventoryItem");
        //delete the inventory item from the db
        new DatabaseDeleteThread(mId).start();
    }

    /**
     * insert the current inventory item into the database
     */
    public void createInventoryItem(){
        new DatabaseNewItemThread(getNewItemContentValues()).start();
    }

    /**
     * get the content value to update the sqlite db row
     *
     * @return
     */
    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableInventory.COLUMN_PRICE, mPrice);
        values.put(DatabaseContract.TableInventory.COLUMN_QUANTITY, mQuantity);
        return values;
    }

    private ContentValues getNewItemContentValues() {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableInventory.COLUMN_NAME, mName);
        values.put(DatabaseContract.TableInventory.COLUMN_PRICE, mPrice);
        values.put(DatabaseContract.TableInventory.COLUMN_QUANTITY, mQuantity);
        values.put(DatabaseContract.TableInventory.COLUMN_ORDER_CONTACT_EMAIL, mContact);

        //get the byte array out of the drawable
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        //put the drawable as a byte array into ContentValues
        values.put(DatabaseContract.TableInventory.COLUMN_PICTURE, stream.toByteArray());

        return values;
    }

    /**
     * this is the thread class we'll use to update the db
     */
    private static class DatabaseUpdateThread extends Thread {
        private ContentValues mContentValues;
        private int mId;

        DatabaseUpdateThread(int id, @NonNull ContentValues contentValues) {
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

    /**
     * this is the thread class we'll use to delete an item from the database
     */
    private static class DatabaseDeleteThread extends Thread {
        private int mId;

        DatabaseDeleteThread(int id) {
            mId = id;
        }

        @Override
        public void run() {
            String selection = "_id=?";
            String[] selectionArgs = {String.valueOf(mId)};
            // delete a row in the database
            MyApplication.getContext().getContentResolver().delete(DatabaseContract.TableInventory.CONTENT_URI, selection, selectionArgs);
        }
    }

    /**
     * this is the thread class we'll use add an item to the database
     */
    private static class DatabaseNewItemThread extends Thread {
        private ContentValues mContentValues;

        public DatabaseNewItemThread(ContentValues contentValues) {
            mContentValues = contentValues;
        }

        @Override
        public void run() {
            // insert a new row into the database
            MyApplication.getContext().getContentResolver().insert(DatabaseContract.TableInventory.CONTENT_URI, mContentValues);
        }
    }


}
