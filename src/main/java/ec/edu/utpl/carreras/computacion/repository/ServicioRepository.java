package ec.edu.utpl.carreras.computacion.repository;

import ec.edu.utpl.carreras.computacion.model.DBConnection;
import ec.edu.utpl.carreras.computacion.model.Servicio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServicioRepository {
    public List<Servicio> getAllServicios() throws SQLException {
        List<Servicio> servicioList = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM servicio")) {

            while (rs.next()) {
                Servicio b = new Servicio(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("duracion")
                );
                servicioList.add(b);
            }
            return servicioList;
        } catch (SQLException e) {
            throw new SQLException("Error al consultar la lista de servicios", e);
        }
    }
}
