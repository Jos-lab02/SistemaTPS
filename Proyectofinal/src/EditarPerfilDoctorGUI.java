import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EditarPerfilDoctorGUI extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contenedor;

    private final Doctor doctor;
    private final String ced;
    private final String pass;

    // Campos editables
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtEdad;
    private JTextField txtCorreo;
    private JTextField txtEspecialidad;
    private JTextField txtTelefono;

    public EditarPerfilDoctorGUI(CardLayout cardLayout, JPanel contenedor,
                                 Doctor doctor, String ced, String pass) {

        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.doctor = doctor;
        this.ced = ced;
        this.pass = pass;

        setLayout(null);
        setBackground(new Color(135, 205, 235));

        JLabel titulo = new JLabel("Editar Perfil del Doctor");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBounds(130, 30, 300, 30);
        add(titulo);

        // --- Labels + TextFields ---
        int xLabel = 70;
        int xField = 200;
        int y = 90;
        int h = 25;
        int gap = 45;

        add(crearLabel("Nombre:", xLabel, y));
        txtNombre = crearField(xField, y);
        add(txtNombre);

        y += gap;
        add(crearLabel("Apellido:", xLabel, y));
        txtApellido = crearField(xField, y);
        add(txtApellido);

        y += gap;
        add(crearLabel("Edad:", xLabel, y));
        txtEdad = crearField(xField, y);
        add(txtEdad);

        y += gap;
        add(crearLabel("Correo:", xLabel, y));
        txtCorreo = crearField(xField, y);
        add(txtCorreo);

        y += gap;
        add(crearLabel("Especialidad:", xLabel, y));
        txtEspecialidad = crearField(xField, y);
        add(txtEspecialidad);

        y += gap;
        add(crearLabel("Teléfono:", xLabel, y));
        txtTelefono = crearField(xField, y);
        add(txtTelefono);

        // --- Botón Guardar ---
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(190, 390, 120, 35);
        add(btnGuardar);

        // Botón volver
        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(330, 390, 120, 35);
        add(btnVolver);

        btnVolver.addActionListener(e -> cardLayout.show(contenedor, "MenuDoctor"));

        btnGuardar.addActionListener(e -> guardarCambios());


        cargarDatosEnCampos();
    }

    private JLabel crearLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setBounds(x, y, 120, 25);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        return lbl;
    }

    private JTextField crearField(int x, int y) {
        JTextField t = new JTextField();
        t.setBounds(x, y, 220, 25);
        return t;
    }

    private void cargarDatosEnCampos() {
        if (doctor == null) return;

        txtNombre.setText(valorSeguro(doctor.getName()));
        txtApellido.setText(valorSeguro(doctor.getLastname()));
        txtEdad.setText(valorSeguro(doctor.getAge()));
        txtCorreo.setText(valorSeguro(doctor.getCorreo()));
        txtEspecialidad.setText(valorSeguro(doctor.getEspecialidad()));
        txtTelefono.setText(valorSeguro(doctor.getNumContacto()));
    }

    private String valorSeguro(String s) {
        return (s == null) ? "" : s;
    }

    private void guardarCambios() {
        if (doctor == null) {
            JOptionPane.showMessageDialog(this, "No hay doctor cargado.");
            return;
        }

        AuthenticationService authService = new AuthenticationService();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String edad = txtEdad.getText().trim();
        String Correo = txtCorreo.getText().trim();
        String especialidad = txtEspecialidad.getText().trim();
        String telefono = txtTelefono.getText().trim();


        if (nombre.isBlank() || apellido.isBlank() || edad.isBlank() ||
                Correo.isBlank() || especialidad.isBlank() || telefono.isBlank()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }

        if (!authService.soloLetras(nombre)) {
            JOptionPane.showMessageDialog(this, "El nombre solo debe contener letras.");
            return;
        }

        if (!authService.soloLetras(apellido)) {
            JOptionPane.showMessageDialog(this, "El apellido solo debe contener letras.");
            return;
        }

        if (edad.length() > 3) {
            JOptionPane.showMessageDialog(this, "Edad inválida. max 3 digitos");
            return;
        }
        int e = Integer.parseInt(edad);
        if (e <= 0 || e > 110) {
            JOptionPane.showMessageDialog(this, "Edad fuera de rango.");
            return;
        }

        if (!edad.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Edad inválida (solo números).");
            return;
        }

        if (!authService.esCorreoValido(Correo)) {
            JOptionPane.showMessageDialog(this, "Correo inválido. Ej: ejemplo@mail.com");
            return;
        }

        if (!authService.soloLetras(especialidad)) {
            JOptionPane.showMessageDialog(this, "La especialidad solo debe contener letras.");
            return;
        }

        if (!authService.soloNumeros(telefono) || !authService.digitosTelefono(telefono)){
            JOptionPane.showMessageDialog(this, "Teléfono inválido (solo números, mínimo 7 dígitos y máximo 15).");
            return;
        }

        doctor.setName(nombre);
        doctor.setLastname(apellido);
        doctor.setAge(edad);
        doctor.setCorreo(Correo);
        doctor.setEspecialidad(especialidad);
        doctor.setNumContacto(telefono);

        actualizarArchivoDoctor(ced, pass, doctor);

        JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
        cardLayout.show(contenedor, "MenuDoctor");
    }

    // Formato objetivo:
    // ced,pass,doctor,nombre;apellido;edad;correo;especialidad;telefono;ConsultorioA:Lunes-8-14|ConsultorioB:
    private void actualizarArchivoDoctor(String ced, String pass, Doctor d) {

        StringBuilder sb = new StringBuilder();

        List<Consultorio> consultorios = d.getConsultoriosAsociados();
        if (consultorios != null) {
            for (Consultorio c : consultorios) {
                sb.append(c.getConsultorio());

                // horarios
                if (c.getHorarios() != null && !c.getHorarios().isEmpty()) {
                    sb.append(":");
                    for (int i = 0; i < c.getHorarios().size(); i++) {
                        Horario h = c.getHorarios().get(i);
                        sb.append(h.getDia())
                                .append("-")
                                .append(h.getHoraInicio())
                                .append("-")
                                .append(h.getHoraFin());

                        if (i < c.getHorarios().size() - 1) sb.append("/");
                    }
                } else {
                    // deja "Consultorio:" si no hay horarios (igual que vienes usando)
                    sb.append(":");
                }
                sb.append("|");
            }
        }

        String consStr = (sb.length() > 0) ? sb.substring(0, sb.length() - 1) : "Ninguno";

        String datosExtra =
                d.getName() + ";" +
                        d.getLastname() + ";" +
                        d.getAge() + ";" +
                        d.getCorreo() + ";" +
                        d.getEspecialidad() + ";" +
                        d.getNumContacto() + ";" +
                        consStr;

        String nuevaLinea = ced + "," + pass + ",doctor," + datosExtra;

        UsuarioFile.actualizarUsuario(ced, nuevaLinea);
    }
}
