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

//	private HashMap<String, Integer> capsHash = new HashMap<String, Integer>();
	
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
    	boolean isOp = player.isOp();
    	if(Permissions == null && isOp == false)
    	{
    		if(Appleseed.Config.AllowNonOpAccess == true)
    		{
    			
    			if(permission.length() >= 5 && permission.toLowerCase().substring(0, 5).equalsIgnoreCase("sign."))
					return true;
    			if (permission.length() >= 6 && permission.toLowerCase().substring(0, 6).equalsIgnoreCase("plant."))
    					return true;
    		}
    		
    		try
    		{
    			Boolean retval = player.hasPermission("appleseed." + permission); 
    			
    			if(retval == false && permission.length() >= 6 && permission.toLowerCase().substring(0, 6).equalsIgnoreCase("plant."))
    				retval = player.hasPermission("appleseed.plant.*");

    			if(retval == false)
    				retval = player.hasPermission("appleseed.*");
    			
    			return retval;
    		}
    		catch (Exception ex){}
    	}
    	else
    	{
        	try{
        		if(Permissions != null)
        			  return Permissions.has(player, "appleseed.");
        	}
        	catch (Exception ex){}
    	}

    	return isOp;	
	}
	
	public Boolean canBuild(Player p, Block b)
	{
		if(WorldGuard != null)
			return WorldGuard.canBuild(p, b);
		else
			return true;
	}

	
}
