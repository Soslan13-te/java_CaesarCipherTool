import java.io.*;
import java.util.*;

public class CaesarCipher {

    // Алфавиты
    public static final String ENGLISH_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String RUSSIAN_ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

    public static void main(String[] args) {
        // Создаем сканер для чтения с консоли
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ПРОГРАММА ШИФРОВАНИЯ ЦЕЗАРЯ ===");
        System.out.println("Что вы хотите сделать?");
        System.out.println("1. Зашифровать текст");
        System.out.println("2. Расшифровать текст (зная ключ)");
        System.out.println("3. Взломать шифр (brute force)");
        System.out.println("4. Взломать шифр (статистический анализ)");
        System.out.print("Ваш выбор (1-4): ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // очищаем буфер

        // Выбираем алфавит
        System.out.println("\nВыберите алфавит:");
        System.out.println("1. Английский");
        System.out.println("2. Русский");
        System.out.print("Ваш выбор (1-2): ");

        int alphabetChoice = scanner.nextInt();
        scanner.nextLine(); // очищаем буфер

        String alphabet;
        if (alphabetChoice == 1) {
            alphabet = ENGLISH_ALPHABET;
        } else {
            alphabet = RUSSIAN_ALPHABET;
        }

        // Спрашиваем про файлы
        System.out.print("\nТекст из файла? (да/нет): ");
        String fromFile = scanner.nextLine().toLowerCase();

        String text = "";
        String outputFile = "";

        if (fromFile.equals("да")) {
            System.out.print("Введите имя входного файла: ");
            String inputFile = scanner.nextLine();

            // Читаем файл
            text = readFromFile(inputFile);
            if (text == null) {
                System.out.println("Ошибка при чтении файла!");
                return;
            }

            System.out.print("Сохранить результат в файл? (да/нет): ");
            String toFile = scanner.nextLine().toLowerCase();
            if (toFile.equals("да")) {
                System.out.print("Введите имя выходного файла: ");
                outputFile = scanner.nextLine();
            }
        } else {
            System.out.print("Введите текст: ");
            text = scanner.nextLine();
        }

        // Выполняем выбранное действие
        if (choice == 1) {
            // Шифрование
            System.out.print("Введите ключ (число): ");
            int key = scanner.nextInt();

            String encrypted = encrypt(text, key, alphabet);
            System.out.println("\nЗашифрованный текст:");
            System.out.println(encrypted);

            if (!outputFile.isEmpty()) {
                writeToFile(outputFile, encrypted);
                System.out.println("Результат сохранен в файл: " + outputFile);
            }

        } else if (choice == 2) {
            // Расшифровка
            System.out.print("Введите ключ (число): ");
            int key = scanner.nextInt();

            String decrypted = decrypt(text, key, alphabet);
            System.out.println("\nРасшифрованный текст:");
            System.out.println(decrypted);

            if (!outputFile.isEmpty()) {
                writeToFile(outputFile, decrypted);
                System.out.println("Результат сохранен в файл: " + outputFile);
            }

        } else if (choice == 3) {
            // Brute force
            System.out.println("\nЗапуск brute force...");
            System.out.println("Будут показаны первые 100 символов для каждого ключа:\n");

            ArrayList<String> results = bruteForce(text, alphabet);

            for (int i = 0; i < results.size(); i++) {
                System.out.println("Ключ " + (i + 1) + ": " + results.get(i));
            }

            if (!outputFile.isEmpty()) {
                // Сохраняем все варианты в файл
                StringBuilder allResults = new StringBuilder();
                for (int i = 0; i < results.size(); i++) {
                    allResults.append("Ключ ").append(i + 1).append(": ").append(results.get(i)).append("\n");
                    allResults.append("-".repeat(50)).append("\n");
                }
                writeToFile(outputFile, allResults.toString());
                System.out.println("\nВсе варианты сохранены в файл: " + outputFile);
            }

        } else if (choice == 4) {
            // Статистический анализ
            System.out.println("\nЗапуск статистического анализа...");

            int foundKey = statisticalAnalysis(text, alphabet);
            System.out.println("Предположительный ключ: " + foundKey);

            String decrypted = decrypt(text, foundKey, alphabet);
            System.out.println("\nРасшифрованный текст:");
            System.out.println(decrypted);

            if (!outputFile.isEmpty()) {
                writeToFile(outputFile, decrypted);
                System.out.println("Результат сохранен в файл: " + outputFile);
            }
        }

        scanner.close();
    }

    // Метод для шифрования
    public static String encrypt(String text, int key, String alphabet) {
        String result = "";

        // Нормализуем ключ
        key = key % alphabet.length();
        if (key < 0) {
            key = key + alphabet.length();
        }

        // Проходим по каждому символу
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            char encryptedChar = ch;

            // Проверяем, есть ли символ в алфавите
            int index = alphabet.indexOf(Character.toUpperCase(ch));

            if (index >= 0) {
                // Символ есть в алфавите
                int newIndex = (index + key) % alphabet.length();

                if (Character.isUpperCase(ch)) {
                    // Заглавная буква
                    encryptedChar = alphabet.charAt(newIndex);
                } else {
                    // Строчная буква
                    encryptedChar = Character.toLowerCase(alphabet.charAt(newIndex));
                }
            }

            result = result + encryptedChar;
        }

        return result;
    }

