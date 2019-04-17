public abstract class StackableItem implements Item
{
    private final String itemName;
    private final int itemId;
    private final String itemDetails;
    private final int MAXSTACKAMOUNT;
    private final int value;
    
    StackableItem(String itemName, int value, int itemId, String itemDetails, int maxamount)
    {
        this.itemName = itemName;
        this.itemId = itemId;
        this.itemDetails = itemDetails;
        this.MAXSTACKAMOUNT = maxamount;
        this.value = value;
    }
    
    public int maxStackAmount()
    {
        return MAXSTACKAMOUNT;
    }
    
    @Override
    public int getValue()
    {
        return this.value;
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
    
    abstract int stackAmount();
    abstract int addAmount();
    abstract int removeAmount();
}
