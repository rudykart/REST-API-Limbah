package com.rudykart.limbah.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rudykart.limbah.dto.CashDto;
import com.rudykart.limbah.dto.DataResponse;
import com.rudykart.limbah.dto.PagingResponse;
import com.rudykart.limbah.entities.Cash;
import com.rudykart.limbah.services.CashService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cashes")
@PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
public class CashController {

    private final CashService cashService;

    public CashController(CashService cashService) {
        this.cashService = cashService;
    }

    // @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping("/{pageSize}/{pageNo}")
    public ResponseEntity<PagingResponse<Cash>> findAllCashesWithPaging(@PathVariable int pageNo,
            @PathVariable int pageSize) {
        return ResponseEntity.ok().body(cashService.findAllCashesWithPaging(pageNo, pageSize));
    }

    @GetMapping("/{description}/{pageSize}/{pageNo}")
    public ResponseEntity<PagingResponse<Cash>> findAllCashesWithPagingAndSearch(@PathVariable int pageNo,
            @PathVariable int pageSize, @PathVariable String description) {
        return ResponseEntity.ok().body(cashService.findAllCashesWithPagingAndSearch(pageNo, pageSize, description));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<Cash>> findCashById(@PathVariable Long id) {
        return ResponseEntity.ok().body(cashService.findCashById(id));
    }

    @PostMapping("/income")
    public ResponseEntity<DataResponse<Cash>> saveCashIncome(@RequestBody @Valid CashDto cashDto) {
        DataResponse<Cash> savedCash = cashService.saveCashIncome(cashDto);
        return new ResponseEntity<>(savedCash, HttpStatus.CREATED);
    }

    @PostMapping("/outcome")
    public ResponseEntity<DataResponse<Cash>> saveCashOutcome(@RequestBody @Valid CashDto cashDto) {
        DataResponse<Cash> savedCash = cashService.saveCashOutcome(cashDto);
        return new ResponseEntity<>(savedCash, HttpStatus.CREATED);
    }
}
