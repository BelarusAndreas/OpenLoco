package openloco.datfiles;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MultiLangString {

    private final Map<Integer, String> strings = new HashMap<>();

    public MultiLangString(DataInputStream in) {
        try {
            byte language;
            while ((language = in.readByte()) != (byte)0xFF){
                StringBuffer sb = new StringBuffer();
                char ch;
                while ((ch = (char)in.readByte()) != (byte)0x00) {
                    sb.append(ch);
                }
                strings.put((int)language, sb.toString());
            }
        }
        catch (IOException ioe) {
            throw new RuntimeException("Could not parse multilang string", ioe);
        }
    }
}
