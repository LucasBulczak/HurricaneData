import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        final int positionOfWindsName = 1;
        final int positionOfWindsSpeed = 6;

        ArrayList<Integer> hurricaneSpeed = new ArrayList<>();
        //LinkedHashMap because it will iterate in the order in which the entries were put into the map
        Map<String, ArrayList<Integer>> hurricaneData = new LinkedHashMap<>();

        try {
            Scanner sc = new Scanner(new File("hurricane_data.txt"));
            String currentLine;

            String headerLineRegex = "^(EP|CP)\\d{2}(2009)";
            String dataLineRegex   = "^(2009)";

            Pattern patternForHeaderLineRegex = Pattern.compile(headerLineRegex, Pattern.CASE_INSENSITIVE);
            Pattern patternForDataLineRegex = Pattern.compile(dataLineRegex, Pattern.CASE_INSENSITIVE);

            Matcher headerLineMatcher, dataLineMatcher;

            String[] rowDataAboutHurricane;
            String windName="";

            int maximumSustainedWindInKnots;

            while (sc.hasNextLine()){
                currentLine = sc.nextLine();
                headerLineMatcher = patternForHeaderLineRegex.matcher(currentLine);
                dataLineMatcher = patternForDataLineRegex.matcher(currentLine);

                if (headerLineMatcher.find()) {
                    rowDataAboutHurricane = currentLine.split(",");
                    windName = rowDataAboutHurricane[positionOfWindsName].replaceAll("\\s+", "");

                    //create new ArrayList for the new key
                    if ( !hurricaneData.containsKey(windName) ) hurricaneSpeed = new ArrayList<>();
                }
                else {
                    if (dataLineMatcher.find()) {
                        rowDataAboutHurricane = currentLine.split(",");
                        maximumSustainedWindInKnots =
                                Integer.parseInt(rowDataAboutHurricane[positionOfWindsSpeed].replaceAll("\\s+", ""));
                        hurricaneSpeed.add( maximumSustainedWindInKnots );
                        hurricaneData.put(windName, hurricaneSpeed);
                    }
                }
            }
            sc.close();
        }
        catch ( FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Source file not found!");
        }

        // DISPLAY THE DATA
        for (Map.Entry<String, ArrayList<Integer>> mapEntry : hurricaneData.entrySet() ) {
            //System.out.println( mapEntry.getKey() + " : " + Collections.max(mapEntry.getValue()) );
            System.out.printf( "Wind: %9s - %3d knots.\n", mapEntry.getKey(), Collections.max(mapEntry.getValue()) );
        }
    }
}

