package ec.edu.utpl.carreras.computacion.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cita {
    private IntegerProperty id;
    private IntegerProperty barberoId;
    private StringProperty barberoNombre;
    private IntegerProperty clienteId;
    private StringProperty clienteNombre;
    private IntegerProperty servicioId;
    private StringProperty servicioNombre;
    private ObjectProperty<LocalDateTime> fechaHora;
    private StringProperty estado;
    private StringProperty notas;


    public Cita(int id, int barberoId, int clienteId, int servicioId, LocalDateTime fechaHora, String estado, String notas) {
        this.id = new SimpleIntegerProperty(id);
        this.barberoId = new SimpleIntegerProperty(barberoId);
        this.clienteId = new SimpleIntegerProperty(clienteId);
        this.servicioId = new SimpleIntegerProperty(servicioId);
        this.fechaHora = new SimpleObjectProperty<>(fechaHora);
        this.estado = new SimpleStringProperty(estado);
        this.notas = new SimpleStringProperty(notas);
    }

    public Cita(int id,
                int barberoId, String barberoNombre,
                int clienteId, String clienteNombre,
                int servicioId, String servicioNombre,
                LocalDateTime fechaHora, String estado, String notas) {
        this.id = new SimpleIntegerProperty(id);
        this.barberoId = new SimpleIntegerProperty(barberoId);
        this.barberoNombre = new SimpleStringProperty(barberoNombre);
        this.clienteId = new SimpleIntegerProperty(clienteId);
        this.clienteNombre = new SimpleStringProperty(clienteNombre);
        this.servicioId = new SimpleIntegerProperty(servicioId);
        this.servicioNombre = new SimpleStringProperty(servicioNombre);
        this.fechaHora = new SimpleObjectProperty<>(fechaHora);
        this.estado = new SimpleStringProperty(estado);
        this.notas = new SimpleStringProperty(notas);
    }

    // Propiedades para TableView
    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public int getBarberoId() { return barberoId.get(); }
    public IntegerProperty barberoProperty() { return barberoId; }

    public StringProperty barberoNombreProperty() {
        return barberoNombre;
    }

    public StringProperty clienteNombrePrperty() {
        return clienteNombre;
    }

    public StringProperty servicioNombreProperty() {
        return servicioNombre;
    }

    public int getClienteId() { return clienteId.get(); }
    public IntegerProperty clienteProperty() { return clienteId; }

    public int getServicioId() { return servicioId.get(); }
    public IntegerProperty servicioProperty() { return servicioId; }

    public LocalDateTime getFechaHora() { return fechaHora.get(); }
    // Se formatea la fecha para mostrar en la tabla
    public StringProperty fechaHoraProperty() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new SimpleStringProperty(fechaHora.get().format(formatter));
    }

    public String getEstado() { return estado.get(); }
    public StringProperty estadoProperty() { return estado; }

    public String getNotas() { return notas.get(); }
    public StringProperty notasProperty() { return notas; }
}

