package me.uranusdestroyer.etexcoreplugin.features.currenciesbank;

import me.uranusdestroyer.etexcoreplugin.backend.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Bank {

    public static int getValue(UUID uuid, String currencyName) {
        Connection conn = DbManager.getConnection();
        String query = "SELECT value FROM etex_currency_" + currencyName + " WHERE uuid = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, String.valueOf(uuid));
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int value = rs.getInt("value");
                rs.close();
                conn.close();
                return value;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } return 0;
    }

    public static void addValue(UUID uuid, String currencyName, int value) {
        Connection conn = DbManager.getConnection();
        String query = "UPDATE etex_currency_" + currencyName + " SET value = value + ? WHERE uuid = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, String.valueOf(uuid));
            preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deductValue(UUID uuid, String currencyName, int value) {
        Connection conn = DbManager.getConnection();
        String query = "UPDATE etex_currency_" + currencyName + " SET value = value - ? WHERE uuid = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, String.valueOf(uuid));
            preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertPlayer(UUID uuid) {
        for(Object currency : Currencies.getCurrencies()){
            Connection conn = DbManager.getConnection();

            System.out.println(currency);
            String query = "INSERT IGNORE INTO etex_currency_" + currency + "(uuid, value) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, String.valueOf(uuid));
                preparedStatement.setInt(2, 0);
                preparedStatement.executeUpdate();
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean hasValue(UUID uuid, String currencyName, int value) {
        Connection conn = DbManager.getConnection();
        String query = "SELECT value FROM etex_currency_" + currencyName + " WHERE uuid = ? AND value >= ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, String.valueOf(uuid));
            preparedStatement.setInt(2, value);
            ResultSet rs = preparedStatement.executeQuery();
            boolean result = rs.next();
            rs.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setValue(UUID uuid, String currencyName, int value) {
        Connection conn = DbManager.getConnection();
        String query = "UPDATE etex_currency_" + currencyName + " SET value = ? WHERE uuid = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, String.valueOf(uuid));
            preparedStatement.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}