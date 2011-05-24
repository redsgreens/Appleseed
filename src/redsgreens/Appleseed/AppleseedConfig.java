package redsgreens.Appleseed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.Yaml;

public class AppleseedConfig {
	public Boolean ShowErrorsInClient = true;
	public Integer DropInterval = 60;
	public Material WandItem = Material.WOOD_HOE;
	public Integer MinimumTreeDistance = -1;

	public HashMap<ItemStack, AppleseedTreeType> TreeTypes;

	public AppleseedConfig()
	{
		TreeTypes = new HashMap<ItemStack, AppleseedTreeType>();
	}
	
	@SuppressWarnings("unchecked")
	public void LoadConfig()
	{
		try
		{
			// create the data folder if it doesn't exist
			File folder = Appleseed.Plugin.getDataFolder();
	    	if(!folder.exists())
	    		folder.mkdirs();
    	
	    	// create a stock config file if it doesn't exist
	    	File configFile = new File(folder, "config.yml");
			if (!configFile.exists()){
				configFile.createNewFile();
				InputStream res = Appleseed.class.getResourceAsStream("/config.yml");
				FileWriter tx = new FileWriter(configFile);
				for (int i = 0; (i = res.read()) > 0;) tx.write(i);
				tx.flush();
				tx.close();
				res.close();
			}

			// create an empty config
			HashMap<String, Object> configMap = new HashMap<String, Object>();
			
			BufferedReader rx = new BufferedReader(new FileReader(configFile));
			Yaml yaml = new Yaml();
			
			try{
				configMap = (HashMap<String,Object>)yaml.load(rx);
			}
			catch (Exception ex){
				System.out.println(ex.getMessage());
			}
			finally
			{
				rx.close();
			}

			if(configMap.containsKey("ShowErrorsInClient"))
				ShowErrorsInClient = (Boolean)configMap.get("ShowErrorsInClient");
			System.out.println("Appleseed: ShowErrorsInClient=" + ShowErrorsInClient.toString());

			if(configMap.containsKey("DropInterval"))
				DropInterval = (Integer)configMap.get("DropInterval");
			System.out.println("Appleseed: DropInterval=" + DropInterval.toString() + " seconds");

			if(configMap.containsKey("WandItem"))
				WandItem = Material.getMaterial((Integer)configMap.get("WandItem"));
			System.out.println("Appleseed: WandItem=" + WandItem.name().toLowerCase());

			if(configMap.containsKey("MinimumTreeDistance"))
				MinimumTreeDistance = (Integer)configMap.get("MinimumTreeDistance");
			if(MinimumTreeDistance == -1)
				System.out.println("Appleseed: MinimumTreeDistance=disabled");
			else
				System.out.println("Appleseed: MinimumTreeDistance=" + MinimumTreeDistance.toString());

			if(!configMap.containsKey("TreeTypes"))
				System.out.println("Appleseed: TreeTypes=");
			else
			{
				HashMap<String, HashMap<String, Object>> treeTypes = (HashMap<String, HashMap<String, Object>>)configMap.get("TreeTypes");

				// process list of tree types
				Iterator<String> itr = treeTypes.keySet().iterator();
				while(itr.hasNext())
				{
					String itemName = itr.next();
					HashMap<String, Object> treeConf = treeTypes.get(itemName);
					
					AppleseedTreeType treeType = AppleseedTreeType.LoadFromHash(itemName, treeConf);
					
					if(treeType == null)
						itr.remove();
					else
						TreeTypes.put(treeType.getItemStack(), treeType);
					
				}
				
				String strTreeTypes = "";
				Iterator<ItemStack> itr2 = TreeTypes.keySet().iterator();
				while(itr2.hasNext())
				{
					ItemStack is = itr2.next();
					if(strTreeTypes.length() != 0)
						strTreeTypes = strTreeTypes + ",";
					
					if(is.getType() == Material.INK_SACK && is.getDurability() == (short)3)
						strTreeTypes = strTreeTypes + "cocoa_beans";
					else
						strTreeTypes = strTreeTypes + is.getType().name().toLowerCase();
				}
				
				System.out.println("Appleseed: TreeTypes=(" + strTreeTypes +")");
			}
		}
		catch (Exception ex){
			System.out.println(ex.getStackTrace());
		}
	}
}
