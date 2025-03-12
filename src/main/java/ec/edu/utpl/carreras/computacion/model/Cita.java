package ec.edu.utpl.carreras.computacion.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cita {
    private IntegerProperty id;
    private IntegerProperty barberoId;
    private IntegerProperty clienteId;
    private IntegerProperty servicioId;
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

    // Propiedades para TableView
    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }

    public int getBarberoId() { return barberoId.get(); }
    public IntegerProperty barberoProperty() { return barberoId; }

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

