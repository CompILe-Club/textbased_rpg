public class GameObjects 
{
    private final int objectID;
    private final String name;
    private final boolean isMoveable;
    private final String details;
    private final boolean isUsable;
    private final String useEffect;
    
    public GameObjects(String name, int objectID, boolean isMoveable, String details,
            boolean isUsable, String useEffect)
    {
        this.objectID = objectID;
        this.name = name;
        this.isMoveable = isMoveable;
        this.details = details;
        this.isUsable = isUsable;
        this.useEffect = useEffect;
    }

    public int getObjectID() 
    {
        return objectID;
    }

    public String getName() 
    {
        return name;
    }

    public boolean isIsMoveable() 
    {
        return isMoveable;
    }

    public String getDetails() 
    {
        return details;
    }

    public boolean isIsUsable() 
    {
        return isUsable;
    }

    public String getUseEffect() 
    {
        return useEffect;
    }
}
