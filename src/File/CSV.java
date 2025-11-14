package File;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class CSV {

    //Appends a new row to the CSV file. Creates file if it does not exist.
    public static void appendRow(String filename, List<String> cols) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(filename),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(toCsvLine(cols));
            bw.newLine();
        }
    }

    //Converts a list of columns to a properly escaped CSV line.
    public static String toCsvLine(List<String> cols) {
        return cols.stream().map(CSV::escape).collect(Collectors.joining(","));
    }

    //Escapes a single value for CSV (handles commas, quotes, newlines).
    public static String escape(String s) {
        if (s == null) s = "";
        boolean needQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String out = s.replace("\"", "\"\"");
        return needQuotes ? "\"" + out + "\"" : out;
    }

}
