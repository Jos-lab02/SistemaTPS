import java.util.ArrayList;
import java.util.List;

public class ConsultorioService {
    // Nombre del archivo manual
    private static final String FILENAME = "data/consultorios.txt";

    public static List<Consultorio> cargarConsultorios() {

        List<Consultorio> lista = new ArrayList<>();

        // Usamos el lector genérico de UsuarioFile
        List<String[]> lineas = UsuarioFile.leer(FILENAME);

        // Si el archivo no existe o está vacío, lineas será una lista vacía
        for (String[] datos : lineas) {
            // Validamos que la línea tenga al menos Nombre y Dirección
            if (datos.length >= 2) {
                String nombre = datos[0].trim();
                String direccion = datos[1].trim();

                // Creamos el objeto y lo añadimos a la lista
                lista.add(new Consultorio(nombre, direccion));
            }
        }

        // Retornamos la lista tal cual (vacía o con datos)
        return lista;
    }
}