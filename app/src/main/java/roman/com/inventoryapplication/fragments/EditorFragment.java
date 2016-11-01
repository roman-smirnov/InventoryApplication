package roman.com.inventoryapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import roman.com.inventoryapplication.Presenters.EditorPresenter;
import roman.com.inventoryapplication.R;
import roman.com.inventoryapplication.contracts.EditorContract;
import roman.com.inventoryapplication.dataobjects.CompleteInventoryItem;
import roman.com.inventoryapplication.listeners.FragmentActionListener;


/**
 * A fragment representing an item editor view
 */
public class EditorFragment extends Fragment implements EditorContract.View {
    /**
     * this key is used to restore an item, by its id, from the database
     */
    public static final String KEY_ITEM_ID = "ITEM_ID";
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

        //set the toolbar
        setHasOptionsMenu(true);

//        getActivity().getActionBar().setHomeButtonEnabled(true);

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
        mIncreasePriceButton.setOnClickListener(view -> mPresenter.increasePrice());
        mDecreasePriceButton.setOnClickListener(view -> mPresenter.decreasePrice());
        mIncreaseQuantityButton.setOnClickListener(view -> mPresenter.increaseQunatity());
        mDecreaseQuantityButton.setOnClickListener(view -> mPresenter.decreaseQuantity());
        mContactButton.setOnClickListener(view -> mPresenter.emailContact());
    }


    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setItem(getArguments().getInt(KEY_ITEM_ID));
        mFragmentActionListener = (FragmentActionListener) getActivity();
    }


    /**
     * load show data loaded from the sqlite database
     *
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

    /**
     * open an external email app to send an email to the given email address
     * @param address
     */
    @Override
    public void showEmailContact(String address) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{address});
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_text_suject));
        i.putExtra(Intent.EXTRA_TEXT   , getString(R.string.email_text_body));
        try {
            startActivity(Intent.createChooser(i, getString(R.string.email_choose_text)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.fragment_editor_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //back arrow cliked on actionbar
                System.out.println(">>> case android.R.id.home ");
                removeFromView();
                return true;
            case R.id.fragment_editor_edit_icon:
                //delete icon clicked on actionbar
                System.out.println(">>> case R.id.fragment_editor_edit_icon ");
                mPresenter.deleteItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void removeFromView() {
        System.out.println(">>> removeFromView ");
        mFragmentActionListener.removeForegroundFragment();
        // TODO tell activity to remove fragment from view
    }
}
