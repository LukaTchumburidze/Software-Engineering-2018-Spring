package assign1;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;

public class TetrisGridTest {
	
	// Provided simple clearRows() test
	// width 2, height 3 grid
	@Test
	public void testClear1() {
		boolean[][] before =
		{	
			{true, true, false, },
			{false, true, true, }
		};
		
		boolean[][] after =
		{	
			{true, false, false},
			{false, true, false}
		};
		
		TetrisGrid tetris = new TetrisGrid(before);
		tetris.clearRows();

		assertTrue( Arrays.deepEquals(after, tetris.getGrid()) );
	}
	
	@Test
	public void testTetrisGrid1() {
		//Only one row which should be cleared
		boolean[][] before = {
				{true}
		};

		boolean[][] after = {
				{false}
		};
		TetrisGrid tetris = new TetrisGrid(before);
		//Test getGrid
		assertTrue(Arrays.deepEquals(before, tetris.getGrid()));
		//Test clearRows
		tetris.clearRows();
		assertTrue(Arrays.deepEquals(after, tetris.getGrid()));
	}

	@Test
	public void testTetrisGrid2() {
		//Do not mind the pattern of Array's Values!!!
		boolean[][] before = {
				{true, false, false, false},
				{true, false, false, false},
				{true, true, true, true},
				{true, false, false, false},
				{true, false, false, false}
		};

		boolean[][] after = {
				{false, false, false, false},
				{false, false, false, false},
				{true, true, true, false},
				{false, false, false, false},
				{false, false, false, false}
		};
		TetrisGrid tetris = new TetrisGrid(before);
		//Test getGrid
		assertTrue(Arrays.deepEquals(before, tetris.getGrid()));
		//Test clearRows
		tetris.clearRows();
		assertTrue(Arrays.deepEquals(after, tetris.getGrid()));
	}

	@Test
	public void testTetrisGrid3() {
		//Every row has exactly 1 false cell
		boolean[][] before = {
				{false, true, true, true},
				{true, false, true, true},
				{true, true, false, true},
				{true, true, true, false}
		};

		boolean[][] after = {
				{false, true, true, true},
				{true, false, true, true},
				{true, true, false, true},
				{true, true, true, false}
		};
		TetrisGrid tetris = new TetrisGrid(before);
		//Test getGrid
		assertTrue(Arrays.deepEquals(before, tetris.getGrid()));
		//Test clearRows
		tetris.clearRows();
		assertTrue(Arrays.deepEquals(after, tetris.getGrid()));
	}

	@Test
	public void testTetrisGrid4() {
		//Several rows should be cleared
		boolean[][] before = {
				{true, true, true, false},
				{true, false, true, true},
				{true, true, true, true},
				{true, true, true, true}
		};

		boolean[][] after = {
				{true, false, false, false},
				{false, true, false, false},
				{true, true, false, false},
				{true, true, false, false}
		};
		TetrisGrid tetris = new TetrisGrid(before);
		//Test getGrid
		assertTrue(Arrays.deepEquals(before, tetris.getGrid()));
		//Test clearRows
		tetris.clearRows();
		assertTrue(Arrays.deepEquals(after, tetris.getGrid()));
	}

	@Test
	public void testTetrisGrid5() {
		//Big test, every row should be cleared, except one
		boolean[][] before = {
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, false, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true},
				{true, true, true, true, true, true, true, true, true, true, true}
		};

		boolean[][] after = {
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{false, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false}
		};
		TetrisGrid tetris = new TetrisGrid(before);
		//Test getGrid
		assertTrue(Arrays.deepEquals(before, tetris.getGrid()));
		//Test clearRows
		tetris.clearRows();
		assertTrue(Arrays.deepEquals(after, tetris.getGrid()));
	}
	
}
