import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CancelarCitasGUI extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contenedor;
    private final Paciente paciente;

    private final DefaultListModel<Cita> model = new DefaultListModel<>();
    private final JList<Cita> lista = new JList<>(model);

    public CancelarCitasGUI(CardLayout cardLayout, JPanel contenedor, Paciente paciente) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.paciente = paciente;

        setLayout(new BorderLayout());
        setBackground(new Color(141, 204, 238));

        // --------- TÍTULO ----------
        JLabel titulo = new JLabel("Cancelar citas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);

        // --------- LISTA ----------
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Arial", Font.PLAIN, 14));


        lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {

                JLabel lb = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value == null) {
                    lb.setText("No tienes citas agendadas.");
                    lb.setForeground(Color.DARK_GRAY);
                    return lb;
                }

                Cita c = (Cita) value;

                String texto = c.getFecha() + "  |  " + c.getHora()
                        + "  |  " + c.getConsultorio().getConsultorio()
                        + "  |  Dr. " + c.getDoctor().getUsername();

                lb.setText(texto);
                return lb;
            }
        });

        add(new JScrollPane(lista), BorderLayout.CENTER);

        // --------- BOTONES ----------
        JButton btnCancelar = new JButton("Cancelar seleccionada");
        JButton btnVolver = new JButton("Volver");

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.setBackground(getBackground());

        acciones.add(btnCancelar);
        acciones.add(btnVolver);

        add(acciones, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> cancelarSeleccionada());
        btnVolver.addActionListener(e -> cardLayout.show(contenedor, "MenuPacientes"));


        cargarMisCitas();
    }

    private void cargarMisCitas() {
        model.clear();

        if (paciente == null) {
            JOptionPane.showMessageDialog(this, "No hay paciente logueado.");
            return;
        }

        Agenda.cargarCitas();

        List<Cita> mias = Agenda.citasDePaciente(paciente);
        if (mias == null || mias.isEmpty()) {
            model.addElement(null);
            lista.revalidate();
            lista.repaint();
            return;
        }

        for (Cita c : mias) {
            model.addElement(c);
        }

        lista.revalidate();
        lista.repaint();
    }

    private void cancelarSeleccionada() {
        Cita seleccion = lista.getSelectedValue();

        if (seleccion == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita.");
            return;
        }

        int r = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea cancelar esta cita?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (r != JOptionPane.YES_OPTION) return;

        Agenda.cargarCitas();

        boolean ok = Agenda.cancelarCita(seleccion);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "No se pudo cancelar (no encontrada).");
            return;
        }

        JOptionPane.showMessageDialog(this, "Cita cancelada con éxito.");
        cargarMisCitas();
    }
}
