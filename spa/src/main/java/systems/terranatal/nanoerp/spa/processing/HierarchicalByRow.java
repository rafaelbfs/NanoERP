package systems.terranatal.nanoerp.spa.processing;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public interface HierarchicalByRow<N> {
    record Context<N>(Sheet sheet, AtomicInteger rowNum, int level, N parentEntry, Context<N> parentContext) {
        public Context(Sheet sheet, int rowNum) {
            this(sheet, new AtomicInteger(rowNum), 0, null, null);
        }

        public Context<N> createChild(N currentEntry, int rowNum) {
            return new Context<>(sheet, new AtomicInteger(rowNum + 1), level + 1, currentEntry, this);
        }

        public boolean isRoot() {
            return Objects.isNull(parentContext);
        }

        public boolean nextRowExists(int rowNum) {
            return rowNum < sheet.getLastRowNum();
        }

        public boolean endOfSheet(int rowNum) {
            return rowNum >= sheet.getLastRowNum();
        }
    }

    public interface Leveled {
        int level();
    }

    static final Comparator<Context<?>> LEVEL_COMPARATOR = Comparator.comparingInt(c -> c.level);



    CompletableFuture<N> processRow(Context<N> ctx, Row row);

    N process(Sheet sheet);

    N parseRow(Row row);
}
