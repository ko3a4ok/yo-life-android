package io.ololo.yolife;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import io.ololo.yolife.model.Grid;
import io.ololo.yolife.model.LiveController;
import io.ololo.yolife.view.LifeGridView;


public class MainActivity extends ActionBarActivity {

    private LifeGridView lifeGridView;
    private Grid model;
    private LiveController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lifeGridView = (LifeGridView) findViewById(R.id.life_grid);
        model = new Grid();
        model.addObserver(lifeGridView);
        controller = new LiveController(model);
        lifeGridView.setOnCellTouchListener(new LifeGridView.OnCellTouchListener() {
            @Override
            public void onTouchCell(int x, int y) {
                model.touched(x, y);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action) {
            if (controller.isRunning()) {
                controller.stop();
            } else {
                controller.start();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    item.setTitle(controller.isRunning() ? R.string.stop : R.string.start);
                }
            });

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.stop();
    }

    public Grid getModel() {
        return model;
    }
}
