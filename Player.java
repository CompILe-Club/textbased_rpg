//Created By: Daniel August Johnston
//Author Title: CompILe President (Since Spring 2019)
//Date of Completion: 14 February 2019
//Parent Project: Text Based RPG
//Company or Organization: CompILe @ Illinois Central College East Peoria, IL
//Purpose: This class defines what a player is within the overall project of the Text Based RPG.

public class Player 
{
    //Holds the player's name which can not be changed after the instance of a player has been made.
    private final String playerName;
    //Holds current level of a player.
    private int playerLevel;
    //Holds the current player experience of a player.
    private int playerExperience;
    //Holds the current skill points unused by a player.
    private int usableSkillPoints;
    //Holds the current skill points being used by a player.
    private int usedSkillPoints;
    //Holds the required experience for each level based on the index of the experience needed
    //i.e. Level 1 requires 100 experience, Level 2 requires 2550 experience
    //This can not be changed after the instance of a player has been made.
    private final int[] LEVELS = {0,100,2550,8574,10000,255000,857400,1000000,5500000,85740000,100000000};
    //Holds the current player's stats first initialized using the basic stats of the player's chosen class.
    private int [] playerStats;
    private int [] playerBuffs;
    //Holds the current player's equipment first initialized using the basic equipment of the player's chosen class.
    private EquipableItem [] playerEquipment;
    //Holds the current player's inventory first initialized using the basic inventory of the player's chosen class.
    private final Inventory playerInventory;
    //Current Location of the player on the map
    private int playerLocation;
    private int maxPlayerHealth;
    private int respawnLocation;
    private int playerHealth;
    private int target;
    private QuestLog questLog;
    
    //Initializing the player based on their chosen name, class from within the main.
    Player(String playerName, Inventory inventory, int playerLevel, int usedSkillPoints, int usableSkillPoints,
            int [] playerStats, int playerExperience, EquipableItem [] playerEquipment, 
            int playerLocation, int playerHealth, int target)
    {
        this.usedSkillPoints = usedSkillPoints;
        this.playerLevel = playerLevel;
        this.usableSkillPoints = this.playerLevel - usedSkillPoints;
        this.playerExperience = playerExperience;
        this.playerName = playerName;
        this.playerStats = playerStats;
        this.playerEquipment = playerEquipment;
        this.playerInventory = inventory;
        this.questLog = new QuestLog();
        this.respawnLocation = 0;
        this.playerLocation = playerLocation;
        this.maxPlayerHealth = this.playerHealth = playerHealth;
        this.target = target;
        this.playerBuffs = new int[4];
    }
    
    public void heal(int healAmount)
    {
        if(this.playerHealth != this.maxPlayerHealth)
        {
            if(this.playerHealth + healAmount > this.maxPlayerHealth)
                this.playerHealth = this.maxPlayerHealth;
            else
                this.playerHealth += healAmount;
        }
    }
    
    public int getRespawnLocation()
    {
        return this.respawnLocation;
    }
    
    public void takeDamage(int damage)
    {
        if(this.playerHealth - damage <= 0)
        {
            System.out.println("Your Dead.");
            this.playerHealth = 0;
            Player current = this;
            long death = System.currentTimeMillis();
            Thread deathtimer = new Thread()
            {
                
                @Override
                public void run() 
                {
                    int oldTime = 0;
                    while(System.currentTimeMillis() - death < 10000)
                    {
                        if(oldTime != (int)((System.currentTimeMillis() - death)/1000))
                        {
                            oldTime = (int)((System.currentTimeMillis() - death)/1000);
                            System.out.printf("Respawn in %d second(s).\n", oldTime);
                        }
                    }
                    System.out.printf("Welcome back to the world of the living %s.\n", current.getPlayerName());
                    current.heal(current.getPlayerMaxHealth());
                    current.setPlayerLocation(current.getRespawnLocation());
                    
                }
            };
            try
            {
                deathtimer.start();
                deathtimer.join();
            }catch(Exception e)
            {
                System.out.println(e);
                while(true)
                {

                }
            }
        }
        else
            this.playerHealth -= damage;
    }
    
    public void buff(String [] buff)
    {
        for(int indexBuff = 0; indexBuff < playerBuffs.length; indexBuff++)
            this.playerBuffs[indexBuff] += Integer.parseInt(buff[indexBuff]);
    }
    
    public void deBuff(String [] deBuff)
    {
        for(int indexBuff = 0; indexBuff < playerBuffs.length; indexBuff++)
            this.playerBuffs[indexBuff] -= Integer.parseInt(deBuff[indexBuff]);
    }
    
    public void buff(int buffTime, long useTime, Item item)
    {
        Thread bufftimer = new Thread()
        {
            int buffTime2 = buffTime;
            long useTime2 = useTime;
            Item item2 = item;
            @Override
            public void run() 
            {
                int lastMessage = 0;
                while(System.currentTimeMillis() - useTime2 < buffTime2)
                {
                    if((((buffTime2)-(System.currentTimeMillis() - useTime2) == 10001) || (buffTime2)-(System.currentTimeMillis() - useTime2) == 5001) &&
                            (int)((buffTime2 - (System.currentTimeMillis() - useTime2))/1000) != lastMessage)
                    {
                        lastMessage = (int)((buffTime2 - (System.currentTimeMillis() - useTime2))/1000);
                        System.out.printf("%s buff running out in %d seconds\n", item2.getItemName(), 
                                lastMessage);
                    }
                }
                System.out.printf("%s buff has ran out.\n", item2.getItemName());
                if(item instanceof StackableConsumableItem)
                    deBuff(((StackableConsumableItem)item2).getItemEffect().split(":")[1].split(";"));
                else
                    deBuff(((UsableItem)item2).getItemEffect().split(":")[1].split(";"));
            }
        };
        try
        {
            bufftimer.start();
        }catch(Exception e)
        {
            System.out.println(e);
            while(true)
            {
                
            }
        }
        
    }
    
