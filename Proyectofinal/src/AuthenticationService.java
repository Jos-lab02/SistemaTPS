import java.util.List;

public class AuthenticationService {
    private static final String FILENAME = "data/usuarios.txt";

    // Metodos de validacion de formato
    public boolean soloLetras(String texto) {
        // Valida letras, espacios y tildes
        return texto != null && texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+");
    }

    public boolean soloNumeros(String texto) {
        // Valida que solo sean dígitos
        return texto != null && texto.matches("\\d+");
    }

    public boolean digitosTelefono(String texto){
        //Valida que el número de telefono sea mayor a 7 y menor a 15 digitos
        return texto.matches("^\\+?[0-9]{7,15}$");
    }

    public boolean esCorreoValido(String correo) {
        // Valida formato básico: texto@texto.com
            return correo != null && correo.matches("^[\\w.-]+@[a-z\\d.-]+\\.[a-z]{2,}$");
    }

    // Variable para guardar los datos del usuario que acaba de entrar
    private String[] datosUsuarioLogueado;

    // Verifica si una cédula ya está registrada en el sistema.
    public boolean existeUsuario(String username) {
        List<String[]> usuarios = UsuarioFile.leer(FILENAME);
        // UsuarioFile.usuarioExistente devuelve FALSE si encuentra al usuario
        return !UsuarioFile.usuarioExistente(username, usuarios);
    }

    // Iniciar sesion
    public Boolean login(String username, String password) {
        List<String[]> usuarios = UsuarioFile.leer(FILENAME);

        for (String[] u : usuarios) {
            // u[0]: Cedula, u[1]: Password, u[2]: Rol, u[3]: DatosExtra (Nombres;Apellidos;etc)
            if (u[0].equals(username)) {
                if (u[1].equals(password)) {
                    this.datosUsuarioLogueado = u;

                    return true; // Retorna el rol
                } else {

                    return false   ;
                }
            }
        }
        return false;
    }


    // Crea una cuenta nueva validando que no exista el usuario previamente.
    public boolean createAccount(String username, String password, String rol, String datosExtra) {
        List<String[]> usuarios = UsuarioFile.leer(FILENAME);

        // Si el usuario ya existe, no permite duplicados
        if (!UsuarioFile.usuarioExistente(username, usuarios)) {

            return false;
        }

        String nuevaLinea = username + "," + password + "," + rol + "," + datosExtra;
        UsuarioFile.escribir(FILENAME, nuevaLinea);
        return true;
    }

    public String[] getDatosLogueado() {
        return datosUsuarioLogueado;
    }
}
