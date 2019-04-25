import javafx.scene.control.TextArea;

public class Quest {
    
    private final int questID;
    private final String name;
    //The list of objectives for the quest, collected from a text file.
    private final String[] objectives;
    //The details of the quest
    private final String details;
    //Whether or not the overall quest has been completed.
    private boolean completed;
    
    //The id of the target of the quest objective.
    //This can be a monster to be killed, a location to travel to etc.
    private int target;
    //Some objectives must be repeated. This counts how many times it must be done.
    private int targetNumber;
    //This counts how many have been done.
    private int counter;
    //This is the index of the active objective.
    private int currentObjective;    
    
    public Quest(String name, int questID, String[] objectives, String details){
        this.name = name;
        this.questID = questID;
        this.objectives = objectives;
        this.details = details;
        completed = false;
        currentObjective = 0;
    }
    
    public boolean getCompleted()
    {
       return  completed;
    }
    
    public String[] getObjectives()
    {
        return objectives;
    }
    
    public int getCurrentObjective()
    {
        return currentObjective;
    }

    public int getQuestID() {
        return questID;
    }

    public String getName() {
        return name;
    }
    
    //This checks if there is another objective, and if there is, increments it.
    public void nextObjective(TextArea textArea){
        textArea.appendText(String.format("Current objective complete for: %s\n", this.name));
        currentObjective++;
    }
    
    //Returns the details about the quest.
    public String getDetails(){
        return details;
    }
    
    //Return the objective description, if the objective is a fetch objective.
    public String fetchDesc(int id, World currentWorld){
        String item = ((Item)(currentWorld.getGameItems().get(id))).getItemName(); //This is a placeholder. When there is a lookup for it, this will get the name of the item with the ID of target.
        return "Collect " + item + ".\n";
    }
    
    //Same but for kill objectives.
    public String killDesc(int id, World currentWorld){
        String monster = ((Creature)(currentWorld.getGameCreatures().get(id))).getName(); //Also a placeholder for the same reasons as above.
        return "Kill " + counter + " " + monster + "s.\n";
    }
    
    //Same but for area objectives.
    public String areaDesc(int id, World currentWorld){
        String place = ((Grid)(currentWorld.getGameGrids().get(id))).getLocationName(); //Also a placeholder for the same reasons as above.
        return "Go to " + place + ".\n";
    }
    
    //Same but for talking objectives.
    public String talkDesc(int id, World currentWorld){
        String person = ((NPC)(currentWorld.getGameNPCs().get(id))).getName(); //Also a placeholder
        return "Talk to " + person + ".\n";
    }
    
    //This returns the description of the current objective based on what type it is.
    public String printObjective( World currentWorld){
        String desc;
        String [] details = objectives[currentObjective].split(":");
        switch (Integer.parseInt(details[0])) 
        {
            case 1:
                desc = fetchDesc(Integer.parseInt(details[1]), currentWorld);
                break;
            case 2:
                desc = killDesc(Integer.parseInt(details[1]), currentWorld);
                break;
            case 3:
                desc = areaDesc(Integer.parseInt(details[1]), currentWorld);
                break;
            default:
                desc = talkDesc(Integer.parseInt(details[1]), currentWorld);
                break;
        }
        
        return desc;
    }
    
    //This is triggered whenever something is done to advance the objective.
    public void completeObjective(String event, World currentWorld, TextArea textArea){
        boolean complete = false;
        String [] details = objectives[currentObjective].split(":");
        String [] currentEvent = event.split(":");
        if(Integer.parseInt(currentEvent[0]) == Integer.parseInt(details[0]))
            complete = (Integer.parseInt(currentEvent[1]) == Integer.parseInt(details[1]));
        //If the objective's completion condition has been completed, increment the counter.
        //If the counter has been completed, then the objective has been completed and the next objective is processed.
        if (complete){
            ++counter;
            if (counter == targetNumber){
                nextObjective(textArea);
            }
        }
    }
    
}
