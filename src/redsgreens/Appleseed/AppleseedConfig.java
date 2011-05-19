package redsgreens.Appleseed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.yaml.snakeyaml.Yaml;

public class AppleseedConfig {
	private Appleseed Plugin;

	public Boolean ShowErrorsInClient = true;
	public Integer DropLikelihood = 33;
	public Integer DropInterval = 60;
	public ArrayList<Material> AllowedTreeMaterials = new ArrayList<Material>();

	public AppleseedConfig(Appleseed plugin)
	{
		Plugin = plugin;
		
	}
	@SuppressWarnings("unchecked")
	public void LoadConfig()
	{
		try
		{
		
			// create the data folder if it doesn't exist
			File folder = Plugin.getDataFolder();
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
			finally
			{
				rx.close();
			}

			if(configMap.containsKey("ShowErrorsInClient"))
				ShowErrorsInClient = (Boolean)configMap.get("ShowErrorsInClient");

			if(configMap.containsKey("DropLikelihood"))
				DropLikelihood = (Integer)configMap.get("DropLikelihood");

			if(configMap.containsKey("DropInterval"))
				DropInterval = (Integer)configMap.get("DropInterval");

			List<String> AllowedTreeTypes = Arrays.asList("apple", "cookie");
			if(configMap.containsKey("AllowedTreeTypes"))
				AllowedTreeTypes = (List<String>)configMap.get("AllowedTreeTypes");
			
			// process list of tree types and generate materials list
			AllowedTreeMaterials.clear();
			for(int i=0; i<AllowedTreeTypes.size(); i++)
			{
				Material m = Material.matchMaterial(AllowedTreeTypes.get(i));
				if(m != null)
					AllowedTreeMaterials.add(m);
			}
			
			// print config status
			System.out.println("Appleseed: ShowErrorsInClient=" + ShowErrorsInClient.toString());
			System.out.println("Appleseed: DropLikelihood=" + DropLikelihood.toString() + "%");
			System.out.println("Appleseed: DropInterval=" + DropInterval.toString() + " seconds");
			System.out.println("Appleseed: AllowedTreeTypes=" + AllowedTreeMaterials.toString());
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
