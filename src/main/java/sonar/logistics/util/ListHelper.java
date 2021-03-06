package sonar.logistics.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class ListHelper {

	public static <T> HashSet<T> newHashSet(T...objs){
		HashSet<T> set = new HashSet<>();
		Collections.addAll(set, objs);
		return set;
	}

	public static <T> boolean addWithCheck(Collection<T> list, T toAdd) {
		if (toAdd != null && !list.contains(toAdd)) {
			list.add(toAdd);
			return true;
		}
		return false;
	}
	public static <T> boolean addWithCheck(Collection<T> list, T[] toAdd) {
		boolean wasAdded = false;
		for (T t : toAdd) {
			if (t != null && !list.contains(t)) {
				list.add(t);				
				wasAdded = true;
			}
		}
		return wasAdded;
	}

	public static <T> boolean addWithCheck(Collection<T> list, Collection<T> toAdd) {
		boolean wasAdded = false;
		for (T t : toAdd) {
			if (t != null && !list.contains(t)) {
				list.add(t);
				wasAdded=true;
			}
		}
		return wasAdded;
	}

	public static int[] getOrdinals(Enum[] enums) {
		int[] listTypes = new int[enums.length];
		for (int e = 0; e < listTypes.length; e++) {
			listTypes[e] = enums[e].ordinal();
		}
		return listTypes;
	}
}