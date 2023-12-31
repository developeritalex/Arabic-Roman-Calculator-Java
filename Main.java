import java.util.Scanner; //импортирую класс Scanner для ввода данных от юзера
import java.util.TreeMap; //TreeMap - для хранения арабских и римских чисел

public class Main { //главный класс. public - это значит, что из любого места программы можно использовать члены этого класса 
    public static void main(String[] args) { /*главный метод. его цель - запустить программу.
                                            static - метод статический (связан именно с классом, а не с его экземпляром)
                                            void - метод не возвращает значения. (String[] args) - массив строк*/
        Scanner scn = new Scanner(System.in); /*scn - переменная для ссылки на объект класса Scanner. scn готов считывать данные с клавиатуры.
                                            new - ключевое слово для создания нового объекта класса.
                                            System.in - это стандартный ввод с клавиатуры*/
        System.out.print("Введите выражение: ");
        String exp = scn.nextLine(); /*exp - переменная для хранения считанных данных от юзера.
                                    scn.nextLine - для считывания следующей строки от юзера.
                                    знак Точка - для доступа к членам объекта или класса*/
        try { /*блок try-catch для обработки исключений (предотвращения ошибок).
                В блоке try находится код, в котором может возникнуть ошибка*/
            String result = calc(exp); /*переменная result хранит результат работы калькулятора calc с аргументом exp.
                                    знак () - внутри них то, с чем работает метод calc*/
            System.out.println(result);
        } catch (IllegalArgumentException e) { /*блок catch обрабатывает исключение конкретного типа. IllegalArgumentException - тип исключения.
                                            e - переменная, хранящая информацию об ошибке*/
            System.out.println(e.getMessage()); //getMessage - метод для получения текстового описания ошибки из переменной e.
        }
    }

