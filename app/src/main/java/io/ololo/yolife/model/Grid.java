package io.ololo.yolife.model;

import java.util.Observable;

/**
 * Created by ko3a4ok on 01.10.15.
 */
public class Grid extends Observable {

    private final static int DEFAULT_SIZE = 20;

    private boolean [][] data;

    public static Grid createCustom() {
        Grid grid = new Grid();
        for (int i = 0; i < 16; i += 8) {
            grid.data[i+5][5] = grid.data[i+5][6] = grid.data[i+5][7] = true;
            grid.data[i+7][4] = grid.data[i+7][8] = true;
            grid.data[i+8][4] = grid.data[i+8][8] = true;
            grid.data[i+10][5] = grid.data[i+10][6] = grid.data[i+10][7] = true;
        }
        for (int i = 5; i < 20; i+= 5)
            grid.data[i][15] = grid.data[i][16] = grid.data[i][17] = true;
        return grid;
    }
    public Grid() {
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    private Grid(int edge) {
        this(edge, edge);
    }

    public Grid(int h, int w) {
        if (h <= 0 || w <= 0) throw new IllegalArgumentException("Grid edges should be positive");
        data = new boolean[w][h];
        data[w/2][h/2] = true;
    }

    public int getWidth() {
        return data[0].length;
    }

    public int getHeight() {
        return data.length;
    }

    public boolean isAlive(int x, int y) {
        if (x < 0 || x >= data.length) return false;
        if (y < 0 || y >= data[x].length) return false;
        return data[x][y];
    }

    public void updateGrid(boolean[][] data){
        this.data = data;
        setChanged();
        notifyObservers();
    }

    public void touched(int x, int y) {
        if (x < 0 || x >= data.length) return;
        if (y < 0 || y >= data[x].length) return;
        data[x][y] = !data[x][y];
        updateGrid(data);
    }
}
