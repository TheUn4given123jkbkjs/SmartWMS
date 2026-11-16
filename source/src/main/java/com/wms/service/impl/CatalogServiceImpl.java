package com.wms.service.impl;

import com.wms.dto.catalog.LocationSetupDTO;
import com.wms.dto.catalog.ProductCreateDTO;
import com.wms.dto.catalog.ProductResponseDTO;
import com.wms.entity.Category;
import com.wms.entity.Product;
import com.wms.entity.WarehouseLocation;
import com.wms.exception.InvalidQuantityException;
import com.wms.exception.ResourceNotFoundException;
import com.wms.repository.CategoryRepository;
import com.wms.repository.ProductRepository;
import com.wms.repository.WarehouseLocationRepository;
import com.wms.service.CatalogService;
import com.wms.util.LocationStatus;
import com.wms.util.ZoneType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Hiện thực Service Quản lý Danh mục hàng hóa (SKU) và Vị trí ô kệ 3M.
 */
@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final WarehouseLocationRepository locationRepository;

    public CatalogServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              WarehouseLocationRepository locationRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public ProductResponseDTO createProduct(ProductCreateDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành hàng ID: " + dto.getCategoryId()));

        if (dto.getMaxStock() <= dto.getMinStock()) {
            throw new InvalidQuantityException("Tồn kho tối đa (maxStock) phải lớn hơn tồn tối thiểu (minStock).");
        }

        Product product = new Product();
        product.setCategory(category);
        product.setSkuCode(dto.getSkuCode());
        product.setName(dto.getName());
        product.setUom(dto.getUom());
        product.setMinStock(dto.getMinStock());
        product.setMaxStock(dto.getMaxStock());
        product.setManageByLot(dto.getManageByLot());

        Product saved = productRepository.save(product);
        return mapToProductResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có ID: " + productId));
        return mapToProductResponseDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::mapToProductResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void setupLocation(LocationSetupDTO dto) {
        WarehouseLocation location = new WarehouseLocation();
        location.setLocationCode(dto.getLocationCode());
        location.setZone(dto.getZone());
        location.setMaxCapacity(dto.getMaxCapacity());
        location.setStatus(LocationStatus.EMPTY);
        locationRepository.save(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationSetupDTO> getAvailableLocationsByZone(ZoneType zone) {
        return locationRepository.findByZone(zone).stream()
            .filter(loc -> loc.getStatus() == LocationStatus.EMPTY)
            .map(loc -> new LocationSetupDTO(loc.getLocationCode(), loc.getZone(), loc.getMaxCapacity()))
            .collect(Collectors.toList());
    }

    private ProductResponseDTO mapToProductResponseDTO(Product p) {
        return new ProductResponseDTO(
            p.getId(), p.getSkuCode(), p.getName(),
            p.getCategory().getName(), p.getUom(),
            p.getMinStock(), p.getMaxStock(), p.getManageByLot()
        );
    }
}
