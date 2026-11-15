package com.wms.service;

import com.wms.dto.catalog.LocationSetupDTO;
import com.wms.dto.catalog.ProductCreateDTO;
import com.wms.dto.catalog.ProductResponseDTO;
import com.wms.util.ZoneType;

import java.util.List;

public interface CatalogService {
    ProductResponseDTO createProduct(ProductCreateDTO dto);
    ProductResponseDTO getProductById(Long productId);
    List<ProductResponseDTO> getAllProducts();
    void setupLocation(LocationSetupDTO dto);
    List<LocationSetupDTO> getAvailableLocationsByZone(ZoneType zone);
}

