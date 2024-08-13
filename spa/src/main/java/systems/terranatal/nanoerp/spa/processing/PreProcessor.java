package systems.terranatal.nanoerp.spa.processing;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record PreProcessor(Sheet sheet, Predicate<Row> isParseable, int startRow, int endRow) {

    public PreProcessor(Sheet sheet, Predicate<Row> isParseable) {
        this(sheet, isParseable, 0, sheet.getLastRowNum());
    }

    public PreProcessor seekUntilParseable() {
        for (int rowNr = startRow; rowNr <= endRow; rowNr++) {
            if (isParseable.test(sheet.getRow(rowNr))) {
                return new PreProcessor(sheet, isParseable, rowNr, endRow);
            }
        }
        return this;
    }

    public <T> Stream<RowHelper<T>> preProcess(Function<Row, RowHelper<T>> supplier) {
        return IntStream.range(startRow, endRow).filter(rowNr -> isParseable.test(sheet.getRow(rowNr)))
                .parallel()
                .mapToObj(row -> supplier.apply(sheet.getRow(row)));
    }

    public interface RowHelper<T> {

        public T parse();

        public int rowNr();

        default Optional<T> safeParse() {
            return Optional.ofNullable(parse());
        }
    }

}
