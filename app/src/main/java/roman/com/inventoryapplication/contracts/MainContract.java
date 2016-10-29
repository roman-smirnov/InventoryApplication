package roman.com.inventoryapplication.contracts;


/**
 * this interface is a contract between the MainActivity (the view) and the MainPresenter (presenter)
 */
public interface MainContract {
    interface View {
        void checkIfFirstRun();

        void incrementRunCounter();

        void showItemFragment();

        void showEditorFragment(int itemId);

        void showNewFragment();
    }

    interface Presenter extends BasePresenter {
        void isFirstRun();

        void editItem(int itemId);

        void newItem();
    }
}
