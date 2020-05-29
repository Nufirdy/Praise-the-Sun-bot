package ClassicKiryusha.Commands;

import java.util.Random;
public class Magic8 {
    public static void main(String[] args) {
        Random rndGen = new Random();
        int fortune = rndGen.nextInt( 20 );

        switch (fortune) {
            case 0:
                System.out.println("Даже не думай об этом");
                break;
            case 1:
                System.out.println("Без сомнений");
                break;
            case 2:
                System.out.println("Спросите снова");
                break;
            case 3:
                System.out.println("Духи говорят да");
                break;
            case 4:
                System.out.println("Да");
                break;
            case 5:
                System.out.println("Безусловно");
                break;
            case 6:
                System.out.println("Спросите позже");
                break;
            case 7:
                System.out.println("Очень вероятно");
                break;
            case 8:
                System.out.println("Звезды говорят нет");
                break;
            case 9:
                System.out.println("Не могу сказать");
                break;
            case 10:
                System.out.println("Абсолютно точно");
                break;
            case 11:
                System.out.println("Нет");
                break;
            case 12:
                System.out.println("Ответ нет");
                break;
            case 13:
                System.out.println("Не надейтесь");
                break;
            case 14:
                System.out.println("Должно быть так");
                break;
            case 15:
                System.out.println("Мало шансов");
                break;
            case 16:
                System.out.println("Мне кажется да");
                break;
            case 17:
                System.out.println("Ответ не ясен");
                break;
            case 18:
                System.out.println("Не похоже");
                break;
            case 19:
                System.out.println("Похоже что да");
                break;
            default:
                System.out.println("Ты шо ебан?");
        }
    }
}