import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

public class IniciarSesionGUI extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contenedor;

    private final GUI gui;
    private final List<Doctor> listaDoctores;

    public IniciarSesionGUI(GUI gui, CardLayout cardLayout, JPanel contenedor, List<Doctor> listaDoctores) {
        this.gui = gui;
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.listaDoctores = listaDoctores;

        setLayout(null);
        setBackground(new Color(135, 205, 235));

        JLabel titulo2 = new JLabel("Iniciar Sesion");
        titulo2.setFont(new Font("Arial", Font.ITALIC, 15));
        titulo2.setBounds(170,50,200,30);
        add(titulo2);

        JLabel titulo3 = new JLabel("Nº de Cedula");
        titulo3.setBounds(100, 100, 150, 30);
        add(titulo3);

        JTextField campoUsuario = new JTextField();
        campoUsuario.setBounds(100, 130, 200, 30);
        add(campoUsuario);

        JLabel titulo4 = new JLabel("CONTRASEÑA");
        titulo4.setBounds(100, 170, 240, 30);
        add(titulo4);

        JPasswordField campoContra = new JPasswordField();
        campoContra.setBounds(100, 200, 200, 30);
        add(campoContra);

        JButton boton = new JButton("Ingresar");
        boton.setBounds(100, 240, 100, 30);
        add(boton);

        boton.addActionListener(e -> {
            String ced = campoUsuario.getText().trim();
            String pass = new String(campoContra.getPassword());

            if (ced.isBlank() || pass.isBlank()) {
                JOptionPane.showMessageDialog(this, "No puede haber campos vacios", "Error", 0);
                return;
            }

            AuthenticationService authService = new AuthenticationService();
            boolean ok = authService.login(ced, pass);

            if (!ok) {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrecta", "Error", 0);
                return;
            }

            String rol = VerificarUsername.validarRol(ced);

            if ("doctor".equalsIgnoreCase(rol)) {


                Doctor doctorLogueado = null;
                for (Doctor d : listaDoctores) {
                    if (d.getUsername().equals(ced)) {
                        doctorLogueado = d;
                        break;
                    }
                }
                if (doctorLogueado == null) {
                    List<Doctor> recargada = DoctorService.obtenerDoctoresRegistrados();
                    listaDoctores.clear();
                    listaDoctores.addAll(recargada);

                    for (Doctor d : listaDoctores) {
                        if (d.getUsername().equals(ced)) {
                            doctorLogueado = d;
                            break;
                        }
                    }
                }


                if (doctorLogueado == null) {
                    JOptionPane.showMessageDialog(this, "Doctor no encontrado en la lista", "Error", 0);
                    return;
                }

                gui.setSesionDoctor(doctorLogueado, ced, pass);
                cardLayout.show(contenedor, "MenuDoctor");
                campoUsuario.setText("");
                campoContra.setText("");


            } else {
                String[] datos =authService.getDatosLogueado();
                String[] extra = datos[3].split(";",-1);

                Paciente paciente=new Paciente(
                        extra[0],
                        extra[1],
                        extra[2],
                        ced,pass,
                        extra[3],
                        extra[4]

                );
                gui.setSesionPaciente(paciente,ced,pass);
                cardLayout.show(contenedor, "MenuPacientes");
                campoUsuario.setText("");
                campoContra.setText("");

            }
        });

        JButton boton2 = new JButton("Volver");
        boton2.setBounds(300, 400, 100, 30);
        add(boton2);

        boton2.addActionListener(e -> {
            campoUsuario.setText("");
            campoContra.setText("");
            cardLayout.show(contenedor, "Menu") ;
        });

    }
}

