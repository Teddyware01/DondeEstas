package dondeestas.auxClass;

public class UbiTest {

    public static void main(String[] args) throws Exception {

        Ubicacion ubi = Ubicacion.obtenerUbicacionPorLatLon( -34.7975, -58.2759);
        System.out.println(ubi);
        System.out.println(ubi.getMunicipio());

    }

}
