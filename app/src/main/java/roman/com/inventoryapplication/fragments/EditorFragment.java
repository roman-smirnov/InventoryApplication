package roman.com.inventoryapplication.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import roman.com.inventoryapplication.Presenters.EditorPresenter;
import roman.com.inventoryapplication.R;
import roman.com.inventoryapplication.contracts.EditorContract;
import roman.com.inventoryapplication.dataobjects.CompleteInventoryItem;
import roman.com.inventoryapplication.listeners.FragmentActionListener;


/**
 * A fragment representing an item editor view
 */
public class EditorFragment extends Fragment implements EditorContract.View{


    private EditorContract.Presenter mPresenter;
    // id of the article loader
    private static final int LOADER_ID = 403;

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
     * this key is used to restore an item, by its id, from the database
     */
    public static final String KEY_ITEM_ID = "ITEM_ID";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EditorFragment() {
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
        mPresenter = new EditorPresenter(this, getLoaderManager());

        mIncreasePriceButton = (Button) view.findViewById(R.id.fragment_editor_increase_price);
        mDecreasePriceButton = (Button) view.findViewById(R.id.fragment_editor_decrease_price);

        mIncreaseQuantityButton = (Button) view.findViewById(R.id.fragment_editor_increase_quantity);
        mDecreaseQuantityButton = (Button) view.findViewById(R.id.fragment_editor_decrease_quantity);

        setButtonListeners();

        return view;
    }

    /**
     * set button click listeners
     */
    private void setButtonListeners() {
        //set listener using lambdas
        mIncreasePriceButton.setOnClickListener((View view) -> mPresenter.increasePrice());
        mDecreasePriceButton.setOnClickListener((View view) -> mPresenter.decreasePrice());
        mIncreaseQuantityButton.setOnClickListener((View view) -> mPresenter.increaseQunatity());
        mDecreaseQuantityButton.setOnClickListener((View view) -> mPresenter.decreaseQuantity());
        mContactButton.setOnClickListener((View view) -> mPresenter.emailContact());
    }


    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setItem(getArguments().getInt(KEY_ITEM_ID));
        mFragmentActionListener = (FragmentActionListener) getActivity();
    }


    /**
     * load show data loaded from the sqlite database
     * @param item
     */
    @Override
    public void showItem(CompleteInventoryItem item) {
        mItemPictureImageView.setImageDrawable(item.getDrawable());
        mItemNameTextView.setText(item.getName());
        mItemPriceTextView.setText(String.valueOf(item.getPrice()));
        mItemQuantityTextView.setText(String.valueOf(item.getQuantity()));
        mContactButton.setText(item.getContactEmail());
    }

    @Override
    public void showEmailContact() {

    }

}
