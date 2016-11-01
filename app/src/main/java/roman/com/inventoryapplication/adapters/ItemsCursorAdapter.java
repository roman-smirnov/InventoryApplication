package roman.com.inventoryapplication.adapters;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import roman.com.inventoryapplication.data.DatabaseContract;
import roman.com.inventoryapplication.dataobjects.CompleteInventoryItem;
import roman.com.inventoryapplication.dataobjects.InventoryItem;
import roman.com.inventoryapplication.utils.MyApplication;

import static roman.com.inventoryapplication.utils.Preconditions.checkNotNull;

/**
 * this class is an adapter between a cursor and a list of inventory items
 */
public class ItemsCursorAdapter {

    /**
     * returns a list of items when given a cursor
     *
     * @param cursor
     * @return
     */
    public static
    @NonNull
    List<InventoryItem> getItemListFromCursor(@NonNull Cursor cursor) {
        checkNotNull(cursor);
        List<InventoryItem> itemList = new ArrayList<>();
        //if the cursor is empty - return an empty list
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_NAME));
                int price = cursor.getInt(cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_QUANTITY));
                itemList.add(new InventoryItem(name, price, quantity, id));
            } while (cursor.moveToNext());
        }
        return itemList;
    }

    /**
     * returns a CompleteInventoryItem object given a cursor - i.e an inventory item including the contact email and a picture of the item
     *
     * @param cursor
     * @return
     */
    public static CompleteInventoryItem getCompleteItemFromCursor(@NonNull Cursor cursor) {
        checkNotNull(cursor);
        try {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_NAME));
            int price = cursor.getInt(cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_PRICE));
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_ID));
            int quantity = cursor.getInt(cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_QUANTITY));
            String contactEmail = cursor.getString(cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_ORDER_CONTACT_EMAIL));
            byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_PICTURE));
            Drawable drawable = new BitmapDrawable(MyApplication.getContext().getResources(), BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length));
            return new CompleteInventoryItem(new InventoryItem(name, price, quantity, id), contactEmail, drawable);
        } catch (Exception e) {
            return null;
        }

    }
}
