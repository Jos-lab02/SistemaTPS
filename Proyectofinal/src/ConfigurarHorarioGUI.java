import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ConfigurarHorarioGUI extends JPanel {

    private final CardLayout cardLayout;
    private final JPanel contenedor;
    private final Doctor doctor;
    private final Consultorio consultorio;
    private final String ced;
    private final String pass;

    private final DefaultListModel<Horario> model = new DefaultListModel<>();
    private final JList<Horario> lista = new JList<>(model);

    private final Map<String, JCheckBox> checks = new LinkedHashMap<>();

    private static final String[] DIAS = {
            "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"
    };

    public ConfigurarHorarioGUI(CardLayout cardLayout, JPanel contenedor,
                                Doctor doctor, Consultorio consultorio,
                                String ced, String pass) {

        this.cardLayout = cardLayout;
        this.contenedor = contenedor;
        this.doctor = doctor;
        this.consultorio = consultorio;
        this.ced = ced;
        this.pass = pass;

        setLayout(new BorderLayout());
        setBackground(new Color(135, 205, 235));

        JLabel titulo = new JLabel("Configurar horario: " + consultorio.getConsultorio(), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 10, 10));
        centro.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        centro.setBackground(getBackground());

        JPanel panelDias = new JPanel();
        panelDias.setLayout(new BoxLayout(panelDias, BoxLayout.Y_AXIS));
        panelDias.setBackground(Color.WHITE);
        panelDias.setBorder(BorderFactory.createTitledBorder("Días disponibles"));

        for (String d : DIAS) {
            JCheckBox cb = new JCheckBox(d);
            cb.setBackground(Color.WHITE);
            checks.put(d, cb);
            panelDias.add(cb);
        }

        JPanel panelHorarios = new JPanel(new BorderLayout());
        panelHorarios.setBackground(Color.WHITE);
        panelHorarios.setBorder(BorderFactory.createTitledBorder("Horarios del consultorio"));

        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(lista);
        panelHorarios.add(scroll, BorderLayout.CENTER);

        centro.add(panelDias);
        centro.add(panelHorarios);
        add(centro, BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.setBackground(getBackground());

        JButton btnAgregar = new JButton("Agregar horario");
        JButton btnEliminar = new JButton("Eliminar seleccionado");

        JButton btnVolver = new JButton("Volver");


        acciones.add(btnAgregar);
        acciones.add(btnEliminar);
        acciones.add(btnVolver);

        add(acciones, BorderLayout.SOUTH);




        btnAgregar.addActionListener(e -> agregarHorariosConSelector());

        btnEliminar.addActionListener(e -> eliminarHorarioSeleccionado());

        btnVolver.addActionListener(e -> cardLayout.show(contenedor, "SeleccionarConsultorioHorario"));

        cargarHorarios();
        marcarDiasQueYaTienenHorario();
    }

    private void cargarHorarios() {
        model.clear();
        if (consultorio == null || consultorio.getHorarios() == null) return;

        for (Horario h : consultorio.getHorarios()) model.addElement(h);

        lista.revalidate();
        lista.repaint();
    }

    private void marcarDiasQueYaTienenHorario() {
        for (JCheckBox cb : checks.values()) cb.setSelected(false);
        if (consultorio == null || consultorio.getHorarios() == null) return;

        Set<String> diasConHorario = new HashSet<>();
        for (Horario h : consultorio.getHorarios()) {
            diasConHorario.add(normalizarDia(h.getDia()));
        }

        for (String d : DIAS) {
            if (diasConHorario.contains(normalizarDia(d))) {
                JCheckBox cb = checks.get(d);
                if (cb != null) cb.setSelected(true);
            }
        }
    }


    private void agregarHorariosConSelector() {
        if (doctor == null || consultorio == null) {
            JOptionPane.showMessageDialog(this, "Error interno: doctor o consultorio null.");
            return;
        }

        List<String> diasSeleccionados = new ArrayList<>();
        for (String d : DIAS) {
            JCheckBox cb = checks.get(d);
            if (cb != null && cb.isSelected()) diasSeleccionados.add(d);
        }

        if (diasSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos un día.");
            return;
        }

        // Panel del formulario (combos)
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.add(new JLabel("Hora inicio:"));

        JComboBox<Integer> cbInicio = new JComboBox<>();
        for (int i = 0; i <= 23; i++) cbInicio.addItem(i);
        form.add(cbInicio);

        form.add(new JLabel("Hora de salida:"));

        JComboBox<Integer> cbFin = new JComboBox<>();
        for (int i = 0; i <= 23; i++) cbFin.addItem(i);
        cbFin.setSelectedItem(14); // un default bonito
        form.add(cbFin);

        int op = JOptionPane.showConfirmDialog(
                this,
                form,
                "Seleccione el rango de horas",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (op != JOptionPane.OK_OPTION) return;

        int ini = (Integer) cbInicio.getSelectedItem();
        int fin = (Integer) cbFin.getSelectedItem();

        if (ini < 0 || ini > 23 || fin < 0 || fin > 23 || ini >= fin) {
            JOptionPane.showMessageDialog(this, "Rango inválido. Inicio < Fin y 0..23.");
            return;
        }

        int agregados = 0;

        for (String dia : diasSeleccionados) {
            String diaOk = formatearDia(dia);

            if (haySolape(diaOk, ini, fin)) {

                continue;
            }

            consultorio.getHorarios().add(new Horario(diaOk, ini, fin));
            agregados++;
        }

        if (agregados == 0) {
            JOptionPane.showMessageDialog(this, "No se agregó nada (se solapaba con horarios existentes).");
            return;
        }

        actualizarArchivoDoctor(ced, pass, doctor);

        JOptionPane.showMessageDialog(this, "Horarios agregados: " + agregados);
        cargarHorarios();
        marcarDiasQueYaTienenHorario();
    }

    private boolean haySolape(String dia, int ini, int fin) {
        for (Horario h : consultorio.getHorarios()) {
            if (normalizarDia(h.getDia()).equals(normalizarDia(dia))) {
                if (ini < h.getHoraFin() && fin > h.getHoraInicio()) return true;
            }
        }
        return false;
    }

    private void eliminarHorarioSeleccionado() {
        Horario elegido = lista.getSelectedValue();
        if (elegido == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un horario.");
            return;
        }

        try {
            Agenda.cargarCitas();
            if (Agenda.tieneCitasAgendadas(doctor, consultorio, elegido)) {
                JOptionPane.showMessageDialog(this,
                        "No puede eliminar este horario porque tiene citas pendientes en ese rango.");
                return;
            }
        } catch (Exception ignored) {}

        consultorio.getHorarios().remove(elegido);

        actualizarArchivoDoctor(ced, pass, doctor);

        JOptionPane.showMessageDialog(this, "Horario eliminado.");
        cargarHorarios();
        marcarDiasQueYaTienenHorario();
    }

    private String normalizarDia(String d) {
        if (d == null) return "";
        return d.trim().toLowerCase();
    }

    private String formatearDia(String d) {
        if (d == null) return "";
        String t = d.trim().toLowerCase();
        if (t.isEmpty()) return "";
        return t.substring(0, 1).toUpperCase() + t.substring(1);
    }

    // Formato:
    // ced,pass,doctor,nombre;apellido;edad;correo;esp;tel;ConsultorioA:Lunes-8-14/Martes-10-12|ConsultorioB:
    private void actualizarArchivoDoctor(String ced, String pass, Doctor d) {
        StringBuilder sb = new StringBuilder();

        for (Consultorio c : d.getConsultoriosAsociados()) {
            sb.append(c.getConsultorio());

            if (c.getHorarios() != null && !c.getHorarios().isEmpty()) {
                sb.append(":");
                for (int i = 0; i < c.getHorarios().size(); i++) {
                    Horario h = c.getHorarios().get(i);
                    sb.append(h.getDia()).append("-").append(h.getHoraInicio()).append("-").append(h.getHoraFin());
                    if (i < c.getHorarios().size() - 1) sb.append("/");
                }
            } else {
                sb.append(":");
            }

            sb.append("|");
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
