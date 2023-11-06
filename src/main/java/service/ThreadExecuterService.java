package service;

import enumeration.OperationType;
import pojo.BankAccount;
import util.ThreadExecuterUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadExecuterService {

    private static Map<Integer, BankAccount> bankAccountMap = ThreadExecuterUtil.getBankAccountMap();

    public void executeTask() throws InterruptedException {
        List<OperationType> operationList = ThreadExecuterUtil.getOperationTypeList();
        List<Callable<String>> callableList = new ArrayList<Callable<String>>();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(1);

        for (Map.Entry<Integer, BankAccount> bankAccountEntry : bankAccountMap.entrySet()) {
            Callable callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    Integer removableEntryKey = null;
                    while (bankAccountEntry.getValue().getBalance().compareTo(BigDecimal.ZERO) > 0) {
                        OperationType operationType = operationList.get(ThreadExecuterUtil.getLimitedRandomIntValue(2));

                        if (OperationType.TRANSFER.equals(operationType)) {
                            BankAccount randomSelectedPairAcount = bankAccountMap
                                    .get(ThreadExecuterUtil.generateRandomIntValueExceptGivenValue(bankAccountMap.size(),
                                            bankAccountEntry.getKey()));
                            transferBalance(bankAccountEntry.getValue(), randomSelectedPairAcount, ThreadExecuterUtil.generateRandomBalance());
                        } else {
                            withdrawalBalance(bankAccountEntry.getValue(), ThreadExecuterUtil.generateRandomBalance());
                        }
                        removableEntryKey = bankAccountEntry.getKey();
                        try {
                            Thread.sleep(ThreadExecuterUtil.getLimitedRandomIntValue(10000));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    removeBankAccountFromMap(bankAccountMap, removableEntryKey);
                    return "Thread : "+ Thread.currentThread().getName() + " finalized the job";
                }
            };
            callableList.add(callable);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (bankAccountMap.size() > 0) {
                    for (Map.Entry<Integer, BankAccount> entry : bankAccountMap.entrySet()) {
                        System.out.println("Account : " + entry.getValue().getAccountName() + "  Balance : "
                                + entry.getValue().getBalance());
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        singleThreadExecutor.execute(runnable);

        List<Future<String>> futureList = executorService.invokeAll(callableList);
        futureList.stream().forEach(future -> {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        executorService.shutdown();
        singleThreadExecutor.shutdown();
    }


    private void removeBankAccountFromMap(Map<Integer, BankAccount> bankAccountMap, Integer key) {
        System.out.println("removeBankAccount -> " + "bankAccount : " + bankAccountMap.get(key).getAccountName());
        if (Objects.nonNull(bankAccountMap.get(key))) {
            bankAccountMap.remove(key);
        }
    }

    private synchronized void transferBalance(BankAccount bankAccountFrom, BankAccount bankAccountTo,BigDecimal balance) {
        if (Objects.nonNull(bankAccountFrom) && Objects.nonNull(bankAccountTo) && bankAccountTo.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("transfer : " + balance + " -> bankAccountFrom: " + bankAccountFrom.getAccountName()
                    + " balance: " + bankAccountFrom.getBalance() + " bankAccountTo: " + bankAccountTo.getAccountName()
                    + " balance: " + bankAccountTo.getBalance() + " Thread : " + Thread.currentThread().getName());
            if (bankAccountFrom.getBalance().compareTo(balance) >= 0) {
                bankAccountFrom.setBalance(bankAccountFrom.getBalance().subtract(balance));
                bankAccountTo.setBalance(bankAccountTo.getBalance().add(balance));
            }
        }
    }

    private synchronized void withdrawalBalance(BankAccount bankAccount, BigDecimal balance) {
        if (Objects.nonNull(bankAccount)) {
            System.out.println("withdraw : " + balance + " -> bankAccount: " + bankAccount.getAccountName() + " balance: "
                    + bankAccount.getBalance() + " Thread : " + Thread.currentThread().getName());

            if (bankAccount.getBalance().compareTo(balance) >= 0) {
                bankAccount.setBalance(bankAccount.getBalance().subtract(balance));
            } else {  //if there is not enough balance withdrawal all
                bankAccount.setBalance(BigDecimal.ZERO);
            }
        }
    }
}
