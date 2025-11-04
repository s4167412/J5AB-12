package locations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import systems.POIGenerator;

public class dev_world {
    public static HashMap<String, Object> main(String[] args) {
        ArrayList<StringBuilder> map = new ArrayList<>(Arrays.asList(
            new StringBuilder("+------------------------------------------------+"),
            new StringBuilder("|            #                           # House |"),
            new StringBuilder("| #########  #                 tExTtEsT  #       |"),
            new StringBuilder("|  ##    ##  #                 TeXtTeSt  #       |"),
            new StringBuilder("|#  ## #                                 +  +====|"),
            new StringBuilder("|#     ####        spawn       .`.`.`.`.`.       |"),
            new StringBuilder("|#######                      .`.`.`.`.`.`.      |"),
            new StringBuilder("|##########                   `.`.`.`.`.`.`      |"),
            new StringBuilder("|                              `.`.`.`.`.`       |"),
            new StringBuilder("|           \\                                    |"),
            new StringBuilder("+------------------------------------------------+")
        ));

        HashMap<String, Object> locationData = new HashMap<>();
        HashMap<String, String> neighbours = new HashMap<>();
        ArrayList<POIGenerator> pois = new ArrayList<>();
        ArrayList<String> walls = new ArrayList<>();

        // POIs point to interaction keys stored in systems.interactions.REGISTRY
        pois.add(new POIGenerator(17, 2, "dev_interaction_01", "INTERACTION", false));
        pois.add(new POIGenerator(10, 10, "dev_enemy_01", "ENEMY", true));

        neighbours.put("North", null);
        neighbours.put("East", null);
        neighbours.put("South", null);
        neighbours.put("West", null);

        walls.add("\\");

        locationData.put("Walls", walls);
        locationData.put("POIs", pois);
        locationData.put("Map", map);
        locationData.put("Neighbours", neighbours);
        locationData.put("Location Name", "Dev World");
        locationData.put("Location Description", "Testing place used to test things");
        return locationData;
    }	
}