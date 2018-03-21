// Test cases for CharGrid -- a few basic tests are provided.
package assign1;

import static org.junit.Assert.*;
import org.junit.Test;

public class CharGridTest {
	
	@Test
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
			};
		
		
		CharGrid cg = new CharGrid(grid);
				
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	
	
	@Test
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
			};
		
		CharGrid cg = new CharGrid(grid);
		
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}
	
	@Test
	public void testCharArea3() {
		//whole grid is filled with same character
		//except one
		char[][] grid = new char[][] {
				{'z', 'z', 'z', 'z', 'z'},
				{'z', 'z', 'z', 'z', 'z'},
				{'z', 'z', 'z', 'z', 'z'},
				{'z', 'z', 'z', 'z', 'z'},
				{'z', 'z', 'a', 'z', 'z'},
				{'z', 'z', 'z', 'z', 'z'},
				{'z', 'z', 'z', 'z', 'z'},
				{'z', 'z', 'z', 'z', 'z'},
				{'z', 'z', 'z', 'z', 'z'},
		};
		CharGrid cg = new CharGrid(grid);

		assertEquals(1, cg.charArea('a'));
		assertEquals(45, cg.charArea('z'));
		assertEquals(0, cg.charArea('b'));
	}

	@Test
	public void testCharArea4() {
		//Non-rectangular grid
		char[][] grid = new char[][] {
				{'a', 'b', 'a', 'a'},
				{'z', 'z'},
				{'c', 'c', 'a'},
				{'k', 'z', 'k'},
				{'a'}
		};
		CharGrid cg = new CharGrid(grid);

		assertEquals(20, cg.charArea('a'));
		assertEquals(6, cg.charArea('z'));
		assertEquals(1, cg.charArea('b'));
		assertEquals(2, cg.charArea('c'));
		assertEquals(0, cg.charArea('p'));
	}

	@Test
	public void testCharArea5() {
		//Empty Grid
		char[][] grid = new char[][] {{}};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.charArea('a'));
		assertEquals(0, cg.charArea('z'));
	}

	@Test
	public void testCountPlus1 () {
		//Empty Grid
		char[][] grid = new char[][] {{}};
		CharGrid cg = new CharGrid(grid);

		assertEquals(0, cg.countPlus());
	}

	@Test
	public void testCountPlus2 () {
		//3/19/2018 11:03PM, Hope this helps
		char[][] grid = new char[][] {
				{'i', 'e', 's', 'o'},
				{' ', 'X', ' ', ' '},
				{'X', 'X', 'X', ' '},
				{' ', 'X', ' ', ' '},
		};
		CharGrid cg = new CharGrid(grid);

		assertEquals(1, cg.countPlus());
	}

	@Test
	public void testCountPlus3 () {
		//Non-Rectangular grid
		char[][] grid = new char[][] {
				{' ', 'X'},
				{'X', 'X', 'X', ' '},
				{' ', 'X'},
		};
		CharGrid cg = new CharGrid(grid);

		assertEquals(1, cg.countPlus());
	}

	@Test
	public void testCountPlus4 () {
		//Several Crosses
		char[][] grid = new char[][] {
				{' ', 'a', ' ', ' ', 'b', ' ', ' '},
				{'a', 'a', 'a', ' ', 'b', ' ', ' '},
				{' ', 'a', 'b', 'b', 'b', 'b', 'b'},
				{' ', ' ', ' ', ' ', 'b', ' ', ' '},
				{' ', ' ', 'b', ' ', 'b', 'c', ' '},
				{' ', 'b', 'b', 'b', 'c', 'c', 'c'},
				{' ', ' ', 'b', 'k', ' ', 'c', ' '},
				{' ', ' ', 'k', 'k', 'k', ' ', ' '},
				{' ', ' ', ' ', 'k', ' ', ' ', ' '}
		};
		CharGrid cg = new CharGrid(grid);

		assertEquals(5, cg.countPlus());
	}
}
