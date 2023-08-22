package ru.basejava.webapp;

import ru.basejava.webapp.model.Organization;
import ru.basejava.webapp.util.DateUtil;

import javax.swing.text.Position;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

public class MainFile {
    public static void main(String[] args) {
        File dir = new File("D:\\");
        File file = new File("testfile.txt");
        File f = new File(dir, "testfile.txt");
        try {
            System.out.println(f.createNewFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(f.exists());
        System.out.println(f.getAbsolutePath());
        try {
            System.out.println(f.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readDir(File dir, int deep) {
        for (File f : dir.listFiles()) {
            for (int i = 0; i < deep; i++) {
                System.out.print("   ");
            }
            System.out.println(f.getName());
            if (f.isDirectory())
                readDir(f, deep++);
        }
    }
}
