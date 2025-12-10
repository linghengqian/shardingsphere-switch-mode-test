package io.github.linghengqian;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.etcd.jetcd.test.EtcdClusterExtension;
import org.apache.curator.test.TestingServer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@SuppressWarnings("SqlNoDataSourceInspection")
public class SimpleTest {

    @RegisterExtension
    public static final EtcdClusterExtension CLUSTER = EtcdClusterExtension.builder().withNodes(1).withMountDirectory(false).build();

    private final String systemPropKeyPrefix = "fixture.test-native.";

    private DataSource logicDataSource;

    @Test
    void assertModes() throws Exception {
        this.assertZookeeper();
        this.assertEtcd();
    }

    void assertEtcd() throws SQLException {
        assertThat(System.getProperty(systemPropKeyPrefix + "server-lists"), is(nullValue()));
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.apache.shardingsphere.driver.ShardingSphereDriver");
        config.setJdbcUrl("jdbc:shardingsphere:classpath:etcd.yaml?placeholder-type=system_props");
        System.setProperty(systemPropKeyPrefix + "server-lists", CLUSTER.clientEndpoints().getFirst().toString());
        logicDataSource = new HikariDataSource(config);
        this.initEnvironment(new OrderRepository(logicDataSource));
        ResourceUtils.closeJdbcDataSource(logicDataSource);
        System.clearProperty(systemPropKeyPrefix + "server-lists");
    }

    void assertZookeeper() throws Exception {
        assertThat(System.getProperty(systemPropKeyPrefix + "server-lists"), is(nullValue()));
        try (TestingServer testingServer = new TestingServer()) {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.apache.shardingsphere.driver.ShardingSphereDriver");
            config.setJdbcUrl("jdbc:shardingsphere:classpath:zookeeper.yaml?placeholder-type=system_props");
            System.setProperty(systemPropKeyPrefix + "server-lists", testingServer.getConnectString());
            logicDataSource = new HikariDataSource(config);
            this.initEnvironment(new OrderRepository(logicDataSource));
            ResourceUtils.closeJdbcDataSource(logicDataSource);
        }
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
}
