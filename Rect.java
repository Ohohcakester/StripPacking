public class Rect {
	public float x1;
	public float y1;
	public float x2;
	public float y2;
	
	public Rect(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public boolean overlaps(Rect other) {
		return this.x2 < other.x1 || this.x1 > other.x2 || this.y2 < other.y1 || this.y1 < other.y2;
	}
}