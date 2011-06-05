package redsgreens.Appleseed;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * AppleseedLocation used instead of the Bukkit Location class so locations can be tracked without the world they belong to being loaded
 *
 * @author redsgreens
 */

public class AppleseedLocation {

	private Double X;
	private Double Y;
	private Double Z;
	private String worldName;
	
	public AppleseedLocation(String w, Double x, Double y, Double z)
	{
		worldName = w;
		X = x;
		Y = y;
		Z = z;
	}
	
	public AppleseedLocation(Location loc)
	{
		worldName = loc.getWorld().getName();
		X = loc.getX();
		Y = loc.getY();
		Z = loc.getZ();
	}
	
	public Location getLocation()
	{
		World w = Appleseed.Plugin.getServer().getWorld(worldName);
		if(w == null)
			return null;
		else
			return new Location(w, X, Y, Z);
	}
	
	public Double getX()
	{
		return X;
	}

	public Double getY()
	{
		return Y;
	}

	public Double getZ()
	{
		return Z;
	}
	
	public String getWorldName()
	{
		return worldName;
	}
	
	public boolean equals(Object other) 
	{
	    if (this == other)
	      return true;
	    if (!(other instanceof AppleseedLocation))
	      return false;
	    AppleseedLocation otherLoc = (AppleseedLocation) other;
	    return ((X.equals(otherLoc.X)) && (Y.equals(otherLoc.Y)) && (Z.equals(otherLoc.Z)) && (worldName.equalsIgnoreCase(otherLoc.getWorldName()))); 
	}

	public int hashCode() { 
		return worldName.hashCode() + X.hashCode() + Y.hashCode() + Z.hashCode();	
	}

}
