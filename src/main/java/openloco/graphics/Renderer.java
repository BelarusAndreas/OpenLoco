package openloco.graphics;

import java.util.List;

public interface Renderer<T> {

    List<SpriteInstance> render(T renderable);

}
