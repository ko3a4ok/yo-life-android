package io.ololo.yolife;

import junit.framework.TestCase;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.ololo.yolife.model.Grid;

/**
 * Created by ko3a4ok on 10/1/15.
 */
public class GridTest extends TestCase {
    private Grid grid;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        grid = new Grid();
    }

    public void testTouched() {
        Random r = new Random();
        int y = r.nextInt(grid.getHeight());
        int x = r.nextInt(grid.getWidth());
        grid.touched(y, x);
        for (int i = 0; i < grid.getWidth(); i++)
            for (int j = 0; j < grid.getHeight(); j++)
                assertEquals(grid.isAlive(j, i), i == x && j == y);
    }

    public void testAddObserver() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        grid.addObserver(new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                latch.countDown();
            }
        });
        grid.touched(0, 0);
        assertTrue(latch.await(1, TimeUnit.SECONDS));

    }
}
