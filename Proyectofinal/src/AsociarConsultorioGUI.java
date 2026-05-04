import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AsociarConsultorioGUI extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contenedor;

    private final DefaultListModel<Consultorio> model = new DefaultListModel<>();
    private final JList<Consultorio> lista = new JList<>(model);

    private final Doctor doctor;
    private final List<Consultorio> globales;

    private final String ced;
    private final String pass;

    public AsociarConsultorioGUI(CardLayout cardLayout, JPanel contenedor,
                                 Doctor doctor, List<Consultorio> globales,
                                 String ced, String pass) {

        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.doctor = doctor;
        this.globales = globales;
        this.ced = ced;
        this.pass = pass;




        setLayout(new BorderLayout());
        setBackground(new Color(135, 205, 235));


        JLabel titulo = new JLabel("Consultorios disponibles", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        add(titulo, BorderLayout.NORTH);

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Arial", Font.PLAIN, 16));
        lista.setVisibleRowCount(8);
        lista.setForeground(Color.BLACK);
        lista.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(lista);
        add(scroll, BorderLayout.CENTER);

        JButton btnAsociar = new JButton("Asociar seleccionado");
        JButton btnVolver = new JButton("Volver");

        JPanel acciones = new JPanel(new FlowLayout());
        acciones.add(btnAsociar);
        acciones.add(btnVolver);
        add(acciones, BorderLayout.SOUTH);

        btnVolver.addActionListener(e -> {
            cardLayout.show(contenedor, "MenuDoctor");
        });

        btnAsociar.addActionListener(e ->{ asociarSeleccionado();});

        cargarDisponibles();
    }

    private void cargarDisponibles() {
        model.clear();

        java.util.Set<String> vistos = new java.util.HashSet<>();

        for (Consultorio c : globales) {

            String clave = (c.getConsultorio() == null ? "" : c.getConsultorio().trim().toLowerCase());

            if (!vistos.add(clave)) {
                continue;
            }
            if (!yaEstaAsociado(c)) {
                model.addElement(c);
            }
        }
        if (model.isEmpty()) {
             JOptionPane.showMessageDialog(this, "No hay consultorios disponibles.");
        }
        lista.revalidate();
        lista.repaint();

    }

    private boolean yaEstaAsociado(Consultorio elegido) {
        if (doctor == null) {

            return false;
        }
        if (doctor.getConsultoriosAsociados() == null) {

            return false;
        }

        for (Consultorio asoc : doctor.getConsultoriosAsociados()) {
            if (asoc.getConsultorio().equalsIgnoreCase(elegido.getConsultorio())) {
                return true;
            }
        }
        return false;
    }

    private void asociarSeleccionado() {
        Consultorio elegido = lista.getSelectedValue();
        if (elegido == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un consultorio.");
            return;
        }

        if (yaEstaAsociado(elegido)) {
            JOptionPane.showMessageDialog(this, "Ya está asociado a ese consultorio.");
            return;
        }

        doctor.getConsultoriosAsociados().add(elegido);

        actualizarArchivoDoctor(ced, pass, doctor);

        JOptionPane.showMessageDialog(this, "Ahora está asociado a: " + elegido.getConsultorio());


        cargarDisponibles();
    }

    private void actualizarArchivoDoctor(String ced, String pass, Doctor d) {
        String consultoriosCampo = serializarConsultoriosConDosPuntos(d.getConsultoriosAsociados());
        String nuevaLinea = construirLineaDoctorActualizada(ced, consultoriosCampo);

        UsuarioFile.actualizarUsuario(ced, nuevaLinea);
    }
    private String serializarConsultoriosConDosPuntos(List<Consultorio> consultorios) {
        if (consultorios == null || consultorios.isEmpty()) return "Ninguno";

        java.util.Set<String> vistos = new java.util.LinkedHashSet<>();

        for (Consultorio c : consultorios) {
            if (c == null || c.getConsultorio() == null) continue;
            String nombre = c.getConsultorio().trim();
            if (!nombre.isEmpty()) {
                vistos.add(nombre + ":");
            }
        }
        return String.join("|", vistos);
    }

    private String construirLineaDoctorActualizada(String cedula, String consultoriosCampo) {
        // Leemos todos los usuarios para encontrar la línea original del doctor
        List<String[]> usuarios = UsuarioFile.leer("data/usuarios.txt");

        for (String[] fila : usuarios) {
            if (fila.length >= 4 && fila[0].equals(cedula) && fila[2].equalsIgnoreCase("doctor")) {

                String ced = fila[0];
                String pass = fila[1];
                String rol = fila[2];

                // fila[3] tiene: "nombre;apellido;edad;email;especialidad;telefono;...."
                String[] datos = fila[3].split(";", -1);

                // aseguramos mínimo 7 campos (0..6)
                if (datos.length < 7) {
                    datos = java.util.Arrays.copyOf(datos, 7);
                    for (int i = 0; i < datos.length; i++) {
                        if (datos[i] == null) datos[i] = "";
                    }
                }


                datos[6] = consultoriosCampo;

                return ced + "," + pass + "," + rol + "," + String.join(";", datos);
            }
        }

        // Si no se encuentra, devolvemos algo seguro (pero idealmente no debería pasar)
        return cedula + ",,,";
    }


}
