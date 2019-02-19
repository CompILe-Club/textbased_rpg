//Created By: Daniel August Johnston
//Author Title: CompILe President (Since Spring 2019)
//Date of Completion: 14 February 2019
//Parent Project: Text Based RPG
//Company or Organization: CompILe @ Illinois Central College East Peoria, IL
//Purpose: This class defines what an item is that is within the overall class of items.

public class TrashItem implements Item
{
    //Holds the item name and can not be changed after the instance of a EquipableItem has been made.
    private final String itemName;
    //Holds the item id and can not be changed after the instance of a EquipableItem has been made.
    //This is an unique item code which defines this item.
    private final int itemId;
    //Holds the favor text of this item, which describes what this item looks like
    //and some other lore related information and can not be changed after the instance of a EquipableItem has been made.
    private final String itemDetails;
    
    //Initializing the item with the desired information input when calling this constructor.
    TrashItem(String itemName, int itemId, String itemDetails)
    {
        this.itemName = itemName;
        this.itemId = itemId;
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
    
    //Returns the items complete detailed information
    @Override
    public String toString()
    {
        return String.format("%s\n\tItem Details:\n\t%s\n"
                , itemName, itemDetails);
    }
}
