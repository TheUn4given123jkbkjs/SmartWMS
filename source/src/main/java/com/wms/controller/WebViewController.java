package com.wms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller điều hướng giao diện Web Prototype (Landing Page & Dashboard WMS).
 * Phục vụ đồ án Sprint 4 - UI/UX Design Prototype.
 */
@Controller
public class WebViewController {

    /**
     * Trang chủ mặc định: Landing Page giới thiệu dịch vụ kho bãi SmartWMS & đối tác Yamaha.
     * Truy cập: http://localhost:8080/ hoặc http://localhost:8080/landing
     */
    @GetMapping({"/", "/landing", "/landing.html"})
    public String landingPage() {
        return "landing";
    }

    /**
     * Trang ứng dụng tương tác 6 màn hình quản trị kho vận (SCR01 - SCR06).
     * Truy cập: http://localhost:8080/app hoặc http://localhost:8080/index hoặc http://localhost:8080/index.html
     */
    @GetMapping({"/app", "/index", "/index.html", "/dashboard"})
    public String appDashboard() {
        return "index";
    }
}
