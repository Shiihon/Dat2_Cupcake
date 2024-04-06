package app.persistence;

import app.entities.CupcakeBottom;
import app.entities.CupcakeTop;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupcakeMapper {

    public static List<CupcakeBottom> getCupcakeBottoms(ConnectionPool connectionPool) throws DatabaseException {
        List<CupcakeBottom> bottoms = new ArrayList<>();

        String sql = "SELECT cupcake_bottom_id, cupcake_bottom_name, cupcake_bottom_price FROM cupcake_bottoms";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int bottom_id = rs.getInt("cupcake_bottom_id");
                String bottom_name = rs.getString("cupcake_bottom_name");
                int bottom_price = rs.getInt("cupcake_bottom_price");

                bottoms.add(new CupcakeBottom(bottom_id, bottom_name, bottom_price));
            }
        }
        catch (SQLException e){
            throw new DatabaseException("Fejl i bottoms!", e.getMessage());
        }
        return bottoms;
    }

    public static List<CupcakeTop> getCupcakeTops(ConnectionPool connectionPool) throws DatabaseException {
        List<CupcakeTop> tops = new ArrayList<>();

        String sql = "SELECT cupcake_top_id, cupcake_top_name, cupcake_top_price FROM cupcake_tops";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int top_id = rs.getInt("cupcake_top_id");
                String top_name = rs.getString("cupcake_top_name");
                int top_price = rs.getInt("cupcake_top_price");

                tops.add(new CupcakeTop(top_id, top_name, top_price));
            }
        }
        catch (SQLException e){
            throw new DatabaseException("Fejl i bottoms!", e.getMessage());
        }
        return tops;
    }
}
