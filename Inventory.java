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
        if(inventoryList[item] instanceof Stackable)
            removeStackingItem(item);
        else
            removeNonstackingItem(item);
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
    public String useItem(int item)
    {
        String effect = "This Item has no use/consume effect.\n";
        if(inventoryList[item] instanceof Usable)
        {
            effect = ((Usable)inventoryList[item]).useEffect();
            if(inventoryList[item] instanceof Consumable)
                removeItem(item);
        }
        return effect;    
    }
    
    //Outputs the information of an item.
    public String viewItem(int item)
    {
        return inventoryList[item].toString();
    }
    
    //Outputs the current inventory with item information for each item based on the item type.
    public String list()
    {
        String outputString = String.format("Inventory List\n\t");
        for(int indexInventory = 0; indexInventory < inventoryList.length; indexInventory++)
        {
            outputString += "Item Slot " + (indexInventory + 1) + " " + inventoryList[indexInventory].toString();
            if(inventoryList[indexInventory] instanceof StackableItem)
                outputString += " " + ((StackableItem)(inventoryList[indexInventory])).stackAmount() + " ";
            if(inventoryList[indexInventory] instanceof EquipableItem)
                if(((EquipableItem)(inventoryList[indexInventory])).isEquip())
                    outputString += " Equipped";
            outputString += "\n\t";
        }
        return outputString;
    }
}
