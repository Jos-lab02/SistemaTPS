import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SolicitarCitasGUI extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contenedor;

    private final Paciente paciente;
    private final JPanel panelCentral = new JPanel();
    private String especialidadSeleccionada = null;
    private Doctor doctorSeleccionado = null;

    private static final String CARD_MENU_PACIENTES = "MenuPacientes";

    public SolicitarCitasGUI(CardLayout cardLayout, JPanel contenedor, Paciente paciente) {
        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.paciente = paciente;

        setLayout(new BorderLayout());
        setBackground(new Color(141, 204, 238));

        JLabel titulo = new JLabel("Solicitud de Citas", SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        panelCentral.setLayout(new GridLayout(0, 1, 10, 10));
        panelCentral.setBackground(new Color(141, 204, 238));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JScrollPane scroll = new JScrollPane(panelCentral);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scroll, BorderLayout.CENTER);

        JButton btnVolver = new JButton("Volver");
        btnVolver.setPreferredSize(new Dimension(90, 35));
        btnVolver.setFocusPainted(false);

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBackground(new Color(141, 204, 238));
        panelBoton.add(btnVolver);
        add(panelBoton, BorderLayout.SOUTH);

        btnVolver.addActionListener(e -> volver());


        cargarEspecialidades();
    }


    private void volver() {
        if (doctorSeleccionado != null) {
            Doctor tmp = doctorSeleccionado;
            doctorSeleccionado = null;
            cargarDoctoresPorEspecialidad(tmp.getEspecialidad());
            return;
        }

        if (especialidadSeleccionada != null) {
            especialidadSeleccionada = null;
            cargarEspecialidades();
            return;
        }


        cardLayout.show(contenedor, CARD_MENU_PACIENTES);
    }

    public void cargarEspecialidades() {
        panelCentral.setLayout(new GridLayout(0, 1, 10, 10));

        panelCentral.removeAll();

        List<Doctor> doctores = DoctorService.obtenerDoctoresRegistrados();
        List<String> especialidades = obtenerEspecialidades(doctores);

        if (especialidades.isEmpty()) {
            panelCentral.add(new JLabel("No hay especialidades registradas"));
        } else {
            for (String esp : especialidades) {
                JButton btn = new JButton(esp);
                btn.addActionListener(e -> {
                    especialidadSeleccionada = esp;
                    cargarDoctoresPorEspecialidad(esp);
                });
                panelCentral.add(btn);
            }
        }

        refrescarPanelCentral();
    }

    private List<String> obtenerEspecialidades(List<Doctor> doctores) {
        List<String> lista = new ArrayList<>();
        for (Doctor d : doctores) {
            String esp = d.getEspecialidad();
            if (esp != null && !esp.isBlank() && !lista.contains(esp)) {
                lista.add(esp);
            }
        }
        return lista;
    }


    private void cargarDoctoresPorEspecialidad(String especialidad) {
        panelCentral.setLayout(new GridLayout(0, 1, 10, 10));

        panelCentral.removeAll();

        List<Doctor> doctores = DoctorService.obtenerDoctoresRegistrados();
        boolean hay = false;

        for (Doctor d : doctores) {
            if (d.getEspecialidad() != null && d.getEspecialidad().equalsIgnoreCase(especialidad)) {
                hay = true;

                JButton btnDoctor = new JButton("Dr/a. " + d.getName() + " " + d.getLastname());
                btnDoctor.addActionListener(e -> {
                    doctorSeleccionado = d;
                    cargarDisponibilidadDoctor(d);
                });

                panelCentral.add(btnDoctor);
            }
        }

        if (!hay) panelCentral.add(new JLabel("No hay doctores en esta especialidad"));

        refrescarPanelCentral();
    }


    private void cargarDisponibilidadDoctor(Doctor doctor) {
        panelCentral.removeAll();
        panelCentral.setLayout(new GridLayout(1, 1, 10, 10));

        // Panel principal
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        box.setBackground(Color.WHITE);

        JLabel lblDoc = new JLabel("Disponibilidad de: Dr/a. " + doctor.getName() + " " + doctor.getLastname());
        lblDoc.setFont(new Font("Arial", Font.BOLD, 16));
        lblDoc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblEsp = new JLabel("Especialidad: " + doctor.getEspecialidad());
        lblEsp.setAlignmentX(Component.LEFT_ALIGNMENT);

        box.add(lblDoc);
        box.add(Box.createVerticalStrut(6));
        box.add(lblEsp);
        box.add(Box.createVerticalStrut(12));

        // Combos
        JComboBox<Consultorio> cbConsultorio = new JComboBox<>();
        JComboBox<String> cbDia = new JComboBox<>();
        JComboBox<String> cbHora = new JComboBox<>();

        cbConsultorio.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbDia.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbHora.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Cargar consultorios del doctor
        List<Consultorio> consultorios = (doctor.getConsultoriosAsociados() == null)
                ? new ArrayList<>()
                : doctor.getConsultoriosAsociados();

        if (consultorios.isEmpty()) {
            box.add(new JLabel("Este doctor no tiene consultorios asociados."));
            panelCentral.add(box);
            refrescarPanelCentral();
            return;
        }

        for (Consultorio c : consultorios) cbConsultorio.addItem(c);


        box.add(new JLabel("Consultorio:"));
        box.add(cbConsultorio);
        box.add(Box.createVerticalStrut(10));

        box.add(new JLabel("Día:"));
        box.add(cbDia);
        box.add(Box.createVerticalStrut(10));

        box.add(new JLabel("Hora disponible:"));
        box.add(cbHora);
        box.add(Box.createVerticalStrut(14));

        JButton btnAgendar = new JButton("Agendar cita");
        btnAgendar.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(btnAgendar);


        try {
            Agenda.cargarCitas();
        } catch (Exception ignored) {}


        cbConsultorio.addActionListener(e -> {
            Consultorio cSel = (Consultorio) cbConsultorio.getSelectedItem();
            cargarDiasDisponibles(cbDia, doctor, cSel);
            // al cambiar consultorio, también carga horas del primer día si existe
            String diaSel = (String) cbDia.getSelectedItem();
            cargarHorasDisponibles(cbHora, doctor, cSel, diaSel);
        });

        cbDia.addActionListener(e -> {
            Consultorio cSel = (Consultorio) cbConsultorio.getSelectedItem();
            String diaSel = (String) cbDia.getSelectedItem();
            cargarHorasDisponibles(cbHora, doctor, cSel, diaSel);
        });


        Consultorio inicial = (Consultorio) cbConsultorio.getSelectedItem();
        cargarDiasDisponibles(cbDia, doctor, inicial);
        String diaInicial = (String) cbDia.getSelectedItem();
        cargarHorasDisponibles(cbHora, doctor, inicial, diaInicial);


        btnAgendar.addActionListener(e -> {
            Consultorio cSel = (Consultorio) cbConsultorio.getSelectedItem();
            String diaSel = (String) cbDia.getSelectedItem();
            String horaSel = (String) cbHora.getSelectedItem();

            if (cSel == null || diaSel == null || horaSel == null) {
                JOptionPane.showMessageDialog(this, "Seleccione consultorio, día y hora.");
                return;
            }


            if (Agenda.estaLaHoraOcupada(doctor.getUsername(), diaSel, horaSel)) {
                JOptionPane.showMessageDialog(this, "Esa hora ya fue tomada. Elija otra.");
                cargarHorasDisponibles(cbHora, doctor, cSel, diaSel);
                return;
            }


            Cita cita = new Cita(doctor, paciente, cSel, diaSel, horaSel, "Consulta");
            boolean ok = Agenda.agendarCita(cita);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Cita agendada correctamente ");

                try { Agenda.cargarCitas(); } catch (Exception ignored) {}
                cargarHorasDisponibles(cbHora, doctor, cSel, diaSel);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agendar la cita.");
            }
        });

        panelCentral.add(box);
        refrescarPanelCentral();
    }

    private void cargarDiasDisponibles(JComboBox<String> cbDia, Doctor doctor, Consultorio consultorio) {
        cbDia.removeAllItems();
        if (consultorio == null || consultorio.getHorarios() == null) return;

        // Días únicos que existan en horarios
        Set<String> dias = new LinkedHashSet<>();
        for (Horario h : consultorio.getHorarios()) {
            if (h.getDia() != null && !h.getDia().isBlank()) {
                dias.add(formatearDia(h.getDia()));
            }
        }

        for (String d : dias) cbDia.addItem(d);

        if (cbDia.getItemCount() == 0) {
            cbDia.addItem("Sin horarios");
        }
    }

    private void cargarHorasDisponibles(JComboBox<String> cbHora, Doctor doctor, Consultorio consultorio, String dia) {
        cbHora.removeAllItems();

        if (consultorio == null || consultorio.getHorarios() == null || dia == null) return;
        if ("Sin horarios".equalsIgnoreCase(dia)) return;


        Set<Integer> horas = new TreeSet<>();

        for (Horario h : consultorio.getHorarios()) {
            if (normalizarDia(h.getDia()).equals(normalizarDia(dia))) {
                int ini = h.getHoraInicio();
                int fin = h.getHoraFin();
                for (int hora = ini; hora < fin; hora++) {

                    if (!Agenda.estaLaHoraOcupada(doctor.getUsername(), formatearDia(dia), String.valueOf(hora))) {
                        horas.add(hora);
                    }
                }
            }
        }

        if (horas.isEmpty()) {
            cbHora.addItem("No hay horas disponibles");
            return;
        }

        for (Integer h : horas) cbHora.addItem(String.valueOf(h));
    }

    private void refrescarPanelCentral() {
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private String normalizarDia(String d) {
        return (d == null) ? "" : d.trim().toLowerCase();
    }

    private String formatearDia(String d) {
        if (d == null) return "";
        String t = d.trim().toLowerCase();
        if (t.isEmpty()) return "";
        return t.substring(0, 1).toUpperCase() + t.substring(1);
    }
}
