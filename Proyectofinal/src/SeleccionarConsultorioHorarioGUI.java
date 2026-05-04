import javax.swing.*;
import java.awt.*;

public class SeleccionarConsultorioHorarioGUI extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel contenedor;
    private final Doctor doctor;
    private final String ced;
    private final String pass;

    private final DefaultListModel<Consultorio> model = new DefaultListModel<>();
    private final JList<Consultorio> lista = new JList<>(model);

    public SeleccionarConsultorioHorarioGUI(CardLayout cardLayout, JPanel contenedor,
                                            Doctor doctor, String ced, String pass) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.doctor = doctor;
        this.ced = ced;
        this.pass = pass;
        setLayout(new BorderLayout());
        setBackground(new Color(135, 205, 235));

        JLabel titulo = new JLabel("Seleccione consultorio", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(lista), BorderLayout.CENTER);

        JButton btnAbrir = new JButton("seleccionar");
        JButton btnVolver = new JButton("Volver");
        JPanel acciones = new JPanel(new FlowLayout());
        acciones.add(btnAbrir);
        acciones.add(btnVolver);
        add(acciones, BorderLayout.SOUTH);

        btnVolver.addActionListener(e -> cardLayout.show(contenedor, "MenuDoctor"));

        btnAbrir.addActionListener(e -> abrirConsultorio());

        cargarConsultorios();

    }

    private void cargarConsultorios() {
        model.clear();
        if (doctor == null || doctor.getConsultoriosAsociados() == null) return;

        for (Consultorio c : doctor.getConsultoriosAsociados()) {
            model.addElement(c);
        }
    }

    private void abrirConsultorio() {
        Consultorio elegido = lista.getSelectedValue();
        if (elegido == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un consultorio.");
            return;
        }

        String key = "Horarios_" + elegido.getConsultorio().toLowerCase();

        contenedor.add(
                new ConfigurarHorarioGUI(cardLayout, contenedor, doctor, elegido, ced, pass),
                key
        );
        cardLayout.show(contenedor, key);
        contenedor.revalidate();
        contenedor.repaint();
    }



}



