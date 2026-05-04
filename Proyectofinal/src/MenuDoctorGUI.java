import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuDoctorGUI extends JPanel {
private final GUI gui;
private CardLayout cardLayout;
private JPanel contenedor;
  private Doctor doctor;
  private List<Consultorio> globales;
  private String ced;
  private String pass;
  public MenuDoctorGUI(CardLayout cardLayout, JPanel contenedor, GUI gui) {
    this.gui = gui;
    this.cardLayout = cardLayout;
    this.contenedor = contenedor;

    setLayout(null);
    setBackground(new Color(135, 205, 235));

    JLabel titulo1 = new JLabel("Bienvenido Doctor");
    titulo1.setFont(new Font("Arial", Font.ITALIC, 18));
    titulo1.setForeground(Color.black);
    titulo1.setBounds(150, 100, 200, 30);
    add(titulo1);

    JButton boton1 = new JButton("Editar Perfil");
    boton1.setBounds(150, 150, 200, 30);
    add(boton1);

    JButton boton2 = new JButton("Consultorios");
    boton2.setBounds(150, 200, 200, 30);
    add(boton2);

    JButton boton3 = new JButton("Configurar horario");
    boton3.setBounds(150, 250, 200, 30);
    add(boton3);

    JButton boton4 = new JButton("Ver agenda");
    boton4.setBounds(150, 300, 200, 30);
    add(boton4);

    JButton boton5 = new JButton("Cerrar Sesion");
    boton5.setBounds(150, 350, 200, 30);
    add(boton5);

    boton1.addActionListener(e -> {
      gui.irEditarPerfilDoctor(gui.getDoctorLogueado(), gui.getCedLogueada(), gui.getPassLogueada());

    });

    boton2.addActionListener(e -> {
      List<Consultorio> globalesCargados = ConsultorioService.cargarConsultorios();
     cardLayout.show(contenedor, "Consultorios");
      });


    boton3.addActionListener(e -> {
      gui.irSeleccionarConsultorioHorario(
        gui.getDoctorLogueado(),gui.getCedLogueada(),gui.getPassLogueada()
      );

    });
    boton4.addActionListener(e -> {
      gui.irVerAgendaDoctor(
              gui.getDoctorLogueado()
      );
    });
    // Cerrar sesión: vuelve al menú principal
    boton5.addActionListener(e ->{
            cardLayout.show(contenedor, "Menu");
            }

    );

  }


}


