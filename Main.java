import java.util.Scanner; //импортирую класс Scanner для ввода данных от юзера
import java.util.TreeMap; //TreeMap - для хранения арабских и римских чисел

public class Main { //главный класс. public - это значит, что из любого места программы можно использовать члены этого класса 
    public static void main(String[] args) { /*главный метод. его цель - запустить программу.
                                            static - метод статический (связан именно с классом, а не с его экземпляром)
                                            void - метод не возвращает значения. (String[] args) - массив строк*/
        Scanner scn = new Scanner(System.in); /*scn - переменная для ссылки на объект класса Scanner. scn готов считывать данные с клавиатуры.
                                            new - ключевое слово, для создания нового объекта класса.
                                            System.in - это стандартный ввод с клавиатуры*/
        System.out.print("Введите выражение: "); 
        String exp = scn.nextLine(); /*exp - переменная для хранения считанных данных от юзера.
                                    scn.nextLine - для считывания следующей строки от юзера.
                                    знак Точка - для доступа к членам объекта или класса*/
        try { /*блок try-catch для обработки исключений (профилактики ошибок).
                В try находится код, в котором может находиться ошибка*/ 
            String result = calc(exp); /*переменная result хранит результат работы калькулятора calc с аргументом exp.
                                    знак () - внутри них то, с чем работает метод calc*/
            System.out.println(result);
        } catch (IllegalArgumentException e) { /*catch обрабатывает исключение конкретного типа. IllegalArgumentException - тип исключения.
                                            e - переменная, хранящая информацию об ошибке*/
            System.out.println(e.getMessage()); //getMessage - метод для получения текстового описания ошибки из переменной e.
        }
    }
    
    
    public static String calc(String input) { //метод calc обрабатывает входящее мат выражение input
        Calculator.Converter converter = new Calculator.Converter();
        String[] actions = {"+", "-", "/", "*"}; //массив допустимых мат операторов
        String[] regexActions = {"\\+", "-", "/", "\\*"};
        int actionIndex = -1;
        for (int i = 0; i < actions.length; i++) {
            if (input.contains(actions[i])) {
                actionIndex = i;
                break;
            }
        }
        if (actionIndex == -1) {
            throw new IllegalArgumentException("Неверный формат выражения");
        }
        String[] data = input.split(regexActions[actionIndex]);
        int a, b; //создание переменных для хранения операндов
        if (converter.isRoman(data[0]) && converter.isRoman(data[1])) {
            a = converter.romanToInt(data[0]);
            b = converter.romanToInt(data[1]);
            if (a < 1 || a > 10 || b < 1 || b > 10) {
                throw new IllegalArgumentException("Ошибка: Числа должны быть в диапазоне от I до X вкл");
            }
        } else {
            try {
                a = Integer.parseInt(data[0].trim());
                b = Integer.parseInt(data[1].trim());
                if (a < 1 || a > 10 || b < 1 || b > 10) {
                    throw new IllegalArgumentException("Ошибка: Числа должны быть в диапазоне от 1 до 10 вкл");
                }
            } catch (NumberFormatException e) {
                if (converter.isRoman(data[0]) || converter.isRoman(data[1])) {
                    throw new IllegalArgumentException("Ошибка: Используются одновременно разные системы счисления");
                } else {
                    throw new IllegalArgumentException("Ошибка: Числа должны быть целыми. В диапазоне от 1 до 10 вкл");
                }
            }
        }
        int result; //объявление переменной для хранения результата
        switch (actions[actionIndex]) { //блок switch-case
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            default:
                if (b == 0) {
                    throw new IllegalArgumentException("Ошибка: Делить на 0 нельзя");
                }
                result = a / b;
                break;
        }
        if (converter.isRoman(data[0]) && converter.isRoman(data[1])) {
            if (result < 1) {
                throw new IllegalArgumentException("Ошибка: Результат операции с римскими числами не может быть меньше 1");
            }
            return converter.intToRoman(result);
        } else {
            return String.valueOf(result);
        }
    }
    
    
    static class Calculator { //внутренний класс класса Main. 
        static class Converter { //внутренний класс класса Calculator
            TreeMap<Character, Integer> romanKeyMap = new TreeMap<>();
            TreeMap<Integer, String> arabianKeyMap = new TreeMap<>();
            public Converter() {
                romanKeyMap.put('I', 1);
                romanKeyMap.put('V', 5);
                romanKeyMap.put('X', 10);
                romanKeyMap.put('L', 50);
                romanKeyMap.put('C', 100);
                romanKeyMap.put('D', 500);
                romanKeyMap.put('M', 1000);
                
                arabianKeyMap.put(1000, "M");
                arabianKeyMap.put(900, "CM");
                arabianKeyMap.put(500, "D");
                arabianKeyMap.put(400, "CD");
                arabianKeyMap.put(100, "C");
                arabianKeyMap.put(90, "XC");
                arabianKeyMap.put(50, "L");
                arabianKeyMap.put(40, "XL");
                arabianKeyMap.put(10, "X");
                arabianKeyMap.put(9, "IX");
                arabianKeyMap.put(5, "V");
                arabianKeyMap.put(4, "IV");
                arabianKeyMap.put(1, "I");
            }
            public boolean isRoman(String number){
                return romanKeyMap.containsKey(number.charAt(0));
            }
            public String intToRoman(int number) {
                String roman = "";
                int arabianKey;
                do { //блок цикла do-while
                    arabianKey = arabianKeyMap.floorKey(number);
                    roman += arabianKeyMap.get(arabianKey);
                    number -= arabianKey;
                } while (number != 0);
                return roman;
            }
            public int romanToInt(String s) {
                int end = s.length() - 1;
                char[] arr = s.toCharArray();
                int arabian;
                int result = romanKeyMap.get(arr[end]);
                for (int i = end - 1; i >= 0; i--) {
                    arabian = romanKeyMap.get(arr[i]);
                    if (arabian < romanKeyMap.get(arr[i + 1])) {
                        result -= arabian;
                    } else {
                        result += arabian;
                    }
                }
                return result; //возврат конечного результата
            }
        }
    }
}