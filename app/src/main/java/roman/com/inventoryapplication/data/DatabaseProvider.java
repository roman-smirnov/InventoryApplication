package roman.com.inventoryapplication.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {

    // uri matcher const for the whole inventory items table
    private static final int ITEMS = 3141;
    // uri matcher const for a single inventory item
    private static final int ITEM_ID = 2718;
    // uriMatcher object to match a content URI to a corresponding code
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // statically initialize the UriMatcher
    static {
        //match the inventory items path to the ITEMS code
        mUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_INVENTORY_ITEMS, ITEMS);
        //match the single inventory item path to the ITEM_ID code
        mUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, DatabaseContract.PATH_INVENTORY_ITEMS + "/#", ITEM_ID);
    }

    // our database helper
    private DatabaseHelper mDatabaseHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDatabaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();

        Cursor cursor;
        // Figure out if the URI matcher can match the URI to a specific code
        int match = mUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // query the table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the inventory items table.
                cursor = database.query(DatabaseContract.TableInventory.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ITEM_ID:
                // extract the id from the uri and use that as the select argument
                selection = DatabaseContract.TableInventory.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // Cursor containing that row of the table.
                cursor = database.query(DatabaseContract.TableInventory.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the cursor
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertItem(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
        //unreacheable
    }


    /**
     * Check that the name is not null
     * @param values
     * will throw an illegalArgumentException at runtime if argument is not valid
     */
    private void checkItemName(ContentValues values) {
        String name = values.getAsString(DatabaseContract.TableInventory.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Item requires a name");
        }
    }

    /**
     * check the price is not null and valid
     * @param values
     * will throw an illegalArgumentException at runtime if argument is not valid
     */
    private void checkItemPrice(ContentValues values) {
        //check the price is not null and valid
        Integer price = values.getAsInteger(DatabaseContract.TableInventory.COLUMN_PRICE);
        if (price == null || !DatabaseContract.TableInventory.isPriceValid(price)) {
            throw new IllegalArgumentException("Price should be a non-negative number");
        }
    }

    /**
     * check the quantity is not null and valid
     * @param values
     * will throw an illegalArgumentException at runtime if argument is not valid
     */
    private void checkItemQuantity(ContentValues values) {
        //check the quantity is not null and valid
        Integer price = values.getAsInteger(DatabaseContract.TableInventory.COLUMN_PRICE);
        if (price == null || !DatabaseContract.TableInventory.isQuantityValid(price)) {
            throw new IllegalArgumentException("Price should be a non-negative number");
        }
    }

    /**
     * check the order contact is not null and valid
     * @param values
     * will throw an illegalArgumentException at runtime if argument is not valid
     */
    private void checkItemContact(ContentValues values) {
        //check the order contact is not null and valid
        String contact = values.getAsString(DatabaseContract.TableInventory.COLUMN_ORDER_CONTACT_EMAIL);
        if (contact == null) {
            throw new IllegalArgumentException("Item requires a order contact email address");
        }
    }

    /**
     * check the picture blob is no null
     * @param values
     * will throw an illegalArgumentException at runtime if argument is not valid
     */
    private void checkItemBlob(ContentValues values) {
        //check the picture blob is no null
        byte[] blob = values.getAsByteArray(DatabaseContract.TableInventory.COLUMN_PICTURE);
        if (blob == null) {
            throw new IllegalArgumentException("Item requires a picture");
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {

        //if one of these is not invalid - an illegalArgumentException will be called at runtime
        checkItemName(values);
        checkItemPrice(values);
        checkItemQuantity(values);
        checkItemContact(values);
        checkItemBlob(values);

        // Get writeable database
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(DatabaseContract.TableInventory.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            System.out.println(">>> INSERTION FAILED");
            return null;
        }

        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }


    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEM_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = DatabaseContract.TableInventory.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        //if one of these is not invalid - an illegalArgumentException will be called at runtime
        checkItemName(values);
        checkItemPrice(values);
        checkItemQuantity(values);
        checkItemContact(values);
        checkItemBlob(values);

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(DatabaseContract.TableInventory.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }


    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = mUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(DatabaseContract.TableInventory.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                // Delete a single row given by the ID in the URI
                selection = DatabaseContract.TableInventory.COLUMN_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(DatabaseContract.TableInventory.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return DatabaseContract.TableInventory.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return DatabaseContract.TableInventory.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}