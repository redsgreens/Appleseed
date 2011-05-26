package redsgreens.Appleseed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

public class AppleseedTreeManager {

    // hashmap of tree worlds, locations and data
    private HashMap<String, HashMap<AppleseedLocation, AppleseedTreeData>> WorldTrees = new HashMap<String, HashMap<AppleseedLocation, AppleseedTreeData>>();
    private HashMap<String, Boolean> treesUpdated = new HashMap<String, Boolean>();
    
    private Random rand = new Random();

    final int treeId = Material.LOG.getId();
    final int leafId = Material.LEAVES.getId();

	public AppleseedTreeManager()
	{
		loadTrees();
	}
	
    // loop through the list of trees and drop items around them, then schedule the next run
    public synchronized void ProcessTrees(){
		List<World> worlds = Appleseed.Plugin.getServer().getWorlds();
		Iterator<World> worldItr = worlds.iterator();
		
		while(worldItr.hasNext())
		{
			World world = worldItr.next();
			String worldName = world.getName();
			HashMap<AppleseedLocation, AppleseedTreeData> trees = WorldTrees.get(worldName);

	    	if(trees.size() != 0){
	        	Set<AppleseedLocation> locations = trees.keySet();
	        	Iterator<AppleseedLocation> itr = locations.iterator();
	        	while(itr.hasNext()){
	        		AppleseedLocation aloc = itr.next();

	        		try {
						Chunk chunk = world.getChunkAt(((Double)aloc.getX()).intValue(), ((Double)aloc.getZ()).intValue());
						if(chunk == null)
							continue;
						else if(world.isChunkLoaded(chunk)){
							Location loc = aloc.getLocation();
							Block block = world.getBlockAt(loc);
							if(block == null)
								continue;

							if(isTree(loc)){
								ItemStack iStack = trees.get(aloc).getItemStack();
								if(iStack != null)
								{
									AppleseedTreeType treeType = Appleseed.Config.TreeTypes.get(iStack);

									if(treeType != null)
									{
										AppleseedTreeData tree = trees.get(aloc);
						    			Integer dropCount = tree.getDropCount(); 
						    			Integer fertilizerCount = tree.getFertilizerCount();

						    			if(rand.nextInt((Integer)(100 / treeType.getDropLikelihood())) == 0 && (dropCount > 0 || dropCount == -1))
						    			{
						    				loc.getWorld().dropItemNaturally(loc, tree.getItemStack());

						    				if(dropCount != -1)
						    				{
						    					tree.setDropCount(dropCount - 1);
						    					if(!treesUpdated.containsKey(worldName))
						    						treesUpdated.put(worldName, true);
						    				}
						    			}
						    			else if(dropCount == 0 && fertilizerCount == 0)
						    			{
						    				KillTree(loc);
					    					if(!treesUpdated.containsKey(worldName))
					    						treesUpdated.put(worldName, true);
						    			}
									}
									else
										System.out.println("Appleseed: No TreeType in config.yml for \"" + iStack.getType().name().toLowerCase() + "\"");
								}
							}
							else if(world.getBlockAt(loc).getType() != Material.SAPLING)
							{
								itr.remove();
		    					if(!treesUpdated.containsKey(worldName))
		    						treesUpdated.put(worldName, true);
							}
						}
					} catch (Exception e) {
						System.out.println("Appleseed: Removed tree from world " + aloc.getWorldName() + ".");
						e.printStackTrace();
						itr.remove();
    					if(!treesUpdated.containsKey(worldName))
    						treesUpdated.put(worldName, true);
					}
	        	}
	        	if(treesUpdated.size() != 0)
	        	{
	        		asyncSaveTrees();
	        	}
	        }
    	
		}

    	// reprocess the list every interval
		Appleseed.Plugin.getServer().getScheduler().scheduleSyncDelayedTask(Appleseed.Plugin, new Runnable() {
		    public void run() {
		    	ProcessTrees();
		    }
		}, Appleseed.Config.DropInterval*20);

    }

    // add a tree to the hashmap and save to disk
    public synchronized void AddTree(AppleseedLocation loc, ItemStack iStack, String player)
    {
    	WorldTrees.get(loc.getWorldName()).put(loc, new AppleseedTreeData(loc, iStack, player));
    	
    	asyncSaveTrees();
    }

    // add a tree to the hashmap and save to disk
    public synchronized void AddTree(AppleseedLocation loc, ItemStack iStack, Integer dropcount, Integer fertilizercount,  String player)
    {
    	WorldTrees.get(loc.getWorldName()).put(loc, new AppleseedTreeData(loc, iStack, dropcount, fertilizercount, player));
    	
    	asyncSaveTrees();
    }

