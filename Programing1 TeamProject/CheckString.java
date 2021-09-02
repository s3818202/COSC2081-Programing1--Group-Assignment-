import java.io.*;
import java.util.*;

public class CheckString {
	public static void main(String[] args) throws IOException{
		String[] arr = Check.toArray("VietNAM");
			Check.CheckString(arr);
		}
	}
class Check{
	public static String[] toArray(String X) { // Can use this function externally
		String[] res = new String[X.length()];
		for (int i=0;i<X.length();i++) {
			String a = Character.toString(X.charAt(i));
			res[i] = a;
		}
		return res;
	}
	public static String toString(String[] args) { // Can use this function externally
		StringBuilder stringb = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
            stringb.append(args[i]);
        }
		String joinedstr3 = stringb.toString();
        return joinedstr3;
	}
	public static boolean CheckString(String[] IP) throws IOException {
		String X = toString(IP).toLowerCase();
		Scanner input = new Scanner(new File("src/nameOfcountryAndcontinent.txt")); // Specifying file
		while (input.hasNext()) {
			String temp = input.nextLine();
			String received = temp.toLowerCase();
			if (X.equals(received)) {
				String[] copier = toArray(temp);
				for (int i =0; i<copier.length;i++) { // Change IP array
					IP[i] = copier[i];
				}
				System.out.println("Your input is recorded");
				input.close();
				return true;
			}
		}
		System.out.println("Your input is invalid, please try again");
		input.close();
		return false;
		//Call back function.....	
	}
	public static boolean CheckString(String[] IP, int i) throws IOException { // For checking continent
		String X = toString(IP).toLowerCase();
		String[] arr = {"Africa","Asia","Europe","North America","Oceania", "South America",""}; //Blank in case of OWID_ASI
		for (String a:arr) {
			String temp = a;
			String received = a.toLowerCase();
			if(X.equals(received)) {
				String[] copier = toArray(temp);
				for (int j =0; j<copier.length;j++) { // Change IP array
					IP[j] = copier[j];
				}
				System.out.println("Your input is recorded");
				return true;
			}
		}
		System.out.println("Your input is invalid, please try again");
		return false;
	}
}
