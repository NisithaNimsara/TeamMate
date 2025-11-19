package Models;

import ValidatorHelp.FileProcessingException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

// This class is responsible for:
// - Loading participants from the system CSV file at startup
// - Saving new participants to the CSV
// - Checking email uniqueness
// - Generating new IDs (P001, P002, ...)
public class ParticipantRepository {

    private final String systemFileName;                              // system CSV file name
    private final List<Participant> participants = new ArrayList<>(); // in-memory list

    private int maxIdNumber = 0; // tracks the highest numeric ID (1 from P001)

    //constructor
    public ParticipantRepository(String systemFileName) {
        this.systemFileName = systemFileName;
        loadFromSystemFile();   // to read CSV first
    }

    //get the next ID (generate)
    public String getNextId(){
        int next = maxIdNumber + 1;
        return String.format("P%03d", next);
    }

    // to check email correctness
    public boolean isValidEmail(String email){
        if (email != null  && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")){
            return true;
        } else  {
            return false;
        }
    }

    // to check email uniqueness
    public boolean isEmailTaken(String email){
        String lover = email.toLowerCase();
        return participants.stream().anyMatch(p -> p.getEmail().toLowerCase().equals(lover));
    }

    // Add new participant to memory and save to CSV.
    public  void addParticipant(Participant p) throws FileProcessingException{
        participants.add(p);
        updateMaxIdFromString(p.getId()); //this will keep id counter correct
        appendToSystemFile(p);            // write to CSV
    }

    // find participant by email
    public Optional<Participant> findByEmail(String email){
        String lower = email.toLowerCase();
        return participants.stream()
                .filter(p -> p.getEmail().toLowerCase().equals(lower))
                .findFirst();
    }

    //--------------------------------------------------------------------------
    // ------------Internal helpers---------------------------------------------

    // Loads all participants from system CSV at startup.
    private void loadFromSystemFile() {
        Path path = Path.of(systemFileName);

        if (!Files.exists(path)) {
            // if CSV not exist, program starts empty.
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(systemFileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                Participant p = parseLine(line);     // convert line into Participant

                if (p != null) {
                    participants.add(p);
                    updateMaxIdFromString(p.getId()); // update ID counter
                }
            }

            System.out.println("Preloaded " + participants.size() + " participants from " + systemFileName);

        } catch (IOException e) {
            System.out.println("Warning: could not preload participants: " + e.getMessage());
        }
    }


    // Parse one CSV line into a Participant object.
    private Participant parseLine(String line) {
        String[] parts = line.split(",");

        // Not enough columns then ignore line
        if (parts.length < 8) {
            return null;
        }

        // Header line check and ignore header row
        if (parts[0].trim().equalsIgnoreCase("ID")
                || parts[1].trim().equalsIgnoreCase("Name")
                || parts[4].trim().equalsIgnoreCase("SkillLevel")) {
            return null;
        }

        // Extract each field
        String id = parts[0].trim();
        String name = parts[1].trim();
        String email = parts[2].trim();
        GameType game = GameType.fromString(parts[3]);
        int skill = Integer.parseInt(parts[4].trim());
        RoleType role = RoleType.fromString(parts[5]);
        int score = Integer.parseInt(parts[6].trim());

        PersonalityType personality = PersonalityType.fromString(parts[7]);
        if (personality == null) {
            personality = PersonalityType.BALANCED;  // safe fallback
        }

        return new Participant(id, name, email, game, skill, role, score, personality);
    }

    // Reads ID like "P012" and extract 12 and update maxIdNumber.
    private void updateMaxIdFromString(String id) {
        if (id != null && id.length() >= 2 && (id.charAt(0) == 'P' || id.charAt(0) == 'p')) {

            try {
                int num = Integer.parseInt(id.substring(1));
                if (num > maxIdNumber) {
                    maxIdNumber = num;
                }
            } catch (NumberFormatException ignored) {
                // ignore malformed IDs
            }
        }
    }

    // Append one participant to the CSV system file.
    private void appendToSystemFile(Participant p) throws FileProcessingException {
        try (FileWriter fw = new FileWriter(systemFileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(p.toCSVRow()); // write CSV row

        } catch (IOException e) {
            throw new FileProcessingException("Failed to write participant to system file", e);
        }
    }

    //------------organizer----------------------
    //Imports participants from CSV file.
    // this will returns: int[]{importedCount, ignoredCount}
    public int[] importFromCsvFile(String file) throws FileProcessingException{

        participants.clear(); // clear current list before import

        int imported = 0;
        int ignored = 0;

        if (!Files.exists(Path.of(file))){
            //Validate the availability
            throw new FileProcessingException("File " + file + " does not exist");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;

            while ((line = br.readLine()) != null) {
                try{
                    Participant p = parseLine(line);
                    if (p == null) {
                        continue;
                    }

                    // prevent duplicates (using email)
                    if (isEmailTaken(p.getEmail())) {
                        ignored++;
                        continue;
                    }

                    participants.add(p);
                    updateMaxIdFromString(p.getId());
                    imported++;

                } catch (Exception e) {
                    // if any fault in that error, it will ignore.
                    ignored++;
                }
            }
        } catch (IOException e){
            throw new FileProcessingException("Error reading external file", e);
        }

        return new int[]{imported, ignored};
    }
}


