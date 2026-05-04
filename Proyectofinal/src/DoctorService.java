import java.util.ArrayList;
import java.util.List;

public class DoctorService {
    public static List<Doctor> obtenerDoctoresRegistrados() {
        List<Doctor> lista = new ArrayList<>();
        // Importante: Necesitas tener acceso a la lista global de consultorios para vincularlos
        // Si no la tienes aquí, puedes crear objetos nuevos solo con el nombre.

        List<String[]> lineas = UsuarioFile.leer("data/usuarios.txt");

        for (String[] fila : lineas) {
            if (fila.length >= 4 && fila[2].equalsIgnoreCase("doctor")) {

                String ced = fila[0];
                String pass = fila[1];
                String[] datos = fila[3].split(";",-1);
                if (datos.length<7){
                    datos=java.util.Arrays.copyOf(datos,7);
                }

                String nom = datos.length > 0 ? datos[0] : "Sin nombre";
                String ape = datos.length > 1 ? datos[1] : "Sin apellido";
                String eda = datos.length > 2 ? datos[2] : "0";
                String cor = datos.length > 3 ? datos[3] : "no-mail@clinica.com";
                String esp = datos.length > 4 ? datos[4] : "General";
                String tel = datos.length > 5 ? datos[5] : "0000000000";

                // 1. Creamos el objeto Doctor
                Doctor nuevoDoc = new Doctor(nom, ape, eda, ced, pass, cor, esp, tel);

                // 2. RECONSTRUCCIÓN DE CONSULTORIOS (Lo que faltaba)
                if (datos.length > 6 && datos[6]!=null&&!datos[6].equalsIgnoreCase("Ninguno")) {
                    // Separamos por si tiene varios: Clínica A:Lunes-8-12|Clínica B:Martes-10-14
                    String[] consultoriosRaw = datos[6].split("\\|");

                    for (String conRaw : consultoriosRaw) {


                        if (conRaw.contains(":")) {
                            String[] partes = conRaw.split(":", -1);

                            if (partes.length < 2) {
                                continue; // línea mal formada, la saltamos
                            }
//                            String[] partes = conRaw.split(":");
                            String nombreCons = partes[0];
                            String horariosPart = partes[1];

                            // Creamos el consultorio para este doctor
                            Consultorio c = new Consultorio(nombreCons, "");

                            // Reconstruimos los horarios (Lunes-8-14)
                            String[] listaHorarios = horariosPart.split("/");
                            for (String hStr : listaHorarios) {
                                String[] hDatos = hStr.split("-");
                                if (hDatos.length == 3) {
                                    c.getHorarios().add(new Horario(
                                            hDatos[0],
                                            Integer.parseInt(hDatos[1]),
                                            Integer.parseInt(hDatos[2])
                                    ));
                                }
                            }
                            // Añadimos el consultorio al doctor
                            nuevoDoc.getConsultoriosAsociados().add(c);
                        }
                    }
                }

                lista.add(nuevoDoc);
            }
        }
        return lista;
    }
}