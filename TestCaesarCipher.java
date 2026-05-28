// TestCaesarCipher.java
public class TestCaesarCipher {
    public static void main(String[] args) {
        System.out.println("ТЕСТ ПРОГРАММЫ ШИФРОВАНИЯ ЦЕЗАРЯ");
        System.out.println("================================\n");

        // Тест 1: Английский текст
        System.out.println("Тест 1: Английский алфавит");
        System.out.println("---------------------------");

        String englishText = "Hello World!";
        String englishAlphabet = CaesarCipher.ENGLISH_ALPHABET;
        int key = 3;

        String encryptedEN = CaesarCipher.encrypt(englishText, key, englishAlphabet);
        String decryptedEN = CaesarCipher.decrypt(encryptedEN, key, englishAlphabet);

        System.out.println("Исходный текст: " + englishText);
        System.out.println("Ключ: " + key);
        System.out.println("Зашифрованный: " + encryptedEN);
        System.out.println("Расшифрованный: " + decryptedEN);

        // Проверка
        if (englishText.equals(decryptedEN)) {
            System.out.println("✓ Тест пройден!");
        } else {
            System.out.println("✗ Тест не пройден!");
        }

        // Тест 2: Русский текст
        System.out.println("\nТест 2: Русский алфавит");
        System.out.println("------------------------");

        String russianText = "Привет, Мир!";
        String russianAlphabet = CaesarCipher.RUSSIAN_ALPHABET;
        key = 5;

        String encryptedRU = CaesarCipher.encrypt(russianText, key, russianAlphabet);
        String decryptedRU = CaesarCipher.decrypt(encryptedRU, key, russianAlphabet);

        System.out.println("Исходный текст: " + russianText);
        System.out.println("Ключ: " + key);
        System.out.println("Зашифрованный: " + encryptedRU);
        System.out.println("Расшифрованный: " + decryptedRU);

        // Проверка
        if (russianText.equals(decryptedRU)) {
            System.out.println("✓ Тест пройден!");
        } else {
            System.out.println("✗ Тест не пройден!");
        }

        // Тест 3: Проверка brute force
        System.out.println("\nТест 3: Brute force");
        System.out.println("-------------------");

        String secretText = CaesarCipher.encrypt("HELLO", 7, englishAlphabet);
        System.out.println("Зашифрованный текст: " + secretText);
        System.out.println("Первые 3 варианта brute force:");

        java.util.ArrayList<String> results = CaesarCipher.bruteForce(secretText, englishAlphabet);
        for (int i = 0; i < 3 && i < results.size(); i++) {
            System.out.println("Вариант " + (i+1) + ": " + results.get(i));
        }

        // Тест 4: Статистический анализ
        System.out.println("\nТест 4: Статистический анализ");
        System.out.println("-----------------------------");

        String longText = "HELLO WORLD THIS IS A TEST MESSAGE FOR STATISTICAL ANALYSIS";
        String encrypted = CaesarCipher.encrypt(longText, 5, englishAlphabet);
        int foundKey = CaesarCipher.statisticalAnalysis(encrypted, englishAlphabet);

        System.out.println("Длинный текст зашифрован с ключом: 5");
        System.out.println("Найденный ключ: " + foundKey);

        if (foundKey == 5) {
            System.out.println("✓ Ключ найден правильно!");
        } else {
            System.out.println("✗ Ключ найден неверно (это нормально для коротких текстов)");
        }
    }
}