import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VerAgendaDoctorGUI extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contenedor;
    private final Doctor doctor;

    private final DefaultListModel<Cita> model = new DefaultListModel<>();
    private final JList<Cita> lista = new JList<>(model);

    public VerAgendaDoctorGUI(CardLayout cardLayout, JPanel contenedor, Doctor doctor) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.doctor = doctor;

        setLayout(new BorderLayout());
        setBackground(new Color(135, 205, 235));

        // -------- TÍTULO --------
        JLabel titulo = new JLabel("Agenda del Doctor", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);

        // -------- LISTA --------
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Arial", Font.PLAIN, 14));

        // Renderer para que NO salga Cita@1234
        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                JLabel lb = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                if (value == null) {
                    lb.setText("No hay citas en su agenda.");
                    lb.setForeground(Color.DARK_GRAY);
                    return lb;
                }

                Cita c = (Cita) value;

                String texto =
                        c.getFecha() + "  |  " +
                                c.getHora() + "h  |  " +
                                c.getConsultorio().getConsultorio() + "  |  " +
                                "Paciente ID: " + c.getPaciente().getUsername();


                lb.setText(texto);
                return lb;
            }
        });

        add(new JScrollPane(lista), BorderLayout.CENTER);

        // -------- BOTÓN VOLVER --------
        JButton btnVolver = new JButton("Volver");
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.setBackground(getBackground());
        acciones.add(btnVolver);
        add(acciones, BorderLayout.SOUTH);

        btnVolver.addActionListener(e ->
                cardLayout.show(contenedor, "MenuDoctor")
        );

        // Carga inicial
        cargarAgenda();
    }

    private void cargarAgenda() {
        model.clear();

        if (doctor == null) {
            JOptionPane.showMessageDialog(this, "No hay doctor logueado.");
            return;
        }

        // Cargar desde archivo
        Agenda.cargarCitas();

        List<Cita> todas = Agenda.getCitas();
        boolean hay = false;

        for (Cita c : todas) {
            if (c.getDoctor().getUsername().equals(doctor.getUsername())) {
                model.addElement(c);
                hay = true;
            }
        }

        if (!hay) {
            model.addElement(null);
        }

        lista.revalidate();
        lista.repaint();
    }
}