    // Метод для расшифровки
    public static String decrypt(String text, int key, String alphabet) {
        // Расшифровка - это шифрование с отрицательным ключом
        return encrypt(text, alphabet.length() - (key % alphabet.length()), alphabet);
    }

    // Метод для brute force
    public static ArrayList<String> bruteForce(String text, String alphabet) {
        ArrayList<String> results = new ArrayList<>();

        // Пробуем все возможные ключи (кроме 0)
        for (int key = 1; key < alphabet.length(); key++) {
            String decrypted = decrypt(text, key, alphabet);

            // Берем только первые 100 символов для просмотра
            String preview;
            if (decrypted.length() > 100) {
                preview = decrypted.substring(0, 100) + "...";
            } else {
                preview = decrypted;
            }

            results.add(preview);
        }

        return results;
    }

    // Метод для статистического анализа
    public static int statisticalAnalysis(String text, String alphabet) {
        // Массив для подсчета частоты букв
        int[] frequencies = new int[alphabet.length()];

        // Сначала считаем частоту всех букв
        for (int i = 0; i < text.length(); i++) {
            char ch = Character.toUpperCase(text.charAt(i));
            int index = alphabet.indexOf(ch);

            if (index >= 0) {
                frequencies[index]++;
            }
        }

        // Находим самую частую букву
        int maxFrequency = 0;
        int maxIndex = 0;

        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > maxFrequency) {
                maxFrequency = frequencies[i];
                maxIndex = i;
            }
        }


        char mostFrequentLetter;
        if (alphabet.equals(ENGLISH_ALPHABET)) {
            mostFrequentLetter = 'E'; // самая частая буква в английском
        } else {
            mostFrequentLetter = 'О'; // самая частая буква в русском
        }

        // Вычисляем предполагаемый ключ
        int mostFrequentIndex = alphabet.indexOf(mostFrequentLetter);
        int key = maxIndex - mostFrequentIndex;

        if (key < 0) {
            key = key + alphabet.length();
        }

        return key;
    }

    // Метод для чтения из файла
    public static String readFromFile(String fileName) {
        try {
            // Проверяем, существует ли файл
            File file = new File(fileName);
            if (!file.exists()) {
                System.out.println("Файл не найден: " + fileName);
                return null;
            }

            // Проверяем размер файла
            if (file.length() > 100000000) { // 100MB
                System.out.println("Файл слишком большой (>100MB)!");
                return null;
            }

            // Читаем файл построчно
            StringBuilder content = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            reader.close();
            return content.toString();

        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
            return null;
        }
    }

    // Метод для записи в файл
    public static void writeToFile(String fileName, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(content);
            writer.close();
            System.out.println("Успешно сохранено в файл: " + fileName);

        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}