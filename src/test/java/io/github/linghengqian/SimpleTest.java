package io.github.linghengqian;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.etcd.jetcd.test.EtcdClusterExtension;
import org.apache.curator.test.TestingServer;
import org.apache.shardingsphere.database.connector.core.DefaultDatabase;
import org.apache.shardingsphere.driver.jdbc.core.connection.ShardingSphereConnection;
import org.apache.shardingsphere.infra.metadata.database.resource.unit.StorageUnit;
import org.apache.shardingsphere.mode.manager.ContextManager;
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
class SimpleTest {

    @RegisterExtension
    public static final EtcdClusterExtension CLUSTER = EtcdClusterExtension.builder().withNodes(1).withMountDirectory(false).build();

    private DataSource logicDataSource;

    @Test
    void assertModes() throws Exception {
        this.assertEtcd();
        this.assertZookeeper();
    }

    void assertEtcd() throws SQLException {
        String systemPropKeyPrefix = "fixture.test-native.yaml.mode.cluster.etcd.";
        assertThat(System.getProperty(systemPropKeyPrefix + "server-lists"), is(nullValue()));
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.apache.shardingsphere.driver.ShardingSphereDriver");
        config.setJdbcUrl("jdbc:shardingsphere:classpath:etcd.yaml?placeholder-type=system_props");
        System.setProperty(systemPropKeyPrefix + "server-lists", CLUSTER.clientEndpoints().getFirst().toString());
        logicDataSource = new HikariDataSource(config);
        this.initEnvironment(logicDataSource);
        this.closeJdbcDataSource(logicDataSource);
        System.clearProperty(systemPropKeyPrefix + "server-lists");
    }

    void assertZookeeper() throws Exception {
        String systemPropKeyPrefix = "fixture.test-native.yaml.mode.cluster.zookeeper.";
        assertThat(System.getProperty(systemPropKeyPrefix + "server-lists"), is(nullValue()));
        try (TestingServer testingServer = new TestingServer()) {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.apache.shardingsphere.driver.ShardingSphereDriver");
            config.setJdbcUrl("jdbc:shardingsphere:classpath:zookeeper.yaml?placeholder-type=system_props");
            System.setProperty(systemPropKeyPrefix + "server-lists", testingServer.getConnectString());
            logicDataSource = new HikariDataSource(config);
            this.initEnvironment(logicDataSource);
            this.closeJdbcDataSource(logicDataSource);
        }
        System.clearProperty(systemPropKeyPrefix + "server-lists");
    }

    private void initEnvironment(DataSource logicDataSource) throws SQLException {
        this.doSql(logicDataSource, """
                CREATE TABLE IF NOT EXISTS t_order\
                (order_id BIGINT NOT NULL AUTO_INCREMENT,order_type INT(11),\
                user_id INT NOT NULL,address_id BIGINT NOT NULL,status VARCHAR(50),\
                PRIMARY KEY (order_id))""");
        this.doSql(logicDataSource, """
                CREATE TABLE IF NOT EXISTS t_order_item\
                (order_item_id BIGINT NOT NULL AUTO_INCREMENT,order_id BIGINT NOT NULL,\
                user_id INT NOT NULL,phone VARCHAR(50),status VARCHAR(50),PRIMARY KEY (order_item_id))
                """);
        this.doSql(logicDataSource, """
                CREATE TABLE IF NOT EXISTS t_address (address_id BIGINT NOT NULL, \
                address_name VARCHAR(100) NOT NULL, PRIMARY KEY (address_id))
                """);
        Awaitility.await().atMost(Duration.ofMinutes(1L)).ignoreExceptions().until(() -> {
            this.doSql(logicDataSource, "SELECT * FROM t_order");
            this.doSql(logicDataSource, "SELECT * FROM t_order_item");
            this.doSql(logicDataSource, "SELECT * FROM t_address");
            return true;
        });
        this.doSql(logicDataSource, "TRUNCATE TABLE t_order");
        this.doSql(logicDataSource, "TRUNCATE TABLE t_order_item");
        this.doSql(logicDataSource, "TRUNCATE TABLE t_address");
        this.doSql(logicDataSource, "INSERT INTO t_order (user_id, order_type, address_id, status) VALUES (1, 0, 1, 'INSERT_TEST')");
        this.doSql(logicDataSource, "INSERT INTO t_order_item (order_id, user_id, phone, status) VALUES (114514, 1, '13800000001', 'INSERT_TEST')");
        this.doSql(logicDataSource, "INSERT INTO t_address (address_id, address_name) VALUES (1, 'address_test_1')");
        this.doSql(logicDataSource, "DELETE FROM t_order WHERE user_id=1");
        this.doSql(logicDataSource, "DELETE FROM t_order WHERE user_id=1");
        this.doSql(logicDataSource, "DELETE FROM t_order WHERE address_id=1");
        this.doSql(logicDataSource, "DROP TABLE IF EXISTS t_order");
        this.doSql(logicDataSource, "DROP TABLE IF EXISTS t_order_item");
        this.doSql(logicDataSource, "DROP TABLE IF EXISTS t_address");
    }

    private void doSql(DataSource logicDataSource, String sql) throws SQLException {
        try (Connection connection = logicDataSource.getConnection()) {
            connection.createStatement().execute(sql);
        }
    }

    private void closeJdbcDataSource(final DataSource logicDataSource) throws SQLException {
        try (Connection connection = logicDataSource.getConnection()) {
            ContextManager contextManager = connection.unwrap(ShardingSphereConnection.class).getContextManager();
            contextManager.getStorageUnits(DefaultDatabase.LOGIC_NAME)
                    .values()
                    .stream()
                    .map(StorageUnit::getDataSource)
                    .forEach(realDataSource -> {
                        if (realDataSource instanceof AutoCloseable) {
                            try {
                                ((AutoCloseable) realDataSource).close();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
            contextManager.close();
        }
    }
}
