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
        cornerHeight = new int[4 * xMax * yMax];
    }

    private int cellIndex(int i, int j) {
        return i + j * xMax;
    }

    private void computeCornerHeight(int cellIndex) {
        int tileHeight = tileHeights[cellIndex];
        int tileType = tileTypes[cellIndex];

        cornerHeight[4*cellIndex + W] = tileHeight + (tileType & 1);
        cornerHeight[4*cellIndex + S] = tileHeight + ((tileType & 2) >> 1);
        cornerHeight[4*cellIndex + E] = tileHeight + ((tileType & 4) >> 2);
        cornerHeight[4*cellIndex + N] = tileHeight + ((tileType & 8) >> 3);
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

    public void setTileType(int i, int j, int tileType) {
        int cellIndex = cellIndex(i, j);
        tileTypes[cellIndex] = tileType;
        computeCornerHeight(cellIndex);
    }

    public int getCornerHeight(int i, int j, int corner) {
        int cellIndex = cellIndex(i, j);
        return cornerHeight[4*cellIndex + corner];
    }

    public void setTileHeights(int i, int j, int height) {
        int cellIndex = cellIndex(i, j);
        tileHeights[cellIndex] = height;
        computeCornerHeight(cellIndex);
    }
}
