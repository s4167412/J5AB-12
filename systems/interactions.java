package systems;
import java.util.HashMap;

public class interactions {
    public static HashMap<String, Object> interaction_map(HashMap<String, Object> additional_data, String interaction_id) {
        switch(interaction_id) {
            case "dev_item_interaction_01" -> { return dev_item_interaction_01(additional_data);}
            case "dev_interaction_01" -> { return dev_interaction_01(additional_data);}
            default -> { return null;} // Failed to find interactions
        }
    }

    public static HashMap<String, Object> dev_item_interaction_01(HashMap<String, Object> additional_data) {
        HashMap<String, Object> interaction_data = new HashMap<>();
        Integer interaction_index = (Integer) additional_data.getOrDefault("current_interaction_index", 0);
        interaction_data.put("item_name", "dev item");
        return interaction_data;
    }

    public static HashMap<String, Object> dev_interaction_01(HashMap<String, Object> additional_data) {
        HashMap<String, Object> interaction_data = new HashMap<>();
        Integer interaction_index = (Integer) additional_data.getOrDefault("current_interaction_index", 0);
        interaction_data.put("npc_name", "Dev NPC 01");
        switch(interaction_index) {
            case 0 -> { // Start of interaction
                interaction_data.put("Dialogue", "Pick an option, any option, really. MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT MORE TEXT ");
                interaction_data.put("Options", new String[]{
                    "[1] Option 1", 
                    "[2] Option 2",
                    "[3] Option 3"
                });
                return interaction_data;
            }
            case 1 -> { // Option 0.1 chosen
                interaction_data.put("Dialogue", "You picked option 1!");
                interaction_data.put("Options", new String[]{
                    "[4] Ok...?", 
                });
                return interaction_data;
            }
            case 2 -> { // Option 0.2 chosen
                interaction_data.put("Dialogue", "You picked option 2!");
                interaction_data.put("Options", new String[]{
                    "[4] Ok...?", 
                });
                return interaction_data;
            }
            case 3 -> { // Option 0.3 chosen
                interaction_data.put("Dialogue", "You picked option 3!");
                interaction_data.put("Options", new String[]{
                    "[4] Ok...?", 
                });
                return interaction_data;
            }
            case 4 -> { // End of 0.x
                interaction_data.put("Dialogue", "...");
                interaction_data.put("Options", null);
                return interaction_data;
            }
            default -> { // Exited early
                interaction_data.put("Dialogue", "Ok, dont talk to me I guess.");
                interaction_data.put("Options", null);
                return interaction_data;
            }
        }
    }
};
