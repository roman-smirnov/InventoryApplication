package roman.com.inventoryapplication.Presenters;

import roman.com.inventoryapplication.contracts.NewItemContract;
import roman.com.inventoryapplication.dataobjects.CompleteInventoryItem;

/**
 * Created by roman on 10/31/16.
 */

public class NewItemPresenter implements NewItemContract.Presenter{
    private NewItemContract.View mView;

    public NewItemPresenter(NewItemContract.View view) {
        mView = view;
    }

    @Override
    public void createItem(CompleteInventoryItem item) {
        item.createInventoryItem();
        mView.removeFromView();
    }
}
