package io.ololo.yolife.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Observable;
import java.util.Observer;

import io.ololo.yolife.model.Grid;
import io.ololo.yolife.utils.Utils;

/**
 * View what renders grid
 */
public class LifeGridView extends SurfaceView implements Observer {

    private static final Paint BG_PAINT = new Paint();
    private static final Paint RECT_PAINT = new Paint();

    static {
        BG_PAINT.setColor(Color.WHITE);
        RECT_PAINT.setColor(Color.BLUE);
    }

    private Rect gridRect;
    private Grid data;

    private OnCellTouchListener touchListener;

    public LifeGridView(Context context) {
        super(context);
        init();
    }

    public LifeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LifeGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setOnCellTouchListener(OnCellTouchListener listener) {
        this.touchListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (touchListener != null && data != null && event.getActionMasked() != MotionEvent.ACTION_UP) {
            int[] cell = Utils.getCell(gridRect, event.getX()-getLeft(), event.getY()-getTop(), data.getWidth(), data.getHeight());
            if (cell != null) {
                touchListener.onTouchCell(cell[0], cell[1]);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    private void init() {
        getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                int edge = Math.min(width, height);
                int x = Utils.positive(width - height) / 2;
                int y = Utils.positive(height - width) / 2;
                gridRect = new Rect(x, y, x + edge, y + edge);
                update(data, null);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
    }

    private void renderGrid(Canvas canvas) {
        canvas.drawRect(gridRect, BG_PAINT);
        Grid grid = data;
        if (grid == null) return;
        float w = 1f * gridRect.width() / grid.getWidth();
        float h = 1f * gridRect.height() / grid.getHeight();
        float dx = gridRect.left;
        float dy = gridRect.top;
        for (int i = 0; i < grid.getHeight(); i++)
            for (int j = 0; j < grid.getWidth(); j++)
                if (grid.isAlive(i, j))
                    canvas.drawRect(dx + j*w, dy + i*h, dx + (j+1)*w, dy + (i+1)*h, RECT_PAINT);
    }

    @Override
    public void update(Observable observable, Object o) {
        data = (Grid) observable;
        if (gridRect == null) return;
        Canvas canvas = getHolder().lockCanvas(null);
        if (canvas == null) return;
        renderGrid(canvas);
        getHolder().unlockCanvasAndPost(canvas);
    }

    public interface OnCellTouchListener {
        void onTouchCell(int x, int y);
    }
}
