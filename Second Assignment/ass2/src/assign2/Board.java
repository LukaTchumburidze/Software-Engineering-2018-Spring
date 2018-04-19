// Board.java
package assign2;

import com.sun.media.sound.SoftSynthesizer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;

	private int[] widths, heights;

	private ArrayList <Block> pastMoves;
	private int[] xWidths, xHeights;
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		
		widths = new int[height];
		heights = new int[width];

		pastMoves = new ArrayList<>();
		xWidths = new int[height];
		xHeights = new int[width];

		commit();
	}

	/**
	 * Simple Block class for storing coordinates
	 * and value for each changed grid block in pastMoves
	 */

	private class Block {
		int x, y;
		boolean val;

		Block (int x, int y, boolean val) {
			this.x = x;
			this.y = y;
			this.val = val;
		}
	}

	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {
		int max = 0;
		for (int i = 0; i < width; i ++) {
			max = Math.max(max, heights[i]);
		}

		return max;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (!DEBUG) {
			return;
		}

		for (int i = 0; i < height; i ++) {
			if (computeRowWidth(i) != widths[i]) {
				throw new RuntimeException("There was an incorrect value for widths[" + i + "]!");
			}
		}

		int maxHeight = 0;
		for (int i = 0; i < width; i ++) {
			maxHeight = Math.max(maxHeight, heights[i]);
			if (computeColumnHeight(i) != heights[i]) {
				throw new RuntimeException("There was an incorrect value for heights[" + i + "]!");
			}
		}
		if (maxHeight != getMaxHeight()) {
			throw new RuntimeException("There is something wrong with getMaxHeight() function!");
		}
	}

	/**
	 * Returns number of filled cells for
	 * p-th row
	 * @param p
	 * @return
	 */

	private int computeRowWidth (int p) {
		int cnt = 0;
		for (int i = 0; i < width; i ++) {
			if (grid[i][p]) {
				cnt ++;
			}
		}

		return cnt;
	}

	/**
	 * Returns height of q-th row
	 * @param q
	 * @return
	 */

	private int computeColumnHeight (int q) {
		int i = 0;
		for (i = height-1; i > -1; i --) {
			if (grid[q][i]) {
				break;
			}
		}

		return i+1;
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int max = 0;
		int[] skirt = piece.getSkirt();
		for (int i = 0; i < skirt.length; i ++) {
			max = Math.max (max, heights[x+i] - skirt[i]);
		}

		return max;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (x < width && y < height && x > -1 && y > -1) {
			return grid[x][y];
		}
		return true;
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;

		if (x+piece.getWidth() > width || y+piece.getHeight() > height || x < 0 || y < 0) {
			return PLACE_OUT_BOUNDS;
		}

		if (dropHeight(piece, x) > y) {
			return PLACE_BAD;
		}

		if (validPlace (piece, x, y)) {
			return PLACE_ROW_FILLED;
		}

		return PLACE_OK;
	}

	/**
	 * After some checks in the place function answer is
	 * either PLACE_ROW_FILLED or PLACE_OK this function
	 * helps me to neatly return the correct answer
	 * @param piece
	 * @param x
	 * @param y
	 * @return
	 */

	private boolean validPlace (Piece piece, int x, int y) {
		boolean rowFilled = false;
		TPoint[] body = piece.getBody();
		for (int i = 0; i < body.length; i ++) {
			rowFilled|= giveValue(x + body[i].x, y + body[i].y, true);
		}

		return rowFilled;
	}

	/**
	 * Just modifies single block of a piece to the board
	 * @param x
	 * @param y
	 * @return
	 */

	private boolean giveValue (int x, int y, boolean val) {
		pastMoves.add(new Block(x, y, grid[x][y]));
		grid[x][y] = val;
		if (val) {
			widths[y] ++;
			heights[x] = Math.max(heights[x], y+1);
		} else {
			widths[y] --;
		}

		if (widths[y] == width) {
			return true;
		}
		return false;
	}


	/**
	 * Generates the heights for all the columns
	 */

	private void generateHeights () {
		for (int i = 0; i < width; i ++) {
			heights[i] = computeColumnHeight(i);
		}
	}

	/**
	 * Clears top rows
 	 * @param p
	 */

	private void clearTopRows (int p) {
		for (int i = p; i < height; i ++) {
			widths[i] = 0;
			for (int j = 0; j < width; j ++) {
				grid[j][i] = false;
			}
		}
	}
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		committed = false;
		int rowsCleared = 0;

		for (int i = 0, j = 0; i < height; i ++, j ++) {
			if (widths[i] == width) {
				rowsCleared ++;
				j--;
				continue;
			}
			if (i == j) {
				continue;
			}

			copyGridRow (i, j);
		}

		clearTopRows (height - rowsCleared);
		generateHeights ();
		sanityCheck();
		return rowsCleared;
	}

	private void copyGridRow (int from, int to) {
		for (int i = 0; i < width; i ++) {
			if (grid[i][to] != grid[i][from]) {
				giveValue(i, to, grid[i][from]);
			}
			if (grid[i][from]) {
				giveValue(i, from, false);
			}
		}
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (committed) {
			return ;
		}

		for (int i = pastMoves.size() - 1; i > -1; i --) {
			grid[pastMoves.get(i).x][pastMoves.get(i).y] = pastMoves.get(i).val;
		}
		pastMoves.clear();

		System.arraycopy(xHeights, 0, heights, 0, width);
		System.arraycopy(xWidths, 0, widths, 0, height);
		committed = true;
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		if (committed) {
			return;
		}
		pastMoves.clear();

		System.arraycopy(heights, 0, xHeights, 0, width);
		System.arraycopy(widths, 0, xWidths, 0, height);
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


