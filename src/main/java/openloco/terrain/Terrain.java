package openloco.terrain;

public class Terrain {

    private int[] tileHeights;

    private int[] tileTypes;

    private int[] cornerHeight;

    private int xMax;
    private int yMax;
    public static final int CELL_WIDTH = 32;
    public static final int HEIGHT_STEP = 16;

    public static final int W = 0;
    public static final int S = 1;
    public static final int E = 2;
    public static final int N = 3;

    public Terrain(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
        tileHeights = new int[xMax*yMax];
        tileTypes = new int[xMax*yMax];
        tileTypes[(yMax/2)*xMax+xMax/2] = 5;
        tileTypes[(yMax/2)*xMax+3*xMax/2] = 10;
        tileTypes[(yMax/2)*xMax+xMax/2+1] = 10;
        tileHeights[(yMax/2)*xMax+xMax/2] = 2;
        computeCornerHeights();
    }

    private int cellIndex(int i, int j) {
        return i + j* xMax;
    }

    private void computeCornerHeights() {
        cornerHeight = new int[4 * xMax * yMax];

        for (int i=0; i< xMax; i++) {
            for (int j=0; j< yMax; j++) {
                int cell = cellIndex(i, j);
                int tileHeight = tileHeights[cell];
                int tileType = tileTypes[cell];

                cornerHeight[4*cell + W] = tileHeight + (tileType & 1);
                cornerHeight[4*cell + S] = tileHeight + ((tileType & 2) >> 1);
                cornerHeight[4*cell + E] = tileHeight + ((tileType & 4) >> 2);
                cornerHeight[4*cell + N] = tileHeight + ((tileType & 8) >> 3);
            }
        }
    }

    public int getXMax() {
        return xMax;
    }

    public int getYMax() {
        return yMax;
    }

    public int getTileType(int i, int j) {
        return tileTypes[cellIndex(i, j)];
    }

    public int getTileHeight(int i, int j) {
        return tileHeights[cellIndex(i, j)];
    }

    public int getCornerHeight(int i, int j, int corner) {
        int cellIndex = cellIndex(i, j);
        return cornerHeight[4*cellIndex + corner];
    }
}
