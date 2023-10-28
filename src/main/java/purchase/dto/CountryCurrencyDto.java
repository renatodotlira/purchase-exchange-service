package purchase.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryCurrencyDto {

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("exchange_rate")
    private double exchangeRate;

    @JsonProperty("record_date")
    private String recordDate;

}
