package roman.com.inventoryapplication.data;


import android.content.ContentResolver;
import android.net.Uri;

/**
 * this class defines the database schema for the sqlite database
 */
public final class DatabaseContract {

    public static final  int    DATABASE_VERSION   = 1;
    public static final  String DATABASE_NAME      = "inventory_database";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String INT_TYPE          = " INTEGER";
    private static final String BLOB_TYPE          = " BLOB";
    private static final String NOTNULL_CONSTRAINT = " NOT NULL";
    private static final String COMMA_SEP          = ",";

    /**
     *     the content authority - same string as in the manifest provider tag
     */
    public static final String CONTENT_AUTHORITY = "roman.com.inventoryapplication";

    /**
     *  base uri with which to contact the content provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * append this to the base content uri to get the invenotry items table
     */
    public static final String PATH_INVENTORY_ITEMS = "items";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DatabaseContract() {}


    /**
     * this class is a template for other classes to represent a database table
     */
    public static abstract class Tables{

        public static String TABLE_NAME;
        public static String COLUMN_ID;
        public static String CREATE_TABLE;
        public static String DELETE_TABLE;
    }


    /**
     * Table habits.
     */
    public static class TableInventory extends Tables {

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of items.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY_ITEMS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single item.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY_ITEMS;

        /**
         * the content uri to access the data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY_ITEMS);



        public static final String TABLE_NAME = "inventory";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_ORDER_CONTACT_EMAIL = "order_contact_email";
        public static final String COLUMN_PICTURE = "picture";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                COLUMN_ID + INT_TYPE + " PRIMARY KEY" + COMMA_SEP +
                COLUMN_NAME + TEXT_TYPE + NOTNULL_CONSTRAINT + COMMA_SEP +
                COLUMN_PRICE + INT_TYPE + NOTNULL_CONSTRAINT + COMMA_SEP +
                COLUMN_QUANTITY + INT_TYPE + NOTNULL_CONSTRAINT + COMMA_SEP +
                COLUMN_ORDER_CONTACT_EMAIL + TEXT_TYPE + NOTNULL_CONSTRAINT + COMMA_SEP +
                COLUMN_PICTURE + BLOB_TYPE + NOTNULL_CONSTRAINT + " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        /**
         * sanity check for the price parameter
         * @param price
         * @return
         */
        public static boolean isPriceValid(int price) {
            return price >= 0;
        }

        /**
         * sanity check for the quantity parameter
         * @param quantity
         * @return
         */
        public static boolean isQuantityValid(int quantity) {
            return quantity >= 0;
        }

    }
}