    public static String calc(String input) { //метод calc обрабатывает входящее мат выражение input от юзера
        Calculator.Converter converter = new Calculator.Converter(); //создание экземпляра класса Converter для конвертации чисел
        String[] actions = {"+", "-", "/", "*"}; //actions - массив допустимых мат операторов
        String[] regexActions = {"\\+", "-", "/", "\\*"}; //regexActions - массив регулярных выражений для поиска операторов в строке
        int actionIndex = -1; //переменная для хранения индекса массива операторов. индекс -1 означает отсутствие операторов
        for (int i = 0; i < actions.length; i++) { /*цикл for идет по массиву actions. Переменная i для отслеживания текущего индекса в массиве.
                                                Цикл будет выполняться, пока значение переменной i меньше длины массива actions.
                                                i++ - зто оператор инкремента (увеличения) переменной i на 1 после каждой итерации цикла.*/
            if (input.contains(actions[i])) { /*если input (с помощью метода contains) определит наличие мат оператора,
                                        то индекс этого оператора сохранится в переменную actionIndex*/
                actionIndex = i;
                break; //выход из цикла
            }
        }
        if (actionIndex == -1) { //если оператор не найден во входной строке, то
            throw new IllegalArgumentException("Неверный формат выражения"); //выбрасывается исключение
        }
        String[] data = input.split(regexActions[actionIndex]); /*здесь разделение входной строки input на операнды. Результат разделения сохраняется в массиве data.
                                                            regexActions[actionIndex] - это регулярное выражение, соответствующее оператору, найденному в строке input.
                                                            split - метод строки, разделяющий строку на подстроки*/
        int a, b; //создание переменных для хранения операндов
        if (converter.isRoman(data[0]) && converter.isRoman(data[1])) { /*условие проверяет, являются ли оба операнда римскими числами. 
                                                                Метод isRoman из экземпляра класса Converter проверяет, является ли переданная строка римским числом.*/
            a = converter.romanToInt(data[0]); /*a и b выполняются, если оба операнда являются римскими числами.
                                            data[0] и data[1] - первый и второй операнды, конвертируются из римского числа в целое число с использованием метода romanToInt() из экземпляра класса Converter.*/
            b = converter.romanToInt(data[1]);
            if (a < 1 || a > 10 || b < 1 || b > 10) { //находятся ли значения a и b в диапазоне от 1 до 10 включительно для римских чисел
                throw new IllegalArgumentException("Ошибка: Числа должны быть в диапазоне от I до X вкл"); //если нет, то выбрасывается исключение
            }
        } else { //выполняется, если хотя бы один из операндов не является римским числом
            try {
                a = Integer.parseInt(data[0].trim()); /*data[0] и data[1] (первый и второй операнды) преобразуются в целые числа с благодаря методу parseInt.
                                                    Метод trim удаляет лишние пробелы перед числом, если они есть.*/
                b = Integer.parseInt(data[1].trim());
                if (a < 1 || a > 10 || b < 1 || b > 10) { //находятся ли значения a и b в диапазоне от 1 до 10 включительно для арабских чисел
                    throw new IllegalArgumentException("Ошибка: Числа должны быть в диапазоне от 1 до 10 вкл"); //если нет, то выбрасывается исключение
                }
            } catch (NumberFormatException e) { //такое исключение означает, что строка не содержит целое число.
                if (converter.isRoman(data[0]) || converter.isRoman(data[1])) { //является ли хотя бы один из операндов римским числом
                    throw new IllegalArgumentException("Ошибка: Используются одновременно разные системы счисления"); //если это так, значит, используются разные системы счисления (римская и арабская) в одном выражении.
                } else {
                    throw new IllegalArgumentException("Ошибка: Числа должны быть целыми. В диапазоне от 1 до 10 вкл"); //в противном случае (если оба операнда не являются римскими числами), выбрасывается исключение
                }
            }
        }
        int result; //объявление переменной для хранения результата мат операции
        switch (actions[actionIndex]) { /*анализирует значение actions[actionIndex] (оператор, найденный в строке input)
                                    и выполняет соответствующий блок кода для конкретного оператора*/
            case "+": //проверяет, соответствует ли значение actions[actionIndex] мат оператору
                result = a + b; //если да, мат выражение выполняется
                break; //выход из блока кода
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            default: //выполняется, если значение actions[actionIndex] не соответствует ни одному из операторов выше
                if (b == 0) {
                    throw new IllegalArgumentException("Ошибка: Делить на 0 нельзя");
                }
                result = a / b;
                break;
        }
        if (converter.isRoman(data[0]) && converter.isRoman(data[1])) { //являются ли оба операнда римскими числами?
            if (result < 1) { //если так и при этом result (результат вычислений) меньше 1, выбрасывается исключение ниже
                throw new IllegalArgumentException("Ошибка: Результат операции с римскими числами не может быть меньше 1");
            }
            return converter.intToRoman(result); //вызывается метод intToRoman(result) из экземпляра класса Converter для преобразования числа result в римскую строку
        } else { //выполняется, если оба операнда не являются римскими числами
            return String.valueOf(result); //result преобразуется в строку с помощью метода String.valueOf() и возвращается как результат метода calc().
        }
    }

    static class Calculator { //класс обрабатывает входные мат выражения, выполняет вычисления и возвращает результат в различных форматах чисел (римские или арабские)
        static class Converter { //класс предоставляет методы для преобразования между арабскими и римскими числами
            TreeMap<Character, Integer> romanKeyMap = new TreeMap<>(); /*romanKeyMap - переменная типа TreeMap<Character, Integer>, которая представляет карту для хранения соответствий между римскими (тип Character) и арабскими (тип Integer).
                                                                    Ключи (символы) хранятся в естественном порядке, так как TreeMap автоматически сортирует элементы по ключу*/
            TreeMap<Integer, String> arabianKeyMap = new TreeMap<>(); /*arabianKeyMap - переменная типа TreeMap<Integer, String>, которая представляет карту для хранения соответствий между арабскими (тип Integer) и римскими (тип String).
                                                                    Ключи (числа) также хранятся в естественном порядке благодаря сортировке TreeMap.
                                                                    <> - для хранения разных типов данных*/

