import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CrearCuentaGUI extends JPanel {

    private boolean datosVisibles = false;
    AuthenticationService authService = new AuthenticationService();
    private JTextField campoUsuario, campoNombre, campoApellido, campoEdad, campoCorreo, campoTelefono, campoEspecialidad;
    private JPasswordField campoContra;
    private JLabel lblNombre, lblApellido, lblEdad, lblGenero, lblCorreo, lblTelefono, lblEspecialidad;
    private JRadioButton rbM, rbF;
    private ButtonGroup grupoGenero;
    private JButton btnContinuar, btnVolver, btnSalir;



    public CrearCuentaGUI(CardLayout cardLayout, JPanel contenedor) {

        setLayout(null);
        setBackground(new Color(135, 205, 235));

        // ---------- TÍTULO ----------
        JLabel titulo = new JLabel("Crear Cuenta");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setBounds(150, 20, 200, 30);
        add(titulo);

        // ---------- USUARIO ----------
        JLabel lblUsuario = new JLabel("Cédula");
        lblUsuario.setBounds(80, 50, 150, 20);
        add(lblUsuario);

         campoUsuario = new JTextField();
        campoUsuario.setBounds(80, 70, 220, 25);
        add(campoUsuario);

        // ---------- CONTRASEÑA ----------
        JLabel lblContra = new JLabel("Contraseña");
        lblContra.setBounds(80, 100, 150, 20);
        add(lblContra);

        campoContra = new JPasswordField();
        campoContra.setBounds(80, 120, 220, 25);
        add(campoContra);



        lblNombre = new JLabel("Nombre");
        lblNombre.setBounds(80, 150, 150, 20);
        lblNombre.setVisible(false);
        add(lblNombre);

         campoNombre = new JTextField();
        campoNombre.setBounds(80, 180, 220, 25);
        campoNombre.setVisible(false);
        add(campoNombre);

         lblApellido = new JLabel("Apellido");
        lblApellido.setBounds(80, 210, 150, 20);
        lblApellido.setVisible(false);
        add(lblApellido);

        campoApellido = new JTextField();
        campoApellido.setBounds(80, 230, 220, 25);
        campoApellido.setVisible(false);
        add(campoApellido);

         lblEdad = new JLabel("Edad");
        lblEdad.setBounds(80, 260, 150, 20);
        lblEdad.setVisible(false);
        add(lblEdad);

         campoEdad = new JTextField();
        campoEdad.setBounds(80, 280, 220, 25);
        campoEdad.setVisible(false);
        add(campoEdad);

        lblGenero = new JLabel("Género");
        lblGenero.setBounds(80, 310, 150, 20);
        lblGenero.setVisible(false);
        add(lblGenero);

        rbM = new JRadioButton("Masculino");
        rbM.setBounds(80, 330, 100, 25);
        rbM.setBackground(getBackground());
        rbM.setVisible(false);

         rbF = new JRadioButton("Femenino");
        rbF.setBounds(200, 330, 100, 25);
        rbF.setBackground(getBackground());
        rbF.setVisible(false);

        grupoGenero = new ButtonGroup();
        grupoGenero.add(rbM);
        grupoGenero.add(rbF);

        add(rbM);
        add(rbF);
       lblCorreo = new JLabel("Correo electronico");
        lblCorreo.setBounds(80, 310, 150, 20);
        lblCorreo.setVisible(false);
        add(lblCorreo);

        campoCorreo = new JTextField();
        campoCorreo.setBounds(80, 330, 220, 25);
        campoCorreo.setVisible(false);
        add(campoCorreo);

        lblTelefono = new JLabel("Teléfono");
        lblTelefono.setBounds(80, 360, 150, 20);
        lblTelefono.setVisible(false);
        add(lblTelefono);

         campoTelefono = new JTextField();
        campoTelefono.setBounds(80, 380, 220, 25);
        campoTelefono.setVisible(false);
        add(campoTelefono);



       lblEspecialidad = new JLabel("Especialidad");
        lblEspecialidad.setBounds(80, 410, 150, 20);
        lblEspecialidad.setVisible(false);
        add(lblEspecialidad);

        campoEspecialidad = new JTextField();
        campoEspecialidad.setBounds(80, 430, 220, 25);
        campoEspecialidad.setVisible(false);
        add(campoEspecialidad);

        // ---------- BOTONES ----------
        btnContinuar = new JButton("Continuar");
        btnContinuar.setBounds(330, 360, 110, 30);
        add(btnContinuar);


        btnVolver = new JButton("Volver");
        btnVolver.setBounds(330, 400, 100, 30);
        btnVolver.setVisible(false);
        add(btnVolver);

        btnSalir = new JButton("Salir");
        btnSalir.setBounds(330, 400, 100, 30);
        add(btnSalir);

        // ---------- SALIR ----------

            btnSalir.addActionListener(e -> {
                resetPantalla();
            cardLayout.show(contenedor, "Menu");
        });

        // ---------- VOLVER ----------
        btnVolver.addActionListener(e -> {
            resetPantalla();

        });

        // ---------- CONTINUAR / CREAR ----------
        btnContinuar.addActionListener(e -> {

            String usuario = campoUsuario.getText();
            String contra = new String(campoContra.getPassword());
            String rol = VerificarUsername.validarRol(usuario);


            if (!datosVisibles) {

                if (usuario.isBlank() || contra.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Campos vacíos");
                    return;
                }

                if (!VerificarUsername.validarUsername(usuario)) {
                    JOptionPane.showMessageDialog(this, "Usuario inválido");
                    return;
                }

                if (!VerificarPassword.validarPassword(contra)) {
                    JOptionPane.showMessageDialog(this, "Contraseña inválida");
                    return;
                }

                List<String[]> usuarios = UsuarioFile.leer("data/usuarios.txt");

                if (!UsuarioFile.usuarioExistente(usuario, usuarios)) {
                    JOptionPane.showMessageDialog(this, "El usuario ya existe");
                    return;
                }
                if (rol.equals("doctor")) {
                    lblNombre.setVisible(true);
                    campoNombre.setVisible(true);
                    lblApellido.setVisible(true);
                    campoApellido.setVisible(true);
                    lblEdad.setVisible(true);
                    campoEdad.setVisible(true);
                    lblEspecialidad.setVisible(true);
                    campoEspecialidad.setVisible(true);
                    lblCorreo.setVisible(true);
                    campoCorreo.setVisible(true);
                    lblTelefono.setVisible(true);
                    campoTelefono.setVisible(true);
                }else {
                    // Mostrar datos personales
                    lblNombre.setVisible(true);
                    campoNombre.setVisible(true);
                    lblApellido.setVisible(true);
                    campoApellido.setVisible(true);
                    lblEdad.setVisible(true);
                    campoEdad.setVisible(true);
                    lblGenero.setVisible(true);
                    rbM.setVisible(true);
                    rbF.setVisible(true);
                    lblTelefono.setVisible(true);
                    campoTelefono.setVisible(true);
                }
                btnContinuar.setText("Crear Cuenta");
                btnVolver.setVisible(true);
                btnSalir.setVisible(false);
                datosVisibles = true;
                return;
            }

            String Correo = campoCorreo.getText();
            String especialidad =  campoEspecialidad.getText();
            String nombre = campoNombre.getText();
            String apellido = campoApellido.getText();
            String edad = campoEdad.getText().trim();
            String telefono = campoTelefono.getText();
            String genero = rbM.isSelected() ? "M" : rbF.isSelected() ? "F" : "";


            if (rol.equals("doctor")) {

                // Validar campos de doctor (NO género)
                if (nombre.isBlank() || apellido.isBlank() || edad.isBlank()
                        || telefono.isBlank() || especialidad.isBlank() || Correo.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Complete todos los datos");
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

                if (!authService.soloNumeros(edad)) {
                    JOptionPane.showMessageDialog(this, "La edad solo debe contener números.");
                    return;
                }

                if (edad.length() > 3) {
                    JOptionPane.showMessageDialog(this, "Edad inválida. max 3 digitos");
                    return;
                }

                int edadNum = Integer.parseInt(edad);
                if (edadNum <= 0 || edadNum > 110) {
                    JOptionPane.showMessageDialog(this, "Edad fuera de rango (1 a 110).");
                    return;
                }

                if (!authService.soloNumeros(telefono) || !authService.digitosTelefono(telefono)) {
                    JOptionPane.showMessageDialog(this, "Teléfono inválido (solo números, mínimo 7 dígitos y máximo 15).");
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

                // Guardar doctor
                String linea = usuario + "," + contra + "," + rol + "," +
                        nombre + ";" + apellido + ";" + edad + ";" + Correo + ";" + especialidad + ";" + telefono;

                UsuarioFile.escribir("data/usuarios.txt", linea);
                JOptionPane.showMessageDialog(this, "Cuenta creada con éxito: " + rol);
                resetPantalla();
                cardLayout.show(contenedor, "Menu");
                return;

            } else {

                // Validar campos de paciente
                if (nombre.isBlank() || apellido.isBlank() || edad.isBlank()
                        || telefono.isBlank() || genero.isBlank()) {
                    JOptionPane.showMessageDialog(this, "Complete todos los datos");
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

                if (!authService.soloNumeros(edad)) {
                    JOptionPane.showMessageDialog(this, "La edad solo debe contener números.");
                    return;
                }

                if (edad.length() > 3) {
                    JOptionPane.showMessageDialog(this, "Edad inválida. max 3 digitos");
                    return;
                }

                int edadNum = Integer.parseInt(edad);
                if (edadNum <= 0 || edadNum > 110) {
                    JOptionPane.showMessageDialog(this, "Edad fuera de rango (1 a 110).");
                    return;
                }

                if (!authService.soloNumeros(telefono) || !authService.digitosTelefono(telefono)) {
                    JOptionPane.showMessageDialog(this, "Teléfono inválido (solo números, mínimo 7 dígitos y máximo 15).");
                    return;
                }

                // Guardar paciente
                String linea = usuario + "," + contra + "," + rol + "," +
                        nombre + ";" + apellido + ";" + edad + ";" + genero + ";" + telefono;

                UsuarioFile.escribir("data/usuarios.txt", linea);
                JOptionPane.showMessageDialog(this, "Cuenta creada con éxito: " + rol);
                resetPantalla();
                cardLayout.show(contenedor, "Menu");

                return;
            }

        });
    }
    private void resetPantalla() {
        // ocultar datos personales
        lblNombre.setVisible(false);
        campoNombre.setVisible(false);

        lblApellido.setVisible(false);
        campoApellido.setVisible(false);

        lblEdad.setVisible(false);
        campoEdad.setVisible(false);

        lblGenero.setVisible(false);
        rbM.setVisible(false);
        rbF.setVisible(false);

        lblCorreo.setVisible(false);
        campoCorreo.setVisible(false);

        lblTelefono.setVisible(false);
        campoTelefono.setVisible(false);

        lblEspecialidad.setVisible(false);
        campoEspecialidad.setVisible(false);

        // botones al estado inicial
        btnContinuar.setText("Continuar");
        btnVolver.setVisible(false);
        btnSalir.setVisible(true);

        // limpiar campos
        campoUsuario.setText("");
        campoContra.setText("");
        campoNombre.setText("");
        campoApellido.setText("");
        campoEdad.setText("");
        campoCorreo.setText("");
        campoTelefono.setText("");
        campoEspecialidad.setText("");

        // limpiar género
        grupoGenero.clearSelection();


        datosVisibles = false;

        revalidate();
        repaint();
    }

}
