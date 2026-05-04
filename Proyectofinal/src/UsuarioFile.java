import java.io.*;
import java.util.*;

public class UsuarioFile {

    public static List<String[]> leer(String nombre) {
        List<String[]> usuarios = new ArrayList<>(); //Lista donde guarda cada usuario

        try (BufferedReader br = new BufferedReader(new FileReader(nombre))) { //Abre y cierra el archivo autommaticamente
            String linea;
            while ((linea = br.readLine()) != null) { //Lee el archivo línea por línea
                usuarios.add(linea.split(",",4)); //Separa la línea por comas
            }
        }catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return usuarios;
    }

    //Metodo para añadir un usuario al archivo
    public static void escribir(String nombre, String linea) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombre, true))) { //Escribe en el archivo sin borrar nada
            bw.write(linea);
            bw.newLine();
        }catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static boolean usuarioExistente(String username, List<String[]> usuarios){
        for(String[] u : usuarios){
            if(u[0].equals(username)){
                return false;
            }
        }
        return true;
    }

    public static void actualizarUsuario(String cedula, String nuevaLinea) {
        List<String[]> usuarios = leer("data/usuarios.txt");


        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/usuarios.txt"))) {
            for (String[] u : usuarios) {

                if (u[0].equals(cedula)) {
                    bw.write(nuevaLinea); // Escribimos los nuevos datos
                } else {
                    // Volvemos a escribir los datos de los demás usuarios sin cambios
                    bw.write(String.join(",",u));
                }
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar el archivo: " + e.getMessage());
        }
    }


}
