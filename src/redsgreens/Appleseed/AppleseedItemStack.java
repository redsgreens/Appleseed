package redsgreens.Appleseed;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AppleseedItemStack {

	private Material material;
	private Short durability;

    private static HashMap<String, AppleseedItemStack> itemStackNames = new HashMap<String, AppleseedItemStack>();

	public AppleseedItemStack(Material m)
	{
		material = m;
		durability = 0;
	}

	public AppleseedItemStack(Material m, Short d)
	{
		material = m;
		durability = d;
	}
	
	public AppleseedItemStack(ItemStack is)
	{
		material = is.getType();
		durability = is.getDurability();
	}

	public ItemStack getItemStack()
	{
		return new ItemStack(material, 1, durability);
	}
	
	public Short getDurability()
	{
		return durability;
	}
	
	public Material getMaterial()
	{
		return material;
	}
	
    public static String getItemStackName(AppleseedItemStack iStack)
    {
    	Short durability = iStack.getDurability();
    	
    	if(durability == 0)
    		return iStack.getMaterial().name().toLowerCase();
    	else
    	{
    		Material material = iStack.getMaterial();
    		switch(material)
    		{
    		case INK_SACK:
    			switch(durability)
    			{
	    			case 0:  return "ink_sack";
	    			case 1:  return "rose_red";
	    			case 2:  return "cactus_green";
	    			case 3:  return "cocoa_beans";
	    			case 4:  return "lapis_lazuli";
	    			case 5:  return "purple_dye";
	    			case 6:  return "cyan_dye";
	    			case 7:  return "lightgray_dye";
	    			case 8:  return "gray_dye";
	    			case 9:  return "pink_dye";
	    			case 10: return "lime_dye";
	    			case 11: return "yellow_dye";
	    			case 12: return "lightblue_dye";
	    			case 13: return "magenta_dye";
	    			case 14: return "orange_dye";
	    			case 15: return "bone_meal";
    			}
    			break;
    			
    		case WOOL:
    			switch(durability)
    			{
	    			case 0:  return "white_wool";
	    			case 1:  return "orange_wool";
	    			case 2:  return "magenta_wool";
	    			case 3:  return "lightblue_wool";
	    			case 4:  return "yellow_wool";
	    			case 5:  return "lightgreen_wool";
	    			case 6:  return "pink_wool";
	    			case 7:  return "gray_wool";
	    			case 8:  return "lightgray_wool";
	    			case 9:  return "cyan_wool";
	    			case 10: return "purple_wool";
	    			case 11: return "blue_wool";
	    			case 12: return "brown_wool";
	    			case 13: return "darkgreen_wool";
	    			case 14: return "red_wool";
	    			case 15: return "black_wool";
    			}
    			break;
    			
    		case LOG:
    			switch(durability)
    			{
	    			case 0: return "log";
	    			case 1: return "spruce_log";
	    			case 2: return "birch_log";
    			}
    			break;
    			
    		case SAPLING:
    			switch(durability)
    			{
	    			case 0: return "sapling";
	    			case 1: return "spruce_sapling";
	    			case 2: return "birch_sapling";
    			}
    			break;
    		}
    	}
    	
    	return null;
    }

    public static AppleseedItemStack getItemStackFromName(String name)
    {
    	// build hashmap of itemstacks for items with durability values
    	if(itemStackNames.size() == 0)
			setupItemStackNames();

		Material material = Material.matchMaterial(name);
    	if(material != null)
    		return new AppleseedItemStack(material);

    	if(itemStackNames.containsKey(name))
    		return itemStackNames.get(name);

    	return null;
    }

	// build hashmap of itemstacks for items with durability values
    private static void setupItemStackNames()
    {
    	itemStackNames.put("rose_red", new AppleseedItemStack(Material.INK_SACK, (short)1));
    	itemStackNames.put("cactus_green", new AppleseedItemStack(Material.INK_SACK, (short)2));
    	itemStackNames.put("cocoa_beans", new AppleseedItemStack(Material.INK_SACK, (short)3));
    	itemStackNames.put("lapis_lazuli", new AppleseedItemStack(Material.INK_SACK, (short)4));
    	itemStackNames.put("purple_dye", new AppleseedItemStack(Material.INK_SACK, (short)5));
    	itemStackNames.put("cyan_dye", new AppleseedItemStack(Material.INK_SACK, (short)6));
    	itemStackNames.put("lightgray_dye", new AppleseedItemStack(Material.INK_SACK, (short)7));
    	itemStackNames.put("gray_dye", new AppleseedItemStack(Material.INK_SACK, (short)8));
    	itemStackNames.put("pink_dye", new AppleseedItemStack(Material.INK_SACK, (short)9));
    	itemStackNames.put("lime_dye", new AppleseedItemStack(Material.INK_SACK, (short)10));
    	itemStackNames.put("yellow_dye", new AppleseedItemStack(Material.INK_SACK, (short)11));
    	itemStackNames.put("lightblue_dye", new AppleseedItemStack(Material.INK_SACK, (short)12));
    	itemStackNames.put("magenta_dye", new AppleseedItemStack(Material.INK_SACK, (short)13));
    	itemStackNames.put("orange_dye", new AppleseedItemStack(Material.INK_SACK, (short)14));
    	itemStackNames.put("bone_meal", new AppleseedItemStack(Material.INK_SACK, (short)15));
     	itemStackNames.put("orange_wool", new AppleseedItemStack(Material.WOOL, (short)1));
     	itemStackNames.put("magenta_wool", new AppleseedItemStack(Material.WOOL, (short)2));
     	itemStackNames.put("lightblue_wool", new AppleseedItemStack(Material.WOOL, (short)3));
     	itemStackNames.put("yellow_wool", new AppleseedItemStack(Material.WOOL, (short)4));
     	itemStackNames.put("lightgreen_wool", new AppleseedItemStack(Material.WOOL, (short)5));
     	itemStackNames.put("pink_wool", new AppleseedItemStack(Material.WOOL, (short)6));
     	itemStackNames.put("gray_wool", new AppleseedItemStack(Material.WOOL, (short)7));
     	itemStackNames.put("lightgray_wool", new AppleseedItemStack(Material.WOOL, (short)8));
     	itemStackNames.put("cyan_wool", new AppleseedItemStack(Material.WOOL, (short)9));
     	itemStackNames.put("purple_wool", new AppleseedItemStack(Material.WOOL, (short)10));
     	itemStackNames.put("blue_wool", new AppleseedItemStack(Material.WOOL, (short)11));
     	itemStackNames.put("brown_wool", new AppleseedItemStack(Material.WOOL, (short)12));
     	itemStackNames.put("darkgreen_wool", new AppleseedItemStack(Material.WOOL, (short)13));
     	itemStackNames.put("red_wool", new AppleseedItemStack(Material.WOOL, (short)14));
     	itemStackNames.put("black_wool", new AppleseedItemStack(Material.WOOL, (short)15));
     	itemStackNames.put("spruce_log", new AppleseedItemStack(Material.LOG, (short)1));
     	itemStackNames.put("birch_log", new AppleseedItemStack(Material.LOG, (short)2));
     	itemStackNames.put("spruce_sapling", new AppleseedItemStack(Material.SAPLING, (short)1));
     	itemStackNames.put("birch_sapling", new AppleseedItemStack(Material.LOG, (short)2));
    }
}
