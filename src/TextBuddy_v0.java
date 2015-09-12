import java.io.*;
import java.util.*;

public class TextBuddy_v0 {

	private static String _fileName;
	private static File _file;
	private static ArrayList<String> _listText = new ArrayList<String>();
	
	private static void addText(String text) {
		_listText.add(text);
		System.out.println("Added to the list: \"" + text  +"\"");
	}
	
	private static void deleteText(int index) {
		if (_listText.size() == 0) {
			System.out.println("The list is empty!");
		} else if (index >= _listText.size()) {
			System.out.println("Wrong index given!");
		} else {
			String result = _listText.remove(index);
			System.out.println("Deleted from the list: \"" + result + "\"");
		}
	}
	
	private static void displayList() {
		if (_listText.size() == 0) {
			System.out.println("The list is empty!");
		} else {
			for (int i = 0; i < _listText.size(); i ++) {
				System.out.println(i+1 + ". " + _listText.get(i));
			}
		}
	}
	
	private static void clearList() {
		_listText.clear();
		System.out.println("All contents deleted.");
	}
	
	private static void getHelp() {
		System.out.println("Unknown command, try again.");
		System.out.println("If you need help, type 'help'");
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
			
			System.out.println(_listText.size() + " items loaded from " + _fileName);
			
			br.close();
			fr.close();
		} catch (IOException e) {
			System.err.println("File reading incomplete!");
			e.printStackTrace();
		} 
	}
	
	private static void writeFile() throws IOException {
		try {
			FileWriter fw = new FileWriter(_file.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(fw);
			
			for (int i = 0; i < _listText.size(); i ++) {
				bw.write(_listText.get(i));
				bw.newLine();
			}
			
			System.out.println(_listText.size() + " items saved into " + _fileName);
			
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		
		try {
			_fileName = args[0];
			_file = new File(_fileName);
			
			if (_file.exists()) {
				readFile();
			} else {
				System.out.println(_fileName + " does not exist! Creating new file...");
				_file.createNewFile();
			}
			
			System.out.println("Welcome to TextBuddy. The list is ready for use.");
			String text = scanner.nextLine();
			while (!text.equals("exit")) {
				if (text.indexOf("add") == 0) {
					text = text.substring(4);
					addText(text);
				} else if (text.indexOf("delete") == 0) {
					text = text.substring(7);
					int index = Integer.parseInt(text) - 1;
					deleteText(index);
				} else if (text.indexOf("display") == 0) {
					displayList();
				} else if (text.indexOf("clear") == 0) {
					clearList();
				} else {
					getHelp();
				}
				
				text = scanner.nextLine();
			}
			
			writeFile();
			System.out.println("Goodbye! See you again :)");
			
			scanner.close();
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("File not specified!");
		}
	}	
}