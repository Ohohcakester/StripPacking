public class FloatingRect {
    public final float width;
    public final float height;

    public FloatingRect(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public static FloatingRect create(double width, double height) {
        return new FloatingRect((float)width, (float)height);
    }

    public Rect place(float x, float y) {
        return Rect.place(x,y,width,height);
    }
}