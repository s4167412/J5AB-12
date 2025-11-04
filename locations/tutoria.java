package locations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import systems.POIGenerator;

public class tutoria {
    public static HashMap<String, Object> main(String[] args) {
        ArrayList<StringBuilder> map = new ArrayList<>(Arrays.asList(
            new StringBuilder("+------------------------------------------------+"),
            new StringBuilder("| __________________________________ . . . . . . |"),
            new StringBuilder("|/                    ##            \\ . . . . .  |"),
            new StringBuilder("|#                      ##           \\___________|"),
            new StringBuilder("|#                       #                       |"),
            new StringBuilder("|###            .`                               |"),
            new StringBuilder("|#               .`.`.`.`.`.`.`.`.`.`.`.`.`.`.`. >"),
            new StringBuilder("|\\                                               |"),
            new StringBuilder("| \\______________________________________________|"),
            new StringBuilder("| . . . . . . . . . . . . . . . . . . . . . . .  |"),
            new StringBuilder("+------------------------------------------------+")
        ));

        HashMap<String, Object> locationData = new HashMap<>();
        HashMap<String, String> neighbours = new HashMap<>();
        ArrayList<POIGenerator> pois = new ArrayList<>();
        ArrayList<String> walls = new ArrayList<>();

        // POIs point to interaction keys stored in systems.interactions.REGISTRY
        pois.add(new POIGenerator(10, 3, "tutoria_interaction_wizard", "INTERACTION", false));
        pois.add(new POIGenerator(15, 5, "tutoria_interaction_training_enemy", "ENEMY", false));

        neighbours.put("North", null);
        neighbours.put("East", "rolling_meadows");
        neighbours.put("South", null);
        neighbours.put("West", null);

        walls.add("\\");
        walls.add("/");
        walls.add("_");

        locationData.put("Walls", walls);
        locationData.put("POIs", pois);
        locationData.put("Map", map);
        locationData.put("Neighbours", neighbours);
        locationData.put("Location Name", "Tutoria");
        locationData.put("Location Description", "A place where adventure begins...with a tutorial");
        return locationData;
    }	
}