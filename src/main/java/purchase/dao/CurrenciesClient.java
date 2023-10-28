package purchase.dao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import purchase.config.FeignCustomConfiguration;
import purchase.dto.DictionaryCountryCurrencyDto;

@FeignClient(name = "currencies", url = "${fiscal.data.treasury.url}", configuration = FeignCustomConfiguration.class)
public interface CurrenciesClient {

	@GetMapping(value = "?filter={filter}&sort=-record_date&page[number]=1&page[size]=1&fields={fields}", consumes = "application/json")
	DictionaryCountryCurrencyDto getCurrency(@PathVariable("filter") String filter, @PathVariable("fields") String fields);
	
}