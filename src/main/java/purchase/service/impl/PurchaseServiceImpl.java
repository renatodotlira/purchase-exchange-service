package purchase.service.impl;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import purchase.dao.CurrenciesClient;
import purchase.domain.Purchase;
import purchase.dto.CountryCurrencyDto;
import purchase.dto.DictionaryCountryCurrencyDto;
import purchase.dto.PurchaseCreateDto;
import purchase.dto.PurchaseDto;
import purchase.repository.PurchaseRepository;
import purchase.service.PurchaseService;
import purchase.translate.PurchaseTranslator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    PurchaseRepository repository;

    CurrenciesClient currenciesClient;

    public PurchaseServiceImpl(PurchaseRepository repository, CurrenciesClient currenciesClient) {
        this.repository = repository;
        this.currenciesClient = currenciesClient;
    }

    @Override
    public Optional<PurchaseDto> save(PurchaseCreateDto purchaseCreateDto) {
        Purchase purchase = PurchaseTranslator.toModel(purchaseCreateDto);
        return Optional.of(PurchaseTranslator.toDto(repository.save(purchase)));
    }

    @Override
    public PurchaseDto getPurchaseSpecifiedCountryCurrency(@NotNull Long id, @NotNull String country) {
        Purchase purchase = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase not found"));
        String filter = getFilter(country, purchase);
        DictionaryCountryCurrencyDto dictionaryCountryCurrencyDto = currenciesClient.getCurrency(filter,"currency,exchange_rate,record_date");
        if (dictionaryCountryCurrencyDto.getData().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Purchase cannot be converted to the target currency.");
        CountryCurrencyDto countryCurrencyDto = dictionaryCountryCurrencyDto.getData().get(0);
        return PurchaseTranslator.toDtoWithExchange(purchase, countryCurrencyDto);
    }

    private String getFilter(String country, Purchase purchase) {
        SimpleDateFormat dateFor = new SimpleDateFormat("yyyy-MM-dd");
        String dateTransaction = dateFor.format(purchase.getTransactionDate());
        Calendar cal = Calendar.getInstance();
        cal.setTime(purchase.getTransactionDate());
        cal.add(Calendar.MONTH, -6);
        String rangeDate = dateFor.format(cal.getTime());
        return "country:eq:"+country+",record_date:lte:"+dateTransaction+",record_date:gte:"+rangeDate;
    }
}
