package redsgreens.Appleseed;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

/**
 * AppleseedCanBuild hooks to WorldGuard to enforce region protection
 *
 * @author redsgreens
 */

public class AppleseedCanBuild {

	private WorldGuardPlugin WorldGuard = null;
	
	public AppleseedCanBuild()
	{
    	try{
            Plugin test = Appleseed.Plugin.getServer().getPluginManager().getPlugin("WorldGuard");

            if (WorldGuard == null) {
                if (test != null) {
                	WorldGuard = (WorldGuardPlugin)test;
                	System.out.println("Appleseed: " + WorldGuard.getDescription().getName() + " " + WorldGuard.getDescription().getVersion() + " found");
                }
            }
    	}
    	catch (Exception ex){
    		WorldGuard = null;
    	}
	}
	
	public Boolean canBuild(Player p, Block b)
	{
		if(WorldGuard != null)
			return WorldGuard.canBuild(p, b);
		else
			return true;
	}
}
