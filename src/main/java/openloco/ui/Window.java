package openloco.ui;

public class Window implements UiComponent {

    private Panel panel;

    public Window(int x, int y, int width, int height) {
        this.panel = new Panel(x, y, width, height, false);
    }

    @Override
    public void render() {
        panel.render();
    }
}
