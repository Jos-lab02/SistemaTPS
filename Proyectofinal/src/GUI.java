import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class GUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel contenedor;
    private List<Doctor> listaDoctores = new ArrayList<>();
    private Doctor doctorLogueado;
    private String cedLogueada;
    private String passLogueada;
    private AsociarConsultorioGUI panelAsociar;
    private Paciente pacienteLogueado;
    private SolicitarCitasGUI panelSolicitarCitas;
    private CancelarCitasGUI panelCancelarCitas;


    public GUI() {
        setTitle("SATM");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        listaDoctores = DoctorService.obtenerDoctoresRegistrados();

        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);

        // -------- PANEL MENU PRINCIPAL --------
        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        panel1.setBackground(new Color(135, 206, 235));

        JLabel titulo = new JLabel("BIENVENIDO A SATM");
        titulo.setFont(new Font("Arial", Font.ITALIC, 18));
        titulo.setBounds(140, 90, 200, 30);
        panel1.add(titulo);

        JButton botonIrAPantalla2 = new JButton("INICIAR SESION");
        botonIrAPantalla2.setBounds(155, 150, 150, 30);
        panel1.add(botonIrAPantalla2);

        JButton botonIrAPantalla3 = new JButton("Crear Cuenta");
        botonIrAPantalla3.setBounds(155, 200, 150, 30);
        panel1.add(botonIrAPantalla3);

        botonIrAPantalla2.addActionListener(e ->
                cardLayout.show(contenedor, "IniciarSesión")
        );

        botonIrAPantalla3.addActionListener(e ->
                cardLayout.show(contenedor, "CrearCuenta")
        );

        contenedor.add(panel1, "Menu");
        contenedor.add(new CrearCuentaGUI(cardLayout, contenedor), "CrearCuenta");
        contenedor.add(new IniciarSesionGUI(this, cardLayout, contenedor, listaDoctores), "IniciarSesión");
        contenedor.add(new MenuDoctorGUI(cardLayout,contenedor,this),"MenuDoctor");
        contenedor.add(new MenuPacientesGUI(this ,cardLayout, contenedor), "MenuPacientes");
        contenedor.add(new ConsultorioGUI(this, cardLayout, contenedor), "Consultorios");

        add(contenedor);
        cardLayout.show(contenedor, "Menu");

    }

    public void irSeleccionarConsultorioHorario(Doctor doctor, String ced, String pass) {
        contenedor.add(
                new SeleccionarConsultorioHorarioGUI(cardLayout, contenedor, doctor, ced, pass),
                "SeleccionarConsultorioHorario"
        );
        cardLayout.show(contenedor, "SeleccionarConsultorioHorario");
        contenedor.revalidate();
        contenedor.repaint();
    }

    public void irAsociarConsultorio(Doctor doctor, List<Consultorio> globales, String ced, String pass) {

        if (doctor == null) {
            JOptionPane.showMessageDialog(this, "No hay doctor logueado.");
            return;
        }
        if (globales == null) {
            globales = ConsultorioService.cargarConsultorios();
        }
        if (globales == null) {
            globales = new ArrayList<>();
        }

        if (panelAsociar != null) {
            contenedor.remove(panelAsociar);
        }

        panelAsociar = new AsociarConsultorioGUI(cardLayout, contenedor, doctor, globales, ced, pass);
        contenedor.add(panelAsociar, "AsociarConsultorio");

        cardLayout.show(contenedor, "AsociarConsultorio");
        contenedor.revalidate();
        contenedor.repaint();
    }
    public void irQuitarConsultorio(Doctor doctor, String ced, String pass) {
        contenedor.add(
                new QuitarConsultoriosGUI(cardLayout, contenedor, doctor, ced, pass),
                "QuitarConsultorio"
        );
        cardLayout.show(contenedor, "QuitarConsultorio");
    }

    public void irMenu() {
        cardLayout.show(contenedor, "Menu");
    }
    public void setSesionDoctor(Doctor d, String ced, String pass) {
        this.doctorLogueado = d;
        this.cedLogueada = ced;
        this.passLogueada = pass;
    }
    public void irEditarPerfilDoctor(Doctor doctor, String ced, String pass) {
        contenedor.add(new EditarPerfilDoctorGUI(cardLayout, contenedor, doctor, ced, pass), "EditarPerfilDoctor");
        cardLayout.show(contenedor, "EditarPerfilDoctor");
        contenedor.revalidate();
        contenedor.repaint();
    }
    public void setSesionPaciente(Paciente p, String ced, String pass) {
        this.pacienteLogueado = p;
        this.cedLogueada = ced;
        this.passLogueada = pass;
    }

    public Paciente getPacienteLogueado() {
        return pacienteLogueado;
    }
    public void irSolicitarCitas() {
        if ( pacienteLogueado == null) {
            JOptionPane.showMessageDialog(this, "No hay paciente logueado.");
            return;
        }

        if (panelSolicitarCitas != null)
            contenedor.remove(panelSolicitarCitas);

        panelSolicitarCitas = new SolicitarCitasGUI(cardLayout, contenedor,pacienteLogueado);
        contenedor.add(panelSolicitarCitas, "SolicitarCitas");

        cardLayout.show(contenedor, "SolicitarCitas");
        contenedor.revalidate();
        contenedor.repaint();
    }
    private VerMisCitasGUI panelVerCitas;

    public void irVerMisCitas() {
        if (pacienteLogueado == null) {
            JOptionPane.showMessageDialog(this, "No hay paciente logueado.");
            return;
        }

        if (panelVerCitas != null)
            contenedor.remove(panelVerCitas);

        panelVerCitas = new VerMisCitasGUI(cardLayout, contenedor, pacienteLogueado);
        contenedor.add(panelVerCitas, "VerMisCitas");

        cardLayout.show(contenedor, "VerMisCitas");
        contenedor.revalidate();
        contenedor.repaint();
    }
    public void irCancelarCitas() {

        if (pacienteLogueado == null) {
            JOptionPane.showMessageDialog(this, "No hay paciente logueado.");
            return;
        }


        if (panelCancelarCitas != null) {
            contenedor.remove(panelCancelarCitas);
        }

        panelCancelarCitas = new CancelarCitasGUI(
                cardLayout,
                contenedor,
                pacienteLogueado
        );

        contenedor.add(panelCancelarCitas, "CancelarCitas");

        cardLayout.show(contenedor, "CancelarCitas");
        contenedor.revalidate();
        contenedor.repaint();
    }

    public void irEditarPerfilPaciente(Paciente paciente, String ced, String pass) {
        contenedor.add(new EditarPerfilPacienteGUI(cardLayout, contenedor, paciente, ced, pass), "EditarPerfilPaciente");
        cardLayout.show(contenedor, "EditarPerfilPaciente");
        contenedor.revalidate();
        contenedor.repaint();
    }
    public void irVerAgendaDoctor(Doctor doctor) {
        if (doctor == null) {
            JOptionPane.showMessageDialog(this, "No hay doctor logueado.");
            return;
        }

        contenedor.add(
                new VerAgendaDoctorGUI(cardLayout, contenedor, doctor),
                "VerAgendaDoctor"
        );

        cardLayout.show(contenedor, "VerAgendaDoctor");
        contenedor.revalidate();
        contenedor.repaint();
    }

    public Doctor getDoctorLogueado() {
        return doctorLogueado;
    }

    public String getCedLogueada() {
        return cedLogueada;
    }

    public String getPassLogueada() {
        return passLogueada;
    }


}

