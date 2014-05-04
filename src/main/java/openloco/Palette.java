package openloco;

public class Palette {

    // see palette and explanation here: http://www.tt-wiki.net/wiki/Sprite_Palette
    private static final int[] COLOUR = {
            0xFF0000FF, // 00 - background transparency?
            0xFFFF00FF, // Transparent
            0xFFFF00FF,
            0xFFFF00FF,
            0xFFFF00FF,
            0xFFFF00FF,
            0xFFFF00FF,
            0xFFFF00FF,
            0xFFFF00FF,
            0xFFFF00FF,  // End Transparent/Reserved
            0xFF172323,
            0xFF233333,
            0xFF2F4343,
            0xFF3F5353,
            0xFF4B6363,
            0xFF5B7373,
            0xFF6F8383,
            0xFF839797,
            0xFF9FAFAF,
            0xFFB7C3C3,
            0xFFD3DBDB,
            0xFFEFF3F3,
            0xFF332F00,
            0xFF3F3B00,
            0xFF4F4B0B,
            0xFF5B5B13,
            0xFF6B6B1F,
            0xFF777B2F,
            0xFF878B3B,
            0xFF979B4F,
            0xFFA7AF5F,
            0xFFBBBF73,
            0xFFCBCF8B,
            0xFFDFE3A3,
            0xFF432B07,
            0xFF573B0B,
            0xFF6F4B17,
            0xFF7F571F,
            0xFF8F6327,
            0xFF9F7333,
            0xFFB38343,
            0xFFBF9757,
            0xFFCBAF6F,
            0xFFDBC787,
            0xFFE7DBA3,
            0xFFF7EFC3,
            0xFF471B00,
            0xFF5F2B00, // glass on stations
            0xFF773F00,
            0xFF8F5307,
            0xFFA76F07,
            0xFFBF8B0F,
            0xFFD7A713,
            0xFFF3CB1B,
            0xFFFFE72F,
            0xFFFFF35F,
            0xFFFFFB8F,
            0xFFFFFFC3,
            0xFF230000,
            0xFF4F0000,
            0xFF5F0707,
            0xFF6F0F0F,
            0xFF7F1B1B,
            0xFF8F2727,
            0xFFA33B3B,
            0xFFB34F4F,
            0xFFC76767,
            0xFFD77F7F,
            0xFFEB9F9F,
            0xFFFFBFBF,
            0xFF1B3313,
            0xFF233F17,
            0xFF2F4F1F,
            0xFF3B5F27,
            0xFF476F2B,
            0xFF577F33,
            0xFF638F3B,
            0xFF739B43,
            0xFF83AB4B,
            0xFF93BB53,
            0xFFA3CB5F,
            0xFFB7DB67,
            0xFF1F371B,
            0xFF2F4723,
            0xFF3B532B,
            0xFF4B6337,
            0xFF5B6F43,
            0xFF6F874F,
            0xFF879F5F,
            0xFF9FB76F,
            0xFFB7CF7F,
            0xFFC3DB93,
            0xFFCFE7A7,
            0xFFDFF7BF,
            0xFF0F3F00,
            0xFF135300,
            0xFF176700,
            0xFF1F7B00,
            0xFF278F07,
            0xFF379F17,
            0xFF47AF27,
            0xFF5BBF3F,
            0xFF6FCF57,
            0xFF8BDF73,
            0xFFA3EF8F,
            0xFFC3FFB3,
            0xFF4F2B13,
            0xFF63371B,
            0xFF77472B,
            0xFF8B573B,
            0xFFA76343,
            0xFFBB7353,
            0xFFCF8363,
            0xFFD79773,
            0xFFE3AB83,
            0xFFEFBF97,
            0xFFF7CFAB,
            0xFFFFE3C3,
            0xFF0F1337,
            0xFF272B57,
            0xFF333767,
            0xFF3F4377,
            0xFF53538B,
            0xFF63639B,
            0xFF7777AF,
            0xFF8B8BBF,
            0xFF9F9FCF,
            0xFFB7B7DF,
            0xFFD3D3EF,
            0xFFEFEFFF,
            0xFF001B6F,
            0xFF002797,
            0xFF0733A7,
            0xFF0F43BB,
            0xFF1B53CB,
            0xFF2B67DF,
            0xFF4387E3,
            0xFF5BA3E7,
            0xFF77BBEF,
            0xFF8FD3F3,
            0xFFAFE7FB,
            0xFFD7F7FF,
            0xFF0B2B0F,
            0xFF0F3717,
            0xFF17471F,
            0xFF23532B,
            0xFF2F633B,
            0xFF3B734B,
            0xFF4F875F,
            0xFF639B77,
            0xFF7BAF8B,
            0xFF93C7A7,
            0xFFAFDBC3,
            0xFFCFF3DF,
            0xFF3F005F, // 154-165 company primary start
            0xFF4B0773,
            0xFF530F7F,
            0xFF5F1F8F,
            0xFF6B2B9B,
            0xFF7B3FAB,
            0xFF8753BB,
            0xFF9B67C7,
            0xFFAB7FD7,
            0xFFBF9BE7,
            0xFFD7C3F3,
            0xFFF3EBFF, // 165 - company primary end
            0xFF3F0000,
            0xFF570000,
            0xFF730000,
            0xFF8F0000,
            0xFFAB0000,
            0xFFC70000,
            0xFFE30700,
            0xFFFF0700,
            0xFFFF4F43,
            0xFFFF7B73,
            0xFFFFABA3,
            0xFFFFDBD7,
            0xFF4F2700,
            0xFF6F3300,
            0xFF933F00,
            0xFFB74700,
            0xFFDB4F00,
            0xFFFF5300,
            0xFFFF6F17,
            0xFFFF8B33,
            0xFFFFA34F,
            0xFFFFB76B,
            0xFFFFCB87,
            0xFFFFDBA3,
            0xFF00332F,
            0xFF003F37,
            0xFF1A3F43,
            0xFF2A5156,
            0xFF3A6369,
            0xFF4A767C,
            0xFF5A888F,
            0xFF6B9BA3,
            0xFF84B4BA,
            0xFF9DCDD1,
            0xFFB6E6E8,
            0xFFCFFFFF,
            0xFF3F001B, // 202 - 213 - company secondary
            0xFF670033,
            0xFF7B0B3F,
            0xFF8F174F,
            0xFFA31F5F,
            0xFFB7276F,
            0xFFDB3B8F,
            0xFFEF5BAB,
            0xFFF377BB,
            0xFFF797CB,
            0xFFFBB7DF,
            0xFFFFD7EF, // 213 - last company secondary
            0xFF271300,
            0xFF371F07,
            0xFF472F0F,
            0xFF5B3F1F,
            0xFF6B5333,
            0xFF7B674B,
            0xFF8F7F6B,
            0xFFA3937F,
            0xFFBBAB93,
            0xFFCFC3AB,
            0xFFE7DBC3,
            0xFFFFF3DF,
            0xFF374B4B,
            0xFFFFB700,
            0xFFFFDB00,
            0xFFFFFF00,
            0xFF402F00,
            0xFF5B4200,
            0xFF765600,
            0xFF916B00,
            0xFFAD7E00,
            0xFFC89200,
            0xFFE3A700,
            0xFFFFBB00,
            0xFFFFC933,
            0xFFFFD666,
            0xFF435B5B,
            0xFF536B6B,
            0xFF637B7B,
            0xFFFFE499,
            0xFF002797,
            0xFF0733A7,
            0xFF0F43BB,  //
            0xFF1B53CB,
            0xFF2B67DF,
            0xFF4387E3,
            0xFF5BA3E7,
            0xFF77BBEF,
            0xFF8FD3F3,
            0xFFAFE7FB,
            0xFFD7F7FF,
            0xFF0B2B0F};

