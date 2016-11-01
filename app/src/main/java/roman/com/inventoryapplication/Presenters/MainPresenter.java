package roman.com.inventoryapplication.Presenters;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

import roman.com.inventoryapplication.R;
import roman.com.inventoryapplication.contracts.MainContract;
import roman.com.inventoryapplication.data.DatabaseContract;
import roman.com.inventoryapplication.utils.MyApplication;


public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        mView.checkIfFirstRun();
    }

    @Override
    public void isFirstRun() {
        insertStuffIntoDb();
        mView.incrementRunCounter();
    }

    @Override
    public void editItem(int itemId) {
        mView.showEditorFragment(itemId);
    }

    @Override
    public void newItem() {
        mView.showNewFragment();
    }

    /**
     * Helper method to insert hardcoded data into the database. For debugging purposes only.
     */
    private void insertStuffIntoDb() {
        System.out.println(">>> insert stuff");
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        for (int i = 0; i < 10; i++) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.TableInventory.COLUMN_NAME, "item" + String.valueOf(i));
            values.put(DatabaseContract.TableInventory.COLUMN_PRICE, i);
            values.put(DatabaseContract.TableInventory.COLUMN_QUANTITY, i);
            values.put(DatabaseContract.TableInventory.COLUMN_ORDER_CONTACT_EMAIL, "email@contact"+String.valueOf(i));

            //get the byte array out of the drawable
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeResource(MyApplication.getContext().getResources(), R.mipmap.ic_launcher);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            //put the drawable as a byte array into ContentValues
            values.put(DatabaseContract.TableInventory.COLUMN_PICTURE, stream.toByteArray());

            // insert a new row into the database
            MyApplication.getContext().getContentResolver().insert(DatabaseContract.TableInventory.CONTENT_URI, values);
        }
    }
}
