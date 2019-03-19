//Created By: Daniel August Johnston
//Author Title: CompILe President (Since Spring 2019)
//Date of Completion: 14 February 2019
//Parent Project: Text Based RPG
//Company or Organization: CompILe @ Illinois Central College East Peoria, IL
//Purpose: This class defines what an consumable item that is also stackable that is within the overall class of items.

public class StackableConsumableItem extends StackableItem implements Consumable
{
    //Holds the current amount of this item in a stack.
    private int currentAmount;
    //Holds a string that describes what the use effect of this item does
    //and can not be changed after the instance of a EquipableItem has been made.
    private final String itemEffect;
    //Holds the amount of time this type of item is on cooldown after the player has used it
    //and can not be changed after the instance of a EquipableItem has been made..
    private final int cooldown;
    //Holds a string that has the requirements for using this item and 
    //can not be changed after the instance of a EquipableItem has been made.
    //This will allow us to control what classes can use and what levels and skills
    //are needed for using the item and can not be changed after the instance of a EquipableItem has been made.
    private final String requirements;
    
    //Initializing the item with the desired information input when calling this constructor.
    StackableConsumableItem(String itemName, int itemId, String itemDetails, int maxamount
            , int currentAmount, String itemEffect, int cooldown, String requirements)
    {
        super(itemName,itemId,itemDetails,maxamount);
        this.currentAmount = currentAmount;
        this.itemEffect = itemEffect;
        this.cooldown = cooldown;
        this.requirements = requirements;
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
    
    //Returns the string of effects given by this item.
    @Override
    public String useEffect()
    {
        return itemEffect;
    }
    
    //Returns the cooldown of this item.
    @Override
    public int cooldownTimer()
    {
        return cooldown;
    }
    
    //Checks to see if a player can use this item or not.
    @Override
    public boolean canUse(String currentStats)
    {
        return currentStats.equalsIgnoreCase(requirements);
    }
    
    //Returns the items complete detailed information
    @Override
    public String toString()
    {
        return String.format("%s\n\tItem Amount:\n\t%d/%d\nItem Effect:\n\t%s\nItem Cooldown:\n\t%d Seconds\nItem Requirements:\n\t%s\nItem Details:\n\t%s\n"
                , super.getItemName(), currentAmount,super.maxStackAmount() ,itemEffect, cooldown,requirements, super.getDetails());
    }
}
