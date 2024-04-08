package app.persistence;
import app.entities.CupcakePart;
import app.exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupcakeMapper {

    public static List<CupcakePart> getCupcakeBottoms(ConnectionPool connectionPool) throws DatabaseException {
        List<CupcakePart> bottoms = new ArrayList<>();

        String sql = "SELECT cupcake_bottom_id, cupcake_bottom_name, cupcake_bottom_price FROM cupcake_bottoms";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int bottom_id = rs.getInt("cupcake_bottom_id");
                String bottom_name = rs.getString("cupcake_bottom_name");
                int bottom_price = rs.getInt("cupcake_bottom_price");

                bottoms.add(new CupcakePart(bottom_id, bottom_name, bottom_price, CupcakePart.Type.BOTTOM));
            }
        }
        catch (SQLException e){
            throw new DatabaseException("No bottoms found", e.getMessage());
        }
        return bottoms;
    }

    public static List<CupcakePart> getCupcakeTops(ConnectionPool connectionPool) throws DatabaseException {

        List<CupcakePart> tops = new ArrayList<>();

        String sql = "SELECT cupcake_top_id, cupcake_top_name, cupcake_top_price FROM cupcake_tops";

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int top_id = rs.getInt("cupcake_top_id");
                String top_name = rs.getString("cupcake_top_name");
                int top_price = rs.getInt("cupcake_top_price");

                tops.add(new CupcakePart(top_id, top_name, top_price, CupcakePart.Type.TOP));
            }
        }
        catch (SQLException e){
            throw new DatabaseException("No tops found.", e.getMessage());
        }
        return tops;
    }

    public static String getCupcakePartName(int partId, ConnectionPool connectionPool, CupcakePart.Type type) throws DatabaseException {

        String sql;

        if (type == CupcakePart.Type.BOTTOM) {
            sql = "SELECT cupcake_bottom_name FROM cupcake_bottoms WHERE cupcake_bottom_id = ?";
        } else { //if typt.top
            sql = "SELECT cupcake_top_name FROM cupcake_tops WHERE cupcake_top_id = ?";
        }

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, partId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return type == CupcakePart.Type.BOTTOM ? rs.getString("cupcake_bottom_name") : rs.getString("cupcake_top_name");
            } else {
                throw new DatabaseException("No cupcake part found with ID: " + partId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("No cupcake part name found.", e.getMessage());
        }
    }

    public static int getCupcakePartPrice(int partId, ConnectionPool connectionPool, CupcakePart.Type type) throws DatabaseException {
        String sql;
        if (type == CupcakePart.Type.BOTTOM) {
            sql = "SELECT cupcake_bottom_price FROM cupcake_bottoms WHERE cupcake_bottom_id = ?";
        } else { // Assuming Type.TOP
            sql = "SELECT cupcake_top_price FROM cupcake_tops WHERE cupcake_top_id = ?";
        }

        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, partId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return type == CupcakePart.Type.BOTTOM ? rs.getInt("cupcake_bottom_price") : rs.getInt("cupcake_top_price");
            } else {
                throw new DatabaseException("No cupcake part found with ID: " + partId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("No cupcake part price found.", e.getMessage());
        }
    }
}