    public void setTarget(int targetID)
    {
        this.target = targetID;
    }
    
    public int getTarget()
    {
        return this.target;
    }
    
    public int getPlayerLocation()
    {
        return this.playerLocation;
    }
    
    public void setPlayerLocation(int gridID)
    {
        this.playerLocation = gridID;
    }
    
    //Returns a player's name.
    public String getPlayerName()
    {
        return playerName;
    }
    
    //Returns a player's current level.
    public int getPlayerLevel()
    {
        return playerLevel;
    }
    
    //Returns a player's current experience.
    public int getPlayerExperience()
    {
        return playerExperience;
    }
    
    //Returns a player's current usable skill points.
    public int getUsableSkillPoints()
    {
        return usableSkillPoints;
    }
    
    //Returns a player's current total of skill point both used and unused..
    public int getNumberOfSkillPoints()
    {
        return usableSkillPoints + usedSkillPoints;
    }
    
    public int getPlayerHealth()
    {
        return this.playerHealth;
    }
    
    public int getPlayerMaxHealth()
    {
        return this.maxPlayerHealth;
    }
    
    public QuestLog getQuestLog()
    {
        return this.questLog;
    }
    
    public void checkEquipItems()
    {
        for(int indexEquipment = 0; indexEquipment < this.playerEquipment.length; indexEquipment++)
        {
            if(!(playerEquipment[indexEquipment] == null))
                System.out.printf(playerEquipment[indexEquipment].toString());
        }
    }
    
    //Returns a player's current inventory
    public Inventory getInventory()
    {
        return playerInventory;
    }
    
    public EquipableItem [] getPlayerEquipment()
    {
        return this.playerEquipment;
    }
    
    //Resets the player's used skill points and resets the player's stats back to basic class stats.
    public void resetSkillPoints(int[] baseClassStats)
    {
        usableSkillPoints += usedSkillPoints;
        usedSkillPoints = 0;
        this.playerStats = baseClassStats;
    }
    
    public boolean hasItemInShop(String Item)
    {
        if(!(this.playerInventory.viewItemByName(Item).equalsIgnoreCase("No Item With That Name Found\n")))
            return true;
        else
            return false;
    }
    
    public int sellItemToShop(String Item)
    {
        playerInventory.removeItemByName(Item);
        return playerInventory.getItemIDByName(Item);
    }
    
    public String getStatus()
    {
        String outputString = String.format("Status Report:\n\tPlayer Level: %d\n\t"
                + "Player XP: %d\n\t"
                + "Player Health: %d/%d\n\tNumber of Active Quest: %d\n\tCurrent Buffs/Debuffs:\n\t\t"
                + "Strength:     %s\n\t\tDexterity:    %s\n\t\tConstitution: %s\n\t\tStamina:      %s\n\n", 
                this.playerLevel, this.playerExperience, this.playerHealth, this.maxPlayerHealth, this.questLog.numberOfActiveQuest(),
                this.playerBuffs[0], this.playerBuffs[1], this.playerBuffs[2], this.playerBuffs[3]);
        return outputString;
    }
    
    public String getStats()
    {
        String outputString = String.format("%d", playerLevel);
        for(int indexStats = 0; indexStats < playerStats.length; indexStats++)
            outputString += ";" + playerStats[indexStats];
        return outputString;
    }
    
    //Add loot into the player's inventory if the player's inventory is not full
    public boolean loot(Item lootedItems)
    {
        return playerInventory.addItem(lootedItems);
    }
    
    public boolean equipItem(EquipableItem equipItem)
    {
        if(playerEquipment[Integer.parseInt(equipItem.getSlotId())] == null)
        {
            playerEquipment[Integer.parseInt(equipItem.getSlotId())] = equipItem;
            return true;
        }
        else
            return false;
    }
    
    public boolean unequipItem(EquipableItem equipItem)
    {
        if( playerEquipment[Integer.parseInt(equipItem.getSlotId())].getSlotId().equalsIgnoreCase(equipItem.getSlotId()))
        {
            playerEquipment[Integer.parseInt(equipItem.getSlotId())] = null;
            return true;
        }
        else
            return false;
    }
    
    //**************************************************************************
    //*                                                                        *
    //*                            NOT COMPLETE YET                            *
    //*                    Need to complete external code                      *
    //*                                 first                                  *
    //*                                                                        *
    //**************************************************************************
    //Allows the player to use unused skill points
    public boolean useSkillPoint (String useSkillPointCommand)
    {
        if(usableSkillPoints != 0)
        {
            usableSkillPoints--;
            usedSkillPoints++;
            return true;
        }
        else
            return false;
    }
    
    //Adds to the player's experience points and checks to see if the player has
    //gained a level.
    public void gainExperience(int experience)
    {
        playerExperience += experience;
        if(LEVELS[playerLevel + 1] <= playerExperience)
        {
            playerLevel++;
            usableSkillPoints++;
            System.out.printf("You have gained a level.\n You are now level %d."
                    + "\nYou have %d skill points to spend.\n", playerLevel, usableSkillPoints);
        }
    }
