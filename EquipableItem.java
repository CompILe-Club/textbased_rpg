package textbasedrpg;
//Created By: Daniel August Johnston
//Author Title: CompILe President (Since Spring 2019)
//Date of Completion: 14 February 2019
//Parent Project: Text Based RPG
//Company or Organization: CompILe @ Illinois Central College East Peoria, IL
//Purpose: This class defines what an equipable item is within the overall class of items.

public class EquipableItem implements Item
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
    //Holds the damage range of weapons
    private final int damage;
    //Holds the armor value of armor
    private final int armor;
    //Tells if the equippable item is armor or weapon
    private final boolean isWeapon;
    //Zero array
    private final int[] ZERO = {0,0};
    private final int value;
    
    //Initializing the item with the desired information input when calling this
    //constructor.
    EquipableItem(String itemName, int value,int itemID, String slotId, String requirements, 
            String itemStats, String itemDetails, boolean equipped, int damage
            ,boolean isWeapon, int armor)
    {
        this.itemName = itemName;
        this.itemId = itemID;
        this.slotId = slotId;
        this.requirements = requirements;
        this.itemStats = itemStats;
        this.itemDetails = itemDetails;
        this.equipped = equipped;
        this.damage = damage;
        this.isWeapon = isWeapon;
        this.armor = armor;
        this.value = value;
    }
    
    //Returns a boolean value true if the item is currently equipped and false if not.
    public boolean isEquip()
    {
        return equipped;
    }
    
    //Returns if this is a weapon or armor
    public boolean isWeapon()
    {
        return isWeapon;
    }
    
    //Returns the armor if its armor
    public int getArmor()
    {
        if(isWeapon)
            return 0;
        else
            return armor;
    }
    
    //Returns the damage range if its a weapon
    public int getDamage(int str)
    {
        if(isWeapon)
            return damage * (str/6);
        else
            return 0;
    }
    
    @Override
    public int getValue()
    {
        return this.value;
    }
    
    //Returns the string of stat bonuses given by this item.
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
    public String getSlotId()
    {
        return slotId;
    }
    
    //Checks to see if a player can wear this item or not.
    public boolean canEquip(String currentStats)
    {
        String []itemStats = requirements.split(";");
        String []playerStats = currentStats.split(";");
        boolean isGood = true;
        for(int indexStats = 0; indexStats < playerStats.length; indexStats++)   
            if(Integer.parseInt(itemStats[indexStats]) > Integer.parseInt(playerStats[indexStats]))
                isGood = false;
        return isGood;
    }
    
    public boolean equip()
    {
        if(this.equipped)
            return this.equipped = false;
        else
            return this.equipped = true;
    }
    
    //Returns the items complete detailed information
    @Override
    public String toString()
    {
        return String.format("%s\n\tItem Stats:\n\t%s\n\tItem Slot:\n\t%s\n\tItem Requirements:\n\t%s\n\tItem Details:\n\t%s\n"
                , this.itemName, this.itemStats, this.slotId, this.requirements, this.itemDetails);
    }
    
    public String returnToTextFile()
    {
        int weapon = 0;
        if(this.isWeapon)
            weapon = 1;
        return String.format("E#%s#%d#%s#%s#%s#%s#%d#%d#%d#%d", 
                this.itemName, this.value, this.slotId, this.requirements
                , this.itemStats, this.itemDetails, 0, this.damage, 
                weapon, this.armor);
    }
}
