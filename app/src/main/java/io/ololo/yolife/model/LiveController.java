package io.ololo.yolife.model;

import android.os.Handler;
import android.os.HandlerThread;

import io.ololo.yolife.utils.Utils;

/**
 * Created by ko3a4ok on 01.10.15.
 */
public class LiveController {

    private static final int UPDATE_INTERVAL = 1000;

    private Grid grid;
    private Handler handler;
    private boolean isRunning = false;

    private Runnable gridUpdater = new Runnable() {
        @Override
        public void run() {
            if (!isRunning) return;
            grid.updateGrid(Utils.nextGrid(grid));
            handler.postDelayed(gridUpdater, UPDATE_INTERVAL);
        }
    };

    public LiveController(Grid grid) {
        this.grid = grid;
        HandlerThread thread = new HandlerThread(getClass().getName());
        thread.start();
        handler = new Handler(thread.getLooper());
    }


    public boolean isRunning() {
        return isRunning;
    }
    public void start() {
        if (!isRunning) {
            isRunning = true;
            handler.post(gridUpdater);
        }
    }

    public void stop() {
        handler.removeCallbacksAndMessages(null);
        isRunning = false;
    }
}
