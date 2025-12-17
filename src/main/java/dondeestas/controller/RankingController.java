package dondeestas.controller;

import dondeestas.auxClass.RankingItem;
import dondeestas.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping // Maneja GET http://localhost:8080/api/ranking
    public ResponseEntity<List<RankingItem>> obtenerRanking() {
        List<RankingItem> ranking = rankingService.obtenerRankingCompleto();

        if (ranking.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(ranking);
    }
}
