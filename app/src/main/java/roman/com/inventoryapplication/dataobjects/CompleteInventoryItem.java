package roman.com.inventoryapplication.dataobjects;

import android.graphics.drawable.Drawable;

/**
 *
 */
public class CompleteInventoryItem extends InventoryItem {
    private Drawable mDrawable;
    private String mContact;

    public CompleteInventoryItem(InventoryItem inventoryItem, String contactEmail, Drawable drawable) {
        super(inventoryItem.getName(), inventoryItem.getPrice(), inventoryItem.getQuantity(), inventoryItem.getId());
        mDrawable = drawable;
        mContact = contactEmail;
    }

    public CompleteInventoryItem(String name, int price, int quantity, int id, Drawable drawable, String contact) {
        super(name, price, quantity, id);
        mDrawable = drawable;
        mContact = contact;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public String getContactEmail() {
        return mContact;
    }


}