            public Converter() { //этот конструктор инициализирует карты romanKeyMap и arabianKeyMap значениями для преобразования между римскими и арабскими числами.
                romanKeyMap.put('I', 1); //метод put используется для добавления элемента в коллекцию. Метод принимает два аргумента: ключ и значение, и помещает их в коллекцию.
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

            public boolean isRoman(String number) { //метод проверяет, является ли переданная строка числом в римской нотации, основываясь на наличии символа в romanKeyMap.
                return romanKeyMap.containsKey(number.charAt(0)); /*возвращает булево значение (true или false) в зависимости от того, содержится ли символ с определенным индексом в коллекции romanKeyMap.
                                                                number - число в римской нотации. charAt(0) - метод возвращает символ по индексу 0 (первый символ в строке).
                                                                romanKeyMap.containsKey - метод коллекции romanKeyMap, проверяющий, содержится ли указанный ключ (в данном случае, символ) в коллекции.
                                                                Т.о., весь код возвращает true, если первый символ римского числа является ключом в romanKeyMap, и false, если он не является ключом в этой коллекции.
                                                                Это позволяет определить, является ли данное число валидным римским числом*/
            }

            public String intToRoman(int number) { /*метод для преобразования арабского числа в римское. Он использует карту arabianKeyMap и проходит через итерацию в цикле do-while, 
                                            находя наибольший ключ (арабское число), которое можно вычесть из текущего числа, чтобы получить римскую цифру*/
                String roman = ""; //эта строка создает пустую строку, в которую будет поочередно добавляться римская цифра
                int arabianKey; //это переменная, которая будет хранить арабский ключ (значение), используемый для преобразования числа
                do
                { //цикл do-while будет выполняться, пока значение переменной number не станет равным нулю. Внутри цикла будет выполняться преобразование арабского числа в римское.
                    arabianKey = arabianKeyMap.floorKey(number); /*находит наибольший ключ (арабское число) в arabianKeyMap, который меньше или равен текущему значению number. 
                                                                Это позволяет найти наибольшую арабскую цифру, которую можно вычесть из числа*/
                    roman += arabianKeyMap.get(arabianKey); //добавление соответствующей римской цифры в строку roman, используя значение, полученное из arabianKeyMap с помощью найденного арабского ключа arabianKey.
                    number -= arabianKey; //из текущего значения number вычитается найденное арабское значение arabianKey, чтобы продолжить процесс вычитания наибольших арабских чисел из оставшегося значения
                } while (number != 0); //do-while завершится, когда number станет равным 0
                return roman; //возвращает римское представление числа
            }

            public int romanToInt(String s) { /*метод для преобразования римской строки в арабское целое число. 
                                        Он разбивает строку на символы, затем с помощью итерации проходит через символы,
                                        вычисляя соответствующее арабское значение для каждого символа и выполняя операции сложения и вычитания для получения окончательного результата.
                                        s - входная римская строка*/
                int end = s.length() - 1; //хранит индекс последнего символа в строке
                char[] arr = s.toCharArray(); /*преобразует римскую строку s в массив символов arr, где каждый символ из строки будет представлен в отдельной ячейке массива.
                                            Это удобно для обработки и манипулирования символами строки в дальнейшем коде*/
                int arabian; //для хранения текущего значения арабской цифры
                int result = romanKeyMap.get(arr[end]); //для хранения окончательного результата
                for (int i = end - 1; i >= 0; i--) { /*цикл for для перебора остальных символов римской строки, начиная с предпоследнего символа.
                                                    Цикл инициализирует переменную i значением end - 1 (последний индекс в массиве) и выполняет итерации, пока i больше или равно 0.
                                                    В каждой итерации i уменьшается на 1*/
                    arabian = romanKeyMap.get(arr[i]); /*получаем текущий символ из римской строки (arr[i]) и смотрим его соответствие в карте romanKeyMap,
                                                    чтобы узнать, какое арабское значение ему соответствует.
                                                    Мы используем карту, чтобы узнать, сколько это число в арабских числах*/
                    if (arabian < romanKeyMap.get(arr[i + 1])) { /*проверяет, если арабское значение текущего символа меньше арабского значения следующего символа.
                                                                Это необходимо для определения, нужно ли вычитать значение текущей цифры из результата*/
                        result -= arabian; /*если текущая цифра меньше следующей, ее арабское значение вычитается из результата.
                                        Ведь правила римской нотации: меньшая цифра перед большей вычитается*/
                    } else { //в противном случае, если текущая цифра больше или равна следующей, то
                        result += arabian; //ее арабское значение добавляется к результату
                    }
                }
                return result; //возврат результата мат выражения, представляющего арабское значение, эквивалентное римской числовой строке
            }
        }
    }
}