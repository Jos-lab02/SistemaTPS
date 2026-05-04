import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VerMisCitasGUI extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contenedor;
    private final Paciente paciente;

    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> lista = new JList<>(model);

    public VerMisCitasGUI(CardLayout cardLayout, JPanel contenedor, Paciente paciente) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.paciente = paciente;

        setLayout(new BorderLayout());
        setBackground(new Color(141, 204, 238));

        // -------- TÍTULO --------
        JLabel titulo = new JLabel("Mis Citas Agendadas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);

        // -------- LISTA --------
        lista.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(lista);
        add(scroll, BorderLayout.CENTER);

        // -------- BOTÓN VOLVER --------
        JButton btnVolver = new JButton("Volver");
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBackground(getBackground());
        panelBoton.add(btnVolver);
        add(panelBoton, BorderLayout.SOUTH);

        btnVolver.addActionListener(e ->
                cardLayout.show(contenedor, "MenuPacientes")
        );

        // Carga inicial
        cargarCitas();
    }

    // ---------------- CARGAR CITAS ----------------
    private void cargarCitas() {
        model.clear();

        Agenda.cargarCitas();
        List<Cita> citasPaciente = Agenda.citasDePaciente(paciente);
        List<Doctor> doctores = DoctorService.obtenerDoctoresRegistrados();

        if (citasPaciente.isEmpty()) {
            model.addElement("No tiene citas registradas.");
            return;
        }

        for (Cita c : citasPaciente) {

            Doctor docDeLaCita = null;

            for (Doctor d : doctores) {
                if (d.getUsername().equals(c.getDoctor().getUsername())) {
                    docDeLaCita = d;
                    break;
                }
            }

            String nombreDoc = (docDeLaCita != null)
                    ? (docDeLaCita.getName() + " " + docDeLaCita.getLastname())
                    : c.getDoctor().getUsername();

            String texto =
                    "Doctor: Dr. " + nombreDoc + " " + "(" + c.getDoctor().getEspecialidad() + ")" +
                            " | Consultorio: " + c.getConsultorio().getConsultorio() +
                            " | Día: " + c.getFecha() +
                            " | Hora: " + c.getHora();

            model.addElement(texto);
        }
    }
}