    public static final int BACKGROUND = COLOUR[0];
    public static final int PRIMARY_START = 154;
    public static final int PRIMARY_END = 165;
    public static final int SECONDARY_START = 202;
    public static final int SECONDARY_END = 213;

    public static boolean isCompanyPrimary(int pixel) {
        return isInRange(pixel, PRIMARY_START, PRIMARY_END);
    }

    public static boolean isCompanySecondary(int pixel) {
        return isInRange(pixel, SECONDARY_START, SECONDARY_END);
    }

    public static boolean isGlass(int pixel) {
        return COLOUR[47] == pixel;
    }

    private static boolean isInRange(int pixel, int start, int end) {
        pixel = 0xFF000000 | pixel;
        return colourIndexInRange(pixel, start, end) != -1;
    }

    private static int colourIndexInRange(int pixel, int start, int end) {
        for (int i=start; i<=end; i++) {
            if (COLOUR[i] == pixel) {
                return i-start;
            }
        }
        return -1;
    }

    public static int primaryShade(int pixel, int primary) {
        return shade(pixel, primary, PRIMARY_START, PRIMARY_END);
    }

    public static int secondaryShade(int pixel, int secondary) {
        return shade(pixel, secondary, SECONDARY_START, SECONDARY_END);
    }

    private static int shade(int pixel, int primary, int start, int end) {
        int index = colourIndexInRange(pixel, start, end);
        if (index == -1) {
            return pixel;
        }
        else {
            int range = end-start;
            double fraction = (double)(index+1)/(double)(range+2);

            int r = 0xFF & (primary >> 16);
            int g = 0xFF & (primary >> 8);
            int b = 0xFF & (primary);

            if (fraction < 0.5) {
                //darken
                r *= (2*fraction);
                g *= (2*fraction);
                b *= (2*fraction);
            }
            else {
                r = Math.round((float)(2*255*(fraction-0.5) + 2*r*(1-fraction)));
                g = Math.round((float)(2*255*(fraction-0.5) + 2*g*(1-fraction)));
                b = Math.round((float)(2*255*(fraction-0.5) + 2*b*(1-fraction)));
            }

            return 0xFF000000 | (r << 16) | (g << 8) | b;
        }
    }
}
