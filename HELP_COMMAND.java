import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class HELP_COMMAND {

    //The full command entered by the user.
    String commandSource;
    //The list of commands in the game.
    Map commands = new HashMap();
    //I don't really know what these do but the project leader will save me
    boolean tooltips;
    
    HELP_COMMAND(String cmd, boolean tt){
        commandSource = cmd;
        tooltips = tt;
    }
    
    String help(){
        //See if the user just typed "HELP" or "HELP [command]"
        if (commandSource.split(" ").length > 1){
            
            //Isolate the specific help command entered.
            String command = commandSource.split(" ")[1];
            
            //Check if the command entered is a sub-command of HELP
            if (command.equals("TOOLTIPS")){
                return toggleTooltips();
            }else if (command.equals("LIST")){
                return listCommands();
            }
            
            //The key is the index of the command in the list of commands. 
            int key = (int) commands.get(command);
            return findCommand(key);
            
        }else{
            return helpList();
        }
    }
    
    //This tells the player how to use the HELP command to find information on the game.
    String helpList(){
        String help = "How to use HELP:\nUse HELP with another parameter to learn more about that command.\n"
                + "HELP LIST lists all of the commands the player can use.\n"
                + "HELP TOOLTIPS toggles the tooltips show/hide.\n"
                + "HELP [command] gives you help for that specific command.";
        return help;
    }
    
    String findCommand(int k){
        
        //Get the name of the command entered sop it can be used more easily later.
        String help;
        
        //This prints the usage directions on the specified command
        //NOTE: These descriptions will get more detailed once more of the game is done.
        switch(k){
            case 0://Status
                help = "STATUS prints some statistics about the player for them to read.\n" + 
                        "This includes their HP, MP, Active Quest, and Level.";
                return help;
            case 1://Quest log
                help = "QUEST tells the player about their current quest.";
                return help;
            case 2://Help
                return helpList();
            case 3://Exit game
                help = "EXIT exits the game, after prompting the player if they're sure.";
                return help;
            case 4://Inventory
                help = "INVENTORY displays the contents of the player's inventory, and lets them manage it.";
                return help;
            case 5://Stance
                help = "STANCE lets the player change their stance between ATTACK and DEFENSE.";
                return help;
            case 6://Cast
                help = "CAST [spell] casts the spell that the user invokes, if their character knows how to use it.";
                return help;
            case 7://Lock Target
                help = "LOCK [name of target] sets the player to aim at the specified target.";
                return help;
            case 8://NPC
                help = "NPC [action] lets the player interact with NPCs.\n" + 
                        "NPC TALK activates a dialogue sequence between the player and the NPC.\n" + 
                        "NPC SHOP lets the player buy from the NPC, if the option is available.";
                return help;
            case 9://Player
                help = "TODO WHEN MULTIPLAYER";
                return help;
            default:
                //Tell the user that the command they entered is invalid.
                help = "" + commands.get(k) + " is not a valid command.";
                return help;
        }
    }
    
    //This method toggles the showing of tooltips in the menu I think I don't actually know how these are supposed to work.
    String toggleTooltips(){
        String tt;
        if (!tooltips == true){
            tt = "ON";
        }else{
            tt = "OFF";
        }
        return "TOOLTIPS " + tt;
    }
    
    //Iterate through the map and print all of the commands in the game.
    String listCommands(){
        String list = "";
        for (Iterator it = commands.keySet().iterator(); it.hasNext();) {
            list += commands.get(it) + " ";
            Object key = it.next();
        }
        return list;
    }

    
}
