package Models;

import ValidatorHelp.FileProcessingException;
import java.io.*;
import java.nio.file.*;
import java.util.*;

//this class handle functionalities that related to participant class
public class ParticipantRepository {
    private final String systemFile;
    private final List<Participant> participants = new ArrayList<>();
    private int maxId = 0;

    //constructor
    public ParticipantRepository(String systemFile) {
        this.systemFile = systemFile;
        loadSystemFile(); //preload CSV
    }

    //---------Core Methods--------------
    //this will return um-modifiable participants list.
    public List<Participant> getAll() {
        return Collections.unmodifiableList(participants);
    }

    //to add new participant to memory and CSV
    public void addParticipant(Participant p) throws FileProcessingException {
        participants.add(p);
        updateMaxId(p.getId());
        appendToFile(systemFile, p.toCSVRow());
    }

    //import participants from CSV file.
    public int[] importExternalFile(String filePath) throws FileProcessingException {
        if (!Files.exists(Path.of(filePath)))
            throw new FileProcessingException("File " + filePath + " does not exist");

        //else
        participants.clear(); //clear current participant list(memory)

        int imported = 0;
        int ignored = 0;


        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            //this will go through every line
            while ((line = br.readLine()) != null) {
                try {
                    Participant p = Participant.parseLine(line);
                    //not participant object null && not email taken
                    if (p != null && !isEmailTaken(p.getEmail())) {
                        participants.add(p);
                        updateMaxId(p.getId());
                        imported++;
                    } else {
                        ignored++;
                    }
                } catch (Exception e) {
                    ignored++;
                }
            }
        } catch (IOException e) {
            throw new FileProcessingException("Error reading external file: " + e.getMessage());
        }
        return new int[]{imported, ignored};
    }

    // --------------Helpers-----------------
    public String generateNextId() {
        return String.format("P%03d", maxId + 1);
    }

    public boolean isEmailTaken(String email) {
        String lowerCaseEmail = email.toLowerCase();
        return participants.stream().anyMatch(
                p -> p.getEmail().toLowerCase().equals(lowerCaseEmail));
    }

    public Optional<Participant> findByEmail(String email) {
        String lowerCaseEmail = email.toLowerCase();
        return participants.stream().filter(
                p -> p.getEmail().toLowerCase().equals(lowerCaseEmail)).findFirst();
    }

    public void loadSystemFile() {
        try {
            importExternalFile(systemFile);
        } catch (FileProcessingException ignored) {
            //if the system file does not exist, at the first run
        }
    }

    private void updateMaxId(String id) {
        try {
            int num = Integer.parseInt(id.substring(1));
            if (num > maxId)
                maxId = num;
        } catch (Exception ignored) {}
    }

    private void appendToFile(String fileName, String data) throws FileProcessingException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(data);
            bw.newLine();
        } catch (IOException e) {
            throw new FileProcessingException("Failed to save to file.");
        }
    }
}