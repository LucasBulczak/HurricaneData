import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public class HurricaneInfo {

    private static final String FILE_NAME = "hurricane_data.txt";
    private static final int POSITION_OF_WINDS_NAME = 1;
    private static final int POSITION_OF_WINDS_SPEED = 6;

    private String windName = null;

    private ArrayList<Integer> hurricaneSpeed = null;
    private Map<String, ArrayList<Integer>> hurricaneData = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        HurricaneInfo hr = new HurricaneInfo();
        hr.readAndProcessInfoFromFile();
    }
    private void readAndProcessInfoFromFile() throws FileNotFoundException {
        Scanner scanner = null;

        try {
            scanner = openFile(FILE_NAME);
            findAndParseData(scanner);
            displayData();
        }
        finally {
            closeFile(scanner);
        }
    }
    private void findAndParseData(Scanner scanner) {
        String currentLine;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            parseHeaderWithWindName(currentLine);
            parseDataLine(currentLine);
        }
    }
    private void parseHeaderWithWindName(String currentLine) {
        Pattern patternForHeaderLineRegex = Pattern.compile("^(EP|CP)\\d{2}(2009)", Pattern.CASE_INSENSITIVE);

        if (patternForHeaderLineRegex.matcher(currentLine).find()) {
            String[] rowDataAboutHurricane = currentLine.split(",");
            windName = rowDataAboutHurricane[POSITION_OF_WINDS_NAME].replaceAll("\\s+", "");

            if ( !hurricaneData.containsKey(windName) )
                hurricaneSpeed = new ArrayList<>();
        }
    }
    private void parseDataLine(String currentLine) {
        Pattern patternForDataLineRegex = Pattern.compile("^(2009)", Pattern.CASE_INSENSITIVE);

        if (patternForDataLineRegex.matcher(currentLine).find()) {
            String[] rowDataAboutHurricane = currentLine.split(",");
            int maximumSustainedWindInKnots =
                    Integer.parseInt(rowDataAboutHurricane[POSITION_OF_WINDS_SPEED].replaceAll("\\s+", ""));
            hurricaneSpeed.add( maximumSustainedWindInKnots );
            hurricaneData.put(windName, hurricaneSpeed);
        }
    }
    private Scanner openFile(String fileName) throws FileNotFoundException {
        return new Scanner (new File(fileName));
    }
    private void closeFile (Scanner scanner) {
        try {
            if (scanner != null)
                scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Source file not found!");
        }
    }
    private void displayData() {
        for (Map.Entry<String, ArrayList<Integer>> mapEntry : hurricaneData.entrySet() )
            System.out.printf( "Wind: %9s - %3d knots.\n", mapEntry.getKey(), Collections.max(mapEntry.getValue()) );
    }
}