package openloco.rail;

public enum BridgeTileType {

    FULL_WALL_N(i(63), i(1), none()),
    FULL_WALL_W(i(39), i(1), none()),
    FULL_WALL_E(i(40), i(36, 37, 38), i(18, 19)),
    FULL_WALL_S(i(64), i(60, 61, 62), i(26, 27)),

    FULL_WALL_NS(i(63, 64), i(60, 61, 62), i(26, 27)),
    FULL_WALL_EW(i(39, 40), i(36, 37, 38), i(18, 19)),

    HALF_NW(i(12), i(11), i(34, 35)),
    HALF_NE(i(14), i(2), none()),
    HALF_SE(i(13), i(3), none()),
    HALF_SW(i(15), i(4), none()),

    HALF_NW_NO_WALL(none(), i(11), i(34, 35)),
    HALF_NE_NO_WALL(none(), i(2), none()),
    HALF_SE_NO_WALL(none(), i(3), none()),
    HALF_SW_NO_WALL(none(), i(4), none()),

    SUPPORTS_ONLY_NS(none(), i(60, 61, 62), i(26, 27)),
    SUPPORTS_ONLY_EW(none(), i(36, 37, 38), i(18, 19)),

    NONE(none(), none(), none());

    private final int[] wallSprites;
    private final int[] baseSprites;
    private final int[] supportSprites;

    private BridgeTileType(int[] wallSprites, int[] baseSprites, int[] supportSprites) {
        this.wallSprites = wallSprites;
        this.baseSprites = baseSprites;
        this.supportSprites = supportSprites;
    }

    public int[] getWallSprites() {
        return wallSprites;
    }

    public int[] getBaseSprites() {
        return baseSprites;
    }

    public int[] getSupportSprites() {
        return supportSprites;
    }

    private static int[] i(int... indices) {
        return indices;
    }

    private static int[] none() {
        return new int[0];
    }

}
