package systems.terranatal.nanoerp.spa.processing;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

public record BasAccount(short code, String description, List<BasAccount> children, BasAccountClass accountClass) {
    static final int PARENT_ACCOUNT_COL = 2;
    static final int SUBACCOUNT_COL = 5;

    BasAccount(short code, String description) {
        this(code, description, new ArrayList<>(), BasAccountClass.determine(code));
    }

    public BasAccount addChild(BasAccount child) {
        children.add(child);
        return this;
    }

    public boolean isChildOf(BasAccount other) {
        return code >= other.code * 10 && code < (other.code + 1) * 10;
    }

    int level() {
        if (code <= 0) {
            return 0;
        }
        int lvl = 1;
        for (short res = (short) (code / 10); res > 0; res /= 10) {
            lvl++;
        }
        return lvl;
    }

    public static enum BasAccountClass {
        ASSET, EQUITY_AND_LIBILITIES, REVENUE, COST, RESULT, UNDETERMINED;

        public static BasAccountClass determine(Short accountCode) {
            var firstDigit = Short.valueOf(accountCode.toString().substring(0, 1));
            return switch (firstDigit) {
                case (1) -> ASSET;
                case (2) -> EQUITY_AND_LIBILITIES;
                case (3) -> REVENUE;
                case (4), (5), (6), (7) -> COST;
                case (8) -> RESULT;
                default -> UNDETERMINED;
            };
        }
    }
}
