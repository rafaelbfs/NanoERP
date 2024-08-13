package systems.terranatal.nanoerp.spa.processing;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

public class TestBasParsing {

    @Test
    public void testBasParsing() {
        var start = Instant.now();
        var ist = TestBasParsing.class.getClassLoader().getResourceAsStream("Chart-of-accounts-2022.xlsx");
        try(var wb = new XSSFWorkbook(ist)) {
            var sheet = wb.getSheetAt(0);
            var processor = new BasAccountXlsx2022Processor(sheet);
            var result = processor.process();
            System.out.printf("Total logic branch evaluations: %d%n", processor.totalEvaluations.get());
            var elapsedTime = Duration.between(start, Instant.now());
            System.out.printf("Total time %d.%09d seconds.%n", elapsedTime.getSeconds(), elapsedTime.getNano());
            Assertions.assertFalse(result.isEmpty());
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }
}
