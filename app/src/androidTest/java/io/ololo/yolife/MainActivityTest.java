package io.ololo.yolife;

import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.MotionEvent;
import android.view.View;


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
}
