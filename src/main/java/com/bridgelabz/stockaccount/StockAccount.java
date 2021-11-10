package com.bridgelabz.stockaccount;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class StockAccount {
    JSONParser jsonparser = new JSONParser();
    FileReader reader = new FileReader("E:\\Day11commercialdataprocessing\\src\\main\\java\\com\\bridgelabz\\jsonfiles\\account.json");
    Object obj = jsonparser.parse(reader);
    JSONObject accounts = (JSONObject) obj;
    static Scanner sc = new Scanner(System.in);

    public StockAccount() throws IOException, ParseException {
    }

    /**
     * method to initiate buy
     *
     * @param symbol is used to get the symbol of stock to buy
     */
    public void buy(String symbol, String name_Of_Stock) throws IOException {
        System.out.println("Enter the Quantity to buy");
        int number_Of_Stocks = sc.nextInt();
        System.out.println("Enter the price");
        int price = sc.nextInt();
        int total_Buy_Value = number_Of_Stocks * price;
        Long account_Balance = (Long) accounts.get("balance");
        int account_Balance_After_Buy = (int) (account_Balance - total_Buy_Value);
        if (total_Buy_Value <= account_Balance && total_Buy_Value >= 0) {
            JSONArray stock = (JSONArray) accounts.get("stock");
            accounts.put("balance", account_Balance_After_Buy);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("symbol", symbol);
            jsonObject.put("name", name_Of_Stock);
            jsonObject.put("quantity", number_Of_Stocks);
            jsonObject.put("price", price);
            jsonObject.put("value", total_Buy_Value);
            stock.add(jsonObject);
            FileWriter file = new FileWriter("E:\\Day11commercialdataprocessing\\src\\main\\java\\com\\bridgelabz\\jsonfiles\\account.json");
            file.write(accounts.toJSONString());
            file.close();
        }
    }

    /**
     * searchStock is used to search the stock inside portfolio
     *
     * @param symbol to check if the specified stock with symbol is present
     */
    private Long searchStock(String symbol) {
        JSONArray stockDetails = (JSONArray) accounts.get("stock");
        for (int i = 0; i < stockDetails.size(); i++) {
            JSONObject stockSymbolDetails = (JSONObject) stockDetails.get(i);
            String symbolOfStocks = (String) stockSymbolDetails.get("symbol");
            if (symbolOfStocks.equals(symbol) == true) {
                Long numberOfStocks = (Long) stockSymbolDetails.get("quantity");
                return numberOfStocks;
            }
        }
        return 0L;
    }

    /**
     * method to initiate sell process
     *
     * @param stockSymbol is used to get the stock symbol to sell .
     */
    private void sell(String stockSymbol) throws IOException {
        Long quantity = searchStock(stockSymbol);
        System.out.println("Enter the quantity to sell");
        int sell_Quantity = sc.nextInt();
        if (sell_Quantity >= 0 && sell_Quantity <= quantity) {
            System.out.println("Enter the sell price");
            int sell_Price = sc.nextInt();
            int amount_Received_After_Selling = sell_Price * sell_Quantity;
            Long balance = (Long) accounts.get("balance");
            balance = balance + amount_Received_After_Selling;
            int number_Of_Shares_Left = (int) (quantity - sell_Quantity);
            accounts.put("balance", balance);
            JSONArray stockDetails = (JSONArray) accounts.get("stock");
            for (int i = 0; i < stockDetails.size(); i++) {
                JSONObject stockSymbolDetails = (JSONObject) stockDetails.get(i);
                String symbolOfStocks = (String) stockSymbolDetails.get("symbol");
                if (symbolOfStocks.equals(stockSymbol) == true) {
                    Long priceOfStocks = (Long) stockSymbolDetails.get("price");
                    Long numberOfStocks = (Long) stockSymbolDetails.get("quantity");
                    stockSymbolDetails.put("quantity", number_Of_Shares_Left);
                    stockSymbolDetails.put("value", numberOfStocks * priceOfStocks);
                }
            }

        } else if (quantity == 0 || sell_Quantity == 0) {
            System.out.println("No holdings present to sell");
        }
        FileWriter file = new FileWriter("E:\\Day11commercialdataprocessing\\src\\main\\java\\com\\bridgelabz\\jsonfiles\\account.json");
        file.write(accounts.toJSONString());
        file.close();
    }

    /*
     * method to print stock details
     * */
    private void stockReport() {
        JSONArray stockDetails = (JSONArray) accounts.get("stock");                       //Array of stocks
        for (int i = 0; i < stockDetails.size(); i++) {
            JSONObject stockSymbolDetails = (JSONObject) stockDetails.get(i);
            System.out.println("Details of " + stockSymbolDetails.get("name") + " is");
            System.out.println("Stock Symbol is : " + stockSymbolDetails.get("symbol"));
            System.out.println("Stock quantity is : " + stockSymbolDetails.get("quantity"));
            System.out.println("Per Stock price is : " + stockSymbolDetails.get("price"));
            System.out.println("Total value of stock is : " + stockSymbolDetails.get("value"));
            System.out.println("======================================");
        }
    }

    /**
     * debit method is used to withdraw
     *
     * @param withdraw_amount is used to get the withdraw amount
     */
    private void debit(int withdraw_amount) throws IOException {
        Long account_Balance = (Long) accounts.get("balance");
        if (withdraw_amount > 0 && withdraw_amount <= account_Balance) {
            int remaining_Balance = (int) (account_Balance - withdraw_amount);
            accounts.put("balance", remaining_Balance);
            System.out.println("Withdraw successful");
        } else System.out.println("Debit amount exceeds account balance");
        FileWriter file = new FileWriter("E:\\Day11commercialdataprocessing\\src\\main\\java\\com\\bridgelabz\\jsonfiles\\account.json");
        file.write(accounts.toJSONString());
        file.close();
    }

    public static void main(String[] args) throws IOException, ParseException {
        StockAccount account = new StockAccount();
        System.out.println("Enter 1 to buy, 2 to sell, 3 to debit, 4 for stock report");
        switch (sc.nextInt()) {
            case 1:
                System.out.println("Enter the stock symbol to buy");
                String stock_To_Buy = sc.next();
                System.out.println("Enter the name of stock");
                String name_Of_Stock = sc.next();
                account.buy(stock_To_Buy, name_Of_Stock);
                break;
            case 2:
                System.out.println("Enter the stock symbol to sell");
                String stock_To_Sell = sc.next();
                account.sell(stock_To_Sell);
                break;
            case 3:
                System.out.println("Enter the amount to withdraw");
                int withdraw_Amount = sc.nextInt();
                account.debit(withdraw_Amount);
                break;
            case 4:
                account.stockReport();
                break;
            default:
                System.out.println("Invalid input");
        }
    }


}
