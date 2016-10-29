package roman.com.inventoryapplication.dataobjects;

import android.database.Cursor;

import roman.com.inventoryapplication.data.DatabaseContract;

public class InventoryItem{
    protected String mName;
    protected int mPrice;
    protected int mQuantity;
    protected int mId;

    public InventoryItem(String name, int price, int quantity, int id) {
        mName = name;
        mPrice = price;
        mQuantity = quantity;
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public int getPrice() {
        return mPrice;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public int getId() {
        return mId;
    }

    public static InventoryItem fromCursor(Cursor cursor) {

        // Find the columns of  attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_NAME);
        int priceColumnIndex = cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_QUANTITY);
        int idColumnIndex = cursor.getColumnIndex(DatabaseContract.TableInventory.COLUMN_ID);


        // Read the pet attributes from the Cursor for the current item
        String itemName = cursor.getString(nameColumnIndex);
        int itemPrice = cursor.getInt(priceColumnIndex);
        int itemQuantity = cursor.getInt(quantityColumnIndex);
        int itemId = cursor.getInt(idColumnIndex);

        System.out.println(">>> id= " + itemId+" name = "+itemName+ " price= "+ itemPrice+" quantity= " +itemQuantity);

        return new InventoryItem(itemName, itemPrice,itemQuantity, itemId);
    }
}