package redsgreens.Appleseed;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * Handle onWorldLoad event
 * 
 * @author redsgreens
 */
public class AppleseedWorldListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
    public void onWorldLoad(WorldLoadEvent event)
	{
		// the trees for a world aren't loaded until the world itself is loaded
		String world = event.getWorld().getName();
		if(!Appleseed.TreeManager.isWorldLoaded(world))
			Appleseed.TreeManager.loadTrees(world);
	}
}
