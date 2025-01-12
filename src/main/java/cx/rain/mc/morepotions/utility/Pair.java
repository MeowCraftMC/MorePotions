package cx.rain.mc.morepotions.utility;

public record Pair<L, R>(L left, R right) {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair<?, ?>(Object left2, Object right2)) {
            return left.equals(left2) && right.equals(right2);
        }
        return false;
    }
}
