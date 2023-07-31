package com.rudykart.limbah.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.rudykart.limbah.dto.DataResponse;
import com.rudykart.limbah.dto.CashDto;
import com.rudykart.limbah.dto.PagingResponse;
import com.rudykart.limbah.entities.Cash;
import com.rudykart.limbah.entities.CashActivity;
import com.rudykart.limbah.exceptions.CashNotEnoughtException;
import com.rudykart.limbah.exceptions.DataNotFoundException;
import com.rudykart.limbah.repositories.CashRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CashService {
    private final CashRepository cashRepository;

    public CashService(CashRepository cashRepository) {
        this.cashRepository = cashRepository;
    }

    public PagingResponse<Cash> findAllCashesWithPaging(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "createdAt"));
        Page<Cash> cashes = cashRepository.findAll(pageable);
        List<Cash> listOfFinancials = cashes.getContent();
        emptyDataCheck(listOfFinancials);
        return new PagingResponse<>(200, listOfFinancials, cashes.getNumber(), cashes.getSize(),
                cashes.getTotalElements(), cashes.getTotalPages(), cashes.isLast());
    }

    public PagingResponse<Cash> findAllCashesWithPagingAndSearch(int pageNo, int pageSize, String description) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "createdAt"));
        Page<Cash> cashes = cashRepository.findByDescriptionContains(description, pageable);
        List<Cash> listOfFinancials = cashes.getContent();
        emptyDataCheck(listOfFinancials);
        return new PagingResponse<>(200, listOfFinancials, cashes.getNumber(), cashes.getSize(),
                cashes.getTotalElements(), cashes.getTotalPages(), cashes.isLast());
    }

    private void emptyDataCheck(List<Cash> data) {
        if (data.isEmpty()) {
            throw new DataNotFoundException("Data is empty");
        }
    }

    public DataResponse<Cash> findCashById(Long id) {
        Cash cash = cashRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cash is not found"));
        return new DataResponse<>(200, cash);
    }

    public DataResponse<Cash> saveCashIncome(CashDto cashDto) {
        Cash cash = new Cash();
        cash.setDescription(cashDto.getDescription());
        cash.setDebit(cashDto.getAmount());
        cash.setCashActivity(CashActivity.INCOME);

        Optional<Cash> cashCheck = cashRepository.findFirstByOrderByCreatedAtDesc();
        if (cashCheck.isPresent()) {
            cash.setBalance(cashCheck.get().getBalance() + cashDto.getAmount());
        } else {
            cash.setBalance(cashDto.getAmount());
        }

        return new DataResponse<>(200, cashRepository.save(cash));
    }

    public DataResponse<Cash> saveCashOutcome(CashDto cashDto) {
        Cash cash = new Cash();
        cash.setDescription(cashDto.getDescription());
        cash.setCredit(cashDto.getAmount());
        cash.setCashActivity(CashActivity.OUTCOME);

        Optional<Cash> cashCheck = cashRepository.findFirstByOrderByCreatedAtDesc();
        if (cashCheck.isEmpty() || cashCheck.get().getBalance() <= cashDto.getAmount()) {
            throw new CashNotEnoughtException("Your cash is not enought");
        } else {
            cash.setBalance(cashCheck.get().getBalance() - cashDto.getAmount());
        }

        return new DataResponse<>(200, cashRepository.save(cash));
    }

}
