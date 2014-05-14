package openloco.terrain;

public class Terrain {

    private int[] tileHeights = {
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 0, 0, 0,
            0, 0, 0, 1, 2, 1, 0, 0, 0,
            0, 0, 0, 1, 1, 1, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
    };

    private int[] tileTypes = {
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 2, 3, 3, 3, 1, 0, 0,
            0, 0, 6, 2, 3, 1, 9, 0, 0,
            0, 0, 6, 6, 0, 9, 9, 0, 0,
            0, 0, 6, 4,12, 8, 9, 0, 0,
            0, 0, 4,12,12,12, 8, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0,
    };

    private int[] edgeHeights;

    private int width = 9;
    private int height = 9;
    private int cellWidth = 32;
    private int heightStep = 16;

    public Terrain() {
    }

    private int cellIndex(int i, int j) {
        return i + j*width;
    }

    private void computeEdgeHeights() {
        edgeHeights = new int[4 * width * height];

        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                int cell = cellIndex(i, j);
                int tileHeight = tileHeights[cell];
                int tileType = tileTypes[cell];

                edgeHeights[4*cell] = tileHeight + ((tileType & 8) >> 3);
                edgeHeights[4*cell+1] = tileHeight + ((tileType & 4) >> 2);
                edgeHeights[4*cell+2] = tileHeight + ((tileType & 2) >> 1);
                edgeHeights[4*cell+3] = tileHeight + (tileType & 1);
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
}
