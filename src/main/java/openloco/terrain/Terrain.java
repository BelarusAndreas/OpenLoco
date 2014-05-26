package openloco.terrain;

public class Terrain {

    private int[] tileHeights;

    private int[] tileTypes;

    private int[] cornerHeight;

    private int width;
    private int height;
    private int cellWidth = 32;
    private int heightStep = 16;

    public static final int W = 0;
    public static final int S = 1;
    public static final int E = 2;
    public static final int N = 3;

    public Terrain(int width, int height) {
        this.width = width;
        this.height = height;
        tileHeights = new int[width*height];
        tileTypes = new int[width*height];
        tileHeights[(height/2)*width+width/2] = 2;
        computeCornerHeights();
    }

    private int cellIndex(int i, int j) {
        return i + j*width;
    }

    private void computeCornerHeights() {
        cornerHeight = new int[4 * width * height];

        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getHeightStep() {
        return heightStep;
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
