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
	private Boolean requireFertilizer;
	private Integer dropsBeforeFertilizer;
	private Integer intervalsBeforeFertilizer;
	private CountMode countMode;
	private Integer maxFertilizer;
	private Byte saplingData;

	public AppleseedTreeType(AppleseedItemStack is, Integer likelihood, Boolean reqFertilizer, Integer dropsFertilizer, Integer intFertilizer, CountMode cm, Integer mxFertilizer, String type)
	{
		itemStack = is;
		dropLikelihood = likelihood;
		requireFertilizer = reqFertilizer;
		dropsBeforeFertilizer = dropsFertilizer;
		maxFertilizer = mxFertilizer;
		intervalsBeforeFertilizer = intFertilizer;
		countMode = cm;
		
		if(type.equalsIgnoreCase("Spruce"))
			saplingData = 1;
		else if(type.equalsIgnoreCase("Birch"))
			saplingData = 2;
		else saplingData = 0;
	}
	
	public static AppleseedTreeType LoadFromHash(String itemName, HashMap<String, Object> loadData)
	{
		if(!loadData.containsKey("DropLikelihood") || !loadData.containsKey("TreeType"))
			return null;

		AppleseedItemStack iStack = AppleseedItemStack.getItemStackFromName(itemName);

		if(iStack == null)
			return null;

		Integer mf;
		if(loadData.containsKey("MaxFertilizer"))
			mf = (Integer)loadData.get("MaxFertilizer");
		else if(loadData.containsKey("MaxFertilzer"))
			mf = (Integer)loadData.get("MaxFertilzer");
		else
			mf = -1;

		Integer dc;
		Integer ic;
		CountMode cm;
		if(loadData.containsKey("DropsBeforeFertilizer"))
		{
			dc = (Integer)loadData.get("DropsBeforeFertilizer");
			ic = -1;
			cm = CountMode.Drop;
			
		}
		else if(loadData.containsKey("DropsBeforeFertilzer"))
		{
			dc = (Integer)loadData.get("DropsBeforeFertilzer");
			ic = -1;
			cm = CountMode.Drop;
			
		}
		else if(loadData.containsKey("IntervalsBeforeFertilizer"))
		{
			dc = -1;
			ic = (Integer)loadData.get("IntervalsBeforeFertilizer");
			cm = CountMode.Interval;
		}
		else
		{
			dc = -1;
			ic = -1;
			cm = CountMode.Infinite;
		}

		Boolean rf;
		if(loadData.containsKey("RequireFertilizer"))
			rf = (Boolean)loadData.get("RequireFertilizer");
		else if(loadData.containsKey("RequireFertilzer"))
			rf = (Boolean)loadData.get("RequireFertilzer");
		else
			rf = false;

		
		AppleseedTreeType tree;
		try
		{
			tree = new AppleseedTreeType(iStack, (Integer)loadData.get("DropLikelihood"), rf, dc, ic, cm, mf, (String)loadData.get("TreeType"));
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
	
	public Boolean getRequireFertilizer()
	{
		return requireFertilizer;
	}
	
	public Integer getDropsBeforeFertilizer()
	{
		return dropsBeforeFertilizer;
	}

	public Integer getIntervalsBeforeFertilizer()
	{
		return intervalsBeforeFertilizer;
	}

	public Integer getMaxFertilizer()
	{
		return maxFertilizer;
	}
	
	public Byte getSaplingData()
	{
		return saplingData;
	}
	
	public CountMode getCountMode()
	{
		return countMode;
	}
	
	
}
