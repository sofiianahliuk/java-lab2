import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    static Scanner sc;

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, "UTF-8"));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err), true, "UTF-8"));
        Locale.setDefault(Locale.forLanguageTag("uk-UA"));
        sc = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8))
                .useLocale(Locale.forLanguageTag("uk-UA"));
        while (true) {
            System.out.println("\nЛАБОРАТОРНА РОБОТА №2");
            System.out.println("1 - Грошові суми");
            System.out.println("2 - Прямі на координатній площині");
            System.out.println("3 - Дуже великі числа");
            System.out.println("0 - Вихід");
            System.out.print("Ваш вибір: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Введіть номер пункту меню.");
                continue;
            }

            if (choice == 1) moneyTask();
            else if (choice == 2) lineTask();
            else if (choice == 3) numberTask();
            else if (choice == 0) break;
            else System.out.println("Неправильний пункт меню.");
        }
    }

    static String такЧиНі(boolean value) {
        return value ? "так" : "ні";
    }

    // Завдання 1. Варіант 12
    static void moneyTask() {
        System.out.print("Введіть першу суму: ");
        Money m1 = new Money(sc.nextLine());
        System.out.print("Введіть другу суму: ");
        Money m2 = new Money(sc.nextLine());

        System.out.println("Перша сума: " + m1);
        System.out.println("Друга сума: " + m2);
        System.out.println("Додавання: " + m1.add(m2));
        System.out.println("Порівняння (перша більша): " + такЧиНі(m1.compareTo(m2) > 0));
        System.out.println("Суми рівні: " + такЧиНі(m1.equals(m2)));

        if (m1.toKopecks() >= m2.toKopecks())
            System.out.println("Віднімання: " + m1.subtract(m2));
        else
            System.out.println("Віднімання першої суми від другої: " + m2.subtract(m1));

        if (m2.toKopecks() != 0)
            System.out.println("Ділення сум: " + m1.divide(m2));

        System.out.print("На яке число поділити першу суму? ");
        double d = Double.parseDouble(sc.nextLine().replace(',', '.'));
        System.out.println("Результат: " + m1.divideBy(d));
    }

    static class Money {
        // Номінали в копійках: 500 грн, 200 грн ... 1 коп.
        static final int[] nominal = {50000, 20000, 10000, 5000, 2000, 1000,
                500, 200, 100, 50, 25, 10, 5, 2, 1};
        static int count = 0;
        long[] amount = new long[nominal.length];

        Money(String s) {
            this((long)(Double.parseDouble(s.replace(',', '.')) * 100 + 0.5));
        }

        Money(long kopecks) {
            if (kopecks < 0) throw new IllegalArgumentException("Сума не може бути від'ємною.");
            for (int i = 0; i < nominal.length; i++) {
                amount[i] = kopecks / nominal[i];
                kopecks %= nominal[i];
            }
            count++;
        }

        long toKopecks() {
            long sum = 0;
            for (int i = 0; i < nominal.length; i++)
                sum += amount[i] * nominal[i];
            return sum;
        }

        Money add(Money m) {
            return new Money(toKopecks() + m.toKopecks());
        }

        Money subtract(Money m) {
            return new Money(toKopecks() - m.toKopecks());
        }

        double divide(Money m) {
            return (double) toKopecks() / m.toKopecks();
        }

        Money divideBy(double d) {
            if (d <= 0) throw new IllegalArgumentException("Дільник має бути більшим за нуль.");
            return new Money(Math.round(toKopecks() / d));
        }

        int compareTo(Money m) {
            return Long.compare(toKopecks(), m.toKopecks());
        }

        public String toString() {
            return (toKopecks() / 100) + "," +
                    String.format("%02d", toKopecks() % 100) + " грн";
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Money)) return false;
            return toKopecks() == ((Money)obj).toKopecks();
        }
    }

    // Завдання 2. Варіант 13
    static void lineTask() {
        System.out.println("Перша пряма ax + by + c = 0");
        System.out.print("Введіть a, b, c через пробіл: ");
        String[] first = sc.nextLine().split(" ");
        Line l1 = new Line(Double.parseDouble(first[0].replace(',', '.')),
                Double.parseDouble(first[1].replace(',', '.')), Double.parseDouble(first[2].replace(',', '.')));

        System.out.println("Друга пряма ax + by + c = 0");
        System.out.print("Введіть a, b, c через пробіл: ");
        String[] second = sc.nextLine().split(" ");
        Line l2 = new Line(Double.parseDouble(second[0].replace(',', '.')),
                Double.parseDouble(second[1].replace(',', '.')), Double.parseDouble(second[2].replace(',', '.')));

        System.out.print("Введіть координати точки x та y через пробіл: ");
        String[] point = sc.nextLine().split(" ");
        double x = Double.parseDouble(point[0].replace(',', '.'));
        double y = Double.parseDouble(point[1].replace(',', '.'));

        System.out.println("Точка належить першій прямій: " + такЧиНі(l1.contains(x, y)));
        l1.intersection(l2);
    }

    static class Line {
        static int count = 0;
        double a, b, c;

        Line(double a, double b, double c) {
            this.a = a;
            this.b = b;
            this.c = c;
            count++;
        }

        Line(double a, double b) {
            this(a, b, 0);
        }

        boolean contains(double x, double y) {
            return Math.abs(a * x + b * y + c) < 0.000001;
        }

        void intersection(Line l) {
            double det = a * l.b - l.a * b;
            if (Math.abs(det) < 0.000001) {
                System.out.println("Прямі паралельні або співпадають.");
            } else {
                double x = (b * l.c - l.b * c) / det;
                double y = (c * l.a - l.c * a) / det;
                System.out.println("Точка перетину: (" + x + "; " + y + ")");
            }
        }

        public String toString() {
            return a + "x + " + b + "y + " + c + " = 0";
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Line)) return false;
            Line l = (Line)obj;
            return Math.abs(a*l.b-l.a*b) < 0.000001
                    && Math.abs(a*l.c-l.a*c) < 0.000001
                    && Math.abs(b*l.c-l.b*c) < 0.000001;
        }
    }

    // Завдання 3. Варіант 2
    static void numberTask() {
        System.out.print("Введіть перше велике ціле число: ");
        VeryLongNumber n1 = new VeryLongNumber(sc.nextLine());
        System.out.print("Введіть друге велике ціле число: ");
        VeryLongNumber n2 = new VeryLongNumber(sc.nextLine());

        System.out.println("Додавання: " + n1.add(n2));
        System.out.println("Віднімання: " + n1.subtract(n2));
        System.out.println("Множення: " + n1.multiply(n2));

        if (!n2.value.equals(BigInteger.ZERO)) {
            System.out.println("Ділення: " + n1.divide(n2));
        } else {
            System.out.println("На нуль ділити не можна.");
        }

        System.out.print("Введіть натуральний показник степеня: ");
        int power = Integer.parseInt(sc.nextLine());
        System.out.println("Степінь першого числа: " + n1.pow(power));
    }

    static class VeryLongNumber {
        static int count = 0;
        BigInteger value;

        VeryLongNumber(String s) {
            value = new BigInteger(s);
            count++;
        }

        VeryLongNumber(BigInteger n) {
            value = n;
            count++;
        }

        VeryLongNumber add(VeryLongNumber n) {
            return new VeryLongNumber(value.add(n.value));
        }

        VeryLongNumber subtract(VeryLongNumber n) {
            return new VeryLongNumber(value.subtract(n.value));
        }

        VeryLongNumber multiply(VeryLongNumber n) {
            return new VeryLongNumber(value.multiply(n.value));
        }

        VeryLongNumber divide(VeryLongNumber n) {
            return new VeryLongNumber(value.divide(n.value));
        }

        VeryLongNumber pow(int p) {
            return new VeryLongNumber(value.pow(p));
        }

        public String toString() {
            return value.toString();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof VeryLongNumber)) return false;
            return value.equals(((VeryLongNumber)obj).value);
        }
    }
}
