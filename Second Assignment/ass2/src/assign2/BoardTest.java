package assign2;

import static org.junit.Assert.*;

import org.junit.*;

import java.io.PipedReader;
import java.util.Arrays;

public class BoardTest {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;


	Board b1x1, b4x4, b8x5, b1000x100;
	Piece[] Pieces;

	Piece s1R0, s1R1;
	Piece s2R0,s2R1;
	Piece l1R0, l1R1, l1R2, l1R3;
	Piece l2R0, l2R1, l2R2, l2R3;
	Piece pyramidR0, pyramidR1, pyramidR2, pyramidR3;
	Piece stickR0, stickR1;
	Piece square;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
	}

	/**
	 * Here I generate all the needed pieces for the tests
	 */

	@Before
	public void mySetUp () {
		Pieces = Piece.getPieces();

		s1R0 = Pieces[Piece.S1];
		s1R1 = s1R0.fastRotation();

		s2R0 = Pieces[Piece.S2];
		s2R1 = s2R0.fastRotation();

		l1R0 = Pieces[Piece.L1];
		l1R1 = l1R0.fastRotation();
		l1R2 = l1R1.fastRotation();
		l1R3 = l1R2.fastRotation();

		l2R0 = Pieces[Piece.L2];
		l2R1 = l2R0.fastRotation();
		l2R2 = l2R1.fastRotation();
		l2R3 = l2R2.fastRotation();

		pyramidR0 = Pieces[Piece.PYRAMID];
		pyramidR1 = pyramidR0.fastRotation();
		pyramidR2 = pyramidR1.fastRotation();
		pyramidR3 = pyramidR2.fastRotation();

		stickR0 = Pieces[Piece.STICK];
		stickR1 = stickR0.fastRotation();

		square = Pieces[Piece.SQUARE];

		b1x1 = new Board(1, 1);
		b4x4 = new Board(4, 4);
		b8x5 = new Board(8, 5);
		b1000x100 = new Board(1000, 1000);
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	
	// Make  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.

	/**
	 * In this test all place() calls should return
	 * PLACE_OUT_BOUNDS
	 */

	@Test
	public void testWrongPlacings() {
		Piece stickR0 = Pieces[Piece.STICK];
		Piece stickR1 = stickR0.fastRotation();

		Piece pyramidR0 = Pieces[Piece.PYRAMID];
		Piece pyramidR1 = pyramidR0.fastRotation();

		assertEquals (Board.PLACE_OUT_BOUNDS, b8x5.place(stickR0, 0, 2));
		b8x5.undo();
		b8x5.sanityCheck();
		assertEquals (Board.PLACE_OUT_BOUNDS, b8x5.place(stickR0, 8, 1));
		b8x5.undo();
		assertEquals (Board.PLACE_OUT_BOUNDS, b1x1.place(stickR1,0, 0));
		b1x1.undo();

		assertEquals(Board.PLACE_OUT_BOUNDS, b8x5.place(pyramidR0, 0, 4));
		b8x5.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b8x5.place(pyramidR1, 0, 100));
		b8x5.undo();
		b8x5.sanityCheck();
		assertEquals(Board.PLACE_OUT_BOUNDS, b1x1.place(pyramidR0, 0, 0));
		b1x1.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b1x1.place(pyramidR1, 0, 0));
		b1x1.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b1x1.place(pyramidR0, 0, 2));
		b1x1.undo();
		b8x5.sanityCheck();
	}

	@Test
	public void sanityCheckTest () {
		b1x1.sanityCheck();
	}

	/**
	 * Fully test 8x5 board with various placings
	 * and row cleanings
	 */

	@Test
	public void test8x5 () {
		/*
			Place several pieces neatly
			(some PLACE_BAD and PLACE_OUT_BOUNDS responses ahead!)
		 */

		assertEquals(Board.PLACE_OK, b8x5.place(pyramidR0, 0, 0));
		b8x5.commit();
		b8x5.sanityCheck();
		assertEquals(Board.PLACE_OK, b8x5.place(s2R1, 0, 1));
		b8x5.commit();
		assertEquals(Board.PLACE_BAD, b8x5.place(s2R1, 0, 2));
		b8x5.undo();
		b8x5.sanityCheck();
		assertEquals(Board.PLACE_OUT_BOUNDS, b8x5.place(s2R1, 0, 3));
		b8x5.undo();
		assertEquals(Board.PLACE_BAD, b8x5.place(stickR0, 2, 0));
		b8x5.undo();
		assertEquals(Board.PLACE_OK, b8x5.place(stickR0, 2, 1));
		b8x5.commit();
		b8x5.sanityCheck();
		//Nothing should change
		assertEquals(0, b8x5.clearRows());
		b8x5.sanityCheck();
		b8x5.commit();
		assertEquals(Board.PLACE_OK, b8x5.place(stickR0, 3, 0));
		b8x5.commit();
		assertEquals(Board.PLACE_OUT_BOUNDS, b8x5.place(stickR0, 5, 3));
		b8x5.undo();
		assertEquals(Board.PLACE_ROW_FILLED, b8x5.place(stickR1, 4, 0));
		b8x5.commit();

		//Clear bottom row
		assertEquals(1, b8x5.clearRows());
		b8x5.commit();
		//Add another rotated stick on same place
		assertEquals(Board.PLACE_BAD, b8x5.place(stickR1, 3, 0));
		b8x5.undo();
		b8x5.undo();
		b8x5.undo();
		assertEquals(Board.PLACE_ROW_FILLED, b8x5.place(stickR1, 4, 0));
		b8x5.commit();
		//Clear again
		assertEquals(1, b8x5.clearRows());
		//Just bunch of commits
		b8x5.commit();
		b8x5.commit();
		b8x5.commit();
		//And again
		assertEquals(Board.PLACE_ROW_FILLED, b8x5.place(stickR1, 4, 0));
		b8x5.commit();
		assertEquals(1, b8x5.clearRows());
		b8x5.commit();
	}


	/**
	 * Just testing 1x1 board, every place() call
	 * should return out of bounds
	 */

	@Test
	public void test1x1 () {
		assertEquals(1, b1x1.getWidth());
		assertEquals(1, b1x1.getHeight());

		assertEquals(Board.PLACE_OUT_BOUNDS, b1x1.place(stickR0, 0, 0));
		b1x1.undo();
		b1x1.sanityCheck();
		assertEquals(Board.PLACE_OUT_BOUNDS, b1x1.place(square, 0, 0));
		b1x1.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b1x1.place(pyramidR0, 0, 0));
		b1x1.sanityCheck();
		b1x1.undo();
		assertEquals(Board.PLACE_OUT_BOUNDS, b1x1.place(l1R0, 0, 0));
		b1x1.undo();

		assertEquals(0, b1x1.getColumnHeight(0));
		assertEquals(0, b1x1.getRowWidth(0));
		b1x1.sanityCheck();
		assertEquals(0, b1x1.getMaxHeight());
		assertEquals(0, b1x1.clearRows());
		b1x1.sanityCheck();
	}

	/**
	 * Tests 4x4, fills it with 4 squares and then cleans it
	 */

	@Test
	public void test4x4 () {
		assertEquals(Board.PLACE_OK, b4x4.place(square, 0, 0));
		b4x4.commit();
		assertEquals(Board.PLACE_OK, b4x4.place(square, 0, 2));
		//Whoops I have forgotten to commit
		try {
			assertEquals(Board.PLACE_ROW_FILLED, b4x4.place(square, 2, 0));
		} catch (RuntimeException e) {
			b4x4.commit();
			assertEquals(Board.PLACE_ROW_FILLED, b4x4.place(square, 2, 0));
		}
		b4x4.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b4x4.place(square, 2, 2));
		b4x4.commit();

		//Clear the rows
		assertEquals(4, b4x4.clearRows());
		b4x4.commit();

		//Make sure all columns' heights are 0
		//assertEquals(0, b4x4.getMaxHeight());


	}

	/**
	 * Does the same thing but with sticks
	 */

	@Test
	public void test4x4Sticks () {
		assertEquals(Board.PLACE_OK, b4x4.place(stickR0, 0, 0));
		b4x4.commit();
		assertEquals(Board.PLACE_OK, b4x4.place(stickR0, 1, 0));
		b4x4.commit();
		assertEquals(Board.PLACE_OK, b4x4.place(stickR0, 2, 0));
		b4x4.commit();
		assertEquals(Board.PLACE_ROW_FILLED, b4x4.place(stickR0, 3, 0));
		b4x4.commit();

		assertEquals(4, b4x4.clearRows());
		b4x4.commit();
	}

	/**
	 * Just big test
	 */

	@Test
	public void test1000x100 () {
		for (int i = 0; i < 999; i ++) {
			for (int j = 0; j < 100; j += stickR0.getHeight()) {
				assertEquals(Board.PLACE_OK, b1000x100.place(stickR0, i, j));
				b1000x100.commit();
			}
		}

		while (b1000x100.getMaxHeight() > 0) {
			assertEquals(Board.PLACE_ROW_FILLED, b1000x100.place(stickR0, 999, 0));
			b1000x100.commit();
			assertEquals(4, b1000x100.clearRows());
			b1000x100.commit();
		}
	}

	/**
	 * Last test, calling various functions
	 */
	@Test
	public void lastTest () {
		Board curBoard = b8x5;
		assertEquals(8, curBoard.getWidth());
		assertEquals(5, curBoard.getHeight());
		assertEquals(Board.PLACE_OK, curBoard.place(stickR1, 0, 0));
		//This should throw RuntimeException
		try {
			assertEquals(Board.PLACE_OK, curBoard.place(stickR0, 0, 1));
			fail();
		} catch (RuntimeException e) {

		}
		curBoard.commit();
		assertTrue(curBoard.getGrid(0, 0));
		assertTrue(curBoard.getGrid(1, 0));
		assertTrue(curBoard.getGrid(2, 0));
		assertTrue(curBoard.getGrid(3, 0));
		assertTrue(curBoard.getGrid(100000, 1));
		assertTrue(curBoard.getGrid(100000, 2));

		curBoard.toString();

	}

}
