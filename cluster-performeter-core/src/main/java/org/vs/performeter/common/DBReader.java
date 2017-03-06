package org.vs.performeter.common;

import com.tmax.tibero.jdbc.data.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
@ConfigurationProperties(prefix = "pump")
public class DBReader<P extends org.vs.performeter.common.Probe> {
    private static Logger LOGGER = LoggerFactory.getLogger(DBReader.class);
    private static final String PARAMETER_PLACEHOLDER = "?";
    private static final String ROW_BY_ROW_MODE = "row-by-row";
    private static final String PREFECH_MODE = "prefetch";

    protected String srcDriverClass;
    protected String srcURL;
    protected String srcUserID;
    protected String srcPassword;
    protected String srcStatement;
    protected Calendar srcCalendar;
    protected boolean srcIsOraSP;
    protected boolean srcNoBindVar;

    protected String pumpMode = ROW_BY_ROW_MODE;
    protected boolean convertDates = false;
    protected Function<P, Boolean> consumer;
    protected ProbeFactory<P> factory;

    public void setSrcDriverClass(String srcDriverClass) {
        this.srcDriverClass = srcDriverClass;
    }

    public void setSrcURL(String srcURL) {
        this.srcURL = srcURL;
    }

    public void setSrcUserID(String srcUserID) {
        this.srcUserID = srcUserID;
    }

    public void setSrcPassword(String srcPassword) {
        this.srcPassword = srcPassword;
    }

    public void setSrcStatement(String srcStatement) {
        this.srcStatement = srcStatement;
    }

    public void setSrcCalendar(Calendar srcCalendar) {
        this.srcCalendar = srcCalendar;
    }

    public void setSrcIsOraSP(boolean srcIsOraSP) {
        this.srcIsOraSP = srcIsOraSP;
    }

    public void setSrcNoBindVar(boolean srcNoBindVar) {
        this.srcNoBindVar = srcNoBindVar;
    }

    public void setPumpMode(String pumpMode) {
        this.pumpMode = pumpMode;
    }

    public void setConvertDates(boolean convertDates) {
        this.convertDates = convertDates;
    }

    public void setConsumer(Function<P, Boolean> consumer) {
        this.consumer = consumer;
    }

    public void setFactory(ProbeFactory<P> factory) {
        this.factory = factory;
    }

    public boolean pump(String... inputParams) {
        long rowCount;

        Connection srcConn = null;

        try {
            srcConn = connect(srcDriverClass, srcURL, srcUserID, srcPassword);
            if (srcConn == null) return false;

            LOGGER.info("Connected to: " + srcURL);

            PreparedStatement srcStm;
            CallableStatement srcCallStm = null;
            ResultSet rs;

            //1. Prepare call
            String statementText = srcStatement;
            if (srcNoBindVar) {
                //Embed all input parameters directly into statement
                //Don't use bind variables
                int parametersToSkip = 0;
                if (srcIsOraSP) {
                    parametersToSkip = 1;
                }
                statementText = susbstituteParameters(statementText, inputParams, parametersToSkip);
            }

            int paramOffset = 1;
            if (srcIsOraSP) {
                srcCallStm = srcConn.prepareCall(statementText);
                srcStm = srcCallStm;
                LOGGER.info("Source is Oracle Stored Procedure");
                //1. Set input param
                srcCallStm.registerOutParameter(1, DataType.CURSOR);
                paramOffset = 2;
            } else {
                srcStm = srcConn.prepareStatement(statementText);
            }

            LOGGER.info("Prepared source statement: '" + statementText + "'");

            //2. Set input params if have any and we don't substitute them
            //directly into statement
            if (inputParams != null && !srcNoBindVar) {
                for (int i = 0; i < inputParams.length; i++) {
                    srcStm.setString(paramOffset + i, inputParams[i]);
                }
                LOGGER.info("Bound parameters");
            }

            LOGGER.info("Executing source statement.. ");
            if (srcIsOraSP) {
                if (srcCallStm != null) {
                    srcCallStm.execute();
                }
                rs = (ResultSet) (srcCallStm != null ? srcCallStm.getObject(1) : null);
            } else {
                rs = srcStm.executeQuery();
            }

            try {
                rs.setFetchSize(10000);
                LOGGER.debug("FetchSize set to 10000");
            } catch (Throwable ex) {
                LOGGER.debug("Seems " + srcDriverClass + " doesn't support setFetchSize()");
            }

            //2. Pump data
            LOGGER.info("Pump mode is: " + pumpMode);

            if (ROW_BY_ROW_MODE.equals(pumpMode)) {
                rowCount = pumpRowByRow(rs);
            } else if (PREFECH_MODE.equals(pumpMode)) {
                rowCount = prefetchThenPump(rs);
            } else {
                throw new IllegalStateException("Wrong pump mode specified: " + pumpMode);
            }

            closeDst();

            LOGGER.info("Done. Processed " + rowCount + " rows");
            return true;

        } catch (Exception ex) {
            LOGGER.error("Fatal copy error", ex);
            return false;
        } finally {
            if (srcConn != null)
                try {
                    srcConn.close();
                } catch (SQLException ex) {
                    LOGGER.error("Error closing source connection", ex);
                }
        }
    }


