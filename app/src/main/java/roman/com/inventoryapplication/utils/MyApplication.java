package roman.com.inventoryapplication.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by roman on 10/8/16.
 */

public class MyApplication extends Application {
    private static Context sContext;
    private static final String MAIN_PACKAGE_NAME = "roman.com.inventoryapplication";

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.sContext = getApplicationContext();
    }

    public static Context getContext() {
        return MyApplication.sContext;
    }

    public static SharedPreferences getSharePreferences() {
        return sContext.getSharedPreferences(MAIN_PACKAGE_NAME, MODE_PRIVATE);
    }

}
