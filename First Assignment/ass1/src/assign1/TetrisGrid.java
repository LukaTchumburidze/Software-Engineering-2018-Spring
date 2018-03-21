//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.
package assign1;

import sun.awt.image.ImageWatched;

import java.util.LinkedList;

public class TetrisGrid {

	private boolean[][] grid;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}
	
	private LinkedList<Integer> getEmptyCells () {
		LinkedList<Integer> emptyCells = new LinkedList<Integer>();
		for (int i = 0; i < grid[0].length; i ++) {
			int j;
			for (j = 0; j < grid.length; j ++) {
				if (!grid[j][i]) {
					break;
				}
			}
			if (j == grid.length) {
				for (j = 0; j < grid.length; j ++) {
					grid[j][i] = false;
				}
				emptyCells.add(i);
			}
		}

		return emptyCells;
	}

	private void swapRows (int I, int J) {
		boolean tmp;
		for (int i = 0; i < grid.length; i ++) {
			tmp = grid[i][I];
			grid[i][I] = grid[i][J];
			grid[i][J] = tmp;
		}
	}

	private void deleteIthRow(int I) {
		for (int i = I; i < grid[0].length-1; i ++) {
			swapRows (i, i+1);
		}
	}

	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		LinkedList<Integer> emptyRows = getEmptyCells();
		for (int i = 0; i < emptyRows.size(); i ++) {
			deleteIthRow(emptyRows.get(i)-i);
		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid;
	}
}
