package redsgreens.Appleseed;

import java.util.HashMap;

/**
 * AppleseedTreeType stores the data about valid tree types that can be planted
 *
 * @author redsgreens
 */

public class AppleseedTreeType {
	private AppleseedItemStack itemStack;
	private Double dropLikelihood;
	private Boolean requireFertilizer;
	private Integer dropsBeforeFertilizer;
	private Integer intervalsBeforeFertilizer;
	private AppleseedCountMode countMode;
	private Integer maxFertilizer;
	private Byte saplingData;

	public AppleseedTreeType(AppleseedItemStack is, Double likelihood, Boolean reqFertilizer, Integer dropsFertilizer, Integer intFertilizer, AppleseedCountMode cm, Integer mxFertilizer, String type)
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
		else if(type.equalsIgnoreCase("Jungle"))
			saplingData = 3;
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
		AppleseedCountMode cm;
		if(loadData.containsKey("DropsBeforeFertilizer"))
		{
			dc = (Integer)loadData.get("DropsBeforeFertilizer");
			ic = -1;
			cm = AppleseedCountMode.Drop;
			
		}
		else if(loadData.containsKey("DropsBeforeFertilzer"))
		{
			dc = (Integer)loadData.get("DropsBeforeFertilzer");
			ic = -1;
			cm = AppleseedCountMode.Drop;
			
		}
		else if(loadData.containsKey("IntervalsBeforeFertilizer"))
		{
			dc = -1;
			ic = (Integer)loadData.get("IntervalsBeforeFertilizer");
			cm = AppleseedCountMode.Interval;
		}
		else
		{
			dc = -1;
			ic = -1;
			cm = AppleseedCountMode.Infinite;
		}

		Boolean rf;
		if(loadData.containsKey("RequireFertilizer"))
			rf = (Boolean)loadData.get("RequireFertilizer");
		else if(loadData.containsKey("RequireFertilzer"))
			rf = (Boolean)loadData.get("RequireFertilzer");
		else
			rf = false;

		String dlStr = loadData.get("DropLikelihood").toString();
		Double dl = Double.parseDouble(dlStr);
		if(dl == 0) // a zero likelihood is not allowed
			return null;
		
		AppleseedTreeType tree;
		try
		{
			tree = new AppleseedTreeType(iStack, dl, rf, dc, ic, cm, mf, (String)loadData.get("TreeType"));
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
	
	public Double getDropLikelihood()
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
	
	public AppleseedCountMode getCountMode()
	{
		return countMode;
	}
	
	
}
