package io.github.jordanmartin.datagenerator.cli;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CliTest extends BaseCliTest {

    @Test
    void stdoutJson() throws Exception {
        String filepath = resourcePath("/cli/simple-def.yml");
        String result = cli("-d", filepath, "-f", "json", "-c", "3", "--stdout");
        assertEquals(
                "[{\"id\":0,\"name\":\"Bob\"},{\"id\":1,\"name\":\"Bob\"},{\"id\":2,\"name\":\"Bob\"}]",
                result
        );
    }

    @Test
    void stdoutCsv() throws Exception {
        String filepath = resourcePath("/cli/simple-def.yml");
        String result = cli("-d", filepath, "-f", "csv", "-c", "3", "--stdout");
        assertEquals("id;name\n" +
                "0;Bob\n" +
                "1;Bob\n" +
                "2;Bob\n", result);
    }

    @Test
    void gzipJsonFile() throws Exception {
        File tmpFile = getNewTempFile();
        String filepath = resourcePath("/cli/simple-def.yml");
        cli("-d", filepath, "-f", "json", "--gzip", "--table-name", "person", "-c", "3", "--out", tmpFile.getPath());
        GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(tmpFile));
        String ungzipped = new String(gzipInputStream.readAllBytes());
        assertEquals(
                "[{\"id\":0,\"name\":\"Bob\"},{\"id\":1,\"name\":\"Bob\"},{\"id\":2,\"name\":\"Bob\"}]",
                ungzipped
        );
    }

    @Test
    void jsonFile() throws Exception {
        File tmpFile = getNewTempFile();
        String filepath = resourcePath("/cli/simple-def.yml");
        cli("-d", filepath, "-f", "sql", "--table-name", "person", "-c", "3", "--out", tmpFile.getPath());
        assertEquals("INSERT INTO person(id,name) VALUE(0,'Bob');\n" +
                "INSERT INTO person(id,name) VALUE(1,'Bob');\n" +
                "INSERT INTO person(id,name) VALUE(2,'Bob');\n", getFileContent(tmpFile));
    }
}