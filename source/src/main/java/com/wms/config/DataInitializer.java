package com.wms.config;

import com.wms.entity.Category;
import com.wms.entity.Product;
import com.wms.entity.User;
import com.wms.entity.WarehouseLocation;
import com.wms.repository.CategoryRepository;
import com.wms.repository.ProductRepository;
import com.wms.repository.UserRepository;
import com.wms.repository.WarehouseLocationRepository;
import com.wms.util.LocationStatus;
import com.wms.util.UserRole;
import com.wms.util.ZoneType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Khởi tạo dữ liệu mẫu (Seed Data) khi ứng dụng SmartWMS khởi động.
 * Tuân thủ đúng chuẩn thiết kế 3 vai trò (UserRole: ROLE_STAFF, ROLE_MANAGER, ROLE_ADMIN).
 * Hỗ trợ thử nghiệm quy tắc Maker-Checker độc lập (BR-SEC-03).
 */
@Component
@org.springframework.core.annotation.Order(0)
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
  private final WarehouseLocationRepository locationRepository;
  private final ProductRepository productRepository;

  public DataInitializer(
    UserRepository userRepository,
    CategoryRepository categoryRepository,
    WarehouseLocationRepository locationRepository,
    ProductRepository productRepository
  ) {
    this.userRepository = userRepository;
    this.categoryRepository = categoryRepository;
    this.locationRepository = locationRepository;
    this.productRepository = productRepository;
  }

  @Override
  public void run(String... args) {
    seedUsers();
    seedCategories();
    seedLocations();
    seedProducts();
  }

  private void seedUsers() {
    if (userRepository.count() > 0) {
      return;
    }

    // 1. Quản trị viên hệ thống (ROLE_ADMIN)
    createUser(
      "admin",
      "123456",
      "Quản trị viên Hệ thống",
      UserRole.ROLE_ADMIN
    );

    // 2. Quản lý kho / Thủ kho trưởng (ROLE_MANAGER - Checker)
    createUser(
      "manager_trung",
      "123456",
      "Nguyễn Thành Trung (Thủ kho trưởng)",
      UserRole.ROLE_MANAGER
    );
    createUser(
      "manager_khang",
      "123456",
      "Nguyễn Bảo Khang (Quản lý kho phó / Checker)",
      UserRole.ROLE_MANAGER
    );

    // 3. Nhân viên kho / Thủ kho thực thi (ROLE_STAFF - Maker)
    createUser(
      "staff_thien",
      "123456",
      "Tôn Thanh Thiên (Thủ kho Tiếp nhận / Maker)",
      UserRole.ROLE_STAFF
    );
    createUser(
      "staff_quan",
      "123456",
      "Thạch Hoàng Quân (Thủ kho Xuất hàng / Maker)",
      UserRole.ROLE_STAFF
    );
  }

  private void createUser(
    String username,
    String password,
    String fullName,
    UserRole role
  ) {
    User user = new User();
    user.setUsername(username);
    user.setPasswordHash(password); // Mật khẩu mẫu demo 123456
    user.setFullName(fullName);
    user.setRole(role);
    user.setActive(true);
    user.setCreatedAt(LocalDateTime.now());
    userRepository.save(user);
  }

  private void seedCategories() {
    if (categoryRepository.count() > 0) {
      return;
    }

    createCategory("LUB", "Dầu nhớt Yamalube & Hóa chất bảo dưỡng chính hãng");
    createCategory(
      "ENG",
      "Động cơ & Cơ khí chính xác (Pít-tông, Bạc đạn, Xú-páp)"
    );
    createCategory(
      "ELE",
      "Hệ thống Điện, Đèn LED & Đánh lửa (Bugi, ECU, Bobin)"
    );
    createCategory(
      "BDY",
      "Dàn áo, Khung sườn & Gương chiếu hậu (Yamaha Genuine Body)"
    );
    createCategory(
      "BRK",
      "Hệ thống Phanh & Dẫn động (Má phanh, Đĩa phanh, Dây curoa)"
    );
  }

  private void createCategory(String code, String name) {
    Category category = new Category();
    category.setCode(code);
    category.setName(name);
    category.setActive(true);
    category.setCreatedAt(LocalDateTime.now());
    categoryRepository.save(category);
  }

  private void seedLocations() {
    if (locationRepository.count() > 0) {
      return;
    }

    createLocation(
      "DOCK-01",
      ZoneType.DOCK,
      new BigDecimal("10000.00"),
      LocationStatus.EMPTY
    );
    createLocation(
      "INSP-01",
      ZoneType.INSP,
      new BigDecimal("5000.00"),
      LocationStatus.EMPTY
    );
    createLocation(
      "RAW-A01-01",
      ZoneType.RAW,
      new BigDecimal("2000.00"),
      LocationStatus.EMPTY
    );
    createLocation(
      "RAW-A01-02",
      ZoneType.RAW,
      new BigDecimal("2000.00"),
      LocationStatus.EMPTY
    );
    createLocation(
      "FG-B01-01",
      ZoneType.FG,
      new BigDecimal("3000.00"),
      LocationStatus.EMPTY
    );
    createLocation(
      "FG-B01-02",
      ZoneType.FG,
      new BigDecimal("3000.00"),
      LocationStatus.EMPTY
    );
    createLocation(
      "FG-B02-01",
      ZoneType.FG,
      new BigDecimal("3000.00"),
      LocationStatus.EMPTY
    );
    createLocation(
      "SCRAP-01",
      ZoneType.SCRAP,
      new BigDecimal("1000.00"),
      LocationStatus.EMPTY
    );
  }

  private void createLocation(
    String locationCode,
    ZoneType zone,
    BigDecimal maxCapacity,
    LocationStatus status
  ) {
    WarehouseLocation loc = new WarehouseLocation();
    loc.setLocationCode(locationCode);
    loc.setZone(zone);
    loc.setShelf("01");
    loc.setMaxCapacity(maxCapacity);
    loc.setStatus(status);
    loc.setCreatedAt(LocalDateTime.now());
    locationRepository.save(loc);
  }

  private void seedProducts() {
    if (productRepository.count() > 0) {
      return;
    }

    Category lub = categoryRepository.findByCode("LUB").orElse(null);
    Category eng = categoryRepository.findByCode("ENG").orElse(null);
    Category brk = categoryRepository.findByCode("BRK").orElse(null);
    Category ele = categoryRepository.findByCode("ELE").orElse(null);
    Category bdy = categoryRepository.findByCode("BDY").orElse(null);

    if (lub != null) {
      createProduct(
        lub,
        "YAM-LUB-001",
        "Dầu nhớt Yamalube 4T 10W-40 GP 800ml (Dầu động cơ xe số)",
        "Chai",
        50,
        500
      );
      createProduct(
        lub,
        "YAM-LUB-002",
        "Dầu nhớt Yamalube AT 10W-40 800ml (Dầu động cơ xe tay ga)",
        "Chai",
        50,
        500
      );
    }
    if (eng != null) {
      createProduct(
        eng,
        "YAM-ENG-001",
        "Bộ pít-tông & xéc-măng Exciter 155 VVA chính hãng",
        "Bộ",
        20,
        200
      );
    }
    if (brk != null) {
      createProduct(
        brk,
        "YAM-BRK-001",
        "Bộ má phanh đĩa trước NVX 155 ABS",
        "Bộ",
        30,
        300
      );
    }
    if (ele != null) {
      createProduct(
        ele,
        "YAM-ELE-001",
        "Bugi tiêu chuẩn NGK CPR8EA-9 Yamaha chính hãng",
        "Cái",
        100,
        1000
      );
    }
    if (bdy != null) {
      createProduct(
        bdy,
        "YAM-BDY-001",
        "Ốp yếm sườn phải YZF-R15 V4 Racing Blue",
        "Cái",
        10,
        100
      );
    }
  }

  private void createProduct(
    Category category,
    String sku,
    String name,
    String uom,
    int minStock,
    int maxStock
  ) {
    Product p = new Product();
    p.setCategory(category);
    p.setSkuCode(sku);
    p.setName(name);
    p.setUom(uom);
    p.setMinStock(BigDecimal.valueOf(minStock));
    p.setMaxStock(BigDecimal.valueOf(maxStock));
    p.setCreatedAt(LocalDateTime.now());
    productRepository.save(p);
  }
}
