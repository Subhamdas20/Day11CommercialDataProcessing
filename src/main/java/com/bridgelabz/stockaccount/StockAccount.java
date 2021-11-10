package com.bridgelabz.stockaccount;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
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

    public void buy() {

    }

    /**
     * searchStock is used to search the stock inside portfolio
     *
     * @param symbol to check if the specified stock with symbol is present
     */
    public Long searchStock(String symbol) {
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

    /*
     * method to initiate sell process
     * */
    public void sell(String stockSymbol) throws IOException {
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
    public void stockReport() {
        JSONArray stockDetails = (JSONArray) accounts.get("stock");                       //Array of stocks
        for (int i = 0; i < stockDetails.size(); i++) {
            JSONObject stockSymbolDetails = (JSONObject) stockDetails.get(i);
            System.out.println("Details of " + stockSymbolDetails.get("name")+" is");
            System.out.println("Stock Symbol is : " + stockSymbolDetails.get("symbol"));
            System.out.println("Stock quantity is : " + stockSymbolDetails.get("quantity"));
            System.out.println("Per Stock price is : " + stockSymbolDetails.get("price"));
            System.out.println("Total value of stock is : " + stockSymbolDetails.get("value"));
            System.out.println("======================================");
        }
    }

    public static void main(String[] args) throws IOException, ParseException {
        StockAccount account = new StockAccount();
        System.out.println("Enter 1 to sell 2 for stock report");
        switch (sc.nextInt()){
            case 1 :
                System.out.println("Enter the stock symbol to sell");
                String stock_To_Sell = sc.next();
                account.sell(stock_To_Sell);
            break;
            case 2 :
                account.stockReport();
            break;
            default:
                System.out.println("Invalid input");
    }}
}
