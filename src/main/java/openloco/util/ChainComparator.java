package openloco.util;

import java.util.Comparator;

public final class ChainComparator<T> implements Comparator<T> {

    private final Comparator<T>[] comparators;

    @SafeVarargs
    public ChainComparator(Comparator<T>... comparators) {
        this.comparators = comparators;
    }

    @Override
    public int compare(T o1, T o2) {
        int result = 0;
        for (Comparator<T> c: comparators) {
            result = c.compare(o1, o2);
            if (result != 0) {
                break;
            }
        }
        return result;
    }
}
