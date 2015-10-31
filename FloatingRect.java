public class FloatingRect {
    public final float width;
    public final float height;

    public FloatingRect(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public Rect place(float x, float y) {
        return Rect.place(x,y,width,height);
    }
}