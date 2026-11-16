package com.wms.repository;

import com.wms.entity.WarehouseLocation;
import com.wms.util.LocationStatus;
import com.wms.util.ZoneType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocation, Long> {
    Optional<WarehouseLocation> findByLocationCode(String locationCode);
    List<WarehouseLocation> findByZone(ZoneType zone);
    List<WarehouseLocation> findByStatus(LocationStatus status);
}
