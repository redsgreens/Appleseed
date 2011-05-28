package redsgreens.Appleseed;

import java.util.HashMap;


/**
 * AppleseedTreeType stores the data about valid tree types that can be planted
 *
 * @author redsgreens
 */
public class AppleseedTreeType {

	private AppleseedItemStack itemStack;
	private Integer dropLikelihood;
	private Boolean requireFertilzer;
	private Integer dropsBeforeFertilzer;
	private Integer maxFertilizer;
	private Byte saplingData;

	public AppleseedTreeType(AppleseedItemStack is, Integer likelihood, Boolean reqFertilizer, Integer dropsFertilizer, Integer mxFertilizer, String type)
	{
		itemStack = is;
		dropLikelihood = likelihood;
		requireFertilzer = reqFertilizer;
		dropsBeforeFertilzer = dropsFertilizer;
		maxFertilizer = mxFertilizer;
		
		if(type.equalsIgnoreCase("Spruce"))
			saplingData = 1;
		else if(type.equalsIgnoreCase("Birch"))
			saplingData = 2;
		else saplingData = 0;
	}
	
	public static AppleseedTreeType LoadFromHash(String itemName, HashMap<String, Object> loadData)
	{
		if(!loadData.containsKey("DropLikelihood") || !loadData.containsKey("RequireFertilzer") || !loadData.containsKey("DropsBeforeFertilzer") || !loadData.containsKey("TreeType"))
			return null;

		AppleseedItemStack iStack = AppleseedItemStack.getItemStackFromName(itemName);

		if(iStack == null)
			return null;

		Integer mf;
		if(loadData.containsKey("MaxFertilizer"))
			mf = (Integer)loadData.get("MaxFertilizer");
		else
			mf = -1;
		
		AppleseedTreeType tree;
		try
		{
			tree = new AppleseedTreeType(iStack, (Integer)loadData.get("DropLikelihood"), (Boolean)loadData.get("RequireFertilzer"), (Integer)loadData.get("DropsBeforeFertilzer"), mf, (String)loadData.get("TreeType"));
		}
		catch (Exception ex)
		{
			System.out.println(ex.getStackTrace());
			tree = null;
		}

		return tree;
	}
	
	public AppleseedItemStack getItemStack()
	{
		return itemStack;
	}
	
	public Integer getDropLikelihood()
	{
		return dropLikelihood;
	}
	
	public Boolean getRequireFertilzer()
	{
		return requireFertilzer;
	}
	
	public Integer getDropsBeforeFertilzer()
	{
		return dropsBeforeFertilzer;
	}
	
	public Integer getMaxFertilizer()
	{
		return maxFertilizer;
	}
	
	public Byte getSaplingData()
	{
		return saplingData;
	}
}
