package purchase.translate;

import purchase.domain.Purchase;
import purchase.dto.CountryCurrencyDto;
import purchase.dto.PurchaseCreateDto;
import purchase.dto.PurchaseDto;
import static purchase.utils.FormatHelper.decinalFormat;

public class PurchaseTranslator {

    public static PurchaseDto toDto(final Purchase from){

        return PurchaseDto.builder()
                .id(from.getId())
                .description(from.getDescription())
                .amount(from.getAmount())
                .build();
    }

    public static PurchaseDto toDtoWithExchange(final Purchase from, CountryCurrencyDto currencyExchange){

        PurchaseDto purchaseDto = PurchaseDto.builder()
                .id(from.getId())
                .description(from.getDescription())
                .amount(from.getAmount())
                .build();

        purchaseDto.setAmountConverted(currencyExchange.getExchangeRate());
        purchaseDto.setExchangeRate(currencyExchange.getExchangeRate());
        purchaseDto.setCurrency(currencyExchange.getCurrency());
        return purchaseDto;
    }

    public static Purchase toModel(PurchaseCreateDto from){
        validateToModel(from);
        Purchase purchase = new Purchase();
        purchase.setAmount(decinalFormat(from.getAmount()).doubleValue());
        purchase.setDescription(from.getDescription());
        purchase.setTransactionDate(from.getDateTransaction());
        return purchase;
    }

    private static void validateToModel(PurchaseCreateDto from) {
        if (from.getAmount() == null)
            throw new IllegalArgumentException("Amount is required.");
        if (from.getDescription() == null)
            throw new IllegalArgumentException("Description is required.");
        if (from.getDateTransaction() == null)
            throw new IllegalArgumentException("Date transaction is required.");
        if (from.getDescription().length() > 50)
            throw new IllegalArgumentException("Description must not exceed 50 characters.");
        if (from.getAmount() <= 0.0)
            throw new IllegalArgumentException("Amount must be a positive value.");
    }
}
