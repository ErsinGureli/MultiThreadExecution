package util;

import enumeration.OperationType;
import pojo.BankAccount;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadExecuterUtil {
    public static Map<Integer, BankAccount> getBankAccountMap() {
        BigDecimal balance = BigDecimal.valueOf(100);
        Map<Integer, BankAccount> bankAccountMap = new ConcurrentHashMap<Integer, BankAccount>();
        bankAccountMap.put(1, BankAccount.builder().accountName("account1").balance(balance).build());
        bankAccountMap.put(2, BankAccount.builder().accountName("account2").balance(balance).build());
        bankAccountMap.put(3, BankAccount.builder().accountName("account3").balance(balance).build());
        bankAccountMap.put(4, BankAccount.builder().accountName("account4").balance(balance).build());
        bankAccountMap.put(5, BankAccount.builder().accountName("account5").balance(balance).build());
        bankAccountMap.put(6, BankAccount.builder().accountName("account6").balance(balance).build());
        bankAccountMap.put(7, BankAccount.builder().accountName("account7").balance(balance).build());
        bankAccountMap.put(8, BankAccount.builder().accountName("account8").balance(balance).build());
        bankAccountMap.put(9, BankAccount.builder().accountName("account9").balance(balance).build());
        bankAccountMap.put(10, BankAccount.builder().accountName("account10").balance(balance).build());

        return bankAccountMap;
    }

    public static BigDecimal generateRandomBalance() {
        int number = getLimitedRandomIntValue(10) * 10;
        return BigDecimal.valueOf(number);
    }

    public static int getLimitedRandomIntValue(int limit) {
        Random r = new Random();
        return r.nextInt(limit);
    }

    public static int generateRandomIntValueExceptGivenValue(int limit, int givenValue) {
        int randomValue = getLimitedRandomIntValue(limit);
        while (randomValue == givenValue) {
            randomValue = getLimitedRandomIntValue(limit);
        }
        return randomValue;
    }

    public static List<OperationType> getOperationTypeList() {
        return Arrays.asList(OperationType.TRANSFER, OperationType.WITHDRAWAL);
    }
}
