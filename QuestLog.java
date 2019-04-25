import javafx.scene.control.TextArea;

public class QuestLog 
{
    private Quest activeQuest[];
    private Quest completeQuest[];
    private Quest trackedQuest[];
    QuestLog()
    {
        activeQuest = new Quest[0];
        completeQuest = new Quest[0];
        trackedQuest = new Quest[5];
    }
    
    public int numberOfActiveQuest()
    {
        return activeQuest.length;
    }
    
    public int numberOfCompleteQuest()
    {
        return completeQuest.length;
    }
    
    public void startQuest(Quest newQuest)
    {
        Quest tempQuests[] = new Quest[activeQuest.length + 1];
        for(int questIndex = 0; questIndex < activeQuest.length; questIndex++)
            tempQuests[questIndex] = activeQuest[questIndex];
        tempQuests[activeQuest.length] = activeQuest[activeQuest.length];
    }
    
    public void endQuest(Quest quest)
    {
        Quest tempActiveQuests[] = new Quest[activeQuest.length - 1];
        Quest tempCompleteQuests[] = new Quest[completeQuest.length + 1];
        for(int questIndex = 0; questIndex < activeQuest.length; questIndex++)
        {
            if(activeQuest[questIndex].getQuestID() == quest.getQuestID())
                for(int quest2Index = 0; quest2Index < completeQuest.length; quest2Index++)
                {
                    tempCompleteQuests[quest2Index] = completeQuest[quest2Index];
                }
            else
                tempActiveQuests[questIndex] = activeQuest[questIndex];
        }
        completeQuest = tempCompleteQuests; 
        activeQuest = tempActiveQuests;     
    }
    
    public Quest[] getActiveQuest()
    {
        return activeQuest;
    }
    
    public String getCurrentQuests()
    {
        String outputString = "Current Active Quest:\n\t";
        for(int indexQuest = 0; indexQuest < activeQuest.length; indexQuest++)
            outputString += String.format("[%d] %s\n\t", indexQuest + 1, activeQuest[indexQuest].getName());
        outputString += "\n";
        return outputString;
    }
    
    public String getCompletedQuests()
    {
        String outputString = "Completed Quest:\n\t";
        for(int indexQuest = 0; indexQuest < completeQuest.length; indexQuest++)
            outputString += String.format("[%d] %s\n\t", indexQuest + 1, completeQuest[indexQuest].getName());
        outputString += "\n";
        return outputString;
    }
    
    public void checkCurrentQuest(String event, World currentWorld, TextArea textArea)
    {
        for(int indexQuest = 0; indexQuest < activeQuest.length; indexQuest++)
            activeQuest[indexQuest].completeObjective(event, currentWorld, textArea);
    }
    
    public void trackQuest(String questName, int trackLocation)
    {
        for(int indexQuest = 0; indexQuest < activeQuest.length; indexQuest++)
            if(activeQuest[indexQuest].getName().equalsIgnoreCase(questName))
                trackedQuest[trackLocation] = activeQuest[indexQuest];
    }
    
    public String getTrackedQuest()
    {
        String outputString = "Tracked Quest:\n\t";
        for(int indexQuest = 0; indexQuest < trackedQuest.length; indexQuest++)
            if(trackedQuest[indexQuest] != null)
                outputString += String.format("[%d] %s\n\t", indexQuest + 1, trackedQuest[indexQuest].getName());
        outputString += "\n";
        return outputString;
    }
    
    public String getQuestObjective(String questName, World currentWorld)
    {
        for(int indexQuest = 0; indexQuest < activeQuest.length; indexQuest++)
            if(activeQuest[indexQuest].getName().equalsIgnoreCase(questName))
            {
                return String.format("%s:\n\tCurrent Objective: %s", activeQuest[indexQuest].getName(),
                        activeQuest[indexQuest].printObjective(currentWorld));
            }
        return "Quest does not exist\n";
    }
    
    public String getQuestDetails(String questName, World currentWorld)
    {
        for(int indexQuest = 0; indexQuest < activeQuest.length; indexQuest++)
            if(activeQuest[indexQuest].getName().equalsIgnoreCase(questName))
            {
                return activeQuest[indexQuest].getDetails() + "\nCurrent Objective: " + activeQuest[indexQuest].printObjective(currentWorld);
            }
        return "Quest does not exist\n";
    }
    
}
