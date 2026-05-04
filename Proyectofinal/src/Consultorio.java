import java.util.ArrayList;
import java.util.List;
public class Consultorio {
    private String nombre;       // Nombre del consultorio
    private String direccion;    // Dirección del consultorio

    private List<Horario> horarios;

    public Consultorio(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.horarios = new ArrayList<>();
    }

    // Getter del nombre del consultorio
    public String getConsultorio() {
        return nombre;
    }

    // Setter del nombre del consultorio
    public void setConsultorio(String nombre) {
        this.nombre = nombre;
    }

    // Getter de la dirección del consultorio
//    public String getDireccionHos() {
//        return direccion;
//    }
//
//    // Setter de la dirección del consultorio
//    public void setDireccionHos(String direccion) {
//        this.direccion = direccion;
//    }
//    public void setHorarios(List<Horario> horarios) {
//        this.horarios = horarios; }

    public List<Horario> getHorarios() {

        return horarios; }

    @Override
    public String toString() {
        return getConsultorio();
    }


}
