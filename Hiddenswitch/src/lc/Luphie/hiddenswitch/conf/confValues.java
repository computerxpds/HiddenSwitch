package lc.Luphie.hiddenswitch.conf;

import java.util.ArrayList;
import java.util.List;

public class confValues {

	public List<Integer> useableBlocks = new ArrayList<Integer>();
	public boolean confLeftClicks;
	public boolean signAllowSigns;
	public String signSignText;
	public boolean signAllowULock;
	public boolean signAllowILock;
	public int signILockOverride;
	public boolean blockAllowBlocks;

	public void blockString(String string) {
		String[] strings = string.split(",");
		for(String toInt : strings) {
			useableBlocks.add(Integer.parseInt(toInt));
		}
	}
}
