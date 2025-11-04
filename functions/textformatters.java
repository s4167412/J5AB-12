package functions;
import java.util.ArrayList;

public class textformatters {
    public static String centerText(String text, int width) {
        int padding = width - text.length();
        int padding_start = padding / 2;
        int padding_end = padding - padding_start;

        if (padding <= 0) { // If centering is not possible
            return text;
        }

        return " ".repeat(padding_start) + text + " ".repeat(padding_end);
    }
    public static String[] wrapText(String text, int width) {
        ArrayList<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : text.split(" ")) {
            if (currentLine.length() + word.length() + 1 > width) {
                lines.add(currentLine.toString().trim());
                currentLine.setLength(0);
            }
            if (currentLine.length() > 0) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString().trim());
        }

        return lines.toArray(new String[0]);
    }
}