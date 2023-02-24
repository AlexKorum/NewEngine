package scenes.entites.components;

public abstract class Component {
    private int tag;
    private String name;

    public Component(int tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public int getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }
}
