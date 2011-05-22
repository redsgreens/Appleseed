package redsgreens.Appleseed;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AppleseedTreeType {

	private ItemStack itemStack;
	private Integer dropLikelihood;
	private Boolean requireFertilzer;
	private Integer dropsBeforeFertilzer;
	private String treeType;

	public AppleseedTreeType(ItemStack is, Integer likelihood, Boolean reqFertilizer, Integer dropsFertilizer, String type)
	{
		itemStack = is;
		dropLikelihood = likelihood;
		requireFertilzer = reqFertilizer;
		dropsBeforeFertilzer = dropsFertilizer;
		treeType = type;
	}
	
	public static AppleseedTreeType LoadFromHash(String itemName, HashMap<String, Object> loadData)
	{
		if(!loadData.containsKey("DropLikelihood") || !loadData.containsKey("RequireFertilzer") || !loadData.containsKey("DropsBeforeFertilzer") || !loadData.containsKey("TreeType"))
			return null;

		Material material = Material.matchMaterial(itemName);
		ItemStack iStack;
		if(material != null)
			iStack = new ItemStack(material, 1);
		else if(itemName.equalsIgnoreCase("cocoa_beans"))
			iStack = new ItemStack(Material.INK_SACK, 1, (short)3);
		else
			return null;

		AppleseedTreeType tree;
		try
		{
			tree = new AppleseedTreeType(iStack, (Integer)loadData.get("DropLikelihood"), (Boolean)loadData.get("RequireFertilzer"), (Integer)loadData.get("DropsBeforeFertilzer"), (String)loadData.get("TreeType"));
		}
		catch (Exception ex)
		{
			System.out.println(ex.getStackTrace());
			tree = null;
		}

		return tree;
	}
	
/*
	for(int i=0; i<tempAllowedTreeTypes.size(); i++)
	{
		Material m = Material.matchMaterial(tempAllowedTreeTypes.get(i));
		if(m != null)
		{
			AllowedTreeItems.add(new ItemStack(m, 1));
			AllowedTreeTypes.add(tempAllowedTreeTypes.get(i).toLowerCase());
		}
		else if(tempAllowedTreeTypes.get(i).equalsIgnoreCase("cocoa_beans"))
		{
			AllowedTreeItems.add(new ItemStack(Material.INK_SACK, 1, (short)3));
			AllowedTreeTypes.add(tempAllowedTreeTypes.get(i).toLowerCase());
		}
	}
*/

	public ItemStack getItemStack()
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
	
	public String getTreeType()
	{
		return treeType;
	}
}
