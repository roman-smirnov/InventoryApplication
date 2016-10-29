package roman.com.inventoryapplication.dataobjects;

import android.graphics.drawable.Drawable;

/**
 * Created by roman on 10/29/16.
 */

public class CompleteInventoryItem extends InventoryItem{
    private Drawable mDrawable;
    private String mContact;

    public CompleteInventoryItem(InventoryItem inventoryItem,String contactEmail, Drawable drawable) {
        super(inventoryItem.getName(),inventoryItem.getPrice(),inventoryItem.getQuantity(),inventoryItem.getId());
        mDrawable = drawable;
        mContact = contactEmail;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public String getContactEmail() {
        return mContact;
    }
}
