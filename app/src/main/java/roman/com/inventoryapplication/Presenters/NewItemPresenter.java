package roman.com.inventoryapplication.Presenters;

import android.support.annotation.NonNull;

import roman.com.inventoryapplication.contracts.NewItemContract;
import roman.com.inventoryapplication.dataobjects.CompleteInventoryItem;

import static roman.com.inventoryapplication.utils.Preconditions.checkNotNull;

/**
 * Created by roman on 10/31/16.
 */

public class NewItemPresenter implements NewItemContract.Presenter{
    private NewItemContract.View mView;

    public NewItemPresenter(NewItemContract.View view) {
        mView = view;
    }

    @Override
    public void createItem(@NonNull CompleteInventoryItem item) {
        checkNotNull(item);
        item.createInventoryItem();
        mView.removeFromView();
    }
}
