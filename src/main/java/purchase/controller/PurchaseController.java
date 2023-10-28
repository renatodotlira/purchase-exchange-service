package purchase.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import purchase.dto.PurchaseCreateDto;
import purchase.dto.PurchaseDto;
import purchase.service.PurchaseService;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService service;

    @PostMapping
    @ApiOperation("Save a new purchase")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Purchase created successfully.", response = PurchaseDto.class),
            @ApiResponse(code = 400, message = "Invalid request."),
            @ApiResponse(code = 500, message = "Internal error.")
    })
    public ResponseEntity save(@Valid @RequestBody PurchaseCreateDto customerDto) {// throws HttpException {
        return service
                .save(customerDto)
                .map(ResponseEntity::ok)
                .orElseThrow(RuntimeException::new);
    }

    @GetMapping(path = "/exchange")
    @ApiOperation("Find purchase and convert to the target currency.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Return purchase from informed id.", response = PurchaseDto.class),
            @ApiResponse(code = 400, message = "Invalid request."),
            @ApiResponse(code = 404, message = "Purchase doesn't exist."),
            @ApiResponse(code = 500, message = "Internal error.")
    })
    public ResponseEntity findById(@RequestParam Long id, @RequestParam String country) {
        return ResponseEntity.ok(service.getPurchaseSpecifiedCountryCurrency(id, country));
    }

}
