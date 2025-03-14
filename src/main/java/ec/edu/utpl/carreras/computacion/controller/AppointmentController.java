package ec.edu.utpl.carreras.computacion.controller;

import ec.edu.utpl.carreras.computacion.model.Barbero;
import ec.edu.utpl.carreras.computacion.model.Cita;
import ec.edu.utpl.carreras.computacion.model.Cliente;
import ec.edu.utpl.carreras.computacion.model.Servicio;
import ec.edu.utpl.carreras.computacion.repository.BarberoRepository;
import ec.edu.utpl.carreras.computacion.repository.CitaRepository;
import ec.edu.utpl.carreras.computacion.repository.ClienteRepository;
import ec.edu.utpl.carreras.computacion.repository.ServicioRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class AppointmentController {

    @FXML
    private TableView<Cita> appointmentTable;
    @FXML
    private TableColumn<Cita, Number> idColumn;
    @FXML
    private TableColumn<Cita, String> barberoColumn;
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
    private CitaRepository citaRepository = new CitaRepository();
    private BarberoRepository barberoRepository = new BarberoRepository();
    private ClienteRepository clienteRepository = new ClienteRepository();
    private ServicioRepository servicioRepository = new ServicioRepository();

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
        ObservableList<Barbero> barberoObtList = FXCollections.observableArrayList();
        try {
            var barberos = barberoRepository.getAllBarberos();
            barberoObtList.addAll(barberos);
            barberoCombo.setItems(barberoObtList);

        } catch (SQLException e) {
            alert("Error", "Error al traer los barberos", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void loadClientes() {
        ObservableList<Cliente> clientesObsList = FXCollections.observableArrayList();
        try {
            var clienteList = clienteRepository.getAllClientes();
            clientesObsList.addAll(clienteList);
            clienteCombo.setItems(clientesObsList);

        } catch (SQLException e) {
            alert("Error", "Error al traer los clientes", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void loadServicios() {
        ObservableList<Servicio> serviciosObList = FXCollections.observableArrayList();
        try {
            var servicios = servicioRepository.getAllServicios();
            serviciosObList.addAll(servicios);
            servicioCombo.setItems(serviciosObList);

        } catch (SQLException e) {
            alert("Error", "Error al traer los servicios", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void loadAppointmentsFromDB() {
        try {
            List<Cita> citas = citaRepository.getAllCitas();
            appointmentList.clear();
            appointmentList.addAll(citas);
            appointmentTable.setItems(appointmentList);
        } catch (SQLException e) {
            e.printStackTrace();
            alert("Error", "Error al recuperar las citas almacenadas", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAddAppointment() {
        // Se obtienen los objetos seleccionados de los ComboBox
        Barbero barberoSelected = barberoCombo.getSelectionModel().getSelectedItem();
        Cliente clienteSelected = clienteCombo.getSelectionModel().getSelectedItem();
        Servicio servicioSelected = servicioCombo.getSelectionModel().getSelectedItem();

        // Validar que se haya seleccionado un objeto en cada Combobox
        if (Objects.isNull(barberoSelected) || Objects.isNull(clienteSelected) || Objects.isNull(servicioSelected)) {
            alert("Error", "Debe seleccionar todos los campos", Alert.AlertType.ERROR);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaHora = fechaHoraPicker.getValue().atStartOfDay();
        String estado = estadoField.getText();
        String notas = notasField.getText();

        Cita newCita = new Cita(0,
                barberoSelected.getId(), barberoSelected.getNombre(),
                clienteSelected.getId(), clienteSelected.getNombre(),
                servicioSelected.getId(), servicioSelected.getNombre(),
                fechaHora, estado, notas);
        try {
            Cita insertedCita = citaRepository.addCita(newCita);
            if (insertedCita != null) {
                appointmentList.add(insertedCita);
                alert("Cita creada", "La cita ha sido registrada correctamente", Alert.AlertType.CONFIRMATION);

            } else {
                alert("Error", "Error al crear la cita", Alert.AlertType.ERROR);
            }

        } catch (SQLException e) {
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
