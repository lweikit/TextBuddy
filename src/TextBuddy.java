import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author WeiKit
 *
 * Assumptions:
 * 1. User knows what are the parameters required for all commands
 * 2. User might accidentally press enter while typing the commands
 * 3. User might type the wrong command keyword input
 * 
 * This class is used to make a list of text and save it into a text file.
 * The program saves when user types the command 'exit'
 * (or it can be changed to every command by un-commenting the writeFile in main)
 * An example of the program interaction is shown below:
 * 
 c:> TextBuddy.exe mytextfile.txt  (OR c:>java  TextBuddy mytextfile.txt)
 Welcome to TextBuddy. mytextfile.txt is ready for use
 command: add little brown fox
 added to mytextfile.txt: “little brown fox”
 command: display
 1. little brown fox
 command: add jumped over the moon
 added to mytextfile.txt: “jumped over the moon”
 command: display
 1. little brown fox
 2. jumped over the moon
 command: delete 2
 deleted from mytextfile.txt: “jumped over the moon”
 command: display
 1. little brown fox
 command: clear
 all content deleted from mytextfile.txt
 command: display
 mytextfile.txt is empty
 command: exit
 c:>
 *
 */

public class TextBuddy {

	// Response messages
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy, %1$s is ready for use.";
	private static final String MESSAGE_PROMPT = "Enter Command: ";
	private static final String MESSAGE_ADDED = "Added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETED = "Deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEARED = "All contents deleted from %1$s";
	
	// Error messages
	private static final String MESSAGE_INVALID_FORMAT = "Invalid command format: %1$s";
	private static final String MESSAGE_EMPTY_LIST = "%1$s is empty!";
	private static final String MESSAGE_NOT_FOUND = "Index %1$s is not found!";
	private static final String MESSAGE_NO_FILE = "No file initialized!";
	
	// Possible command types
	enum COMMAND_TYPE {
		ADD, DELETE, DISPLAY, CLEAR, INVALID, EXIT
	};

	private static String _fileName;
	private static ArrayList<String> _listText = new ArrayList<String>();
	private static Scanner scanner = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		initialize(args);
		
		showToUser(String.format(MESSAGE_WELCOME, _fileName));
		while (true) {
			System.out.print(MESSAGE_PROMPT);
			String userCommand = scanner.nextLine();
			String feedback = executeCommand(userCommand);
			showToUser(feedback);
			//writeFile();
		}
	}

	private static void showToUser(String text) {
		System.out.println(text);
	}

	public static String executeCommand(String userCommand) throws IOException {
		if (userCommand.trim().equals(""))
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);

		String commandTypeString = getFirstWord(userCommand);

		COMMAND_TYPE commandType = determineCommandType(commandTypeString);

		switch (commandType) {
		case ADD:
			return addText(userCommand);
		case DELETE:
			return deleteText(userCommand);
		case DISPLAY:
			return displayText();
		case CLEAR:
			return clearText();
		case EXIT:
			writeFile();
			System.exit(0);
		case INVALID:
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		default:
			//throw an error if the command is not recognized
			throw new Error("Unrecognized command type");
		}
	}

	/**
	 * This operation determines which of the supported command types the user
	 * wants to perform
	 * 
	 * @param commandTypeString is the first word of the user command
	 */
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return COMMAND_TYPE.CLEAR;
		} else if (commandTypeString.equalsIgnoreCase("exit")) {
		 	return COMMAND_TYPE.EXIT;
		} else {
			return COMMAND_TYPE.INVALID;
		}
	}

	
	// Program operations
	
	/**
	 * This operation is used to add the message into the cache
	 * 
	 * @param userCommand is the full string user has entered as the command
	 * 
	 * @return the response that message is added
	 */
	private static String addText(String userCommand) {

		String text = removeFirstWord(userCommand);

		if (text.trim().equals("")) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		_listText.add(text);
		return String.format(MESSAGE_ADDED, _fileName, text);
	}

	/**
	 * This operation is used to delete the message from the cache
	 * 
	 * @param userCommand is the full string user has entered as the command
	 * 
	 * @return the response that message is deleted
	 */
	private static String deleteText(String userCommand) {
		
		String text = removeFirstWord(userCommand);
		
		if (text.trim().equals("")) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		int index = Integer.parseInt(text);
		
		if (_listText.size() == 0) {
			return String.format(MESSAGE_EMPTY_LIST, _fileName);
		} else if (index > _listText.size()) {
			return String.format(MESSAGE_NOT_FOUND, text);
		} else {
			text = _listText.remove(index - 1);
			return String.format(MESSAGE_DELETED, _fileName, text);
		}
	}

	/**
	 * This operation is used to display all the text from the cache
	 *  
	 * @return the string of all texts in the cache
	 */
	private static String displayText() {
		
		String feedback = "";
		
		if (_listText.size() == 0) {
			return String.format(MESSAGE_EMPTY_LIST, _fileName);
		} else {
			for (int i = 0; i < _listText.size(); i ++) {
				feedback += i+1 + ". " + _listText.get(i);
				if (i != _listText.size() - 1) {
					feedback += "\n";
				}
			}
		}
		
		return feedback;
	}

	/**
	 * This operation is used to clear all the text from the cache
	 *  
	 * @return the response that the cache has been cleared
	 */
	private static String clearText() {
		_listText.clear();
		return String.format(MESSAGE_CLEARED, _fileName);
	}
	
	
	// Back-end operations
	
	/**
	 * This operation determines if there's an input file
	 * if file exists, readFile()
	 * else exit program
	 */
	private static void initialize(String[] args) throws IOException {
		if (args.length == 0) {
			showToUser(MESSAGE_NO_FILE);
			System.exit(0);
		} else {
			_fileName = args[0];
			File file = new File(_fileName);
			
			if (file.exists()) {
				readFile();
			}
		}
	}
	
	private static void readFile() throws IOException {
		try {
			FileReader fr = new FileReader(_fileName);
			BufferedReader br = new BufferedReader(fr);
			
			String line = br.readLine();
			while (line != null) {
				_listText.add(line);
				line = br.readLine();
			}
			
			br.close();
			fr.close();
		} catch (IOException e) {
			System.err.println("File reading incomplete!");
			e.printStackTrace();
		} 
	}
	
	private static void writeFile() throws IOException {
		try {
			FileWriter fw = new FileWriter(_fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (int i = 0; i < _listText.size(); i ++) {
				bw.write(_listText.get(i));
				bw.newLine();
			}
			
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

}