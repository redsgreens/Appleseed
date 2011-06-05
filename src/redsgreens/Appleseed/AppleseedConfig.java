package redsgreens.Appleseed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import org.yaml.snakeyaml.Yaml;

/**
 * AppleseedConfig loads the config file from disk and presents the settings as public variables
 *
 * @author redsgreens
 */

public class AppleseedConfig {
	public Boolean ShowErrorsInClient = true;
	public Integer DropInterval = 60;
	public AppleseedItemStack WandItem = AppleseedItemStack.getItemStackFromName("wood_hoe");
	public Integer MinimumTreeDistance = -1;
	public Integer MaxTreesPerPlayer = -1;
	public Boolean MaxIsPerWorld = false;
	public AppleseedItemStack FertilizerItem = AppleseedItemStack.getItemStackFromName("bone_meal");

	public HashMap<AppleseedItemStack, AppleseedTreeType> TreeTypes = new HashMap<AppleseedItemStack, AppleseedTreeType>();

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

			if(configMap.containsKey("MaxTreesPerPlayer"))
				MaxTreesPerPlayer = (Integer)configMap.get("MaxTreesPerPlayer");
			if(MaxTreesPerPlayer != -1)
				System.out.println("Appleseed: MaxTreesPerPlayer=" + MaxTreesPerPlayer.toString());

			if(configMap.containsKey("MaxIsPerWorld") && MaxTreesPerPlayer != -1)
			{
				MaxIsPerWorld = (Boolean)configMap.get("MaxIsPerWorld");
				System.out.println("Appleseed: MaxIsPerWorld=" + MaxIsPerWorld.toString());
			}

			if(configMap.containsKey("WandItem"))
			{
				String wiStr = configMap.get("WandItem").toString();
				WandItem = AppleseedItemStack.getItemStackFromName(wiStr);
			}
			System.out.println("Appleseed: WandItem=" + AppleseedItemStack.getItemStackName(WandItem).toLowerCase());

			if(configMap.containsKey("FertilizerItem"))
			{
				String fiStr = configMap.get("FertilizerItem").toString();
				FertilizerItem = AppleseedItemStack.getItemStackFromName(fiStr);
			}
			System.out.println("Appleseed: FertilizerItem=" + AppleseedItemStack.getItemStackName(FertilizerItem).toLowerCase());

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
				Iterator<AppleseedItemStack> itr2 = TreeTypes.keySet().iterator();
				while(itr2.hasNext())
				{
					AppleseedItemStack is = itr2.next();
					if(strTreeTypes.length() != 0)
						strTreeTypes = strTreeTypes + ",";
					
					strTreeTypes = strTreeTypes + AppleseedItemStack.getItemStackName(is);
				}
				
				System.out.println("Appleseed: TreeTypes=(" + strTreeTypes +")");
			}
		}
		catch (Exception ex){
			System.out.println(ex.getStackTrace());
		}
	}
}
