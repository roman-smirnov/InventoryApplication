package roman.com.inventoryapplication.contracts;

import android.support.annotation.NonNull;

import java.util.List;

import roman.com.inventoryapplication.dataobjects.InventoryItem;

/**
 * this interface is a contract between the ItemsFragment and its presenter
 */
public interface ItemsContract {
    interface View {
        void showItems(@NonNull List<InventoryItem> itemList);

        void showNewItem();

        void showEditItem(int itemId);

        void showEmpty();

    }

    interface Presenter extends BasePresenter {

        void editItem(int itemId);

        void newItem();
    }
}
