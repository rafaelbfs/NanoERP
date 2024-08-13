package systems.terranatal.nanoerp.spa.processing;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BasAccountXlsx2022Processor {
    static final int PARENT_ACCOUNT_COL = 2;
    static final int SUBACCOUNT_COL = 5;

    public final AtomicLong totalEvaluations = new AtomicLong(0);
    private final PreProcessor preProcessor;

    public BasAccountXlsx2022Processor(Sheet sheet) {
        this.preProcessor = new PreProcessor(sheet, BasAccountXlsx2022Processor::isParseable)
                .seekUntilParseable();
    }

    public static short getCodeFromCell(Cell cell) {
        if (Objects.isNull(cell)) {
            return 0;
        }
        return cell.getCellType() == CellType.NUMERIC ?
                Double.valueOf(cell.getNumericCellValue()).shortValue() : 0;
    }

    public static boolean isParseable(Row row) {
        if (row == null) {
            return false;
        }
        var mainCodeCell = row.getCell(PARENT_ACCOUNT_COL);
        var subCodeCell = row.getCell(SUBACCOUNT_COL);
        var mainCode = getCodeFromCell(mainCodeCell);
        var subCode = getCodeFromCell(subCodeCell);

        return mainCode > 0 || subCode > 0;
    }

    public static BasAccount parseRow(Row row) {
        var mainCodeCell = row.getCell(PARENT_ACCOUNT_COL);
        var subCodeCell = row.getCell(SUBACCOUNT_COL);
        var mainCode = getCodeFromCell(mainCodeCell);
        var subCode = getCodeFromCell(subCodeCell);

        if (mainCode > 0 && subCode > 0) {
            return new BasAccount(mainCode, row.getCell(PARENT_ACCOUNT_COL + 1)
                    .getStringCellValue())
                    .addChild(new BasAccount(subCode, row.getCell(SUBACCOUNT_COL + 1)
                            .getStringCellValue()));
        }

        if (mainCode > 0) {
            return new BasAccount(mainCode, row.getCell(PARENT_ACCOUNT_COL + 1)
                    .getStringCellValue());
        }
        if (subCode > 0) {
            return new BasAccount(subCode, row.getCell(SUBACCOUNT_COL + 1)
                    .getStringCellValue());
        }

        return null;
    }

    public record BasRowHelper(Row row) implements PreProcessor.RowHelper<BasAccount> {
        public BasAccount parse() {
            return parseRow(row);
        }

        public int rowNr() {
            return row.getRowNum();
        }
    }

    public List<BasAccount> process() {
        var stream = preProcessor.preProcess(BasRowHelper::new);
        var acctsByCode = stream.map(PreProcessor.RowHelper::safeParse).filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(BasAccount::code, Function.identity(), this::resolve));
        return CompletableFuture.supplyAsync(() -> processLevel(acctsByCode,
                acctsByCode.values().stream().filter(basAccount -> basAccount.level() == 4)
                        .toList(), 4)).join();
    }

    private List<BasAccount> processLevel(Map<Short, BasAccount> allAccounts,
            List<BasAccount> current, int rank) {
        if (rank <= 1) {
            return current;
        }

        var rankAbove = allAccounts.entrySet().parallelStream().filter(e -> e.getValue().level() == rank - 1)
                .map(Map.Entry::getValue)
                .peek(a -> a.children().addAll(current.stream().filter(c -> {
                    totalEvaluations.incrementAndGet();
                    return c.isChildOf(a);
                }).toList()));

        return processLevel(allAccounts, rankAbove.toList(), rank - 1);
    }

    private BasAccount resolve(BasAccount ba1, BasAccount ba2) {
        if (!ba1.description().equalsIgnoreCase(ba2.description())) {
            return new BasAccount(ba1.code(), ba1.description() + "," + ba2.description());
        }
        return ba1;
    }

}
