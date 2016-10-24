package roman.com.inventoryapplication.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import roman.com.inventoryapplication.R;
import roman.com.inventoryapplication.adapters.ItemsCursorRecyclerViewAdaper;
import roman.com.inventoryapplication.data.DatabaseContract;
import roman.com.inventoryapplication.listeners.RecyclerTouchListener;


/**
 * A fragment representing a list of inventory items
 */
public class ItemFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, RecyclerTouchListener.ClickListener{

    // id of the article loader
    private static final int LOADER_ID = 2701;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ItemsCursorRecyclerViewAdaper mAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.inventory_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.circular_progress_bar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new ItemsCursorRecyclerViewAdaper(getContext(),null);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        //touch events will be called on 'this'
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView, this));
        return view;
    }

    /**
     * a list item onclick listener
     * @param view
     * @param position
     */
    @Override
    public void onClick(View view, int position) {
        //TODO onClick open the detail activity
    }

    /**
     * a list item onLongClick listener
     * @param view
     * @param position
     */
    @Override
    public void onLongClick(View view, int position) {
        // not implemented in our app
    }


    @Override
    public void onStart() {
        super.onStart();
        // Kick off the loader
        getLoaderManager().initLoader(LOADER_ID, null, this);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                DatabaseContract.TableInventory.COLUMN_ID,
                DatabaseContract.TableInventory.COLUMN_NAME,
                DatabaseContract.TableInventory.COLUMN_PRICE,
                DatabaseContract.TableInventory.COLUMN_QUANTITY };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getContext(),   // Parent activity context
                DatabaseContract.TableInventory.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
