package roman.com.inventoryapplication.listeners;


/**
 * callback listener from fragment to activty
 */
public interface FragmentActionListener {

    void onCreateNewItem();

    void onOpenExistingItem(int id);

    void removeForegroundFragment();
}
