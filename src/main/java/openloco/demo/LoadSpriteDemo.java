package openloco.demo;

import java.util.ArrayList;
import java.util.List;

import openloco.assets.Assets;
import openloco.assets.Ground;
import openloco.assets.Vehicle;
import openloco.assets.Sprites;
import openloco.assets.VehicleSpriteVar;
import openloco.graphics.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class LoadSpriteDemo extends BaseDemo {

    private List<OpenGlSprite> sprites = new ArrayList<>();

    private OpenGlSprite grassSprite;
    private Assets assets;
    private int spriteIndex = 0;
    private Vehicle vehicle;
    private boolean rotating = true;

    private static final int TILE_COUNT = 9;

    public LoadSpriteDemo(Assets assets) {
        super(TILE_COUNT, TILE_COUNT);
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

        GL11.glTranslatef(-IsoUtil.isoX(Tile.WIDTH * TILE_COUNT / 2, Tile.WIDTH * TILE_COUNT / 2, 0), -IsoUtil.isoY(Tile.WIDTH * TILE_COUNT / 2, Tile.WIDTH * TILE_COUNT / 2, 0), 0f);

        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glPushMatrix();
        for (int i=0; i< TILE_COUNT; i++) {
            for (int j=0; j< TILE_COUNT; j++) {
                grassSprite.draw(IsoUtil.calculateScreenCoord(new CartCoord(i*Tile.WIDTH, j*Tile.WIDTH, 0)));
            }
        }
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glLineWidth(1f);
        GL11.glColor4f(0.7f, 0.7f, 0.7f, 0.1f);
        GL11.glBegin(GL11.GL_LINES);

        for (int i=0; i<= TILE_COUNT; i++) {
            int a = 0;
            int b = i * Tile.WIDTH;
            int c = TILE_COUNT * Tile.WIDTH;

            GL11.glVertex2f(IsoUtil.isoX(a, b, 0), IsoUtil.isoY(a, b, 0));
            GL11.glVertex2f(IsoUtil.isoX(c, b, 0), IsoUtil.isoY(c, b, 0));
            GL11.glVertex2f(IsoUtil.isoX(b, a, 0), IsoUtil.isoY(b, a, 0));
            GL11.glVertex2f(IsoUtil.isoX(b, c, 0), IsoUtil.isoY(b, c, 0));
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
        sprite.draw(new ScreenCoord((int)(getScreenWidth() / 2.0f), (int)(getScreenHeight() / 2.0f)));

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
}