    public synchronized AppleseedTreeData GetTree(AppleseedLocation loc)
    {
    	HashMap<AppleseedLocation, AppleseedTreeData> trees = WorldTrees.get(loc.getWorldName());
    	
    	if(trees.containsKey(loc))
    		return trees.get(loc);
    	else 
    	{

    		World world = Appleseed.Plugin.getServer().getWorld(loc.getWorldName());
    		if(world == null)
    			return null;
    		
        	Block block = world.getBlockAt(loc.getLocation());
        	int treeCount = 0;
        	while(treeCount < 15 && block.getTypeId() == treeId && !trees.containsKey(block.getLocation()))
        	{
        		Block blockDown = block.getFace(BlockFace.DOWN);
        		if(blockDown.getTypeId() == treeId)
        			block = blockDown;
        		else
        			break;
        		
        		treeCount++;
        	}

        	AppleseedLocation retval = new AppleseedLocation(block.getLocation());
        	if(!trees.containsKey(retval))
        		return null;
        	else
        		return trees.get(retval);
    	}
    }

    public synchronized Boolean IsNewTreeTooClose(Location loc)
    {
    	HashMap<AppleseedLocation, AppleseedTreeData> trees = WorldTrees.get(loc.getWorld().getName());
    	Set<AppleseedLocation> locations = trees.keySet();
    	Iterator<AppleseedLocation> itr = locations.iterator();
    	
    	while(itr.hasNext())
    		if(calcDistanceSquared(itr.next().getLocation(), loc) < (Appleseed.Config.MinimumTreeDistance * Appleseed.Config.MinimumTreeDistance))
    			return true;
    	
    	return false;
    }
    
    public synchronized void KillTree(Location loc)
    {
    	if(!WorldTrees.get(loc.getWorld().getName()).containsKey(new AppleseedLocation(loc)))
    		return;
    	
    	World world = loc.getWorld();
    	Block block = world.getBlockAt(loc);
    	
    	if(block.getType() != Material.LOG)
    		return;
    	
    	int i = 0;
    	while(block.getType() == Material.LOG && i < 16)
    	{
    		block.setType(Material.AIR);
    		
    		Block neighbor = block.getFace(BlockFace.EAST);
    		if(neighbor.getType() == Material.LOG)
    			neighbor.setType(Material.AIR);

    		neighbor = block.getFace(BlockFace.NORTH);
    		if(neighbor.getType() == Material.LOG)
    			neighbor.setType(Material.AIR);

    		neighbor = block.getFace(BlockFace.NORTH_EAST);
    		if(neighbor.getType() == Material.LOG)
    			neighbor.setType(Material.AIR);

    		neighbor = block.getFace(BlockFace.NORTH_WEST);
    		if(neighbor.getType() == Material.LOG)
    			neighbor.setType(Material.AIR);

    		neighbor = block.getFace(BlockFace.SOUTH);
    		if(neighbor.getType() == Material.LOG)
    			neighbor.setType(Material.AIR);

    		neighbor = block.getFace(BlockFace.SOUTH_EAST);
    		if(neighbor.getType() == Material.LOG)
    			neighbor.setType(Material.AIR);

    		neighbor = block.getFace(BlockFace.SOUTH_WEST);
    		if(neighbor.getType() == Material.LOG)
    			neighbor.setType(Material.AIR);

    		neighbor = block.getFace(BlockFace.WEST);
    		if(neighbor.getType() == Material.LOG)
    			neighbor.setType(Material.AIR);

    		block = block.getFace(BlockFace.UP);
    		i++;
    	}
    }
    
    // load trees from disk
    @SuppressWarnings("unchecked")
	private synchronized void loadTrees()
    {
    	try
    	{
    		// load trees for all loaded worlds
    		List<World> worlds = Appleseed.Plugin.getServer().getWorlds();
    		Iterator<World> worldItr = worlds.iterator();
    		while(worldItr.hasNext())
    			loadTrees(worldItr.next().getName());
    		
    		// load trees from old style trees.yml file 
            File inFile = new File(Appleseed.Plugin.getDataFolder(), "trees.yml");
            if (inFile.exists()){
            	Integer importedCount = 0;
                Yaml yaml = new Yaml();
                FileInputStream fis = new FileInputStream(inFile);
                ArrayList<HashMap<String, Object>> loadData = (ArrayList<HashMap<String, Object>>)yaml.load(fis);
                
                for(int i=0; i<loadData.size(); i++)
                {
                	HashMap<String, Object> treeHash = loadData.get(i);

                	AppleseedTreeData tree = AppleseedTreeData.LoadFromHash(treeHash);
                	
                	if(tree != null)
                	{
                		String treeWorld = tree.getWorld();
                		if(!WorldTrees.containsKey(treeWorld))
                			WorldTrees.put(treeWorld, new HashMap<AppleseedLocation, AppleseedTreeData>());
                		
                		WorldTrees.get(treeWorld).put(tree.getLocation(), tree);
                		importedCount++;
                	}
                }
                
                fis.close();
                inFile.renameTo(new File(Appleseed.Plugin.getDataFolder(), "trees.yml.old"));
                saveTrees();
            	System.out.println("Appleseed: Imported " + importedCount.toString() + " trees from trees.yml.");
            }
    	}
    	catch (Exception ex)
    	{
            ex.printStackTrace();
    	}
    }

