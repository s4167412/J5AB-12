import functions.textformatters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import systems.POIGenerator;
import systems.filemanager;
import systems.interactions;
import systems.regionmanager;


// Items cant be more than 9 characters long

// AN NPC TO TALK TO (INTERACT)
// INTERACTION SYSTEM
// Item use system (apply the effect into player data)
// COMBAT SYSTEM + UI
// START SCREEN
// Map file templates
// come up with good game name
// poi spawning
// crashes when inventory empty :(

// E X P A N D

public class game {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        // Window Calibration (manual)
        System.out.println("""
                \n\n+-------------------------------------------------------------------------------------------------------------------------+
                |                                                                                                                         |
                |                                                                                                                         |
                |                                                                                                                         |
                |                                 _____            _               _____                    _                             |
                |                                |_   _|   _ _ __ (_)_ __   __ _  |_   _| __ __ ___   _____| |___                         |
                |                                  | || | | | '_ \\| | '_ \\ / _` |   | || '__/ _` \\ \\ / / _ \\ / __|                        |
                |                                  | || |_| | |_) | | | | | (_| |   | || | | (_| |\\ V /  __/ \\__ \\                        |
                |                                  |_| \\__, | .__/|_|_| |_|\\__, |   |_||_|  \\__,_| \\_/ \\___|_|___/                        |
                |                                      |___/|_|            |___/                                                          |
                |                                                                                                                         |
                |                                                                                                                         |
                |                                                                                                                         |
                |                                                   WINDOW CALIBRATION                                                    |
                |                                    +----------------------------------------------+                                     |
                |                                                                                                                         |
                |                                Resize your window so the entire box is shown correctly                                  |
                |                                                                                                                         |
                |                                    Only the box and input field should be showing                                       |
                |                                                                                                                         |
                |                                               Press [ENTER] once ready                                                  |
                |                                                                                                                         |
                |                                                                                                                         |
                |                                        vvv Input field is below this line vvv                                           |""");
        userInput.nextLine();

        String choice;
        Boolean in_menu = true;
        HashMap<String, Object> playerData = new HashMap<>(); // I hate java, errors if its not defined exactly here

        // Main Menu
        while(in_menu) {
            System.out.println("WORKING TITLE\n");
            System.out.println("1. New Game\n2. Load Game\n3. Help\n4. Credits\n5. Close Game\n");
            choice = userInput.nextLine();

            switch(choice) {
                case "1" -> {
                    System.out.println("WARNING\nAny existing save file will be overwritten. Back up current save file if you wish to keep it.\n\nPress [ENTER] to continue or close the game to cancel.");
                    userInput.nextLine();
                    filemanager.createSaveFile();
                    playerData = filemanager.loadData();
                    in_menu = false;
                }
                case "2" -> {
                    playerData = filemanager.loadData();
                    if (playerData == null || playerData.isEmpty()) {
                        // User is alerted from loadData() if conditions arent met, cant have double ! in if???? JAVA WHYYYYY
                    } else {
                        in_menu = false;
                    }
                }
                case "3" -> {
                    System.out.println("Help"); //TODO
                    // SHOW INFORMATION
                }
                case "4" -> {
                    System.out.println("""
                        \nCREDITS
                        Programming - Cameron Young (s4167412)
                        Art* - Cameron Young (s4167412)
                        Planning - Cameron Young (s4167412)

                        *Text based ASCII art was created at: https://www.asciiart.eu/text-to-ascii-art
                        All other ASCII art shown (Maps & UI Elements) was created by Cameron Young (s4167412)

                        Press [ENTER] to return to menu
                        """);
                    userInput.nextLine();
                }
                case "5" -> {
                    System.out.println(":(");
                    System.exit(0); // Closes game
                }
            }
        }

        System.out.println(playerData); // TODO: REMOVE DEBUG

        HashMap<String, Object> locationData = regionmanager.getRegionData("dev_world", new String[0]);
        System.out.println(locationData);

        String situation = "DEFAULT";
        HashMap<String, Object> additional_data = new HashMap<>();

