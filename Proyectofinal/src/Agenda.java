import java.util.*;
import java.io.*;

public class Agenda {

    private static List<Cita> citas = new ArrayList<>();
    private static final String FILE_PATH = "data/citas.txt";

    // -------- Cargar citas desde archivo --------
    public static void cargarCitas() {
        citas.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea;

            while ((linea = br.readLine()) != null) {
                // IMPORTANTE: sin límite de columnas
                String[] p = linea.split(",", -1);

                // cedPac, cedDoc, esp, cons, dia, hora
                if (p.length < 6) continue;

                Doctor doctor = new Doctor("Dr.", "Apellido", "0", p[1], "N/A", "", p[2], "");
                Paciente paciente = new Paciente("Paciente", "Apellido", "0", p[0], "N/A", "N/A", "N/A");
                Consultorio consultorio = new Consultorio(p[3], "");

                Cita cita = new Cita(doctor, paciente, consultorio, p[4], p[5], "Consulta");
                citas.add(cita);
            }
        } catch (IOException e) {
            System.out.println("Error al leer citas: " + e.getMessage());
        }
    }


    // -------- Guardar TODAS las citas --------
    private static void guardarArchivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Cita c : citas) {
                // Escribimos exactamente los 6 campos que el cargador espera
                bw.write(
                        c.getPaciente().getUsername() + "," +
                                c.getDoctor().getUsername() + "," +
                                c.getDoctor().getEspecialidad() + "," +
                                c.getConsultorio().getConsultorio() + "," +
                                c.getFecha() + "," +
                                c.getHora()
                );
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar citas: " + e.getMessage());
        }
    }
    public static List<Cita> getCitas() {
        return citas;
    }
    // -------- Agregar cita --------
    public static boolean agendarCita(Cita cita) {
        cargarCitas();              // trae lo que ya existe en el txt
        citas.add(cita);            // agrega la nueva
        guardarArchivo();           // reescribe TODO pero ya incluye lo anterior
        return true;
    }

    // -------- Cancelar cita --------
    public static boolean cancelarCita(Cita cita) {
        cargarCitas();
        // Usamos un iterador o un bucle tradicional para encontrar el índice
        int indiceAEliminar = -1;

        for (int i = 0; i < citas.size(); i++) {
            Cita c = citas.get(i);
            // Comparamos por los identificadores únicos: Cédula Paciente, Fecha y Hora
            if (c.getPaciente().getUsername().equals(cita.getPaciente().getUsername()) &&
                    c.getFecha().equals(cita.getFecha()) &&
                    c.getHora().equals(cita.getHora())) {
                indiceAEliminar = i;
                break;
            }
        }
        if (indiceAEliminar != -1) {
            citas.remove(indiceAEliminar); // Eliminamos de la RAM
            guardarArchivo();             // Sincronizamos con el TXT
            return true;
        }
        return false;
    }

    // -------- Citas por paciente --------
    public static List<Cita> citasDePaciente(Paciente paciente) {
        List<Cita> res = new ArrayList<>();
        for (Cita c : citas) {

            if (c.getPaciente().getUsername().equals(paciente.getUsername())) {
                res.add(c);
            }
        }
        return res;
    }


    // -------- Ver disponibilidad --------
    public static boolean tieneCitasAgendadas(Doctor d, Consultorio c, Horario h) {
        // Si no hay citas cargadas, obviamente no hay nada que bloquee
        if (citas == null || citas.isEmpty()) return false;

        for (Cita cita : citas) {
            // 1. ¿Es el mismo doctor? (Comparamos cédulas)
            boolean mismoDoc = cita.getDoctor().getUsername().equals(d.getUsername());

            // 2. ¿Es el mismo día?
            boolean mismoDia = cita.getFecha().equalsIgnoreCase(h.getDia());

            // 3. ¿La hora de la cita está dentro del rango del horario?
            int horaCita = Integer.parseInt(cita.getHora());
            boolean enRango = (horaCita >= h.getHoraInicio() && horaCita < h.getHoraFin());

            // Si se cumplen las 3, hay un conflicto
            if (mismoDoc && mismoDia && enRango) {
                return true;
            }
        }
        return false;
    }

    public static boolean estaLaHoraOcupada(String cedulaDoc, String dia, String horaExacta) {
        // Si no hay citas, la hora está libre
        if (citas == null || citas.isEmpty()) return false;

        for (Cita cita : citas) {
            // Comparamos los 3 pilares: Doctor, Día y Hora
            if (cita.getDoctor().getUsername().equals(cedulaDoc) &&
                    cita.getFecha().equalsIgnoreCase(dia) &&
                    cita.getHora().equals(horaExacta)) {
                return true; // La hora ya tiene un paciente
            }
        }
        return false; // La hora está disponible
    }
}
