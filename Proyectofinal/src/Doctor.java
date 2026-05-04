import java.util.List;
import java.util.ArrayList;

public class Doctor extends Usuario{
    private String especialidad;
    private String correo;
    private String numContacto;

    // Esta lista es vital para que el paciente vea dónde atiende el doctor
    private List<Consultorio> consultoriosAsociados;

    public Doctor(String nombre, String apellido, String edad, String cedula, String password, String correo, String especialidad, String numContacto) {
        // 'super' envía los datos a la clase Usuario.
        // Usamos el correo como username y definimos el rol fijo como "doctor"
        super(nombre, apellido, edad, cedula, password, "doctor");

        this.correo = correo;
        this.especialidad = especialidad;
        this.numContacto = numContacto;
        this.consultoriosAsociados = new ArrayList<>();
    }

    // --- GETTERS Y SETTERS ---
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumContacto() {
        return numContacto;
    }

    public void setNumContacto(String numContacto) {
        this.numContacto = numContacto;
    }

    public List<Consultorio> getConsultoriosAsociados() {
        return consultoriosAsociados;
    }

//    public void setConsultoriosAsociados(List<Consultorio> consultoriosAsociados) {
//        this.consultoriosAsociados = consultoriosAsociados;
//    }
}

