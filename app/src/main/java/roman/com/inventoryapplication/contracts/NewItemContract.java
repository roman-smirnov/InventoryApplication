package roman.com.inventoryapplication.contracts;

import roman.com.inventoryapplication.dataobjects.CompleteInventoryItem;

/**
 * Created by roman on 10/31/16.
 */

public interface NewItemContract {

    interface View{
        void removeFromView();
    }

    interface Presenter{
        void createItem(CompleteInventoryItem item);
    }
}
