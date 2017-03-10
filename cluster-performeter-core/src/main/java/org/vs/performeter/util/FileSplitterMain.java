package org.vs.performeter.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

/**
 * Created by Denis Karpov on 10.03.2017.
 */
public class FileSplitterMain {
    static final Charset charset = Charset.forName("windows-1251");

    // Params
    static final String srcFileName = "d:\\Download\\Redis_data\\data.csv";
    static final String resFileNameTempl = "d:\\Download\\Redis_data\\data{0}.csv";
    static final int N = 3;

    public static void main(String[] args) throws IOException {
        Path srcPath = Paths.get(srcFileName);

        BufferedWriter[] writers = openWriters(N);
        try {
            try (BufferedReader reader = Files.newBufferedReader(srcPath, charset) ){
                String line = null;
                int lineNo = 0;
                BufferedWriter writer;
                while ((line = reader.readLine()) != null) {
                    writer = writers[lineNo++ % N];
                    writer.write(line);
                    writer.newLine();
                }
            } catch(IOException x){
                System.err.format("IOException: %s%n", x);
            }
        }
        finally {
            closeWriters(writers);
        }
    }

    private static void closeWriters(BufferedWriter[] writers) throws IOException {
        for (BufferedWriter w: writers){
            if (w!=null) w.close();
        }
    }

    public static BufferedWriter[] openWriters(int n) throws IOException {
        BufferedWriter[] res = new BufferedWriter[n];
        for (int i=0; i<n; i++){
            String resFileName = MessageFormat.format(resFileNameTempl, Integer.toString(i+1));
            Path resPath = Paths.get(resFileName);
            res[i] = Files.newBufferedWriter(resPath, charset);
        }
        return res;
    }
}
