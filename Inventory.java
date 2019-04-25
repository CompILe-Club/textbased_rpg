import javafx.scene.control.TextArea;
//Created By: Daniel August Johnston
//Author Title: CompILe President (Since Spring 2019)
//Date of Completion: 14 February 2019
//Parent Project: Text Based RPG
//Company or Organization: CompILe @ Illinois Central College East Peoria, IL
//Purpose: This class defines what an inventory is within the overall project of the Text Based RPG.
public class Inventory 
{
    //Holds an array of the Item interface
    private Item inventoryList[];
    //Holds the maximum allowed inventory space and can not be changed after the instance of a inventory has been made 
    private final int MAXINVENTORYSIZE = 100;
    
    //Initializing the inventory with an blank array.
    Inventory()
    {
        inventoryList = new Item[0];
    }
    
    public Item[] getInventory()
    {
        return inventoryList;
    }
    
    //Allows and item to be added into the inventory if the inventory is not full.
    //If the item being added is stackable then the addStackingItem method is called
    //else addNonstackingItem method is called.
    public boolean addItem(Item newItem)
    {
        if(inventoryList.length < MAXINVENTORYSIZE)
        {
            if(newItem instanceof StackableItem)
                addStackingItem(newItem);
            else
                addNonstackingItem(newItem);
            return true;
        }
        else
            return false;
    }
    
    //This method is called if the item being added is a stackable item.
    //This checks to see if there is a non full stack to add the items into and
    //if none is found this will call the addNonstackingItem method which will create 
    //a new item in the inventory.
    private void addStackingItem(Item newItem)
    {
        for(int itemIndex = 0; itemIndex < inventoryList.length; itemIndex++)
        {
                if(newItem.getItemId() == inventoryList[itemIndex].getItemId())
                {
                    if(((StackableItem)(inventoryList[itemIndex])).stackAmount() != ((StackableItem)(inventoryList[itemIndex])).maxStackAmount())
                    {
                        ((StackableItem)(inventoryList[itemIndex])).addAmount();
                        return;
                    }         
                }
        }
        addNonstackingItem(newItem);
                    
    }
    
    //This will create a temp inventory that will allow us to clone the current
    //inventory array and then add on the new item. Once this is done the temp
    //inventory will become the new inventory.
    private void addNonstackingItem(Item newItem)
    {
            Item tempInventory [] =  new Item[inventoryList.length + 1];
            for(int itemIndex = 0; itemIndex < inventoryList.length; itemIndex++)
                tempInventory[itemIndex] = inventoryList[itemIndex];
            tempInventory[inventoryList.length] = newItem;
            inventoryList = tempInventory;
    }
    
    //Allow for items to be removed from the inventory based on if the item is
    //stackable or not.
    public void removeItem(int item)
    {
        if(inventoryList[item] instanceof StackableItem)
            removeStackingItem(item);
        else
            removeNonstackingItem(item);
    }
    
    public void removeItemByName(String item, TextArea textArea)
    {
        boolean noItem = true;
        for(int indexInventory = 0; indexInventory < inventoryList.length; indexInventory++)
        {
            if(inventoryList[indexInventory].getItemName().equalsIgnoreCase(item))
            {
                if(inventoryList[indexInventory] instanceof EquipableItem)
                {
                    if(((EquipableItem)(inventoryList[indexInventory])).isEquip())
                    {
                        ((EquipableItem)(inventoryList[indexInventory])).equip();
                    }
                }
                if(inventoryList[indexInventory] instanceof StackableItem)
                {
                    removeStackingItem(indexInventory);
                    noItem = false;
                    break;
                }
                else
                {
                    removeNonstackingItem(indexInventory);
                    noItem = false;
                    break;
                }
            }
        }
        if(noItem)
            textArea.appendText("No Item With That Name Found\n");
    }
    
