//Created By: Daniel August Johnston
//Author Title: CompILe President (Since Spring 2019)
//Date of Completion: 14 February 2019
//Parent Project: Text Based RPG
//Company or Organization: CompILe @ Illinois Central College East Peoria, IL
//Purpose: This class defines what an consumable item that is also stackable that is within the overall class of items.

public class StackableConsumableItem extends StackableItem
{
    //Holds the current amount of this item in a stack.
    private int currentAmount;
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
    
    //Initializing the item with the desired information input when calling this constructor.
    StackableConsumableItem(String itemName, int value,int itemId, String itemDetails, int maxamount
            , int currentAmount, String itemEffect, int cooldown, String requirements)
    {
        super(itemName,value, itemId,itemDetails,maxamount);
        this.currentAmount = currentAmount;
        this.itemEffect = itemEffect;
        this.cooldown = cooldown;
        this.requirements = requirements;
        this.isConsumable = true;
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
    
    //Returns the current amount of this item in this item's stack.
    @Override
    public int stackAmount()
    {
        return currentAmount;
    }
    
    public String getRequirements()
    {
        return this.requirements;
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
    public String useEffect()
    {
        String stats[] = itemEffect.split(":");
         if(stats[0].equalsIgnoreCase("buff"))
            return String.format("Item Effect:\n\t" + 
                    "Strength:     %s\n\tDexterity:    %s\n\tConstitution: %s\n\tStamina:      %s\n\n",
                    stats[1].split(";")[0], stats[1].split(";")[1], stats[1].split(";")[2],
                    stats[1].split(";")[3]);
         else
            return String.format("Item Effect:\n\t" + 
                   "Heal: %s HP\n", stats[1]);
    }
    
    //Returns the cooldown of this item.
    public int cooldownTimer()
    {
        return cooldown;
    }
    
    public String getItemEffect()
    {
        return itemEffect;
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
            return String.format("%s\n\tItem Amount:\n\t%d/%d\n\t"
                    + "Item Effect:\n\t\t" + "Strength:     %s\n\t\tDexterity:    "
                    + "%s\n\t\tConstitution: %s\n\t\tStamina:      %s"
                    + "\n\tItem Cooldown:\n\t%d Seconds\n\tItem Requirements:\n\t%s\n\tItem Details:\n\t%s\n\n"
                    , super.getItemName(), currentAmount,super.maxStackAmount(),
                    stats[1].split(";")[0], stats[1].split(";")[1], stats[1].split(";")[2],
                    stats[1].split(";")[3], cooldown/1000 ,requirements, super.getDetails());
        else
            return String.format("%s\n\tItem Amount:\n\t%d/%d\n\t"
                    + "Item Effect:\n\t\t" + "Heal: %s HP\n"
                    + "\tItem Cooldown:\n\t%d Seconds\n\tItem Requirements:\n\t%s\n\tItem Details:\n\t%s\n\n"
                    , super.getItemName(), currentAmount,super.maxStackAmount(),
                    stats[1], cooldown/1000 ,requirements, super.getDetails());
    }

    public String returnToTextFile()
    {
        return String.format("C#%s#%d#%s#%d#%d#%s#%d#%s", 
                this.getItemName(), this.getValue(), this.getDetails(), this.maxStackAmount()
                , this.stackAmount(), this.itemEffect, this.cooldown, this.requirements);
    }
}
