import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

public class CaseGen {
    Random random = new Random();

	public static void main(String[] args) throws IOException {
		Scanner sc  = new Scanner(System.in);
        System.out.println("Input number of items");
		int n = sc.nextInt();
        System.out.println("Input strip width");
		int scale = sc.nextInt();
        generateCase("test.txt", n, scale);
	}
    
    public static void generateCase(String outputFile, int n, int scale) throws IOException {
        BufferedWriter fw = new BufferedWriter(new FileWriter(outputFile));
        fw.write(n + " " + scale);
		fw.newLine();
		for (int i = 0; i < n; i++) {
			int width = (int) Math.floor(widthFun(scale));
			int height = (int) Math.floor(heightFun(scale));
            width = Math.min(width, scale);
			fw.write(width + " " + height);
			fw.newLine();
		}
		fw.flush();
		fw.close();
    }
    
    public static double widthFun(int scale) {
        return (Math.random()*(Math.random()+0.15) * scale) + 1;
    }
    
    public static double heightFun(int scale) {
        return (Math.random()*(Math.random()+0.3) * scale) + 1;
    }
}