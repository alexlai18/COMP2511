package splitter;
import java.util.Scanner;

public class Splitter {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a message: ");
        String input_str = input.nextLine();
        String[] arr = input_str.split(" ");
        for (String s : arr) {
            System.out.println(s);
        }
        input.close();
    }
}
