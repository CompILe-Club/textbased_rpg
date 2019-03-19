//Created By: Daniel August Johnston
//Author Title: CompILe President (Since Spring 2019)
//Date of Completion: 14 February 2019
//Parent Project: Text Based RPG
//Company or Organization: CompILe @ Illinois Central College East Peoria, IL
//Purpose: This class defines what an usable item is that is within the overall class of items.

public class UsableItem implements Usable
{
    //Holds the item name and can not be changed after the instance of a EquipableItem has been made.
    private final String itemName;
    //Holds the item id and can not be changed after the instance of a EquipableItem has been made.
    //This is an unique item code which defines this item.
    private final int itemId;
    //Holds the favor text of this item, which describes what this item looks like
    //and some other lore related information and can not be changed after the instance of a EquipableItem has been made.
    private final String itemDetails;
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
    UsableItem(String itemName, int itemId, String itemEffect, int cooldown
            , String requirements, String itemDetails)
    {
        this.itemName = itemName;
        this.itemId = itemId;
        this.itemEffect = itemEffect;
        this.cooldown = cooldown;
        this.requirements = requirements;
        this.itemDetails = itemDetails;
    }
    
    //Returns the favor text of this item.
    @Override
    public String getDetails()
    {
        return itemDetails;
    }
    
    //Returns the sring of the items name.
    @Override
    public String getItemName()
    {
        return itemName;
    }
    
    //Returns the items id unique to this item.
    @Override
    public int getItemId()
    {
        return itemId;
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
        return String.format("%s\n\tItem Effect:\n\t%s\nItem Cooldown:\n\t%d Seconds\nItem Requirements:\n\t%s\nItem Details:\n\t%s\n"
                , itemName, itemEffect, cooldown,requirements, itemDetails);
    }
}
