package purchase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseCreateDto {
    @NotNull(message="Description cannot be null")
    @NotEmpty(message="Description cannot be blank")
    private String description;

    @NotNull(message="Amount cannot be null")
    @Positive(message="Amount must be a positive value")
    private Double amount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date dateTransaction;

}