        Integer game_frame = 1; // TODO: Possible bug if 100th frame is in a non default state, add check and queue up saves if in non default
        while(true) { // Game Loop, autosaves every 100 loops or 'frames' (in generateScreen())
            generateScreen(playerData, locationData, situation, game_frame, additional_data);
            switch(situation) { // Secondary game loop, handles each situation separately
                case "DEFAULT" -> { // Used when only moving around the map
                    HashMap<String, Integer> player_coordinates = (HashMap<String, Integer>) playerData.get("coordinates");
                    additional_data.clear(); // never needed for this case
                    choice = userInput.nextLine();

                    Integer[] player_future_coordinates = new Integer[] {player_coordinates.get("x"), player_coordinates.get("y")};

                    switch(choice) {
                        case "w" -> player_future_coordinates = new Integer[] {player_coordinates.get("x"), player_coordinates.get("y") - 1};
                        case "a" -> player_future_coordinates = new Integer[] {player_coordinates.get("x") - 1, player_coordinates.get("y")};
                        case "s" -> player_future_coordinates = new Integer[] {player_coordinates.get("x"), player_coordinates.get("y") + 1};
                        case "d" -> player_future_coordinates = new Integer[] {player_coordinates.get("x") + 1, player_coordinates.get("y")};
                        case "e" -> {
                            situation = "INVENTORY";
                            additional_data.put("selected_inventory_index", 0);
                            continue;
                        }
                        case "0" -> {
                            Boolean save_success;
                            save_success = filemanager.saveData(playerData);
                            if (save_success) {
                                System.out.println("Game saved successfully.");
                            } else {
                                System.out.println("Error saving game. Progress has been lost");
                            }
                            System.exit(0);
                        }
                        default -> { // Invaild option
                            continue;
                        }
                    }

                    String possible_move = "false";
                    possible_move = movementCheck(player_future_coordinates, locationData);
                    System.out.println("Possible Move: " + possible_move);
                    switch(possible_move) {
                        case String s when s.startsWith("INTERACTION:") -> {
                            System.out.println("Interaction POI found");
                            // Split the returned string to get interaction ID and type
                            String[] interaction_parts = s.split(":");  // INTERACTION:id:type
                            String interaction_id = interaction_parts[1];
                            String interaction_type = interaction_parts[0];

                            situation = "INTERACTION";
                            additional_data.put("current_interaction", interaction_id);
                            additional_data.put("current_interaction_index", 0);
                        }
                        case "ENEMY" -> {
                            situation = "DEFAULT"; // TEMP
                        }
                        case "SHOP" -> {
                            situation = "DEFAULT"; // TEMP
                        }
                        case "false" -> { // invalid move
                        }
                        case "true" -> { // vaild move
                            player_coordinates.put("x", player_future_coordinates[0]);
                            player_coordinates.put("y", player_future_coordinates[1]);
                        }
                        default -> {
                            // Shouldn't happen
                        }
                    }
                }
                case "INVENTORY" -> { // Used when in inventory screen
                    choice = userInput.nextLine();
                    switch(choice) {
                        case "w" -> { // Move selection up
                            additional_data.put("selected_inventory_index", (int) additional_data.get("selected_inventory_index") - 1);
                        }
                        case "s" -> { // Move selection down
                            additional_data.put("selected_inventory_index", (int) additional_data.get("selected_inventory_index") + 1);
                        }
                        case "e" -> { // Exit inventory
                            situation = "DEFAULT";
                        }
                        case "q" -> { // Use selected item
                            additional_data.put("use_inventory_index", additional_data.get("selected_inventory_index"));
                            // TODO: apply effects to playerData
                        }
                        case "0" -> { // Discard selected item
                            ArrayList<String> inventory = (ArrayList<String>) playerData.get("inventory");
                            Integer discard_index = (Integer) additional_data.get("selected_inventory_index");
                            if (discard_index >= 0 && discard_index < inventory.size()) {
                                inventory.remove((int) discard_index);
                            }
                        }
                        default -> { // Invaild option
                            continue;
                        }
                    }
                }
                case "INTERACTION" -> {
                    String interaction_id = (String) additional_data.get("current_interaction");
                    String interaction_type = (String) additional_data.get("current_interaction_type");

                    HashMap<String, Object> interaction_data = interactions.interaction_map(additional_data, interaction_id);
                    Integer interaction_index = (Integer) additional_data.get("current_interaction_index");
                    String[] options = (String[]) interaction_data.get("Options");

                    System.out.println(interaction_index);

                    if (options == null) { // End interaction
                        situation = "DEFAULT";
                        continue;
                    }

                    for (String entry : options) {
                        System.out.println(entry);
                    }

                    choice = userInput.nextLine();

                    Integer picked = 0;
                    try {
                        picked = Integer.parseInt(choice);
                        System.out.println("converted");
                    } catch (Exception e) {
                        continue;
                    }

                    System.out.println("picked = " + picked);
                    System.out.println("option length = " + options.length);
                    String selected_option = options[picked-1];

                    switch(picked) { // Get next interaction dialogue
                        case 1 -> {
                            if (options.length >= 1) {
                                Integer next_index = Integer.parseInt(selected_option.substring(selected_option.indexOf("[") + 1, selected_option.indexOf("]")));
                                additional_data.replace("current_interaction_index", next_index);
                            }
                        }
                    }

                }
            }
            game_frame += 1;
        }
    }

    public static String movementCheck(Integer[] player_future_coordinates, HashMap<String, Object> locationData) {
        ArrayList<StringBuilder> region_map = renderMap(locationData);
        ArrayList<String> walls = (ArrayList<String>) locationData.get("Walls");
        String future_character = String.valueOf(region_map.get(player_future_coordinates[1] - 1).charAt(player_future_coordinates[0] - 1)); // -1's due to index 0s

        if (!walls.isEmpty()) { // Per region wall detection
            for (String wall : walls) {
                if (future_character.equals(wall)) {
                    return "false";
                }
            }
        }

        switch(future_character) {
            case "^" -> { return "TRAVEL_NORTH"; } // Move to north region
            case ">" -> { return "TRAVEL_EAST"; } // Move to east region
            case "v" -> { return "TRAVEL_SOUTH"; } // Move to south region
            case "<" -> { return "TRAVEL_WEST"; } // Move to west region
            case "#", "=" -> { return "false"; } // Wall collision
            case "?" -> { // Interaction POI
                ArrayList<POIGenerator> pois = (ArrayList<POIGenerator>) locationData.get("POIs");
                for (POIGenerator poi : pois) {
                    if (poi.x == player_future_coordinates[0] && poi.y == player_future_coordinates[1]) {
                        return "INTERACTION:" + poi.id;
                    }
                }
                return "false"; // No POI found at coordinates (shouldn't happen)
            }
            case "!" -> { return "ENEMY"; } // Enemy POI
            case "$" -> { return "SHOP"; } // Shop POI
            case "|", "-", "+" -> { return "false"; } // Region wall collision
            default -> { return "true"; } // Free to move
        }
    }

    /// Screen Generation
    public static HashMap<String, Object> generateScreen(HashMap<String, Object> playerData, HashMap<String, Object> locationData, String situation, Integer game_frame, HashMap<String, Object> additional_data) {
        // Redraw entire screen each 'frame'
        // Reset all vars to DEFAULT state NVM TEMP
        Boolean inBattle = false;

        /// Data to replace, that is ALWAYS required:
        Character playerSymbol = '@';

        // Player Data Display
        String playerDisplay = " " + playerData.get("characterName").toString() + " || " + playerData.get("health").toString() + "/100 HP || COINS: $" + playerData.get("coins").toString();
        if (playerDisplay.length() < 48) {
            playerDisplay = playerDisplay + " ".repeat(48 - playerDisplay.length());
        }

        int player_health = (int) playerData.get("health");
        int player_dashes = (int) Math.round(player_health / 2.5); // Health
        int player_stars = (int) Math.round((100 - player_health) / 2.5); // Missing Health
        String playerHealthDisplay = "-".repeat(Math.max(0, player_dashes)) + "*".repeat(Math.max(0, player_stars));

        // Map Display
        ArrayList<StringBuilder> region_map = renderMap(locationData);
        for (StringBuilder sb : (ArrayList<StringBuilder>) locationData.get("Map")) {
            region_map.add(new StringBuilder(sb.toString()));
        }

        // Tips Display
        ArrayList<String> tips = new ArrayList<>(Arrays.asList(
            " Tip: Look for '?', you can interact with them. ",
            " Tip: Watch out for !, unless you want a fight! ",
            " Tip: Arrows point the way to new regions.      ",
            " Tip: Autosaves occur every 100 frames.         ",
            " Tip: Defeat enemies to earn coins!             ",
            " Tip: You can only hold one of each item!       "
        ));
        String tipDisplay = " ".repeat(48);
        if (game_frame % 100 == 0) {
            filemanager.saveData(playerData);
            tipDisplay = " Not a Tip but..Your game has been autosaved :) ";
            Random generator = new Random();
            tipDisplay = tips.get((int) (generator.nextInt(0, tips.size())));
        }

        // Inventory Display
        ArrayList<String> inventory = (ArrayList<String>) playerData.get("inventory");
        ArrayList<String> inventory_display = new ArrayList<>();
        Integer first_item_index = 0, last_item_index;
        for (String item_name : inventory) {
            String output = item_name;
            if (output.length() < 13) {
                output = output + " ".repeat(13 - output.length());
            }
            inventory_display.add(output);
        }

        last_item_index = inventory_display.size() - 1;
        System.out.println(last_item_index);
        while (inventory_display.size() < 6) { // Fill empty inventory slots with blanks
            inventory_display.add(" ".repeat(13));
        }

        // Compass Display
        String north_display = "      ^^      ", south_display = "      vv      ", east_display = " <", west_display = "> ";
        System.out.println(locationData.get("Neighbours"));
        HashMap<String, String> neighbours = (HashMap<String, String>) locationData.get("Neighbours");
        if (neighbours.get("North") == null) {
            north_display = " ".repeat(14);
        }
        if (neighbours.get("South") == null) {
            south_display = " ".repeat(14);
        }
        if (neighbours.get("East") == null) {
            east_display = " ".repeat(2);
        }
        if (neighbours.get("West") == null) {
            west_display = " ".repeat(2);
        }

        // Enemy Data Display
        String enemyDisplay = "";
        String enemyHealthDisplay = "";
        if (inBattle) {
            // To be added later
            // String enemyname = "LONGNAMEDENEMYYYYYYYY";
            // enemyDisplay = "  LVL: XX || XX/YY HP || " + enemyname + " ";
            // if (enemyDisplay.length() < 48) {
            //     enemyDisplay = " ".repeat(48 - enemyDisplay.length()) + enemyDisplay;
            // }

            // int enemy_max_health = (int) 84921;
            // int enemy_health = (int) 53201;
            // Double enemy_segment = (double) (enemy_max_health / 40.0);
            // System.out.println(enemy_segment);
            // int enemy_dashes = (int) Math.round(enemy_health / enemy_segment); // Health
            // int enemy_stars = (int) Math.round((enemy_max_health - enemy_health) / enemy_segment); // Missing Health
            // enemyHealthDisplay = "-".repeat(Math.max(0, enemy_dashes)) + "*".repeat(Math.max(0, enemy_stars));
        } else {
            // No battle, empty display
            enemyDisplay = " ".repeat(48);
            enemyHealthDisplay = " ".repeat(40);
        }

        ArrayList<StringBuilder> game_screen = new ArrayList<>();

        switch(situation) {
            case "DEFAULT" -> { // Used when only moving around the map
                game_screen = new ArrayList<>(Arrays.asList(
                    new StringBuilder(region_map.get(0)+"--------------+---------------------------------------------------------+"),
                    new StringBuilder(region_map.get(1)+"              |     *          ____        __  __  __     __      *     |"),
                    new StringBuilder(region_map.get(2)+" "+inventory_display.get(0)+"|    /          / __ )____ _/ /_/ /_/ /__  / /       \\    |"),
                    new StringBuilder(region_map.get(3)+" "+inventory_display.get(1)+"|  *|          / __  / __ `/ __/ __/ / _ \\/ /         |*  |"),
                    new StringBuilder(region_map.get(4)+" "+inventory_display.get(2)+"|  ||         / /_/ / /_/ / /_/ /_/ /  __/_/          ||  |"),
                    new StringBuilder(region_map.get(5)+"              |  ||        /_____/\\__,_/\\__/\\__/_/\\___(_)           ||  |"),
                    new StringBuilder(region_map.get(6)+" "+inventory_display.get(3)+"|  \\\\======_____________________________________======//  |"),
                    new StringBuilder(region_map.get(7)+" "+inventory_display.get(4)+"|            (CharacterName) vs lvl xx (enemy)            |"),
                    new StringBuilder(region_map.get(8)+" "+inventory_display.get(5)+"|               Difficulty [x] [x] [x] [x]                |"),
                    new StringBuilder(region_map.get(9)+"              |                                                         |"),
                    new StringBuilder(region_map.get(10)+"--------------+            Your speed - 1894ms                          |"),
                    new StringBuilder("|"+playerDisplay+"|"+north_display+"|                         1682ms - Enemy Speed            |"),
                    new StringBuilder("|    "+playerHealthDisplay+"    |              |                                                         |"),
                    new StringBuilder("|                                                |"+east_display+"          "+west_display+"|                        You Lost                         |"),
                    new StringBuilder("|"+enemyDisplay+"|"+east_display+"          "+west_display+"|                                                         |"),
                    new StringBuilder("|    "+enemyHealthDisplay+"    |              | Words to type:                                          |"),
                    new StringBuilder("|"+tipDisplay+"|"+south_display+"|   jfhsajfhsa shfsaf asfshf gpoinsa (Bolded words)       |"),
                    new StringBuilder("+------------------------------------------------+--------------+---------------------------------------------------------+"),
                    new StringBuilder("|                   North (w)                    |              |                                                          "),
                    new StringBuilder("|                                                |              |                                                          "),
                    new StringBuilder("|    West (a)     Inventory (e)     East (d)     |              |                                                          "),
                    new StringBuilder("|                                                |              |                                                          "),
                    new StringBuilder("|                   South (s)                    |              |                                                          "),
                    new StringBuilder("|                                Save & Exit (0) |              |                                                          ")
                ));
            }

            case "INVENTORY" -> { // Used when in inventory screen
                Integer selected_index = additional_data.containsKey("selected_inventory_index") ? (Integer) additional_data.get("selected_inventory_index") : 0;

                if (selected_index < 0) { // If too low (wrap to bottom)
                    selected_index = last_item_index; // Wrap to bottom
                    additional_data.put("selected_inventory_index", last_item_index);
                } else if (selected_index > last_item_index) { // If too high (wrap to top)
                    selected_index = 0;
                    additional_data.put("selected_inventory_index", first_item_index);
                }

                if (inventory_display.get(selected_index).trim().isEmpty()) {
                    selected_index = 0; // Reset to 0 if selected index is empty, handles cases where the index (max index) is discarded or used
                    additional_data.put("selected_inventory_index", 0);
                }

                // Edit selected index to include marker
                String item = inventory_display.get(selected_index);
                if (!item.trim().isEmpty()) {
                    item = item.trim() + " <<";
                    if (item.length() < 13) {
                        item = item + " ".repeat(13 - item.length());
                    }
                    inventory_display.set(selected_index, item);
                }

                game_screen = new ArrayList<>(Arrays.asList(
                    new StringBuilder(region_map.get(0)+"--------------+---------------------------------------------------------+"),
                    new StringBuilder(region_map.get(1)+"              |     *          ____        __  __  __     __      *     |"),
                    new StringBuilder(region_map.get(2)+" "+inventory_display.get(0)+"|    /          / __ )____ _/ /_/ /_/ /__  / /       \\    |"),
                    new StringBuilder(region_map.get(3)+" "+inventory_display.get(1)+"|  *|          / __  / __ `/ __/ __/ / _ \\/ /         |*  |"),
                    new StringBuilder(region_map.get(4)+" "+inventory_display.get(2)+"|  ||         / /_/ / /_/ / /_/ /_/ /  __/_/          ||  |"),
                    new StringBuilder(region_map.get(5)+"              |  ||        /_____/\\__,_/\\__/\\__/_/\\___(_)           ||  |"),
                    new StringBuilder(region_map.get(6)+" "+inventory_display.get(3)+"|  \\\\======_____________________________________======//  |"),
                    new StringBuilder(region_map.get(7)+" "+inventory_display.get(4)+"|            (CharacterName) vs lvl xx (enemy)            |"),
                    new StringBuilder(region_map.get(8)+" "+inventory_display.get(5)+"|               Difficulty [x] [x] [x] [x]                |"),
                    new StringBuilder(region_map.get(9)+"              |                                                         |"),
                    new StringBuilder(region_map.get(10)+"--------------+            Your speed - 1894ms                          |"),
                    new StringBuilder("|"+playerDisplay+"|      ^^      |                         1682ms - Enemy Speed            |"),
                    new StringBuilder("|    "+playerHealthDisplay+"    |              |                                                         |"),
                    new StringBuilder("|                                                | <          > |                        You Lost                         |"),
                    new StringBuilder("|"+enemyDisplay+"| <          > |                                                         |"),
                    new StringBuilder("|    "+enemyHealthDisplay+"    |              | Words to type:                                          |"),
                    new StringBuilder("|"+tipDisplay+"|      vv      |   jfhsajfhsa shfsaf asfshf gpoinsa (Bolded words)       |"),
                    new StringBuilder("+------------------------------------------------+--------------+---------------------------------------------------------+"),
                    new StringBuilder("|                     Up (w)                     |              |                                                         |"),
                    new StringBuilder("|                                                |              |                                                         |"),
                    new StringBuilder("|               Exit Inventory (e)               |              |                                                         |"),
                    new StringBuilder("|                                                |              |                                                         |"),
                    new StringBuilder("|                    Down (s)                    |              |                                                         |"),
                    new StringBuilder("|     Use (q)                        Discard (0) |              |                                                         |")
                ));
            }

            case "INTERACTION" -> { // Used when in interaction screen
                String interaction_id = (String) additional_data.get("current_interaction");

                // if (additional_data.get("npc_name") == null) { // NPC interaction handling
                HashMap<String, Object> interaction_data = interactions.interaction_map(additional_data, interaction_id);
                    // if (interaction_data != null) {
                String dialogue = (String) interaction_data.get("Dialogue");
                String[] options = (String[]) interaction_data.get("Options");
                String[] split_dialogue = textformatters.wrapText(dialogue, 56);

                if (options == null) { // End dialogue
                    return additional_data;
                }

                for (int i = 0; i < split_dialogue.length; i++) {
                    split_dialogue[i] = textformatters.centerText(split_dialogue[i], 56);
                }

                String interaction_name_display = (String) textformatters.centerText((String) interaction_data.get("npc_name"), 57); // NPC Name

                String interaction_display_1 = (split_dialogue.length > 0) ? split_dialogue[0]: " ".repeat(57);
                String interaction_display_2 = (split_dialogue.length > 1) ? split_dialogue[1]: " ".repeat(57);
                String interaction_display_3 = (split_dialogue.length > 2) ? split_dialogue[2]: " ".repeat(57);

                String option_display_1 = (options.length > 0) ? options[0].substring(options[0].indexOf("] ") + 2) + " (1)": " ".repeat(48);
                String option_display_2 = (options.length > 1) ? options[1].substring(options[1].indexOf("] ") + 2) + " (2)": " ".repeat(48);
                String option_display_3 = (options.length > 2) ? options[2].substring(options[2].indexOf("] ") + 2) + " (3)": " ".repeat(48);
                String option_display_4 = (options.length > 3) ? options[3].substring(options[3].indexOf("] ") + 2) + " (4)": " ".repeat(48);
                    // }

                // } else if (additional_data.get("item_name") == null) { // Item interaction handling
                //     if (inventory.size() < 5) {
                //         playerData.
                //     }
                // }

                game_screen = new ArrayList<>(Arrays.asList(
                    new StringBuilder(region_map.get(0)+"--------------+---------------------------------------------------------+"),
                    new StringBuilder(region_map.get(1)+"              |     *          ____        __  __  __     __      *     |"),
                    new StringBuilder(region_map.get(2)+" "+inventory_display.get(0)+"|    /          / __ )____ _/ /_/ /_/ /__  / /       \\    |"),
                    new StringBuilder(region_map.get(3)+" "+inventory_display.get(1)+"|  *|          / __  / __ `/ __/ __/ / _ \\/ /         |*  |"),
                    new StringBuilder(region_map.get(4)+" "+inventory_display.get(2)+"|  ||         / /_/ / /_/ / /_/ /_/ /  __/_/          ||  |"),
                    new StringBuilder(region_map.get(5)+"              |  ||        /_____/\\__,_/\\__/\\__/_/\\___(_)           ||  |"),
                    new StringBuilder(region_map.get(6)+" "+inventory_display.get(3)+"|  \\\\======_____________________________________======//  |"),
                    new StringBuilder(region_map.get(7)+" "+inventory_display.get(4)+"|            (CharacterName) vs lvl xx (enemy)            |"),
                    new StringBuilder(region_map.get(8)+" "+inventory_display.get(5)+"|               Difficulty [x] [x] [x] [x]                |"),
                    new StringBuilder(region_map.get(9)+"              |                                                         |"),
                    new StringBuilder(region_map.get(10)+"--------------+            Your speed - 1894ms                          |"),
                    new StringBuilder("|"+playerDisplay+"|"+north_display+"|                         1682ms - Enemy Speed            |"),
                    new StringBuilder("|    "+playerHealthDisplay+"    |              |                                                         |"),
                    new StringBuilder("|                                                |"+east_display+"          "+west_display+"|                        You Lost                         |"),
                    new StringBuilder("|"+enemyDisplay+"|"+east_display+"          "+west_display+"|                                                         |"),
                    new StringBuilder("|    "+enemyHealthDisplay+"    |              | Words to type:                                          |"),
                    new StringBuilder("|"+tipDisplay+"|"+south_display+"|   jfhsajfhsa shfsaf asfshf gpoinsa (Bolded words)       |"),
                    new StringBuilder("+------------------------------------------------+--------------+---------------------------------------------------------+"),
                    new StringBuilder("|"+textformatters.centerText(option_display_1, 48)+"|              |"+interaction_name_display+"|"),
                    new StringBuilder("|"+textformatters.centerText(option_display_2, 48)+"|              |                                                         |"),
                    new StringBuilder("|"+textformatters.centerText(option_display_3, 48)+"|              | "+textformatters.centerText(interaction_display_1, 56)+"|"),
                    new StringBuilder("|"+textformatters.centerText(option_display_4, 48)+"|              | "+textformatters.centerText(interaction_display_2, 56)+"|"),
                    new StringBuilder("|                                                |              | "+textformatters.centerText(interaction_display_3, 56)+"|"),
                    new StringBuilder("|                   Leave (q)                    |              |                                                         |")
                ));
            }

            case "BATTLE" -> {
                // Used when in combat
                inBattle = true;
            }

            case "SHOP" -> {
                // Used when in shop
            }
        }

        // Replace playerX, playerY with player symbol // -1s due to index 0s
        HashMap<String, Integer> coords = (HashMap<String, Integer>) playerData.get("coordinates");
        Integer[] player_position = new Integer[] {coords.get("x"), coords.get("y")};
        game_screen.get(player_position[1] - 1).setCharAt(player_position[0] - 1, playerSymbol);

        System.out.println(String.join("\n", game_screen)); // Outputs entire screen
        return additional_data;
    }

    public static ArrayList<StringBuilder> renderMap(HashMap<String, Object> locationData) {
        Character interaction_symbol = '?', enemy_symbol = '!', shop_symbol = '$';
        ArrayList<StringBuilder> region_map = new ArrayList<>();
        for (StringBuilder sb : (ArrayList<StringBuilder>) locationData.get("Map")) {
            region_map.add(new StringBuilder(sb.toString()));
        }

        // Spawn POIs
        ArrayList<POIGenerator> pois = (ArrayList<POIGenerator>) locationData.get("POIs");
        for (POIGenerator poi : pois) {
            switch(poi.type) {
                case "INTERACTION" -> region_map.get(poi.y - 1).setCharAt(poi.x - 1, interaction_symbol);
                case "ENEMY" -> region_map.get(poi.y - 1).setCharAt(poi.x - 1, enemy_symbol);
                case "SHOP" -> region_map.get(poi.y - 1).setCharAt(poi.x - 1, shop_symbol);
            }
        }
        return region_map;
    }
}