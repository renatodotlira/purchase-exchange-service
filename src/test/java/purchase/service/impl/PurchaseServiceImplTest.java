package purchase.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;
import purchase.dao.CurrenciesClient;
import purchase.domain.Purchase;
import purchase.dto.CountryCurrencyDto;
import purchase.dto.DictionaryCountryCurrencyDto;
import purchase.dto.PurchaseCreateDto;
import purchase.dto.PurchaseDto;
import purchase.repository.PurchaseRepository;
import purchase.translate.PurchaseTranslator;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {
    @MockBean
    private PurchaseRepository purchaseRepository;
    @MockBean
    private PurchaseServiceImpl purchaseService;
    @MockBean
    private CurrenciesClient currenciesClient;

    @BeforeEach
    void setUp() {
        currenciesClient = mock(CurrenciesClient.class);
        purchaseRepository = mock(PurchaseRepository.class);
        purchaseService = new PurchaseServiceImpl(purchaseRepository, currenciesClient);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("should Save Purchase With Success")
    void shouldSavePurchaseWithSuccess() {
        PurchaseCreateDto purchaseCreateDto = PurchaseCreateDto.builder()
                .amount(10.0)
                .dateTransaction(new Date())
                .description("")
                .build();
        Purchase purchaseResponse = PurchaseTranslator.toModel(purchaseCreateDto);
        purchaseResponse.setId(1);
        when(purchaseRepository.save(any())).thenReturn(purchaseResponse);

        purchaseService.save(purchaseCreateDto);
        verify(purchaseRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("should Save Purchase With Amount Required Exception")
    void shouldSavePurchaseWithAmountRequiredException() {
        PurchaseCreateDto purchaseCreateDto = PurchaseCreateDto.builder()
                .description("")
                .dateTransaction(new Date())
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> purchaseService.save(purchaseCreateDto));

        String expectedMessage = "Amount is required.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("should Save Purchase With Description Required Exception")
    void shouldSavePurchaseWithDescriptionRequiredException() {
        PurchaseCreateDto purchaseCreateDto = PurchaseCreateDto.builder()
                .amount(10.0)
                .dateTransaction(new Date())
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> purchaseService.save(purchaseCreateDto));

        String expectedMessage = "Description is required.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("should Save Purchase With DateTransaction Required Exception")
    void shouldSavePurchaseWithDateTransactionRequiredException() {
        PurchaseCreateDto purchaseCreateDto = PurchaseCreateDto.builder()
                .amount(10.0)
                .description("")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> purchaseService.save(purchaseCreateDto));

        String expectedMessage = "Date transaction is required.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("should Save Purchase With Description Exceeded Characters Exception")
    void shouldSavePurchaseWithDescriptionExceededCharactersException() {
        String description = "x".repeat(51);
        PurchaseCreateDto purchaseCreateDto = PurchaseCreateDto.builder()
                .amount(10.0)
                .description(description)
                .dateTransaction(new Date())
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> purchaseService.save(purchaseCreateDto));

        String expectedMessage = "Description must not exceed 50 characters.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("should Save Purchase With Non Positive Amount Exception")
    void shouldSavePurchaseWithNonPositiveAmountException() {
        PurchaseCreateDto purchaseCreateDto = PurchaseCreateDto.builder()
                .amount(0.0)
                .description("")
                .dateTransaction(new Date())
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> purchaseService.save(purchaseCreateDto));

        String expectedMessage = "Amount must be a positive value.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("should Get Purchase Specified Country Currency With Success")
    void shouldGetPurchaseSpecifiedCountryCurrencyWithSuccess() {
        PurchaseCreateDto purchaseCreateDto = PurchaseCreateDto.builder()
                .amount(10.0)
                .dateTransaction(new Date())
                .description("")
                .build();
        CountryCurrencyDto countryCurrencyDto = CountryCurrencyDto.builder()
                .currency("")
                .exchangeRate(2.5)
                .recordDate(new Date().toString())
                .build();
        DictionaryCountryCurrencyDto dictionaryCountryCurrencyDto = DictionaryCountryCurrencyDto.builder()
                .data(List.of(countryCurrencyDto))
                .build();
        when(currenciesClient.getCurrency(getFilter(), "currency,exchange_rate,record_date")).thenReturn(dictionaryCountryCurrencyDto);
        Purchase purchaseResponse = PurchaseTranslator.toModel(purchaseCreateDto);
        purchaseResponse.setId(1);
        when(purchaseRepository.findById(any())).thenReturn(Optional.of(purchaseResponse));

        PurchaseDto purchaseDto = purchaseService.getPurchaseSpecifiedCountryCurrency(1L, "");
        assertEquals(purchaseDto.getAmountConverted(), "25.00");
        verify(currenciesClient, times(1)).getCurrency(getFilter(), "currency,exchange_rate,record_date");
        verify(purchaseRepository, times(1)).findById(any());
    }

    private String getFilter() {
        SimpleDateFormat dateFor = new SimpleDateFormat("yyyy-MM-dd");
        String dateTransaction = dateFor.format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -6);
        String rangeDate = dateFor.format(cal.getTime());
        return "country:eq:,record_date:lte:"+dateTransaction+",record_date:gte:"+rangeDate;
    }

    @Test
    @DisplayName("should Get Purchase Specified Country Purchase Not Found Exception")
    void shouldGetPurchaseSpecifiedCountryCurrencyWithPurchaseNotFoundException() {
        PurchaseCreateDto purchaseCreateDto = PurchaseCreateDto.builder()
                .amount(10.0)
                .dateTransaction(new Date())
                .description("")
                .build();
        Purchase purchaseResponse = PurchaseTranslator.toModel(purchaseCreateDto);
        purchaseResponse.setId(1);
        when(purchaseRepository.findById(any())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> purchaseService.getPurchaseSpecifiedCountryCurrency(1L, ""));

        String expectedMessage = "404 NOT_FOUND \"Purchase not found\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

    }

    @Test
    @DisplayName("should Get Purchase Specified Country Country Currency Not Found Exception")
    void shouldGetPurchaseSpecifiedCountryCurrencyWithCountryCurrencyNotFoundException() {
        PurchaseCreateDto purchaseCreateDto = PurchaseCreateDto.builder()
                .amount(10.0)
                .dateTransaction(new Date())
                .description("")
                .build();
        Purchase purchaseResponse = PurchaseTranslator.toModel(purchaseCreateDto);
        purchaseResponse.setId(1);
        DictionaryCountryCurrencyDto dictionaryCountryCurrencyDto = DictionaryCountryCurrencyDto.builder()
                .data(new ArrayList<>())
                .build();
        when(purchaseRepository.findById(any())).thenReturn(Optional.of(purchaseResponse));
        when(currenciesClient.getCurrency(getFilter(), "currency,exchange_rate,record_date")).thenReturn(dictionaryCountryCurrencyDto);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> purchaseService.getPurchaseSpecifiedCountryCurrency(1L, ""));

        String expectedMessage = "400 BAD_REQUEST \"Purchase cannot be converted to the target currency.\"";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);

    }
}