package dondeestas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerPrueba {

    @GetMapping("/")
    public String home() {
        return "¡La aplicación está funcionando correctamente!";
    }
}