    public void removeItemByName(String item, Player currentPlayer, TextArea textArea)
    {
        boolean noItem = true;
        for(int indexInventory = 0; indexInventory < inventoryList.length; indexInventory++)
        {
            if(inventoryList[indexInventory].getItemName().equalsIgnoreCase(item))
            {
                if(inventoryList[indexInventory] instanceof EquipableItem)
                {
                    if(((EquipableItem)(inventoryList[indexInventory])).isEquip())
                    {
                        ((EquipableItem)(inventoryList[indexInventory])).equip();
                        currentPlayer.unequipItem(((EquipableItem)(inventoryList[indexInventory])));
                    }
                }
                if(inventoryList[indexInventory] instanceof StackableItem)
                {
                    removeStackingItem(indexInventory);
                    noItem = false;
                    break;
                }
                else
                {
                    removeNonstackingItem(indexInventory);
                    noItem = false;
                    break;
                }
            }
        }
        if(noItem)
            textArea.appendText("No Item With That Name Found\n");
    }
    
    //This method makes a new temp inventory with one less space, then clones all 
    //of the currently inventory without the item that is being dropped or used.
    private void removeNonstackingItem(int item)
    {
        Item tempInventory [] =  new Item[inventoryList.length - 1];
        for(int itemIndex = 0; itemIndex < tempInventory.length; itemIndex++)
        {
            if(itemIndex != item)
               tempInventory[itemIndex] = inventoryList[itemIndex];
            else
            {
                for(int leftItems = itemIndex + 1; leftItems < inventoryList.length; leftItems++)
                    tempInventory[leftItems - 1] = inventoryList[leftItems];
                break;
            }
        }
        inventoryList = tempInventory;
    }
    
    //This method will decrease the number of items in a stack or remove the stack
    //all together if there is only one item in the stack that is being dropped or used.
    private void removeStackingItem(int item)
    {
        if(((StackableItem)(inventoryList[item])).stackAmount() != 1)
            ((StackableItem)(inventoryList[item])).removeAmount();
        else
             removeNonstackingItem(item);
    }
    
    //This method checks to see if an item is usable or not and outputs the effect
    //of the item if it is or a message that the item is not usable if it is not.
    //It also checks to see if the item is a consumable item if so the comsumable
    //is destoryed on use.
    public String useItem(int item, Player currentPlayer, TextArea textArea)
    {
        String effect = "No Use Item With That Name Found\n";
        if(inventoryList[item] instanceof UsableItem)
        {
            if(System.currentTimeMillis() - ((UsableItem)inventoryList[item]).getLastUsed() > ((UsableItem)inventoryList[item]).cooldownTimer())
            {
                ((UsableItem)inventoryList[item]).setLastUsed(System.currentTimeMillis());
                effect = ((UsableItem)inventoryList[item]).useEffect();
                if(((UsableItem)inventoryList[item]).getItemEffect().split(":")[0].equalsIgnoreCase("heal"))
                {
                    currentPlayer.heal(Integer.parseInt((((UsableItem)inventoryList[item]).getItemEffect().split(":")[1])));
                }
                else if(((UsableItem)inventoryList[item]).getItemEffect().split(":")[0].equalsIgnoreCase("buff"))
                {
                    currentPlayer.buff(((UsableItem)inventoryList[item]).getItemEffect().split(":")[1].split(";"));
                    currentPlayer.buff(100000, (System.currentTimeMillis()), inventoryList[item], textArea);
                }
                if(((UsableItem)inventoryList[item]).isConsumable())
                    removeItem(item);
            }
            else
                effect = String.format("%s is currently on cooldown %d seconds remaining",
                        inventoryList[item].getItemName(), (-1)*(System.currentTimeMillis() - ((UsableItem)inventoryList[item]).getLastUsed() - ((UsableItem)inventoryList[item]).cooldownTimer())/1000);
        }
        else if(inventoryList[item] instanceof StackableConsumableItem)
        {
            if(System.currentTimeMillis() - ((StackableConsumableItem)inventoryList[item]).getLastUsed() > ((StackableConsumableItem)inventoryList[item]).cooldownTimer())
            {
                ((StackableConsumableItem)inventoryList[item]).setLastUsed(System.currentTimeMillis());
                if(((StackableConsumableItem)inventoryList[item]).getItemEffect().split(":")[0].equalsIgnoreCase("heal"))
                {
                    currentPlayer.heal(Integer.parseInt(((StackableConsumableItem)inventoryList[item]).getItemEffect().split(":")[1]));
                }
                else if(((StackableConsumableItem)inventoryList[item]).getItemEffect().split(":")[0].equalsIgnoreCase("buff"))
                {
                    currentPlayer.buff(((StackableConsumableItem)inventoryList[item]).getItemEffect().split(":")[1].split(";"));
                    currentPlayer.buff(100000, (System.currentTimeMillis()), inventoryList[item], textArea);
                }
                effect = ((StackableConsumableItem)inventoryList[item]).useEffect();
                currentPlayer.buff(100000, (System.currentTimeMillis()), inventoryList[item], textArea);
                removeItem(item);
            }
            else
                effect = String.format("%s is currently on cooldown %d seconds remaining",
                        inventoryList[item].getItemName(), (-1)*(System.currentTimeMillis() - ((StackableConsumableItem)inventoryList[item]).getLastUsed() - ((StackableConsumableItem)inventoryList[item]).cooldownTimer())/1000);
        }
        return effect;    
    }
    
