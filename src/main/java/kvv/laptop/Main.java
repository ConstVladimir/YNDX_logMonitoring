package kvv.laptop;

import javax.swing.text.DateFormatter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
//import java.time.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Scanner;

public class Main {

    private static final String rscPath = "/src/main/resources/";
    private static final String inputFile = "input.txt";
    private static final String outputFile = "output.txt";

    public static void main(String[] args) {

        int msec = 60 * 1000;
        int N = 3;
        String needDT ="";

        Path currentRelativePath = Paths.get("");
        String inF = currentRelativePath.toAbsolutePath() + rscPath + inputFile;
        String outF = currentRelativePath.toAbsolutePath() + rscPath + outputFile;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ArrayDeque <Long> errTimes = new ArrayDeque<>(N);

        try {
            File file = new File(inF);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String nowStr = scanner.nextLine();
                if (nowStr.startsWith("ERROR", 22))
                {
                    String dt = nowStr.substring(1,20);
                    LocalDateTime ldt = LocalDateTime.parse(dt, dtf);
                    ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
                    Long tme = zdt.toInstant().toEpochMilli();
                    errTimes.addLast(tme);
                    if (errTimes.size() == N){
                        if (errTimes.getLast()-errTimes.getFirst()<msec){
                            needDT = dt;
                            System.out.println(needDT);
                            break;
                        }
                        else {
                            errTimes.removeFirst();
                        }
                    }
                }
                System.out.println(nowStr);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try (FileWriter file = new FileWriter(outF)) {
            file.write(needDT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
