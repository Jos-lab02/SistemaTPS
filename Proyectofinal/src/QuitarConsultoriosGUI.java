import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuitarConsultoriosGUI extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contenedor;

    private final DefaultListModel<Consultorio> model = new DefaultListModel<>();
    private final JList<Consultorio> lista = new JList<>(model);

    private final Doctor doctor;
    private final String ced;
    private final String pass;

    public QuitarConsultoriosGUI(CardLayout cardLayout, JPanel contenedor,
                                Doctor doctor, String ced, String pass) {

        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.doctor = doctor;
        this.ced = ced;
        this.pass = pass;

        setLayout(new BorderLayout());
        setBackground(new Color(135, 205, 235));

        JLabel titulo = new JLabel("Consultorios asociados", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        add(titulo, BorderLayout.NORTH);

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scroll = new JScrollPane(lista);
        add(scroll, BorderLayout.CENTER);

        JButton btnQuitar = new JButton("Quitar consultorio");
        JButton btnVolver = new JButton("Volver");

        JPanel acciones = new JPanel(new FlowLayout());
        acciones.add(btnQuitar);
        acciones.add(btnVolver);
        add(acciones, BorderLayout.SOUTH);

        btnVolver.addActionListener(e ->
                cardLayout.show(contenedor, "MenuDoctor")
        );

        btnQuitar.addActionListener(e -> quitarSeleccionado());

        cargarAsociados();
    }

    private void cargarAsociados() {
        model.clear();

        if (doctor == null || doctor.getConsultoriosAsociados() == null) {
            return;
        }

        for (Consultorio c : doctor.getConsultoriosAsociados()) {
            model.addElement(c);
        }

        if (model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tiene consultorios asociados.");
        }
    }

    private void quitarSeleccionado() {
        Consultorio elegido = lista.getSelectedValue();

        if (elegido == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un consultorio.");
            return;
        }

        doctor.getConsultoriosAsociados().removeIf(c ->
                c.getConsultorio().equalsIgnoreCase(elegido.getConsultorio())
        );


        UsuarioFile.actualizarUsuario(ced, construirLineaActualizada());

        JOptionPane.showMessageDialog(this,
                "Consultorio eliminado: " + elegido.getConsultorio());

        cargarAsociados();
    }


    private String construirLineaActualizada() {

        List<String[]> usuarios = UsuarioFile.leer("data/usuarios.txt");

        for (String[] fila : usuarios) {
            if (fila.length >= 4 && fila[0].equals(ced)) {

                String[] datos = fila[3].split(";", -1);

                if (datos.length < 7) {
                    datos = java.util.Arrays.copyOf(datos, 7);
                }

                // serializamos consultorios restantes
                StringBuilder sb = new StringBuilder();
                for (Consultorio c : doctor.getConsultoriosAsociados()) {
                    if (sb.length() > 0) sb.append("|");
                    sb.append(c.getConsultorio()).append(":");
                }

                datos[6] = sb.toString();

                return fila[0] + "," + fila[1] + "," + fila[2] + "," +
                        String.join(";", datos);
            }
        }
        return "";
    }
}

