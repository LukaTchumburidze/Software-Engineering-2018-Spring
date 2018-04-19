package assign2;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.*;

import com.sun.xml.internal.ws.wsdl.writer.document.soap.Body;
import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;

	private Piece[] Pieces;

	@Before
	public void setUp() throws Exception {
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		Pieces = Piece.getPieces();
	}

	// Here are some sample tests to get you started
	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		//System.out.println(Arrays.toString(pyr3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}

	//Checking rotation periods for each Piece
	@Test
	public void checkPeriods () {
		int[] periods = {2, 4, 4, 2, 2, 1, 4};

		Piece curPiece;
		int period;
		for (int i = 0; i < Pieces.length; i ++) {
			curPiece = Pieces[i].fastRotation();
			period = 1;
			while (curPiece != Pieces[i]) {
				period ++;
				curPiece = curPiece.fastRotation();
			}
			assertEquals(period, periods[i]);
		}
	}

	@Test
	public void rotatedStickIsNotPyramid() {
		Piece stick = Pieces[Piece.STICK];
		Piece rotatedStick = stick.fastRotation();
		Piece pyramid = Pieces[Piece.PYRAMID];

		assertEquals(false, rotatedStick.equals(pyramid));
	}

	@Test
	public void checkPiecesWithDifferentLengths() {
		Piece stick = Pieces[Piece.STICK];
		TPoint[] Body = new TPoint[2];
		Body[0] = new TPoint(0, 0);
		Body[1] = new TPoint(0, 0);

		Piece uglyPiece = new Piece(Body);
		assertEquals(false, stick.equals(uglyPiece));

		/*
			Nothing can change ugly body,
			There is no salvation for defected!

			Death is the only remedy for hunchback!
		 */
		assertEquals(Body, uglyPiece.getBody());
	}

	@Test
	public void passMutatedString() {
		try {
			Piece mutation = new Piece("Gigo /-_-\\");
			fail();
		} catch (Exception e) {

		}
	}


	/**
		Following tests contain changed, but
	 	valid strings as constructors, I compare them
	 	with already created pieces from Pieces array.
	 	Since strings are valid all Pieces produced by
	 	them should be same as ones created in Pieces array
	 */
	@Test
	public void fuckedUpStick () {
		String fuckedUpStickString	= "0 3	0 1  0 0  0 2";
		Piece fuckedUpStick = new Piece (fuckedUpStickString);
		Piece stick = Pieces[Piece.STICK];

		assertEquals(true, stick.equals(fuckedUpStick));
		//Check if their rotations are equal as well
		assertEquals(true, fuckedUpStick.computeNextRotation().equals(stick.fastRotation()));
	}

	@Test
	public void fuckedUpSquare () {
		String fuckedUpSquareString	= "1 0  0 0  0 1  1 1";
		Piece fuckedUpSquare = new Piece (fuckedUpSquareString);
		Piece square = Pieces[Piece.SQUARE];

		assertEquals(true, square.equals(fuckedUpSquare));
		//Check if their rotations are equal as well, whatever
		assertEquals(true, fuckedUpSquare.computeNextRotation().equals(square.fastRotation()));
	}

	@Test
	public void fuckedUpS1 () {
		String fuckedUpS1String	= "0 0  2 1  1 0  1 1";
		Piece fuckedUpS1 = new Piece (fuckedUpS1String);
		Piece s1 = Pieces[Piece.S1];

		assertEquals(true, s1.equals(fuckedUpS1));
		//Check if their rotations are equal as well, whatever
		assertEquals(true, fuckedUpS1.computeNextRotation().equals(s1.fastRotation()));
	}

	@Test
	public void fuckedUpPyramid () {
		String fuckedUpPyramidString	= "2 0  1 1  0 0  1 0";
		Piece fuckedUpPyramid = new Piece (fuckedUpPyramidString);
		Piece pyramid = Pieces[Piece.PYRAMID];

		assertEquals(true, pyramid.equals(fuckedUpPyramid));
		//Check if their rotations are equal as well, whatever
		assertEquals(true, fuckedUpPyramid.computeNextRotation().equals(pyramid.fastRotation()));
	}

	@Test
	public void fuckedUpL1 () {
		String fuckedUpL1String	= "0 1  0 0  1 0  0 2";
		Piece fuckedUpL1 = new Piece (fuckedUpL1String);
		Piece L1 = Pieces[Piece.L1];

		assertEquals(true, L1.equals(fuckedUpL1));
		//Check if their rotations are equal as well, whatever
		assertEquals(true, fuckedUpL1.computeNextRotation().equals(L1.fastRotation()));
	}


	/**
	 * Following test checks dimensions for the
	 * Tetris pieces and for their rotations
	 */
	@Test
	public void checkDimensionsStick () {
		Piece stick = Pieces[Piece.STICK];

		assertEquals(1, stick.getWidth());
		assertEquals(4, stick.getHeight());

		stick = stick.fastRotation();

		assertEquals(4, stick.getWidth());
		assertEquals(1, stick.getHeight());
	}

	@Test
	public void checkDimensionsS1 () {
		Piece s1 = Pieces[Piece.S1];

		assertEquals(3, s1.getWidth());
		assertEquals(2, s1.getHeight());

		s1 = s1.fastRotation();

		assertEquals(2, s1.getWidth());
		assertEquals(3, s1.getHeight());

		s1 = s1.fastRotation();
		assertEquals(3, s1.getWidth());
		assertEquals(2, s1.getHeight());
	}

	@Test
	public void checkDimensionsSquare () {
		Piece square = Pieces[Piece.SQUARE];

		assertEquals(square.getHeight(), square.getHeight());

		square = square.fastRotation();

		assertEquals(square.getHeight(), square.getHeight());
	}

	@Test
	public void checkDimensionsL1 () {
		Piece l1 = Pieces[Piece.L1];

		assertEquals(2, l1.getWidth());
		assertEquals(3, l1.getHeight());

		l1 = l1.fastRotation();

		assertEquals(3, l1.getWidth());
		assertEquals(2, l1.getHeight());

		l1 = l1.fastRotation();
		assertEquals(2, l1.getWidth());
		assertEquals(3, l1.getHeight());
	}

	/**
	 * Following tests will check the skirt
	 * array for every pieces each rotation
	 */

	@Test
	public void checkL1Skirt () {
		Piece l1 = Pieces[Piece.L1];
		int[] skirt = new int[]{0, 0};
		assertTrue(Arrays.equals(l1.getSkirt(), skirt));

		l1 = l1.fastRotation();
		skirt = new int[]{0, 0, 0};
		assertTrue(Arrays.equals(l1.getSkirt(), skirt));

		l1 = l1.fastRotation();
		skirt = new int[]{2, 0};
		assertTrue(Arrays.equals(l1.getSkirt(), skirt));

		l1 = l1.fastRotation();
		skirt = new int[]{0, 1, 1};
		assertTrue(Arrays.equals(l1.getSkirt(), skirt));
	}

	@Test
	public void checkL2Skirt () {
		Piece l2 = Pieces[Piece.L2];
		int[] skirt = new int[]{0, 0};
		assertTrue(Arrays.equals(l2.getSkirt(), skirt));

		l2 = l2.fastRotation();
		skirt = new int[]{1, 1, 0};
		assertTrue(Arrays.equals(l2.getSkirt(), skirt));

		l2 = l2.fastRotation();
		skirt = new int[]{0, 2};
		assertTrue(Arrays.equals(l2.getSkirt(), skirt));

		l2 = l2.fastRotation();
		skirt = new int[]{0, 0, 0};
		assertTrue(Arrays.equals(l2.getSkirt(), skirt));
	}

	@Test
	public void checkSquareSkirt() {
		Piece square = Pieces[Piece.SQUARE];
		int[] skirt = new int[]{0, 0};
		assertTrue(Arrays.equals(square.getSkirt(), skirt));

		//It's still the same but, whatever, check it!
		square = square.fastRotation();
		assertTrue(Arrays.equals(square.getSkirt(), skirt));
	}

	@Test
	public void checkStickSkirt() {
		Piece stick = Pieces[Piece.STICK];
		int[] skirt = new int[]{0};
		assertTrue(Arrays.equals(stick.getSkirt(), skirt));

		stick = stick.fastRotation();
		skirt = new int[]{0, 0, 0, 0};
		assertTrue(Arrays.equals(stick.getSkirt(), skirt));
	}

	@Test
	public void checkS1Skirt() {
		Piece s1 = Pieces[Piece.S1];
		int[] skirt = new int[]{0, 0, 1};
		assertTrue(Arrays.equals(s1.getSkirt(), skirt));

		s1 = s1.fastRotation();
		skirt = new int[]{1, 0};
		assertTrue(Arrays.equals(s1.getSkirt(), skirt));
	}

	@Test
	public void checkS2Skirt() {
		Piece s2 = Pieces[Piece.S2];
		int[] skirt = new int[]{1, 0, 0};
		assertTrue(Arrays.equals(s2.getSkirt(), skirt));

		s2 = s2.fastRotation();
		skirt = new int[]{0, 1};
		assertTrue(Arrays.equals(s2.getSkirt(), skirt));
	}

	@Test
	public void checkPyramid() {
		Piece pyramid = Pieces[Piece.PYRAMID];
		int[] skirt = new int[]{0, 0, 0};
		assertTrue(Arrays.equals(pyramid.getSkirt(), skirt));

		pyramid = pyramid.fastRotation();
		skirt = new int[]{1, 0};
		assertTrue(Arrays.equals(pyramid.getSkirt(), skirt));

		pyramid = pyramid.fastRotation();
		skirt = new int[]{1, 0, 1};
		assertTrue(Arrays.equals(pyramid.getSkirt(), skirt));

		pyramid = pyramid.fastRotation();
		skirt = new int[]{0, 1};
		assertTrue(Arrays.equals(pyramid.getSkirt(), skirt));
	}
}
