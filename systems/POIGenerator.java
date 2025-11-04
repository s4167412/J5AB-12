package systems;

public class POIGenerator {
    public int x, y;
    public String id, type;
    public boolean repeatable;

    public POIGenerator(int x, int y, String id, String type, boolean repeatable) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.type = type;
        this.repeatable = repeatable;
    }
}
