package spliterators.part1.exercise;


import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class RectangleSpliterator extends Spliterators.AbstractIntSpliterator {
    private final int[][] array;
    private final int innerLength;
    private int startOuterInclusive;
    private final int endOuterExclusive;
    private int startInnerInclusive;

    public RectangleSpliterator(int[][] array) {
        this(array, 0, array.length, 0);
    }

    private RectangleSpliterator(int[][] array, int startOuterInclusive, int endOuterExclusive, int startInnerInclusive) {
        super(Long.MAX_VALUE, Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED | Spliterator.NONNULL);

        this.array = array;
        this.innerLength = array.length == 0 ? 0 : array[0].length;
        this.startOuterInclusive = startOuterInclusive;
        this.endOuterExclusive = endOuterExclusive;
        this.startInnerInclusive = startInnerInclusive;
    }

    @Override
    public RectangleSpliterator trySplit() {
        RectangleSpliterator newSpliterator = null;

        if (endOuterExclusive - startOuterInclusive > 1) {
            int midOuter = (endOuterExclusive + startOuterInclusive) >> 1;
            newSpliterator = new RectangleSpliterator(array, startOuterInclusive, midOuter,
                    0);
            startOuterInclusive = midOuter;
        }

        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        return ((long) endOuterExclusive - startOuterInclusive) * innerLength - startInnerInclusive;
    }

    @Override
    public boolean tryAdvance(IntConsumer action) {
        if (startInnerInclusive >= innerLength){
            startInnerInclusive = 0;
            startOuterInclusive++;
        }
        if (startOuterInclusive >= endOuterExclusive) {
            return false;
        }

        action.accept(array[startOuterInclusive][startInnerInclusive++]);


        return true;
    }
}
