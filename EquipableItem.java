//Created By: Daniel August Johnston
//Author Title: CompILe President (Since Spring 2019)
//Date of Completion: 14 February 2019
//Parent Project: Text Based RPG
//Company or Organization: CompILe @ Illinois Central College East Peoria, IL
//Purpose: This class defines what an equipable item is within the overall class of items.

public class EquipableItem implements Equipable
{
    //Holds the item name and can not be changed after the instance of a EquipableItem has been made.
    private final String itemName;
    //Holds the item id and can not be changed after the instance of a EquipableItem has been made.
    //This is an unique item code which defines this item.
    private final int itemId;
    //Holds the slot id and can not be changed after the instance of a EquipableItem has been made.
    //This allows us to know what equipment slot this item is for.
    private final String slotId;
    //Holds a string that has the requirements for using this item and 
    //can not be changed after the instance of a EquipableItem has been made.
    //This will allow us to control what classes can wear and what levels and skills
    //are needed for wearing the item and can not be changed after the instance of a EquipableItem has been made.
    private final String requirements;
    //Holds a string that has the stat bonuses given to the wearer of this item
    //and can not be changed after the instance of a EquipableItem has been made.
    private final String itemStats;
    //Holds the favor text of this item, which describes what this item looks like
    //and some other lore related information and can not be changed after the instance of a EquipableItem has been made.
    private final String itemDetails;
    //Tells if the item is currently equipped or not and can not be changed after the instance of a EquipableItem has been made.
    private boolean equipped;
    
    //Initializing the item with the desired information input when calling this
    //constructor.
    EquipableItem(String itemName, int itemID, String slotId, String requirements, 
            String itemStats, String itemDetails, boolean equipped)
    {
        this.itemName = itemName;
        this.itemId = itemID;
        this.slotId = slotId;
        this.requirements = requirements;
        this.itemStats = itemStats;
        this.itemDetails = itemDetails;
        this.equipped = equipped;
    }
    
    //Returns a boolean value true if the item is currently equipped and false if not.
    @Override
    public boolean isEquip()
    {
        return equipped;
    }
    
    //Returns the string of stat bonuses given by this item.
    @Override
    public String getItemStats()
    {
        return itemStats;
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
    
    //Returns the favor text of this item.
    @Override
    public String getDetails()
    {
        return  itemDetails;
    }
    
    //Returns the slot type of this item.
    @Override
    public String getSlotId()
    {
        return slotId;
    }
    
    //Checks to see if a player can wear this item or not.
    @Override
    public boolean canEquit(String currentStats)
    {
        return currentStats.equalsIgnoreCase(requirements);
    }
    
    //Returns the items complete detailed information
    @Override
    public String toString()
    {
        return String.format("%s\n\tItem Stats:\n\t%s\nItem Requirements:\n\t%s\nItem Details:\n\t%s\n"
                , itemName, itemStats,requirements, itemDetails);
    }
}
