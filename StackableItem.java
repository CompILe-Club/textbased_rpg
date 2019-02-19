public abstract class StackableItem implements Stackable
{
    private final String itemName;
    private final int itemId;
    private final String itemDetails;
    private final int MAXSTACKAMOUNT;
    
    StackableItem(String itemName, int itemId, String itemDetails, int maxamount)
    {
        this.itemName = itemName;
        this.itemId = itemId;
        this.itemDetails = itemDetails;
        this.MAXSTACKAMOUNT = maxamount;
    }
    
    @Override
    public int maxStackAmount()
    {
        return MAXSTACKAMOUNT;
    }

    @Override
    public String getDetails()
    {
        return itemDetails;
    }
    
    @Override
    public String getItemName()
    {
        return itemName;
    }
    
    @Override
    public int getItemId()
    {
        return itemId;
    }
}
