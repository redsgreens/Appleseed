package redsgreens.Appleseed;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class AppleseedTreeData {

	private Location location;
	private ItemStack itemStack;
	private String player;
	private Integer dropCount;

	private static Random rand = new Random();

	public AppleseedTreeData(Location loc, ItemStack is, String p)
	{
		location = loc;
		itemStack = is;
		player = p;
		
		Integer dcMin = (int) (Appleseed.Config.DropsBeforeTired - (0.3 * Appleseed.Config.DropsBeforeTired));
		Integer dcMax = (int) (Appleseed.Config.DropsBeforeTired + (0.3 * Appleseed.Config.DropsBeforeTired));
		dropCount = rand.nextInt(dcMax - dcMin + 1) + dcMin;
	}

	public AppleseedTreeData(Location loc, ItemStack is, Integer dc, String p)
	{
		location = loc;
		itemStack = is;
		player = p;
		dropCount = dc;
	}

	public Location getLocation()
	{
		return location;
	}
	
	public ItemStack getItemStack()
	{
		return itemStack;
	}
	
	public String getPlayer()
	{
		return player;
	}
	
	public Integer getDropCount()
	{
		return dropCount;
	}
	
	public void setDropCount(Integer dc)
	{
		dropCount = dc;
	}
}
