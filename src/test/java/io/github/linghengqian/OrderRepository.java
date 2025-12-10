package io.github.linghengqian;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public final class OrderRepository {

    private final DataSource dataSource;

    public OrderRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createTableIfNotExistsInMySQL() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS t_order\
                    (order_id BIGINT NOT NULL AUTO_INCREMENT,order_type INT(11),\
                    user_id INT NOT NULL,address_id BIGINT NOT NULL,status VARCHAR(50),\
                    PRIMARY KEY (order_id))""");
        }
    }

    public void truncateTable() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE t_order");
        }
    }
}
