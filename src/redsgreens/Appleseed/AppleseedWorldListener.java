package redsgreens.Appleseed;

import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;

public class AppleseedWorldListener extends WorldListener {

	@Override
    public void onWorldLoad(WorldLoadEvent event)
	{
		Appleseed.TreeManager.loadTrees(event.getWorld().getName());
	}
}
