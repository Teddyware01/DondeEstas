package dondeestas.controller;

import dondeestas.dto.DashboardStats;
import dondeestas.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> obtenerEstadisticas() {
        DashboardStats stats = dashboardService.obtenerEstadisticas();
        return ResponseEntity.ok(stats);
    }
}