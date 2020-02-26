package org.learning.managers;

import org.learning.api.Habitable;
import org.learning.model.*;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class HabitableBuildingsManagerImplementation implements org.learning.api.HabitableBuildingsManager {

    private DataSource dataSource;

    public HabitableBuildingsManagerImplementation(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void addToDB(Habitable building) throws HabitableBuildingException {

        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO HabitableBuildings (MaxInhabitants, CurrentInhabitants, Size, Coordinates, Type, Active) VALUES ?, ?, ?, ?, ?, ?")){
                statement.    setInt(1, building.getMaxInhabitants());
                statement.    setInt(2, building.getCurrentInhabitants());
                statement.    setInt(3, building.getSize());
                statement. setString(4, building.getCoordinates());
                statement. setString(5, building.getType().toString());
                statement.setBoolean(6, building.getBuildingsState());

                statement.executeUpdate();

            } catch (SQLException e) {
                throw new HabitableBuildingException("Problem with connection", e);
            }

        } catch (SQLException e) {
            throw new HabitableBuildingException("Problem with connection", e);
        }
    }

    @Override
    public void deleteFromDB(Habitable building) throws HabitableBuildingException {

        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM HabitableBuildings WHERE Coordinates = ?")) {
                statement.setString(1, building.getCoordinates());
                statement.executeUpdate();

            } catch (SQLException e) {
                throw new HabitableBuildingException("Problem with connection", e);
            }

        } catch (SQLException e) {
            throw new HabitableBuildingException("Problem with connection", e);
        }

    }

    @Override
    public HabitableBuilding SetToHabitableBuilding(ResultSet resultSet) throws SQLException {
        String type = resultSet.getString("Type");
        HabitableBuildingTypes TypeOfBuilding;

        switch(type){
            case "CASTLE":
                TypeOfBuilding = HabitableBuildingTypes.CASTLE;
                break;

            case "HOTEL":
                TypeOfBuilding = HabitableBuildingTypes.HOTEL;
                break;

            case "SINGLEFAMILYHOUSE":
                TypeOfBuilding = HabitableBuildingTypes.SINGLEFAMILYHOUSE;
                break;

            case "PALACE":
                TypeOfBuilding = HabitableBuildingTypes.PALACE;
                break;

            case "COTTAGE":
                TypeOfBuilding = HabitableBuildingTypes.COTTAGE;
                break;

            case "HOUSEBOAT":
                TypeOfBuilding = HabitableBuildingTypes.HOUSEBOAT;
                break;

            case "RESIDENCE":
                TypeOfBuilding = HabitableBuildingTypes.RESIDENCE;
                break;

            case "FORT":
                TypeOfBuilding = HabitableBuildingTypes.FORT;
                break;

            case "MULTIFAMILYHOUSE":
                TypeOfBuilding = HabitableBuildingTypes.MULTIFAMILYHOUSE;
                break;

            case "UNDERGROUNDHOUSE":
                TypeOfBuilding = HabitableBuildingTypes.UNDERGROUNDHOUSE;
                break;

            case "MANSION":
                TypeOfBuilding = HabitableBuildingTypes.MANSION;
                break;

            case "TINYHOME":
                TypeOfBuilding = HabitableBuildingTypes.TINYHOME;
                break;

            case "BUNGALOW":
                TypeOfBuilding = HabitableBuildingTypes.BUNGALOW;
                break;

            default:
                TypeOfBuilding = null;
        }

        return new HabitableBuilding(
                resultSet.getInt("MaxInhabitants"),
                resultSet.getInt("CurrentInhabitants"),
                TypeOfBuilding,
                resultSet.getInt("Size"),
                resultSet.getString("Coordinates"),
                resultSet.getBoolean("Active"));
    }


    @Override
    public HabitableBuilding selectFromDBbyLocation(Array location) throws HabitableBuildingException {
        try (Connection connection = dataSource.getConnection()){
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM NHabitableBuildings WHERE Coordinates = ?")){
                statement.setArray(1, location);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()){
                    return SetToHabitableBuilding(resultSet);
                }
                else return null;

            } catch (SQLException e) {
                throw new HabitableBuildingException("Problem with connection", e);
            }
        } catch (SQLException e) {
            throw new HabitableBuildingException("Problem with connection", e);
        }
    }

    @Override
    public ArrayList<HabitableBuilding> selectFromDBbyType(HabitableBuildingTypes type) throws HabitableBuildingException {
        ArrayList<HabitableBuilding> output = new ArrayList<>();

        try  (Connection connection = dataSource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM NonResidentialBuildings WHERE Type = ?")){
                statement.setObject(1, type);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()){
                    output.add(SetToHabitableBuilding(resultSet));
                }
                return output;

            } catch (SQLException e) {
                throw new HabitableBuildingException("Problem with connection", e);
            }
        } catch (SQLException e) {
            throw new HabitableBuildingException("Problem with connection", e);
        }
    }
}
