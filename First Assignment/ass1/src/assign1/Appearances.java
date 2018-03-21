package assign1;

import java.util.*;

public class Appearances {
	
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		HashMap <T, Integer> hMap1 = new HashMap<T, Integer>();
		HashMap <T, Integer> hMap2 = new HashMap<T, Integer>();

		fillMap (hMap1, a);
		fillMap (hMap2, b);
		int ans = 0;

		for (T el: hMap2.keySet()) {
			if (hMap1.containsKey(el)) {
				if (hMap1.get(el).equals(hMap2.get(el))) {
					ans ++;
				}
			}
		}

		return ans;
	}

	private static <T> void fillMap (HashMap <T, Integer> hMap,  Collection<T> b) {
		for (T el:b) {
			if (!hMap.containsKey(el)) {
				hMap.put(el, 1);
			} else {
				int val = hMap.get(el);
				hMap.put(el, val+1);
			}
		}
	}
	
}
