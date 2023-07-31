package com.rudykart.limbah.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.BeanUtils;
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
import com.rudykart.limbah.dto.MaterialPurchaseDto;
import com.rudykart.limbah.dto.PagingResponse;
import com.rudykart.limbah.dto.PurchaseDto;
import com.rudykart.limbah.entities.Cash;
import com.rudykart.limbah.entities.CashActivity;
import com.rudykart.limbah.entities.Customer;
import com.rudykart.limbah.entities.Material;
import com.rudykart.limbah.entities.MaterialPurchase;
import com.rudykart.limbah.entities.Purchase;
import com.rudykart.limbah.entities.StatusPurchase;
import com.rudykart.limbah.entities.user.User;
import com.rudykart.limbah.exceptions.CashNotEnoughtException;
import com.rudykart.limbah.exceptions.DataNotFoundException;
import com.rudykart.limbah.repositories.CashRepository;
import com.rudykart.limbah.repositories.CustomerRepository;
import com.rudykart.limbah.repositories.MaterialPurchaseRepository;
import com.rudykart.limbah.repositories.MaterialRepository;
import com.rudykart.limbah.repositories.PurchaseRepository;
import com.rudykart.limbah.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PurchaseService {

    private final CustomerRepository customerRepository;
    private final PurchaseRepository purchaseRepository;
    private final MaterialPurchaseRepository materialPurchaseRepository;
    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;
    private final CashRepository cashRepository;

    public PurchaseService(CustomerRepository customerRepository, PurchaseRepository purchaseRepository,
            MaterialPurchaseRepository materialPurchaseRepository, MaterialRepository materialRepository,
            UserRepository userRepository, CashRepository cashRepository) {
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
        this.materialPurchaseRepository = materialPurchaseRepository;
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
        this.cashRepository = cashRepository;
    }

    public DataResponse<List<Purchase>> findAllPurchases() {
        List<Purchase> purchases = purchaseRepository.findAll(Sort.by(Direction.DESC, "createdAt"));
        return new DataResponse<>(200, purchases);
    }

    public PagingResponse<Purchase> findAllPurchaseWithPaging(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "createdAt"));
        Page<Purchase> purchases = purchaseRepository.findAll(pageable);
        List<Purchase> listOfPurchase = purchases.getContent();
        if (listOfPurchase.isEmpty()) {
            throw new DataNotFoundException("Data is empty");
        }
        return new PagingResponse<>(200, listOfPurchase, purchases.getNumber(), purchases.getSize(),
                purchases.getTotalElements(), purchases.getTotalPages(), purchases.isLast());
    }

    private Integer calculateTotalPaid(Set<MaterialPurchase> materialPurchases) {
        Integer totalPaid = 0;
        for (MaterialPurchase materialPurchase : materialPurchases) {
            totalPaid += materialPurchase.getPurchasePrice() * materialPurchase.getQuantity();
        }
        return totalPaid;
    }

    public DataResponse<Purchase> savePurchase(PurchaseDto purchaseDto) {
        // Mendapatkan informasi pengguna yang sedang login
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Customer customer = customerRepository.findById(purchaseDto.getCustomerId())
                .orElseThrow(() -> new DataNotFoundException("Customer not found"));
        Purchase purchase = new Purchase();
        // Set data dari PurchaseDTO ke objek Purchase
        purchase.setCustomer(customer);
        purchase.setUser(user);
        purchase.setStatusPurchase(StatusPurchase.NOT_SOLD);
        // Set data MaterialPurchase dari PurchaseDTO ke objek Purchase
        Set<MaterialPurchase> materialPurchases = new HashSet<>();
        for (MaterialPurchaseDto materialPurchaseDto : purchaseDto.getMaterialPurchases()) {
            Material material = materialRepository.findById(materialPurchaseDto.getMaterialId())
                    .orElseThrow(() -> new DataNotFoundException("Material not found"));
            MaterialPurchase materialPurchase = new MaterialPurchase();
            materialPurchase.setPurchasePrice(material.getPrice());
            materialPurchase.setQuantity(materialPurchaseDto.getQuantity());
            materialPurchase.setMaterial(material);
            materialPurchase.setPurchase(purchase);
            materialPurchases.add(materialPurchase);
        }

        purchase.setMaterialPurchases(materialPurchases);

        // Hitung totalPaid dan set ke objek Purchase
        Integer totalPaid = calculateTotalPaid(materialPurchases);
        purchase.setTotalPaid(totalPaid);

        // Cek saldo
        Optional<Cash> cashCheck = cashRepository.findFirstByOrderByCreatedAtDesc();
        if (cashCheck.isEmpty() || cashCheck.get().getBalance() <= totalPaid) {
            throw new CashNotEnoughtException("Your cash is not enought");
        } else {
            Cash cash = new Cash();
            cash.setDescription("payment material " + customer.getName());
            cash.setCredit(totalPaid);
            cash.setCashActivity(CashActivity.PURCHASE);
            cash.setBalance(cashCheck.get().getBalance() - totalPaid);
            cashRepository.save(cash);
        }

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return new DataResponse<>(200, savedPurchase);
    }

    public DataResponse<Purchase> findPurchaseById(Long Id) {
        Purchase purchase = purchaseRepository.findById(Id)
                .orElseThrow(() -> new DataNotFoundException("Data not found"));
        return new DataResponse<>(200, purchase);
    }

    public DataResponse<Purchase> updatePurchase(Long purchaseId, PurchaseDto purchaseDto) {
        Purchase existingPurchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new DataNotFoundException("Purchase not found"));

        // Lakukan pengecekan dan update data sesuai kebutuhan dari purchaseDto
        // Contoh: Update data customer dari purchase
        if (purchaseDto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(purchaseDto.getCustomerId())
                    .orElseThrow(() -> new DataNotFoundException("Customer not found"));
            existingPurchase.setCustomer(customer);
        }

        // Update data lainnya sesuai kebutuhan dari purchaseDto
        // ...

        // Cek apakah ada materialPurchase yang ditambahkan
        if (purchaseDto.getMaterialPurchasesToAdd() != null && !purchaseDto.getMaterialPurchasesToAdd().isEmpty()) {
            for (MaterialPurchaseDto materialPurchaseDto : purchaseDto.getMaterialPurchasesToAdd()) {
                Material material = materialRepository.findById(materialPurchaseDto.getMaterialId())
                        .orElseThrow(() -> new DataNotFoundException("Material not found"));

                MaterialPurchase materialPurchase = new MaterialPurchase();
                materialPurchase.setPurchasePrice(material.getPrice());
                materialPurchase.setQuantity(materialPurchaseDto.getQuantity());
                materialPurchase.setMaterial(material);
                materialPurchase.setPurchase(existingPurchase);
                existingPurchase.getMaterialPurchases().add(materialPurchase);
            }
        }

        // Cek apakah ada materialPurchase yang dihapus
        if (purchaseDto.getMaterialPurchaseIdsToDelete() != null
                && !purchaseDto.getMaterialPurchaseIdsToDelete().isEmpty()) {
            Set<MaterialPurchase> materialPurchasesToDelete = new HashSet<>();
            for (Long materialPurchaseId : purchaseDto.getMaterialPurchaseIdsToDelete()) {
                MaterialPurchase materialPurchase = materialPurchaseRepository.findById(materialPurchaseId)
                        .orElseThrow(() -> new DataNotFoundException("MaterialPurchase not found"));
                if (materialPurchase.getPurchase().getId().equals(existingPurchase.getId())) {
                    materialPurchasesToDelete.add(materialPurchase);
                }
            }
            existingPurchase.getMaterialPurchases().removeAll(materialPurchasesToDelete);
        }

        // Set data MaterialPurchase yang ada di purchaseDto ke objek Purchase
        if (purchaseDto.getMaterialPurchases() != null && !purchaseDto.getMaterialPurchases().isEmpty()) {
            for (MaterialPurchaseDto materialPurchaseDto : purchaseDto.getMaterialPurchases()) {
                Material material = materialRepository.findById(materialPurchaseDto.getMaterialId())
                        .orElseThrow(() -> new DataNotFoundException("Material not found"));

                // Cari materialPurchase berdasarkan materialId, jika ada lakukan update
                MaterialPurchase existingMaterialPurchase = existingPurchase.getMaterialPurchases().stream()
                        .filter(mp -> mp.getMaterial().getId().equals(material.getId())).findFirst().orElse(null);

                if (existingMaterialPurchase != null) {
                    existingMaterialPurchase.setPurchasePrice(material.getPrice());
                    existingMaterialPurchase.setQuantity(materialPurchaseDto.getQuantity());
                    // existingMaterialPurchase.setStatusPurchase(StatusPurchase.NOT_SOLD);
                    existingMaterialPurchase.setMaterial(material);
                } else {
                    // Jika tidak ada materialPurchase dengan materialId yang sama, berarti ini
                    // material baru yang ditambahkan
                    MaterialPurchase newMaterialPurchase = new MaterialPurchase();
                    newMaterialPurchase.setPurchasePrice(material.getPrice());
                    newMaterialPurchase.setQuantity(materialPurchaseDto.getQuantity());
                    // newMaterialPurchase.setStatusPurchase(StatusPurchase.NOT_SOLD);
                    newMaterialPurchase.setMaterial(material);
                    newMaterialPurchase.setPurchase(existingPurchase);
                    existingPurchase.getMaterialPurchases().add(newMaterialPurchase);
                }
            }
        }

        // Hitung ulang totalPaid dan set ke objek Purchase
        Integer totalPaid = calculateTotalPaid(existingPurchase.getMaterialPurchases());
        existingPurchase.setTotalPaid(totalPaid);

        // BeanUtils untuk mengcopy atribut yang ada di purchaseDto ke existingPurchase
        BeanUtils.copyProperties(purchaseDto, existingPurchase, "id", "createdAt", "updatedAt");

        // Simpan perubahan ke database
        Purchase updatedPurchase = purchaseRepository.save(existingPurchase);

        return new DataResponse<>(200, updatedPurchase);
    }

    public DataResponse<Map<String, String>> deletePurchase(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new DataNotFoundException("Purchase not found"));

        Map<String, String> data = new HashMap<String, String>();

        if (!purchase.getMaterialPurchases().isEmpty()) {
            materialPurchaseRepository.deleteAll(purchase.getMaterialPurchases());
        }

        data.put("message",
                "Purchase with ID " + purchaseId + " and its associated MaterialPurchases have been deleted.");

        purchaseRepository.delete(purchase);
        return new DataResponse<>(200, data);
    }
}
