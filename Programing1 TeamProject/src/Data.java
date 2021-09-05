import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Data {
    // Setup Data
    Scanner sc = new Scanner(System.in);
    LocalDate DateBegin;
    LocalDate DateEnd;
    int ArayOfDays;
    int ArrayOfWeeks;
    String Area;
    String[] RangeOfTime;
    String newInput;
    String breakDay[];
    String PickArea;

    // Constructors of class Data
    public Data() {
    }

    public Data(String Area, LocalDate DateBegin, LocalDate DateEnd, String[] RangeOfTime) {
        this.Area = Area;
        this.DateBegin = DateBegin;
        this.DateEnd = DateEnd;
        this.RangeOfTime = RangeOfTime;
    }

// ALL SUPPORT FUNCTIONS

    // Convert String to LocalDate type
    public LocalDate dateInput(String userInput) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date = LocalDate.parse(userInput, dateFormat);
        return date;
    }

    // function


    public void insertStart() {
        System.out.println("Input start day By this format: M-D-YYY (Example: 1/1/2001) ");
        dayInsert();
        this.DateBegin = dateInput(this.newInput);
    }

    public void insertEnd() {
        System.out.println("Input end day By this format: M-D-YYY (Example: 1/1/2001) ");
        dayInsert();
        this.DateEnd = dateInput(this.newInput);
    }

    // Return an Array of all day from Start Date to End Date of user's input
    public String[] arrayOfTime() {
        List<LocalDate> listOfDates = this.DateBegin.datesUntil(this.DateEnd.plusDays(1)).collect(Collectors.toList());
        this.RangeOfTime = new String[listOfDates.size()];
        DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");

        for (int i = 0; i < this.RangeOfTime.length; i++) {
            this.RangeOfTime[i] = (listOfDates.get(i)).format(format);
        }
        return this.RangeOfTime;
    }


    // check if the day input is valid Local date or not
    public void dayInsert() {
        String s = sc.nextLine();
        if (s.contains("/")) {
            this.breakDay = s.split("/");
            int month = Integer.parseInt(this.breakDay[0]);
            int day = Integer.parseInt(this.breakDay[1]);
            int year = Integer.parseInt(this.breakDay[2]);
            // check if valid month
            if (month < 1 || month > 12) {
                System.out.println("Month input is invalid");
                dayInsert();
            }
            // Check if leap day or not
            if (month == 2) {
                if (day == 29) {
                    if (year == 2020) {
                    } else {
                        System.out.println("""
                                Invalid input,
                                2021 is not a leap year so there is no  2/29 in 2021.
                                Only 2020 is a leap year.
                                Please input again.
                                        """);
                        dayInsert();
                    }
                }
            }
            // check if valid day
            if (day < 1 || day > 31) {
                System.out.println("Day input is invalid");
                dayInsert();
            }

            if (year != 2020 && year != 2021) {
                System.out.println("Year input is invalid! (The data is from 2020 to 2021)");
                dayInsert();
            }

            for (int i = 0; i < breakDay.length; i++) {
                this.newInput = s;
            }
        } else {
            this.breakDay = s.split("-");
            int month = Integer.parseInt(this.breakDay[0]);
            int day = Integer.parseInt(this.breakDay[1]);
            int year = Integer.parseInt(this.breakDay[2]);

            if (month < 1 || month > 12) {
                System.out.println("Month input is invalid");
                dayInsert();
            }

            if (month == 2) {
                if (day == 29) {
                    if (year == 2020) {
                    } else {
                        System.out.println("""
                                Invalid input,
                                2021 is not a leap year so there is no  2/29 in 2021.
                                Only 2020 is a leap year.
                                Please input again.
                                        """);
                        dayInsert();
                    }
                }
            }

            if (day < 1 || day > 31) {
                System.out.println("Day input is invalid! ");
                dayInsert();
            }

            if (year != 2020 && year != 2021) {
                System.out.println("Year input is invalid! (The data is from 2020 to 2021)");
                dayInsert();
            }

            for (int i = 0; i < breakDay.length; i++) {
                this.newInput = String.join("/", breakDay[0], breakDay[1], breakDay[2]);
            }
        }
    }



    // MAIN FUNCTIONS
    // Methods
    public void InsertArea() throws IOException {
        System.out.println("Welcome to the Checking COVID-19 DATA Program");
        System.out.println("""
                Please select country(By Press (1)) or continent (By Press (2)) to choose the Area you want to check in:
                1. By Country
                2. By Continent""");
        // choosing case
        this.PickArea = sc.nextLine();

        switch (this.PickArea) {
            case "1":
                System.out.println("Input the country you want to search: ");
                this.Area = sc.nextLine();
                // Check if input valid with CheckString function
                String[] countrychar = Check.toArray(this.Area);
                while (!Check.CheckString(countrychar)) {
                    System.out.println("Input the country you want to search: ");
                    this.Area = sc.nextLine();
                    countrychar = Check.toArray(this.Area);
                }
                this.Area = Check.toString(countrychar); // this.Area now have the correct typing version
                this.Area = this.Area.toLowerCase(Locale.ROOT);// but bc the group use the lowercase then this.Area will
                                                               // be converted to lowercase!
                break;
            case "2":
                System.out.println("Input the continent you want to search: ");
                this.Area = sc.nextLine();
                // Check if input valid with CheckString function
                String[] continentchar = Check.toArray(this.Area);
                while (!Check.CheckString(continentchar, 2)) {
                    System.out.println("Input the continent you want to search: ");
                    this.Area = sc.nextLine();
                    continentchar = Check.toArray(this.Area);
                }
                this.Area = Check.toString(continentchar);
                this.Area = this.Area.toLowerCase(Locale.ROOT);
                break;
            default:
                System.out.println(" Invalid data, please try again!");
                InsertArea();
        }
    }




    // Choose the method for select time Range type input:
    public void timeFieldSet() {
        String opt, sub_opt;

        System.out.println("""
                Choosing the method that help you take the range of time to of data:
                Method 1. Pair Method 
                Method 2. Days or weeks from specific date
                Method 3. Days or weeks to specific date""");
        opt = sc.nextLine();
        switch (opt) {
            // Choosing the get Pair method
            case "1":
                getPair();
                break;
            // Choosing the get day from method
            case "2":
                System.out.println("Input the range of time by: Day (Press 1) or by: Week (Press 2) ");
                sub_opt = sc.nextLine();
                switch (sub_opt) {
                    // by day
                    case "1":
                        getDaysFrom();
                        break;
                    // by week
                    case "2":
                        TakeWeekFrom();
                        break;
                    default:
                        System.out.println("INVALID. Please try to input again");
                        timeFieldSet();
                }
                break;
            // Choosing the get day to method
            case "3":
                System.out.println("Input the range of time by: Day (Press 1) or by: Week (Press 2)  ");
                sub_opt = sc.nextLine();
                switch (sub_opt) {
                    // by day
                    case "1":
                        getDaysTo();
                        break;
                    // by week
                    case "2":
                        TakeWeekTo();
                        break;
                    default:
                        System.out.println("INVALID. Please try to input again");
                        timeFieldSet();
                }
                break;
            default:
                System.out.println("INVALID. Please try to input again");
                timeFieldSet();
        }
    }

    // function to method 1: get Pair
    public String[] getPair() {
        insertStart();
        insertEnd();
        arrayOfTime();
        System.out.println("The data you check will start from the date " + DateBegin + " to the date " + DateEnd);
        return this.arrayOfTime();
    }

    // function to method 2: get Days from
    public String[] getDaysFrom() {
        insertStart();
        System.out.println("Enter the number of days from start date (Start Date inclusive): ");
        this.ArayOfDays = sc.nextInt();

        // Count
        this.DateEnd = DateBegin.plusDays(this.ArayOfDays);
        arrayOfTime();

        System.out.println("The data you check will start from the date " + DateBegin + " to the date " + DateEnd);
        return this.arrayOfTime();
    }

    // function to method 3: get Days to
    public String[] getDaysTo() {
        sc.nextLine();
        insertEnd();
        System.out.println("Enter the number of days before end date (End Date inclusive): ");
        this.ArayOfDays = sc.nextInt();

        // Count
        this.DateBegin = this.DateEnd.minusDays(this.ArayOfDays);
        arrayOfTime();

        System.out.println("The data you check will start from the date " + DateBegin + " to the date " + DateEnd);
        return this.arrayOfTime();
    }

    public String[] TakeWeekFrom() {
        insertStart();
        System.out.println("Input a number of week you want to check: ");
        this.ArrayOfWeeks = sc.nextInt();

        // Convert weeks to days
        this.ArayOfDays = this.ArrayOfWeeks * 7;
        this.DateEnd = DateBegin.plusDays(this.ArayOfDays);

        arrayOfTime();
        return this.arrayOfTime();
    }

    public String[] TakeWeekTo() {
        sc.nextLine();
        insertEnd();
        System.out.println("Input a number of week you want to check: ");
        this.ArrayOfWeeks = sc.nextInt();

        // Convert to days
        this.ArayOfDays = this.ArrayOfWeeks * 7;
        this.DateBegin = this.DateEnd.minusDays(this.ArayOfDays);
        arrayOfTime();
        return this.arrayOfTime();
    }

}
