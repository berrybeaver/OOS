package bank;

import bank.exceptions.*;

import java.util.*;

    public class PrivateBankAlt implements Bank{
        private String name;
        private double incomingInterest;
        private double outgoingInterest;
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

        /**
         * Constructor mit 3 Attribute
         */
        public PrivateBankAlt(String newName, double newIncomingInterest, double newOutgoingInterest) {
            this.name = newName;
            this.incomingInterest = newIncomingInterest;
            this.outgoingInterest = newOutgoingInterest;

        }

        /**
         * Copy Constructor
         */
        public PrivateBankAlt(PrivateBankAlt newPrivateBank) {
            this(newPrivateBank.name, newPrivateBank.incomingInterest, newPrivateBank.outgoingInterest);
            this.accountsToTransactions = newPrivateBank.accountsToTransactions;
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
            if (obj instanceof PrivateBankAlt privateBank)
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
        public void createAccount(String account) throws AccountAlreadyExistsException {
            System.out.println("Creating new account <" + account + "> to bank <" + name + ">");
            if (accountsToTransactions.containsKey(account))
                throw new AccountAlreadyExistsException("ACCOUNT <" + account +"> ALREADY EXISTS!\n");
            else {
                accountsToTransactions.put(account, List.of());
                System.out.println("=> SUCCESS!\n");
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
                        payment.setIncomingInterest(PrivateBankAlt.this.incomingInterest);
                        payment.setOutcomingInterest(PrivateBankAlt.this.outgoingInterest);
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
                System.out.println("=> SUCCESS!\n");

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
                if(accountsToTransactions.get(account).contains(transaction)){
                    throw new TransactionAlreadyExistException("THIS TRANSACTION ALREADY EXIST!\n");
                } else{
                    if(transaction instanceof Payment payment){
                        payment.setIncomingInterest(PrivateBankAlt.this.incomingInterest);
                        payment.setOutcomingInterest(PrivateBankAlt.this.outgoingInterest);
                    }
                    List<Transaction> transactionList = new ArrayList<>(accountsToTransactions.get(account));
                    transactionList.add(transaction);
                    accountsToTransactions.put(account, transactionList);
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
                payment.setIncomingInterest(PrivateBankAlt.this.incomingInterest);
                payment.setOutcomingInterest(PrivateBankAlt.this.outgoingInterest);
            }
            if(!accountsToTransactions.get(account).contains(transaction)){
                throw new TransactionDoesNotExistException("THIS TRANSACTION DOES NOT EXISTS!\n");
            } else if (!accountsToTransactions.containsKey(account)) {
                throw new AccountDoesNotExistException("ACCOUNT <" + account + "> DOES NOT EXISTS!\n");
            } else{
                List<Transaction> transactionList = new ArrayList<>(accountsToTransactions.get(account));
                transactionList.remove(transaction);
                accountsToTransactions.put(account, transactionList);
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
                payment.setIncomingInterest(PrivateBankAlt.this.incomingInterest);
                payment.setOutcomingInterest(PrivateBankAlt.this.outgoingInterest);
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
        public double getAccountBalance(String account) {
            double balance = 0.0;
            for (Transaction t : getTransactions(account)) {
                if (!(t instanceof Transfer)) {
                    balance += t.calculate();
                } else {
                    Transfer tf = (Transfer) t;
                    if (account.equals(tf.getSender())) balance -= t.calculate(); // Falls das aktuelle Konto ist der Sender (Betrag abgezogen)
                    else balance += t.calculate(); // Falls das aktuelle Konto ist der Empfänger (Betrag addiert)
                }
            }
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

    }
