package redsgreens.Appleseed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
    private final AppleseedPlayerListener playerListener = new AppleseedPlayerListener(this);
//    private final AppleseedBlockListener blockListener = new AppleseedBlockListener(this);
    private final HashMap<Player, Boolean> debugees = new HashMap<Player, Boolean>();
    
    private static Appleseed Plugin;
    
    private static Random rand = new Random();
    
    // list of allowed tree types
    public static ArrayList<Material> treeTypes = new ArrayList<Material>();
    
    // hashmap of tree locations and types
    public static HashMap<Location, Material> treeLocations = new HashMap<Location, Material>();
    
    
    
    public void onEnable() {

    	Plugin = this;
    	
    	// add some types to the allowed list
    	treeTypes.add(Material.APPLE);
    	treeTypes.add(Material.GOLDEN_APPLE);
    	treeTypes.add(Material.COOKIE);
    	treeTypes.add(Material.SLIME_BALL);
    	treeTypes.add(Material.SULPHUR);
    	treeTypes.add(Material.CAKE);
    	
        // register our event
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Monitor, this);

        // start the timer
        processTrees();
        
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
    
    // loop through the list of trees and drop items around them, then schedule the next run
    private static void processTrees(){

    	int worldPercent = 33;
    	
    	if(treeLocations.size() != 0){
        	Set<Location> locations = treeLocations.keySet();
        	Iterator<Location> itr = locations.iterator();
        	while(itr.hasNext()){
        		Location loc = itr.next();
        		World world = loc.getWorld();
        		if(world.isChunkLoaded(world.getChunkAt(world.getBlockAt(loc)))){
            		if(isTree(loc)){
            			if(rand.nextInt((Integer)(100 / worldPercent)) == 0){
                			ItemStack is = new ItemStack(treeLocations.get(loc), 1);
                			loc.getWorld().dropItemNaturally(loc, is);
            			}
            		}
            		else
            			treeLocations.remove(loc);
        		}
        	}
        }

    	// reprocess the list every minute
		Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
		    public void run() {
		    	processTrees();
		    }
		}, 60*20);
    }
    // see if the given location is the root of a tree
    public static final boolean isTree(Location rootBlock)
    {
        final World world = rootBlock.getWorld();
        final int rootX = rootBlock.getBlockX();
        final int rootY = rootBlock.getBlockY();
        final int rootZ = rootBlock.getBlockZ();
        
        final int treeId = Material.LOG.getId();
        final int leafId = Material.LEAVES.getId();
        
        final int maxY = 7;
        final int radius = 3;
        
        int treeCount = 0;
        int leafCount = 0;

        for (int y = rootY; y <= rootY+maxY; y++) {
            for (int x = rootX-radius; x <= rootX+radius; x++) {
                for (int z = rootZ-radius; z <= rootZ+radius; z++) {
                    final int blockId = world.getBlockTypeIdAt(x, y, z);
                    if(blockId == treeId) 
                    	treeCount++;
                    else if(blockId == leafId) 
                    	leafCount++;

                    if(treeCount >= 3 && leafCount >= 8)
                    	return true;
                    
                }
            }
        }

        return false;
    }

}

