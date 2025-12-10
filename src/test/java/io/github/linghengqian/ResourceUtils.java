package io.github.linghengqian;

import org.apache.shardingsphere.database.connector.core.DefaultDatabase;
import org.apache.shardingsphere.driver.jdbc.core.connection.ShardingSphereConnection;
import org.apache.shardingsphere.infra.metadata.database.resource.unit.StorageUnit;
import org.apache.shardingsphere.mode.manager.ContextManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ResourceUtils {

    public static void closeJdbcDataSource(final DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            ContextManager contextManager = connection.unwrap(ShardingSphereConnection.class).getContextManager();
            contextManager.getStorageUnits(DefaultDatabase.LOGIC_NAME).values().stream().map(StorageUnit::getDataSource).forEach(ResourceUtils::close);
            contextManager.close();
        }
    }

    private static void close(final DataSource dataSource) {
        if (dataSource instanceof AutoCloseable) {
            try {
                ((AutoCloseable) dataSource).close();
            } catch (final Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