    private static Connection connect(String driverClass, String jdbcUrl, String userId, String password) throws SQLException {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException ex) {
            LOGGER.error("Driver class not found", ex);
            return null;
        }

        return DriverManager.getConnection(jdbcUrl, userId, password);
    }

    private static String susbstituteParameters(
            String template, String[] parameters, int parametersToSkipCount
    ) {
        int currentParameter = 0;
        int prevPlaceholder = 0;
        int placeholderIndex = template.indexOf(PARAMETER_PLACEHOLDER);
        StringBuilder result = new StringBuilder();

        while (placeholderIndex >= 0) {
            String parameterToAppend;
            if (parametersToSkipCount > 0) {
                parameterToAppend = PARAMETER_PLACEHOLDER;
                parametersToSkipCount--;
            } else {
                parameterToAppend = parameters[currentParameter++];
            }
            result.append(template.substring(prevPlaceholder, placeholderIndex));
            result.append(parameterToAppend);
            prevPlaceholder = placeholderIndex + PARAMETER_PLACEHOLDER.length();
            placeholderIndex = template.indexOf(PARAMETER_PLACEHOLDER, prevPlaceholder);
        }

        //append rest
        result.append(template.substring(prevPlaceholder));

        return result.toString();
    }


    private long pumpRowByRow(ResultSet rs) {
        try {
            LOGGER.info("Going to pump data");

            ResultSetMetaData rsMetaData = rs.getMetaData();
            //Use parameters count from dst statement to
            //transparently handle situations when new columns were added to the end of dst resulset
            //(this often happens)
            //int colsNum = dstStm.getParameterMetaData().getParameterCount(); //Not implemented in Sybase jConnect

            long rowCount = 0;
            while (rs.next()) {
                rowCount++;
                if (consumer.apply(readRow(rs, rsMetaData))) {
                    if (rowCount % 10000 == 0) LOGGER.info(rowCount + " rows processed");
                } else {
                    LOGGER.info("Signal to finish loading");
                    return rowCount;
                }
            }
            consumer.apply((P) Probe.END_PROBE);
            return rowCount;
        } catch (Exception ex) {
            ex.printStackTrace();
            consumer.apply((P) Probe.ERROR_PROBE);
            throw new RuntimeException(ex);
        }
    }

    private long prefetchThenPump(ResultSet rs) {
        Object[] rowData = null;

        try {
            LOGGER.info("Going to prefetch data");

            ResultSetMetaData rsMetaData = rs.getMetaData();
            List<P> rows = new ArrayList<>();

            //Prefetch data into memory
            long rowCount = 0;
            while (rs.next()) {
                rows.add(readRow(rs, rsMetaData));
                rowCount++;
                if (rowCount % 10000 == 0) {
                    LOGGER.info(rowCount + " rows read");
                }
            }

            //Upload data to consumer
            LOGGER.info("Going to upload data");

            rowCount = 0;
            for (P row : rows) {
                rowCount++;
                if (consumer.apply(row)) {
                    rowCount++;
                    if (rowCount % 10000 == 0) LOGGER.info(rowCount + " rows consumed");
                } else {
                    consumer.apply((P) Probe.ERROR_PROBE);
                    return rowCount;
                }
            }
            consumer.apply((P) Probe.END_PROBE);

            return rowCount;

        } catch (Exception ex) {
            ex.printStackTrace();
            consumer.apply((P) Probe.ERROR_PROBE);
            throw new RuntimeException(ex);
        }
    }

    protected P readRow(ResultSet rs, ResultSetMetaData rsMetaData) throws SQLException {
        for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
            int type = rsMetaData.getColumnType(i);
            String columnName = rsMetaData.getColumnName(i);
            if (type == DataType.DATE || type == DataType.TIMESTAMP || type == DataType.TIMESTAMP_TZ || type == DataType.TIMESTAMP_LTZ) {
                Timestamp ts;
                if (convertDates) {
                    ts = rs.getTimestamp(i, srcCalendar);
                } else {
                    ts = rs.getTimestamp(i);
                }
                if (ts != null)
                    factory.setTimestamp(columnName, ts.toLocalDateTime());
                else
                    factory.setNull(columnName, type);
            } else {
                factory.setObject(columnName, rs.getObject(i), rsMetaData.getColumnType(i));
            }
        }
        return factory.create();
    }

    protected void closeDst() {
    }

    public void stop() {

    }
}
