package openloco.terrain;

public class Terrain {

    private int[] tileHeights;

    private int[] tileTypes;

    private int[] edgeHeights;

    private int width;
    private int height;
    private int cellWidth = 32;
    private int heightStep = 16;

    public Terrain(int width, int height) {
        this.width = width;
        this.height = height;
        tileHeights = new int[width*height];
        tileTypes = new int[width*height];
        computeEdgeHeights();
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
