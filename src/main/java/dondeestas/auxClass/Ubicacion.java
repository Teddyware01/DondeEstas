package dondeestas.auxClass;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

@Data
public class Ubicacion {
    private String provincia;
    private String departamento;
    private String municipio;

    public Ubicacion(String provincia, String departamento, String municipio) {
        this.provincia = provincia;
        this.departamento = departamento;
        this.municipio = municipio;
    }

 public static Ubicacion obtenerUbicacionPorLatLon(double lat, double lon) {
     String url = String.format(
            "https://apis.datos.gob.ar/georef/api/ubicacion?lat=%s&lon=%s",
            lat, lon
    );

     RestTemplate restTemplate = new RestTemplate();
    JsonNode json = restTemplate.getForObject(url, JsonNode.class);
    JsonNode u = json.get("ubicacion");

    String provincia,  departamento, municipio;
     provincia = u.get("provincia").get("nombre").asText();
     departamento = u.get("departamento").get("nombre").asText();
     municipio = u.get("municipio").get("nombre").asText();

    return new Ubicacion(provincia,departamento,municipio);

 }
}
