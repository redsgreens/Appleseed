package redsgreens.Appleseed;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;


/**
 * Appleseed for Bukkit
 *
 * @author redsgreens
 */
public class Appleseed extends JavaPlugin {
    private final AppleseedPlayerListener playerListener = new AppleseedPlayerListener();
    private final AppleseedBlockListener blockListener = new AppleseedBlockListener();
    private final AppleseedWorldListener worldListener = new AppleseedWorldListener();
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();

    private static HashMap<String, ItemStack> itemStackNames = new HashMap<String, ItemStack>();
    
    public static Appleseed Plugin;
    public static AppleseedConfig Config;
    public static AppleseedPermissionsManager Permissions;
    public static AppleseedTreeManager TreeManager;
    public static AppleseedCanBuild CanBuild;
    
    public void onEnable() {

    	Plugin = this;

    	// build hashmap of itemstacks for items with durability values
    	setupItemStackNames();

    	// initialize the config object and load the config 
    	Config = new AppleseedConfig();
    	Config.LoadConfig();
    	
    	// initialize the permissions handler
    	Permissions = new AppleseedPermissionsManager();
    	
    	// load the worldguard handler
    	CanBuild = new AppleseedCanBuild();
    	
    	
    	// initialize the tree manager
    	TreeManager = new AppleseedTreeManager();
    	
        // register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Monitor, this);
        pm.registerEvent(Type.SIGN_CHANGE, blockListener, Priority.Monitor, this);
        pm.registerEvent(Type.BLOCK_BREAK, blockListener, Priority.Monitor, this);
        pm.registerEvent(Type.WORLD_LOAD, worldListener, Priority.Monitor, this);

        // start the timer
        TreeManager.ProcessTrees();
        
        System.out.println( getDescription().getName() + " version " + getDescription().getVersion() + " is enabled!" );
    }

    public void onDisable() {
    	if(!AppleseedTreeManager.SaveRunning)
    		TreeManager.saveTrees();
        System.out.println( getDescription().getName() + " version " + getDescription().getVersion() + " is disabled." );
    }

