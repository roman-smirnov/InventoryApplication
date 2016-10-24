package roman.com.inventoryapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * this class is responsible for performing CRUD operations with the sqlite database
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    /**
     * main constructor
     *
     * @param context
     */
    public DatabaseHandler(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    /**
     * initialize database on first use of app
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.TableInventory.CREATE_TABLE);
    }

    /**
     * on database upgrade - drop all tables and re-create the database
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL(DatabaseContract.TableInventory.DELETE_TABLE);

        // Create tables again
        onCreate(db);
    }

    /**
     * this method adds an inventory item to the database
     * @param name
     * @param price
     * @param quantity
     * @param contactEmail
     * @param bitmapPic
     */
    public void addItem(String name, int price, int quantity, String contactEmail, byte[] bitmapPic) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.TableInventory.COLUMN_NAME, name);
        values.put(DatabaseContract.TableInventory.COLUMN_PRICE, price);
        values.put(DatabaseContract.TableInventory.COLUMN_QUANTITY, quantity);
        values.put(DatabaseContract.TableInventory.COLUMN_ORDER_CONTACT_EMAIL, quantity);
        values.put(DatabaseContract.TableInventory.COLUMN_PICTURE, bitmapPic);


        // INSERT INTO TABLE_NAME VALUES values
        db.insert(DatabaseContract.TableInventory.TABLE_NAME, null, values);

        //close the connection
        db.close();
    }

    /**
     * this method retrieves all inventory items from the database
     */
    public Cursor getAllItems() {

        SQLiteDatabase db = this.getReadableDatabase();

        // SELECT * FROM TABLE_NAME
        Cursor cursor = db.query(DatabaseContract.TableInventory.TABLE_NAME, null, null, null, null, null, null);

        //if cursor is not empty
        if (cursor.moveToFirst()) {
            return cursor;
        }else{
            //if cursor is empty
            return null;
        }
    }


    /**
     * get a specific item from the db
     * @param itemId
     * @return
     */
    public Cursor getItem(int itemId){
        SQLiteDatabase db = this.getReadableDatabase();

        String whereClause = "id = ?";
        String[] whereArgs = { String.valueOf(itemId)};

        // SELECT * FROM TABLE_NAME WHERE id=itemId
        Cursor cursor = db.query(DatabaseContract.TableInventory.TABLE_NAME, null, whereClause, whereArgs, null, null, null);

        //if cursor is not empty
        if (cursor.moveToFirst()) {
            return cursor;
        }else{
            //if cursor is empty
            return null;
        }
    }
}
