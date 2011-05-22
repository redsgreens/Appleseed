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

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

public class AppleseedTreeManager {

    // hashmap of tree locations and types
    private static HashMap<Location, AppleseedTreeData> Trees = new HashMap<Location, AppleseedTreeData>();
    
    private Random rand = new Random();

	public AppleseedTreeManager()
	{
		loadTrees();
	}
	
    // loop through the list of trees and drop items around them, then schedule the next run
    public synchronized void ProcessTrees(){
    	Boolean treesRemoved = false;
    	Boolean treesUpdated = false;
    	
    	if(Trees.size() != 0){
        	Set<Location> locations = Trees.keySet();
        	Iterator<Location> itr = locations.iterator();
        	while(itr.hasNext()){
        		Location loc = itr.next();
        		if(loc == null)
        			continue;
       		
        		World world = loc.getWorld();
        		if(world == null)
        			continue;
        		
        		Block block = world.getBlockAt(loc);
        		if(block == null)
        			continue;
        		
        		Chunk chunk = world.getChunkAt(block);
        		if(chunk == null)
        			continue;
        		
        		if(world.isChunkLoaded(chunk)){
            		if(isTree(loc)){
            			ItemStack iStack = Trees.get(loc).getItemStack();
            			if(iStack != null)
            			{
            				AppleseedTreeType treeType = Appleseed.Config.TreeTypes.get(iStack);

            				if(treeType != null)
            				{
            					if(rand.nextInt((Integer)(100 / treeType.getDropLikelihood())) == 0)
                    			{
                    				AppleseedTreeData tree = Trees.get(loc);
                        			Integer dropCount = tree.getDropCount(); 

                        			if(dropCount > 0 || dropCount == -1)
                        			{
                        				loc.getWorld().dropItemNaturally(loc, tree.getItemStack());

                        				if(dropCount != -1)
                        				{
                        					tree.setDropCount(dropCount - 1);
                        					treesUpdated = true;
                        				}
                        			}
                    			}
            				}
            				else
            					System.out.println("Appleseed: No TreeType in config.yml for \"" + iStack.getType().name().toLowerCase() + "\"");
            			}
            		}
            		else if(world.getBlockAt(loc).getType() != Material.SAPLING)
            		{
            			itr.remove();
            			treesRemoved = true;
            		}
        		}
        	}
        	if(treesRemoved || treesUpdated)
        	{
        		asyncSaveTrees();
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
    public synchronized void AddTree(Location loc, ItemStack iStack, String player)
    {
    	Trees.put(loc, new AppleseedTreeData(loc, iStack, player));
    	
    	asyncSaveTrees();
    }

    // add a tree to the hashmap and save to disk
    public synchronized void AddTree(Location loc, ItemStack iStack, Integer dropcount, String player)
    {
    	Trees.put(loc, new AppleseedTreeData(loc, iStack, dropcount, player));
    	
    	asyncSaveTrees();
    }

    public synchronized void ResetTreeDropCount(Location loc)
    {
    	AppleseedTreeData tree = Trees.get(loc);
    	
    	if(tree != null)
    	{
    		tree.ResetDropCount();
    		asyncSaveTrees();
    	}
    }

    public synchronized void ResetTreeDropCount(Location loc, Integer dropcount)
    {
    	AppleseedTreeData tree = Trees.get(loc);
    	
    	if(tree != null)
    	{
    		tree.ResetDropCount(dropcount);
    		asyncSaveTrees();
    	}
    }

    // load trees from disk
    @SuppressWarnings("unchecked")
	private synchronized void loadTrees()
    {
    	try
    	{
            Yaml yaml = new Yaml();
            File inFile = new File(Appleseed.Plugin.getDataFolder(), "trees.yml");
            if (inFile.exists()){
                FileInputStream fis = new FileInputStream(inFile);
                ArrayList<HashMap<String, Object>> loadData = (ArrayList<HashMap<String, Object>>)yaml.load(fis);
                
                for(int i=0; i<loadData.size(); i++)
                {
                	HashMap<String, Object> treeHash = loadData.get(i);

                	AppleseedTreeData tree = AppleseedTreeData.LoadFromHash(treeHash);
                	
                	if(tree != null)
                		Trees.put(tree.getLocation(), tree);
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

    public synchronized void saveTrees()
    {
    	ArrayList<HashMap<String, Object>> saveData = new ArrayList<HashMap<String, Object>>();
    	
    	Set<Location> locations = Trees.keySet();
    	Iterator<Location> itr = locations.iterator();
    	while(itr.hasNext()){
    		Location loc = itr.next();
    		saveData.add(Trees.get(loc).MakeHashFromTree());
    	}
    	
    	try
    	{
            Yaml yaml = new Yaml();
            File outFile = new File(Appleseed.Plugin.getDataFolder(), "trees.yml");
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

    private synchronized void asyncSaveTrees()
    {
		Appleseed.Plugin.getServer().getScheduler().scheduleAsyncDelayedTask(Appleseed.Plugin, new Runnable() {
		    public void run() {
		    	saveTrees();
		    }
		}, 0);
    }
    
    // see if the given location is the root of a tree
    public final boolean isTree(Location rootBlock)
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
