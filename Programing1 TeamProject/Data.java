import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Data {
    // Setup  Data Fields
    String Area;
    int NumberDays;
    int NumberWeeks;
    Scanner sc = new Scanner(System.in);
    LocalDate DateStart;
    LocalDate DateEnd;
    String[] TimeRange;
    String newInput;
    String splitDay[];
    String chooseArea;

    // Constructors of class Data
    public Data() {
    }

    public Data(String Area, LocalDate DateStart, LocalDate DateEnd, String[] TimeRange) {
        this.Area = Area;
        this.DateStart = DateStart;
        this.DateEnd = DateEnd;
        this.TimeRange = TimeRange;
    }

    // Methods
    public void inputArea() throws IOException {
        System.out.println("Welcome to the Checking COVID-19 DATA Program");
        System.out.println("""
                Please select country(By Press (1)) or continent (By Press (2)) to choose the Area you want to check in:
                1. By Country
                2. By Continent""");
       // choosing case
        this.chooseArea = sc.nextLine();

        switch (this.chooseArea) {
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
                this.Area = this.Area.toLowerCase(Locale.ROOT);//but bc the group use the lowercase then this.Area will be converted to lowercase!
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
                inputArea();
        }
    }

    // Convert String to LocalDate type
    public LocalDate dateInput(String userInput) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date = LocalDate.parse(userInput, dateFormat);
        return date;
    }


    // Choose the method for select time Range type input:
    public void timeRangeType() {
        String opt, sub_opt;

        System.out.println("""
                Choosing the method that help you take the range of time to of data:
                Method 1. Pair Method by Input StartDate and EndDate
                Method 2. Input Number of days or weeks from a particular date
                Method 3. Input Number of days or weeks to a particular date""");
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
                        getWeekFrom();
                        break;
                    default:
                        System.out.println("INVALID. Please try again");
                        timeRangeType();
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
                        getWeekTo();
                        break;
                    default:
                        System.out.println("INVALID. Please try again");
                        timeRangeType();
                }
                break;
            default:
                System.out.println("INVALID. Please try again");
                timeRangeType();
        }
    }
    // function
    public String[] getWeekFrom() {
        inputStart();
        System.out.println("Input a number of week you want to check: ");
        this.NumberWeeks = sc.nextInt();

        // Convert weeks to days
        this.NumberDays = this.NumberWeeks * 7;
        this.DateEnd = DateStart.plusDays(this.NumberDays);

        arrayOfTime();
        return this.arrayOfTime();
    }

    public String[] getWeekTo() {
        sc.nextLine();
        inputEnd();
        System.out.println("Input a number of week you want to check: ");
        this.NumberWeeks = sc.nextInt();

        // Convert to days
        this.NumberDays = this.NumberWeeks * 7;
        this.DateStart = this.DateEnd.minusDays(this.NumberDays);
        arrayOfTime();
        return this.arrayOfTime();
    }

    public void inputStart() {
        System.out.println("Input start day By this format: M-D-YYY (Example: 1/1/2001) ");
        dayInput();
        this.DateStart = dateInput(this.newInput);
    }

    public void inputEnd() {
        System.out.println("Input end day By this format: M-D-YYY (Example: 1/1/2001) ");
        dayInput();
        this.DateEnd = dateInput(this.newInput);
    }

    // Return an Array of all day from Start Date to End Date of user's input
    public String[] arrayOfTime() {
        List<LocalDate> listOfDates = this.DateStart.datesUntil(this.DateEnd.plusDays(1)).collect(Collectors.toList());
        this.TimeRange = new String[listOfDates.size()];
        DateTimeFormatter format = DateTimeFormatter.ofPattern("M/d/yyyy");

        for (int i = 0; i < this.TimeRange.length; i++) {
            this.TimeRange[i] = (listOfDates.get(i)).format(format);
        }
        return this.TimeRange;
    }
    // function to method 1: get Pair
    public String[] getPair() {
        inputStart();
        inputEnd();
        arrayOfTime();
        System.out.println("The data you check will start from the date " +DateStart+ " to the date " + DateEnd);
        return this.arrayOfTime();
    }
    // function to method 2: get Days from
    public String[] getDaysFrom() {
        inputStart();
        System.out.println("Enter the number of days from start date (Start Date inclusive): ");
        this.NumberDays = sc.nextInt();

        // Count
        this.DateEnd = DateStart.plusDays(this.NumberDays);
        arrayOfTime();

        System.out.println("The data you check will start from the date " +DateStart+ " to the date " + DateEnd);
        return this.arrayOfTime();
    }
    // function to method 3: get Days to
    public String[] getDaysTo() {
        sc.nextLine();
        inputEnd();
        System.out.println("Enter the number of days before end date (End Date inclusive): ");
        this.NumberDays = sc.nextInt();

        // Count
        this.DateStart = this.DateEnd.minusDays(this.NumberDays);
        arrayOfTime();

        System.out.println("The data you check will start from the date " +DateStart+ " to the date " + DateEnd);
        return this.arrayOfTime();
    }
    // check if the day input is valid Local date or not
    public void dayInput() {
        String s = sc.nextLine();
        if (s.contains("/")) {
            this.splitDay = s.split("/");
            int month = Integer.parseInt(this.splitDay[0]);
            int day = Integer.parseInt(this.splitDay[1]);
            int year = Integer.parseInt(this.splitDay[2]);
    // check if valid month
            if (month < 1 || month > 12) {
                System.out.println("Month input is invalid");
                dayInput();
            }
    //  Check if leap day or not
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
                        dayInput();
                    }
                }
            }
    // check if valid day
            if (day < 1 || day > 31) {
                System.out.println("Day input is invalid");
                dayInput();
            }

            if (year != 2020 && year != 2021) {
                System.out.println("Year input is invalid! (The data is from 2020 to 2021)");
                dayInput();
            }

            for (int i = 0; i < splitDay.length; i++) {
                this.newInput = s;
            }
        } else {
            this.splitDay = s.split("-");
            int month = Integer.parseInt(this.splitDay[0]);
            int day = Integer.parseInt(this.splitDay[1]);
            int year = Integer.parseInt(this.splitDay[2]);

            if (month < 1 || month > 12) {
                System.out.println("Month input is invalid");
                dayInput();
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
                        dayInput();
                    }
                }
            }

            if (day < 1 || day > 31) {
                System.out.println("Day input is invalid! ");
                dayInput();
            }

            if (year != 2020 && year != 2021) {
                System.out.println("Year input is invalid! (The data is from 2020 to 2021)");
                dayInput();
            }

            for (int i = 0; i < splitDay.length; i++) {
                this.newInput = String.join("/", splitDay[0], splitDay[1], splitDay[2]);
            }
        }
    }
}
