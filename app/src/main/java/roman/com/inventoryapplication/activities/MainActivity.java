package roman.com.inventoryapplication.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import roman.com.inventoryapplication.Presenters.MainPresenter;
import roman.com.inventoryapplication.R;
import roman.com.inventoryapplication.contracts.MainContract;
import roman.com.inventoryapplication.fragments.EditorFragment;
import roman.com.inventoryapplication.fragments.ItemsFragment;
import roman.com.inventoryapplication.fragments.NewItemFragment;
import roman.com.inventoryapplication.listeners.FragmentActionListener;
import roman.com.inventoryapplication.utils.MyApplication;

/**
 * the main acitivty for the inventory items app
 */
public class MainActivity extends AppCompatActivity
        implements FragmentActionListener, MainContract.View {

    // stuff for shared prefs run counter
    private static final String KEY_RUN_COUNTER = "RUN_COUNTER";
    private static final int DEFAULT_PREF_VALUE = 0;

    // a key to get the fragment id from savedInstanceState
    private static final String KEY_FRAGMENT = "FRAGMENT";
    //holds the current fragment
    private Fragment mForegroundFragment;


    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(">>> MainActivity oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            // if a fragment does not exist
            mForegroundFragment = new ItemsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, mForegroundFragment);
            fragmentTransaction.commit();
        } else {
            //get the fragment if it already exists
            mForegroundFragment = getSupportFragmentManager().findFragmentById(savedInstanceState.getInt(KEY_FRAGMENT));
        }

        mPresenter = new MainPresenter(this);
        mPresenter.start();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save the current fragment id - so it can be reused when activity is recreated
        outState.putInt(KEY_FRAGMENT, mForegroundFragment.getId());
    }


    /**
     * check if it's the first run of the app
     */
    @Override
    public void checkIfFirstRun() {
        int runNumber = MyApplication.getSharePreferences().getInt(KEY_RUN_COUNTER, DEFAULT_PREF_VALUE);
        if (runNumber == DEFAULT_PREF_VALUE) {
            //notify the presenter
            mPresenter.isFirstRun();
        }

    }

    /**
     * increment the app run counter by 1 - not actuall run number since orientation changes count as a new run
     */
    @Override
    public void incrementRunCounter() {
        int runNumber = MyApplication.getSharePreferences().getInt(KEY_RUN_COUNTER, DEFAULT_PREF_VALUE);
        //increment the run counter
        MyApplication.getSharePreferences().edit().putInt(KEY_RUN_COUNTER, ++runNumber).apply();
    }

    @Override
    public void showItemFragment() {

    }

    @Override
    public void showEditorFragment(int itemId) {
        System.out.println(">>> showEditorFragment");
        showBackButonOnFragment();
        mForegroundFragment = new EditorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EditorFragment.KEY_ITEM_ID, itemId);
        mForegroundFragment.setArguments(bundle);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mForegroundFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void showNewFragment() {
        showBackButonOnFragment();
        System.out.println(">>> showNewFragment");
        mForegroundFragment = new NewItemFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mForegroundFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onCreateNewItem() {
        mPresenter.newItem();
    }

    @Override
    public void onOpenExistingItem(int itemId) {

        System.out.println(">>> onOpenExistingItem");
        mPresenter.editItem(itemId);
    }

    @Override
    public void removeForegroundFragment() {
        hideBackButtonOnFragment();
        getSupportFragmentManager().popBackStack();
        System.out.println(">>> removeForegroundFragment");
//        todo FIND out if you need to reload fragment or keep the old one
//        mForegroundFragment = new ItemsFragment();
//        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, mForegroundFragment);
//        fragmentTransaction.commit();
    }

    private void showBackButonOnFragment() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void hideBackButtonOnFragment(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
    }
}

