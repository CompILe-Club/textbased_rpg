import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;
import javafx.scene.input.KeyCode;
import javafx.application.Platform;


public class TextBasedRPGGameEngine extends Application {
    private static World WORLD;
    private static Player currentPlayer;
    private static String playerName = "";
    private static String LOADSAVE;
    private static final TextField INPUT =  new TextField ();
    private static TextArea textArea = new TextArea();
    private static String [] lastText = new String[0];
    private static int currentLastTextIndex = 0;
    private static boolean currentlyIndexing = false;
    Thread loadWorld = new Thread()
    {
        @Override
        public void run() 
        {
            try
            {
                LOADSAVE = loadSave();
                loadWorld();
                linkGrids();
                loadPlayer();
                textArea.appendText(String.format("Welcome %s\n\n", playerName));
                textArea.appendText(new HELP_COMMAND("HELP", true).printOutput() + "\n");
                textArea.appendText(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName() + "\n");
                textArea.appendText(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails() + "\n");
                textArea.appendText(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).searchArea() + "\n");
            }
            catch(Exception e)
            {
                textArea.appendText(e + "\n");
                while(true)
                {

                }
            }
            finally
            {

            } 
        }
    };
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Text Based RPG Game Engine");
        double height = 475;
        double width = 800; 
        double height2 = 10;
        double width2 = 800;
                
        textArea.setPrefHeight(height);
        textArea.setPrefWidth(width);  
        INPUT.setPrefHeight(height2);
        INPUT.setPrefWidth(width2); 
        textArea.setWrapText(true);
        textArea.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00");
        INPUT.setLayoutY(490);
        textArea.setEditable(false);
        
        VBox vbox = new VBox(textArea, INPUT);

        Scene scene = new Scene(vbox, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        INPUT.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER && (currentPlayer == null || currentPlayer.getPlayerHealth() > 0) && 
                        (!INPUT.getText().equalsIgnoreCase("")&& !INPUT.getText().equalsIgnoreCase(" ")))  {
                    if(!playerName.equalsIgnoreCase("NEW") && !playerName.equalsIgnoreCase(""))
                    {
                        Map commands = new HashMap();
                        //Done
                        commands.put("STATUS", 0);
                        //
                        commands.put("QUEST", 1);
                        //Done
                        commands.put("HELP", 2);
                        //Done
                        commands.put("INVENTORY", 4);
                        //Done
                        commands.put("NPC", 6);
                        //Done
                        commands.put("MOVE", 7);
                        //Done
                        commands.put("ATTACK", 8);
                        //Done
                        commands.put("SEARCH", 9);
                        //Done
                        commands.put("SAVE", 10);
                        //Done
                        commands.put("LOOT", 11);
                        String command = INPUT.getText();
                        addToLastText(command);
                        currentlyIndexing = false;
                        INPUT.setText("");
                        textArea.appendText(command + "\n\n");
                        String mainCommand = command.split(" ")[0].toUpperCase();
                        int commandIndex = 25;
                        if(commands.containsKey(mainCommand))
                            commandIndex = (int)(commands.get(mainCommand));
                        switch(commandIndex)
                        {
                            case 0:
                                textArea.appendText(currentPlayer.getStatus());
                                break;
                            case 1:
                                if(command.split(" ").length > 1)
                                    questCommands(command);
                                else
                                    textArea.appendText(new HELP_COMMAND("HELP QUEST", true).printOutput());
                                break;
                            case 2:
                                textArea.appendText(new HELP_COMMAND(command, true).printOutput());
                                break;
                            case 4:
                                if(command.split(" ").length > 1)
                                    inventoryCommands(command);
                                else
                                    textArea.appendText(new HELP_COMMAND("HELP INVENTORY", true).printOutput());
                                break;
                            case 6:
                                if(command.split(" ").length > 1)
                                    npcCommands(command);
                                else
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput());
                                break;
                            case 7:
                                if(command.split(" ").length > 1)
                                    moveCommands(command);
                                else
                                    textArea.appendText(new HELP_COMMAND("HELP MOVE", true).printOutput());
                                break;
                            case 8:
                                    if(command.split(" ").length > 1)
                                        attack(command);
                                    else
                                        textArea.appendText(new HELP_COMMAND("HELP ATTACK", true).printOutput());
                                break;
                            case 9:
                                    textArea.appendText(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName());
                                    textArea.appendText(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails());
                                    textArea.appendText(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).searchArea());
                                break;
                            case 10:
                                try
                                {
                                    savePlayer();
                                    textArea.appendText("Current Game World Has Been Saved.\n");
                                }
                                catch(Exception e)
                                {

                                }
                                break;
                            case 11:
                                try
                                {
                                    if(command.split(" ").length > 1)
                                            loot(command);
                                    else
                                        textArea.appendText(new HELP_COMMAND("HELP LOOT", true).printOutput());
                                }
                                catch(Exception e)
                                {
                                    textArea.appendText(e + "\n");
                                }
                                break;
                            default:
                                textArea.appendText(String.format(command.split(" ")[0] + " is not a valid command.\n%s", new HELP_COMMAND("Help", true).printOutput()));
                        }
                        textArea.appendText("\n");
                    }
                    else
                    {
                        String command = INPUT.getText();
                        INPUT.setText("");
                        textArea.appendText(command + "\n\n");
                        playerName = command;
                    }
                }
                else if(keyEvent.getCode() == KeyCode.UP && currentLastTextIndex >= 0 && lastText.length > 0)
                {
                    INPUT.setText(lastText[currentLastTextIndex]);
                    if(currentLastTextIndex > 0 && currentlyIndexing)
                       currentLastTextIndex --;
                    currentlyIndexing = true;
                }
                else if(keyEvent.getCode() == KeyCode.DOWN && currentLastTextIndex < lastText.length && lastText.length > 0 && currentlyIndexing)
                {
                    INPUT.setText(lastText[currentLastTextIndex]);
                    if(currentLastTextIndex < lastText.length - 1)
                       currentLastTextIndex ++;
                }
            }
        });
        try
        {
            loadWorld.start();
        }catch(Exception e)
        {
            System.out.println(e);
            while(true)
            {
                
            }
        }
    }
    @Override
    public void stop(){
        try
        {
            if(!playerName.equalsIgnoreCase("NEW") && !playerName.equalsIgnoreCase(""))
                savePlayer();
            textArea.appendText("Current Game World Has Been Saved.\n");
            loadWorld.destroy();
            Platform.exit();
        }
        catch(Exception e)
        {

        }
    }

    public static void main(String[] args) 
    {
        launch(args);
        
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
                textArea.appendText(String.format("Current Saves:\n\t"));
                for(int indexSaves = 0; indexSaves < directories.length; indexSaves++)
                    textArea.appendText(String.format("[%d] %s\n\t", indexSaves + 1, directories[indexSaves]));
                textArea.appendText(String.format("\nChoose A Save By Name or New: "));
                while(playerName.equalsIgnoreCase(""))
                {
                    
                }
                for(int indexSaves = 0; indexSaves < directories.length; indexSaves++)
                    if(playerName.equalsIgnoreCase(directories[indexSaves]))
                        goodSave = true;
                if(playerName.equalsIgnoreCase("NEW"))
                {
                    goodSave = true;
                    playerName = createNewSave();
                }
                if(goodSave == false)
                {
                    playerName = "";
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
            textArea.appendText("\nNew Character Name: ");
            while(playerName.equalsIgnoreCase("NEW") || playerName.equalsIgnoreCase(""))
            {

            }
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
                textArea.appendText(String.format("Current Save Already Exist or Key Word Was Input.\n"));
            }
            if(goodName == false)
            {
                playerName = "NEW";
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
        fileStream.println("1000");
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
                String [] creatureBreakDown = lineBreakDown[creatureIndex].split("#");
                String [] dropBreakDown = creatureBreakDown[9].split(";");
                int [] drops = new int[dropBreakDown.length];
                int [] dropChance = new int [dropBreakDown.length];
                for(int dropIndex = 0; dropIndex < dropBreakDown.length; dropIndex++)
                {
                    String [] split = dropBreakDown[dropIndex].split(":");
                    drops[dropIndex] = Integer.parseInt(split[0]);
                    dropChance[dropIndex] = Integer.parseInt(split[1]);
                }
                Creature newCreature =  new Creature(creatureBreakDown[0], creatureIndex, 
                        Integer.parseInt(creatureBreakDown[1]), creatureBreakDown[2], 
                        Integer.parseInt(creatureBreakDown[3]), new int[0],
                        Integer.parseInt(creatureBreakDown[4]), Integer.parseInt(creatureBreakDown[5]), 
                        Integer.parseInt(creatureBreakDown[6]), Integer.parseInt(creatureBreakDown[7]), 
                        Integer.parseInt(creatureBreakDown[8]), drops, dropChance,
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
                currentLine += textBuffer.readLine() + "=";
            }
            lineBreakDown = currentLine.split("=");
            for(int npcIndex = 0; npcIndex < lineBreakDown.length; npcIndex++)
            {
                String [] npcBreakDown = lineBreakDown[npcIndex].split("#");
                
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
                        items.addItem((Item)(gameItems.get(Integer.parseInt(stackableItemBreakDown[0]))));
                    else
                    {
                        Item newItem;
                        if(((Item)(gameItems.get(Integer.parseInt(stackableItemBreakDown[0])))) instanceof StackableConsumableItem)
                        {
                            StackableConsumableItem currentItem = ((StackableConsumableItem)(gameItems.get(Integer.parseInt(stackableItemBreakDown[0]))));
                            newItem = new StackableConsumableItem(currentItem.getItemName(), currentItem.getValue()
                                    , currentItem.getItemId(), currentItem.getDetails(), currentItem.maxStackAmount(), 
                                    1,currentItem.getItemEffect(), currentItem.cooldownTimer(),currentItem.getRequirements()); 
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
                
                NPC newNPC = new NPC(npcBreakDown[0], npcBreakDown[1], npcBreakDown[2], 
                        stats, npcBreakDown[4].split(";"), items, Integer.parseInt(npcBreakDown[6]) == 1, 
                        quests, npcBreakDown[8].split(";"), Integer.parseInt(npcBreakDown[9]), equipment, npcBreakDown[11], 
                        Integer.parseInt(npcBreakDown[12]), Integer.parseInt(npcBreakDown[13]), 
                        Integer.parseInt(npcBreakDown[14]), npcIndex);
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
                {
                    GameObjects current = (GameObjects)(gameObjects.get(Integer.parseInt(gameObjectsBreakDown[objectIndex])));
                    if(current != null)
                    { 
                        gridObjects [objectIndex] = new GameObjects(current.getName(), current.getObjectID(), current.isIsMoveable()
                                , current.getDetails(), current.isIsUsable(), current.getUseEffect());
                    }
                    else
                    {
                        gridObjects[objectIndex] = null;
                    }
                }
                
                String [] creaturesBreakDown = gridBreakDown[1].split(";");
                Creature [] creatures = new Creature[creaturesBreakDown.length];
                for(int creatureIndex = 0; creatureIndex < creaturesBreakDown.length; creatureIndex++)
                {
                    Creature current = (Creature)(gameCreatures.get(Integer.parseInt(creaturesBreakDown[creatureIndex])));
                    if(current != null)
                    {
                        creatures [creatureIndex] = new Creature(current.getName(), current.getCreatureID(),
                        current.getLevel(), current.getDetails(), current.getAttack(), new int[0],
                        current.getMaxItems(), current.getMinItems(), current.getXp(), current.getHealth(),
                        current.getGridSize(),  current.getDrops(), current.getDropsChance(), current.getRespawn(), false, 0);
                    }
                    else
                    {
                        creatures[creatureIndex] = null;
                    }
                }
                
                String [] NPCBreakDown = gridBreakDown[2].split(";");
                NPC [] NPCs = new NPC[NPCBreakDown.length];
                for(int npcIndex = 0; npcIndex < NPCBreakDown.length; npcIndex++)
                {
                    NPC current = (NPC)(gameNPCs.get(Integer.parseInt(NPCBreakDown[npcIndex])));
                    if(current != null)
                    {
                        NPCs[npcIndex] = new NPC(current.getFirstName(), current.getLastName(), current.getDescription(),
                            current.getStats(), current.getUseableAction(), current.getInventory(), current.isShopKeeper(),
                            current.getLinkedQuest(), current.getQuestDialogue(), gridIndex, current.getEquipment(),
                            current.getDialogue(), current.getHealth(), current.getMana(), current.getLevel(), current.getNPCID());
                    }
                    else
                    {
                        NPCs[npcIndex] = null;
                    }
                }
                
                String [] structureBreakDown = gridBreakDown[3].split(";");
                Structure [] structures = new Structure[structureBreakDown.length];
                for(int structureIndex = 0; structureIndex < structureBreakDown.length; structureIndex++)
                {
                     Structure current = (Structure)(gameStructures.get(Integer.parseInt(NPCBreakDown[structureIndex])));
                    if(current != null)
                    {
                        structures[structureIndex] = new Structure(current.getGridsCovered(), current.getDoorGrid()
                                , current.getEnterFrom(), current.getStructureID(), current.getName()
                                , current.getDetail(), current.isRideable(), current.isPushable());
                    }
                    else
                    {
                        structures[structureIndex] = null;
                    }
                }
                
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
                    Integer.parseInt(lineBreakDown[9]), Integer.parseInt(lineBreakDown[10]));
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
        fileStream.println(currentPlayer.getCurrency());
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
                    textArea.appendText(String.format(currentPlayer.getQuestLog().getCurrentQuests()));
                break;
            case 1:
                    textArea.appendText(String.format(currentPlayer.getQuestLog().getCompletedQuests()));
                break;
            case 2:
                    if(command.split(" ").length > 2)
                        textArea.appendText(String.format(currentPlayer.getQuestLog().getQuestDetails(command.split(" ")[2], WORLD)));
                    else
                    {
                        textArea.appendText("No quest Name Was Input.\n");
                        textArea.appendText(new HELP_COMMAND("HELP QUEST", true).printOutput()); 
                    }
                break;
            default:
                textArea.appendText(String.format("%s is not a valid sub-command of quest.\n", subCommand));
                textArea.appendText(new HELP_COMMAND("HELP QUEST", true).printOutput()); 
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
                textArea.appendText(String.format("%s is not a valid sub-command of target.\n", subCommand));
                textArea.appendText(new HELP_COMMAND("HELP TARGET", true).printOutput()); 
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
                        if(command.split(" ").length == 3)
                        {
                            for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            {
                                if(npcs[indexNPCs] != null && command.split(" ")[2].equalsIgnoreCase(npcs[indexNPCs].getName()))
                                    textArea.appendText(npcs[indexNPCs].getDialogue() + "\n");
                                else
                                {
                                    textArea.appendText("There is no NPC named that.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                            }
                        }
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
                            for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            {
                                if(npcs[indexNPCs] != null && name.equalsIgnoreCase(npcs[indexNPCs].getName()))
                                {
                                    textArea.appendText(npcs[indexNPCs].getDialogue() + "\n");
                                    break;
                                }
                                else if(indexNPCs == npcs.length - 1)
                                {
                                    textArea.appendText("There is no NPC named that.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                            }
                        }
                        else
                        {
                            textArea.appendText("There is no NPC named that.\n");
                            textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                        }
                    }
                    else
                    {
                        textArea.appendText("There is no NPC named that.\n");
                        textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                    }
                        
                break;
            case 1:
                    if(command.split(" ").length > 2)
                    {
                        npcs = ((NPC [])(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getPeople()));
                        if(command.split(" ").length == 3)
                        {
                            for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            {
                                if(npcs[indexNPCs] != null && command.split(" ")[2].equalsIgnoreCase(npcs[indexNPCs].getName()) && npcs[indexNPCs].isShopKeeper())
                                {
                                    textArea.appendText(((Inventory)(npcs[indexNPCs].getInventory())).list());
                                    break;
                                }
                                else if(indexNPCs == npcs.length - 1)
                                {
                                    textArea.appendText("There is no NPC named that.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                            }
                        }
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
                            for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            {
                                if(npcs[indexNPCs] != null && name.equalsIgnoreCase(npcs[indexNPCs].getName()) && npcs[indexNPCs].isShopKeeper())
                                {
                                    textArea.appendText(((Inventory)(npcs[indexNPCs].getInventory())).list());
                                    break;
                                }
                                else if(indexNPCs == npcs.length - 1)
                                {
                                    textArea.appendText("There is no NPC named that.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                            }
                        }  
                    }
                    else
                    {
                        textArea.appendText("There is no NPC named that.\n");
                        textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                    }
                break;
            case 2:
                    if(command.split(" ").length > 2)
                    {
                        npcs = ((NPC [])(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getPeople()));
                        if(command.split(" ").length == 3)
                        {
                            for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            {
                                if(npcs[indexNPCs] != null && command.split(" ")[3].equalsIgnoreCase(npcs[indexNPCs].getName()))
                                {
                                    for(int indexNPCQuest = 0; indexNPCQuest < npcs[indexNPCs].getLinkedQuest().length; indexNPCQuest++)
                                    {
                                        for(int indexPlayerQuest = 0; indexPlayerQuest < currentPlayer.getQuestLog().getActiveQuest().length; indexPlayerQuest++)
                                        {
                                            if((npcs[indexNPCs].getLinkedQuest()[indexNPCQuest]) == ((Quest[])(currentPlayer.getQuestLog().getActiveQuest()))[indexPlayerQuest].getQuestID())
                                                textArea.appendText(npcs[indexNPCs].getQuestDialogue()[indexNPCs] + "\n");
                                        }
                                    }
                                    break;
                                }
                                else if(indexNPCs == npcs.length - 1)
                                {
                                    textArea.appendText("There is no NPC named that.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                            }
                        }
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
                            for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            {
                                if(npcs[indexNPCs] != null && name.equalsIgnoreCase(npcs[indexNPCs].getName()))
                                {
                                    for(int indexNPCQuest = 0; indexNPCQuest < npcs[indexNPCs].getLinkedQuest().length; indexNPCQuest++)
                                    {
                                        for(int indexPlayerQuest = 0; indexPlayerQuest < currentPlayer.getQuestLog().getActiveQuest().length; indexPlayerQuest++)
                                        {
                                            if((npcs[indexNPCs].getLinkedQuest()[indexNPCQuest]) == ((Quest[])(currentPlayer.getQuestLog().getActiveQuest()))[indexPlayerQuest].getQuestID())
                                                textArea.appendText(npcs[indexNPCs].getQuestDialogue()[indexNPCs] + "\n");
                                        }
                                    }
                                    break;
                                }
                                else if(indexNPCs == npcs.length - 1)
                                {
                                    textArea.appendText("There is no NPC named that.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                            }
                        }
                        else
                        {
                            textArea.appendText("There is no NPC named that.\n");
                            textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                        }
                    }
                    else
                    {
                        textArea.appendText("There is no NPC named that.\n");
                        textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                    }
                break;
            case 3:
                    if(command.split(" ").length > 2)
                    {
                        npcs = ((NPC [])(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getPeople()));
                        if(command.split(" ").length == 3)
                        {
                            for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            {
                                if(npcs[indexNPCs] != null &&  command.split(" ")[3].split(":")[0].equalsIgnoreCase(npcs[indexNPCs].getName()) && command.split(" ")[3].split(":").length > 1)
                                {
                                    String itemName = "";
                                    for(int indexName = 1; indexName < command.split(" ")[3].split(":").length; indexName++)
                                    {
                                        if(indexName == 1)
                                            itemName += command.split(" ")[3].split(":")[indexName];
                                        else
                                            itemName += " " + command.split(" ")[3].split(":")[indexName];
                                    }
                                    if(npcs[indexNPCs].hasItemInShop(itemName))
                                    {
                                        if(((Item)(WORLD.getGameItems().get(currentPlayer.getInventory().getItemIDByName(itemName)))).getValue() < currentPlayer.getCurrency())
                                        {
                                            currentPlayer.useCurrency(((Item)(WORLD.getGameItems().get(currentPlayer.getInventory().getItemIDByName(itemName)))).getValue() * 3);
                                            currentPlayer.getInventory().addItem((Item)(WORLD.getGameItems().get(npcs[indexNPCs].buyItemFromShop(itemName, textArea))));
                                            textArea.appendText(String.format("You have bought: %s\n", itemName));
                                        }
                                        else
                                        {
                                            textArea.appendText(String.format("You do not have the money to buy: %s cost %d\n", itemName, ((Item)(WORLD.getGameItems().get(currentPlayer.getInventory().getItemIDByName(itemName)))).getValue()));
                                        }
                                    }
                                    else
                                        textArea.appendText(String.format("No item %s found in this shop\n", itemName));
                                    break;
                                }
                                else if(indexNPCs == npcs.length - 1)
                                {
                                    textArea.appendText("There is no NPC named that.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                                else if(command.split(" ")[3].split(":").length <= 1)
                                {
                                    textArea.appendText("Need to input an item.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                            }
                        }
                        else if(command.split(" ").length > 3)
                        {
                            String name = "";
                            for(int indexName = 2; indexName < command.split(" ").length; indexName++)
                            {
                                if(indexName == 2)
                                    name += command.split(" ")[indexName];
                                else
                                    name += " " + command.split(" ")[indexName];
                            }
                            for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            {
                                if(npcs[indexNPCs] != null && name.split(":")[0].equalsIgnoreCase(npcs[indexNPCs].getName()) && name.split(":").length > 1)
                                {
                                    String itemName = "";
                                    for(int indexName = 1; indexName < name.split(":").length; indexName++)
                                    {
                                        if(indexName == 1)
                                            itemName += name.split(":")[indexName];
                                        else
                                            itemName += " " + name.split(":")[indexName];
                                    }
                                    if(npcs[indexNPCs].hasItemInShop(itemName))
                                    {
                                        currentPlayer.useCurrency(((Item)(WORLD.getGameItems().get(currentPlayer.getInventory().getItemIDByName(itemName)))).getValue() * 3);
                                        currentPlayer.getInventory().addItem((Item)(WORLD.getGameItems().get(npcs[indexNPCs].buyItemFromShop(itemName, textArea))));
                                        textArea.appendText(String.format("You have bought: %s\n", itemName));
                                    }
                                    else
                                        textArea.appendText(String.format("No item %s found in this shop\n", itemName));
                                    break;
                                }
                                else if(indexNPCs == npcs.length - 1)
                                {
                                    textArea.appendText("There is no NPC named that.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                                else if(name.split(":").length <= 1)
                                {
                                    textArea.appendText("Need to input an item.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                            }
                        }
                    }
                    else
                    {
                        textArea.appendText("There is no NPC named that.\n");
                        textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                    }
                break;
            case 4:
                if(command.split(" ").length > 2)
                    {
                        npcs = ((NPC [])(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getPeople()));
                        if(command.split(" ").length == 3)
                        {
                            for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            {
                                if(npcs[indexNPCs] != null && command.split(" ")[2].split(":")[0].equalsIgnoreCase(npcs[indexNPCs].getName()) && command.split(" ")[2].split(":").length > 1)
                                {
                                    String itemName = "";
                                    for(int indexName = 1; indexName < command.split(" ")[3].split(":").length; indexName++)
                                    {
                                        if(indexName == 1)
                                            itemName += command.split(" ")[3].split(":")[indexName];
                                        else
                                            itemName += " " + command.split(" ")[3].split(":")[indexName];
                                    }
                                    if(currentPlayer.hasItemInShop(itemName))
                                    {
                                        currentPlayer.gainCurrency(((Item)(WORLD.getGameItems().get(currentPlayer.getInventory().getItemIDByName(itemName)))).getValue());
                                        npcs[indexNPCs].getInventory().addItem((Item)(WORLD.getGameItems().get(currentPlayer.sellItemToShop(itemName, textArea))));
                                        textArea.appendText(String.format("You have sold: %s\n", itemName));
                                    }
                                    else
                                        textArea.appendText(String.format("No item %s found in your inventory\n", itemName));
                                    break;
                                }
                                else if(indexNPCs == npcs.length - 1)
                                {
                                    textArea.appendText("There is no NPC named that.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                                else if (command.split(" ")[2].split(":").length > 1)
                                {
                                    textArea.appendText("Need to input an item.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                    break;
                                }
                            }
                        }
                        else if(command.split(" ").length > 3)
                        {
                            String name = "";
                            for(int indexName = 2; indexName < command.split(" ").length; indexName++)
                            {
                                if(indexName == 2)
                                    name += command.split(" ")[indexName];
                                else
                                    name += " " + command.split(" ")[indexName];
                            }
                            for(int indexNPCs = 0; indexNPCs < npcs.length; indexNPCs++)
                            {
                                if(npcs[indexNPCs] != null && name.split(":")[0].equalsIgnoreCase(npcs[indexNPCs].getName()) && name.split(":").length > 1)
                                {
                                    String itemName = "";
                                    for(int indexName = 1; indexName < name.split(":").length; indexName++)
                                    {
                                        if(indexName == 1)
                                            itemName += name.split(":")[indexName];
                                        else
                                            itemName += " " + name.split(":")[indexName];
                                    }
                                    if(currentPlayer.hasItemInShop(itemName))
                                    {
                                        currentPlayer.gainCurrency(((Item)(WORLD.getGameItems().get(currentPlayer.getInventory().getItemIDByName(itemName)))).getValue());
                                        npcs[indexNPCs].getInventory().addItem((Item)(WORLD.getGameItems().get(currentPlayer.sellItemToShop(itemName, textArea))));
                                        textArea.appendText(String.format("You have sold: %s\n", itemName));
                                    }
                                    else
                                        textArea.appendText(String.format("No item %s found in your inventory\n", itemName));
                                    break;
                                }
                                else if(indexNPCs == npcs.length - 1)
                                {
                                    textArea.appendText("There is no NPC named that.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                }
                                else if (name.split(":").length > 1)
                                {
                                    textArea.appendText("Need to input an item.\n");
                                    textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        textArea.appendText("There is no NPC named that.\n");
                        textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
                    }
                break;
            default:
                textArea.appendText(String.format("%s is not a valid sub-command of NPC.\n", subCommand));
                textArea.appendText(new HELP_COMMAND("HELP NPC", true).printOutput()); 
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
                    textArea.appendText(currentPlayer.getInventory().list());
                break;
            case 1:
                    if(command.split(" ").length == 3)
                        textArea.appendText(currentPlayer.getInventory().viewItemByName(command.split(" ")[2]));
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
                        textArea.appendText(currentPlayer.getInventory().viewItemByName(name));
                    }  
                    else
                    {
                        textArea.appendText("No Item Name Was Input.\n");
                        textArea.appendText(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
                    }
                break;
            case 2:
                    if(command.split(" ").length == 3)
                        textArea.appendText(currentPlayer.getInventory().useItemByName(command.split(" ")[2], currentPlayer, textArea));
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
                        textArea.appendText(currentPlayer.getInventory().useItemByName(name, currentPlayer, textArea) + "\n");
                    }       
                    else
                    {
                        textArea.appendText("No Item Name Was Input.\n");
                        textArea.appendText(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
                    }
                break;
            case 3:
                    if(command.split(" ").length == 3)
                        currentPlayer.getInventory().removeItemByName(command.split(" ")[2],currentPlayer, textArea);
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
                        currentPlayer.getInventory().removeItemByName(name,currentPlayer, textArea);
                    }
                    else
                    {
                        textArea.appendText("No Item Name Was Input.\n");
                        textArea.appendText(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
                    }
                break;
            case 4:
                    if(command.split(" ").length == 3)
                         textArea.appendText(currentPlayer.getInventory().equipItemByName(command.split(" ")[2], currentPlayer));
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
                        textArea.appendText(currentPlayer.getInventory().equipItemByName(name, currentPlayer));
                    } 
                    else
                    {
                        textArea.appendText("No Item Name Was Input.\n");
                        textArea.appendText(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
                    }
                break;
            case 5:
                    if(command.split(" ").length == 3)
                         textArea.appendText(currentPlayer.getInventory().unequipItemByName(command.split(" ")[2], currentPlayer));
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
                        textArea.appendText(currentPlayer.getInventory().unequipItemByName(name, currentPlayer));
                    } 
                    else
                    {
                        textArea.appendText("No Item Name Was Input.\n");
                        textArea.appendText(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
                    }
                break;    
            case 6:
                    currentPlayer.getInventory().checkEquipedItems(currentPlayer, textArea);
                break;
            default:
                textArea.appendText(String.format("%s is not a valid sub-command of inventory.\n", subCommand));
                textArea.appendText(new HELP_COMMAND("HELP INVENTORY", true).printOutput()); 
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
                    textArea.appendText(String.format("%s:\n\t%s\n%s\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).searchArea()));
                }
                else
                     textArea.appendText("There is no path in that direction.\n");
                break;
            case 1:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getEast() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getEast().getTransitFromWest().equalsIgnoreCase("Block")))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getEast().getGridID());
                    textArea.appendText(String.format("%s:\n\t%s\n%s\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).searchArea()));
                }
                else
                     textArea.appendText("There is no path in that direction.\n");
                break;
            case 2:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouth() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouth().getTransitFromNorth().equalsIgnoreCase("Block")))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouth().getGridID());
                    textArea.appendText(String.format("%s:\n\t%s\n%s\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).searchArea()));
                }
                else
                     textArea.appendText("There is no path in that direction.\n");
                break;
            case 3:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getWest() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getWest().getTransitFromEast().equalsIgnoreCase("Block")))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getWest().getGridID());
                    textArea.appendText(String.format("%s:\n\t%s\n%s\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).searchArea()));
                }
                else
                     textArea.appendText("There is no path in that direction.\n");
                break;
            case 4:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthEast() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthEast().getTransitFromSouthWest()).equalsIgnoreCase("Block"))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthEast().getGridID());
                    textArea.appendText(String.format("%s:\n\t%s\n%s\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).searchArea()));
                }
                else
                     textArea.appendText("There is no path in that direction.\n");
                break;
            case 5:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthWest() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthWest().getTransitFromSouthEast()).equalsIgnoreCase("Block"))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getNorthWest().getGridID());
                    textArea.appendText(String.format("%s:\n\t%s\n%s\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).searchArea()));
                }
                else
                     textArea.appendText("There is no path in that direction.\n");
                break;
            case 6:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthEast() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthEast().getTransitFromNorthWest()).equalsIgnoreCase("Block"))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthEast().getGridID());
                    textArea.appendText(String.format("%s:\n\t%s\n%s\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).searchArea()));
                }
                else
                     textArea.appendText("There is no path in that direction.\n");
                break;
            case 7:
                if(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthWest() != null &&
                        !(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthWest().getTransitFromNorthEast()).equalsIgnoreCase("Block"))
                {
                    currentPlayer.setPlayerLocation(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getSouthWest().getGridID());
                    textArea.appendText(String.format("%s:\n\t%s\n%s\n",
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getLocationName(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDetails(),
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).searchArea()));
                }
                else
                     textArea.appendText("There is no path in that direction.\n");
                break;
            default:
                textArea.appendText(String.format("%s is not a valid sub-command of Move.\n", subCommand));
                textArea.appendText(new HELP_COMMAND("HELP MOVE", true).printOutput()); 
        }
    }
    
    private static void attack(String command)
    {
        boolean playerdead = false;
        String subCommand = "";
        for(int indexName = 1; indexName < command.split(" ").length; indexName++)
        {
            if(indexName == 1)
                subCommand += command.split(" ")[indexName];
            else
                subCommand += " " + command.split(" ")[indexName];
        }
        subCommand = subCommand.toUpperCase();
        
        Creature [] currentCreatures = ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getCreatures();
        Creature currentCreature = null;
        if(currentCreatures[currentPlayer.getTarget()] != null && !(currentCreatures[currentPlayer.getTarget()].isIsdead()) && currentCreatures[currentPlayer.getTarget()].getName().equalsIgnoreCase(subCommand))
        {
            currentCreature = currentCreatures[currentPlayer.getTarget()];
        }
        else
        {
            currentCreature = null;
            for(int indexCreatures = 0; indexCreatures < currentCreatures.length; indexCreatures++)
            {
                if(currentCreatures[currentPlayer.getTarget()] != null && currentCreatures[indexCreatures].getName().equalsIgnoreCase(subCommand) && !(currentCreatures[indexCreatures].isIsdead()))
                {
                    currentPlayer.setTarget(indexCreatures);
                    currentCreature = currentCreatures[indexCreatures];
                    break;
                }
            }
        }
        if(currentCreature != null &&  currentCreature.getName().equalsIgnoreCase(subCommand) && !currentCreature.isIsdead())
        {
            int hit = (int)(Math.random()*1 + (.7) + ((.1)*(currentCreature.getLevel() - currentPlayer.getPlayerLevel())));
            if(hit == 1)
            {
                currentPlayer.takeDamage(currentCreature.getAttack(), textArea, INPUT, WORLD);
                if(currentPlayer.getPlayerHealth() > 0 )
                    textArea.appendText(String.format("Your Health %d/%d\n", currentPlayer.getPlayerHealth(),currentPlayer.getPlayerMaxHealth()));
                else
                    playerdead = true;  
            }
            else
                textArea.appendText(String.format("The %s missed.\n", currentCreature.getName()));
            int hit2 = (int)(Math.random()*1 + (.5) + ((.1)*(currentPlayer.getPlayerLevel() - currentCreature.getLevel())));
            if(hit2 == 1 && !playerdead)
            {
                if(currentPlayer.getPlayerEquipment()[10] != null)
                {
                    currentCreature.setHealth(currentCreature.getHealth() - 
                        currentPlayer.getPlayerEquipment()[10].getDamage((currentPlayer.getPlayerStats()[0]) + (currentPlayer.getPlayerBuffs()[0])), 
                        currentPlayer, ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))), WORLD, textArea);
                }
                else
                {
                    currentCreature.setHealth(currentCreature.getHealth() - ((currentPlayer.getPlayerStats()[0]) + (currentPlayer.getPlayerBuffs()[0]))/3, 
                        currentPlayer, ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))), WORLD, textArea);
                }
                if(currentCreature.getHealth() <= 0)
                {
                    currentPlayer.gainExperience(currentCreature.getXp(), textArea);
                }
                else
                    textArea.appendText(String.format("Creature Health %d/%d\n", currentCreature.getHealth(),currentCreature.getMaxHealth()));
            }
            else if(playerdead)
            {
                playerdead = false;
            }
            else
                textArea.appendText(String.format("You missed the %s\n", currentCreature.getName()));
        }
        else
        {
            textArea.appendText(String.format("There is no creature by the name %s in this area.\n", subCommand));
        }
    }
    
    private static void linkGrids() throws Exception
    {
        File connectionsFile = new File(LOADSAVE + "Connections.txt"); 
        BufferedReader textBuffer = new BufferedReader(new FileReader(connectionsFile)); 
        String currentLine = "";
        while (textBuffer.ready()) 
        {
            currentLine += textBuffer.readLine() + "#";
        }
        String [] lineBreakDown = currentLine.split("#");
        for(int connectionsIndex = 0; connectionsIndex < lineBreakDown.length; connectionsIndex++)
        {
            String [] connectionLinks = lineBreakDown[connectionsIndex].split(" ");
            if(Integer.parseInt(connectionLinks[1]) != -1)
                ((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[0])))).setNorth(((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[1])))));
            if(Integer.parseInt(connectionLinks[2]) != -1)
                ((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[0])))).setEast(((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[2])))));
            if(Integer.parseInt(connectionLinks[3]) != -1)
                ((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[0])))).setSouth(((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[3])))));
            if(Integer.parseInt(connectionLinks[4]) != -1)
                ((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[0])))).setWest(((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[4])))));
           if(Integer.parseInt(connectionLinks[5]) != -1)
                ((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[0])))).setNorthEast(((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[5])))));
           if(Integer.parseInt(connectionLinks[6]) != -1)
                ((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[0])))).setNorthWest(((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[6])))));
           if(Integer.parseInt(connectionLinks[7]) != -1)
                ((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[0])))).setSouthEast(((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[7])))));
           if(Integer.parseInt(connectionLinks[8]) != -1)
                ((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[0])))).setSouthWest(((Grid)(WORLD.getGameGrids().get(Integer.parseInt(connectionLinks[8])))));
        }
    }
    
    private static void loot(String command) throws Exception
    {
        Map lootCommands = new HashMap();
        lootCommands.put("SEARCH", 0);
        lootCommands.put("PICKUP", 1);
        String subCommand;
        int subCommandIndex = 25;
        
        subCommand = command.split(" ")[1].toUpperCase();
        if(lootCommands.containsKey(subCommand))
            subCommandIndex = (int)(lootCommands.get(subCommand));
        switch(subCommandIndex)
        {
            case 0:
                    Item drops[] = ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).getDrops();
                    if(drops.length >= 1)
                    {
                        textArea.appendText("Drops:\n");
                        for(int indexLoot = 0; indexLoot < drops.length; indexLoot++)
                            textArea.appendText(String.format("\t%s\n", drops[indexLoot].getItemName()));
                    }
                    else
                        textArea.appendText("There is no items on the ground.\n");
                break;
            case 1:
                    if(command.split(" ").length == 3)
                    {
                        Item drop = ((Item)(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).pickUpItem(command.split(" ")[2])));
                        if(drop != null)
                        {
                            if(!(drop instanceof StackableItem))
                                currentPlayer.getInventory().addItem(drop);
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
                                currentPlayer.getInventory().addItem(newItem);
                            }
                            textArea.appendText(String.format("Item %s added to your inventory.\n" , drop.getItemName()));
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).removeItem(command.split(" ")[2]);
                        }
                        else
                            textArea.appendText("There is not item of that name of the ground.\n");
                    }
                    else if(command.split(" ").length > 3)
                    {
                        String name = "";
                        for(int indexName = 2; indexName < command.split(" ").length; indexName++)
                        {
                            if(indexName == 2)
                                name += command.split(" ")[indexName];
                            else
                                name += " " + command.split(" ")[indexName];
                        }
                        Item drop = ((Item)(((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).pickUpItem(name)));
                        if(drop != null)
                        {
                            if(!(drop instanceof StackableItem))
                            {
                                currentPlayer.getInventory().addItem(drop);
                            }
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
                                currentPlayer.getInventory().addItem(newItem);
                            }
                            textArea.appendText(String.format("Item %s added to your inventory.\n" , drop.getItemName()));
                            ((Grid)(WORLD.getGameGrids().get(currentPlayer.getPlayerLocation()))).removeItem(name);
                        }
                        else
                            textArea.appendText("There is not item of that name of the ground.\n");
                    }
                    else
                        textArea.appendText("You must put in am item name\n");
                break;
            default:
                textArea.appendText(String.format("%s is not a valid sub-command of Loot.\n", subCommand));
                textArea.appendText(new HELP_COMMAND("HELP LOOT", true).printOutput()); 
        }
    }
    
    private static void addToLastText(String command)
    {
        boolean existText = false;
        for(int indexLastText = 0; indexLastText < lastText.length; indexLastText++)
        {
            if(lastText[indexLastText].equalsIgnoreCase(command))
                existText = true;
        }
        if(!existText)
        {
            String []newLastText = new String[lastText.length + 1];
            for(int indexLastText = 0; indexLastText < lastText.length; indexLastText++)
                newLastText[indexLastText] = lastText[indexLastText];
            newLastText[lastText.length] = command;
            lastText = newLastText;
            currentLastTextIndex = lastText.length - 1;
        }
    }
}
