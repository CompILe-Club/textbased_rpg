import java.util.Map;

public class World 
{
    private final Map gameObjects;
    private final Map gameItems;
    private final Map gameCreatures;
    private final Map gameQuest;
    private final Map gameNPCs;
    private final Map gameGrids;
    
    public World(Map gameObjects, Map gameItems, Map gameCreatures,
            Map gameQuest, Map gameNPCs, Map gameGrids)
    {
        this.gameObjects = gameObjects;
        this.gameItems = gameItems;
        this.gameCreatures = gameCreatures;
        this.gameQuest = gameQuest;
        this.gameNPCs = gameNPCs;
        this.gameGrids = gameGrids;
    }

    public Map getGameObjects() {
        return gameObjects;
    }

    public Map getGameItems() {
        return gameItems;
    }

    public Map getGameCreatures() {
        return gameCreatures;
    }

    public Map getGameQuest() {
        return gameQuest;
    }

    public Map getGameNPCs() {
        return gameNPCs;
    }

    public Map getGameGrids() {
        return gameGrids;
    }

    @Override
    public String toString()
    {
        return String.format("Game Objects:\n\t%s\nGame Items:\n\t%s\nGame Creatures:\n\t%s\n"
                + "Game Quest:\n\t%s\nGame NPCs:\n\t%s\nGame Grids:\n\t%s\nGame Regions:\n\t%s\n",
                gameObjects.toString(), gameItems.toString(),gameCreatures.toString(), 
                gameQuest.toString(), gameNPCs.toString(), gameGrids.toString());
    }
}
