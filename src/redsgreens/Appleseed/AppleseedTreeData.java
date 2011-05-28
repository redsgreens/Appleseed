package redsgreens.Appleseed;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

/**
 * AppleseedTreeData stores the data about a real tree in game
 *
 * @author redsgreens
 */
public class AppleseedTreeData {

	private AppleseedLocation location;
	private AppleseedItemStack itemStack;
	private String player;
	private Integer dropCount;
	private Integer fertilizerCount;
	private Boolean hasSign;
	private AppleseedLocation signLocation;
	
	private static Random rand = new Random();

	public AppleseedTreeData(AppleseedLocation loc, AppleseedItemStack is, String p)
	{
		location = new AppleseedLocation(loc.getWorldName(), loc.getX(), loc.getY(), loc.getZ());
		itemStack = is;
		player = p;
		hasSign = false;
		signLocation = null;

    	AppleseedTreeType treeType = Appleseed.Config.TreeTypes.get(is);
    	Integer fertilizer = treeType.getMaxFertilizer();
    	if(fertilizer == -1)
    		fertilizerCount = -1;
    	else
    	{
    		Integer fcMin = (int) (fertilizer - (0.3 * fertilizer));
    		Integer fcMax = (int) (fertilizer + (0.3 * fertilizer));
    		fertilizerCount = rand.nextInt(fcMax - fcMin + 1) + fcMin;
    	}
    	
		ResetDropCount();		
	}

	public AppleseedTreeData(AppleseedLocation loc, AppleseedItemStack is, Integer dc, Integer fc, String p)
	{
		location = new AppleseedLocation(loc.getWorldName(), loc.getX(), loc.getY(), loc.getZ());
		itemStack = is;
		player = p;
		dropCount = dc;
		fertilizerCount = fc;
		hasSign = false;
		signLocation = null;
	}

	public AppleseedTreeData(String world, Double x, Double y, Double z, AppleseedItemStack is, Integer dc, Integer fc, String p)
	{
		location = new AppleseedLocation(world, x, y, z);
		itemStack = is;
		player = p;
		dropCount = dc;
		fertilizerCount = fc;
		hasSign = false;
		signLocation = null;
	}

	public AppleseedTreeData(String world, Double x, Double y, Double z, AppleseedItemStack is, Integer dc, Integer fc, String p, Double sx, Double sy, Double sz)
	{
		location = new AppleseedLocation(world, x, y, z);
		itemStack = is;
		player = p;
		dropCount = dc;
		fertilizerCount = fc;
		hasSign = true;
		signLocation = new AppleseedLocation(world, sx, sy, sz);
	}

	// take a hashmap and make a tree from it
	public static AppleseedTreeData LoadFromHash(HashMap<String, Object> loadData)
	{
		if(!loadData.containsKey("world") || !loadData.containsKey("x") || !loadData.containsKey("y") || !loadData.containsKey("z") || !loadData.containsKey("itemid"))
			return null;
		
		World world = Appleseed.Plugin.getServer().getWorld((String)loadData.get("world"));
		if(world == null)
			return null;
		
    	String player;
		if(loadData.containsKey("player"))
			player = (String)loadData.get("player");
		else
			player = "unknown";

		Integer dc;
		if(loadData.containsKey("dropcount"))
			dc = (Integer)loadData.get("dropcount");
		else
			dc = -1;

		Integer fc;
		if(loadData.containsKey("fertilizercount"))
			fc = (Integer)loadData.get("fertilizercount");
		else
			fc = -1;

		AppleseedItemStack iStack;
		if(loadData.containsKey("durability"))
    		iStack = new AppleseedItemStack(Material.getMaterial((Integer)loadData.get("itemid")), ((Integer)loadData.get("durability")).shortValue()); 
    	else
    		iStack = new AppleseedItemStack(Material.getMaterial((Integer)loadData.get("itemid")));
		
		Boolean sign = false;
		Double signx = null;
		Double signy = null;
		Double signz = null;
		if(loadData.containsKey("sign"))
		{
			sign = (Boolean)loadData.get("sign");
			if(sign == true)
			{
				if(loadData.containsKey("signx") && loadData.containsKey("signy") && loadData.containsKey("signz"))
				{
					signx = (Double)loadData.get("signx");
					signy = (Double)loadData.get("signy");
					signz = (Double)loadData.get("signz");
				}
				else
					sign = false;
			}
		}

		if(sign)
			return new AppleseedTreeData(world.getName(), (Double)loadData.get("x"), (Double)loadData.get("y"), (Double)loadData.get("z"), iStack, dc, fc, player, signx, signy, signz);
		else
			return new AppleseedTreeData(world.getName(), (Double)loadData.get("x"), (Double)loadData.get("y"), (Double)loadData.get("z"), iStack, dc, fc, player);
	}
	
    // take a tree location and item and return a hash for saving to disk
    public HashMap<String, Object> MakeHashFromTree()
    {
    	HashMap<String, Object> treeHash = new HashMap<String, Object>();
    	
    	treeHash.put("world", location.getWorldName());
    	treeHash.put("x", location.getX());
    	treeHash.put("y", location.getY());
    	treeHash.put("z", location.getZ());
    	
    	treeHash.put("itemid", itemStack.getMaterial().getId());
    	if(itemStack.getDurability() != 0)
    		treeHash.put("durability", itemStack.getDurability());

    	treeHash.put("player", player);
    	treeHash.put("dropcount", dropCount);
    	treeHash.put("fertilizercount", fertilizerCount);

    	if(hasSign)
    	{
        	treeHash.put("sign", true);
        	treeHash.put("signx", signLocation.getX());
        	treeHash.put("signy", signLocation.getY());
        	treeHash.put("signz", signLocation.getZ());
    	}
    	
    	return treeHash;
    }
	
    public void ResetDropCount()
    {
    	AppleseedTreeType treeType = Appleseed.Config.TreeTypes.get(itemStack);
    	Integer drops = treeType.getDropsBeforeFertilzer();
    	if(drops == -1)
    		dropCount = -1;
    	else
    	{
    		Integer dcMin = (int) (drops - (0.3 * drops));
    		Integer dcMax = (int) (drops + (0.3 * drops));
    		dropCount = rand.nextInt(dcMax - dcMin + 1) + dcMin;
    	}
    }

    public String getWorld()
    {
    	return location.getWorldName();
    }
    
	public AppleseedLocation getLocation()
	{
		return location;
	}

	public Location getBukkitLocation()
	{
		return location.getLocation();
	}

	public AppleseedItemStack getItemStack()
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
	
	public Integer getFertilizerCount()
	{
		return fertilizerCount;
	}
	
	public void setFertilizerCount(Integer fc)
	{
		fertilizerCount = fc;
	}
	
	public Boolean hasSign()
	{
		return hasSign;
	}
	
	public AppleseedLocation getSign()
	{
		return signLocation;
	}
	
	public void setSign(Location loc)
	{
		if(loc != null)
		{
		hasSign = true;
		signLocation = new AppleseedLocation(loc);
		}
		else
		{
			hasSign = false;
			signLocation = null;
		}
	}
}
