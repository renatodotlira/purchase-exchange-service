package purchase.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static purchase.utils.FormatHelper.decinalFormat;

@Getter
@Setter
@Builder
public class PurchaseDto {

    private int id;

    private String description;

    @Getter(AccessLevel.NONE)
    private double amount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String amountConverted;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String exchangeRate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String currency;

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAmountConverted(double exchangeRate) {
        double amountConverted = exchangeRate * this.amount;
        this.amountConverted = decinalFormat(amountConverted).toString();
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = String.valueOf(exchangeRate);
    }

    public String getAmount() {
        return decinalFormat(this.amount).toString();
    }
}
