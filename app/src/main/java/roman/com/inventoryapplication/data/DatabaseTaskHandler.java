package roman.com.inventoryapplication.data;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;

import roman.com.inventoryapplication.dataobjects.CompleteInventoryItem;
import roman.com.inventoryapplication.dataobjects.InventoryItem;
import roman.com.inventoryapplication.utils.MyApplication;

import static roman.com.inventoryapplication.utils.Preconditions.checkNotNull;


/**
 *  this is a helper class that takes in inventoryitems and save their data to the db on other threads
 */
public class DatabaseTaskHandler {

    /**
     * get contentvalues for all fields of the inventory item
     */
    private static ContentValues getNewItemContentValues(@NonNull CompleteInventoryItem inventoryItem) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableInventory.COLUMN_NAME, inventoryItem.getName());
        values.put(DatabaseContract.TableInventory.COLUMN_PRICE, inventoryItem.getPrice());
        values.put(DatabaseContract.TableInventory.COLUMN_QUANTITY, inventoryItem.getQuantity());
        values.put(DatabaseContract.TableInventory.COLUMN_ORDER_CONTACT_EMAIL, inventoryItem.getContactEmail());

        //get the byte array out of the drawable
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) inventoryItem.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        //put the drawable as a byte array into ContentValues
        values.put(DatabaseContract.TableInventory.COLUMN_PICTURE, stream.toByteArray());

        return values;
    }

    /**
     * launches a thread to increse the price value in the db
     * @param inventoryItem
     */
    public static void increasePrice(@NonNull InventoryItem inventoryItem) {
        new DatabaseUpdateThread(inventoryItem.getId(), getContentValues(inventoryItem.getPrice()+1, inventoryItem.getQuantity())).start();
    }

    /**
     * launches a thread to increse the price value in the db
     * does nothing if you try to set the price below 0
     * @param inventoryItem
     */
    public static void decreasePrice(@NonNull InventoryItem inventoryItem) {
        //validate no negative price is set
        if (inventoryItem.getPrice() < 1) {
            return;
        }
        new DatabaseUpdateThread(inventoryItem.getId(), getContentValues(inventoryItem.getPrice()-1, inventoryItem.getQuantity())).start();

    }

    /**
     * launches a thread to increse the price value in the db
     * @param inventoryItem
     */
    public static void increaseQuantity(@NonNull InventoryItem inventoryItem) {
        new DatabaseUpdateThread(inventoryItem.getId(), getContentValues(inventoryItem.getPrice(), inventoryItem.getQuantity()+1)).start();

    }

    /**
     * launches a thread to increse the price value in the db
     * does nothing if you try to set the quantity below 0
     * @param inventoryItem
     */
    public static void decreaseQuantity(@NonNull InventoryItem inventoryItem) {
        //validate no negative quantity is set
        if (inventoryItem.getQuantity() < 1) {
            return;
        }
        new DatabaseUpdateThread(inventoryItem.getId(), getContentValues(inventoryItem.getPrice(), inventoryItem.getQuantity()-1)).start();

    }

    /**
     * get the content value to update the sqlite db row
     *
     * @return
     */
    private static ContentValues getContentValues(int price, int quantity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableInventory.COLUMN_PRICE, price);
        values.put(DatabaseContract.TableInventory.COLUMN_QUANTITY, quantity);
        return values;
    }

    /**
     * delete the inventory item from the db
     * this method will NOT get rid of the actual object - this is your responsibility
     */
    public static void deleteInventoryItem(@NonNull CompleteInventoryItem inventoryItem) {
        System.out.println("deleteInventoryItem");
        //delete the inventory item from the db
        new DatabaseDeleteThread(inventoryItem.getId()).start();
    }

    /**
     * insert the current inventory item into the database
     * will do nothing if fields are not in the right range
     */
    public static void createInventoryItem(@NonNull CompleteInventoryItem inventoryItem) {
        //check if fields are in right range
        if(validateFields(inventoryItem)){
            new DatabaseNewItemThread(getNewItemContentValues(inventoryItem)).start();
        }
    }

    /**
     * check required fields are not empty and not negative where applicable
     * @param inventoryItem
     * @return
     */
    private static boolean validateFields(CompleteInventoryItem inventoryItem) {
        return !(inventoryItem.getPrice() < 0
                || inventoryItem.getQuantity() < 0
                || inventoryItem.getName().isEmpty()
                || inventoryItem.getContactEmail().isEmpty());
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
