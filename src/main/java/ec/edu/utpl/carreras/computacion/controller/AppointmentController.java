package ec.edu.utpl.carreras.computacion.controller;

import ec.edu.utpl.carreras.computacion.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AppointmentController {

    @FXML
    private TableView<Cita> appointmentTable;
    @FXML
    private TableColumn<Cita, Number> idColumn;
    @FXML
    private TableColumn<Cita, String > barberoColumn;
    @FXML
    private TableColumn<Cita, String> clienteColumn;
    @FXML
    private TableColumn<Cita, String> servicioColumn;
    @FXML
    private TableColumn<Cita, String> fechaHoraColumn;
    @FXML
    private TableColumn<Cita, String> estadoColumn;

    @FXML
    private ComboBox<Barbero> barberoCombo;
    @FXML
    private ComboBox<Cliente> clienteCombo;
    @FXML
    private ComboBox<Servicio> servicioCombo;
    @FXML
    private DatePicker fechaHoraPicker;
    @FXML
    private TextField estadoField;
    @FXML
    private TextField notasField;

    private ObservableList<Cita> appointmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar columnas del TableView
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        barberoColumn.setCellValueFactory(cellData -> cellData.getValue().barberoNombreProperty());
        clienteColumn.setCellValueFactory(cellData -> cellData.getValue().clienteNombrePrperty());
        servicioColumn.setCellValueFactory(cellData -> cellData.getValue().servicioNombreProperty());
        fechaHoraColumn.setCellValueFactory(cellData -> cellData.getValue().fechaHoraProperty());
        estadoColumn.setCellValueFactory(cellData -> cellData.getValue().estadoProperty());

        // Cargar citas desde la base de datos
        loadAppointmentsFromDB();
        loadBarberos();
        loadClientes();
        loadServicios();
    }

    private void loadBarberos() {
        try(Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM barbero")) {

            ObservableList<Barbero> barberoList = FXCollections.observableArrayList();
            while (rs.next()) {
                Barbero b = new Barbero(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                barberoList.add(b);
            }
            barberoCombo.setItems(barberoList);

        } catch(SQLException e) {
            alert("Error", "Error al traer los barberos", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void loadClientes() {
        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM cliente")) {

            ObservableList<Cliente> clientes = FXCollections.observableArrayList();
            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                clientes.add(c);
            }
            clienteCombo.setItems(clientes);

        } catch(SQLException e) {
            alert("Error", "Error al traer los barberos", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void loadServicios() {
        try(Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM servicio")) {

            ObservableList<Servicio> servicios = FXCollections.observableArrayList();
            while (rs.next()) {
                Servicio s = new Servicio(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("duracion")
                );
                servicios.add(s);
            }
            servicioCombo.setItems(servicios);

        } catch(SQLException e) {
            alert("Error", "Error al traer los barberos", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void loadAppointmentsFromDB() {
        String qry = """
        SELECT c.id, c.barbero_id, b.nombre AS barbero_nombre,
        c.cliente_id, cl.nombre AS cliente_nombre,
        c.servicio_id, s.nombre AS servicio_nombre,
        c.fecha_hora, c.estado, c.notas
        FROM cita c
        JOIN barbero b ON c.barbero_id = b.id
        JOIN cliente cl ON c.cliente_id = cl.id
        JOIN servicio s ON c.servicio_id = s.id;
        """;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(qry)) {

            while (rs.next()) {
                Cita cita = new Cita(
                        rs.getInt("id"),
                        rs.getInt("barbero_id"),
                        rs.getString("barbero_nombre"),
                        rs.getInt("cliente_id"),
                        rs.getString("cliente_nombre"),
                        rs.getInt("servicio_id"),
                        rs.getString("servicio_nombre"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getString("estado"),
                        rs.getString("notas")
                );
                appointmentList.add(cita);
            }
            appointmentTable.setItems(appointmentList);
        } catch (SQLException e) {
            e.printStackTrace();
            alert("Error", "Error al recuperar las citas almacenadas", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAddAppointment() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO cita (barbero_id, cliente_id, servicio_id, fecha_hora, estado, notas) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Se obtienen los objetos seleccionados de los ComboBox
            Barbero barberoSelected = barberoCombo.getSelectionModel().getSelectedItem();
            Cliente clienteSelected = clienteCombo.getSelectionModel().getSelectedItem();
            Servicio servicioSelected = servicioCombo.getSelectionModel().getSelectedItem();

            // Validar que se haya seleccionado un objeto en cada Combobox
            if(Objects.isNull(barberoSelected) || Objects.isNull(clienteSelected) || Objects.isNull(servicioSelected)) {
                alert("Error", "Debe seleccionar todos los campos", Alert.AlertType.ERROR);
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraPicker.getValue(), formatter);
            LocalDateTime fechaHora = fechaHoraPicker.getValue().atStartOfDay();
            String estado = estadoField.getText();
            String notas = notasField.getText();

            pstmt.setInt(1, barberoSelected.getId());
            pstmt.setInt(2, clienteSelected.getId());
            pstmt.setInt(3, servicioSelected.getId());
            pstmt.setTimestamp(4, Timestamp.valueOf(fechaHora));
            pstmt.setString(5, estado);
            pstmt.setString(6, notas);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al insertar la cita, no se afectaron filas.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    Cita newCita = new Cita(newId,
                            barberoSelected.getId(), barberoSelected.getNombre(),
                            clienteSelected.getId(), clienteSelected.getNombre(),
                            servicioSelected.getId(), servicioSelected.getNombre(),
                            fechaHora, estado, notas);
                    appointmentList.add(newCita);
                    alert("Cita creada", "La cita ha sido registrada correctamente", Alert.AlertType.CONFIRMATION);
                } else {
                    alert("Error", "Error al crear la cita", Alert.AlertType.ERROR);
                    throw new SQLException("Error al insertar la cita, no se obtuvo ID.");
                }
            }
        } catch (Exception e) {
            alert("Error", "Error al crear la cita", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    private void alert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
