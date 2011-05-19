package redsgreens.Appleseed;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

/**
 * Handle events for all Player related events
 * @author redsgreens
 */
public class AppleseedPlayerListener extends PlayerListener {
//    private final Appleseed plugin;

    public AppleseedPlayerListener(Appleseed instance) {
//        plugin = instance;
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event)
    // catch player right-click events
    {
    	// return if the event is already cancelled, or if it's not a right-click event
		if(event.isCancelled() || event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

		Block block = event.getClickedBlock();
		
		// return if the player didn't right click on farmland
		if(block.getType() != Material.SOIL)
			return;

		// return if they don't have an allowed item in hand
		Player player = event.getPlayer();
		ItemStack iStack = player.getItemInHand();
		if(iStack == null)
			return;
		else if(!Appleseed.Config.AllowedTreeMaterials.contains(iStack.getType()))
			return;
		
		// return if the block above is not air
		Block blockRoot = block.getRelative(BlockFace.UP);
		if(blockRoot.getType() != Material.AIR)
			return;
		
		// all tests satisfied, proceed
		
		// cancel the event so we're the only one processing it
		event.setCancelled(true);
		
		// add the root location and type to the list of trees
		Appleseed.treeLocations.put(blockRoot.getLocation(), iStack.getType());
		
		// set the clicked block to dirt
		block.setType(Material.DIRT);
		
		// plant a sapling
		blockRoot.setType(Material.SAPLING);
		
		// take the item from the player
		if(iStack.getAmount() == 1)
			player.setItemInHand(null);
		else
		{
			iStack.setAmount(iStack.getAmount() - 1);
			player.setItemInHand(iStack);			
		}
		
    }
}