    public boolean isDebugging(final Player player) {
        if (debugees.containsKey(player)) {
            return debugees.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        debugees.put(player, value);
    }
    
    public static String getItemStackName(ItemStack iStack)
    {
    	Short durability = iStack.getDurability();
    	
    	if(durability == 0)
    		return iStack.getType().name().toLowerCase();
    	else
    	{
    		Material material = iStack.getType();
    		switch(material)
    		{
    		case INK_SACK:
    			switch(durability)
    			{
	    			case 0:  return "ink_sack";
	    			case 1:  return "rose_red";
	    			case 2:  return "cactus_green";
	    			case 3:  return "cocoa_beans";
	    			case 4:  return "lapis_lazuli";
	    			case 5:  return "purple_dye";
	    			case 6:  return "cyan_dye";
	    			case 7:  return "lightgray_dye";
	    			case 8:  return "gray_dye";
	    			case 9:  return "pink_dye";
	    			case 10: return "lime_dye";
	    			case 11: return "yellow_dye";
	    			case 12: return "lightblue_dye";
	    			case 13: return "magenta_dye";
	    			case 14: return "orange_dye";
	    			case 15: return "bone_meal";
    			}
    			break;
    			
    		case WOOL:
    			switch(durability)
    			{
	    			case 0:  return "white_wool";
	    			case 1:  return "orange_wool";
	    			case 2:  return "magenta_wool";
	    			case 3:  return "lightblue_wool";
	    			case 4:  return "yellow_wool";
	    			case 5:  return "lightgreen_wool";
	    			case 6:  return "pink_wool";
	    			case 7:  return "gray_wool";
	    			case 8:  return "lightgray_wool";
	    			case 9:  return "cyan_wool";
	    			case 10: return "purple_wool";
	    			case 11: return "blue_wool";
	    			case 12: return "brown_wool";
	    			case 13: return "darkgreen_wool";
	    			case 14: return "red_wool";
	    			case 15: return "black_wool";
    			}
    			break;
    			
    		case LOG:
    			switch(durability)
    			{
	    			case 0: return "log";
	    			case 1: return "spruce_log";
	    			case 2: return "birch_log";
    			}
    			break;
    			
    		case SAPLING:
    			switch(durability)
    			{
	    			case 0: return "sapling";
	    			case 1: return "spruce_sapling";
	    			case 2: return "birch_sapling";
    			}
    			break;
    		}
    	}
    	
    	return null;
    }

    public static ItemStack getItemStackFromName(String name)
    {
    	Material material = Material.matchMaterial(name);
    	if(material != null)
    		return new ItemStack(material, 1);

    	if(itemStackNames.containsKey(name))
    		return itemStackNames.get(name);

    	return null;
    }

    private void setupItemStackNames()
    {
    	itemStackNames.put("rose_red", new ItemStack(Material.INK_SACK, 1, (short)1));
    	itemStackNames.put("cactus_green", new ItemStack(Material.INK_SACK, 1, (short)2));
    	itemStackNames.put("cocoa_beans", new ItemStack(Material.INK_SACK, 1, (short)3));
    	itemStackNames.put("lapis_lazuli", new ItemStack(Material.INK_SACK, 1, (short)4));
    	itemStackNames.put("purple_dye", new ItemStack(Material.INK_SACK, 1, (short)5));
    	itemStackNames.put("cyan_dye", new ItemStack(Material.INK_SACK, 1, (short)6));
    	itemStackNames.put("lightgray_dye", new ItemStack(Material.INK_SACK, 1, (short)7));
    	itemStackNames.put("gray_dye", new ItemStack(Material.INK_SACK, 1, (short)8));
    	itemStackNames.put("pink_dye", new ItemStack(Material.INK_SACK, 1, (short)9));
    	itemStackNames.put("lime_dye", new ItemStack(Material.INK_SACK, 1, (short)10));
    	itemStackNames.put("yellow_dye", new ItemStack(Material.INK_SACK, 1, (short)11));
    	itemStackNames.put("lightblue_dye", new ItemStack(Material.INK_SACK, 1, (short)12));
    	itemStackNames.put("magenta_dye", new ItemStack(Material.INK_SACK, 1, (short)13));
    	itemStackNames.put("orange_dye", new ItemStack(Material.INK_SACK, 1, (short)14));
    	itemStackNames.put("bone_meal", new ItemStack(Material.INK_SACK, 1, (short)15));
     	itemStackNames.put("orange_wool", new ItemStack(Material.WOOL, 1, (short)1));
     	itemStackNames.put("magenta_wool", new ItemStack(Material.WOOL, 1, (short)2));
     	itemStackNames.put("lightblue_wool", new ItemStack(Material.WOOL, 1, (short)3));
     	itemStackNames.put("yellow_wool", new ItemStack(Material.WOOL, 1, (short)4));
     	itemStackNames.put("lightgreen_wool", new ItemStack(Material.WOOL, 1, (short)5));
     	itemStackNames.put("pink_wool", new ItemStack(Material.WOOL, 1, (short)6));
     	itemStackNames.put("gray_wool", new ItemStack(Material.WOOL, 1, (short)7));
     	itemStackNames.put("lightgray_wool", new ItemStack(Material.WOOL, 1, (short)8));
     	itemStackNames.put("cyan_wool", new ItemStack(Material.WOOL, 1, (short)9));
     	itemStackNames.put("purple_wool", new ItemStack(Material.WOOL, 1, (short)10));
     	itemStackNames.put("blue_wool", new ItemStack(Material.WOOL, 1, (short)11));
     	itemStackNames.put("brown_wool", new ItemStack(Material.WOOL, 1, (short)12));
     	itemStackNames.put("darkgreen_wool", new ItemStack(Material.WOOL, 1, (short)13));
     	itemStackNames.put("red_wool", new ItemStack(Material.WOOL, 1, (short)14));
     	itemStackNames.put("black_wool", new ItemStack(Material.WOOL, 1, (short)15));
     	itemStackNames.put("spruce_log", new ItemStack(Material.LOG, 1, (short)1));
     	itemStackNames.put("birch_log", new ItemStack(Material.LOG, 1, (short)2));
     	itemStackNames.put("spruce_sapling", new ItemStack(Material.SAPLING, 1, (short)1));
     	itemStackNames.put("birch_sapling", new ItemStack(Material.LOG, 1, (short)2));
    }
}

