package ec.edu.utpl.carreras.computacion.repository;

import ec.edu.utpl.carreras.computacion.model.Cita;
import ec.edu.utpl.carreras.computacion.model.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaRepository {
    public List<Cita> getAllCitas() throws SQLException {
        List<Cita> citas = new ArrayList<>();
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
        try (Connection conn = DBConnection.getInstance().getConnection();
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
                citas.add(cita);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al consultar los datos", e);
        }
        return citas;
    }

    public Cita addCita(Cita cita) throws SQLException {
        String insert = "INSERT INTO  cita (barbero_id, cliente_id, servicio_id, fecha_hora, estado, notas) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, cita.getBarberoId());
            pstmt.setInt(2, cita.getClienteId());
            pstmt.setInt(3, cita.getServicioId());
            pstmt.setTimestamp(4, Timestamp.valueOf(cita.getFechaHora()));
            pstmt.setString(5, cita.getEstado());
            pstmt.setString(6, cita.getNotas());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear la cita. Cita no creada");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cita.setId(generatedKeys.getInt(1));
                    return cita;
                } else {
                    throw new SQLException("Error al crear la cita. No se pudo generar el ID.");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al crear la cita", e);
        }
    }
}