    public String useItemByName(String item, Player currentPlayer, TextArea textArea)
    {
        String effect = "No Item With That Name Found\n";
        for(int indexInventory = 0; indexInventory < inventoryList.length; indexInventory++)
            if(inventoryList[indexInventory].getItemName().equalsIgnoreCase(item))
            {
                effect = "This Item has no use/consume effect.\n";
                if(inventoryList[indexInventory] instanceof UsableItem)
                {
                    if((System.currentTimeMillis() - ((UsableItem)inventoryList[indexInventory]).getLastUsed()) > ((UsableItem)inventoryList[indexInventory]).cooldownTimer())
                    {
                        ((UsableItem)inventoryList[indexInventory]).setLastUsed(System.currentTimeMillis()); 
                        effect = ((UsableItem)inventoryList[indexInventory]).useEffect();
                        if(((UsableItem)inventoryList[indexInventory]).getItemEffect().split(":")[0].equalsIgnoreCase("heal"))
                        {
                            currentPlayer.heal(Integer.parseInt((((UsableItem)inventoryList[indexInventory]).getItemEffect().split(":")[1])));
                        }
                        else if(((UsableItem)inventoryList[indexInventory]).getItemEffect().split(":")[0].equalsIgnoreCase("buff"))
                        {
                            currentPlayer.buff(((UsableItem)inventoryList[indexInventory]).getItemEffect().split(":")[1].split(";"));
                            currentPlayer.buff(100000, (System.currentTimeMillis()), inventoryList[indexInventory], textArea);
                        }
                        if(((UsableItem)inventoryList[indexInventory]).isConsumable())
                        {
                            removeItem(indexInventory);
                            break;
                        }
                    }
                    else
                    effect = String.format("%s is currently on cooldown %d seconds remaining",
                            inventoryList[indexInventory].getItemName(),(-1)*(System.currentTimeMillis() - ((UsableItem)inventoryList[indexInventory]).getLastUsed() - ((UsableItem)inventoryList[indexInventory]).cooldownTimer())/1000);
                }
                else if(inventoryList[indexInventory] instanceof StackableConsumableItem)
                {
                    if(System.currentTimeMillis() - ((StackableConsumableItem)inventoryList[indexInventory]).getLastUsed() > ((StackableConsumableItem)inventoryList[indexInventory]).cooldownTimer())
                    {
                        ((StackableConsumableItem)inventoryList[indexInventory]).setLastUsed(System.currentTimeMillis());
                        effect = ((StackableConsumableItem)inventoryList[indexInventory]).useEffect();
                        if(((StackableConsumableItem)inventoryList[indexInventory]).getItemEffect().split(":")[0].equalsIgnoreCase("heal"))
                        {
                            currentPlayer.heal(Integer.parseInt((((StackableConsumableItem)inventoryList[indexInventory]).getItemEffect().split(":")[1])));
                        }
                        else if(((StackableConsumableItem)inventoryList[indexInventory]).getItemEffect().split(":")[0].equalsIgnoreCase("buff"))
                        {
                            currentPlayer.buff(((StackableConsumableItem)inventoryList[indexInventory]).getItemEffect().split(":")[1].split(";"));
                            currentPlayer.buff(100000, (System.currentTimeMillis()), inventoryList[indexInventory], textArea);
                        }
                        removeItem(indexInventory);
                        break;
                    }
                    else
                        effect = String.format("%s is currently on cooldown %d seconds remaining",
                            inventoryList[indexInventory].getItemName(), (-1)*(System.currentTimeMillis() - ((StackableConsumableItem)inventoryList[indexInventory]).getLastUsed() - ((StackableConsumableItem)inventoryList[indexInventory]).cooldownTimer())/1000);
                }
            }
        return effect;    
    }
    
