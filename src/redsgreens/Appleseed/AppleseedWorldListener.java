package redsgreens.Appleseed;

import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;

/**
 * Handle onWorldLoad event
 * 
 * @author redsgreens
 */
public class AppleseedWorldListener extends WorldListener {

	@Override
    public void onWorldLoad(WorldLoadEvent event)
	{
		// the trees for a world aren't loaded until the world itself is loaded 
		Appleseed.TreeManager.loadTrees(event.getWorld().getName());
	}
}
