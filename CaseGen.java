import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

public class CaseGen {
	public static void main(String[] args) throws IOException {
		BufferedWriter fw = new BufferedWriter(new FileWriter("test.txt"));
		Scanner sc  = new Scanner(System.in);
		int n = sc.nextInt();
		int scale = sc.nextInt();
		fw.write(n + " " + scale);
		fw.newLine();
		for (int i = 0; i < n; i++) {
			int width = (int) Math.floor((Math.random() * scale) + 1);
			int height = (int) Math.floor((Math.random() * scale) + 1);
			fw.write(width + " " + height);
			fw.newLine();
		}
		fw.flush();
		fw.close();
	}
}