import java.io.File;
import java.util.*;
public class Main {
    static void main(String[] args) {

        GUI gui = new GUI();
        gui.setVisible(true);
        System.out.println(new File("data/usuarios.txt").getAbsolutePath());


    }

}
