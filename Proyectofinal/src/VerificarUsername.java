import java.util.List;
import java.util.*;

public class VerificarUsername {
    public static boolean validarUsername(String username){ //Valida que el username tenga 10 caracteres
        if(username.length() == 10 && username.matches("\\d+")){
            // \d significa cualquier digito(0-9)
            // + significa una o mas veces
            // \d+ juntos garantiza que toda la cadena (username) este compuesta solo de digitos
            return true;
        }else{
            return false;
        }
    }

   //Si el username inicia con 99 le asigna el rol de doctor
    public static String validarRol(String username){
        if(username.startsWith("99")){
            return "doctor";
        }else{
            return "paciente";
        }
    }
}
