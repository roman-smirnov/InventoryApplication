package roman.com.inventoryapplication.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

import roman.com.inventoryapplication.Presenters.NewItemPresenter;
import roman.com.inventoryapplication.R;
import roman.com.inventoryapplication.contracts.NewItemContract;
import roman.com.inventoryapplication.dataobjects.CompleteInventoryItem;
import roman.com.inventoryapplication.listeners.FragmentActionListener;

import static android.app.Activity.RESULT_OK;
import static roman.com.inventoryapplication.utils.Preconditions.checkNotNull;


/**
 * A fragment representing an item editor view
 */
public class NewItemFragment extends Fragment implements NewItemContract.View {


    private static final int RESULT_LOAD_IMAGE = 403;

    private NewItemContract.Presenter mPresenter;
    private FragmentActionListener mFragmentActionListener;
    private ImageView mItemPictureImageView;
    private EditText mItemNameEditText;
    private EditText mItemPriceEditText;
    private EditText mItemQuantityEditText;
    private EditText mItemContactEditTExt;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new NewItemPresenter(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_item, container, false);

        System.out.println(">>> NewItemFragment oncreateview");


        //set the toolbar
        setHasOptionsMenu(true);

        mItemPictureImageView = (ImageView) view.findViewById(R.id.fragment_new_item_picture);

        //set the click listener to get the image url
        mItemPictureImageView.setOnClickListener(v -> {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
        );

        mItemNameEditText = (EditText) view.findViewById(R.id.fragment_new_item_name);
        mItemPriceEditText = (EditText) view.findViewById(R.id.fragment_new_item_price);
        mItemQuantityEditText = (EditText) view.findViewById(R.id.fragment_new_item_quantity);
        mItemContactEditTExt = (EditText) view.findViewById(R.id.fragment_new_item_contact);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFragmentActionListener = (FragmentActionListener) getActivity();
    }

    /**
     * what
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //back arrow cliked on actionbar
            case android.R.id.home:
                System.out.println(">>> case android.R.id.home ");

                //check fields are in the right range
                if (mItemNameEditText.getText().toString().isEmpty()
                        || mItemPriceEditText.getText().toString().isEmpty()
                        || mItemQuantityEditText.getText().toString().isEmpty()
                        || mItemContactEditTExt.getText().toString().isEmpty()) {

                    removeFromView();
                    return true;
                }

                //save whatever the user did here
                String name = mItemNameEditText.getText().toString();
                int price = Integer.valueOf(mItemPriceEditText.getText().toString());
                int quantity = Integer.valueOf(mItemPriceEditText.getText().toString());
                String contactEmail = mItemContactEditTExt.getText().toString();
                Drawable drawable = mItemPictureImageView.getDrawable();
                CompleteInventoryItem inventoryItem = new CompleteInventoryItem(name, price, quantity, 0, drawable, contactEmail);
                //tell presenter to save the item
                mPresenter.createItem(inventoryItem);
                removeFromView();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mItemPictureImageView.setImageBitmap(bmp);

        }

    }

    private Bitmap getBitmapFromUri(@NonNull Uri uri) throws IOException {
        checkNotNull(uri);
        ParcelFileDescriptor parcelFileDescriptor =
                getActivity().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void removeFromView() {
        System.out.println(">>> removeFromView ");
        mFragmentActionListener.removeForegroundFragment();
    }

}
