import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteForce extends StripPacking {
	
	public int maxHeight;
	
	public BruteForce(FloatingRect[] floatingRects, int width) {
		super(floatingRects, width);
		// maxHeight is sum of all rectangle heights plus one. nothing goes above this.
		maxHeight = Arrays.stream(floatingRects).map(floatingRect -> floatingRect.height).reduce(1, (a, b) -> a + b);
	}
	
	@Override
	public void execute() {
		this.height = maxHeight;
		System.out.println(maxHeight);
	}
	
}
