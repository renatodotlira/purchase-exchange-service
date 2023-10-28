package purchase.service;

import purchase.dto.PurchaseCreateDto;
import purchase.dto.PurchaseDto;
import java.util.Optional;

public interface PurchaseService {

    Optional<PurchaseDto> save(PurchaseCreateDto purchaseCreateDto);// throws HttpException;

    PurchaseDto getPurchaseSpecifiedCountryCurrency(Long id, String country);

}
