package redsgreens.Appleseed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

public class AppleseedTreeManager {

	private Appleseed Plugin;
	
    // hashmap of tree locations and types
    private static HashMap<Location, AppleseedTreeData> Trees = new HashMap<Location, AppleseedTreeData>();
	
    private static Random rand = new Random();

	public AppleseedTreeManager(Appleseed plugin)
	{
		Plugin = plugin;

		loadTrees();
	}
	
    // loop through the list of trees and drop items around them, then schedule the next run
    public void ProcessTrees(){
    	Boolean treesRemoved = false;
    	
    	if(Trees.size() != 0){
        	Set<Location> locations = Trees.keySet();
        	Iterator<Location> itr = locations.iterator();
        	while(itr.hasNext()){
        		Location loc = itr.next();
        		World world = loc.getWorld();
        		if(world.isChunkLoaded(world.getChunkAt(world.getBlockAt(loc)))){
            		if(isTree(loc)){
            			if(rand.nextInt((Integer)(100 / Appleseed.Config.DropLikelihood)) == 0)
                			loc.getWorld().dropItemNaturally(loc, Trees.get(loc).getItemStack());
            		}
            		else if(world.getBlockAt(loc).getType() != Material.SAPLING)
            		{
            			itr.remove();
            			treesRemoved = true;
            		}
        		}
        	}
        	if(treesRemoved == true)
        		saveTrees();
        }

    	// reprocess the list every minute
		Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Plugin, new Runnable() {
		    public void run() {
		    	ProcessTrees();
		    }
		}, Appleseed.Config.DropInterval*20);
    }

    // add a tree to the hashmap and save to disk
    public void AddTree(Location loc, ItemStack iStack, String player)
    {
    	Trees.put(loc, new AppleseedTreeData(loc, iStack, player));
    	
    	saveTrees();
    }
    
    // load trees from disk
    @SuppressWarnings("unchecked")
	private void loadTrees()
    {
    	try
    	{
            Yaml yaml = new Yaml();
            File inFile = new File(Plugin.getDataFolder(), "trees.yml");
            if (inFile.exists()){
                FileInputStream fis = new FileInputStream(inFile);
                ArrayList<HashMap<String, Object>> loadData = (ArrayList<HashMap<String, Object>>)yaml.load(fis);
                
                for(int i=0; i<loadData.size(); i++)
                {
                	HashMap<String, Object> tree = loadData.get(i);

                	Location loc = new Location(Plugin.getServer().getWorld((String)tree.get("world")), (Double)tree.get("x"), (Double)tree.get("y"), (Double)tree.get("z"));

                	String player;
            		if(tree.containsKey("player"))
            			player = (String)tree.get("player");
            		else
            			player = "unknown";

            		Integer dc;
            		if(tree.containsKey("dropcount"))
            			dc = (Integer)tree.get("dropcount");
            		else
            			dc = -1;

            		ItemStack iStack;
            		if(tree.containsKey("durability"))
                		iStack = new ItemStack(Material.getMaterial((Integer)tree.get("itemid")), 1, ((Integer)tree.get("durability")).shortValue()); 
                	else
                		iStack = new ItemStack(Material.getMaterial((Integer)tree.get("itemid")), 1);

            		Trees.put(loc, new AppleseedTreeData(loc, iStack, dc, player));
                }
            }
    	}
    	catch (Exception ex)
    	{
            ex.printStackTrace();
    	}
    	System.out.println("Appleseed: " + ((Integer)Trees.size()).toString() + " trees loaded.");
    }
    
    // save trees to disk
    private void saveTrees()
    {
    	ArrayList<HashMap<String, Object>> saveData = new ArrayList<HashMap<String, Object>>();
    	
    	if(Trees.size() != 0){
        	Set<Location> locations = Trees.keySet();
        	Iterator<Location> itr = locations.iterator();
        	while(itr.hasNext()){
        		Location loc = itr.next();
        		saveData.add(makeHashFromTree(Trees.get(loc)));
        	}
        	
        	try
        	{
                Yaml yaml = new Yaml();
                File outFile = new File(Plugin.getDataFolder(), "trees.yml");
                FileOutputStream fos = new FileOutputStream(outFile);
                OutputStreamWriter out = new OutputStreamWriter(fos);
                out.write(yaml.dump(saveData));
                out.close();
                fos.close();
        		
        	}
        	catch (Exception ex)
        	{
        		ex.printStackTrace();
        	}
    	}
    }
    
    // take a tree location and item and return a hash for saving to disk
    private HashMap<String, Object> makeHashFromTree(AppleseedTreeData tree)
    {
    	HashMap<String, Object> treeHash = new HashMap<String, Object>();
    	
    	Location loc = tree.getLocation();
    	ItemStack iStack = tree.getItemStack();
    	
    	treeHash.put("world", loc.getWorld().getName());
    	treeHash.put("x", loc.getX());
    	treeHash.put("y", loc.getY());
    	treeHash.put("z", loc.getZ());
    	
    	treeHash.put("itemid", iStack.getTypeId());
    	if(iStack.getType() == Material.INK_SACK && iStack.getDurability() == 3)
    		treeHash.put("durability", iStack.getDurability());
    	
    	treeHash.put("player", tree.getPlayer());
    	treeHash.put("dropcount", tree.getDropCount());
    	
    	return treeHash;
    }
    
    // see if the given location is the root of a tree
    public static final boolean isTree(Location rootBlock)
    {
        final World world = rootBlock.getWorld();
        final int rootX = rootBlock.getBlockX();
        final int rootY = rootBlock.getBlockY();
        final int rootZ = rootBlock.getBlockZ();
        
        final int treeId = Material.LOG.getId();
        final int leafId = Material.LEAVES.getId();
        
        final int maxY = 7;
        final int radius = 3;
        
        int treeCount = 0;
        int leafCount = 0;

        if(world.getBlockTypeIdAt(rootBlock) == treeId)
        {
            for (int y = rootY; y <= rootY+maxY; y++) {
                for (int x = rootX-radius; x <= rootX+radius; x++) {
                    for (int z = rootZ-radius; z <= rootZ+radius; z++) {
                        final int blockId = world.getBlockTypeIdAt(x, y, z);
                        if(blockId == treeId) 
                        	treeCount++;
                        else if(blockId == leafId) 
                        	leafCount++;

                        if(treeCount >= 3 && leafCount >= 8)
                        	return true;
                        
                    }
                }
            }
        }
        return false;
    }
}
