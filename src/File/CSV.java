package File;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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

    //Writes the header row in the CSV file (overwrites any existing content).
    public static void writeHeader(String filename, List<String> header) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(filename))) {
            bw.write(toCsvLine(header));
            bw.newLine();
        }
    }

    // getting CSV line into columns and handling quoted fields.
    public static List<String> parseLine(String line) {
        List<String> out = new ArrayList<>();
        if(line == null) return out;
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        sb.append('"');
                        i++; //escape quote
                    } else {
                        inQuotes = false;
                    }
                } else {
                    sb.append(c);
                }
            } else  {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    out.add(sb.toString());
                    sb.append(c);
                }
            }
        }
        out.add(sb.toString());
        return out;
    }

}
