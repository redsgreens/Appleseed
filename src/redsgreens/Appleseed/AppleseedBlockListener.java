package redsgreens.Appleseed;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class AppleseedBlockListener extends BlockListener {

	@Override
	public void onSignChange(SignChangeEvent event)
	{
		// return if the event is already cancelled
		if (event.isCancelled()) return;

		Block signBlock = event.getBlock();

		// only wall signs on trees supported
		if(signBlock.getType() != Material.WALL_SIGN)
			return;
		
		// get the block behind the sign
		Block blockAgainst =  getBlockBehindWallSign(new CraftSign(signBlock));
		
		// the sign must be on a tree
		if(blockAgainst.getType() != Material.LOG)
			return;
		
		// only proceed if it's a new sign
		if (event.getLine(0).equalsIgnoreCase("[Appleseed]"))
		{
			Player player = event.getPlayer();
			
			if(!Appleseed.Permissions.hasPermission(player, "sign.place"))
			{
				if(Appleseed.Config.ShowErrorsInClient)
					player.sendMessage("§cErr: You don't have permission to place this sign.");
				return;
			}

			// cancel the event so we're the only one processing it
			event.setCancelled(true);

			// set the first line to blue
			event.setLine(0, "§1[Appleseed]");

			// TODO: make the sign do something
		}
	}
	
	
	// get the block that has a wall sign on it
	public  Block getBlockBehindWallSign(Sign sign)
	{
		Block blockAgainst = null;
		Block signBlock = sign.getBlock();
		
		if(sign.getType() == Material.WALL_SIGN)
		{
			switch(signBlock.getData()){ // determine sign direction and get block behind it
			case 2: // facing east
				blockAgainst = signBlock.getFace(BlockFace.WEST);
				break;
			case 3: // facing west
				blockAgainst = signBlock.getFace(BlockFace.EAST);
				break;
			case 4: // facing north
				blockAgainst = signBlock.getFace(BlockFace.SOUTH);
				break;
			case 5: // facing south
				blockAgainst = signBlock.getFace(BlockFace.NORTH);
				break;
			}
		}
		
		return blockAgainst;
	}
	

}
