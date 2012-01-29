package redsgreens.Appleseed;

import java.util.HashMap;
import org.bukkit.entity.Player;
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

    
    public static Appleseed Plugin;
    public static AppleseedConfig Config;
    public static AppleseedPlayerManager PlayerManager;
    public static AppleseedTreeManager TreeManager;
    
    public void onEnable() {

    	Plugin = this;

    	// initialize the config object and load the config 
    	Config = new AppleseedConfig();
    	Config.LoadConfig();
    	
    	// initialize the player manager
    	PlayerManager = new AppleseedPlayerManager();
    	
    	// initialize the tree manager
    	TreeManager = new AppleseedTreeManager();
    	
        // register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(worldListener, this);

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
    

}

