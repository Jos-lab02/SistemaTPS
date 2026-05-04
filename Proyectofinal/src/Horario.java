public class Horario {
    private String dia; // Ejemplo: "Lunes"
    private int horaInicio; // Ejemplo: 8
    private int horaFin; // Ejemplo: 12

    public Horario(String dia, int horaInicio, int horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    // Getters
    public String getDia() {
        return dia; }
    public int getHoraInicio() {
        return horaInicio; }
    public int getHoraFin() {
        return horaFin; }

    @Override
    public String toString() {
        return dia + ": " + horaInicio + ":00 - " + horaFin + ":00";
    }
}