package com.scc.ui;

import com.scc.dao.ScenarioDao;
import com.scc.model.ScenarioEntity;

import java.util.List;
import java.util.Scanner;

public class ScenarioMenu {

    private static final String DOUBLE_LINE = "=".repeat(50);
    private static final String TABLE_DOUBLE_LINE = "=".repeat(67);
    private static final String TABLE_BORDER = "+----+----------------+-------------+----------+---------+";

    private final Scanner scanner = new Scanner(System.in);
    private final ScenarioDao dao = new ScenarioDao();

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter choice: ");
            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Please enter 1, 2, or 3.\n");
                continue;
            }

            System.out.println();

            switch (choice) {
                case 1 -> addTicket();
                case 2 -> viewTickets();
                case 3 -> {
                    System.out.println("Exiting system... Thank you!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.\n");
            }
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println(DOUBLE_LINE);
        System.out.println("           EVENT TICKET SALES SYSTEM");
        System.out.println(DOUBLE_LINE);
        System.out.println("[1] Add New Ticket");
        System.out.println("[2] View Sold Tickets");
        System.out.println("[3] Exit System");
        System.out.println(DOUBLE_LINE);
    }

    private void addTicket() {
        System.out.println("--- ISSUE EVENT TICKET ---");

        String name = readNonEmpty("Enter Attendee Name: ");
        String tier = readChoice("Enter Ticket Tier (REGULAR / VIP / VVIP): ",
                "REGULAR", "VIP", "VVIP");
        double price = readPrice("Enter Price: ");
        String payment = readChoice("Enter Payment Method (CASH / GCASH / CARD): ",
                "CASH", "GCASH", "CARD");

        ScenarioEntity ticket = new ScenarioEntity(0, name, tier, price, payment);

        if (dao.insert(ticket)) {
            System.out.println("\nSUCCESS: Ticket issued to attendee!\n");
        } else {
            System.out.println("\nFAILED: Ticket could not be issued.\n");
        }
    }

    private void viewTickets() {
        List<ScenarioEntity> tickets = dao.findAll();

        System.out.println(TABLE_DOUBLE_LINE);
        System.out.println("                      EVENT TICKET PURCHASES");
        System.out.println(TABLE_DOUBLE_LINE);
        System.out.println(TABLE_BORDER);
        System.out.printf("| %-2s | %-14s | %-11s | %-8s | %-7s |%n",
                "ID", "ATTENDEE NAME", "TICKET TIER", "PRICE", "PAYMENT");
        System.out.println(TABLE_BORDER);

        for (ScenarioEntity ticket : tickets) {
            System.out.printf("| %-2d | %-14s | %-11s | %-8.2f | %-7s |%n",
                    ticket.id(),
                    ticket.attendeeName(),
                    ticket.ticketTier(),
                    ticket.price(),
                    ticket.payment());
        }

        System.out.println(TABLE_BORDER);
        System.out.println("Total Records Displayed: " + tickets.size());
        System.out.println();
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty. Try again.");
        }
    }

    private String readChoice(String prompt, String... validOptions) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim().toUpperCase();
            for (String option : validOptions) {
                if (option.equals(value)) {
                    return value;
                }
            }
            System.out.println("Invalid option. Try again.");
        }
    }

    private double readPrice(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                double price = Double.parseDouble(value);
                if (price >= 0) {
                    return price;
                }
                System.out.println("Price cannot be negative. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Enter a number (e.g., 7500.00).");
            }
        }
    }
}