package dondeestas.auxClass;

import dondeestas.entity.Usuario;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RankingItem {
    @Getter
    @Setter
    private Usuario usuario;

    @Getter
    @Setter
    private int totalPuntos;

    @Getter
    @Setter
    private int cantMedallas;

    public RankingItem(Usuario usuario, int totalPuntos, int cantMedallas) {
        this.usuario = usuario;
        this.totalPuntos = totalPuntos;
        this.cantMedallas = cantMedallas;
    }


    public String toRankingString(int posicion) {
        return String.format(
                "%d) %s — %d pts — 🏅 %d medallas",
                posicion,
                usuario.getNombre(),   // Ajustá según tu entidad Usuario
                totalPuntos,
                cantMedallas
        );
    }

}
