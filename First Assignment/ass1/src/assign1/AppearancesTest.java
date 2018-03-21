package assign1;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class AppearancesTest {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}
	
	@Test
	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}
	
	@Test
	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}

	@Test
	public void testSameCount3() {
		//Compare Different Collections
		HashSet <Integer> hSet = new HashSet<Integer>(){{
			add (1);
			add (2);
			add (3);
			add (4);
			add (5);
		}};

		List <Integer> ls = Arrays.asList(1, 1, 1, 2, 3, 4, 4, 4, 5);

		assertEquals(3, Appearances.sameCount(hSet, ls));
	}

	@Test
	public void testSameCount4() {
		//Compare HashSet with an empty List
		HashSet <Integer> hSet = new HashSet<Integer>(){{
			add (1);
			add (2);
			add (3);
		}};

		List<Integer> ls = new LinkedList<>();

		assertEquals(0, Appearances.sameCount(hSet, ls));
	}

	@Test
	public void testSameCount5() {
		//Compare an empty List with HashSet
		List <Integer> ls = Arrays.asList(1, 2, 3);
		HashSet <Integer> hSet = new HashSet<>();

		assertEquals(0, Appearances.sameCount(hSet, ls));
	}

	@Test
	public void testSameCount6() {
		//Compare collections of String
		List<String> ls1 = new LinkedList<String>(){{
			add ("abc");
			add ("abc");
			add ("abc");
			add ("a");
			add ("a");
			add ("ay");
		}};
		List<String> ls2 = new LinkedList<String>(){{
			add ("abc");
			add ("abc");
			add ("a");
			add ("a");
			add ("b");
		}};
		assertEquals(1, Appearances.sameCount(ls1, ls2));
		ls2.add("abc");
		ls2.remove("b");
		ls2.add("ay");
		assertEquals(3, Appearances.sameCount(ls1, ls2));
	}
}
