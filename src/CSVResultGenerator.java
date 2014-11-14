import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/** 
 * This class generates a CSV representation of all the Planets in a single
 * code base SolarSystem.
 * 
 * NOTE: This class is adapted from an answer on the following Stack Overflow page:
 * http://stackoverflow.com/questions/3666007/how-to-serialize-object-to-csv-file
 * 
 * @author Susannah
 *
 */
public class CSVResultGenerator {
	
    private static final String CSV_SEPARATOR = ",";
    private static final String HEADERS = "period,planet_radius,semimajor_axis,stellar_radius,stellar_teff,id,planet_name,hue";
    
    public static void writeToCSV(SolarSystem solarSystem)
    {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("site/planets.csv")));
            
            ArrayList<Planet> planetList = solarSystem.getPlanets();
            StringBuffer oneLine = new StringBuffer();
        	oneLine.append(HEADERS);
        	writeNewLine(bw, oneLine);
            
            for (Planet planet : planetList) {
                oneLine = new StringBuffer();
                // period, planet_radius, semimajor_axis, stellar_radius, stellar_teff, id, planet_name, hue    
                oneLine.append(planet.getPeriod());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(planet.getPlanetRadius());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(planet.getSemimajorAxis());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(planet.getStarRadius());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(planet.getStarTeff());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(planet.getSolarSystemId());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(planet.getPlanetName());
                oneLine.append(CSV_SEPARATOR);
                oneLine.append(planet.getHue());
                oneLine.append(CSV_SEPARATOR);
                writeNewLine(bw, oneLine);
            }
            
            bw.flush();
            bw.close();
        } catch (UnsupportedEncodingException e) {
        	System.out.println(String.format("Whoops, UnsupportedEncodingException: %s", e.getMessage()));
        } catch (FileNotFoundException e){
        	System.out.println(String.format("Whoops, FileNotFoundException: %s", e.getMessage()));
        } catch (IOException e){
        	System.out.println(String.format("Whoops, IOException: %s", e.getMessage()));
        }
    }

	private static void writeNewLine(BufferedWriter bw, StringBuffer oneLine)
			throws IOException {
		bw.write(oneLine.toString());
		bw.newLine();
	}

}
