package org.vs.performeter.data.providers;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.vs.performeter.data.DataProvider;
import org.vs.performeter.data.Probe;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Denis Karpov on 06.03.2017.
 */
@Component
@ConfigurationProperties(prefix = "dataProvider")
public class IsoFileDataProvider implements DataProvider<Probe> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    BufferedReader reader;
    CsvParser parser;
    public String baseFileNameTemplate; // = "D:\\Download\\Redis_data\\data{0}.csv";
    public String envFileName;
    private byte[] data;
//    private String baseFileNameTemplate;// = "/usr/cluster-performer/Redis_data/data{0}.csv";


    public String getEnvFileName() {
        return envFileName;
    }

    public void setEnvFileName(String envFileName) {
        this.envFileName = envFileName;
    }

    public String getBaseFileNameTemplate() {
        return baseFileNameTemplate;
    }

    public void setBaseFileNameTemplate(String baseFileNameTemplate) {
        this.baseFileNameTemplate = baseFileNameTemplate;
    }

    @Override
    public void open(int instanceId) {
        try {
            close();

            if (envFileName!=null) {
                Path path = Paths.get(envFileName);
                data = Files.readAllBytes(path);
            }
            Charset charset = Charset.forName("windows-1251");
            String srcFileName = MessageFormat.format(baseFileNameTemplate, instanceId);
            Path srcPath = Paths.get(srcFileName);
            reader = Files.newBufferedReader(srcPath, charset);

            CsvParserSettings csvParserSettings = new CsvParserSettings();
            csvParserSettings.getFormat().setLineSeparator("\n");
            csvParserSettings.getFormat().setQuote('\'');
            parser = new CsvParser(csvParserSettings);
            parser.beginParsing(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (parser != null) {
            parser.stopParsing();
            parser = null;
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader = null;
        }
    }

    @Override
    public synchronized Probe nextData() {
        Probe res = null;
        if (parser != null) {
            String[] line = parser.parseNext();
            if (line != null) {
                res = createObj(line);
            }
        }
        if (data!=null && res!=null){
            res.data = data.clone();
        }
        return res;
    }

    private Probe createObj(String[] line) {
        LocalDateTime dateTime = LocalDateTime.parse(line[0], formatter);
        return new Probe(
                dateTime,
                line[1],
                line[2],
                line[3],
                line[4],
                line[5],
                line[6],
                line[7],
                line[8],
                line[9],
                line[10]
        );
    }
}
