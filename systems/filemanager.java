package systems;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class filemanager {
        public static HashMap<String, Object> loadData() {
        HashMap<String, Object> playerData = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("saves/save.txt"))) {
            String line;
            ArrayList<String> inventory = new ArrayList<>();
            HashMap<String, Integer> coordinates = new HashMap<>();
            HashMap<String, Integer> settings = new HashMap<>();
            HashMap<String, Boolean> progress = new HashMap<>();

            if (!reader.ready()) { // If no save file found
                System.out.println("No save file was found, you will have to create a new save.");
                return null;
            }

            while((line = reader.readLine()) != null) { // Reads whole file until empty line (null)
                switch(line) {
                    case "CHARACTER NAME" -> {
                        playerData.put("characterName", reader.readLine());
                    }

                    case "SAVE CREATED AT" -> {
                        playerData.put("saveCreatedAt", reader.readLine());
                    }

                    case "SAVE LAST PLAYED AT" -> {
                        playerData.put("saveLastPlayedAt", reader.readLine());
                    }

                    case "LOCATION" -> {
                        playerData.put("location", reader.readLine());
                    }

                    case "GLOBAL COORDINATE (X)" -> {
                        coordinates.put("x", Integer.parseInt(reader.readLine()));
                        reader.readLine();
                        coordinates.put("y", Integer.parseInt(reader.readLine()));
                        playerData.put("coordinates", coordinates);
                    }

                    case "INVENTORY" -> {
                        while((line = reader.readLine()) != null && !line.equals("END INVENTORY")) {
                            String item = line.trim();
                            inventory.add(item);
                        }
                        playerData.put("inventory", inventory);
                    }

                    case "HEALTH" -> {
                        playerData.put("health", Integer.parseInt(reader.readLine()));
                    }

                    case "COINS" -> {
                        playerData.put("coins", Integer.parseInt(reader.readLine()));
                    }

                    case "SETTINGS" -> {
                        while((line = reader.readLine()) != null && !line.equals("END SETTINGS")) {
                            // Extract option of each setting | SETTINGNAME [int]
                            String[] parts = line.split("\\[");
                            String setting = parts[0].trim();
                            Integer data = Integer.parseInt(parts[1].replace("]", "").trim());
                            settings.put(setting, data);
                        }
                        playerData.put("settings", settings);
                    }

                    case "PROGRESS" -> {
                        while((line = reader.readLine()) != null && !line.equals("END PROGRESS")) {
                            // Extract progress of each item | PROGRESSNAME [boolean]
                            String[] parts = line.split("\\[");
                            String progressItem = parts[0].trim();
                            Boolean data = Boolean.parseBoolean(parts[1].replace("]", "").trim());
                            progress.put(progressItem, data);
                        }
                        playerData.put("progress", progress);
                    }
                }
            }
        } catch(IOException e) {
            System.out.println("Error loading existing save file");
            System.err.println(e);
        }
        return playerData;
    }

    public static void createSaveFile() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        ArrayList<String> output = new ArrayList<>();
        String character_name, difficulty_selection;
        Scanner userInput = new Scanner(System.in);

        System.out.println("Welcome adventurer! What is your name? (Max xx characters)"); // TODO: ADD CHARACTER NAME LIMIT
        character_name = userInput.nextLine();

        System.out.println("What difficulty would you like to play on?\n1. Easy\n2. Medium\n3. Hard\n4. Hardcore (Single Life)");
        difficulty_selection = userInput.nextLine();
        switch(difficulty_selection) {
            case "1" -> {
                difficulty_selection = "1";
                System.out.println("Generating "+character_name+"'s save file on EASY difficulty...");
            }
            case "2" -> {
                difficulty_selection = "2";
                System.out.println("Generating "+character_name+"'s save file on MEDIUM difficulty...");
            }
            case "3" -> {
                difficulty_selection = "3";
                System.out.println("Generating "+character_name+"'s save file on HARD difficulty...");
            }
            case "4" -> {
                difficulty_selection = "4";
                System.out.println("Generating "+character_name+"'s save file on HARDCORE difficulty...");
            }
            default -> {
                difficulty_selection = "1";
                System.out.println("Generating "+character_name+"'s save file on EASY difficulty...\nSince you couldn't follow simple instructions, clearly you need the easiest difficulty.");
            }
        }

        output.add("CHARACTER NAME");
        output.add(character_name);
        output.add("SAVE CREATED AT");
        output.add(currentDateTime.toString());
        output.add("SAVE LAST PLAYED AT");
        output.add(currentDateTime.toString());
        output.add("LOCATION");
        output.add("tutoria");
        output.add("GLOBAL COORDINATE (X)");
        output.add("3"); // Starting location in tutoria
        output.add("GLOBAL COORDINATE (Y)");
        output.add("5"); // Starting location in tutoria
        output.add("INVENTORY");

        //TODO: TEMP
        output.add("sword");
        output.add("shield");
        output.add("bottle");
        output.add("plank");
        output.add("rock");

        output.add("END INVENTORY");
        output.add("HEALTH");
        output.add("100");
        output.add("COINS");
        output.add("0");
        output.add("SETTINGS");
        output.add("Difficulty [" + difficulty_selection + "]");
        output.add("END SETTINGS");
        output.add("PROGRESS");
        output.add("END PROGRESS");
        output.add("\n## SAVE FILE ##");
        output.add("# CHANGING ANY OF THESE VALUES CAN EASILY BREAK SOMETHING, KEEP A BACKUP WHEN EDITING SAVE FILES!! #");
        output.add("# FILE FOLLOWS A HEADER VALUE FORMAT #");

        deleteSaveFile();
        File savefile = new File("saves/save.txt");
        try {
            if (savefile.createNewFile()) {
                FileWriter filewriter = new FileWriter("saves/save.txt");
                filewriter.write(String.join("\n", output));
                filewriter.close();
            } else {
                System.out.println("File creation failed");
            }
        } catch(IOException e) {
            System.out.println("Error while creating save file\n" + e);
        }
    }

    public static void deleteSaveFile() {
        File savefile = new File("saves/save.txt");
        savefile.delete();
        try {
            savefile.delete();
        } catch(Exception e) {
            System.out.println("Error deleting save file\n" + e);
        }
    }

    public static Boolean saveData(HashMap<String, Object> playerData) {
        ArrayList<String> output = new ArrayList<>();
        output.add("CHARACTER NAME");
        output.add(playerData.get("characterName").toString());
        output.add("SAVE CREATED AT");
        output.add(playerData.get("saveCreatedAt").toString());
        output.add("SAVE LAST PLAYED AT");
        output.add(playerData.get("saveLastPlayedAt").toString());
        output.add("LOCATION");
        output.add(playerData.get("location").toString());
        output.add("GLOBAL COORDINATE (X)");
        HashMap<String, Integer> coordinates = (HashMap<String, Integer>) playerData.get("coordinates");
        output.add(coordinates.get("x").toString());
        output.add("GLOBAL COORDINATE (Y)");
        output.add(coordinates.get("y").toString());
        output.add("INVENTORY");
        ArrayList<String> inventory = (ArrayList<String>) playerData.get("inventory");
        for (String item : inventory) {output.add(item);}
        output.add("END INVENTORY");
        output.add("HEALTH");
        output.add(playerData.get("health").toString());
        output.add("COINS");
        output.add(playerData.get("coins").toString());
        output.add("SETTINGS");
        HashMap<String, Integer> settings = (HashMap<String, Integer>) playerData.get("settings");
        settings.forEach((Key, Value) -> output.add(Key + " [" + Value + "]"));
        output.add("END SETTINGS");
        output.add("PROGRESS");
        HashMap<String, Boolean> progress = (HashMap<String, Boolean>) playerData.get("progress");
        progress.forEach((Key, Value) -> output.add(Key + " [" + Value + "]"));
        output.add("END PROGRESS");

        File savefile = new File("saves/save.txt");
        try {
            deleteSaveFile();
            if (savefile.createNewFile()) {
                FileWriter filewriter = new FileWriter("saves/save.txt");
                filewriter.write(String.join("\n", output));
                filewriter.close();
                return true;
            } else {
                System.out.println("File creation failed");
                return false;
            }
        } catch(IOException e) {
            System.out.println("No save file was found, create a new one instead");
            return false;
        }
    }
}
