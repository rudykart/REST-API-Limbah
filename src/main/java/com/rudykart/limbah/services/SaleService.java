package com.rudykart.limbah.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.rudykart.limbah.dto.DataResponse;
import com.rudykart.limbah.dto.PagingResponse;
import com.rudykart.limbah.dto.SaleDto;
import com.rudykart.limbah.entities.Cash;
import com.rudykart.limbah.entities.CashActivity;
import com.rudykart.limbah.entities.Purchase;
import com.rudykart.limbah.entities.Sale;
import com.rudykart.limbah.entities.StatusPurchase;
import com.rudykart.limbah.entities.user.User;
import com.rudykart.limbah.exceptions.DataNotFoundException;
import com.rudykart.limbah.repositories.CashRepository;
import com.rudykart.limbah.repositories.PurchaseRepository;
import com.rudykart.limbah.repositories.SaleRepository;
import com.rudykart.limbah.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SaleService {
    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final CashRepository cashRepository;

    public SaleService(SaleRepository saleRepository, PurchaseRepository purchaseRepository,
            UserRepository userRepository, CashRepository cashRepository) {
        this.saleRepository = saleRepository;
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.cashRepository = cashRepository;
    }

    public PagingResponse<Sale> findAllSalesWithPaging(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "createdAt"));
        Page<Sale> sales = saleRepository.findAll(pageable);
        List<Sale> listOfSale = sales.getContent();
        emptyDataCheck(listOfSale);
        return new PagingResponse<>(200, listOfSale, sales.getNumber(), sales.getSize(),
                sales.getTotalElements(), sales.getTotalPages(), sales.isLast());
    }

    public DataResponse<Sale> findAllSaleById(Long id) {
        Sale sale = saleRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Data is not found"));
        return new DataResponse<>(200, sale);
    }

    private void emptyDataCheck(List<Sale> data) {
        if (data.isEmpty()) {
            throw new DataNotFoundException("Data is empty");
        }
    }

    public DataResponse<Sale> saveSale(SaleDto saleDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Sale sale = new Sale();
        sale.setBuyerName(saleDto.getBuyerName());
        sale.setDescription(saleDto.getDescription());
        sale.setIncome(saleDto.getIncome());
        sale.setUser(user);

        Set<Purchase> purchases = new HashSet<>();
        List<Long> purchaseIds = saleDto.getPurchases();

        for (Long purchaseId : purchaseIds) {
            Purchase purchase = purchaseRepository.findById(purchaseId)
                    .orElseThrow(() -> new DataNotFoundException("Purchase with ID " + purchaseId + " not found"));

            if (purchase.getStatusPurchase() == StatusPurchase.SOLD) {
                throw new DataNotFoundException(
                        "Purchase with ID " + purchaseId + " is already associated with a Sale");
            }

            purchase.setStatusPurchase(StatusPurchase.SOLD);
            purchaseRepository.save(purchase);
            purchases.add(purchase);
        }

        Cash cash = new Cash();
        cash.setDescription("Sale material from " + saleDto.getBuyerName());
        cash.setDebit(saleDto.getIncome());
        cash.setCashActivity(CashActivity.SALE);

        Optional<Cash> cashCheck = cashRepository.findFirstByOrderByCreatedAtDesc();
        if (cashCheck.isPresent()) {
            cash.setBalance(cashCheck.get().getBalance() + saleDto.getIncome());
        } else {
            cash.setBalance(saleDto.getIncome());
        }

        cashRepository.save(cash);
        sale.setPurchases(purchases);
        Sale savedSale = saleRepository.save(sale);
        return new DataResponse<>(200, savedSale);
    }

    public DataResponse<Sale> updateSale(Long saleId, SaleDto saleDto) {
        Sale existingSale = saleRepository.findById(saleId)
                .orElseThrow(() -> new DataNotFoundException("Sale not found"));

        // Lakukan update pada atribut Sale dari saleDto
        existingSale.setDescription(saleDto.getDescription());
        existingSale.setBuyerName(saleDto.getBuyerName());
        existingSale.setIncome(saleDto.getIncome());

        // Ambil set data Purchase yang ada pada Sale
        Set<Purchase> existingPurchases = existingSale.getPurchases();
        if (existingPurchases == null) {
            existingPurchases = new HashSet<>();
        }

        // Ambil list purchaseIds yang ada pada saleDto
        List<Long> purchaseIds = saleDto.getPurchases();

        // Hapus purchase yang tidak ada di list purchaseIds dari relasi Sale-Purchase
        // existingPurchases.removeIf(purchase ->
        // !purchaseIds.contains(purchase.getId()));

        // Tambahkan purchase baru dari list purchaseIds yang belum ada di relasi
        // Sale-Purchase
        for (Long purchaseId : purchaseIds) {
            Purchase purchase = purchaseRepository.findById(purchaseId)
                    .orElseThrow(() -> new DataNotFoundException("Purchase with ID " + purchaseId + " not found"));

            // if (purchase.getStatusPurchase() == StatusPurchase.SOLD) {
            // throw new DataNotFoundException(
            // "Purchase with ID " + purchaseId + " is already associated with a Sale");
            // }

            purchase.setStatusPurchase(StatusPurchase.SOLD);
            purchaseRepository.save(purchase);

            existingPurchases.add(purchase);
        }

        // Set data Purchase yang telah diperbarui ke Sale
        existingSale.setPurchases(existingPurchases);

        // Simpan perubahan Sale ke database
        Sale updatedSale = saleRepository.save(existingSale);

        return new DataResponse<>(200, updatedSale);
    }

    public DataResponse<Map<String, String>> deleteSale(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new DataNotFoundException("Sale not found"));

        Map<String, String> data = new HashMap<String, String>();

        // Hapus asosiasi dengan pembelian yang terkait
        Set<Purchase> purchases = sale.getPurchases();
        for (Purchase purchase : purchases) {
            purchase.setStatusPurchase(StatusPurchase.NOT_SOLD);
            // Jangan menghapus data Purchase, hanya simpan perubahan status
            purchaseRepository.save(purchase);
        }

        data.put("message", "Sale with ID " + saleId + " has been disassociated from its Purchases.");
        sale.setPurchases(null);
        saleRepository.save(sale);
        // Hapus data Sale dari database
        saleRepository.delete(sale);

        return new DataResponse<>(200, data);
    }

    // public DataResponse<Map<String, String>> deleteSale(Long saleId) {
    // Sale sale = saleRepository.findById(saleId)
    // .orElseThrow(() -> new DataNotFoundException("Sale not found"));

    // Map<String, String> data = new HashMap<String, String>();

    // // Hapus asosiasi dengan pembelian yang terkait
    // Set<Purchase> purchases = sale.getPurchases();
    // for (Purchase purchase : purchases) {
    // purchase.setStatusPurchase(StatusPurchase.NOT_SOLD);
    // }

    // // Hapus asosiasi antara Sale dan Purchase
    // sale.getPurchases().clear();

    // data.put("message", "Sale with ID " + saleId + " has been disassociated from
    // its Purchases.");

    // // Hapus data Sale dari database
    // saleRepository.delete(sale);

    // return new DataResponse<>(200, data);
    // }
}
