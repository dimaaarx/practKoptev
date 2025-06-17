package utils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FilesystemUtilities {

    public static int loadFromFile(String path, LocalDateTime[] dates, String[] texts, DateTimeFormatter formatter) {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null && count < dates.length) {
                try {
                    LocalDateTime date = LocalDateTime.parse(line, formatter);
                    String text = reader.readLine();
                    reader.readLine();
                    dates[count] = date;
                    texts[count] = text;
                    count++;
                } catch (DateTimeParseException | IOException e) {
                    System.out.println("Помилка з рядком. Пропущено.");
                }
            }
        } catch (IOException e) {
            System.out.println("Не вдалося відкрити файл: " + e.getMessage());
        }
        return count;
    }

    public static void saveToFile(String path, LocalDateTime[] dates, String[] texts, int count, DateTimeFormatter formatter) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (int i = 0; i < count; i++) {
                writer.write(dates[i].format(formatter));
                writer.newLine();
                writer.write(texts[i]);
                writer.newLine();
                writer.newLine();
            }
            System.out.println("Дані збережено.");
        } catch (IOException e) {
            System.out.println("Помилка запису у файл: " + e.getMessage());
        }
    }
}