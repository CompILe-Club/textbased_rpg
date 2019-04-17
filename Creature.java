import java.util.Map;

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
    private final Map dropTable;
    private final int respawn;
    private long death;
    private boolean isdead;
    private final int maxHealth;
    
    public Creature(String name, int creatureID, int level, String details, int attack, int[] items,
            int maxItems, int minItems, int xp, int health, int gridSize, Map dropTable,
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

    public void setHealth(int health, Player currentPlayer, Grid where) 
    {
        
        if(health == 0)
        {
            this.health = 0;
            this.isdead = true;
            this.death = System.currentTimeMillis();
            System.out.printf("%s is dead.\n", this.name);
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
                    
                    if(currentPlayer.getPlayerLocation() == currentGrid.getGridID())
                        System.out.printf("%s has respawn\n", name);
                    current.setHealth(current.getMaxHealth(), currentPlayer, currentGrid);
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

    public Map getDropTable() 
    {
        return dropTable;
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
