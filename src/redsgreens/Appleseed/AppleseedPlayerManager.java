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
		// use op status if no permissions plugin is installed
		if(Permissions == null)
		{
			if(Appleseed.Config.AllowNonOpAccess == false)
				return player.isOp();
			else
			{
				if(player.isOp())
					return true;
				else if(permission.toLowerCase().substring(0, 6).equalsIgnoreCase("plant.") || permission.toLowerCase().substring(0, 5).equalsIgnoreCase("sign."))
					return true;
				else
					return false;
			}
		}
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
/*
	public Boolean CapAddTree(String player, String world)
	{
		return CapAddTree(player, world, false);
	}

	public Boolean CapAddTree(String player, String world, Boolean force)
	{
		if(Appleseed.Config.MaxTreesPerPlayer == -1)
			return true;
		
		String capStr;
		if(Appleseed.Config.MaxIsPerWorld)
			capStr = world + "_" + player;
		else
			capStr = player;
		
		if(capsHash.containsKey(capStr))
		{
			Integer x = capsHash.get(capStr);
			if(x < Appleseed.Config.MaxTreesPerPlayer || force == true)
			{
				capsHash.remove(capStr);
				capsHash.put(capStr, x+1);
				return true;
			}
			else
				return false;
		}
		else
		{
			capsHash.put(capStr, 1);
			return true;
		}
	}

	public void CapRemoveTree(AppleseedTreeData tree)
	{
		String capStr;
		if(Appleseed.Config.MaxIsPerWorld)
			capStr = tree.getWorld() + "_" + tree.getPlayer();
		else
			capStr = tree.getPlayer();

		if(capsHash.containsKey(capStr))
		{
			Integer x = capsHash.get(capStr);
			capsHash.remove(capStr);
			capsHash.put(capStr, x-1);
		}
	}
*/	
	
}
