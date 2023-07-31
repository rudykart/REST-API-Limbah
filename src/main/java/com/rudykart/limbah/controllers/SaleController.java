package com.rudykart.limbah.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rudykart.limbah.dto.DataResponse;
import com.rudykart.limbah.dto.PagingResponse;
import com.rudykart.limbah.dto.SaleDto;
import com.rudykart.limbah.entities.Sale;
import com.rudykart.limbah.services.SaleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping("/{pageSize}/{pageNo}")
    public ResponseEntity<PagingResponse<Sale>> findAllSales(@PathVariable int pageSize,
            @PathVariable int pageNo) {
        return ResponseEntity.ok().body(saleService.findAllSalesWithPaging(pageNo, pageSize));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<Sale>> findSaleById(@PathVariable Long id) {
        return ResponseEntity.ok().body(saleService.findAllSaleById(id));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @PostMapping
    public ResponseEntity<DataResponse<Sale>> saveSale(@RequestBody @Valid SaleDto saleDto) {
        return ResponseEntity.ok().body(saleService.saveSale(saleDto));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @PatchMapping("/{id}/update")
    public ResponseEntity<DataResponse<Sale>> updateSale(@PathVariable Long id, @RequestBody @Valid SaleDto saleDto) {
        return ResponseEntity.ok().body(saleService.updateSale(id, saleDto));
    }

    @PreAuthorize(" hasAuthority('BOSS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<Map<String, String>>> deleteSale(@PathVariable Long id) {
        DataResponse<Map<String, String>> response = saleService.deleteSale(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
