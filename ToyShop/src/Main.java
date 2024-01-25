
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n                ===   Menu   ===\n");
        Lottery lottery = new Lottery();
        initToys(lottery);


//        start(lottery);


        boolean continueLoop = true;
        while (lottery.getToysListSize() != 0 && continueLoop){
            System.out.println("""
                    1 - �������� �����
                    2 - ���������� ����������� ��������� �������
                    3 - �������� �������
                    4 - �����
                    """);
            try {
                Scanner sc = new Scanner(System.in);
                switch (sc.nextLine()) {
                    case "1" -> start(lottery);
                    case "2" -> chancePrinter(lottery);
                    case "3" -> addToy(lottery);
                    case "4" -> {
                        System.out.println("�� �������� � �����!");
                        continueLoop = false;
                    }
                }
            }catch (InputMismatchException e) {
                System.out.println("������������ �������.");
            } catch (NoSuchElementException ex) {
                System.out.println("�� ������ �� �����!");
            }
        }
    }

    private static void chancePrinter(Lottery lottery) {
        System.out.println("����������� ��������� �������: ");
        lottery.updateOverallRate().forEach(System.out::println);
    }

    private static void initToys(Lottery lottery) {
        lottery.addToy(new Toy(1, 2, "Superman"));
        lottery.addToy(new Toy(2, 2, "Batman"));
        lottery.addToy(new Toy(3, 6, "Ninja"));
        System.out.println("��������� ����� �������: Superman, Batman, Ninja.");
    }

    private static void addToy(Lottery lottery) {
        try  {
            Scanner scanner = new Scanner(System.in);
            System.out.println("������� id: ");
            int id = scanner.nextInt();
            System.out.println("������� ���������� �������: ");
            double rate = scanner.nextDouble();
            System.out.println("������� �������� �������: ");
            String name = scanner.nextLine();
            lottery.addToy(new Toy(id, rate, name));
            System.out.println("������� ���������!");

        } catch (InputMismatchException e) {
            System.out.println("������������ �������.");
        } catch (NoSuchElementException ex) {
            System.out.println("�� ������ �� �����!");
        }
    }

    private static void start(Lottery lottery) {
        if (lottery.getToysListSize() == 0){
            System.out.println("���, ������� �����������.");
        }else System.out.println("�� ��������: " + lottery.getToy());
    }

}
