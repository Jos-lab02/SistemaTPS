import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultorioGUI extends JPanel {
    private final GUI gui;
    private final CardLayout cardLayout;
    private final JPanel contenedor;

    public ConsultorioGUI(GUI gui , CardLayout cardLayout,JPanel contenedor) {
        this.gui = gui;
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;


        setLayout(null);
        setBackground(new Color(135, 205, 235));

        JLabel titulo1= new JLabel("Consultorios");
        titulo1.setFont(new Font("Arial",Font.BOLD,20));
        titulo1.setBounds(150,100,200,30);
        add(titulo1);

        JButton btn1= new JButton("Asignar consultorio");
        btn1.setBounds(150,150,200,30);
        add(btn1);

        JButton btn2= new JButton("Quitar consultorio");
        btn2.setBounds(150,250,200,30);
        add(btn2);

        JButton btn3= new JButton("Volver");
        btn3.setBounds(150,300,200,30);
        add(btn3);

        btn1.addActionListener(e -> {

            List<Consultorio> globalesCargados = ConsultorioService.cargarConsultorios();
            gui.irAsociarConsultorio(gui.getDoctorLogueado(), globalesCargados,
                    gui.getCedLogueada(), gui.getPassLogueada());

        });
        btn2.addActionListener(e -> {
            gui.irQuitarConsultorio(
                    gui.getDoctorLogueado(),
                    gui.getCedLogueada(),
                    gui.getPassLogueada()
            );

        });
        btn3.addActionListener(e -> {
            cardLayout.show(contenedor,"MenuDoctor");

        });

    }

}
