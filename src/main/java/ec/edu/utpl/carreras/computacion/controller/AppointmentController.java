package ec.edu.utpl.carreras.computacion.controller;

import ec.edu.utpl.carreras.computacion.model.Cita;
import ec.edu.utpl.carreras.computacion.model.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentController {

    @FXML
    private TableView<Cita> appointmentTable;
    @FXML
    private TableColumn<Cita, Number> idColumn;
    @FXML
    private TableColumn<Cita, Number> barberoColumn;
    @FXML
    private TableColumn<Cita, Number> clienteColumn;
    @FXML
    private TableColumn<Cita, Number> servicioColumn;
    @FXML
    private TableColumn<Cita, String> fechaHoraColumn;
    @FXML
    private TableColumn<Cita, String> estadoColumn;

    @FXML
    private TextField barberoField;
    @FXML
    private TextField clienteField;
    @FXML
    private TextField servicioField;
    @FXML
    private TextField fechaHoraField;
    @FXML
    private TextField estadoField;
    @FXML
    private TextField notasField;

    private ObservableList<Cita> appointmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar columnas del TableView
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        barberoColumn.setCellValueFactory(cellData -> cellData.getValue().barberoProperty());
        clienteColumn.setCellValueFactory(cellData -> cellData.getValue().clienteProperty());
        servicioColumn.setCellValueFactory(cellData -> cellData.getValue().servicioProperty());
        fechaHoraColumn.setCellValueFactory(cellData -> cellData.getValue().fechaHoraProperty());
        estadoColumn.setCellValueFactory(cellData -> cellData.getValue().estadoProperty());

        // Cargar citas desde la base de datos
        loadAppointmentsFromDB();
    }

    private void loadAppointmentsFromDB() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM cita")) {

            while (rs.next()) {
                Cita cita = new Cita(
                        rs.getInt("id"),
                        rs.getInt("barbero_id"),
                        rs.getInt("cliente_id"),
                        rs.getInt("servicio_id"),
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

            // Para simplificar, se asume que se ingresan los ID de barbero, cliente y servicio
            int barberoId = Integer.parseInt(barberoField.getText());
            int clienteId = Integer.parseInt(clienteField.getText());
            int servicioId = Integer.parseInt(servicioField.getText());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraField.getText(), formatter);
            String estado = estadoField.getText();
            String notas = notasField.getText();

            pstmt.setInt(1, barberoId);
            pstmt.setInt(2, clienteId);
            pstmt.setInt(3, servicioId);
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
                    Cita newCita = new Cita(newId, barberoId, clienteId, servicioId, fechaHora, estado, notas);
                    appointmentList.add(newCita);
                    alert("Cita creadoa", "La cita ha sido registrada correctamente", Alert.AlertType.CONFIRMATION);
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
