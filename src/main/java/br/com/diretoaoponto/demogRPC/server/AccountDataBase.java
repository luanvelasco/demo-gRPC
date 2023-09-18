package br.com.diretoaoponto.demogRPC.server;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountDataBase {

    /**
     * This is a mock database
     * There are 10 accounts in the database
     * @return for each account number, the balance is 10 times the account number
     *
     * Account Number 1, Balance = 10
     * Account Number 2, Balance = 20
     * Account Number 3, Balance = 30
     * Account Number 4, Balance = 40
     * Account Number 5, Balance = 50
     * Account Number 6, Balance = 60
     * Account Number 7, Balance = 70
     * Account Number 8, Balance = 80
     * Account Number 9, Balance = 90
     * Account Number 10, Balance = 100
     */
    private static final Map<Integer, Integer> MAP = IntStream.rangeClosed(1, 10)
            .boxed()
            .collect(Collectors.toMap(
                    Function.identity(),
                    v -> v * 10
            ));

    public static int getBalance(int accountNId) {
        return MAP.get(accountNId);
    }

    /**
     * This method is used to add balance to an account
     */
    public static int addBalance(int accountNId, int amount) {
        return MAP.computeIfPresent(accountNId, (k, v) -> v + amount);
    }

    public static int deductBalance(int accountNId, int amount) {
        return MAP.computeIfPresent(accountNId, (k, v) -> v - amount);
    }



}
