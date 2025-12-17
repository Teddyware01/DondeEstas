package dondeestas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStats {
    private long totalUsuarios;
    private long busquedasActivas;
    private long reencuentrosFelices;
    private long barriosCubiertos;
    private long provinciasCubiertas;


}