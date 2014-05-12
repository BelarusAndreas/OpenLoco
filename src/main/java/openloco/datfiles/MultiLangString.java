package openloco.datfiles;

import java.util.HashMap;
import java.util.Map;

public class MultiLangString {

    private final Map<Integer, String> strings = new HashMap<>();

    public MultiLangString(Map<Integer, String> strings) {
        this.strings.putAll(strings);
    }
}
