package assign1;

import com.sun.deploy.util.StringUtils;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		int ans  = 1, curAdj = 1;
		for (int i = 1; i < str.length(); i ++) {
			if (str.charAt(i-1) != str.charAt(i)) {
				ans = Math.max(ans, curAdj);
				curAdj = 1;
			} else {
				curAdj ++;
			}
		}

		return Math.min(ans, str.length());
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		String ans = "";
		for (int i = 0; i < str.length()-1; i ++) {
			if (str.charAt(i) >= '0' && str.charAt(i) < '9') {
				ans = ans + repeat (str.charAt(i+1), (int)str.charAt(i)-'0');
			} else {
				ans = ans + str.charAt(i);
			}
		}
		if (str.length() > 0 && (str.charAt(str.length()-1) < '0' || str.charAt(str.length()-1) > '9')) {
			ans = ans + str.charAt(str.length()-1);
		}

		return ans;
	}

	private static String repeat (char ch, int n) {
		String ans = "";
		for (int i = 0; i < n ; i ++) {
			ans = ans + ch;
		}

		return ans;
	}
	
	/**
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		HashSet <String> hSet = new HashSet<>();

		for (int i = 0; i <= a.length() - len; i ++) {
			String str = a.substring(i, i+len);
			hSet.add(str);
		}

		for (int i = 0; i <= b.length()-len; i ++) {
			String str = b.substring(i, i+len);
			if (hSet.contains(str)) {
				return true;
			}
		}

		return false;
	}
}
