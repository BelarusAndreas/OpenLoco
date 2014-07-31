package openloco.demo;

import java.util.ArrayList;
import java.util.List;

import openloco.Assets;
import openloco.entities.Ground;
import openloco.entities.Vehicle;
import openloco.entities.Sprites;
import openloco.entities.VehicleSpriteVar;
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

    public LoadSpriteDemo(Assets assets) {
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

        int tileCount = 9;

        GL11.glTranslatef(-IsoUtil.isoX(Tile.WIDTH * tileCount / 2, Tile.WIDTH * tileCount / 2, 0), -IsoUtil.isoY(Tile.WIDTH * tileCount / 2, Tile.WIDTH * tileCount / 2, 0), 0f);

        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glPushMatrix();
        for (int i=0; i<tileCount; i++) {
            for (int j=0; j<tileCount; j++) {
                grassSprite.draw(IsoUtil.calculateScreenCoord(new CartCoord(i*Tile.WIDTH, j*Tile.WIDTH, 0)));
            }
        }
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glLineWidth(1f);
        GL11.glColor4f(0.7f, 0.7f, 0.7f, 0.1f);
        GL11.glBegin(GL11.GL_LINES);

        for (int i=0; i<=tileCount; i++) {
            int a = 0;
            int b = i * Tile.WIDTH;
            int c = tileCount * Tile.WIDTH;

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