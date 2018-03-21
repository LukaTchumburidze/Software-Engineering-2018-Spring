// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

package assign1;

public class CharGrid {
	private char[][] grid;
	private final int MAXNUM = 10000000;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int xMin = MAXNUM, xMax = -MAXNUM, yMin = MAXNUM, yMax = -MAXNUM;
		for (int i = 0; i < grid.length; i ++) {
			for (int j = 0; j < grid[i].length; j ++) {
				if (grid[i][j] == ch) {
					xMin = Math.min(xMin, j);
					xMax = Math.max(xMax, j);

					yMin = Math.min(yMin, i);
					yMax = Math.max(yMax, i);
				}
			}
		}

		if (xMin == MAXNUM) {
			return 0;
		}
		return (xMax-xMin+1)*(yMax-yMin+1);
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int ans = 0;

		for (int i = 0; i < grid.length; i ++) {
			for (int j = 0; j < grid[i].length; j ++) {
				if (isCross(i, j)) {
					ans ++;
				}
			}
		}

		return ans;
	}

	private boolean isCross (int i, int j) {
		int dist1 = getCross (0, -1, i, j);
		int dist2 = getCross (0, 1, i, j);
		int dist3 = getCross (-1, 0, i, j);
		int dist4 = getCross (1, 0, i, j);

		return (dist1 > 1 && dist1 == dist2 && dist2 == dist3 && dist3 == dist4);
	}

	private int getCross (int a, int b, int I, int J) {
		int i = 0;
		for (i = 1; I+a*i>-1 && I+a*i<grid.length && J+b*i > -1 && J+b*i<grid[I+a*i].length; i ++) {
			if (grid[I+a*(i-1)][J+b*(i-1)] != grid[I+a*i][J+b*i]) {
				break;
			}
		}

		return i;
	}
}
