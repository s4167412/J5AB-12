package systems;
import java.util.HashMap;
import locations.*;

public class regionmanager {
    public static HashMap<String, Object> getRegionData(String location, String[] args) {
        HashMap<String, Object> location_data = new HashMap<>();

        switch(location) {
            case "dev_world" -> {
                location_data = locations.dev_world.main(args);
            }
            case "tutoria" -> {
                location_data = locations.tutoria.main(args);
            }
        }
        return location_data;
    }
}