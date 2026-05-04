import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Arrays;

public class Cita {

    private Doctor doctor;
    private Paciente paciente;
    private Consultorio consultorio;
    private static final String FILE_PATH = "data/citas.txt";

    private String fecha;   // Ej: "2025-04-10"
    private String hora;    // Ej: "14:30"
    private String motivo;

    private String estado; // "Programada", "Atendida", "Cancelada"

    public Cita(Doctor doctor, Paciente paciente, Consultorio consultorio,
                String fecha, String hora, String motivo) {

        validarFecha(fecha);
        validarHora(hora);
        this.doctor = doctor;
        this.paciente = paciente;
        this.consultorio = consultorio;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = "Programada";
    }
    //---------VALIDACIONES--------
    private void validarFecha(String fecha) {
        // Lista de días válidos
        List<String> dias = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");

        // Si es un día de la semana, es válido
        if (dias.contains(fecha)) {
            return;
        }

        // Si no es un día, intentamos validar como fecha real (por si reagendas con fecha exacta)
        try {
            LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Fecha o Día inválido (Use 'Lunes' o 'YYYY-MM-DD')");
        }
    }
    private void validarHora(String hora) {
        try {
            // Intentamos ver si es un número simple (8, 14, etc.)
            int h = Integer.parseInt(hora);
            if (h < 0 || h > 23) throw new IllegalArgumentException("Hora fuera de rango");
        } catch (NumberFormatException e) {
            // Si no es número, intentamos el parse estándar de LocalTime
            try {
                LocalTime.parse(hora);
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("Hora inválida (Use '14' o '14:30')");
            }
        }
    }
    // ------- GETTERS -------
    public Doctor getDoctor() { return doctor; }
    public Paciente getPaciente() { return paciente; }
    public Consultorio getConsultorio() { return consultorio; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getMotivo() { return motivo; }
    public String getEstado() { return estado; }

    // ------- SETTERS -------
//    public void setFecha(String fecha) { this.fecha = fecha; }
//    public void setHora(String hora) { this.hora = hora; }
//    public void setEstado(String estado) { this.estado = estado; }

    //Mostrar Cita
//    public void mostrarCita() {
//        System.out.println("----- CITA -----");
//        System.out.println("Doctor: " + doctor.getName() + " " + doctor.getLastname() +
//                " (" + doctor.getEspecialidad() + ")");
//        System.out.println("Paciente: " + paciente.getName() + " " + paciente.getLastname());
//        System.out.println("Consultorio: " + consultorio.getConsultorio());
//        System.out.println("Fecha: " + fecha);
//        System.out.println("Hora: " + hora);
//        System.out.println("Motivo: " + motivo);
//        System.out.println("Estado: " + estado);
//    }
//
//    public void mostrarCitaDetallada(String nombreDoctorCompleto) {
//        System.out.println("Día: " + fecha + " | Hora: " + hora + ":00");
//        System.out.println("Doctor: " + nombreDoctorCompleto);
//        System.out.println("Lugar: " + consultorio.getConsultorio());
//    }
}
