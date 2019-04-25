import javafx.scene.control.TextArea;

public class Creature 
{
    private final String name;
    private final int level;
    private final String details;
    private final int creatureID;
    private final int attack;
    private int[] items;
    private final int maxItems;
    private final int minItems;
    private final int xp;
    private int health;
    private final int gridSize;
    private final int[] dropTable;
    private final int[] dropTableChance;
    private final int respawn;
    private long death;
    private boolean isdead;
    private final int maxHealth;
    
    public Creature(String name, int creatureID, int level, String details, int attack, int[] items,
            int maxItems, int minItems, int xp, int health, int gridSize, int[] dropTable, int[] dropTableChance,
            int respawn, boolean isdead, long death)
    {
        this.name = name;
        this.creatureID = creatureID;
        this.level = level;
        this.details = details;
        this.attack = attack;
        this.items = items;
        this.maxItems = maxItems;
        this.minItems = minItems;
        this.xp = xp;
        this.maxHealth = health;
        this.health = health;
        this.gridSize = gridSize;
        this.dropTable = dropTable;
        this.respawn = respawn;
        this.death = death;
        this.isdead = isdead;
        this.dropTableChance = dropTableChance;
    }

    public int getMaxHealth()
    {
        return this.maxHealth;
    }
    
    public void setDeath(int death) 
    {
        this.death = death;
    }

    public void setIsdead(boolean isdead) 
    {
        this.isdead = isdead;
    }
    
    public void setItems(int[] items) 
    {
        this.items = items;
    }

    public void setHealth(int health, Player currentPlayer, Grid where, World currentWorld, TextArea textArea) 
    {
        
        if(health <= 0)
        {
            this.health = 0;
            this.isdead = true;
            this.death = System.currentTimeMillis();
            textArea.appendText(String.format("Creature Health %d/%d\n", 0, this.maxHealth));
            textArea.appendText(String.format("%s is dead.\n", this.name));
            textArea.appendText(String.format("You gained %d xp from killing a(n) %s\n",this.xp, this.name));
            for(int indexDrops = 0; indexDrops < ((int)(Math.random()*((maxItems - minItems) + 1) + minItems)); indexDrops++)
            {
                int itemNumber = ((int)(Math.random()*(((dropTable.length - 1) - 0) + 1) + 0));
                int itemChance = ((int)(Math.random()*100));
                if(dropTableChance[itemNumber] < itemChance)
                {
                    Item drop = ((Item)(currentWorld.getGameItems().get(itemNumber)));
                    if(!(drop instanceof StackableItem))
                        where.addItem(drop);
                    else
                    {
                        Item newItem;
                        if(drop instanceof StackableConsumableItem)
                        {
                            StackableConsumableItem currentItem = ((StackableConsumableItem) (drop));
                            newItem = new StackableConsumableItem(currentItem.getItemName(), currentItem.getValue()
                                    , currentItem.getItemId(), currentItem.getDetails(), currentItem.maxStackAmount(), 
                                    1,currentItem.getItemEffect(), currentItem.cooldownTimer(),currentItem.getRequirements()); 
                        }
                        else
                        {
                            StackableNonconsumableItem currentItem = ((StackableNonconsumableItem)(drop));
                            newItem = new StackableNonconsumableItem(currentItem.getItemName(),currentItem.getValue(), 
                                    currentItem.getItemId(), currentItem.getDetails(), currentItem.maxStackAmount(), 1);
                        }   
                        where.addItem(newItem);
                    }
                    
                     textArea.appendText(String.format("Item Dropped %s\n", ((Item)(currentWorld.getGameItems().get(itemNumber))).getItemName()));
                }
                
            }
            Creature current = this;
            Player you = currentPlayer;
            Grid currentGrid = where;
            Thread deathtimer = new Thread()
            {
                
                @Override
                public void run() 
                {
                    while(System.currentTimeMillis() - death < respawn)
                    {
                        
                    }
                    current.setHealth(current.getMaxHealth(), currentPlayer, currentGrid, currentWorld, textArea);
                    current.setIsdead(false);
                }
            };
            try
            {
                deathtimer.start();
            }catch(Exception e)
            {
                System.out.println(e);
                while(true)
                {

                }
            }
        }
        else
            this.health = health;
    }

    public String getName() 
    {
        return name;
    }

    public int getLevel() 
    {
        return level;
    }

    public String getDetails() 
    {
        return details;
    }

    public int getAttack() 
    {
        return attack;
    }

    public int[] getItems() 
    {
        return items;
    }

    public int getMaxItems() 
    {
        return maxItems;
    }

    public int getMinItems() 
    {
        return minItems;
    }

    public int getXp() 
    {
        return xp;
    }

    public int getHealth() 
    {
        return health;
    }

    public int getGridSize() 
    {
        return gridSize;
    }

    public int[] getDrops() 
    {
        return dropTable;
    }

    public int[] getDropsChance() 
    {
        return dropTableChance;
    }
    
    public int getRespawn() 
    {
        return respawn;
    }

    public long getDeath() 
    {
        return death;
    }

    public boolean isIsdead() 
    {
        return isdead;
    }

    public int getCreatureID() 
    {
        return creatureID;
    }
    
}
