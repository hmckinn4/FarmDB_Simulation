package com.solvd.farm.DAO.jdbcimpl;

import com.solvd.farm.DAO.CropDAO;
import com.solvd.farm.binary.Crop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CropDAOImpl extends AbstractDAO implements CropDAO {

    private static final Logger logger = LogManager.getLogger(CropDAOImpl.class);

    private static final String SQL_QUERY_CREATE_CROP = "INSERT INTO crop (Crop_Name, crop_variety, crop_Season) VALUES (?,?, ?)";

    private static final String SQL_QUERY_GET_CROP_BY_ID = "SELECT * FROM Crop WHERE Crop_ID = ?";

    private static final String SQL_QUERY_GET_ALL_CROPS = "SELECT * FROM crop";

    private static final String SQL_QUERY_UPDATE_CROP = "UPDATE crop SET Crop_Name = ?, crop_variety = ?, crop_Season = ? WHERE Crop_ID = ?";

    private static final String SQL_QUERY_DELETE_CROP = "DELETE FROM crop WHERE Crop_ID = ?";

    @Override
    public boolean create(Crop crop) {
        Connection connection = getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY_CREATE_CROP)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, crop.getName());
            preparedStatement.setString(2, crop.getVariety());
            preparedStatement.setString(3, crop.getGrowingSeason());
            //setting values
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result > 0;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return false;
        } finally {
            releaseConnection(connection);
        }
    }

    @Override
    public Crop getById(long id) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            logger.info("Getting crop with ID " + id);
            preparedStatement = connection.prepareStatement(SQL_QUERY_GET_CROP_BY_ID);
            preparedStatement.setLong(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                long cropId = resultSet.getLong("Crop_ID");
                String name = resultSet.getString("Crop_Name");
                String variety = resultSet.getString("Crop_Variety");
                String growingSeason = resultSet.getString("Crop_Season");
                logger.info("Crop with ID " + id + " found: " + name + " " + variety + " " + growingSeason);
                return new Crop(cropId, name, variety, growingSeason);
            } else {
                logger.info("Crop with ID " + id + " not found");
                return null;
            }

        } catch (SQLException e) {
            logger.error("Get crop by ID failed", e);
            return null;
        } finally {
            releaseConnection(connection);
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @Override
    public List<Crop> getAll() {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_QUERY_GET_ALL_CROPS);
            resultSet = preparedStatement.executeQuery();
            List<Crop> crops = new ArrayList<>();
            while (resultSet.next()) {
                long cropId = resultSet.getLong("Crop_ID");
                String name = resultSet.getString("Crop_Name");
                String variety = resultSet.getString("Crop_Variety");
                String growingSeason = resultSet.getString("Crop_Season");
                crops.add(new Crop(cropId, name, variety, growingSeason));
            }
            return crops;
        } catch (SQLException e) {
            logger.error("Get all Crops Query failed");
            return null;
        } finally {
            releaseConnection(connection);
            try {
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean update(Crop crop) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_QUERY_UPDATE_CROP);
            connection.setAutoCommit(false);
            preparedStatement.setString(1, crop.getName());
            preparedStatement.setString(2, crop.getVariety());
            preparedStatement.setString(3, crop.getGrowingSeason());
            preparedStatement.setLong(4, crop.getId());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            releaseConnection(connection);
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean delete(Crop crop) {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_QUERY_DELETE_CROP);
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, crop.getId());
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result > 0;
        } catch (SQLException e) {
            logger.error("Delete crop failed");
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return false;
        } finally {
            releaseConnection(connection);
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
