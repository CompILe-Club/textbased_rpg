import java.util.Map;
import java.util.HashMap;

public class HELP_COMMAND implements Command {

    //The full command entered by the user.
    private final String input;
    //The list of commands in the game.
    private final Map commands = new HashMap();

    private final boolean tooltips;
    
    HELP_COMMAND(String cmd, boolean tt){
        commands.put("STATUS", 0);
        commands.put("QUEST", 1);
        commands.put("HELP", 2);
        commands.put("INVENTORY", 4);
        commands.put("NPC", 6);
        commands.put("MOVE", 7);
        commands.put("ATTACK", 8);
        commands.put("SEARCH", 9);
        commands.put("SAVE", 10);
        commands.put("LOOT", 11);
        input = cmd;
        tooltips = tt;
    }
    
    @Override
    public String printOutput(){
        //See if the user just typed "HELP" or "HELP [command]"
        if (input.split(" ").length > 1){
            
            //Isolate the specific help command entered.
            String command = input.split(" ")[1];
            
            //Check if the command entered is a sub-command of HELP
            if (command.equalsIgnoreCase("TOOLTIPS")){
                return toggleTooltips();
            }else if (command.equalsIgnoreCase("LIST")){
                return listCommands();
            }
            
            //Processes sub commands if not list or tooltips
            return processCommand(command.toUpperCase());
            
        }else{
            return helpList();
        }
    }
    
    //This tells the player how to use the HELP command to find information on the game.
    private String helpList(){
        String help = "How to use HELP:\nUse HELP with another parameter to learn more about that command.\n"
                + "HELP LIST lists all of the commands the player can use.\n"
                + "HELP TOOLTIPS toggles the tooltips show/hide.\n"
                + "HELP [command] gives you help for that specific command.\n";
        return help;
    }
    
    private String processCommand(String subCommand){
        
        //Get the name of the command entered sop it can be used more easily later.
        String help;
        //Creates a key to use the switch
        int subCommandIndex = 25;
        //Checks to see if the sub command exist then sets the map value of that command
        if(commands.containsKey(subCommand))
            subCommandIndex = (int)(commands.get(subCommand));
        //This prints the usage directions on the specified command
        //NOTE: These descriptions will get more detailed once more of the game is done.
        switch(subCommandIndex){
            case 0://Status
                help = "STATUS prints some statistics about the player for them to read.\n" + 
                        "This includes their HP, Active Quest, and Level.\n";
                return help;
            case 1://Quest log
                help = "QUEST tells the player about their current quest.\n"
                        + "QUEST commands include\n"
                        + "QUEST LIST lists all of the current quest you have in your log.\n"
                        + "QUEST COMPLETED lists all of the quest you have completed.\n"
                        + "QUEST DETAILS [quest name] outputs the details about a given quest.\n";
                return help;
            case 2://Help
                return helpList();
            case 4://Inventory
                help = "INVENTORY displays the contents of the player's inventory, and lets them manage it.\n"
                        + "INVENTORY commands include\n"
                        + "INVENTORY LIST lists off all of the current items in your inventory.\n"
                        + "INVENTORY VIEW [item name] outpus the details about a given item.\n"
                        + "INVENTORY USE [item name] allows you to use an item if it is useable.\n"
                        + "INVENTORY DROP [item name] allows you to drop an item from your inventory.\n"
                        + "INVENTORY EQUIP [item name] allows you to equip an item that can be equipped.\n"
                        + "INVENTORY UNEQUIP [item name] allows you to unequip an item that is equipped.\n"
                        + "INVENTORY EQUIPMENT view current equiped items.\n";
                return help;
            case 6://NPC
                help = "NPC [action] lets the player interact with NPCs.\n" + 
                        "NPC TALK [Name] activates a dialogue sequence between the player and the NPC.\n" + 
                        "NPC SHOP [Name] let the player see the shop from the NPC, if the option is available.\n" +
                        "NPC QUEST [Name] activates a dialogue sequence between the player and the NPC for a quest.\n" +
                        "NPC BUY [Name]:[Item Name] lets the player buy from the NPC, if the option is available.\n" +
                        "NPC SELL [Name]:[Item Name] lets the player sell to the NPC, if the option is available.\n" ;
                return help;
            case 7://MOVE
                help = "MOVE [action] lets the player move in the direction they wish.\n" + 
                        "MOVE NORTH move to the NORTH if the option is available.\n" + 
                        "MOVE EAST move to the EAST if the option is available.\n" +
                        "MOVE SOUTH move to the SOUTH if the option is available.\n" +
                        "MOVE WEST move to the WEST if the option is available.\n" +
                        "MOVE NORTHEAST move to the NORTHEAST if the option is available.\n" +
                        "MOVE NORTHWEST move to the NORTHWEST if the option is available.\n" +
                        "MOVE SOUTHEAST move to the SOUTHEAST if the option is available.\n" +
                        "MOVE SOUTHWEST move to the SOUTHWEST if the option is available.\n";
                return help;
            case 8://ATTACK
                help = "ATTACK [Target] attacks your target based on its name.\n"
                        +"ATTACK attacks your set target based on its name or the first target listed in search\n";
                return help;
            case 9://SEARCH
                help = "SEARCH allows you to see what exists within your grid.";
                return help;
            case 10://SAVE
                help = "SAVE will save your current state in the world.\n";
                return help;
            case 11://LOOT
                help = "LOOT [action] lets the player do things with loot on the ground.\n" + 
                       "LOOT SEARCH look for loot on the ground.\n" +
                       "LOOT PICKUP [item name] get loot on the ground.\n";;
                return help;
            default:
                //Tell the user that the command they entered is invalid.
                help = subCommand + " is not a valid sub-command of help.\n" + helpList();
                return help;
        }
    }
    
    //This method toggles the showing of tooltips in the menu I think I don't actually know how these are supposed to work.
    private String toggleTooltips(){
        String tt;
        if (!tooltips == true){
            tt = "ON\n";
        }else{
            tt = "OFF\n";
        }
        return "TOOLTIPS " + tt;
    }
    
    //Iterate through the map and print all of the commands in the game.
    String listCommands()
    {
        return commands.keySet().toString() + "\n";
    }  
}
