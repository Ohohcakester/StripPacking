public class Rect {
	private static final float BUFFER = 0.0001f;
	public final float x1;
	public final float y1;
	public final float x2;
	public final float y2;
	
	public Rect(float x1, float y1, float x2, float y2) {
		if (x2<x1 || y2<y1) throw new UnsupportedOperationException("INVALID RECTANGLE");
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public static Rect place(float x, float y, float width, float height) {
		return new Rect(x,y,x+width,y+height);
	}
	
	public boolean overlaps(Rect other) {
		return  this.x2 > other.x1+BUFFER && this.x1+BUFFER < other.x2 &&
				this.y2 > other.y1+BUFFER && this.y1+BUFFER < other.y2;
		//if (r) System.out.println(this + " | " + other);
	}

	public String toString() {
		return "("+x1+","+y1+","+x2+","+y2+")";
	}
}