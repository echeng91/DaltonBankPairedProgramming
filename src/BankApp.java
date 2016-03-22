import java.util.Scanner;
import java.text.NumberFormat;

public class BankApp {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		DaltonBank bank = new DaltonBank();
		bank.connect();

		System.out.println("Welcome to Dalton Corp Savings and Loan");
		addCustomers(bank, sc);
		addAccounts(bank, sc);
		addTransactions(bank, sc);
		System.out.println(summary(bank, sc));

		bank.disconnect();
		sc.close();
	}

	public static void addCustomers(DaltonBank bank, Scanner sc)
	{
		int custID= 0;
		String custName = "";
		System.out.println("Please create the user(s)");
		while (custID != -1)
		{
			System.out.print("Enter an customer # or -1 to stop entering customers : ");
			try
			{
				custID = sc.nextInt();
				sc.nextLine();
				if (custID != -1)
				{
					System.out.print("Enter customer name: ");
					custName = sc.nextLine();
					bank.addCustomer(custName, custID);
				}
			}
			catch (java.util.InputMismatchException e)
			{
				e.getMessage();
			}
		}
	}
	public static void addAccounts(DaltonBank bank, Scanner sc)
	{
		int custID = 0;
		int accID = 0;
		double balance = 0.0;
		System.out.println("Please create the user account(s)");
		while (accID != -1)
		{
			System.out.print("Enter an account # or -1 to stop entering accounts : ");
			try
			{
				accID = sc.nextInt();
				sc.nextLine();
			}
			catch (java.util.InputMismatchException e)
			{
				e.getMessage();
			}
			if (accID != -1)
			{
				try
				{
					System.out.print("Enter customer ID for this account: ");
					custID = sc.nextInt();
					sc.nextLine();
					System.out.print("Enter balance for this account: ");
					balance = sc.nextDouble();
					sc.nextLine();
					bank.addAccount(custID, accID, balance);
				}
				catch(java.util.InputMismatchException e)
				{
					e.getMessage();
				}
			}
		}
	}
	public static void addTransactions(DaltonBank bank, Scanner sc)
	{
		int accID = 0;
		double amount = 0.0;
		String transType = "";
		String dateString ="";

		while(!transType.equals("-1"))
		{
			System.out.print("Enter a transaction type(Check, Debit card, Deposit, or Withdrawal) or -1 finish: ");
			transType = sc.nextLine();
			if(!transType.equals("-1"))
			{
				try
				{
					System.out.print("Enter the account #: ");
					accID = sc.nextInt();
					sc.nextLine();
					System.out.print("Enter the amount of the ");
					if(transType.equalsIgnoreCase("transfer"))
					{
						System.out.print("transfer: ");
						amount = sc.nextDouble();
						sc.nextLine();
						System.out.print("Enter the account # to transfer to: ");
						int otherAccID = sc.nextInt();
						sc.nextLine();
						System.out.print("Enter the date of the transaction: ");
						dateString = sc.nextLine();
						bank.addTransaction(accID, amount, "transfer from", dateString);
						bank.addTransaction(otherAccID, amount, "transfer to", dateString);
					}
					else
					{
						switch(transType)
						{
						case "c":
						case "check": System.out.print("check: ");
						break;
						case "d":
						case "debit": System.out.print("debit: ");
						break;
						case "de":
						case "deposit": System.out.print("deposit: ");
						break;
						case "w":
						case "withdrawal": System.out.print("withdrawal: ");
						break;
						}
						amount = sc.nextDouble();
						sc.nextLine();
						System.out.print("Enter the date of the transaction: ");
						dateString = sc.nextLine();
						bank.addTransaction(accID, amount, transType, dateString);
					}
				}
				catch(java.util.InputMismatchException e)
				{
					e.getMessage();
				}
			}
		}
	}

	public static String summary(DaltonBank bank, Scanner sc)
	{
		String summaryString = "The balance for account ";
		NumberFormat currency = NumberFormat.getCurrencyInstance();
		try
		{
			System.out.print("Enter account number for balance: ");
			int accID = sc.nextInt();

			double balance = bank.currentBalance(accID);
			summaryString += accID + " is " + currency.format(balance);
		}
		catch(java.util.InputMismatchException e)
		{
			e.getMessage();
		}
		return summaryString;
	}
}

