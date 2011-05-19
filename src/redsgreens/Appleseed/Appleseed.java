package redsgreens.Appleseed;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;


/**
 * Appleseed for Bukkit
 *
 * @author redsgreens
 */
public class Appleseed extends JavaPlugin {
    private final AppleseedPlayerListener playerListener = new AppleseedPlayerListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    
    private static Appleseed Plugin;
    public static AppleseedConfig Config;
    public static AppleseedPermissionsManager Permissions;
    public static AppleseedTreeManager TreeManager;
    
    public void onEnable() {

    	Plugin = this;
    	
    	// initialize the config object and load the config 
    	Config = new AppleseedConfig(Plugin);
    	Config.LoadConfig();
    	
    	// initialize the permissions handler
    	Permissions = new AppleseedPermissionsManager(Plugin);
    	
    	// initialize the tree manager
    	TreeManager = new AppleseedTreeManager(Plugin);
    	
        // register our event
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Monitor, this);

        // start the timer
        TreeManager.ProcessTrees();
        
        System.out.println( getDescription().getName() + " version " + getDescription().getVersion() + " is enabled!" );
    }

    public void onDisable() {
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

