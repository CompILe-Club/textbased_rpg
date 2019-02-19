//Created By: Daniel August Johnston
//Author Title: CompILe President (Since Spring 2019)
//Date of Completion: 14 February 2019
//Parent Project: Text Based RPG
//Company or Organization: CompILe @ Illinois Central College East Peoria, IL
//Purpose: This class defines what an stackable item is that is within the overall class of items.

public class StackableNonconsumableItem extends StackableItem 
{
    //Holds the current amount of this item in a stack.
    private int currentAmount;
    
    //Initializing the item with the desired information input when calling this constructor.
    StackableNonconsumableItem(String itemName, int itemId, String itemDetails, int maxamount
            , int currentAmount)
    {
        super(itemName,itemId,itemDetails,maxamount);
        this.currentAmount = currentAmount;
    }
    
    //Returns the current amount of this item in this item's stack.
    @Override
    public int stackAmount()
    {
        return currentAmount;
    }
    
    //Adds another item into the item's stack.
    @Override
    public int addAmount()
    {
        return currentAmount++;
    }
    
    //removes another item from the item's stack.
    @Override
    public int removeAmount()
    {
        return currentAmount--;
    }
    
    //Returns the items complete detailed information
    @Override
    public String toString()
    {
        return String.format("%s\n\tItem Amount:\n\t%d/%d\nItem Details:\n\t%s\n"
                , super.getItemName(), currentAmount,super.maxStackAmount(), super.getDetails());
    }
}
