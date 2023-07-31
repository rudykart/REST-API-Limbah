package com.rudykart.limbah.controllers;

import java.util.List;

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
import com.rudykart.limbah.dto.PurchaseDto;
import com.rudykart.limbah.entities.Purchase;
import com.rudykart.limbah.services.PurchaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping
    public ResponseEntity<DataResponse<List<Purchase>>> findAllPurchases() {
        return ResponseEntity.ok().body(purchaseService.findAllPurchases());
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping("/{pageSize}/{pageNo}")
    public ResponseEntity<PagingResponse<Purchase>> findAllPurchaseWithPaging(@PathVariable int pageSize,
            @PathVariable int pageNo) {
        return ResponseEntity.ok().body(purchaseService.findAllPurchaseWithPaging(pageNo, pageSize));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<Purchase>> findPurchaseById(@PathVariable Long id) {
        return ResponseEntity.ok().body(purchaseService.findPurchaseById(id));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @PostMapping
    public ResponseEntity<DataResponse<Purchase>> savePurchase(@RequestBody @Valid PurchaseDto purchaseDto) {
        DataResponse<Purchase> response = purchaseService.savePurchase(purchaseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @PatchMapping("/{id}/update")
    public ResponseEntity<DataResponse<Purchase>> updatePurchase(@PathVariable Long id,
            @RequestBody @Valid PurchaseDto purchaseDto) {
        DataResponse<Purchase> response = purchaseService.updatePurchase(id, purchaseDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize(" hasAuthority('BOSS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePurchase(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.deletePurchase(id));
    }
}
