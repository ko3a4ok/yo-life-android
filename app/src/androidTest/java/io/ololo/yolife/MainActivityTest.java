package io.ololo.yolife;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.MotionEvent;
import android.view.View;


import io.ololo.yolife.model.Grid;
import io.ololo.yolife.model.LiveController;
import io.ololo.yolife.utils.Utils;


/**
 * Created by ko3a4ok on 10/2/15.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity activity;
    ActionMenuItemView item;
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        item = (ActionMenuItemView) activity.findViewById(R.id.action);
        assertNotNull(item);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @UiThreadTest
    public void testMenu(){
        if (!item.getText().toString().equals(getActivity().getString(R.string.stop)))
            item.callOnClick();
        assertEquals(item.getText().toString(), getActivity().getString(R.string.stop));
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    public void testClickOnGrid() throws InterruptedException {
        item.callOnClick();
        long downTime = SystemClock.uptimeMillis();
        assertFalse(activity.getModel().isAlive(0, 0));
        View v = activity.findViewById(R.id.life_grid);
        int []loc = new int[2];
        v.getLocationOnScreen(loc);
        int x = Utils.positive(v.getWidth() - v.getHeight())/2 + loc[0];
        int y = Utils.positive(v.getHeight() - v.getWidth())/2 + loc[1];
        final MotionEvent event = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x+1, y+1, 0);
        final Instrumentation inst = getInstrumentation();
        inst.sendPointerSync(event);
        assertTrue(activity.getModel().isAlive(0, 0));
        assertFalse(activity.getModel().isAlive(1, 0));
        assertFalse(activity.getModel().isAlive(1, 1));
        assertFalse(activity.getModel().isAlive(0, 1));
        SystemClock.sleep(LiveController.UPDATE_INTERVAL);
        assertFalse(activity.getModel().isAlive(0, 0));
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @LargeTest
    public void testStableFigure() throws InterruptedException {
        item.callOnClick();
        long downTime = SystemClock.uptimeMillis();
        assertFalse(activity.getModel().isAlive(0, 0));
        View v = activity.findViewById(R.id.life_grid);
        int []loc = new int[2];
        Grid grid = activity.getModel();
        v.getLocationOnScreen(loc);
        int x = Utils.positive(v.getWidth() - v.getHeight())/2 + loc[0];
        int y = Utils.positive(v.getHeight() - v.getWidth())/2 + loc[1];
        int side = Math.min(v.getHeight(), v.getWidth());
        int hedge = side/grid.getHeight();
        int wedge = side/grid.getHeight();
        final Instrumentation inst = getInstrumentation();

        for (int i = 0; i < grid.getHeight(); i++)
            for (int j = 0; j < grid.getWidth(); j++)
                assertFalse(grid.isAlive(i, j));

        inst.sendPointerSync(MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x + 1, y + 1, 0));
        inst.sendPointerSync(MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, wedge + x + 1, y + 1, 0));
        inst.sendPointerSync(MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, x + 1, hedge + y + 1, 0));
        inst.sendPointerSync(MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN, wedge + x + 1, hedge + y + 1, 0));
        for (int i = 0; i < grid.getHeight(); i++)
            for (int j = 0; j < grid.getWidth(); j++)
                assertEquals(i + " " + j, grid.isAlive(i, j), i < 2 && j < 2);

        SystemClock.sleep(3000);
        for (int i = 0; i < grid.getHeight(); i++)
            for (int j = 0; j < grid.getWidth(); j++)
                assertEquals(grid.isAlive(i, j), i < 2 && j < 2);
    }

    private static boolean between(int a, int l, int r) {
        return (l <= a && a <= r);
    }
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @LargeTest
    public void testLineCoverage() throws InterruptedException {
        item.callOnClick();
        long downTime = SystemClock.uptimeMillis();
        assertFalse(activity.getModel().isAlive(0, 0));
        View v = activity.findViewById(R.id.life_grid);
        int []loc = new int[2];
        Grid grid = activity.getModel();
        v.getLocationOnScreen(loc);
        int x = Utils.positive(v.getWidth() - v.getHeight())/2 + loc[0];
        int y = Utils.positive(v.getHeight() - v.getWidth())/2 + loc[1];
        int side = Math.min(v.getHeight(), v.getWidth());
        int hedge = side/grid.getHeight();
        int wedge = side/grid.getHeight();
        final Instrumentation inst = getInstrumentation();

        for (int i = 0; i < grid.getHeight(); i++)
            for (int j = 0; j < grid.getWidth(); j++)
                assertFalse(grid.isAlive(i, j));

        for (int j = 0; j < grid.getWidth(); j++) {
            assertFalse(grid.isAlive(1, j));
            inst.sendPointerSync(MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN,
                    wedge * j + x + wedge / 2, hedge * 1 + y + hedge / 2, 0));
            assertTrue(grid.isAlive(1, j));
        }

        SystemClock.sleep(LiveController.UPDATE_INTERVAL);
        for (int i = 0; i < grid.getHeight(); i++)
            for (int j = 0; j < grid.getWidth(); j++) {
                assertEquals(grid.isAlive(i, j), between(i, 0, 2) && between(j, 1, grid.getWidth() - 2));
            }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @LargeTest
    public void testFreeze() throws InterruptedException {
        testLineCoverage();
        item.callOnClick();
        Grid grid = activity.getModel();
        SystemClock.sleep(2*LiveController.UPDATE_INTERVAL);
        for (int i = 0; i < grid.getHeight(); i++)
            for (int j = 0; j < grid.getWidth(); j++) {
                assertEquals(grid.isAlive(i, j), between(i, 0, 2) && between(j, 1, grid.getWidth() - 2));
            }
    }



}
