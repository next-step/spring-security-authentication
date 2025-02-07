package nextstep.security.util;

public class Pair<FIRST, SECOND> {
    private final FIRST first;
    private final SECOND second;

    private Pair(final FIRST first, final SECOND second) {
        this.first = first;
        this.second = second;
    }

    public static <FIRST, SECOND> Pair<FIRST, SECOND> of(final FIRST first, SECOND second) {
        return new Pair<>(first, second);
    }

    public FIRST getFirst() {
        return first;
    }

    public SECOND getSecond() {
        return second;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Pair<?, ?> pair)) return false;

        if (getFirst() != null ? !getFirst().equals(pair.getFirst()) : pair.getFirst() != null) return false;
        return getSecond() != null ? getSecond().equals(pair.getSecond()) : pair.getSecond() == null;
    }

    @Override
    public int hashCode() {
        int result = getFirst() != null ? getFirst().hashCode() : 0;
        result = 31 * result + (getSecond() != null ? getSecond().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
