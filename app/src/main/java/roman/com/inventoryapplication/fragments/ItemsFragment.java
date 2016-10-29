package roman.com.inventoryapplication.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import roman.com.inventoryapplication.Presenters.ItemsPresenter;
import roman.com.inventoryapplication.R;
import roman.com.inventoryapplication.adapters.ItemsRecyclerAdapter;
import roman.com.inventoryapplication.contracts.ItemsContract;
import roman.com.inventoryapplication.dataobjects.InventoryItem;
import roman.com.inventoryapplication.listeners.FragmentActionListener;
import roman.com.inventoryapplication.listeners.RecyclerTouchListener;
import roman.com.inventoryapplication.utils.MyApplication;

import static roman.com.inventoryapplication.utils.Preconditions.checkNotNull;


/**
 * A fragment representing a list of inventory items
 */
public class ItemsFragment extends Fragment implements RecyclerTouchListener.ClickListener, ItemsContract.View {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private FragmentActionListener mFragmentActionListener;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ItemsRecyclerAdapter mAdapter;

    private ItemsContract.Presenter mPresenter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemsFragment() {
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

        mAdapter = new ItemsRecyclerAdapter(new ArrayList<InventoryItem>(0));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        //touch events will be called on 'this'
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(MyApplication.getContext(), mRecyclerView, this));
        mPresenter = new ItemsPresenter(this, getLoaderManager());
        return view;
    }

    /**
     * a list item onclick listener
     *
     * @param view
     * @param position
     */
    @Override
    public void onClick(View view, int position) {
        System.out.println(">>> onClick recyclerview");
        mPresenter.editItem(mAdapter.getItem(position).getId());
    }

    /**
     * a list item onLongClick listener
     *
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
        mFragmentActionListener = (FragmentActionListener) getActivity();
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mPresenter.start();

    }

    @Override
    public void showItems(@NonNull List<InventoryItem> itemList) {
        System.out.println(">>> showItems");
        checkNotNull(itemList);
        mAdapter.replaceData(itemList);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNewItem() {

    }

    @Override
    public void showEditItem(int itemId) {
        mFragmentActionListener.onOpenExistingItem(itemId);
    }
}
