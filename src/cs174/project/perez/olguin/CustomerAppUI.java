package cs174.project.perez.olguin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;


public class CustomerAppUI {
    private JLabel welcome;
    private JPanel panel;
    private JComboBox transactionType;
    private static JFrame frame;

    public CustomerAppUI(String id) {
        panel.setSize(400, 500);

        String name = JDBCExample.getNameFromId(id);
        welcome.setText("Welcome " + name + " !");
        //pull name from database with pin  and set welcome message on top
        //pull up dialog if more then one account
        //numOfAccounts(name,pin);  Implement this function to find out how many and which accounts are avaiable to pick
        //For now we assume only one
        HashMap<String, String> example = JDBCExample.getCheckingSavingAccounts(id);
        int length = example.size();
        String[] options = new String[length];
        int counter = 0;
        for (String s : example.keySet()) {
            //Show account type and account id to select accounts
            options[counter] = s + ":" + example.get(s);
            counter++;
        }
        String account;

        account = (String) JOptionPane.showInputDialog(null, "Chose which account you want to access",
                "Choose Account", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        String split[] = account.split(":");
        String accountType = split[0];
        String accountId = example.get(accountType);
        System.out.println(accountType + " " + accountId);


        String[] transactions = {
                " ",
                "Deposit",
                "Top-Up",
                "Withdrawal",
                "Purchase",
                "Transfer",
                "Collect",
                "Wire",
                "Pay-friend",
                "Quick-cash",
                "Quick-refill",
                "Write-Check",
                "Accrue-Interest"
        };


        for (String s : transactions) {
            transactionType.addItem(s);
        }

        //Depending on transaction selected call function that will populate necessary fields for the user to enter their info
        transactionType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if (transactionType.getSelectedItem().equals("Deposit")) {
                    System.out.println(transactionType.getSelectedItem());
                    Deposit(accountType, id, accountId);
                } else if (transactionType.getSelectedItem().equals("Top-Up")) {
                    System.out.println(transactionType.getSelectedItem());
                    ArrayList<String> pocketAccountId = new ArrayList<>();
                    pocketAccountId.addAll(JDBCExample.getPocketAccountIds(id));
                    if (pocketAccountId.size() == 0) {
                        JLabel warning = new JLabel("You don't have a pocket account.");
                        JOptionPane.showMessageDialog(warning, "You don't have a pocket account.");
                    } else {
                        TopUp(id, accountId, pocketAccountId);
                    }
                } else if (transactionType.getSelectedItem().equals("Withdrawal")) {
                    System.out.println(transactionType.getSelectedItem());
                    Withdrawal(accountType, id, accountId);
                } else if (transactionType.getSelectedItem().equals("Transfer")) {
                    System.out.println(transactionType.getSelectedItem());
                    if (example.size() >= 2) {
                        Transfer(accountType, id, accountId, example);
                    }
                    //MESSAGE THAT YOU DON'T HAVE ENOUGH ACCOUNTS TO TRANSFER TO
                    else {
                        JLabel warning = new JLabel("You only have one account.");
                        JOptionPane.showMessageDialog(warning, "You only have one account.");
                    }
                    //Transfer(pin);
                } else if (transactionType.getSelectedItem().equals("Wire")) {
                    System.out.println(transactionType.getSelectedItem());
                    if (example.size() >= 2) {
                        Wire(accountType, id, accountId, example);
                    }
                    //MESSAGE THAT YOU DON'T HAVE ENOUGH ACCOUNTS TO TRANSFER TO
                    else {
                        JLabel warning = new JLabel("You only have one account.");
                        JOptionPane.showMessageDialog(warning, "You only have one account.");
                    }
                } else if (transactionType.getSelectedItem().equals("Pay-friend")) {
                    System.out.println(transactionType.getSelectedItem());
                    ArrayList<String> pocketAccountId = new ArrayList<>();
                    pocketAccountId.addAll(JDBCExample.getPocketAccountIds(id));
                    if (pocketAccountId.size() == 0) {
                        JLabel warning = new JLabel("You don't have a pocket account.");
                        JOptionPane.showMessageDialog(warning, "You don't have a pocket account.");
                    } else {
                        PayFriend(id, pocketAccountId);
                    }
                } else if (transactionType.getSelectedItem().equals("Quick-cash")) {
                    System.out.println(transactionType.getSelectedItem());
                    QuickCash(id, accountId);

                } else if (transactionType.getSelectedItem().equals("Quick-refill")) {
                    System.out.println(transactionType.getSelectedItem());
                    ArrayList<String> pocketAccountId = new ArrayList<>();
                    pocketAccountId.addAll(JDBCExample.getPocketAccountIds(id));
                    if (pocketAccountId.size() == 0) {
                        JLabel warning = new JLabel("You don't have a pocket account.");
                        JOptionPane.showMessageDialog(warning, "You don't have a pocket account.");
                    } else {
                        QuickRefill(id, accountId, pocketAccountId);
                    }


                } else if (transactionType.getSelectedItem().equals("Purchase")) {
                    System.out.println(transactionType.getSelectedItem());
                    ArrayList<String> pocketAccountId = new ArrayList<>();
                    pocketAccountId.addAll(JDBCExample.getPocketAccountIds(id));
                    if (pocketAccountId.size() == 0) {
                        JLabel warning = new JLabel("You don't have a pocket account.");
                        JOptionPane.showMessageDialog(warning, "You don't have a pocket account.");
                    } else {
                        Purchase(id, pocketAccountId);
                    }

                } else if (transactionType.getSelectedItem().equals("Collect")) {
                    System.out.println(transactionType.getSelectedItem());
                    ArrayList<String> pocketAccountId = new ArrayList<>();
                    pocketAccountId.addAll(JDBCExample.getPocketAccountIds(id));
                    if (pocketAccountId.size() == 0) {
                        JLabel warning = new JLabel("You don't have a pocket account.");
                        JOptionPane.showMessageDialog(warning, "You don't have a pocket account.");
                    } else {
                        Collect(id, accountId, pocketAccountId);
                    }
                } else if (transactionType.getSelectedItem().equals("Write-Check")) {
                    if (accountType.equals("Checking") || accountType.equals("Student-Checking") || accountType.equals("Interest-Checking")) {
                        WriteCheck(id, accountId);
                    } else {
                        JLabel warning = new JLabel("You did not select your checking account.");
                        JOptionPane.showMessageDialog(warning, "You did not select your checking account.");
                    }

                } else if (transactionType.getSelectedItem().equals("Accrue-Interest")) {
                    AccrueInterest(id, accountId);
                }
            }

        });


        //Deposit, Top-Up, Withdrawl, Purchase, Transfer, collect, Wire , Pay-friend,quick-cash
        //
    }

    public void Deposit(String account, String customerId, String accountId) {
        //Function:Add money to the checking or savings account balance
        //open up a dialog like the checking and balance one

        String value = (String) JOptionPane.showInputDialog("Enter an amount");
        //get current timestamp
        Double amount;
        try {
            amount = Double.parseDouble(value);
            System.out.println(amount);
            JDBCExample.CustomerDepositTransaction(accountId, customerId,
                    amount, "Deposit");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


    }

    public void Withdrawal(String account, String customerId, String accountId) {
        //Function:Add money to the checking or savings account balance
        //open up a dialog like the checking and balance one

        String value = (String) JOptionPane.showInputDialog("Enter an amount");
        //get current timestamp
        Double amount;
        try {
            amount = Double.parseDouble(value);
            System.out.println(amount);
            Double result = JDBCExample.getBalance(accountId) - amount;
            if (result > 0.01) {
                JDBCExample.CustomerWithdrawalTransaction(accountId, customerId,
                        amount, "Withdrawal");
            } else {
                String statement = "Balance has reached an amount of $0.01 or less. Closing account..";
                JOptionPane.showMessageDialog(null, statement);
                JDBCExample.CloseAccount(accountId);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


    }

    public void Wire(String selectedAccount, String customerId, String accountId, HashMap otherAccounts) {
        //Function: Subtract money from one savings or checking account balance & add to another

        JTextField value = new JTextField();
        JComboBox accounts = new JComboBox();
        for (Object account : otherAccounts.keySet()) {
            if (!selectedAccount.equals(account)) {
                accounts.addItem(account.toString());
            }
        }
        Double amount;
        Object[] message = {
                "Enter an amount:", value,
                "Select an account:", accounts
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            amount = Double.parseDouble(value.getText());
            String selection = accounts.getSelectedItem().toString();
            Double result = JDBCExample.getBalance(accountId) - amount;
            if (result > 0.01) {
                JDBCExample.CustomerWireTransaction(accountId, customerId, amount, otherAccounts.get(selection).toString());
            }
            else {
                String statement = "Balance has reached an amount of $0.01 or less. Closing account..";
                JOptionPane.showMessageDialog(null, statement);
                JDBCExample.CloseAccount(accountId);
                }
        }
    }

    public void TopUp(String taxid, String selectedAccountId, ArrayList<String> pocketAccountIds) {

        JTextField value = new JTextField();
        JComboBox accounts = new JComboBox();
        accounts.addItem(" ");
        for (Object account : pocketAccountIds) {
            accounts.addItem(account.toString());
        }
        Double amount;
        Object[] message = {
                "Enter an amount:", value,
                "Select a Pocket Account:", accounts
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            amount = Double.parseDouble(value.getText());
            String pocket = accounts.getSelectedItem().toString();
            Double result = JDBCExample.getBalance(selectedAccountId) - amount;
            if (result > 0.01) {
                JDBCExample.CustomerTopUpTransaction(selectedAccountId, taxid, pocket, amount);
            }
            else {
                String statement = "Balance has reached an amount of $0.01 or less. Closing account..";
                JOptionPane.showMessageDialog(null, statement);
                JDBCExample.CloseAccount(selectedAccountId);
            }
        }

    }

    public void PayFriend(String taxid, ArrayList<String> pocketAccountIds) {

        JTextField value = new JTextField();
        JComboBox accounts = new JComboBox();
        JTextField friendIdField = new JTextField();

        accounts.addItem(" ");
        for (Object account : pocketAccountIds) {
            accounts.addItem(account.toString());
        }
        Double amount;
        Object[] message = {
                "Enter an amount:", value,
                "Select a Pocket Account to Withdraw From:", accounts,
                "Enter friends accountId:", friendIdField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            amount = Double.parseDouble(value.getText());
            String pocketId = accounts.getSelectedItem().toString();
            String friendId = friendIdField.getText();
            System.out.println(amount + pocketId + friendId);
            Double result = JDBCExample.getBalance(pocketId) - amount;
            if (result > 0.01) {
                JDBCExample.CustomerPayFriendTransaction(taxid, friendId, pocketId, amount);
            }
            else {
                String statement = "Balance has reached an amount of $0.01 or less. Closing account..";
                JOptionPane.showMessageDialog(null, statement);
            }
        }

    }

    public void Purchase(String taxid, ArrayList<String> pocketAccountIds) {

        JTextField value = new JTextField();
        JComboBox accountsField = new JComboBox();

        accountsField.addItem(" ");
        for (Object account : pocketAccountIds) {
            accountsField.addItem(account.toString());
        }
        Double amount;
        Object[] message = {
                "Enter an amount:", value,
                "Select a Pocket Account to Withdraw From:", accountsField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            amount = Double.parseDouble(value.getText());
            String pocketId = accountsField.getSelectedItem().toString();
            Double result = JDBCExample.getBalance(pocketId) - amount;
            if (result > 0.01) {
                JDBCExample.CustomerPurchaseTransaction(taxid, pocketId, amount);
            }
            else {
                String statement = "Balance has reached an amount of $0.01 or less. Closing account..";
                JOptionPane.showMessageDialog(null, statement);
            }
        }

    }

    public void Collect(String taxid, String accountid, ArrayList<String> pocketAccountIds) {

        JTextField value = new JTextField();
        JComboBox accounts = new JComboBox();

        accounts.addItem(" ");
        for (Object account : pocketAccountIds) {
            accounts.addItem(account.toString());
        }
        Double amount;
        Object[] message = {
                "Enter an amount:", value,
                "Select a Pocket Account to Collect From:", accounts
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            amount = Double.parseDouble(value.getText());
            String pocketId = accounts.getSelectedItem().toString();
            Double result = JDBCExample.getBalance(pocketId) - amount;
            if (result > 0.01) {
                JDBCExample.CustomerCollectTransaction(taxid, pocketId, accountid, amount);
            }
            else {
                String statement = "Balance has reached an amount of $0.01 or less. Closing account..";
                JOptionPane.showMessageDialog(null, statement);
            }
        }

    }

    public void WriteCheck(String taxid, String accountid) {

        String value = (String) JOptionPane.showInputDialog("Enter an amount");
        Double amount = Double.parseDouble(value);
        Double result = JDBCExample.getBalance(accountid) - amount;
        if (result > 0.01) {
            String checkNumber = JDBCExample.CustomerWriteCheckTransaction(taxid, accountid, amount);
            JOptionPane.showMessageDialog(null, checkNumber + " is your Check Number !");

        }
        else {
            String statement = "Balance has reached an amount of $0.01 or less. Closing account..";
            JOptionPane.showMessageDialog(null, statement);
            JDBCExample.CloseAccount(accountid);
        }
    }

    public void QuickCash(String taxid, String accountid) {

        Double result = JDBCExample.getBalance(accountid) - 20.0;
        if (result > 0.01) {
            JDBCExample.CustomerQuickCashTransaction(taxid, accountid, 20.0);
        }
        else {
            String statement = "Balance has reached an amount of $0.01 or less. Closing account..";
            JOptionPane.showMessageDialog(null, statement);
            JDBCExample.CloseAccount(accountid);
        }

    }

    public void QuickRefill(String taxid, String accountid, ArrayList<String> pocketAccountIds) {

        JComboBox accountsField = new JComboBox();

        accountsField.addItem(" ");
        for (Object account : pocketAccountIds) {
            accountsField.addItem(account.toString());
        }
        Object[] message = {
                "Select a Pocket Account to Withdraw From:", accountsField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String pocketId = accountsField.getSelectedItem().toString();
            Double result = JDBCExample.getBalance(pocketId) - 20.0;
            if (result > 0.01) {
                JDBCExample.CustomerQuickRefillTransaction(taxid, accountid, 20.0, pocketId);
            }
            else {
                String statement = "Balance has reached an amount of $0.01 or less. Closing account..";
                JOptionPane.showMessageDialog(null, statement);
            }
        }


    }

    public void Transfer(String selectedAccount, String customerId, String accountId, HashMap otherAccounts) {
        //Function: Transfer money from one savings or checking account balance & add to another
        //Something else

        JTextField value = new JTextField();
        JComboBox accounts = new JComboBox();
        for (Object account : otherAccounts.keySet()) {
            if (!selectedAccount.equals(account)) {
                accounts.addItem(account.toString());
            }
        }
        Double amount;
        Object[] message = {
                "Enter an amount to Transfer:", value,
                "Select an account:", accounts
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            amount = Double.parseDouble(value.getText());
            Double result = JDBCExample.getBalance(accountId) - amount;
            if (result > 0.01) {
                if (amount > 2000.0) {
                    JOptionPane.showMessageDialog(null, "You can not transfer more than 2000 dollars");
                } else {
                    String selection = accounts.getSelectedItem().toString();
                    JDBCExample.CustomerTransferTransaction(accountId, customerId, amount, otherAccounts.get(selection).toString());
                }
            }
            else {
                String statement = "Balance has reached an amount of $0.01 or less. Closing account..";
                JOptionPane.showMessageDialog(null, statement);
                JDBCExample.CloseAccount(accountId);
            }

        }

    }

    public void AccrueInterest(String taxid, String accountid) {
        Double interest = JDBCExample.getInterest(accountid);
        System.out.println(interest);
        Double balance = JDBCExample.AccrueInterest(accountid, taxid);
        JOptionPane.showMessageDialog(null, "You accrued " + balance + " dollars");
        JDBCExample.CustomerAccruedInterestTransaction(taxid, accountid, balance);
    }


    public static void main(String[] args) {
        frame = new JFrame("CustomerApp");
        frame.setContentPane(new CustomerAppUI(args[0]).panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 4, new Insets(0, 0, 0, 0), -1, -1));
        welcome = new JLabel();
        welcome.setText("Label");
        panel.add(welcome, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 3, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        transactionType = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        transactionType.setModel(defaultComboBoxModel1);
        panel.add(transactionType, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Enter your desired Transaction");
        panel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}