package openloco.demo;

import openloco.Assets;
import openloco.Ground;
import openloco.datfiles.DatFileLoader;
import openloco.datfiles.Sprites;
import openloco.graphics.OpenGlSprite;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TerrainDemo extends BaseDemo {

    private final Assets assets;
    private final List<OpenGlSprite> tiles = new ArrayList<>();

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

    public TerrainDemo(Assets assets) {
        this.assets = assets;
    }

    @Override
    protected void init() {
        Ground ground = assets.getGround("GRASS1  ");
        for (int i=385; i<400; i++) {
            Sprites.RawSprite sprite = ground.getSprites().get(i);
            tiles.add(OpenGlSprite.createFromRawSprite(sprite));
        }

        computeEdgeHeights();
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

    @Override
    protected void render() {
        drawTiles();
        drawGrid();
    }

    private void drawTiles() {
        GL11.glPushMatrix();
        GL11.glTranslatef(getScreenWidth()/2.0f, getScreenHeight()/2.0f, 0f);

        GL11.glTranslatef(-isoX(cellWidth * width / 2, cellWidth * height / 2, 0), -isoY(cellWidth * width / 2, cellWidth * height / 2, 0), 0f);

        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        for (int i=0; i<width; i++) {
            for (int j = 0; j < height; j++) {
                OpenGlSprite sprite = tiles.get(tileTypes[cellIndex(i, j)]);
                int cartX = i * cellWidth;
                int cartY = j * cellWidth;
                int cartZ = heightStep*tileHeights[cellIndex(i, j)];
                sprite.draw(isoX(cartX, cartY, cartZ), isoY(cartX, cartY, cartZ));
            }
        }

        GL11.glPopMatrix();
    }

    private int cellIndex(int i, int j) {
        return i + j*width;
    }

    private void drawGrid() {
        GL11.glPushMatrix();
        GL11.glTranslatef(getScreenWidth()/2.0f, getScreenHeight()/2.0f, 0f);

        GL11.glTranslatef(-isoX(cellWidth * width / 2, cellWidth * height / 2, 0), -isoY(cellWidth * width / 2, cellWidth * height / 2, 0), 0f);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glLineWidth(1f);
        GL11.glColor4f(0.9f, 0.9f, 0.9f, 0.1f);

        for (int i=0; i<width; i++) {
            for (int j=0; j<height; j++) {
                int x1 = i*cellWidth; int x2 = (i+1)*cellWidth;
                int y1 = j*cellWidth; int y2 = (j+1)*cellWidth;

                int cellOffset = 4*cellIndex(i, j);

                int a = edgeHeights[cellOffset]*heightStep;
                int b = edgeHeights[cellOffset+1]*heightStep;
                int c = edgeHeights[cellOffset+2]*heightStep;
                int d = edgeHeights[cellOffset+3]*heightStep;

                GL11.glBegin(GL11.GL_LINE_LOOP);
                GL11.glVertex2f(isoX(x1, y1, a), isoY(x1, y1, a));
                GL11.glVertex2f(isoX(x2, y1, b), isoY(x2, y1, b));
                GL11.glVertex2f(isoX(x2, y2, c), isoY(x2, y2, c));
                GL11.glVertex2f(isoX(x1, y2, d), isoY(x1, y2, d));
                GL11.glEnd();
            }
        }

        GL11.glPopMatrix();
    }

    public static void main(String[] args) throws IOException {
        final String DATA_DIR = args[0];
        Assets assets = new Assets();
        new DatFileLoader(assets, DATA_DIR).loadFiles();
        new TerrainDemo(assets).run();
    }

}
