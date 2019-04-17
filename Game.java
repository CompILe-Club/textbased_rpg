import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;

public class TextBasedRPG 
{
    private static final Scanner INPUT =  new Scanner(System.in);
    private static World WORLD;
    private static Player currentPlayer;
    private static String playerName;
    private static String LOADSAVE;

    public static void main(String[] args) 
    {
        Thread thread1 = new Thread()
        {
            @Override
            public void run()
            {
                boolean online = true;
                Map commands = new HashMap();
                //Done
                commands.put("STATUS", 0);
                //
                commands.put("QUEST", 1);
                //Done
                commands.put("HELP", 2);
                //Done
                commands.put("EXIT", 3);
                //Done
                commands.put("INVENTORY", 4);
                //
                commands.put("LOCK", 5);
                //
                commands.put("NPC", 6);
                //Done
                commands.put("MOVE", 7);
                //
                commands.put("ATTACK", 8);
                //Done
                commands.put("SEARCH", 9);
                //Done
                commands.put("SAVE", 10);
                
                while(online)
                {
                    String command = INPUT.next();
                    command += INPUT.nextLine();
                    String mainCommand = command.split(" ")[0].toUpperCase();
                    int commandIndex = 25;
                    if(commands.containsKey(mainCommand))
                        commandIndex = (int)(commands.get(mainCommand));
                    switch(commandIndex)
                    {
                        case 0:
                            System.out.printf(currentPlayer.getStatus());
                            break;
                        case 1:
                            if(command.split(" ").length > 1)
                                questCommands(command);
                            else
                                System.out.printf(new HELP_COMMAND("HELP QUEST", true).printOutput());
                            break;
                        case 2:
                            System.out.printf(new HELP_COMMAND(command, true).printOutput());
                            break;
                        case 3:
                                online = false;
                            break;
                        case 4:
                            if(command.split(" ").length > 1)
                                inventoryCommands(command);
                            else
                                System.out.printf(new HELP_COMMAND("HELP INVENTORY", true).printOutput());
                            break;
                        case 5:
                            if(command.split(" ").length > 1)
                                lockCommands(command);
                            else
                                System.out.printf(new HELP_COMMAND("HELP LOCK", true).printOutput());
                            break;
                        case 6:
                            if(command.split(" ").length > 1)
                                npcCommands(command);
                            else
                                System.out.printf(new HELP_COMMAND("HELP NPC", true).printOutput());
                            break;
                        case 7:
                            if(command.split(" ").length > 1)
                                moveCommands(command);
                            else
                                System.out.printf(new HELP_COMMAND("HELP MOVE", true).printOutput());
                            break;
                        case 8:
                                if(command.split(" ").length > 1)
                                    attack(command);
                                else
                                    System.out.printf(new HELP_COMMAND("HELP ATTACK", true).printOutput());
                            break;
                        case 9:
                                System.out.printf(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).searchArea());
                            break;
                        case 10:
                            try
                            {
                                savePlayer();
                                System.out.println("Current Game World Has Been Saved.");
                            }
                            catch(Exception e)
                            {
                                
                            }
                            break;
                        default:
                            System.out.printf(command.split(" ")[0] + " is not a valid command.\n%s", new HELP_COMMAND("Help", true).printOutput());
                    }
                }
            }
        };
        try
        {
            LOADSAVE = loadSave();
            loadWorld();
            loadPlayer();
            thread1.start();
            thread1.join();
        }catch(Exception e)
        {
            System.out.println(e);
            while(true)
            {
                
            }
        }
        finally
        {

        }
        
    }
    
    

    private static String loadSave() throws Exception
    {
        File file = new File("gameSaves");
        String[] directories = file.list();
        if(directories.length == 0)
        {
            return "gameSaves/" + createNewSave() + "/";
        }
        else
        {
            boolean goodSave;
            do
            {
                goodSave = false;
                System.out.printf("Current Saves:\n\t");
                for(int indexSaves = 0; indexSaves < directories.length; indexSaves++)
                    System.out.printf("[%d] %s\n\t", indexSaves + 1, directories[indexSaves]);
                System.out.printf("\nChoose A Save By Name or New: ");
                playerName = INPUT.next();
                for(int indexSaves = 0; indexSaves < directories.length; indexSaves++)
                    if(playerName.equalsIgnoreCase(directories[indexSaves]))
                        goodSave = true;
                if(playerName.equalsIgnoreCase("NEW"))
                {
                    goodSave = true;
                    playerName = createNewSave();
                }
            }while(!goodSave);
            
            return "gameSaves/" + playerName + "/";
        }
    }
    
    private static String createNewSave() throws Exception
    {
        boolean goodName;
        do
        {
            goodName = true;
            System.out.printf("New Character Name: ");
            playerName = INPUT.next();
            String sourceFilesNames [] = new File("gameFiles").list();
            File sourceFiles[] = new File[sourceFilesNames.length];
            for(int indexFiles = 0; indexFiles < sourceFiles.length; indexFiles++)
                sourceFiles[indexFiles] = new File("gameFiles/" + sourceFilesNames[indexFiles]);
            File dest = new File("gameSaves/" + playerName);
            if(!(dest.isDirectory()) && !(playerName.equalsIgnoreCase("NEW")))
            {
                dest.mkdir();
                copyFiles(sourceFiles, "gameSaves/" + playerName, sourceFilesNames);
                createPlayerFile();
            }
            else
            {
                goodName = false;
                System.out.printf("Current Save Already Exist or Key Word Was Input.\n");
            }
        }while(!goodName);
        return playerName;
    }
    
    private static void createPlayerFile()throws Exception
    {
        File newFile = new File("gameSaves/" + playerName + "/" + playerName + ".txt");
        newFile.createNewFile();
        
        PrintStream fileStream = new PrintStream(newFile);
        fileStream.println("1;2;3;4;5");
        fileStream.println("1");
        fileStream.println("0");
        fileStream.println("0");
        fileStream.println("10;10;10;10");
        fileStream.println("0");
        fileStream.println("-1;-1;-1;-1;-1;-1;-1;-1;-1;-1;-1;-1");
        fileStream.println("0");
        fileStream.println("100");
        fileStream.println("0");
        fileStream.flush();
        fileStream.close();
    }
    
    private static void copyFiles(File []files, String dest, String [] fileNames) throws Exception
    {
        try
        {
            for(int indexFiles = 0; indexFiles < files.length; indexFiles++)
            {
                File newFile = new File(dest + "/" + fileNames[indexFiles]);
                FileInputStream inChannel = new FileInputStream(files[indexFiles]);
                FileOutputStream outChannel = new FileOutputStream(newFile);
                newFile.createNewFile();
                byte[] buf = new byte[1024];
                int bytesRead;
                while ((bytesRead = inChannel.read(buf)) > 0)
                {
                    outChannel.write(buf, 0, bytesRead);
                }
                inChannel.close();
                outChannel.close();
            }
        }
        catch(IOException e)
        {
            
        }
    }
    
    private static void loadWorld() throws Exception
    {
            Map gameObjects = new HashMap();
            Map gameItems  = new HashMap();
            Map gameCreatures  = new HashMap();
            Map gameQuest  = new HashMap();
            Map gameNPCs  = new HashMap();
            Map gameStructures = new HashMap();
            Map gameGrids  = new HashMap();
            //***********************************************************************************************
            //Reads in items from the Items.txt file and adds them into the list of items in the world.
            File itemsFile = new File(LOADSAVE + "Items.txt"); 
            BufferedReader textBuffer = new BufferedReader(new FileReader(itemsFile)); 
            String currentLine = "";
            while (textBuffer.ready()) 
            {
                currentLine += textBuffer.readLine() + "-";
            }
            String [] lineBreakDown = currentLine.split("-");
            for(int itemIndex = 0; itemIndex < lineBreakDown.length; itemIndex++)
            {
                String [] itemBreakDown = lineBreakDown[itemIndex].split("#");
                Item newItem;
                switch(itemBreakDown [0].charAt(0))
                {
                    case'E':
                        newItem = new EquipableItem(itemBreakDown[1],  Integer.parseInt(itemBreakDown[2]), 
                                itemIndex, itemBreakDown[3], itemBreakDown[4], 
                                itemBreakDown[5], itemBreakDown[6], Integer.parseInt(itemBreakDown[7]) == 1, 
                                Integer.parseInt(itemBreakDown[8]),Integer.parseInt(itemBreakDown[9]) == 1, 
                                Integer.parseInt(itemBreakDown[10]));
                        gameItems.put(itemIndex, newItem);
                        break;
                    case'C':
                        newItem = new StackableConsumableItem(itemBreakDown[1], Integer.parseInt(itemBreakDown[2]), itemIndex, 
                                itemBreakDown[3], Integer.parseInt(itemBreakDown[4]), 
                                Integer.parseInt(itemBreakDown[5]),itemBreakDown[6], Integer.parseInt(itemBreakDown[7]),
                                itemBreakDown[8]);
                        gameItems.put(itemIndex, newItem);
                        break;
                    case'S':
                        newItem = new StackableNonconsumableItem(itemBreakDown[1],Integer.parseInt(itemBreakDown[2]), 
                                itemIndex, itemBreakDown[3], Integer.parseInt(itemBreakDown[4]), 1);
                        gameItems.put(itemIndex, newItem);
                        break;
                    case'T':
                        newItem = new TrashItem(itemBreakDown[1], Integer.parseInt(itemBreakDown[2]), 
                                itemIndex, itemBreakDown[3]);
                        gameItems.put(itemIndex, newItem);
                        break;
                    case'U':
                        newItem = new UsableItem(itemBreakDown[1], Integer.parseInt(itemBreakDown[2]), 
                                itemIndex, itemBreakDown[3], Integer.parseInt(itemBreakDown[4]), 
                                itemBreakDown[5], itemBreakDown[6], Integer.parseInt(itemBreakDown[7]) == 1);
                        gameItems.put(itemIndex, newItem);
                        break;
                    default:
                        break;
                }
            }
            //***********************************************************************************************
            
            //***********************************************************************************************
            //Reads in objects from the Objects.txt file and adds them into the list of objects in the world.
            File objectsFile = new File(LOADSAVE + "Objects.txt"); 
            textBuffer = new BufferedReader(new FileReader(objectsFile)); 
            currentLine = "";
            while (textBuffer.ready()) 
            {
                currentLine += textBuffer.readLine() + "-";
            }
            lineBreakDown = currentLine.split("-");
            for(int objectIndex = 0; objectIndex < lineBreakDown.length; objectIndex++)
            {
                String [] objectBreakDown = lineBreakDown[objectIndex].split("#");
                GameObjects newObject = new GameObjects(objectBreakDown[0], objectIndex, 
                        Integer.parseInt(objectBreakDown[1]) == 1, objectBreakDown[2],
                        Integer.parseInt(objectBreakDown[3]) == 1, objectBreakDown[4]); 
                gameObjects.put(objectIndex, newObject);
            }
            //***********************************************************************************************
            
            //***********************************************************************************************
            //Reads in creatures from the Creatures.txt file and adds them into the list of creatures in the world.
            File creatureFile = new File(LOADSAVE + "Creatures.txt"); 
            textBuffer = new BufferedReader(new FileReader(creatureFile)); 
            currentLine = "";
            while (textBuffer.ready()) 
            {
                currentLine += textBuffer.readLine() + "-";
            }
            lineBreakDown = currentLine.split("-");
            for(int creatureIndex = 0; creatureIndex < lineBreakDown.length; creatureIndex++)
            {
                Map dropTable = new HashMap();
                String [] creatureBreakDown = lineBreakDown[creatureIndex].split("#");
                String [] dropBreakDown = creatureBreakDown[9].split(";");
                for(int dropIndex = 0; dropIndex < dropBreakDown.length; dropIndex++)
                {
                    String [] split = dropBreakDown[dropIndex].split(":");
                    dropTable.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                }
                Creature newCreature =  new Creature(creatureBreakDown[0], creatureIndex, 
                        Integer.parseInt(creatureBreakDown[1]), creatureBreakDown[2], 
                        Integer.parseInt(creatureBreakDown[3]), new int[0],
                        Integer.parseInt(creatureBreakDown[4]), Integer.parseInt(creatureBreakDown[5]), 
                        Integer.parseInt(creatureBreakDown[6]), Integer.parseInt(creatureBreakDown[7]), 
                        Integer.parseInt(creatureBreakDown[8]), dropTable,
                        Integer.parseInt(creatureBreakDown[10]), false, 0); 
                gameCreatures.put(creatureIndex, newCreature);
            }
            //***********************************************************************************************
            
            //***********************************************************************************************
            //Reads in quest from the Quest.txt file and adds them into the list of quests in the world.
            File questFile = new File(LOADSAVE + "Quest.txt"); 
            textBuffer = new BufferedReader(new FileReader(questFile)); 
            currentLine = "";
            while (textBuffer.ready()) 
            {
                currentLine += textBuffer.readLine() + "-";
            }
            lineBreakDown = currentLine.split("-");
            for(int questIndex = 0; questIndex < lineBreakDown.length; questIndex++)
            {
                String [] questBreakDown = lineBreakDown[questIndex].split("#");
                String [] objectives = questBreakDown[1].split(";");
                Quest newQuest = new Quest(questBreakDown[0], questIndex, objectives, 
                        questBreakDown[2]);
                gameQuest.put(questIndex, newQuest);
            }
            //***********************************************************************************************
            
            //***********************************************************************************************
            //Reads in npcs from the NPCs.txt file and adds them into the list of npcs in the world.
            File npcFile = new File(LOADSAVE + "NPCs.txt"); 
            textBuffer = new BufferedReader(new FileReader(npcFile)); 
            currentLine = "";
            while (textBuffer.ready()) 
            {
                currentLine += textBuffer.readLine() + "#";
            }
            lineBreakDown = currentLine.split("#");
            for(int npcIndex = 0; npcIndex < lineBreakDown.length; npcIndex++)
            {
                String [] npcBreakDown = lineBreakDown[npcIndex].split(":");
                
                String [] statsBreakDown = npcBreakDown[3].split(";");
                int [] stats = new int[statsBreakDown.length];
                for(int statIndex = 0; statIndex < statsBreakDown.length; statIndex++)
                    stats[statIndex] = Integer.parseInt(statsBreakDown[statIndex]);
                
                String [] itemBreakDown = npcBreakDown[5].split(";");
                Inventory items = new Inventory();
                for(int itemIndex = 0; itemIndex < itemBreakDown.length; itemIndex++)
                {
                    String [] stackableItemBreakDown = itemBreakDown[itemIndex].split(":");
                    if(stackableItemBreakDown.length == 1)
                        items.addItem((Item)(WORLD.getGameItems().get(Integer.parseInt(stackableItemBreakDown[0]))));
                    else
                    {
                        Item newItem;

                        if(((Item)(gameItems.get(Integer.parseInt(stackableItemBreakDown[0])))) instanceof StackableConsumableItem)
                        {
                            StackableConsumableItem currentItem = ((StackableConsumableItem)(gameItems.get(Integer.parseInt(stackableItemBreakDown[0]))));
                            newItem = new StackableConsumableItem(currentItem.getItemName(), currentItem.getValue()
                                    , currentItem.getItemId(), currentItem.getDetails(), currentItem.maxStackAmount(), 
                                    1,currentItem.useEffect(), currentItem.cooldownTimer(),currentItem.getRequirements()); 
                        }
                        else
                        {
                            StackableNonconsumableItem currentItem = ((StackableNonconsumableItem)(gameItems.get(Integer.parseInt(stackableItemBreakDown[0]))));
                            newItem = new StackableNonconsumableItem(currentItem.getItemName(),currentItem.getValue(), 
                                    currentItem.getItemId(), currentItem.getDetails(), currentItem.maxStackAmount(), 1);
                        }

                        for(int indexItemCount = 0; indexItemCount < Integer.parseInt(stackableItemBreakDown[1]); indexItemCount++)
                            items.addItem(newItem);      
                    }
                }
                
                String [] questBreakDown = npcBreakDown[7].split(";");
                int [] quests = new int[questBreakDown.length];
                for(int questIndex = 0; questIndex < questBreakDown.length; questIndex++)
                    quests[questIndex] = Integer.parseInt(questBreakDown[questIndex]);
                
                String [] equipmentBreakDown = npcBreakDown[10].split(";");
                int [] equipment = new int[equipmentBreakDown.length];
                for(int equipmentIndex = 0; equipmentIndex < equipmentBreakDown.length; equipmentIndex++)
                    equipment[equipmentIndex] = Integer.parseInt(equipmentBreakDown[equipmentIndex]);
                
                NPC newNPC = new NPC(npcBreakDown[0], npcBreakDown[1], npcBreakDown[2], 
                        stats, npcBreakDown[4].split(";"), items, Integer.parseInt(itemBreakDown[6]) == 1, 
                        quests, itemBreakDown[8].split(";"), Integer.parseInt(itemBreakDown[9]), equipment, itemBreakDown[11], 
                        Integer.parseInt(itemBreakDown[12]), Integer.parseInt(itemBreakDown[13]), 
                        Integer.parseInt(itemBreakDown[14]), npcIndex);
                gameNPCs.put(npcIndex, newNPC);
            }
            //***********************************************************************************************
            
            //***********************************************************************************************
            //Reads in structures from the Structures.txt file and adds them into the list of structures in the world.
            File structureFile = new File(LOADSAVE + "Structures.txt"); 
            textBuffer = new BufferedReader(new FileReader(structureFile)); 
            currentLine = "";
            while (textBuffer.ready()) 
            {
                currentLine += textBuffer.readLine() + "-";
            }
            lineBreakDown = currentLine.split("-");
            for(int structureIndex = 0; structureIndex < lineBreakDown.length; structureIndex++)
            {
                String [] structureBreakDown = lineBreakDown[structureIndex].split("#");
                
                String [] gridsBreakdown = structureBreakDown[0].split(";");
                int [] grids = new int [gridsBreakdown.length];
                for(int gridIndex = 0; gridIndex < gridsBreakdown.length; gridIndex++)
                    grids[gridIndex] = Integer.parseInt(gridsBreakdown[gridIndex]);
                
                String [] doorBreakdown = structureBreakDown[1].split(";");
                int [] doors = new int [doorBreakdown.length];
                for(int doorIndex = 0; doorIndex < doorBreakdown.length; doorIndex++)
                    doors[doorIndex] = Integer.parseInt(doorBreakdown[doorIndex]);
                
                String [] enterBreakdown = structureBreakDown[2].split(";");
                int [] enterFromGrid = new int [enterBreakdown.length];
                for(int enterIndex = 0; enterIndex < doorBreakdown.length; enterIndex++)
                    enterFromGrid[enterIndex] = Integer.parseInt(enterBreakdown[enterIndex]);
                
                Structure newStructure = new Structure(grids, doors, enterFromGrid, 
                        Integer.parseInt(structureBreakDown[3]), structureBreakDown[4], 
                        structureBreakDown[5], Integer.parseInt(structureBreakDown[6]) == 1, 
                        Integer.parseInt(structureBreakDown[7]) == 1);
                gameStructures.put(structureIndex, newStructure);
            }
            //***********************************************************************************************
            
            //***********************************************************************************************
            //Reads in grids from the Grids.txt file and adds them into the list of grids in the world.
            File gridFile = new File(LOADSAVE + "Grids.txt"); 
            textBuffer = new BufferedReader(new FileReader(gridFile)); 
            currentLine = "";
            while (textBuffer.ready()) 
            {
                currentLine += textBuffer.readLine() + "#";
            }
            lineBreakDown = currentLine.split("#");
            for(int gridIndex = 0; gridIndex < lineBreakDown.length; gridIndex++)
            {
                String [] gridBreakDown = lineBreakDown[gridIndex].split(":");
                
                String [] gameObjectsBreakDown = gridBreakDown[0].split(";");
                GameObjects [] gridObjects = new GameObjects[gameObjectsBreakDown.length];
                for(int objectIndex = 0; objectIndex < gameObjectsBreakDown.length; objectIndex++)
                    gridObjects[objectIndex] = (GameObjects)(gameObjects.get(Integer.parseInt(gameObjectsBreakDown[objectIndex])));
                
                String [] creaturesBreakDown = gridBreakDown[1].split(";");
                Creature [] creatures = new Creature[creaturesBreakDown.length];
                for(int creatureIndex = 0; creatureIndex < creaturesBreakDown.length; creatureIndex++)
                {
                    Creature current = (Creature)(gameCreatures.get(Integer.parseInt(creaturesBreakDown[creatureIndex])));
                    creatures [creatureIndex] = new Creature(current.getName(), current.getCreatureID(),
                    current.getLevel(), current.getDetails(), current.getAttack(), new int[0],
                    current.getMaxItems(), current.getMinItems(), current.getXp(), current.getHealth(),
                    current.getGridSize(),  current.getDropTable(), current.getRespawn(), false, 0);
                }
                
                String [] NPCBreakDown = gridBreakDown[2].split(";");
                NPC [] NPCs = new NPC[NPCBreakDown.length];
                for(int npcIndex = 0; npcIndex < NPCBreakDown.length; npcIndex++)
                    NPCs[npcIndex] = (NPC)(gameNPCs.get(Integer.parseInt(NPCBreakDown[npcIndex])));
                
                String [] structureBreakDown = gridBreakDown[3].split(";");
                Structure [] structures = new Structure[structureBreakDown.length];
                for(int structureIndex = 0; structureIndex < structureBreakDown.length; structureIndex++)
                    structures[structureIndex] = (Structure)(gameStructures.get(Integer.parseInt(NPCBreakDown[structureIndex])));
                
                Grid newGrid = new Grid(gridObjects, creatures, NPCs, structures,
                        gridBreakDown[4], gridIndex, gridBreakDown[5]);
                
                gameGrids.put(gridIndex, newGrid);
            }
            //***********************************************************************************************
            
            WORLD = new World(gameObjects, gameItems, gameCreatures,
            gameQuest, gameNPCs, gameGrids);
    }
    
    private static void loadPlayer() throws Exception
    {
        //***********************************************************************************************
            //Reads in items from the Items.txt file and adds them into the list of items in the world.
            ((Grid)(WORLD.getGameGrids().get(0))).setNorth(((Grid)(WORLD.getGameGrids().get(1))));
            ((Grid)(WORLD.getGameGrids().get(1))).setSouth(((Grid)(WORLD.getGameGrids().get(0))));
            File playerFile = new File(LOADSAVE + playerName + ".txt"); 
            BufferedReader textBuffer = new BufferedReader(new FileReader(playerFile)); 
            String currentLine = "";
            while (textBuffer.ready()) 
            {
                currentLine += textBuffer.readLine() + "#";
            }
            String [] lineBreakDown = currentLine.split("#");
            String [] itemBreakDown = lineBreakDown[0].split(";");
            Inventory items = new Inventory();
            for(int itemIndex = 0; itemIndex < itemBreakDown.length; itemIndex++)
            {
                String [] stackableItemBreakDown = itemBreakDown[itemIndex].split(":");
                if(stackableItemBreakDown.length == 1)
                    items.addItem((Item)(WORLD.getGameItems().get(Integer.parseInt(stackableItemBreakDown[0]))));
                else
                {
                    Item newItem;
                    if(((Item)(WORLD.getGameItems().get(Integer.parseInt(stackableItemBreakDown[0])))) instanceof StackableConsumableItem)
                    {
                        StackableConsumableItem currentItem = ((StackableConsumableItem)(WORLD.getGameItems().get(Integer.parseInt(stackableItemBreakDown[0]))));
                        newItem = new StackableConsumableItem(currentItem.getItemName(), currentItem.getValue()
                                , currentItem.getItemId(), currentItem.getDetails(), currentItem.maxStackAmount(), 
                                1,currentItem.getItemEffect(), currentItem.cooldownTimer(),currentItem.getRequirements()); 
                    }
                    else
                    {
                        StackableNonconsumableItem currentItem = ((StackableNonconsumableItem)(WORLD.getGameItems().get(Integer.parseInt(stackableItemBreakDown[0]))));
                        newItem = new StackableNonconsumableItem(currentItem.getItemName(),currentItem.getValue(), 
                                currentItem.getItemId(), currentItem.getDetails(), currentItem.maxStackAmount(), 1);
                    }
                    
                    for(int indexItemCount = 0; indexItemCount < Integer.parseInt(stackableItemBreakDown[1]); indexItemCount++)
                        items.addItem(newItem);      
                }
            }

            String [] statsBreakDown = lineBreakDown[4].split(";");
            int[] stats = new int [statsBreakDown.length];
            for(int statsIndex = 0; statsIndex < statsBreakDown.length; statsIndex++)
                stats[statsIndex]  = Integer.parseInt(statsBreakDown[statsIndex]);

            String [] equipmentBreakDown = lineBreakDown[6].split(";");
            EquipableItem [] equipment = new EquipableItem[equipmentBreakDown.length];
            for(int equipmentIndex = 0; equipmentIndex < equipmentBreakDown.length; equipmentIndex++)
            {
                for(int indexInventory = 0; indexInventory < items.getInventory().length; indexInventory++)
                {
                    if((items.getInventory()[indexInventory].getItemId() == Integer.parseInt(equipmentBreakDown[equipmentIndex])))
                    {
                        if(!((EquipableItem)(items.getInventory()[indexInventory])).isEquip())
                        {
                            ((EquipableItem)(items.getInventory()[indexInventory])).equip();
                            equipment[equipmentIndex] = ((EquipableItem)(items.getInventory()[indexInventory]));
                        }
                    }
                }
            }

            currentPlayer =  new Player(playerName, items, Integer.parseInt(lineBreakDown[1]), 
                    Integer.parseInt(lineBreakDown[2]), Integer.parseInt(lineBreakDown[3]),
                    stats, Integer.parseInt(lineBreakDown[5]), equipment, 
                    Integer.parseInt(lineBreakDown[7]), Integer.parseInt(lineBreakDown[8]),
                    Integer.parseInt(lineBreakDown[9]));
            //***********************************************************************************************
    }
    
    private static void savePlayer() throws Exception
    {
        File newFile = new File("gameSaves/" + playerName + "/" + playerName + ".txt");
        
        PrintStream fileStream = new PrintStream(newFile);
        String inventory = "";
        for(int indexInventory = 0; indexInventory < currentPlayer.getInventory().getInventory().length; indexInventory++)
        {
            if(currentPlayer.getInventory().getInventory()[indexInventory] instanceof StackableItem)
            {
                if(indexInventory != currentPlayer.getInventory().getInventory().length - 1)
                    inventory += ((StackableItem)currentPlayer.getInventory().getInventory()[indexInventory]).getItemId() + ":"
                            + ((StackableItem)currentPlayer.getInventory().getInventory()[indexInventory]).stackAmount() + ";";
                else
                    inventory += ((StackableItem)currentPlayer.getInventory().getInventory()[indexInventory]).getItemId() + ":"
                            + ((StackableItem)currentPlayer.getInventory().getInventory()[indexInventory]).stackAmount();
            }
            else
            {
                if(indexInventory != currentPlayer.getInventory().getInventory().length - 1)
                    inventory += currentPlayer.getInventory().getInventory()[indexInventory].getItemId() + ";";
                else
                    inventory += currentPlayer.getInventory().getInventory()[indexInventory].getItemId();
            }
        }
        String [] stats = currentPlayer.getStats().split(";");
        fileStream.println(inventory);
        fileStream.println(currentPlayer.getPlayerLevel());
        fileStream.println((currentPlayer.getNumberOfSkillPoints() - currentPlayer.getUsableSkillPoints()));
        fileStream.println((currentPlayer.getUsableSkillPoints() - 1)) ;
        fileStream.println(stats[1] + ";" + stats[2] + ";" + stats[3] + ";" + stats[4]);
        fileStream.println(currentPlayer.getPlayerExperience());
        String equipment = "";
        for(int indexEquipment = 0; indexEquipment < 12; indexEquipment++)
            if(currentPlayer.getPlayerEquipment()[indexEquipment] != null)
            {
                if(indexEquipment != 11)
                    equipment += currentPlayer.getPlayerEquipment()[indexEquipment].getItemId() + ";";
                else
                    equipment += currentPlayer.getPlayerEquipment()[indexEquipment].getItemId();
            }
            else
            {
                if(indexEquipment != 11)
                    equipment += "-1;";
                else
                    equipment += "-1";
            }
        fileStream.println(equipment);
        fileStream.println(currentPlayer.getPlayerLocation());
        fileStream.println(currentPlayer.getPlayerMaxHealth());
        fileStream.println(0);
        fileStream.flush();
        fileStream.close();
    }
    
    private static void questCommands(String command)
    {
        Map questCommands = new HashMap();
        questCommands.put("LIST", 0);
        questCommands.put("COMPLETED", 1);
        questCommands.put("DETAILS", 2);
        String subCommand;
        int subCommandIndex = 25;
        
        subCommand = command.split(" ")[1].toUpperCase();
        if(questCommands.containsKey(subCommand))
            subCommandIndex = (int)(questCommands.get(subCommand));
        switch(subCommandIndex)
        {
            case 0:
                    System.out.printf(currentPlayer.getQuestLog().getCurrentQuests());
                break;
            case 1:
                    System.out.printf(currentPlayer.getQuestLog().getCompletedQuests());
                break;
            case 2:
                    if(command.split(" ").length > 2)
                        System.out.printf(currentPlayer.getQuestLog().getQuestDetails(command.split(" ")[2], WORLD));
                    else
                    {
                        System.out.printf("No quest Name Was Input.\n\n", subCommand);
                        System.out.printf(new HELP_COMMAND("HELP QUEST", true).printOutput()); 
                    }
                break;
            default:
                System.out.printf("%s is not a valid sub-command of quest.\n\n", subCommand);
                System.out.printf(new HELP_COMMAND("HELP QUEST", true).printOutput()); 
        }
    }
    
    private static void lockCommands(String command)
    {
        Map lockCommands = new HashMap();
        lockCommands.put("NEXTENEMY", 0);
        String subCommand;
        int subCommandIndex = 25;
        
        subCommand = command.split(" ")[1].toUpperCase();
        if(lockCommands.containsKey(subCommand))
            subCommandIndex = (int)(lockCommands.get(subCommand));
        switch(subCommandIndex)
        {
            case 0:
                    currentPlayer.setTarget(currentPlayer.getTarget() + 1);
                break;
            default:
                System.out.printf("%s is not a valid sub-command of target.\n\n", subCommand);
                System.out.printf(new HELP_COMMAND("HELP TARGET", true).printOutput()); 
        }
    }
    
    private static void npcCommands(String command)
    {
        Map npcCommands = new HashMap();
        npcCommands.put("TALK", 0);
        npcCommands.put("SHOP", 1);
        npcCommands.put("QUEST", 2);
        npcCommands.put("BUY", 3);
        npcCommands.put("SELL", 4);
        String subCommand;
        int subCommandIndex = 25;
        
        subCommand = command.split(" ")[1].toUpperCase();
        if(npcCommands.containsKey(subCommand))
            subCommandIndex = (int)(npcCommands.get(subCommand));
        NPC npcs[];
        switch(subCommandIndex)
        {
            case 0:
                    if(command.split(" ").length > 2)
                    {
                        npcs = ((NPC [])(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getPeople()));
                        for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            if(command.split(" ")[3].equalsIgnoreCase(npcs[indexNPCs].getName()))
                                System.out.printf(npcs[indexNPCs].getDialogue() + "\n");
                    }
                    else
                    {
                        System.out.printf("There is no NPC named that.\n\n", subCommand);
                        System.out.printf(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                    }
                        
                break;
            case 1:
                    if(command.split(" ").length > 2)
                    {
                        npcs = ((NPC [])(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getPeople()));
                        for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                        {
                            if(command.split(" ")[3].equalsIgnoreCase(npcs[indexNPCs].getName()) && npcs[indexNPCs].isShopKeeper())
                                System.out.printf(((Inventory)(npcs[indexNPCs].getInventory())).list());
                            else
                                System.out.printf("This NPC is not a shop\n");
                        }
                    }
                    else
                    {
                        System.out.printf("There is no NPC named that.\n\n", subCommand);
                        System.out.printf(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                    }
                break;
            case 2:
                    if(command.split(" ").length > 2)
                    {
                        npcs = ((NPC [])(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getPeople()));
                        for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                        {
                            if(command.split(" ")[3].equalsIgnoreCase(npcs[indexNPCs].getName()))
                            {
                                for(int indexNPCQuest = 0; indexNPCQuest < npcs[indexNPCs].getLinkedQuest().length; indexNPCQuest++)
                                {
                                    for(int indexPlayerQuest = 0; indexPlayerQuest < currentPlayer.getQuestLog().getActiveQuest().length; indexPlayerQuest++)
                                    {
                                        if((npcs[indexNPCs].getLinkedQuest()[indexNPCQuest]) == ((Quest[])(currentPlayer.getQuestLog().getActiveQuest()))[indexPlayerQuest].getQuestID())
                                            System.out.printf(npcs[indexNPCs].getQuestDialogue()[indexNPCs] + "\n");
                                    }
                                }
                            }
                            else
                                System.out.printf("You are not on a quest that with this NPC.\n");
                        }
                    }
                    else
                    {
                        System.out.printf("There is no NPC named that.\n\n", subCommand);
                        System.out.printf(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                    }
                break;
            case 3:
                    if(command.split(" ").length > 2)
                    {
                        npcs = ((NPC [])(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getPeople()));
                        for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            if(command.split(" ")[3].equalsIgnoreCase(npcs[indexNPCs].getName()) && command.split(" ").length > 3)
                            {
                                for(int indexItem = 0; indexItem < 100; indexItem++)
                                {
                                    if(npcs[indexNPCs].hasItemInShop(command.split(" ")[4]))
                                        currentPlayer.getInventory().addItem((Item)(WORLD.getGameItems().get(npcs[indexNPCs].buyItemFromShop(command.split(" ")[4]))));
                                    else
                                        System.out.printf("There is no item named that in this shop.\n\n", subCommand);
                                }
                            }
                            else
                            {
                                System.out.printf(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                            }
                    }
                    else
                    {
                        System.out.printf("There is no NPC named that.\n\n", subCommand);
                        System.out.printf(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                    }
                break;
            case 4:
                    if(command.split(" ").length > 2)
                    {
                        npcs = ((NPC [])(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getPeople()));
                        for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            if(command.split(" ")[3].equalsIgnoreCase(npcs[indexNPCs].getName()) && command.split(" ").length > 3)
                            {
                                for(int indexItem = 0; indexItem < 100; indexItem++)
                                {
                                    if(currentPlayer.hasItemInShop(command.split(" ")[4]))
                                        npcs[indexNPCs].getInventory().addItem((Item)(WORLD.getGameItems().get(currentPlayer.sellItemToShop(command.split(" ")[4]))));
                                    else
                                        System.out.printf("There is no item named that in this shop.\n\n", subCommand);
                                }
                            }
                            else
                            {
                                System.out.printf(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                            }
                    }
                    else
                    {
                        System.out.printf("There is no NPC named that.\n\n", subCommand);
                        System.out.printf(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                    }
                break;
            default:
                System.out.printf("%s is not a valid sub-command of NPC.\n\n", subCommand);
                System.out.printf(new HELP_COMMAND("HELP NPC", true).printOutput()); 
        }
    }
    
    private static void inventoryCommands(String command)
    {
        Map inventoryCommands = new HashMap();
        inventoryCommands.put("LIST", 0);
        inventoryCommands.put("VIEW", 1);
        inventoryCommands.put("USE", 2);
        inventoryCommands.put("DROP", 3);
        inventoryCommands.put("EQUIP", 4);
        inventoryCommands.put("UNEQUIP", 5);
        inventoryCommands.put("EQUIPMENT", 6);
        String subCommand;
        int subCommandIndex = 25;
        
        subCommand = command.split(" ")[1].toUpperCase();
        if(inventoryCommands.containsKey(subCommand))
            subCommandIndex = (int)(inventoryCommands.get(subCommand));
        switch(subCommandIndex)
        {
            case 0:
                    System.out.printf(currentPlayer.getInventory().list());
                break;
            case 1:
                    if(command.split(" ").length == 3)
                        System.out.printf(currentPlayer.getInventory().viewItemByName(command.split(" ")[2]));
                    else if (command.split(" ").length > 3)
                    {
                        String name = "";
                        for(int indexName = 2; indexName < command.split(" ").length; indexName++)
                        {
                            if(indexName == 2)
                                name += command.split(" ")[indexName];
                            else
                                name += " " + command.split(" ")[indexName];
                        }
                        System.out.printf(currentPlayer.getInventory().viewItemByName(name));
                    }  
                    else
                    {
                        System.out.printf("No Item Name Was Input.\n\n");
                        System.out.printf(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
                    }
                break;
            case 2:
                    if(command.split(" ").length == 3)
                        System.out.printf(currentPlayer.getInventory().useItemByName(command.split(" ")[2], currentPlayer));
                    else if (command.split(" ").length > 3)
                    {
                        String name = "";
                        for(int indexName = 2; indexName < command.split(" ").length; indexName++)
                        {
                            if(indexName == 2)
                                name += command.split(" ")[indexName];
                            else
                                name += " " + command.split(" ")[indexName];
                        }
                        System.out.printf(currentPlayer.getInventory().useItemByName(name, currentPlayer) + "\n");
                    }       
                    else
                    {
                        System.out.printf("No Item Name Was Input.\n\n");
                        System.out.printf(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
                    }
                break;
            case 3:
                    if(command.split(" ").length == 3)
                        currentPlayer.getInventory().removeItemByName(command.split(" ")[2]);
                    else if (command.split(" ").length > 3)
                    {
                        String name = "";
                        for(int indexName = 2; indexName < command.split(" ").length; indexName++)
                        {
                            if(indexName == 2)
                                name += command.split(" ")[indexName];
                            else
                                name += " " + command.split(" ")[indexName];
                        }
                        currentPlayer.getInventory().removeItemByName(name);
                    }
                    else
                    {
                        System.out.printf("No Item Name Was Input.\n\n");
                        System.out.printf(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
                    }
                break;
            case 4:
                    if(command.split(" ").length == 3)
                         System.out.printf(currentPlayer.getInventory().equipItemByName(command.split(" ")[2], currentPlayer));
                    else if (command.split(" ").length > 3)
                    {
                        String name = "";
                        for(int indexName = 2; indexName < command.split(" ").length; indexName++)
                        {
                            if(indexName == 2)
                                name += command.split(" ")[indexName];
                            else
                                name += " " + command.split(" ")[indexName];
                        }
                        System.out.printf(currentPlayer.getInventory().equipItemByName(name, currentPlayer));
                    } 
                    else
                    {
                        System.out.printf("No Item Name Was Input.\n\n");
                        System.out.printf(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
                    }
                break;
            case 5:
                    if(command.split(" ").length == 3)
                         System.out.printf(currentPlayer.getInventory().unequipItemByName(command.split(" ")[2], currentPlayer));
                    else if (command.split(" ").length > 3)
                    {
                        String name = "";
                        for(int indexName = 2; indexName < command.split(" ").length; indexName++)
                        {
                            if(indexName == 2)
                                name += command.split(" ")[indexName];
                            else
                                name += " " + command.split(" ")[indexName];
                        }
                        System.out.printf(currentPlayer.getInventory().unequipItemByName(name, currentPlayer));
                    } 
                    else
                    {
                        System.out.printf("No Item Name Was Input.\n\n");
                        System.out.printf(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
                    }
                break;    
            case 6:
                    currentPlayer.getInventory().checkEquipedItems(currentPlayer);
                break;
            default:
                System.out.printf("%s is not a valid sub-command of inventory.\n\n", subCommand);
                System.out.printf(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
        }
    }
    
    private static void moveCommands(String command)
    {
        Map moveCommands = new HashMap();
        moveCommands.put("NORTH", 0);
        moveCommands.put("EAST", 1);
        moveCommands.put("SOUTH", 2);
        moveCommands.put("WEST", 3);
        moveCommands.put("NORTHEAST", 4);
        moveCommands.put("NORTHWEST", 5);
        moveCommands.put("SOUTHEAST", 6);
        moveCommands.put("SOUTHWEST", 7);
        String subCommand;
        int subCommandIndex = 25;
        
        subCommand = command.split(" ")[1].toUpperCase();
        if(moveCommands.containsKey(subCommand))
            subCommandIndex = (int)(moveCommands.get(subCommand));
        switch(subCommandIndex)
        {
            case 0:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorth() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorth().getTransitFromSouth().equalsIgnoreCase("Block")))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorth().getGridID());
                    System.out.printf("%s:\n\t%s\n\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails());
                }
                else
                     System.out.printf("There is no path in that direction.\n\n");
                break;
            case 1:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getEast() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getEast().getTransitFromWest().equalsIgnoreCase("Block")))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getEast().getGridID());
                    System.out.printf("%s:\n\t%s\n\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails());
                }
                else
                     System.out.printf("There is no path in that direction.\n\n");
                break;
            case 2:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouth() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouth().getTransitFromNorth().equalsIgnoreCase("Block")))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouth().getGridID());
                    System.out.printf("%s:\n\t%s\n\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails());
                }
                else
                     System.out.printf("There is no path in that direction.\n\n");
                break;
            case 3:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getWest() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getWest().getTransitFromEast().equalsIgnoreCase("Block")))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getWest().getGridID());
                    System.out.printf("%s:\n\t%s\n\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails());
                }
                else
                     System.out.printf("There is no path in that direction.\n\n");
                break;
            case 4:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthEast() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthEast().getTransitFromSouthWest()).equalsIgnoreCase("Block"))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthEast().getGridID());
                    System.out.printf("%s:\n\t%s\n\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails());
                }
                else
                     System.out.printf("There is no path in that direction.\n\n");
                break;
            case 5:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthWest() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthWest().getTransitFromSouthEast()).equalsIgnoreCase("Block"))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthWest().getGridID());
                    System.out.printf("%s:\n\t%s\n\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails());
                }
                else
                     System.out.printf("There is no path in that direction.\n\n");
                break;
            case 6:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthEast() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthEast().getTransitFromNorthWest()).equalsIgnoreCase("Block"))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthEast().getGridID());
                    System.out.printf("%s:\n\t%s\n\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails());
                }
                else
                     System.out.printf("There is no path in that direction.\n\n");
                break;
            case 7:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthWest() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthWest().getTransitFromNorthEast()).equalsIgnoreCase("Block"))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthWest().getGridID());
                    System.out.printf("%s:\n\t%s\n\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails());
                }
                else
                     System.out.printf("There is no path in that direction.\n\n");
                break;
            default:
                System.out.printf("%s is not a valid sub-command of Move.\n\n", subCommand);
                System.out.printf(new HELP_COMMAND("HELP TARGET", true).printOutput()); 
        }
    }
    
    private static void attack(String command)
    {
        String subCommand = command.split(" ")[1].toUpperCase();
        Creature [] currentCreatures = ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getCreatures();
        Creature currentCreature = null;
        for(int indexCreatures = 0; indexCreatures < currentCreatures.length; indexCreatures++)
            currentCreature = currentCreatures[indexCreatures];
            if(currentCreature.getName().equalsIgnoreCase(subCommand) && !currentCreature.isIsdead())
            {
                int hit = (int)(Math.random()*1 + .2);
                if(hit == 1)
                {
                    currentPlayer.takeDamage(currentCreature.getAttack());
                    System.out.printf("Your Health %d/%d\n", currentPlayer.getPlayerHealth(),currentPlayer.getPlayerMaxHealth());
                }
                else
                    System.out.printf("The %s missed.\n", currentCreature.getName());
                int hit2 = (int)(Math.random()*1 + .3);
                if(hit2 == 1)
                {
                    currentCreature.setHealth(currentCreature.getHealth() - Integer.parseInt(currentPlayer.getStats().split(";")[1]), 
                            currentPlayer, ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))));
                    System.out.printf("Creature Health %d/%d\n", currentCreature.getHealth(),currentCreature.getMaxHealth());
                    if(currentCreature.getHealth() == 0)
                    {
                        currentPlayer.gainExperience(currentCreature.getXp());
                        System.out.printf("You gained %d xp from killing a(n) %s\n",currentCreature.getXp(), currentCreature.getName());
                        System.out.println(currentCreature.getDropTable().toString());
                    }
                }
                else
                    System.out.printf("You missed the %s\n", currentCreature.getName());
            }
            else
            {
                System.out.printf("There is no creature by the name %s in this area.\n", command.split(" ")[1]);
            }
    }
}
