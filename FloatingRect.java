public class FloatingRect {
    public final int width;
    public final int height;
    public int id; // assignable with no consequence

    public FloatingRect(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public FloatingRect(FloatingRect copy) {
        this.width = copy.width;
        this.height = copy.height;
        this.id = copy.id;
    }

    public static FloatingRect create(double width, double height) {
        return new FloatingRect((int)width, (int)height);
    }

    public Rect place(int x, int y) {
        return Rect.place(x,y,width,height);
    }

    @Override
    public String toString() {
        return "("+width+","+height+")";
    }
}