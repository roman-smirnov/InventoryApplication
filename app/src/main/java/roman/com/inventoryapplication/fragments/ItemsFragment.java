package roman.com.inventoryapplication.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import roman.com.inventoryapplication.Presenters.ItemsPresenter;
import roman.com.inventoryapplication.R;
import roman.com.inventoryapplication.adapters.ItemsRecyclerAdapter;
import roman.com.inventoryapplication.contracts.ItemsContract;
import roman.com.inventoryapplication.data.DatabaseTaskHandler;
import roman.com.inventoryapplication.dataobjects.InventoryItem;
import roman.com.inventoryapplication.listeners.FragmentActionListener;
import roman.com.inventoryapplication.listeners.RecyclerClickListener;

import static roman.com.inventoryapplication.utils.Preconditions.checkNotNull;


/**
 * A fragment representing a list of inventory items
 */
public class ItemsFragment extends Fragment implements ItemsContract.View, RecyclerClickListener{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private FragmentActionListener mFragmentActionListener;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ItemsRecyclerAdapter mAdapter;

    private FloatingActionButton mFloatingActionButton;

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

        System.out.println(">>> onCreateView");
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);


        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.fragment_items_fab);

        mFloatingActionButton.setOnClickListener(v -> mPresenter.newItem());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.inventory_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.circular_progress_bar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new ItemsRecyclerAdapter(new ArrayList<InventoryItem>(0), this);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        //touch events will be called on 'this'
        mPresenter = new ItemsPresenter(this, getLoaderManager());
        return view;
    }


    /**
     * the whole row was clicked
     * @param view
     * @param position
     */
    @Override
    public void onClickRow(View view, int position) {
        System.out.println(">>> whoel row was clicked");
        mPresenter.editItem(mAdapter.getItem(position).getId());
    }

    /**
     * just the buttom inside the row was clicked
     * @param view
     * @param position
     */
    @Override
    public void onClickRowItem(View view, int position) {
        System.out.println(">>> just the button in the row was clicked");
        DatabaseTaskHandler.decreaseQuantity(mAdapter.getItem(position));
    }

    @Override
    public void onStart() {
        System.out.println(">>> onCreateView");
        super.onStart();
        mFragmentActionListener = (FragmentActionListener) getActivity();
        showLoading();
        mPresenter.start();

    }

    @Override
    public void showItems(@NonNull List<InventoryItem> itemList) {
        System.out.println(">>> showItems");
        checkNotNull(itemList);
        mAdapter.replaceData(itemList);
        hideLoading();
        hideEmptyMessage();
    }

    @Override
    public void showNewItem() {
        System.out.println(">>> showNewItem");
        mFragmentActionListener.onCreateNewItem();
    }

    @Override
    public void showEditItem(int itemId) {
        System.out.println(">>> showEditItem");
        mFragmentActionListener.onOpenExistingItem(itemId);
    }

    @Override
    public void showEmpty() {
        System.out.println(">>> showEmpty");
        showEmptyMessage();
        hideLoading();
    }

    private void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showEmptyMessage() {
        TextView noItemsTextView = (TextView) getActivity().findViewById(R.id.fragment_items_no_items_message);
        noItemsTextView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyMessage() {
        TextView noItemsTextView = (TextView) getActivity().findViewById(R.id.fragment_items_no_items_message);
        noItemsTextView.setVisibility(View.GONE);
    }
}
