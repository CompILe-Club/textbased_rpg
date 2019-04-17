public class NPC implements Lifeforms{
    
    private final int[] stats;
    private boolean essential;
    private final String fName;
    private final String lName;
    private final String description;
    private final String[] useableAction;
    private final int NPCID;
    private Inventory inventory;
    private final boolean shopKeeper;
    private final int[] linkedQuest;
    private final int location;
    private final int[] equipment;
    private final String dialogue;
    private final String[] questDialogue; 
    private int health;
    private int mana;
    private int level;
    
    public NPC(String fName, String lName, String description, int[] stats, String[] useableAction, Inventory inventory,
    boolean shopKeeper, int[] linkedQuest, String[] questDialogue, int location, int[] equipment, String dialogue, int health, int mana,
    int level, int NPCID)
         {
             this.fName = fName;
             this.lName = lName;
             this.description = description;
             this.stats = stats;
             this.useableAction = useableAction;
             this.inventory = inventory;
             this.shopKeeper = shopKeeper;
             this.linkedQuest = linkedQuest;
             this.location = location;
             this.equipment = equipment;
             this.dialogue = dialogue;
             this.health = health;
             this.mana = mana;
             this.level = level;
             this.NPCID = NPCID;
             this.questDialogue = questDialogue;
             //Change useableAction.length == ? to equal the same amount of commands as a trash NPC will have
            if(linkedQuest.length == 0 && shopKeeper == false && useableAction.length <= 2)     
                this.essential = false;
            else
                this.essential = true;
         }
    
    
    @Override
    public int accuracy(Lifeforms target)
    {
        int result = (int)(Math.random()* 101);
        if(result >= ((target.getStats()[1]/5 - this.stats[1]/5) + 30 + (target.getLevel() - this.level)))
           return result;
        else
            return 0;
    }
        
    @Override
    public int attack(Lifeforms target, World currentWorld)
    { 
       int critChance = accuracy(target);
       if(!(target.getHealth() <= 0) && this.health > 0 && !(critChance == 0))
       {    if(critChance  > (98 - this.stats[8]/5))
                return (int)(((EquipableItem)(currentWorld.getGameItems().get(equipment[4]))).getDamage(stats[2]) * 1.5);
            return ((EquipableItem)(currentWorld.getGameItems().get(equipment[4]))).getDamage(stats[2]);
       }
       return 0; 
    }
    
    public String[] getQuestDialogue()
    {
        return this.questDialogue;
    }
    
    public int getNPCID()
    {
        return this.NPCID;
    }
    
    @Override
    public int[] getStats() {
        return stats;
    }

    public boolean isEssential() {
        return essential;
    }
    
    @Override
    public String getName() {
        String fullName = fName + " " + lName;
        return fullName;
    }

    public String getDescription() {
        return description;
    }

    public String[] getUseableAction() {
        return useableAction;
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public boolean isShopKeeper() {
        return shopKeeper;
    }

    public int[] getLinkedQuest() {
        return linkedQuest;
    }
    
    public boolean hasItemInShop(String Item)
    {
        if(!(this.inventory.viewItemByName(Item).equalsIgnoreCase("No Item With That Name Found\n")))
            return true;
        else
            return false;
    }
    
    public int buyItemFromShop(String Item)
    {
        inventory.removeItemByName(Item);
        return inventory.getItemIDByName(Item);
    }
    
    @Override
    public int getLocation() {
        return location;
    }

    public int[] getEquipment() {
        return equipment;
    }

    public String getDialogue() {
        return dialogue;
    }
    
    @Override
    public int getHealth() {
        return health;
    }
    
    @Override
    public int getMana() {
        return mana;
    }
    
    @Override
    public int getLevel() {
        return level;
    }
    
}
