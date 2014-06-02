package openloco.rail;

import openloco.Assets;
import openloco.entities.Track;
import openloco.graphics.IsoUtil;
import openloco.graphics.OpenGlSprite;
import openloco.graphics.Renderer;
import openloco.graphics.SpriteInstance;

import java.util.ArrayList;
import java.util.List;

public class TrackRenderer implements Renderer<TrackNetwork>{

    private final Assets assets;

    public TrackRenderer(Assets assets) {
        this.assets = assets;
    }

    @Override
    public List<SpriteInstance> render(TrackNetwork network) {
        Track track = assets.getTrack("TRACKST ");

        OpenGlSprite ballastSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(18));
        OpenGlSprite sleeperSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(20));
        OpenGlSprite railSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(22));

        List<SpriteInstance> sprites = new ArrayList<>();

        for (TrackNode node: network.getAllNodes()) {
            int screenX = Math.round(IsoUtil.isoX(node.getX(), node.getY(), node.getZ()));
            int screenY = Math.round(IsoUtil.isoY(node.getX(), node.getY(), node.getZ()));

            sprites.add(new SpriteInstance(ballastSprite, screenX, screenY));
            sprites.add(new SpriteInstance(sleeperSprite, screenX, screenY));
            sprites.add(new SpriteInstance(railSprite, screenX, screenY));
        }

        return sprites;
    }
}
