package roman.com.inventoryapplication.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import roman.com.inventoryapplication.R;
import roman.com.inventoryapplication.contracts.EditorContract;
import roman.com.inventoryapplication.data.DatabaseContract;
import roman.com.inventoryapplication.listeners.FragmentActionListener;


/**
 * A fragment representing an item editor view
 */
public class NewItemFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {


    // id of the article loader
    private static final int LOADER_ID = 403;
    private EditorContract.Presenter mPresenter;
    private FragmentActionListener mFragmentActionListener;

    private ImageView mItemPictureImageView;
    private TextView mItemNameTextView;
    private TextView mItemPriceTextView;
    private TextView mItemQuantityTextView;
    private Button mIncreasePriceButton;
    private Button mDecreasePriceButton;
    private Button mIncreaseQuantityButton;
    private Button mDecreaseQuantityButton;
    private Button mContactButton;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editor, container, false);
        mItemPictureImageView = (ImageView) view.findViewById(R.id.fragment_editor_picture);
        mItemNameTextView = (TextView) view.findViewById(R.id.fragment_editor_name);
        mItemPriceTextView = (TextView) view.findViewById(R.id.fragment_editor_price);
        mItemQuantityTextView = (TextView) view.findViewById(R.id.fragment_editor_quantity);
        mContactButton = (Button) view.findViewById(R.id.fragment_editor_contact);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        mFragmentActionListener = (FragmentActionListener) getActivity();
        // Kick off the loader
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                DatabaseContract.TableInventory.COLUMN_ID,
                DatabaseContract.TableInventory.COLUMN_NAME,
                DatabaseContract.TableInventory.COLUMN_PRICE,
                DatabaseContract.TableInventory.COLUMN_QUANTITY};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getContext(),   // Parent activity context
                DatabaseContract.TableInventory.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
