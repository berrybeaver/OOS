package bank;


import bank.exceptions.*;
import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PrivateBank implements Bank{
    private String name;
    /**
     * directory for all Json files for PrivateBank
     */
    private String directoryName;

    /**
     * full path of directory where stores all files
     */
    private String fullPath;
    private double incomingInterest;
    private double outgoingInterest;
    /**
     * Links accounts to transactions so that 0 to N transactions can be assigned to
     * each stored account
     */
    private Map<String, List<Transaction>> accountsToTransactions =  new HashMap<>();

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }


    public void setIncomingInterest(double incomingInterest) {
        this.incomingInterest = incomingInterest;
    }
    public double getIncomingInterest() {
        return incomingInterest;
    }


    public void setOutgoingInterest(double outgoingInterest) {
        this.outgoingInterest = outgoingInterest;
    }
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setFullPath(String directoryName, boolean copiedBankPath) {
        if (copiedBankPath)
            fullPath = "data/_CopiedPrivateBanks/" + directoryName;
        else
            fullPath = "data/" + directoryName;
    }

    public String getFullPath() {
        return fullPath;
    }

    /**
     * Constructor with four attributes
     */
    public PrivateBank(String newName, String newDirectoryName, double newIncomingInterest, double newOutgoingInterest) {
        this.name = newName;
        this.directoryName = newDirectoryName;
        this.incomingInterest = newIncomingInterest;
        this.outgoingInterest = newOutgoingInterest;

        setFullPath(newDirectoryName, false);

        try {
            Path path = Paths.get(fullPath);

            if (Files.notExists(path)) {
                Files.createDirectories(path);
                System.out.println("\nDirectory for " + PrivateBank.this.getName() + " is created!");
            }
            else {
                System.out.println("\nDirectory for " + PrivateBank.this.getName() + " is already exist!");
                System.out.println("=> Start reading account(s) from directory to " + PrivateBank.this.getName() + ":");
                readAccounts();
                System.out.println("FINISHED reading account(s) for " + PrivateBank.this.getName() + "\n");
            }
        } catch (IOException | AccountAlreadyExistsException e) {
            System.out.println("Failed to create directory for " + PrivateBank.this.getName() + "!");
        }


    }

    /**
     * Copy Constructor
     */
    public PrivateBank(PrivateBank newPrivateBank) throws AccountAlreadyExistsException {
        this(newPrivateBank.name, newPrivateBank.directoryName, newPrivateBank.incomingInterest, newPrivateBank.outgoingInterest);
        this.accountsToTransactions = newPrivateBank.accountsToTransactions;

        setFullPath(newPrivateBank.directoryName, true);

        try {
            Path path = Paths.get(fullPath);

            if (Files.notExists(path)) {
                Files.createDirectories(path);
                System.out.println("\nDirectory for copied " + newPrivateBank.getName() + " is created!");
            } else {
                System.out.println("\nDirectory for copied " + newPrivateBank.getName() + " already exist!");
                System.out.println("=> Start adding account(s) from directory to copied " + newPrivateBank.getName() + ":");
                readAccounts();
                System.out.println("FINISHED reading account(s) for copied " + newPrivateBank.getName() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Failed to create directory for copied " + newPrivateBank.getName() + "!");
        }
    }

    /**
     * @return contents of all attributes
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Set<String> setKey = accountsToTransactions.keySet();
        for (String key : setKey) {
            str.append(key).append(" => \n");
            List<Transaction> transactionsList = accountsToTransactions.get(key);
            for (Transaction transaction : transactionsList)
                str.append("\t\t").append(transaction);
        }
        return "Name: " + name + "\nIncoming Interest: " + incomingInterest + "\noutgoing Interest: " + outgoingInterest + "\n" + str;
    }
    public boolean equals(Object obj) {
        if (obj instanceof PrivateBank privateBank)
            return (name.equals(privateBank.name) && incomingInterest == privateBank.incomingInterest && outgoingInterest == privateBank.outgoingInterest && accountsToTransactions.equals(privateBank.accountsToTransactions));
        return false;
    }

    /**
     * Adds an account to the bank. If the account ALREADY EXISTS, an exception is thrown.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account ALREADY EXISTS
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException {
        Path path = Path.of(PrivateBank.this.getFullPath() + "/" + account + ".json");

        if (Files.exists(path)) {
            System.out.print("\nAdding <" + account + "> from the data system to bank <" + name + "> " );
            if (accountsToTransactions.containsKey(account))
                throw new AccountAlreadyExistsException("=> FAILED! ACCOUNT <" + account + "> ALREADY EXISTS!\n");
            else {
                accountsToTransactions.put(account, List.of());
                System.out.println("=> SUCCESS!");
            }
        }
        else {
            System.out.print("\nCreating new account <" + account + "> to bank <" + name + "> ");
            accountsToTransactions.put(account, List.of());
            writeAccount(account);
            System.out.println("=> SUCCESS!");
        }
    }
    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
        System.out.print("Creating new account <" + account + "> to bank <" + name + "> with transactions list: \n\t\t" + transactions.toString().replaceAll("[]]|[\\[]", "").replace("\n, ", "\n\t\t"));
        if ( (accountsToTransactions.containsKey(account)) || (accountsToTransactions.containsKey(account) && accountsToTransactions.containsValue(transactions)) ){
            throw new AccountAlreadyExistsException("ACCOUNT <" + account + "> ALREADY EXISTS!\n");}
        else {
            for (Transaction valueOfTransactions : transactions) {
                if (valueOfTransactions instanceof Payment payment) {
                    payment.setIncomingInterest(PrivateBank.this.incomingInterest);
                    payment.setOutgoingInterest(PrivateBank.this.outgoingInterest);
                    /**
                    if(payment.getIncomingInterest() <= 0 || payment.getIncomingInterest() >= 1){
                        throw new TransactionAttributeException("INCOMING INTEREST MUST BE IN BETWEEN 0 AND 1!\n");
                    } else if (payment.getOutcomingInterest() <= 0 || payment.getOutcomingInterest() >= 1) {
                        throw new TransactionAttributeException("OUTGOING INTEREST MUST BE IN BETWEEN 0 AND 1!\n");
                    }
                     */
                }
            }

            accountsToTransactions.put(account, transactions);
            writeAccount(account);
            System.out.println("=> SUCCESS!");
        }
    }
    /**
     * Adds a transaction to an already existing account.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        System.out.println("Adding new transaction <" + transaction.toString().replace("\n", "") + "> to account <" + account + "> in bank <" + name + ">");
        if(!accountsToTransactions.containsKey(account)){
            throw new AccountDoesNotExistException("ACCOUNT <" + account + "> DOES NOT EXISTS!\n");
        }
        else {
            if(containsTransaction(account, transaction)){
                throw new TransactionAlreadyExistException("THIS TRANSACTION ALREADY EXIST!\n");
            } else{
                if(transaction instanceof Payment payment){
                    payment.setIncomingInterest(PrivateBank.this.incomingInterest);
                    payment.setOutgoingInterest(PrivateBank.this.outgoingInterest);
                }
                List<Transaction> transactionList = new ArrayList<>(accountsToTransactions.get(account));
                transactionList.add(transaction);
                accountsToTransactions.put(account, transactionList);
                writeAccount(account);
                System.out.println("=> SUCCESS!\n");
            }
        }
    }
    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */
    @Override
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException{
        System.out.println("Removing transaction <" + transaction.toString().replace("\n", "") + "> from account <" + account + "> in bank <" + name + ">");
        if (transaction instanceof Payment payment) {
            payment.setIncomingInterest(PrivateBank.this.incomingInterest);
            payment.setOutgoingInterest(PrivateBank.this.outgoingInterest);
        }
        if(!accountsToTransactions.get(account).contains(transaction)){
            throw new TransactionDoesNotExistException("THIS TRANSACTION DOES NOT EXISTS!\n");
        } else if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("ACCOUNT <" + account + "> DOES NOT EXISTS!\n");
        } else{
            List<Transaction> transactionList = new ArrayList<>(accountsToTransactions.get(account));
            transactionList.remove(transaction);
            accountsToTransactions.put(account, transactionList);
            writeAccount(account);
            System.out.println("=> SUCCESS!\n");
        }
    }
    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction which is added to the account
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction){
        if (transaction instanceof Payment payment){
            payment.setIncomingInterest(PrivateBank.this.incomingInterest);
            payment.setOutgoingInterest(PrivateBank.this.outgoingInterest);
        }
        System.out.println("Checking account <" + account + "> contains the transaction <" + transaction.toString().replace("\n", "") + "> : " + accountsToTransactions.get(account).contains(transaction) + "\n");
        return accountsToTransactions.get(account).contains(transaction);
    }
    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    @Override
    public double getAccountBalance(String account){
        double balance =0;
        for (Transaction transaction : accountsToTransactions.get(account)){
            balance = balance + transaction.calculate();
        }
        System.out.println("Balance of account <" + account + "> in bank <" + name + "> : " + (double) Math.round(balance * 100)/100 + "\n");
        return balance;
    }
    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactions(String account) {
        System.out.println("Transactions list of account <" + account + "> in bank <" + name + ">\n" + accountsToTransactions.get(account).toString().replace("[", "\t\t").replace("]","").replace("\n, ", "\n\t\t"));
        return accountsToTransactions.get(account);
    }
    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account . Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted ascending or descending
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        // create new list to store sorted list without affecting original list
        List<Transaction> sortedTransactionsList = new ArrayList<>(accountsToTransactions.get(account));
        if(asc) {
            sortedTransactionsList.sort(Comparator.comparingDouble(Transaction::calculate));
            System.out.println("Sorting transactions of account <" + account + "> by calculated amounts in ASCENDING order:\n" + sortedTransactionsList.toString().replace("[", "\t\t").replace("]","").replace("\n, ", "\n\t\t"));
        }
        else {
            sortedTransactionsList.sort(Comparator.comparingDouble(Transaction::calculate).reversed());
            System.out.println("Sorting transactions of account <" + account + "> by calculated amounts in DESCENDING order:\n" + sortedTransactionsList.toString().replace("[", "\t\t").replace("]","").replace("\n, ", "\n\t\t"));
        }
        return sortedTransactionsList;
    }
    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive  or negative transactions are listed
     * @return the list of transactions
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> transactionsListByType = new ArrayList<>();
        if (positive)
            System.out.println("List of POSITIVE transactions of account <" + account + "> :");
        else
            System.out.println("List of NEGATIVE transactions of account <" + account + "> :");
        for (Transaction transaction : accountsToTransactions.get(account)) {
            if (positive && transaction.calculate() >= 0)
                transactionsListByType.add(transaction);
            else if (!positive && transaction.calculate() < 0)
                transactionsListByType.add(transaction);
        }
        System.out.println(transactionsListByType.toString().replace("[", "\t\t").replace("]","").replace("\n, ", "\n\t\t"));

        return transactionsListByType;
    }
    /**
     * Persists the specified account in the file system (serialize then save into JSON)
     *
     * @param account the account to be written
     */
    private void writeAccount(String account) {

        try (FileWriter file = new FileWriter(getFullPath() + "/" + account + ".json")) {

            file.write("[");

            for (Transaction transaction : accountsToTransactions.get(account)) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(transaction.getClass(), new CustomDeSerializer())
                        .setPrettyPrinting()
                        .create();
                String json = gson.toJson(transaction);
                if (accountsToTransactions.get(account).indexOf(transaction) != 0)
                    file.write(",");
                file.write(json);
            }

            file.write("]");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * read all existing accounts from data system and make them available in PrivateBank object
     * @throws AccountAlreadyExistsException when account already exist
     * @throws IOException io
     */
    private void readAccounts() throws AccountAlreadyExistsException, IOException {

        final File folder = new File(PrivateBank.this.getFullPath());
        final File[] listOfFiles = Objects.requireNonNull(folder.listFiles());

        for (File file : listOfFiles) {
            String accountName = file.getName().replace(".json", "");
            String accountNameFile = file.getName();
            PrivateBank.this.createAccount(accountName);

            try {

                Reader reader = new FileReader(PrivateBank.this.getFullPath() + "/" + accountNameFile);
                JsonArray parser = JsonParser.parseReader(reader).getAsJsonArray();
                for (JsonElement jsonElement : parser.getAsJsonArray()) {

                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    Gson customGson = new GsonBuilder()
                            .registerTypeAdapter(Transaction.class, new CustomDeSerializer())
                            .create();

                    String str = customGson.toJson(jsonObject.get("INSTANCE"));

                    if (jsonObject.get("CLASSNAME").getAsString().equals("Payment")) {
                        Payment payment = customGson.fromJson(str, Payment.class);
                        PrivateBank.this.addTransaction(accountName, payment);
                    }
                    else if (jsonObject.get("CLASSNAME").getAsString().equals("IncomingTransfer")) {
                        IncomingTransfer incomingTransfer = customGson.fromJson(str, IncomingTransfer.class);
                        PrivateBank.this.addTransaction(accountName, incomingTransfer);
                    }
                    else {
                        OutgoingTransfer outgoingTransfer = customGson.fromJson(str, OutgoingTransfer.class);
                        PrivateBank.this.addTransaction(accountName, outgoingTransfer);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransactionAlreadyExistException | AccountDoesNotExistException e) {
                System.out.println(e);
            }
        }
    }
    /**
     * delete an account in accountsToTransactions and in data system
     * @param account account to be deleted
     * @throws AccountDoesNotExistException when account is not valid
     */
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        System.out.print("\nDelete account <" + account + "> from bank <" + this.getName() + "> ");
        if (!accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException("=> FAILED! ACCOUNT <" + account + "> DOES NOT EXISTS!\n");
        else {
            accountsToTransactions.remove(account);
            Path path = Path.of(this.getFullPath() + "/" + account + ".json");
            Files.deleteIfExists(path);
            System.out.println("=> SUCCESS!");
        }
    }

    /**
     * show all accounts in accountsToTransactions
     * @return a list of all accounts
     */
    public List<String> getAllAccounts() {
        Set<String> setKey = accountsToTransactions.keySet();
        return new ArrayList<>(setKey);
    }

}