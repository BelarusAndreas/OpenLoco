package openloco;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import openloco.datfiles.DatFileLoader;
import openloco.datfiles.Sprites;
import openloco.datfiles.VehicleSpriteVar;
import openloco.demo.BaseDemo;
import openloco.graphics.OpenGlSprite;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class LoadSprite extends BaseDemo {

    private List<OpenGlSprite> sprites = new ArrayList<>();

    private OpenGlSprite grassSprite;

    private Assets assets;
    private int spriteIndex = 0;
    private Vehicle vehicle;
    private boolean rotating = true;

    public LoadSprite(Assets assets) {
        this.assets = assets;
    }

    @Override
    protected void init() {
        vehicle = assets.getVehicle("HST     ");
        
        Ground ground = assets.getGround("GRASS1  ");
        Sprites.RawSprite sprite = ground.getSprites().get(385);
        grassSprite = OpenGlSprite.createFromRawSprite(sprite);

        for (Sprites.RawSprite rawSprite: vehicle.getSprites().getList()) {
            OpenGlSprite openGlSprite = OpenGlSprite.createFromRawSprite(rawSprite);
            this.sprites.add(openGlSprite);
        }
    }

    @Override
    protected void render() {
        drawGrid();
        drawSprites();
    }

    private void drawGrid() {
        GL11.glPushMatrix();
        GL11.glTranslatef(getScreenWidth()/2.0f, getScreenHeight()/2.0f, 0f);

        int gridWidth = 32;
        int cellCount = 9;

        GL11.glTranslatef(-isoX(gridWidth * cellCount / 2, gridWidth * cellCount / 2, 0), -isoY(gridWidth * cellCount / 2, gridWidth * cellCount / 2, 0), 0f);

        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glPushMatrix();
        for (int i=0; i<cellCount; i++) {
            for (int j=0; j<cellCount; j++) {
                grassSprite.draw(isoX(i * gridWidth, j * gridWidth, 0), isoY(i * gridWidth, j * gridWidth, 0));
            }
        }
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glLineWidth(1f);
        GL11.glColor4f(0.7f, 0.7f, 0.7f, 0.1f);
        GL11.glBegin(GL11.GL_LINES);

        for (int i=0; i<=cellCount; i++) {
            int a = 0;
            int b = i * gridWidth;
            int c = cellCount * gridWidth;

            GL11.glVertex2f(isoX(a, b, 0), isoY(a, b, 0));
            GL11.glVertex2f(isoX(c, b, 0), isoY(c, b, 0));
            GL11.glVertex2f(isoX(b, a, 0), isoY(b, a, 0));
            GL11.glVertex2f(isoX(b, c, 0), isoY(b, c, 0));
        }

        GL11.glEnd();
        GL11.glPopMatrix();
    }

    private void drawSprites() {
        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        OpenGlSprite sprite = sprites.get(spriteIndex);
        sprite.draw(getScreenWidth() / 2.0f, getScreenHeight() / 2.0f);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    protected void update() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_SPACE && Keyboard.getEventKeyState()) {
                rotating = !rotating;
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT && Keyboard.getEventKeyState()) {
                spriteIndex++;
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_LEFT && Keyboard.getEventKeyState()) {
                spriteIndex--;
            }
        }

        VehicleSpriteVar spriteVar = vehicle.getVars().getVehSprites().get(0);
        int frameOffset = 0;
        if (rotating) {
            frameOffset = spriteVar.getFrames() > 1 ? spriteVar.getFrames() + spriteVar.getTiltCount() : spriteVar.getTiltCount();
        }
        int spriteCount = spriteVar.getLevelSpriteCount() * spriteVar.getFrames() * spriteVar.getTiltCount();
        if (spriteIndex < 0) {
            spriteIndex += spriteCount;
        }
        spriteIndex = (spriteIndex + frameOffset) % spriteCount;
    }

    public static void main(String[] args) throws IOException {
        final String DATA_DIR = args[0];
        Assets assets = new Assets();
        new DatFileLoader(assets, DATA_DIR).loadFiles();
        new LoadSprite(assets).run();
    }
}