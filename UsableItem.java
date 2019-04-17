//Created By: Daniel August Johnston
//Author Title: CompILe President (Since Spring 2019)
//Date of Completion: 14 February 2019
//Parent Project: Text Based RPG
//Company or Organization: CompILe @ Illinois Central College East Peoria, IL
//Purpose: This class defines what an usable item is that is within the overall class of items.

public class UsableItem implements Item
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
    private long lastUsed;
    //Holds the amount of time this type of item is on cooldown after the player has used it
    //and can not be changed after the instance of a EquipableItem has been made..
    private final int cooldown;
    //Holds a string that has the requirements for using this item and 
    //can not be changed after the instance of a EquipableItem has been made.
    //This will allow us to control what classes can use and what levels and skills
    //are needed for using the item and can not be changed after the instance of a EquipableItem has been made.
    private final String requirements;
    //Tells us if the item is consumed on use or not
    private final boolean isConsumable;
    private final int value;
    
    //Initializing the item with the desired information input when calling this constructor.
    UsableItem(String itemName, int value, int itemId, String itemEffect, int cooldown
            , String requirements, String itemDetails, boolean isConsumable)
    {
        this.itemName = itemName;
        this.itemId = itemId;
        this.itemEffect = itemEffect;
        this.cooldown = cooldown;
        this.requirements = requirements;
        this.itemDetails = itemDetails;
        this.isConsumable = isConsumable;
        this.value = value;
        this.lastUsed = 0;
    }
    
    public long getLastUsed()
    {
        return lastUsed;
    }
    
    public void setLastUsed(long time)
    {
        lastUsed = time;
    }
    
    //Returns the isConsumable value
    public boolean isConsumable()
    {
        return isConsumable;
    }
    
    @Override
    public int getValue()
    {
        return this.value;
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
    
    public String getItemEffect()
    {
        return itemEffect;
    }
    
    //Returns the string of effects given by this item.
    public String useEffect()
    {
        String stats[] = itemEffect.split(":");
         if(stats[0].equalsIgnoreCase("buff"))
            return String.format("Item Effect:\n\t" + 
                    "Strength:     %s\n\tDexterity:    %s\n\tConstitution: %s\n\tStamina:      %s\t\t",
                    stats[1].split(";")[0], stats[1].split(";")[1], stats[1].split(";")[2],
                    stats[1].split(";")[3]);
         else
            return String.format("Item Effect:\n\t" + 
                   "Heal: %s HP\n\n", stats[1]);
    }
    
    //Returns the cooldown of this item.
    public int cooldownTimer()
    {
        return cooldown;
    }
    
    //Checks to see if a player can use this item or not.
    public boolean canUse(String currentStats)
    {
        String []itemStats = requirements.split(";");
        String []playerStats = currentStats.split(";");
        boolean isGood = true;
        for(int indexStats = 0; indexStats < playerStats.length; indexStats++)   
            if(Integer.parseInt(itemStats[indexStats]) > Integer.parseInt(playerStats[indexStats]))
                isGood = false;
        return isGood;
    }
    
    //Returns the items complete detailed information
    @Override
    public String toString()
    {
        String stats[] = itemEffect.split(":");
        if(stats[0].equalsIgnoreCase("buff"))
            return String.format("%s\n\tItem Effect:\n\t\t" + 
                    "Strength:     %s\n\t\tDexterity:    %s\n\t\tConstitution: %s\n\t\tStamina:      %s"
                    + "\n\tItem Cooldown:\n\t%d Seconds\n\tItem Requirements:\n\t%s\n\tItem Details:\n\t%s\n"
                    , itemName, stats[1].split(";")[0], stats[1].split(";")[1], stats[1].split(";")[2],
                    stats[1].split(";")[3], cooldown/1000, requirements, itemDetails);
        else
            return String.format("%s\n\tItem Effect:\n\t\t" + 
                    "Heal: %s HP\n\tItem Cooldown:\n\t%d Seconds\n\t"
                    + "Item Requirements:\n\t%s\n\tItem Details:\n\t%s\n"
                    , itemName, stats[1], cooldown/1000, requirements, itemDetails);
    }
    
    public String returnToTextFile()
    {
        int consumable = 0;
        if(this.isConsumable)
            consumable = 1;
        return String.format("U#%s#%s#%d#%s#%s#%d", 
                this.itemName, this.itemEffect, this.cooldown, this.requirements,
                this.itemDetails,consumable);
    }
}
