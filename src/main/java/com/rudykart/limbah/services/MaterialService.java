package com.rudykart.limbah.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
// import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.rudykart.limbah.dto.DataResponse;
import com.rudykart.limbah.dto.MaterialDto;
import com.rudykart.limbah.dto.PagingResponse;
import com.rudykart.limbah.entities.Material;
import com.rudykart.limbah.entities.StatusMaterial;
import com.rudykart.limbah.exceptions.DataNotFoundException;
import com.rudykart.limbah.repositories.MaterialRepository;

@Service
public class MaterialService {
    private static final Logger LOGGER = Logger.getLogger(MaterialService.class.getName());
    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public DataResponse<List<Material>> findAllMaterials() {
        List<Material> materials = materialRepository.findAll(Sort.by(Direction.DESC, "createdAt"));
        emptyDataCheck(materials);
        materials.forEach(material -> {
            LOGGER.log(Level.INFO, "Material : {0}", material.getName());
        });
        return new DataResponse<>(200, materials);
    }

    public PagingResponse<Material> findAllMaterialsWithPaging(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "createdAt"));
        Page<Material> materials = materialRepository.findAll(pageable);
        List<Material> listOfMaterial = materials.getContent();
        emptyDataCheck(listOfMaterial);

        PagingResponse<Material> response = new PagingResponse<>(HttpStatus.OK.value(), listOfMaterial,
                materials.getNumber(), materials.getSize(),
                materials.getTotalElements(), materials.getTotalPages(), materials.isLast());
        return response;
    }

    public PagingResponse<Material> findAllMaterialsWithPagingAndSearch(int pageNo, int pageSize, String name) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "createdAt"));
        Page<Material> materials = materialRepository.findByNameContains(name, pageable);
        List<Material> listOfMaterial = materials.getContent();
        emptyDataCheck(listOfMaterial);

        PagingResponse<Material> response = new PagingResponse<>(HttpStatus.OK.value(), listOfMaterial,
                materials.getNumber(), materials.getSize(),
                materials.getTotalElements(), materials.getTotalPages(), materials.isLast());
        return response;
    }

    private void emptyDataCheck(List<Material> data) {
        if (data.isEmpty()) {
            throw new DataNotFoundException("Data is empty");
        }
    }

    public DataResponse<Material> saveMaterial(MaterialDto materialDto) {
        Material material = new Material();
        material.setName(materialDto.getName());
        material.setPrice(materialDto.getPrice());
        material.setStatus(StatusMaterial.valueOf(materialDto.getStatus()));
        return new DataResponse<>(200, materialRepository.save(material));
    }

    public DataResponse<Material> findMaterialById(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Material is not found"));
        return new DataResponse<>(200, material);
    }

    public DataResponse<Material> updateMaterial(Long id, MaterialDto materialDto) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Material is not found"));
        material.setName(materialDto.getName());
        material.setPrice(materialDto.getPrice());
        material.setStatus(StatusMaterial.valueOf(materialDto.getStatus()));
        return new DataResponse<>(200, materialRepository.save(material));
    }

    public DataResponse<Map<String, String>> deleteMaterial(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Material is not found"));

        Map<String, String> data = new HashMap<String, String>();
        data.put("name", material.getName());
        data.put("price", Integer.toString(material.getPrice()));
        data.put("status", "Deleted");

        materialRepository.deleteById(id);
        return new DataResponse<>(200, data);
    }

}
