package lc.Luphie.hiddenswitch.conf;

import org.bukkit.World;

public class KeyBlock {
	public String id;
	public String users;
	public String key;
	public int x;
	public int y;
	public int z;
	public String world;
	
	KeyBlock(String id, String world, int x, int y, int z, String users, String key){
		this.id = id;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.users = users;
		this.key = key;
	}
	KeyBlock(World world) {
		this.world = world.getName();
	}
	
}
