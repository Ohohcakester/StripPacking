public class Rect {
	public final int x1;
	public final int y1;
	public final int x2;
	public final int y2;
	public final int height;
	public final int width;
	
	public Rect(int x1, int y1, int x2, int y2) {
		if (x2<x1 || y2<y1) throw new UnsupportedOperationException("INVALID RECTANGLE");
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.height = y2 - y1;
		this.width = x2 - x1;
	}

	public static Rect place(int x, int y, int width, int height) {
		return new Rect(x,y,x+width,y+height);
	}
	
	public boolean overlaps(Rect other) {
		return  this.x2 > other.x1 && this.x1 < other.x2 &&
				this.y2 > other.y1 && this.y1 < other.y2;
		//if (r) System.out.println(this + " | " + other);
	}

	public String toString() {
		return "("+x1+","+y1+","+x2+","+y2+")";
	}
}