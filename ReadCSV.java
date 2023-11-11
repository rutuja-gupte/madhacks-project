import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class ReadCSV {
  static double[][] csvArray = new double[1000][1500];

  public double[][] readFile(String file) {

    try {
      File testFile = new File(file);
      Scanner scanner = new Scanner(testFile);

      scanner.useDelimiter(",");

      int lineNum = 0;
      while (scanner.hasNextLine()) {
        String[] fields = scanner.nextLine().split(",");
        double[] doubles = new double[fields.length];

        for (int i = 0; i < fields.length; i++) {
          doubles[i] = Double.parseDouble(fields[i]);
        }

        for (int i = 0; i < fields.length; i++) {
          csvArray[lineNum] = doubles;
        }
        lineNum++;
      }
    } catch (FileNotFoundException e) {
      System.out.print("Wrong");
    }

    return csvArray;
  }
}
