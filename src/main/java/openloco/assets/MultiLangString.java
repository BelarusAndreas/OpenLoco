package openloco.assets;

import com.google.common.base.Objects;

import java.util.HashMap;
import java.util.Map;

public class MultiLangString {

    private final Map<Integer, String> strings = new HashMap<>();

    public MultiLangString(Map<Integer, String> strings) {
        this.strings.putAll(strings);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(strings.get(0))
                .addValue("...")
                .toString();
    }
}
