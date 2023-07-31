package com.rudykart.limbah.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rudykart.limbah.dto.DataResponse;
import com.rudykart.limbah.dto.MaterialDto;
import com.rudykart.limbah.dto.PagingResponse;
import com.rudykart.limbah.entities.Material;
import com.rudykart.limbah.services.MaterialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/materials")

public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping
    public ResponseEntity<DataResponse<List<Material>>> findAllMaterials() {
        return ResponseEntity.ok().body(materialService.findAllMaterials());
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping("/{pageSize}/{pageNo}")
    public ResponseEntity<PagingResponse<Material>> findAllMaterialsWithPaging(@PathVariable int pageNo,
            @PathVariable int pageSize) {
        return ResponseEntity.ok().body(materialService.findAllMaterialsWithPaging(pageNo, pageSize));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping("/{search}/{pageSize}/{pageNo}")
    public ResponseEntity<PagingResponse<Material>> findAllMaterialsWithPagingAndSearch(@PathVariable int pageNo,
            @PathVariable int pageSize, @PathVariable String search) {
        return ResponseEntity.ok().body(materialService.findAllMaterialsWithPagingAndSearch(pageNo, pageSize, search));
    }

    @PreAuthorize("hasAuthority('BOSS')")
    @PostMapping
    public ResponseEntity<DataResponse<Material>> saveMaterial(@RequestBody @Valid MaterialDto materialDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.saveMaterial(materialDto));
    }

    @PreAuthorize("hasAuthority('EMPLOYEE') OR hasAuthority('BOSS')")
    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<Material>> findMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok().body(materialService.findMaterialById(id));
    }

    @PreAuthorize("hasAuthority('BOSS')")
    @PutMapping("/{id}/update")
    public ResponseEntity<DataResponse<Material>> updateMaerial(@PathVariable Long id,
            @Valid @RequestBody MaterialDto materialDto) {
        return ResponseEntity.ok().body(materialService.updateMaterial(id, materialDto));
    }

       @PreAuthorize("hasAuthority('BOSS')")
 @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMaterial(@PathVariable Long id) {
        return new ResponseEntity<>(materialService.deleteMaterial(id), HttpStatus.OK);
    }
}
