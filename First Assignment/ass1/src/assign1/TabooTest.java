// TabooTest.java
// Taboo class tests -- nothing provided.
package assign1;

import static org.junit.Assert.*;
import org.junit.Test;
import sun.awt.image.ImageWatched;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class TabooTest {

    @Test
    public void testTaboo1 () {
        //All of the elements are null
        LinkedList <String> ls = new LinkedList<String>(){{
            add (null);
            add (null);
            add (null);
            add (null);
            add (null);
        }};
        Taboo t = new Taboo(ls);
        assertEquals(Collections.emptySet(), t.noFollow("string1"));
        assertEquals(Collections.emptySet(), t.noFollow(""));
        LinkedList <String> nextInput = new LinkedList<String>(){{
            add("");
            add("a");
            add("b");
            add("c");
            add("d");
            add("e");
            add("f");
        }};
        LinkedList <String> nextAns = new LinkedList<String>(nextInput);
        t.reduce(nextInput);
        assertEquals(nextInput, nextAns);
    }

    @Test
    public void testTaboo2 () {
        //All of the elements can't be preceded by anything
        LinkedList <Character> ls = new LinkedList<Character>(){{
            add ('a');
            add ('a');
            add (null);
            add ('a');
            add ('b');
            add ('a');
            add (null);
            add ('a');
            add ('c');
            add ('a');
            add (null);
            add ('b');
            add ('b');
            add (null);
            add ('b');
            add ('c');
            add ('b');
            add (null);
            add ('c');
            add ('c');
        }};

        Taboo t = new Taboo(ls);
        HashSet<Character> ans = new HashSet<Character>(){{
            add ('a');
            add ('b');
            add ('c');
        }};
        assertEquals(ans, t.noFollow('a'));
        assertEquals(ans, t.noFollow('b'));
        assertEquals(ans, t.noFollow('c'));
        LinkedList<Character> reduceInput = new LinkedList<Character>(){{
            add ('a');
            add ('a');
            add ('b');
            add ('c');
            add ('a');
            add ('c');
            add ('a');
            add ('a');
            add ('c');
        }};
        LinkedList<Character> reduceAnswer = new LinkedList<Character>(){{
            add('a');
        }};
        t.reduce(reduceInput);
        assertEquals(reduceAnswer, reduceInput);
        reduceInput.add('k');
        reduceInput.add('a');
        reduceInput.add('m');
        reduceInput.add('b');
        reduceAnswer = reduceInput;
        t.reduce(reduceInput);
        assertEquals(reduceAnswer, reduceInput);

    }
}
