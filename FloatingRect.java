public class FloatingRect {
    public final float width;
    public final float height;
    public int id; // assignable with no consequence

    public FloatingRect(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public FloatingRect(FloatingRect copy) {
        this.width = copy.width;
        this.height = copy.height;
        this.id = copy.id;
    }

    public static FloatingRect create(double width, double height) {
        return new FloatingRect((float)width, (float)height);
    }

    public Rect place(float x, float y) {
        return Rect.place(x,y,width,height);
    }

    @Override
    public String toString() {
        return "("+width+","+height+")";
    }
}