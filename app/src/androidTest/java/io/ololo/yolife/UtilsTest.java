package io.ololo.yolife;

import android.graphics.Rect;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import java.util.Arrays;

import io.ololo.yolife.model.Grid;
import io.ololo.yolife.utils.Utils;

/**
 * Created by ko3a4ok on 01.10.15.
 */
public class UtilsTest extends TestCase {

    @SmallTest
    public void testPositive() {
        assertEquals(Utils.positive(7), 7);
        assertEquals(Utils.positive(0), 0);
        assertEquals(Utils.positive(-100), 0);
    }

    @SmallTest
    public void testBoolToInt() {
        assertEquals(Utils.boolToInt(true), 1);
        assertEquals(Utils.boolToInt(false), 0);
    }

    @SmallTest
    public void testNeighboursCount() {
        Grid g = new Grid();
        assertEquals(Utils.neighboursCount(g, -1, -1), 0);
        for (int i = 0; i < g.getWidth(); i++)
            for (int j = 0; j < g.getHeight(); j++)
                assertEquals(Utils.neighboursCount(g, i, j), 0);
        boolean[][] b = new boolean[3][3];
        for (int i = 0; i < b.length; i++)
            for (int j = 0; j < b[i].length; j++)
                b[i][j] = true;
        g.updateGrid(b);
        int[][] expect = {{3,5,3},{5,8,5}, {3,5,3}};
        for (int i = 0; i < g.getWidth(); i++)
            for (int j = 0; j < g.getHeight(); j++)
                assertEquals(Utils.neighboursCount(g, i, j), expect[i][j]);
    }

    @SmallTest
    public void testWillBeAlive() {
        Grid g = new Grid();
        for (int i = 0; i < g.getWidth(); i++)
            for (int j = 0; j < g.getHeight(); j++)
                assertEquals(Utils.willBeAlive(g, i, j), false);
        boolean[][] b = new boolean[3][3];
        for (int i = 0; i < b.length; i++)
            for (int j = 0; j < b[i].length; j++)
                b[i][j] = true;
        g.updateGrid(b);
        for (int i = 0; i < g.getWidth(); i++)
            for (int j = 0; j < g.getHeight(); j++)
                assertEquals(Utils.willBeAlive(g, i, j), i%2 == 0 && j%2 == 0);

        boolean[][] expect = {{false,true,true},{true,false,true}, {false,true,false}};
        b = new boolean[][]{{false, true, true}, {true, false, true}, {false, true, false}};
        g.updateGrid(b);
        for (int i = 0; i < g.getHeight(); i++) {
            for (int j = 0; j < g.getWidth(); j++)
                assertEquals(Utils.willBeAlive(g, i, j), expect[i][j]);
        }
    }

    @SmallTest
    public void testNextGrid() {
        Grid g = new Grid();
        assertTrue(Arrays.deepEquals(new boolean[20][20], Utils.nextGrid(g)));
        boolean[][] b = new boolean[3][3];
        for (int i = 0; i < b.length; i++)
            for (int j = 0; j < b[i].length; j++)
                b[i][j] = true;

        g.updateGrid(b);
        boolean[][] ex = {{true, false, true}, {false, false, false}, {true, false, true}};
        assertTrue(Arrays.deepEquals(ex, Utils.nextGrid(g)));
        boolean[][] expect = {{false,true,true},{true,false,true}, {false,true,false}};
        b = new boolean[][]{{false, true, true}, {true, false, true}, {false, true, false}};
        g.updateGrid(b);
        assertTrue(Arrays.deepEquals(Utils.nextGrid(g), expect));
    }

    @SmallTest
    public void testGetCell() {
        Rect rect = new Rect(0, 0, 100, 100);
        assertNull(Utils.getCell(rect, -1, 0, 10, 10));
        assertTrue(Arrays.equals(Utils.getCell(rect, 0, 0, 10, 10), new int[]{0, 0}));
        assertTrue(Arrays.equals(Utils.getCell(rect, 55, 45, 10, 10), new int[]{4, 5}));
    }

    public void testPerformance() {
        long start = System.currentTimeMillis();
        Grid grid = new Grid(1000, 1000);
        Utils.nextGrid(grid);
        long duration = System.currentTimeMillis() - start;
        assertTrue(duration < 500);
    }

}
