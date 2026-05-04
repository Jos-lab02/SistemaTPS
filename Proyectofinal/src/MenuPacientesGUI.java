import javax.swing.*;
import java.awt.*;
public class MenuPacientesGUI extends JPanel {
    private CardLayout cardLayout;
    private JPanel contenedor;
    private GUI gui;

    public MenuPacientesGUI(GUI gui ,CardLayout cardLayout, JPanel contenedor) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.gui = gui;


        setLayout(null);
        setBackground(new Color(135, 205, 235));


        JLabel titulo1= new JLabel("Bienvenido a SATM");
        titulo1.setFont(new Font("Arial", Font.ITALIC, 18));
        titulo1.setForeground(Color.black);
        titulo1.setBounds(150 ,90, 200, 30);
        add(titulo1);

        JButton boton1= new JButton("Solicitar Cita");
        boton1.setBounds(130,150,200,30);
        add(boton1);


        JButton boton2= new JButton("Ver mis  Citas");
        boton2.setBounds(130,200,200,30);
        add(boton2);

        JButton boton3= new JButton("Cancelar cita");
        boton3.setBounds(130,250,200,30);
        add(boton3);

        JButton boton4= new JButton("Editar Perfil");
        boton4.setBounds(130,300,200,30);
        add(boton4);

        JButton boton5= new JButton("Salir");
        boton5.setBounds(130,350,200,30);
        add(boton5);

        boton1.addActionListener(e -> {
            for (Component comp : contenedor.getComponents()) {
                if (comp instanceof SolicitarCitasGUI sc) {
                    sc.cargarEspecialidades(); // recarga las especialidades
                    break;
                }
            }


            gui.irSolicitarCitas();

        });
        boton2.addActionListener(e -> {
            gui.irVerMisCitas();

        });
        boton3.addActionListener(e -> {
            gui.irCancelarCitas();

        });
        boton4.addActionListener(e -> {
            gui.irEditarPerfilPaciente(
                    gui.getPacienteLogueado(), gui.getCedLogueada(), gui.getPassLogueada());

        });
        boton5.addActionListener(e -> {
            cardLayout.show(contenedor,"Menu");
        });



    }
}