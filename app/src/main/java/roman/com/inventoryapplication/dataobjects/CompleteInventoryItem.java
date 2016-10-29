package roman.com.inventoryapplication.dataobjects;

import android.graphics.drawable.Drawable;

/**
 *
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

    public void increasePrice() {
        mPrice++;
        new PriceIncreaser().start();
    }

    public void decreasePrice(){
        mPrice--;
        new PriceDecreaser().start();
    }

    public void increaseQuantity(){
        mQuantity++;
        new QuantityIncreaser().start();
    }
    public void decreaseQuantity(){
        mQuantity--;
        new QuantityDecreaser().start();
    }


    /**
     * this thread is used to update the price increase at the sqlite database
     */
    private static class PriceIncreaser extends Thread{
        @Override
        public void run() {
            //TODO implement price increase query
        }
    }


    /**
     * this thread is used to update the price increase at the sqlite database
     */
    private static class PriceDecreaser extends Thread{
        @Override
        public void run() {
        }
    }


    /**
     * this thread is used to update the quantity up  at the sqlite database
     */
    private static class QuantityIncreaser extends Thread{
        @Override
        public void run() {
            //TODO implement db quantity increase
        }
    }

    /**
     * this thread is used to update the quantity down at the sqlite database
     */
    private static class QuantityDecreaser extends Thread{
        @Override
        public void run() {
            //TODO implement price increase query
        }
    }


}
