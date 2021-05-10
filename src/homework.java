import pk.usc.ai.FolEngine;
import pk.usc.ai.Unit.KBSentence;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;

public class homework {
    static ArrayList<String> ll = new ArrayList<>();
    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("input.txt");
            Scanner sc = new Scanner(fis);

            int qCount = Integer.parseInt(sc.nextLine());
            for (int i = 0; i < qCount; i++) {
                FolEngine.addKBQuery(sc.nextLine());
                //System.out.println(sc.nextLine());
            }

            int kbCount = Integer.parseInt(sc.nextLine());
            for (int i = 0; i < kbCount; i++) {
                FolEngine.addKBSentenceIfPossible(sc.nextLine());
                //System.out.println(kbSentence);
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //FolEngine.printKB2();
        FolEngine.runEngine();

        try {
            FileOutputStream fileWriter = new FileOutputStream("output.txt");
            for (Boolean querySol: FolEngine.QuerySol) {
                System.out.println(String.valueOf(querySol).toUpperCase(Locale.ROOT));
                fileWriter.write((String.valueOf(querySol).toUpperCase(Locale.ROOT) + "\n").getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
