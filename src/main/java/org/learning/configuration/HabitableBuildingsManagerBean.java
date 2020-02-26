package org.learning.configuration;

import org.h2.jdbcx.JdbcDataSource;
import org.learning.managers.HabitableBuildingsManagerImplementation;
import org.learning.managers.NonResidentialBuildingsManagerImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Configuration
public class HabitableBuildingsManagerBean {

    DataSource dataSource;

    @Bean
    public HabitableBuildingsManagerImplementation habitableBuildingsManagerImplementation() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
        ds.setUser("sa");
        ds.setPassword("sa");
        dataSource = ds;

        try (Connection conn = dataSource.getConnection()) {

            conn.prepareStatement("CREATE TABLE HabitableBuildings ("
                    + "MaxInhabitants INT NOT NULL, "
                    + "CurrentInhabitants INT NOT NULL, "
                    + "Size INT NOT NULL, "
                    + "Coordinates VARCHAR(255) NOT NULL, "
                    + "Type VARCHAR(255) NOT NULL, "
                    + "Active BOOLEAN NOT NULL, "
                    + "UNIQUE KEY Coordinates (Coordinates))").executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new HabitableBuildingsManagerImplementation(dataSource);
    }
}