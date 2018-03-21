/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/
package assign1;

import java.util.*;

public class Taboo<T> {

	private HashMap <T, HashSet<T> > hMap;
	
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		hMap = new HashMap<T, HashSet<T> >();
		for (int i = 0; i < rules.size()-1; i ++) {
			if (rules.get(i) == null || rules.get(i+1) == null) {
				continue;
			}
			if (!hMap.containsKey(rules.get(i))) {
				hMap.put(rules.get(i), new HashSet<>());
			}
			HashSet<T> temp = hMap.get(rules.get(i));
			temp.add(rules.get(i+1));
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		 if (hMap.containsKey(elem)) {
			return hMap.get(elem);
		 }
		 return Collections.emptySet();
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		for (int i = 0; i < list.size()-1; i ++) {
			if (hMap.containsKey(list.get(i))) {
				if (hMap.get(list.get(i)).contains(list.get(i+1))) {
					list.remove(i+1);
					i --;
				}
			}
		}
	}
}