    @SuppressWarnings("unchecked")
	public void loadTrees(String world)
    // load trees from disk for a specific world
    {
    	HashMap<AppleseedLocation, AppleseedTreeData> trees;
		if(WorldTrees.containsKey(world))
			trees = WorldTrees.get(world);
		else
		{
			trees = new HashMap<AppleseedLocation, AppleseedTreeData>();
			WorldTrees.put(world, trees);
		}

        try {
			Yaml yaml = new Yaml();
			File inFile = new File(Appleseed.Plugin.getDataFolder(), "trees-" + world + ".yml");
			if (inFile.exists()){
			    FileInputStream fis = new FileInputStream(inFile);
			    ArrayList<HashMap<String, Object>> loadData = (ArrayList<HashMap<String, Object>>)yaml.load(fis);
			    
			    for(int i=0; i<loadData.size(); i++)
			    {
			    	HashMap<String, Object> treeHash = loadData.get(i);

			    	AppleseedTreeData tree = AppleseedTreeData.LoadFromHash(treeHash);
			    	
			    	if(tree != null)
			    		if(!trees.containsKey(tree.getLocation()))
			    			trees.put(tree.getLocation(), tree);
			    }
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

    	System.out.println("Appleseed: " + ((Integer)trees.size()).toString() + " trees loaded in world " + world + ".");
    }
    
    // save trees to disk
    public synchronized void saveTrees()
    {
        Iterator<String> worldItr;
        
        if(treesUpdated.size() == 0)
        	worldItr = WorldTrees.keySet().iterator();
        else
        	worldItr = treesUpdated.keySet().iterator();
        
		while(worldItr.hasNext())
		{
			String world = worldItr.next();
			HashMap<AppleseedLocation, AppleseedTreeData> trees = WorldTrees.get(world);
	    	ArrayList<HashMap<String, Object>> saveData = new ArrayList<HashMap<String, Object>>();

	    	Set<AppleseedLocation> locations = trees.keySet();
	    	Iterator<AppleseedLocation> itr = locations.iterator();
	    	while(itr.hasNext()){
	    		AppleseedLocation loc = itr.next();
	    		saveData.add(trees.get(loc).MakeHashFromTree());
	    	}
	    	
	    	try
	    	{
	            Yaml yaml = new Yaml();
	            File outFile = new File(Appleseed.Plugin.getDataFolder(), "trees-" + world + ".yml");
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
		
		treesUpdated.clear();
    }

    public synchronized void asyncSaveTrees()
    {
		Appleseed.Plugin.getServer().getScheduler().scheduleAsyncDelayedTask(Appleseed.Plugin, new Runnable() {
		    public void run() {
		    	saveTrees();
		    }
		}, 10);
    }
    
    // see if the given location is the root of a tree
    public final boolean isTree(Location loc)
    {
    	Location rootLoc;
    	if(WorldTrees.get(loc.getWorld().getName()).containsKey(new AppleseedLocation(loc)))
    		rootLoc = loc;
    	else
    	{
    		AppleseedTreeData tree = GetTree(new AppleseedLocation(loc));
    		if(tree == null)
    			return false;
    		else rootLoc = tree.getBukkitLocation();   			
    	}
    	
        final World world = rootLoc.getWorld();

        int treeCount = 0;        	
        final int rootX = rootLoc.getBlockX();
        final int rootY = rootLoc.getBlockY();
        final int rootZ = rootLoc.getBlockZ();
       
        final int maxY = 7;
        final int radius = 3;
        
        int leafCount = 0;

        if(world.getBlockTypeIdAt(rootLoc) == treeId)
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

    private double calcDistanceSquared(Location loc1, Location loc2)
    {
    	if(loc1.getWorld() != loc2.getWorld())
        	return Double.MAX_VALUE;
    	
    	double dX = loc1.getX() - loc2.getX();
    	double dY = loc1.getY() - loc2.getY();
    	double dZ = loc1.getZ() - loc2.getZ();

    	Double retval = (dX*dX) + (dY*dY) + (dZ*dZ);
    	
    	if(retval.isInfinite() || retval.isNaN())
    		return Double.MAX_VALUE;
    	else
    		return retval;
    }

}
