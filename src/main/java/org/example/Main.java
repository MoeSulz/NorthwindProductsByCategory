package org.example;


import com.mysql.cj.protocol.Resultset;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws ClassNotFoundException {
        if (args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "java com.hca.jdbc.UsingDriverManager <username> " +
                            "<password>");
            System.exit(1);
        }
        String username = args[0];
        String password = args[1];

        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/northwind",
                username, password);

             PreparedStatement preparedStatement =
                     connection.prepareStatement(
                             "SELECT Categoryid, Categoryname FROM categories ORDER BY categoryid")) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    System.out.printf("Category ID = %s, Category Name = %s;\n",
                            resultSet.getInt(1), resultSet.getString(2));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Select a Category ID:");
        int choiceOfCategory = scanner.nextInt();

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/northwind",
                username, password);

             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT productid, productname, unitprice, unitsinstock FROM products WHERE categoryid = ?")) {
            preparedStatement.setInt(1, choiceOfCategory);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    System.out.printf("Product ID = %s" + "\n" + "Product Name = %s" + "\n" + "UnitPrice = %s" + "\n" + "UnitsInStock = %s" + "\n" + "------------------" + "\n",
                            resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3), resultSet.getInt(4));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
