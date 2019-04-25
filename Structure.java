public class Structure 
{
    private int[] gridsCovered;
    private int[] doorGrid;
    private int[] enterFrom;
    private final int structureID;
    private final String name;
    private final String detail;
    private final boolean rideable;
    private final boolean pushable;
    
    public Structure(int[] gridsCovered, int[] doorGrid, int[] enterFrom, int structureID,
            String name, String detail, boolean rideable, boolean pushable)
    {
        this.gridsCovered = gridsCovered;
        this.doorGrid = doorGrid;
        this.enterFrom = enterFrom;
        this.structureID = structureID;
        this.name = name;
        this.detail = detail;
        this.rideable = rideable;
        this.pushable = pushable;
    }

    public void setGridsCovered(int[] gridsCovered) 
    {
        this.gridsCovered = gridsCovered;
    }

    public void setDoorGrid(int[] doorGrid) 
    {
        this.doorGrid = doorGrid;
    }

    public void setEnterFrom(int[] enterFrom) 
    {
        this.enterFrom = enterFrom;
    }

    public int[] getGridsCovered() 
    {
        return gridsCovered;
    }

    public int[] getDoorGrid() 
    {
        return doorGrid;
    }

    public int[] getEnterFrom() 
    {
        return enterFrom;
    }

    public int getStructureID() 
    {
        return structureID;
    }

    public String getName() 
    {
        return name;
    }

    public String getDetail() 
    {
        return detail;
    }

    public boolean isRideable() 
    {
        return rideable;
    }

    public boolean isPushable() 
    {
        return pushable;
    }
}