    public void checkEquipedItems(Player currentPlayer, TextArea textArea)
    {
        currentPlayer.checkEquipItems(textArea);
    }
    
    //Outputs the information of an item.
    public String equipItemByName(String item, Player currentPlayer)
    {
        for(int indexInventory = 0; indexInventory < inventoryList.length; indexInventory++)
            if(inventoryList[indexInventory].getItemName().equalsIgnoreCase(item) && inventoryList[indexInventory] instanceof EquipableItem)
            {
                if(((EquipableItem)(inventoryList[indexInventory])).canEquip(currentPlayer.getStats()))
                {
                    if(!((EquipableItem)(inventoryList[indexInventory])).isEquip())
                    {
                        if(currentPlayer.equipItem(((EquipableItem)(inventoryList[indexInventory]))))
                        {
                            ((EquipableItem)(inventoryList[indexInventory])).equip();
                            currentPlayer.addToStats(((EquipableItem)(inventoryList[indexInventory])).getItemStats());
                            return String.format("%s is now equip\n", inventoryList[indexInventory].getItemName());
                        }
                        else
                            return String.format("There is currently an item in that equip slot\n");
                    }
                    else
                    {
                        return String.format("%s is already equipped\n", inventoryList[indexInventory].getItemName());
                    }
                }
                else
                {
                    return String.format("You can not equip this item\n");
                }
            }
        return "No Equipable Item With That Name Found\n";
    }
    
    public String unequipItemByName(String item, Player currentPlayer)
    {
        for(int indexInventory = 0; indexInventory < inventoryList.length; indexInventory++)
            if(inventoryList[indexInventory].getItemName().equalsIgnoreCase(item) && inventoryList[indexInventory] instanceof EquipableItem)
            {
                    if(((EquipableItem)(inventoryList[indexInventory])).isEquip())
                    {
                        currentPlayer.unequipItem((((EquipableItem)(inventoryList[indexInventory]))));
                        ((EquipableItem)(inventoryList[indexInventory])).equip();
                        currentPlayer.removeFromStats(((EquipableItem)(inventoryList[indexInventory])).getItemStats());
                        return String.format("%s is now unequip\n", inventoryList[indexInventory].getItemName());     
                    }
                    else
                    {
                        return String.format("%s was never equip\n", inventoryList[indexInventory].getItemName());
                    }
            }
        return "No Equipable Item With That Name Found\n";
    }
    
    
    
    //Outputs the information of an item.
    public String viewItem(int item)
    {
        return inventoryList[item].toString();
    }
    
    public String viewItemByName(String item)
    {
        for(int indexInventory = 0; indexInventory < inventoryList.length; indexInventory++)
            if(inventoryList[indexInventory].getItemName().equalsIgnoreCase(item))
                return inventoryList[indexInventory].toString();
        return "No Item With That Name Found\n";
    }
    
    public int getItemIDByName(String item)
    {
        for(int indexInventory = 0; indexInventory < inventoryList.length; indexInventory++)
            if(inventoryList[indexInventory].getItemName().equalsIgnoreCase(item))
                return inventoryList[indexInventory].getItemId();
        return 0;
    }
    
    //Outputs the current inventory with item information for each item based on the item type.
    public String list()
    {
            String outputString = "";
            for(int indexInventory = 0; indexInventory < inventoryList.length; indexInventory++)
            {
                outputString += "\tItem Slot " + (indexInventory + 1) + " " + inventoryList[indexInventory].toString();
                if(inventoryList[indexInventory] instanceof EquipableItem)
                    if(((EquipableItem)(inventoryList[indexInventory])).isEquip())
                        outputString += " Equipped";
                outputString += "\n";
            }
            return outputString;
    }
}
