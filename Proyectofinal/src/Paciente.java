import java.util.Scanner;

public class Paciente extends Usuario {
    private String genero;
    private String numContacto;

    // Constructor actualizado con 7 parámetros
    public Paciente(String name, String lastname, String age, String cedula, String password, String genero, String numContacto) {

        super(name, lastname, age, cedula, password, "paciente");
        this.genero = genero;
        this.numContacto = numContacto;
    }

    public Paciente() {
        // Llama al constructor vacío de Usuario
        super("", "", "", "", "", "paciente");
    }

    // --- GETTERS Y SETTERS ---
    public String getGenero() {
        return genero;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getNumContacto() {
        return numContacto;
    }

    public void setNumContacto(String numContacto) {
        this.numContacto = numContacto;
    }

    // ------- MÉTODOS DE INTERFAZ -------

//    public static void menuPaciente() {
//        System.out.println("\n--- MENÚ PRINCIPAL DEL PACIENTE ---");
//        System.out.println("1. Solicitar cita");
//        System.out.println("2. Ver mis citas");
//        System.out.println("3. Cancelar cita");
//        System.out.println("4. Editar perfil");
//        System.out.println("5. Cerrar sesión");
//    }

//    public void mostrarPaciente() {
//        System.out.println("1. Nombres: " + getName());
//        System.out.println("2. Apellidos: " + getLastname());
//        System.out.println("3. Edad: " + getAge());
//        System.out.println("4. Género: " + getGenero());
//        System.out.println("5. Número de contacto: " + getNumContacto());
//        // Añadimos la cédula para que el paciente la vea
//        System.out.println("6. Cédula (Usuario): " + getUsername());
//    }
}
