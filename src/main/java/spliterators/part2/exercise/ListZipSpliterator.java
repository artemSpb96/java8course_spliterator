package spliterators.part2.exercise;

import java.util.List;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class ListZipSpliterator<L, R, T>  implements Spliterator<T> {


    private List<L> list1;
    private List<R> list2;
    private BiFunction<L, R, T> combiner;

    private int startInclusive;
    private int endExclusive;

    public ListZipSpliterator(List<L> list1, List<R> list2, BiFunction<L, R, T> combiner) {
        this.list1 = list1;
        this.list2 = list2;
        this.combiner = combiner;
        this.startInclusive = 0;
        this.endExclusive = Math.min(list1.size(), list2.size());
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (startInclusive >= endExclusive) {
            return false;
        }

        action.accept(combiner.apply(list1.get(startInclusive), list2.get(startInclusive)));
        startInclusive++;

        return true;
    }

    @Override
    public Spliterator<T> trySplit() {
        if (endExclusive - startInclusive < 2) {
            return null;
        }

        int mid = (startInclusive + endExclusive) >> 1;

        ListZipSpliterator<L, R, T> newSpliterator = new ListZipSpliterator<>(list1.subList(startInclusive, mid),
                list2.subList(startInclusive, mid), combiner);

        startInclusive = mid;

        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        return endExclusive - startInclusive;
    }

    @Override
    public int characteristics() {
        return Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED |
                Spliterator.NONNULL;
    }
}
