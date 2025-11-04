package locations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import systems.POIGenerator;

public class rolling_meadows {
    public static HashMap<String, Object> main(String[] args) {
        ArrayList<StringBuilder> map = new ArrayList<>(Arrays.asList(
            new StringBuilder("+------------------------------------------------+"),
            new StringBuilder("|                                                |"),
            new StringBuilder("|                                                |"),
            new StringBuilder("|                                                |"),
            new StringBuilder("|                                                |"),
            new StringBuilder("|                                                |"),
            new StringBuilder("<                                                |"),
            new StringBuilder("|                                                |"),
            new StringBuilder("|                                                |"),
            new StringBuilder("|                                                |"),
            new StringBuilder("+------------------------------------------------+")
        ));

        HashMap<String, Object> locationData = new HashMap<>();
        HashMap<String, String> neighbours = new HashMap<>();
        ArrayList<POIGenerator> pois = new ArrayList<>();
        ArrayList<String> walls = new ArrayList<>();

        // POIs point to interaction keys stored in systems.interactions.REGISTRY
        pois.add(new POIGenerator(10, 3, "template_interaction_", "INTERACTION", false));
        pois.add(new POIGenerator(15, 5, "template_interaction", "ENEMY", true));

        neighbours.put("North", null);
        neighbours.put("East", null);
        neighbours.put("South", null);
        neighbours.put("West", "tutoria");

        walls.add("\\");
        walls.add("/");
        walls.add("_");

        locationData.put("Walls", walls);
        locationData.put("POIs", pois);
        locationData.put("Map", map);
        locationData.put("Neighbours", neighbours);
        locationData.put("Location Name", "template");
        locationData.put("Location Description", "template");
        return locationData;
    }	
}