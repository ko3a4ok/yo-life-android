package io.ololo.yolife.utils;

import android.graphics.Rect;

import io.ololo.yolife.model.Grid;

/**
 * Created by ko3a4ok on 01.10.15.
 */
public class Utils {
    /**
     * @param a
     * @return return 0 if @a negative and a if >= 0
     */
    public static int positive(int a){
        return a < 0 ? 0 : a;
    }

    /**
     * Convert boolean value to int
     * @param b
     * @return 1 if b is true, and 0, if b is false
     */
    public static int boolToInt(boolean b) {
        return b ? 1: 0;

    }

    /**
     * @param grid
     * @param x
     * @param y
     * @return amount of alive neighbours for [x,y] cell
     */
    public static int neighboursCount(Grid grid, int x, int y) {
        int count = 0;
        int[] dx = {-1, 0, 1, 1, 1, 0, -1, -1};
        int[] dy = {-1, -1, -1, 0, 1, 1, 1, 0};
        for (int i = 0; i < dx.length; i++)
            count += boolToInt(grid.isAlive(x + dx[i], y + dy[i]));
        return count;
    }


    /**
     * @param grid
     * @param x
     * @param y
     * @return true, if cell[x,y] will be live in next epoch.
     */
    public static boolean willBeAlive(Grid grid, int x, int y) {
        int count = neighboursCount(grid, x, y);
        if (count < 2) return false;
        if (count > 3) return false;
        if (count == 3) return true;
        return grid.isAlive(x, y);
    }

    /**
     * @param grid
     * @return next grid iteration of an epoch.
     */
    public static boolean[][] nextGrid(Grid grid) {
        boolean[][] next = new boolean[grid.getHeight()][grid.getWidth()];
        for (int i = 0; i < next.length; i++)
            for (int j = 0; j < next[i].length; j++)
                next[i][j] = willBeAlive(grid, i, j);
        return next;
    }

    /**
     * @param gridRect
     * @param x
     * @param y
     * @param width - width of grid
     * @param height - height of grid
     * @return cell position which is touched
     */
    public static int[] getCell(Rect gridRect, float x, float y, int width, int height) {
        if (!gridRect.contains((int)x, (int) y)) return null;
        int i = (int) (width*(x - gridRect.left)/gridRect.width());
        int j = (int) (height*(y - gridRect.top)/gridRect.height());
        return new int[]{j, i};
    }
}
