package com.wms.service;

import com.wms.repository.CategoryRepository;
import com.wms.repository.WarehouseLocationRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Read models for catalog controls; no entity serialization across the API boundary. */
@Service
@Transactional(readOnly = true)
public class UiQueryService {

  private final CategoryRepository categories;
  private final WarehouseLocationRepository locations;

  public UiQueryService(
    CategoryRepository categories,
    WarehouseLocationRepository locations
  ) {
    this.categories = categories;
    this.locations = locations;
  }

  public record CategoryView(Long id, String name) {}

  public record LocationView(
    Long id,
    String locationCode,
    String zone,
    String status,
    BigDecimal maxCapacity,
    String shelf,
    String aisle,
    String rack,
    String bin
  ) {}

  public List<CategoryView> categories() {
    return categories
      .findAll()
      .stream()
      .filter(category -> Boolean.TRUE.equals(category.getActive()))
      .map(category -> new CategoryView(category.getId(), category.getName()))
      .toList();
  }

  public List<LocationView> locations() {
    return locations
      .findAll()
      .stream()
      .map(location ->
        new LocationView(
          location.getId(),
          location.getLocationCode(),
          location.getZone().name(),
          location.getStatus().name(),
          location.getMaxCapacity(),
          location.getShelf(),
          location.getAisle(),
          location.getRack(),
          location.getBin()
        )
      )
      .toList();
  }
}
