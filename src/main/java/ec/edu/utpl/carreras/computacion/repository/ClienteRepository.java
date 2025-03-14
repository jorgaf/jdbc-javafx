package ec.edu.utpl.carreras.computacion.repository;

import ec.edu.utpl.carreras.computacion.model.Cliente;
import ec.edu.utpl.carreras.computacion.model.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    public List<Cliente> getAllClientes() throws SQLException {
        List<Cliente> clienteList = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM cliente")) {

            while (rs.next()) {
                Cliente b = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                clienteList.add(b);
            }
            return clienteList;

        } catch (SQLException e) {
            throw new SQLException("Error al consultar la lista de clientes", e);
        }
    }
}
