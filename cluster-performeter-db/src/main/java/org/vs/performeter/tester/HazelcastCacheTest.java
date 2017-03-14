package org.vs.performeter.tester;

import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.Probe;
import org.vs.performeter.data.collision.CollisionStatistics;
import org.vs.performeter.data.collision.CollisionStatisticsBuilderImpl;
import org.vs.performeter.data.providers.DBReader;
import org.vs.performeter.data.providers.DBReaderProvider;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Denis Karpov on 14.09.2015.
 */
@Component
@ConfigurationProperties(prefix = "performeter.db")
public class HazelcastCacheTest extends AbstractTester<CollisionStatistics, CollisionStatisticsBuilderImpl> {
    private static Logger LOG = LoggerFactory.getLogger(HazelcastCacheTest.class);

    private long cnt = 0;

    private String dstDriverClass;
    private String dstURL;
    private String dstUserID;
    private String dstPassword;
    private String insertStatement;
    private String searchStatement;


    @Resource(name = "DBReaderProvider")
    protected DataProvider<Probe> provider;
    private volatile boolean started;
    private Connection dstConn;
    private PreparedStatement insert;
    private PreparedStatement search;

    public DataProvider<Probe> getProvider() {
        return provider;
    }

    public void setProvider(DBReaderProvider provider) {
        this.provider = provider;
    }

    @Override
    public void beforeTests() {
        try {
            dstConn = DBReader.connect(dstDriverClass, dstURL, dstUserID, dstPassword);
            if (dstConn == null) throw new RuntimeException("Null dst connection");
            ;
            dstConn.setAutoCommit(true);
            insert = dstConn.prepareStatement(insertStatement);
            search = dstConn.prepareStatement(searchStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        provider.open(containerManager.getId());
        LOG.info("open done containerManager.getId()=", containerManager.getId());
//        super.beforeTests();
    }

    @Override
    public void afterTests() {
//        super.afterTests();
        LOG.info("afterTests called.");
        provider.close();
        if (insert != null) try {
            insert.close();
            insert=null;
        } catch (SQLException e) {
            LOG.error("Error closing insert", e);
        }

        if (search != null) try {
            search.close();
            search=null;
        } catch (SQLException e) {
            LOG.error("Error closing search", e);
        }

        if (dstConn != null) try {
            dstConn.close();
            dstConn=null;
        } catch (SQLException ex) {
            LOG.error("Error closing destination connection", ex);
        }
    }

    @Override
    public void doSingleTest() {
        Probe probe = provider.nextData();
        if (!started) {
            started = true;
            super.beforeTests();
        }
        if (probe == null || probe == Probe.END_PROBE || probe == Probe.ERROR_PROBE) {
            statisticsBuilder.stop();
            containerManager.stop();
            return;
        }

        statisticsBuilder.countPlusPlus();
        Object key = probe.getKey();

        ResultSet resultSet = null;
        try {
            search.setObject(1, key);
            resultSet = search.executeQuery();
            if (resultSet.next()) {
                statisticsBuilder.collisionPlusPlus();
                return;
            }
            resultSet.close();
        } catch (SQLException e) {
            LOG.error("Error searching key", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        try {
            insert.setObject(1, key);
            insert.execute();
//            LOG.info("Inserted records {}", insert.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
            afterTests();
            throw new RuntimeException(e);
        } finally {
        }
        cnt++;
        if (cnt % 10000 == 0) {
            LOG.info("testMap.size = {}", cnt);
        }
    }

    public String getDstDriverClass() {
        return dstDriverClass;
    }

    public void setDstDriverClass(String dstDriverClass) {
        this.dstDriverClass = dstDriverClass;
    }

    public String getDstURL() {
        return dstURL;
    }

    public void setDstURL(String dstURL) {
        this.dstURL = dstURL;
    }

    public String getDstUserID() {
        return dstUserID;
    }

    public void setDstUserID(String dstUserID) {
        this.dstUserID = dstUserID;
    }

    public String getDstPassword() {
        return dstPassword;
    }

    public void setDstPassword(String dstPassword) {
        this.dstPassword = dstPassword;
    }

    public String getInsertStatement() {
        return insertStatement;
    }

    public void setInsertStatement(String insertStatement) {
        this.insertStatement = insertStatement;
    }

    public String getSearchStatement() {
        return searchStatement;
    }

    public void setSearchStatement(String searchStatement) {
        this.searchStatement = searchStatement;
    }
}
