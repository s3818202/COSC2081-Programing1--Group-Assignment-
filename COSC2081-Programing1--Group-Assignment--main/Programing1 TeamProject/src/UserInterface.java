import java.util.Scanner;

public class UserInterface {
    // Set up
    Scanner scan = new Scanner(System.in);
    Display disp = new Display();
    Summary suma = new Summary();
    String option;
    // Constructors of class UserInterface
    public UserInterface() {
    }

    public UserInterface(String option) {
        this.option = option;
    }

    public void Runagain() throws Exception {
        UserInterface Interface = new UserInterface();
        Interface.userInterface();
    }

    public void userInterface() throws Exception {
        disp.Type();
        System.out.println("Do you want to restart (1) or exit (2) the the program?");
        option = scan.nextLine();

        if (option.equals("1")) {
            Runagain();
        } else if (option.equals("2")) {
            System.exit(0);
        }
        else {
            System.out.println("Invalid Input. PLease try again !!! ");
            Thread.sleep(3000);
            userInterface();
        }
    }

    
}
