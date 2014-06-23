package openloco.rail;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrackLayerTranslationTest {

    private static final int START_X = 10;
    private static final int START_Y = 10;

    private TrackLayer layer;

    @Test
    public void testStraightTranslation() {
        testStraightTranslation(Orientation.N, 0, -1);
        testStraightTranslation(Orientation.E, 1, 0);
        testStraightTranslation(Orientation.S, 0, 1);
        testStraightTranslation(Orientation.W, -1, 0);
    }

    private void testStraightTranslation(Orientation orientation, int deltaX, int deltaY) {
        layer = createTrackLayer(orientation);
        layer.addStraight();
        assertPosition(START_X+deltaX, START_Y+deltaY);
        assertOrientation(orientation);
    }

    @Test
    public void testDiagonalTranslation() {
        testDiagonalTranslation(Orientation.NE, 1, -1);
        testDiagonalTranslation(Orientation.SE, 1, 1);
        testDiagonalTranslation(Orientation.SW, -1, 1);
        testDiagonalTranslation(Orientation.NW, -1, -1);
    }

    private void testDiagonalTranslation(Orientation orientation, int deltaX, int deltaY) {
        layer = createTrackLayer(orientation);
        layer.addStraight();
        assertPosition(START_X+deltaX, START_Y+deltaY);
        assertOrientation(orientation);
    }

    @Test
    public void testSBendTranslation() {
        testSBendTranslation(Orientation.N, CurveDirection.LEFT, -1, -3);
        testSBendTranslation(Orientation.N, CurveDirection.RIGHT, 1, -3);
        testSBendTranslation(Orientation.E, CurveDirection.LEFT, 3, -1);
        testSBendTranslation(Orientation.E, CurveDirection.RIGHT, 3, 1);
        testSBendTranslation(Orientation.S, CurveDirection.LEFT, 1, 3);
        testSBendTranslation(Orientation.S, CurveDirection.RIGHT, -1, 3);
        testSBendTranslation(Orientation.W, CurveDirection.LEFT, -3, 1);
        testSBendTranslation(Orientation.W, CurveDirection.RIGHT,-3, -1);
    }

    private void testSBendTranslation(Orientation orientation, CurveDirection curveDirection, int deltaX, int deltaY) {
        layer = createTrackLayer(orientation);
        layer.addSBend(curveDirection);
        assertPosition(START_X+deltaX, START_Y+deltaY);
        assertOrientation(orientation);
    }

    @Test
    public void testSmallCurveTranslation() {
        testCurveTranslation(Orientation.N, CurveDirection.LEFT, -2, -1, Orientation.W, TrackLayer::addSmallCurve);
        testCurveTranslation(Orientation.N, CurveDirection.RIGHT, 2, -1, Orientation.E, TrackLayer::addSmallCurve);
        testCurveTranslation(Orientation.E, CurveDirection.LEFT, 1, -2, Orientation.N, TrackLayer::addSmallCurve);
        testCurveTranslation(Orientation.E, CurveDirection.RIGHT, 1, 2, Orientation.S, TrackLayer::addSmallCurve);
        testCurveTranslation(Orientation.S, CurveDirection.LEFT, 2, 1, Orientation.E, TrackLayer::addSmallCurve);
        testCurveTranslation(Orientation.S, CurveDirection.RIGHT, -2, 1, Orientation.W, TrackLayer::addSmallCurve);
        testCurveTranslation(Orientation.W, CurveDirection.LEFT, -1, 2, Orientation.S, TrackLayer::addSmallCurve);
        testCurveTranslation(Orientation.W, CurveDirection.RIGHT, -1, -2, Orientation.N, TrackLayer::addSmallCurve);
    }

    @Test
    public void testMediumCurveTranslation() {
        testCurveTranslation(Orientation.N, CurveDirection.LEFT, -3, -2, Orientation.W, TrackLayer::addMediumCurve);
        testCurveTranslation(Orientation.N, CurveDirection.RIGHT, 3, -2, Orientation.E, TrackLayer::addMediumCurve);
        testCurveTranslation(Orientation.E, CurveDirection.LEFT, 2, -3, Orientation.N, TrackLayer::addMediumCurve);
        testCurveTranslation(Orientation.E, CurveDirection.RIGHT, 2, 3, Orientation.S, TrackLayer::addMediumCurve);
        testCurveTranslation(Orientation.S, CurveDirection.LEFT, 3, 2, Orientation.E, TrackLayer::addMediumCurve);
        testCurveTranslation(Orientation.S, CurveDirection.RIGHT, -3, 2, Orientation.W, TrackLayer::addMediumCurve);
        testCurveTranslation(Orientation.W, CurveDirection.LEFT, -2, 3, Orientation.S, TrackLayer::addMediumCurve);
        testCurveTranslation(Orientation.W, CurveDirection.RIGHT, -2, -3, Orientation.N, TrackLayer::addMediumCurve);
    }

    @Test
    public void testWideCurveTranslation() {
        testCurveTranslation(Orientation.N, CurveDirection.LEFT, -1, -2, Orientation.NW, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.N, CurveDirection.RIGHT, 1, -2, Orientation.NE, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.NE, CurveDirection.LEFT, 1, -3, Orientation.N, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.NE, CurveDirection.RIGHT, 3, -1, Orientation.E, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.E, CurveDirection.LEFT, 2, -1, Orientation.NE, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.E, CurveDirection.RIGHT, 2, 1, Orientation.SE, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.SE, CurveDirection.LEFT, 3, 1, Orientation.E, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.SE, CurveDirection.RIGHT, 1, 3, Orientation.S, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.S, CurveDirection.LEFT, 1, 2, Orientation.SE, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.S, CurveDirection.RIGHT, -1, 2, Orientation.SW, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.SW, CurveDirection.LEFT, -1, 3, Orientation.S, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.SW, CurveDirection.RIGHT, -3, 1, Orientation.W, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.W, CurveDirection.LEFT, -2, 1, Orientation.SW, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.W, CurveDirection.RIGHT, -2, -1, Orientation.NW, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.NW, CurveDirection.LEFT, -3, -1, Orientation.W, TrackLayer::addWideCurve);
        testCurveTranslation(Orientation.NW, CurveDirection.RIGHT, -1, -3, Orientation.N, TrackLayer::addWideCurve);
    }

    private void testCurveTranslation(Orientation orientation, CurveDirection curveDirection, int deltaX, int deltaY, Orientation orientationAfter, CurveAdder curveAdder) {
        layer = createTrackLayer(orientation);
        curveAdder.addCurve(layer, curveDirection);
        assertPosition(START_X+deltaX, START_Y+deltaY);
        assertOrientation(orientationAfter);
    }


    private TrackLayer createTrackLayer(Orientation orientation) {
        return new TrackLayer(START_X, START_Y, 0, orientation);
    }

    private void assertPosition(int x, int y) {
        assertEquals("Incorrect x", x, layer.getCurrentX());
        assertEquals("Incorrect y", y, layer.getCurrentY());
    }

    private void assertOrientation(Orientation orientation) {
        assertEquals("Incorrect orientation", orientation, layer.getOrientation());
    }

    @FunctionalInterface
    interface CurveAdder {
        void addCurve(TrackLayer layer, CurveDirection direction);
    }

}
