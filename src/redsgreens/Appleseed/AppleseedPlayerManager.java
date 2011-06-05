package redsgreens.Appleseed;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class AppleseedPlayerManager {

	private PermissionHandler Permissions = null;
	private WorldGuardPlugin WorldGuard = null;

	public AppleseedPlayerManager()
	{
		// attempt to hook to the permissions plugin
    	try{
            Plugin test = Appleseed.Plugin.getServer().getPluginManager().getPlugin("Permissions");

            if (Permissions == null) {
                if (test != null) {
                    Permissions = ((Permissions)test).getHandler();
                	System.out.println("Appleseed: " + test.getDescription().getName() + " " + test.getDescription().getVersion() + " found");
                }
            }
    	}
    	catch (Exception ex){
    		Permissions = null;
    	}

		// attempt to hook to the worldguard plugin
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
	
	public Boolean hasPermission(Player player, String permission){
		// use op status if no permissions plugin is installed
		if(Permissions == null)
			return player.isOp();
		else
			return Permissions.has(player, "appleseed." + permission);
	}
	
	public Boolean canBuild(Player p, Block b)
	{
		if(WorldGuard != null)
			return WorldGuard.canBuild(p, b);
		else
			return true;
	}
}
