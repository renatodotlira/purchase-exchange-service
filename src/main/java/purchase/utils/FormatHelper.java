package purchase.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormatHelper {
    public static BigDecimal decinalFormat(double value) {
        BigDecimal valueB = BigDecimal.valueOf(value);
        return valueB.setScale(2, RoundingMode.HALF_UP);
    }
}
