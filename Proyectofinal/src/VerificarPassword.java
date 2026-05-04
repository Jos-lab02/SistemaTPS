public class VerificarPassword{

    public static boolean validarPassword(String password){
        if(password.length() >= 8 && password.matches(".*\\d.*")){ //Valida que password tenga 8 caracteres y un número
            return true;
        }else{
            return false;
        }
    }

}
