package com.wms.controller;

import com.wms.dto.catalog.LocationSetupDTO;
import com.wms.dto.catalog.ProductCreateDTO;
import com.wms.dto.catalog.ProductResponseDTO;
import com.wms.service.CatalogService;
import com.wms.util.ZoneType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller Quản lý Danh mục hàng hóa (SKU) và Vị trí ô kệ kho bãi (UC01 / SCR03).
 */
@Controller
@RequestMapping("/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /**
     * Màn hình Danh mục Hàng hóa & Ô kệ.
     */
    @GetMapping("/products")
    public String productsView(Model model) {
        model.addAttribute("products", catalogService.getAllProducts());
        return "catalog/products";
    }

    /**
     * Màn hình Sơ đồ ô kệ 3M.
     */
    @GetMapping("/locations")
    public String locationsView(Model model) {
        return "catalog/locations";
    }

    // --- REST APIs ---

    @GetMapping("/api/products")
    @ResponseBody
    public ResponseEntity<List<ProductResponseDTO>> getAllProductsApi() {
        return ResponseEntity.ok(catalogService.getAllProducts());
    }

    @PostMapping("/api/products")
    @ResponseBody
    public ResponseEntity<ProductResponseDTO> createProductApi(@Valid @RequestBody ProductCreateDTO dto) {
        ProductResponseDTO created = catalogService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/api/locations")
    @ResponseBody
    public ResponseEntity<Void> setupLocationApi(@Valid @RequestBody LocationSetupDTO dto) {
        catalogService.setupLocation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/api/locations/available")
    @ResponseBody
    public ResponseEntity<List<LocationSetupDTO>> getAvailableLocationsApi(@RequestParam ZoneType zone) {
        return ResponseEntity.ok(catalogService.getAvailableLocationsByZone(zone));
    }
}
