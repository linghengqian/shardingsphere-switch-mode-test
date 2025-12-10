package io.github.linghengqian;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.etcd.jetcd.test.EtcdClusterExtension;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@SuppressWarnings("SqlNoDataSourceInspection")
public class SimpleTest {
    @RegisterExtension
    public static final EtcdClusterExtension CLUSTER = EtcdClusterExtension.builder().withNodes(1).withMountDirectory(false).build();
    private final String systemPropKeyPrefix = "fixture.test-native.yaml.mode.cluster.etcd.";
    private DataSource logicDataSource;

    @Test
    void assertEtcd() throws SQLException {
        assertThat(System.getProperty(systemPropKeyPrefix + "server-lists"), is(nullValue()));
        logicDataSource = createDataSource(CLUSTER.clientEndpoints());
        this.initEnvironment(new OrderRepository(logicDataSource));
        ResourceUtils.closeJdbcDataSource(logicDataSource);
        System.clearProperty(systemPropKeyPrefix + "server-lists");
    }

    private void initEnvironment(OrderRepository orderRepository) throws SQLException {
        orderRepository.createTableIfNotExistsInMySQL();
        Awaitility.await().atMost(Duration.ofMinutes(2L)).ignoreExceptions().until(() -> {
            try (Connection connection = logicDataSource.getConnection()) {
                connection.createStatement().execute("SELECT * FROM t_order");
            }
            return true;
        });
        orderRepository.truncateTable();
    }

    private DataSource createDataSource(final List<URI> clientEndpoints) {
        URI clientEndpoint = clientEndpoints.getFirst();
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.apache.shardingsphere.driver.ShardingSphereDriver");
        config.setJdbcUrl("jdbc:shardingsphere:classpath:etcd.yaml?placeholder-type=system_props");
        System.setProperty(systemPropKeyPrefix + "server-lists", clientEndpoint.toString());
        return new HikariDataSource(config);
    }
}
