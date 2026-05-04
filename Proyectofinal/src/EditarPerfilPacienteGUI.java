import javax.swing.*;
import java.awt.*;

public class EditarPerfilPacienteGUI extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contenedor;
    private final Paciente paciente;
    private final String ced;
    private final String pass;

    private final JTextField txtNombre = new JTextField();
    private final JTextField txtApellido = new JTextField();
    private final JTextField txtEdad = new JTextField();
    private final JComboBox<String> cbGenero = new JComboBox<>(new String[]{"M", "F"});
    private final JTextField txtTelefono = new JTextField();

    public EditarPerfilPacienteGUI(CardLayout cardLayout, JPanel contenedor,
                                   Paciente paciente, String ced, String pass) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.paciente = paciente;
        this.ced = ced;
        this.pass = pass;

        setLayout(null);
        setBackground(new Color(135, 205, 235));

        JLabel titulo = new JLabel("Editar Perfil - Paciente");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBounds(150, 40, 250, 30);
        add(titulo);


        JLabel lbNombre = new JLabel("Nombre:");
        lbNombre.setBounds(90, 100, 150, 25);
        add(lbNombre);

        JLabel lbApellido = new JLabel("Apellido:");
        lbApellido.setBounds(90, 140, 150, 25);
        add(lbApellido);

        JLabel lbEdad = new JLabel("Edad:");
        lbEdad.setBounds(90, 180, 150, 25);
        add(lbEdad);

        JLabel lbGenero = new JLabel("Género (M/F):");
        lbGenero.setBounds(90, 220, 150, 25);
        add(lbGenero);

        JLabel lbTelefono = new JLabel("Teléfono:");
        lbTelefono.setBounds(90, 260, 150, 25);
        add(lbTelefono);


        txtNombre.setBounds(200, 100, 200, 25);
        add(txtNombre);

        txtApellido.setBounds(200, 140, 200, 25);
        add(txtApellido);

        txtEdad.setBounds(200, 180, 200, 25);
        add(txtEdad);

        cbGenero.setBounds(200, 220, 200, 25);
        add(cbGenero);

        txtTelefono.setBounds(200, 260, 200, 25);
        add(txtTelefono);


        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(180, 330, 120, 30);
        add(btnGuardar);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setBounds(320, 330, 100, 30);
        add(btnVolver);


        cargarDatosEnCampos();

        btnGuardar.addActionListener(e -> guardarCambios());

        btnVolver.addActionListener(e -> cardLayout.show(contenedor, "MenuPacientes"));
    }

    private void cargarDatosEnCampos() {
        if (paciente == null) return;

        txtNombre.setText(paciente.getName());
        txtApellido.setText(paciente.getLastname());
        txtEdad.setText(paciente.getAge());

        String gen = paciente.getGenero();
        if (gen != null) {
            gen = gen.trim().toUpperCase();
            if (gen.equals("M")) cbGenero.setSelectedItem("M");
            else if (gen.equals("F")) cbGenero.setSelectedItem("F");
        }

        txtTelefono.setText(paciente.getNumContacto());
    }

    private void guardarCambios() {

        AuthenticationService authService = new AuthenticationService();
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String edad = txtEdad.getText().trim();
        String genero = String.valueOf(cbGenero.getSelectedItem()).trim().toUpperCase();
        String telefono = txtTelefono.getText().trim();

        //Para evitar campos vacíos
        if (nombre.isBlank() || apellido.isBlank() || edad.isBlank() || telefono.isBlank()) {
            JOptionPane.showMessageDialog(this, "No deje campos vacíos.");
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

        //Controlar la edad
        if (edad.length() > 3) { // 999 como máximo
            JOptionPane.showMessageDialog(this, "Edad inválida. max 3 digitos");
            return;
        }

        if (!edad.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Edad inválida (solo números).");
            return;
        }
        int e = Integer.parseInt(edad);
        if (e <= 0 || e > 110) {
            JOptionPane.showMessageDialog(this, "Edad fuera de rango.");
            return;
        }

        if (!authService.soloNumeros(telefono) || !authService.digitosTelefono(telefono)){
            JOptionPane.showMessageDialog(this, "Teléfono inválido (solo números, mínimo 7 dígitos y máximo 15).");
            return;
        }

        if (!genero.equals("M") && !genero.equals("F")) {
            JOptionPane.showMessageDialog(this, "Género inválido.");
            return;
        }

        paciente.setName(nombre);
        paciente.setLastname(apellido);
        paciente.setAge(String.valueOf(e));
        paciente.setGenero(genero);
        paciente.setNumContacto(telefono);

        String datosExtra = nombre + ";" + apellido + ";" + e + ";" + genero + ";" + telefono;
        String nuevaLinea = ced + "," + pass + ",paciente," + datosExtra;


        UsuarioFile.actualizarUsuario(ced, nuevaLinea);

        JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
        cardLayout.show(contenedor, "MenuPacientes");
    }
}
