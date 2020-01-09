package model;

import java.util.Scanner;

public class Lobby {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose 'HOST' or 'JOIN': ");
        String input = scanner.next();
        if (input.equals("HOST")) {

        } else if (input.equals("JOIN")) {

        }
    }
}
