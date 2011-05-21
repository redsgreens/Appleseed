package redsgreens.Appleseed;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class AppleseedPermissionsManager {

	private static PermissionHandler Permissions = null;
	
	public AppleseedPermissionsManager(){
    	try{
            Plugin test = Appleseed.Plugin.getServer().getPluginManager().getPlugin("Permissions");

            if (Permissions == null) {
                if (test != null) {
                    Permissions = ((Permissions)test).getHandler();
                }
            }
    	}
    	catch (Exception ex){
    		Permissions = null;
    	}
	}
	
	public Boolean hasPermission(Player player, String permission){
		// use op status if no permissions plugin is installed
		if(Permissions == null)
			return player.isOp();
		else
			return Permissions.has(player, "appleseed." + permission);
	}
}
