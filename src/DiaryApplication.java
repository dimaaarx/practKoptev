import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import utils.FilesystemUtilities;

public class DiaryApplication {
    private static final int MAX = 50;
    private LocalDateTime[] dates = new LocalDateTime[MAX];
    private String[] texts = new String[MAX];
    private int count = 0;
    private DateTimeFormatter formatter;

    public void run() {
        Scanner sc = new Scanner(System.in);
        formatter = chooseFormat(sc);

        System.out.print("1. Створити новий щоденник\n2. Завантажити з файлу\nВаш вибір: ");
        if (sc.nextLine().equals("2")) {
            System.out.print("Шлях до файлу: ");
            String path = sc.nextLine();
            count = FilesystemUtilities.loadFromFile(path, dates, texts, formatter);
        }

        boolean running = true;
        while (running) {
            System.out.println("\n1. Додати\n2. Видалити\n3. Показати\n4. Вийти");
            System.out.print("Ваш вибір: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    addEntry(sc);
                    break;
                case "2":
                    deleteEntry(sc);
                    break;
                case "3":
                    showEntries();
                    break;
                case "4":
                    System.out.print("Зберегти у файл? (так/ні): ");
                    if (sc.nextLine().equalsIgnoreCase("так")) {
                        System.out.print("Файл: ");
                        String savePath = sc.nextLine();
                        FilesystemUtilities.saveToFile(savePath, dates, texts, count, formatter);
                    }
                    running = false;
                    break;
            }
        }

        sc.close();
    }

    private DateTimeFormatter chooseFormat(Scanner sc) {
        System.out.println("1. yyyy-MM-dd HH:mm\n2. dd.MM.yyyy HH:mm\n3. Власний формат");
        while (true) {
            System.out.print("Оберіть формат дати: ");
            String choice = sc.nextLine();
            try {
                if (choice.equals("1")) return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                if (choice.equals("2")) return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                if (choice.equals("3")) {
                    System.out.print("Введіть власний формат: ");
                    return DateTimeFormatter.ofPattern(sc.nextLine());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Невірний формат.");
            }
        }
    }

    private void addEntry(Scanner sc) {
        if (count >= MAX) {
            System.out.println("Досягнуто максимуму записів.");
            return;
        }

        System.out.print("Введіть дату (" + formatter.toString() + "): ");
        try {
            LocalDateTime date = LocalDateTime.parse(sc.nextLine(), formatter);
            System.out.println("Введіть текст (порожній рядок - завершення):");
            StringBuilder text = new StringBuilder();
            while (true) {
                String line = sc.nextLine();
                if (line.isEmpty()) break;
                text.append(line).append(" ");
            }
            dates[count] = date;
            texts[count] = text.toString().trim();
            count++;
            System.out.println("Запис додано.");
        } catch (DateTimeParseException e) {
            System.out.println("Невірна дата.");
        }
    }

    private void deleteEntry(Scanner sc) {
        System.out.print("Введіть дату для видалення (" + formatter.toString() + "): ");
        try {
            LocalDateTime date = LocalDateTime.parse(sc.nextLine(), formatter);
            for (int i = 0; i < count; i++) {
                if (dates[i].equals(date)) {
                    for (int j = i; j < count - 1; j++) {
                        dates[j] = dates[j + 1];
                        texts[j] = texts[j + 1];
                    }
                    dates[count - 1] = null;
                    texts[count - 1] = null;
                    count--;
                    System.out.println("Запис видалено.");
                    return;
                }
            }
            System.out.println("Запис з такою датою не знайдено.");
        } catch (DateTimeParseException e) {
            System.out.println("Невірна дата.");
        }
    }

    private void showEntries() {
        if (count == 0) {
            System.out.println("Щоденник порожній.");
            return;
        }

        for (int i = 0; i < count; i++) {
            System.out.println(dates[i].format(formatter) + ": " + texts[i]);
        }
    }
}



