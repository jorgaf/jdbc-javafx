package ec.edu.utpl.carreras.computacion.repository;

import ec.edu.utpl.carreras.computacion.model.Barbero;
import ec.edu.utpl.carreras.computacion.model.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BarberoRepository {

    public List<Barbero> getAllBarberos() throws SQLException {
        List<Barbero> barberoList = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM barbero")) {

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
            return barberoList;

        } catch (SQLException e) {
            throw new SQLException("Error al consultar la lista de barberos", e);
        }
    }
}
