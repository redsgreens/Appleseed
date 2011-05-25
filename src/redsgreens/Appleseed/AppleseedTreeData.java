package redsgreens.Appleseed;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public class AppleseedTreeData {

	private Location location;
	private ItemStack itemStack;
	private String player;
	private Integer dropCount;
	private Integer fertilizerCount;

	private static Random rand = new Random();

	public AppleseedTreeData(Location loc, ItemStack is, String p)
	{
		location = loc;
		itemStack = is;
		player = p;

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

	public AppleseedTreeData(Location loc, ItemStack is, Integer dc, Integer fc, String p)
	{
		location = loc;
		itemStack = is;
		player = p;
		dropCount = dc;
		fertilizerCount = fc;
	}

	// take a hashmap and make a tree from it
	public static AppleseedTreeData LoadFromHash(HashMap<String, Object> loadData)
	{
		if(!loadData.containsKey("world") || !loadData.containsKey("x") || !loadData.containsKey("y") || !loadData.containsKey("z") || !loadData.containsKey("itemid"))
			return null;
		
		World world = Appleseed.Plugin.getServer().getWorld((String)loadData.get("world"));
		if(world == null)
			return null;
		
    	Location loc = new Location(world, (Double)loadData.get("x"), (Double)loadData.get("y"), (Double)loadData.get("z"));

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

		ItemStack iStack;
		if(loadData.containsKey("durability"))
    		iStack = new ItemStack(Material.getMaterial((Integer)loadData.get("itemid")), 1, ((Integer)loadData.get("durability")).shortValue()); 
    	else
    		iStack = new ItemStack(Material.getMaterial((Integer)loadData.get("itemid")), 1);

		return new AppleseedTreeData(loc, iStack, dc, fc, player);
	}
	
    // take a tree location and item and return a hash for saving to disk
    public HashMap<String, Object> MakeHashFromTree()
    {
    	HashMap<String, Object> treeHash = new HashMap<String, Object>();
    	
    	treeHash.put("world", location.getWorld().getName());
    	treeHash.put("x", location.getX());
    	treeHash.put("y", location.getY());
    	treeHash.put("z", location.getZ());
    	
    	treeHash.put("itemid", itemStack.getTypeId());
    	if(itemStack.getType() == Material.INK_SACK && itemStack.getDurability() == 3)
    		treeHash.put("durability", itemStack.getDurability());
    	
    	treeHash.put("player", player);
    	treeHash.put("dropcount", dropCount);
    	treeHash.put("fertilizercount", fertilizerCount);
    	
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
	
	public Integer getFertilizerCount()
	{
		return fertilizerCount;
	}
	
	public void setFertilizerCount(Integer fc)
	{
		fertilizerCount = fc;
	}
}
