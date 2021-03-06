public class Grid 
{
    private Grid north = null;
    private Grid south;
    private Grid east;
    private Grid west;
    private Grid northEast;
    private Grid northWest;
    private Grid southEast;
    private Grid southWest;
    private String transitFromNorth;
    private String transitFromSouth;
    private String transitFromEast;
    private String transitFromWest;
    private String transitFromNorthEast;
    private String transitFromNorthWest;
    private String transitFromSouthEast;
    private String transitFromSouthWest;
    private GameObjects [] objects;
    private Creature [] creatures;
    private NPC [] NPCs;
    private final Structure [] buildings;
    private final String details;
    private final int gridID;
    private final String locationName;
    private Item[] droppedItems;
    
    Grid(GameObjects [] objects, Creature [] creatures, NPC [] people, Structure [] buildings,
            String details, int gridID, String locationName)
    {
        this.north = null;
        this.south = null;
        this.east = null;
        this.west = null;
        this.northEast = null;
        this.northWest = null;
        this.southEast = null;
        this.southWest = null;
        this.transitFromNorth = "";
        this.transitFromSouth = "";
        this.transitFromEast = "";
        this.transitFromWest = "";
        this.transitFromNorthEast = "";
        this.transitFromNorthWest = "";
        this.transitFromSouthEast = "";
        this.transitFromSouthWest = "";
        this.objects = objects;
        this.creatures = creatures;
        this.NPCs = people;
        this.buildings = buildings;
        this.details= details;
        this.gridID = gridID;
        this.locationName = locationName;
        this.droppedItems = new Item[0];
    }

    public Item[] getDrops()
    {
        return this.droppedItems;
    }
    
    public void addItem(Item droppedItem)
    {
        Item tempItems[] = new Item[this.droppedItems.length + 1];
        for(int indexItems = 0; indexItems < this.droppedItems.length; indexItems++)
            tempItems[indexItems] = this.droppedItems[indexItems];
        tempItems[this.droppedItems.length] = droppedItem;
        this.droppedItems = tempItems;
    }
    
    public void removeItem(String itemName)
    {
        if(this.droppedItems.length != 0)
        {
            Item tempItems[] = new Item[this.droppedItems.length - 1];
            for(int indexItems = 0; indexItems < this.droppedItems.length; indexItems++)
            {
                if(droppedItems[indexItems].getItemName().equalsIgnoreCase(itemName))
                {
                     for(int indexItems2 = indexItems + 1; indexItems2 < this.droppedItems.length; indexItems2++)
                     {
                         tempItems[indexItems2 - 1] = this.droppedItems[indexItems2];
                     }
                     this.droppedItems = tempItems;
                     break;
                }
                else if(indexItems != this.droppedItems.length - 1)
                    tempItems[indexItems] = this.droppedItems[indexItems];
            }
        }
    }
    
    public Item pickUpItem(String itemName)
    {
            for(int indexItems = 0; indexItems < this.droppedItems.length; indexItems++)
            {
                if(droppedItems[indexItems].getItemName().equalsIgnoreCase(itemName))
                     return this.droppedItems[indexItems];
            }
        return null;
    }
    
    public Grid getNorth() {
        return north;
    }

    public Grid getSouth() {
        return south;
    }

    public Grid getEast() {
        return east;
    }

    public Grid getWest() {
        return west;
    }

    public Grid getNorthEast() {
        return northEast;
    }

    public Grid getNorthWest() {
        return northWest;
    }

    public Grid getSouthEast() {
        return southEast;
    }

    public Grid getSouthWest() {
        return southWest;
    }

    public String getTransitFromNorth() {
        return transitFromNorth;
    }

    public String getTransitFromSouth() {
        return transitFromSouth;
    }

    public String getTransitFromEast() {
        return transitFromEast;
    }

    public String getTransitFromWest() {
        return transitFromWest;
    }

    public String getTransitFromNorthEast() {
        return transitFromNorthEast;
    }

    public String getTransitFromNorthWest() {
        return transitFromNorthWest;
    }

    public String getTransitFromSouthEast() {
        return transitFromSouthEast;
    }

    public String getTransitFromSouthWest() {
        return transitFromSouthWest;
    }

    public GameObjects [] getObjects() {
        return objects;
    }

    public Creature [] getCreatures() {
        return creatures;
    }

    public NPC [] getPeople() {
        return NPCs;
    }

    public Structure [] getBuildings() {
        return buildings;
    }

    public String getDetails() {
        return details;
    }

    public int getGridID() {
        return gridID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setNorth(Grid north) {
        this.north = north;
    }

    public void setSouth(Grid south) {
        this.south = south;
    }

    public void setEast(Grid east) {
        this.east = east;
    }

    public void setWest(Grid west) {
        this.west = west;
    }

    public void setNorthEast(Grid northEast) {
        this.northEast = northEast;
    }

    public void setNorthWest(Grid northWest) {
        this.northWest = northWest;
    }

    public void setSouthEast(Grid southEast) {
        this.southEast = southEast;
    }

    public void setSouthWest(Grid southWest) {
        this.southWest = southWest;
    }

    public void setTransitFromNorth(String transitFromNorth) {
        this.transitFromNorth = transitFromNorth;
    }

    public void setTransitFromSouth(String transitFromSouth) {
        this.transitFromSouth = transitFromSouth;
    }

    public void setTransitFromEast(String transitFromEast) {
        this.transitFromEast = transitFromEast;
    }

    public void setTransitFromWest(String transitFromWest) {
        this.transitFromWest = transitFromWest;
    }

    public void setTransitFromNorthEast(String transitFromNorthEast) {
        this.transitFromNorthEast = transitFromNorthEast;
    }

    public void setTransitFromNorthWest(String transitFromNorthWest) {
        this.transitFromNorthWest = transitFromNorthWest;
    }

    public void setTransitFromSouthEast(String transitFromSouthEast) {
        this.transitFromSouthEast = transitFromSouthEast;
    }

    public void setTransitFromSouthWest(String transitFromSouthWest) {
        this.transitFromSouthWest = transitFromSouthWest;
    }
    
    public String searchArea()
    {
        String outputString = "";
        if((this.creatures[0] != null) && !(this.creatures[0].isIsdead()))
            outputString += String.format("Creature(s):\n");
        for(int indexCreatures = 0; indexCreatures < this.creatures.length; indexCreatures++)
        {
            if((this.creatures[indexCreatures] != null) && !(this.creatures[indexCreatures].isIsdead()))
                outputString += String.format("\t%s\n", this.creatures[indexCreatures].getName());
        }
        if((this.buildings[0] != null))
            outputString += String.format("\nStructure(s):\n");
        for(int indexBuildings = 0; indexBuildings < this.buildings.length; indexBuildings++)
        {
            if((this.buildings[indexBuildings] != null))
                outputString += String.format("\t%s\n", this.buildings[indexBuildings]);
        }
        if((this.objects[0] != null))
         outputString += String.format("\nItem(s):\n");
        for(int indexObjects = 0; indexObjects < this.objects.length; indexObjects++)
        {
            if((this.objects[indexObjects] != null))
                outputString += String.format("\t%s\n", this.objects[indexObjects]);
        }
        if((this.NPCs[0] != null))
            outputString += String.format("\nPeople:\n");
        for(int indexNPCs = 0; indexNPCs < this.NPCs.length; indexNPCs++)
        {
            if((this.NPCs[indexNPCs] != null))
                outputString += String.format("\t%s\n", this.NPCs[indexNPCs].getName());
        }
        if(outputString.equalsIgnoreCase(""))
            outputString = String.format("There is currently nothing here.\n");
        return outputString;
    }
    
    @Override
    public String toString()
    {
        return String.format("%s\n\n%s\n\n", this.locationName, this.details);
    }
}
