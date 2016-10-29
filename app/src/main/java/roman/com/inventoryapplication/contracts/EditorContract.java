package roman.com.inventoryapplication.contracts;

import roman.com.inventoryapplication.dataobjects.CompleteInventoryItem;

/**
 * Created by roman on 10/27/16.
 */

public interface EditorContract {
    interface View {
        void showItem(CompleteInventoryItem item);

        void showEmailContact();
    }

    interface Presenter {
        void setItem(int id);

        void decreaseQuantity();

        void increaseQunatity();

        void decreasePrice();

        void increasePrice();

        void emailContact();
    }
}
