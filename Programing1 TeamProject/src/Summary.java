import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Summary {
    // Attribute
    int NumberOfGroup, InputDays;
    String path = "src/data.csv";
    String line = "";
    String MetricChoice = "";
    String TypeOfResult = "";
    String option = "";
    Scanner sc = new Scanner(System.in);
    // Fetched and Filtered by Area
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> ByArea = new ArrayList<>();

    // Filtered by Area and Time from Beginning to Start Date
    ArrayList<String> FromBeginToStartDate = new ArrayList<>();
    ArrayList<String> AreaMetricBeforeStartDate = new ArrayList<>();
    ArrayList<String> TimeRangeBeforeStartDate = new ArrayList<>();

    // Metric and Time Range User Input
    ArrayList<String> RangeTime = new ArrayList<>();
    ArrayList<String> UserMetric = new ArrayList<>();
    ArrayList<String> UserTimeRange = new ArrayList<>();

    // Group of metric data and time data
    List<List<String>> dataList = new ArrayList<>();
    List<List<String>> dataTime = new ArrayList<>();

    // Result of newtotal type and upto
    ArrayList<Integer> NewTotal = new ArrayList<>();
    ArrayList<Integer> UpTo = new ArrayList<>();

    // Convert result to string to display (Result and Time)
    ArrayList<String> ResultInString = new ArrayList<>();
    ArrayList<String> TimeToString = new ArrayList<>();

    // Create a User
    Data user = new Data();

    // Constructor
    public Summary() {
    }

    // Main function for summary
    public void sumData() throws Exception, FileNotFoundException {
        // User input data by area and time range
        user.inputArea();
        user.timeRangeType();

        // fetch data and put in list
        fetchData();

        // ask users process data to get result
        GroupData();

        // convert result from int to string
        processMetricAndTime();
    }

    // Get and filter data from csv
    public void fetchData() throws IOException {
        // fetch data from csv
        BufferedReader br = new BufferedReader(new FileReader(path));
        while ((line = br.readLine()) != null) {
            this.list.add(line.toLowerCase(Locale.ROOT));
        }

        // filter data by area user inputs
        // input by country
        if (user.chooseArea.equals("1")) {
            for (String e : this.list) {
                if (isContained(e, user.Area)) {
                    this.ByArea.add(e);
                }
            }
        }
        // input by continent
        else {
            for (String e : this.list) {
                // check all the continent area in the data
                if (isContained(e, user.Area) && ((isContained(e, "owid_asi")) ||

                        (isContained(e, "owid_eur")) ||

                        (isContained(e, "owid_afr")) ||

                        (isContained(e, "owid_oce")) ||

                        (isContained(e, "owid_nam")) ||

                        (isContained(e, "owid_eun")) ||

                        (isContained(e, "owid_sam")) ||

                        (isContained(e, "owid_wrl")))) {
                    this.ByArea.add(e);
                }
            }
        }

        // filter data from beginning of the area to start date user inputs
        for (String e : this.ByArea) {
            if (isContained(e, user.TimeRange[0]) != true) {
                this.FromBeginToStartDate.add(e);
            } else {
                break;
            }
        }

        // get data by area and time by user inputs
        for (String e : this.ByArea) {
            for (int i = 0; i < user.TimeRange.length; i++) {
                if (isContained(e, user.TimeRange[i]) == true) {
                    this.RangeTime.add(e);
                }
            }
        }
        br.close();
    }
    // function nogrouping
    public void NoGrouping() {
        this.dataList = createListofData(this.dataList, RangeTime.size());
        this.dataTime = createListofData(this.dataTime, RangeTime.size());
        this.NumberOfGroup = RangeTime.size();
        for (int i = 0; i < RangeTime.size(); i++) {
            for (int j = 1; j <= 1; j++) {
                (this.dataList.get(i)).add(this.UserMetric.get((j - 1) + i));
                (this.dataTime.get(i)).add(this.UserTimeRange.get((j - 1) + i));
            }
        }
    }
    // function group by input the number of group
    public void NumberOfGroups() {
        int qty_days = RangeTime.size() / this.NumberOfGroup;
        int checkdays = RangeTime.size() % this.NumberOfGroup;
        if (checkdays <= RangeTime.size()) {
            this.dataList = createListofData(this.dataList, this.NumberOfGroup);
            this.dataTime = createListofData(this.dataTime, this.NumberOfGroup);
            int count = 0;
            int pos_datalist = checkdays;
            for (int i = 0; i < this.NumberOfGroup; i++) {
                int value = i * qty_days;
                if (i == this.NumberOfGroup - pos_datalist) {
                    for (int j = 1; j <= qty_days + 1; j++) {
                        (this.dataList.get(i)).add(this.UserMetric.get((j - 1) + value + count));
                        (this.dataTime.get(i)).add(this.UserTimeRange.get((j - 1) + value + count));
                    }
                    count++;
                    pos_datalist--;
                } else {
                    for (int j = 1; j <= qty_days; j++) {
                        (this.dataList.get(i)).add(this.UserMetric.get((j - 1) + value));
                        (this.dataTime.get(i)).add(this.UserTimeRange.get((j - 1) + value + count));
                    }
                }
            }
        }
    }
    // function grouping by input numbers of day
    public void NumberOfDays() {
        // Number of groups in case % == 0
        this.NumberOfGroup = RangeTime.size() / this.InputDays;
        // Check
        this.dataList = createListofData(this.dataList, this.NumberOfGroup);
        this.dataTime = createListofData(this.dataTime, this.NumberOfGroup);

        int flag = 0;
        for (int i = 0; i < this.dataList.size(); i++) {
            for (int j = 0; j < this.InputDays; j++) {
                (this.dataList.get(i)).add(this.UserMetric.get(flag));
                (this.dataTime.get(i)).add(this.UserTimeRange.get(flag));
                flag++;
            }
        }
    }

    // User Choose how to group data
    public void GroupData() {
        System.out.println("""
                Choose the type to grouping your data to:
                1. No Grouping
                2. Number of Groups
                3. Number of Days in a group""");
        this.option = sc.nextLine();
        boolean check = true;
        switch (option) {
            case "1":
                // No grouping Option
                InputforMetric(option);
                break;
            case "2":
                // Number of groups Option
                System.out.println("Choose number of group: ");
                this.NumberOfGroup = sc.nextInt();
                // Input again if user input is invalid
                while (check) {
                    if ((this.NumberOfGroup >= 2 && this.NumberOfGroup < 80)
                            && (this.NumberOfGroup <= this.RangeTime.size())) {
                        check = false;
                    } else {
                        annInvalid();
                        System.out.println("Choose number of group again: ");
                        this.NumberOfGroup = sc.nextInt();
                        check = true;
                    }
                }
                sc.nextLine();
                InputforMetric(option);
                break;
            case "3":
                // Number of days in a group
                System.out.println("Enter your number of days: ");
                this.InputDays = sc.nextInt();
                // Input again if user input is invalid
                while (check) {
                    if (checkDivisible(this.InputDays) != true) {
                        annInvalid();
                        System.out.println("Enter your number of days again: ");
                        this.InputDays = sc.nextInt();
                    } else {
                        check = false;
                    }
                }
                sc.nextLine();
                InputforMetric(option);
                break;
            default:
                // Input again if user input is invalid
                annInvalid();
                GroupData();
        }
    }

    // Let Users choose user metric
    public void InputforMetric(String num) {
        System.out.println("""
                Select the information you want to show on the screen:
                1. New Cases
                2. New Deaths
                3. People Vaccinated""");
        String option = sc.nextLine();
        switch (option) {
            case "1":
                // Metric is new covid cases
                this.MetricChoice = "cases";
                RunForMetric(num);
                break;
            case "2":
                // Metric is people deaths
                this.MetricChoice = "deaths";
                RunForMetric(num);
                break;
            case "3":
                // Metric is people who got vaccinated
                this.MetricChoice = "vaccinations";
                RunForMetric(num);
                break;
            default:
                // Input again because user input is invalid
                annInvalid();
                InputforMetric(num);
                break;
        }
    }

    // runMetric function will proccess to get exact metric data and then group
    // users data choices
    // choose result type
    public void RunForMetric(String num) {
        // Get Metric Data from beginning of the area to start date in order to
        // calculate the upTo function
        GetMetricDataNTimeRange(this.FromBeginToStartDate, this.AreaMetricBeforeStartDate,
                this.TimeRangeBeforeStartDate);
        // Get Metric Data and Time Range Data as user choice
        GetMetricDataNTimeRange(this.RangeTime, this.UserMetric, this.UserTimeRange);
        switch (num) {
            case "1":
                // Run no grouping and run function to choose result type to calculate
                NoGrouping();
                chooseResultType();
                break;
            case "2":
                // Run number of groups grouping and run function to choose result type to
                // calculate
                NumberOfGroups();
                chooseResultType();
                break;
            case "3":
                // Run number of days in a group grouping and run function to choose result type
                // to calculate
                NumberOfDays();
                chooseResultType();
                break;
            default:
                // Input value is invalid, input again announcement
                annInvalid();
                RunForMetric(num);
        }
    }

    // User Chooses result type function
    public void chooseResultType() {
        System.out.println("""
                Select the the result type:
                1. New Total
                2. Up To""");
        int option = sc.nextInt();
        switch (option) {
            case 1:
                // Run function newTotal and get the resultType String to use later in display
                this.TypeOfResult = "newtotal";
                NewofTotal();
                break;
            case 2:
                // Run function upTo and get the resultType String to use later in display
                this.TypeOfResult = "upto";
                UpTo();
                break;
            default:
                // Input is invalid, announce and ask users to input again
                annInvalid();
                chooseResultType();
        }
    }

    public ArrayList<Integer> NewofTotal() {
        ArrayList<Integer> groupMax = new ArrayList<>();
        ArrayList<Integer> maxBeforeStartDate = new ArrayList<>();
        // Go through each group in a dataList to calculate data
        if (this.MetricChoice != "vaccinations") {
            for (int i = 0; i < this.dataList.size(); i++) {
                int sum = 0;

                // Only calculate new covid cases and deaths
                for (String str : this.dataList.get(i)) {
                    if (str == "") {
                        str = "0";
                    }
                    int num = Integer.parseInt(str); // Convert to Int in order to sum up
                    sum += num;
                }

                this.NewTotal.add(sum);
            }
        } else {
            ArrayList<Integer> maxEachGroup = new ArrayList<>();
            for (String str : this.AreaMetricBeforeStartDate) {
                if (str == "") {
                    str = "0";
                }
                int num = Integer.parseInt(str);
                maxBeforeStartDate.add(num);
            }
            int maxfirst = Collections.max(maxBeforeStartDate);

            for (int i = 0; i < this.dataList.size(); i++) {
                int maxBigger = 0;
                for (String str : this.dataList.get(i)) {
                    if (str == "") {
                        str = "0";
                    }
                    int num = Integer.parseInt(str); // Convert to Int in order to sum up
                    groupMax.add(num);
                }
                maxBigger = Collections.max(groupMax);
                maxEachGroup.add(maxBigger);
            }
            for (int i = 0; i < maxEachGroup.size(); i++) {
                int sum = 0;
                if (i == 0) {
                    sum = maxEachGroup.get(i) - maxfirst;
                } else {
                    sum = maxEachGroup.get(i) - maxEachGroup.get(i - 1);
                }
                this.NewTotal.add(sum);
            }
        }
        return this.NewTotal;
    }

    public ArrayList<Integer> UpTo() {
        int sumBeforeRange = 0;
        int sumPerGroup = 0;
        ArrayList<Integer> listMetricInt = new ArrayList<>();

        // Calculate all the value each group then plus with the sumBefore Range to get
        // upTo of each group in list
        for (int i = 0; i < this.dataList.size(); i++) {
            // Only calculate new covid cases and deaths
            if (this.MetricChoice != "vaccinations") {
                // Calculate all the value from beginning upto start date for covid new cases
                // and deaths
                for (String str : this.AreaMetricBeforeStartDate) {
                    if (str == "") {
                        str = "0";
                    }
                    int num = Integer.parseInt(str);
                    sumBeforeRange += num;
                }
                for (String str : this.dataList.get(i)) {
                    if (str == "") {
                        str = "0";
                    }
                    int num = Integer.parseInt(str);
                    sumPerGroup += num;
                }
                this.UpTo.add(sumBeforeRange + sumPerGroup);
            } else {
                // Only calculate people got vaccinations
                // Calculate all the value from beginning upto start date for vaccinations
                for (String str : this.AreaMetricBeforeStartDate) {
                    if (str == "") {
                        str = "0";
                    }
                    int num = Integer.parseInt(str);
                    listMetricInt.add(num);
                }
                for (String str : this.dataList.get(i)) {
                    if (str == "") {
                        str = "0";
                    }
                    int num = Integer.parseInt(str);
                    listMetricInt.add(num);
                }
                this.UpTo.add(Collections.max(listMetricInt));
            }
        }

        return this.UpTo;
    }

    public void GetMetricDataNTimeRange(ArrayList<String> list, ArrayList<String> metric, ArrayList<String> rangetime) {
        // convert list to array
        String[] value;
        String[] userListConvert = new String[list.size()];
        userListConvert = list.toArray(userListConvert);

        // get each index
        for (String s : userListConvert) {
            value = s.split(",");
            switch (this.MetricChoice) {
                case "cases":
                    metric.add(value[4]);
                    rangetime.add(value[3]);
                    break;
                case "deaths":
                    metric.add(value[5]);
                    rangetime.add(value[3]);
                    break;
                case "vaccinations":
                    metric.add(value[6]);
                    rangetime.add(value[3]);
                    break;
            }
        }
    }

    // Function supports to create DataList (List contains groups)
    public List<List<String>> createListofData(List<List<String>> list, int num_of_group) {
        list = new ArrayList<>();
        for (int i = 1; i <= num_of_group; i++) {
            List<String> eachGroupList = new ArrayList<>();
            list.add(eachGroupList);
        }
        return list;
    }

    public boolean isContained(String s, String str) {
        String check = "\\b" + str + "\\b";
        Pattern pattern = Pattern.compile(check);
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }

    public boolean checkDivisible(int num) {
        boolean result = true;
        if (RangeTime.size() % num != 0) {
            result = false;
            return result;
        }
        return result;
    }

    // 3 functions below works together to convert result and time to string to
    // display
    public void convertResultToString() {
        if (this.TypeOfResult == "newtotal") {
            for (int num : this.NewTotal) {
                String s = String.valueOf(num);
                this.ResultInString.add(s);
            }
        } else {
            for (int num : this.UpTo) {
                String s = String.valueOf(num);
                this.ResultInString.add(s);
            }
        }
    }

    public void convertGroupOfTimeToArray() {
        String groupFirstDate = "";
        String groupLastDate = "";
        if (this.option.equals("1")) {
            for (int i = 0; i < this.dataTime.size(); i++) {
                groupFirstDate = this.dataTime.get(i).get(0);
                this.TimeToString.add(groupFirstDate);
            }
        } else {
            if (this.NumberOfGroup == RangeTime.size()) {
                for (int i = 0; i < this.dataTime.size(); i++) {
                    groupFirstDate = this.dataTime.get(i).get(0);
                    this.TimeToString.add(groupFirstDate);
                }
            }
            for (int i = 0; i < this.dataTime.size(); i++) {
                groupFirstDate = this.dataTime.get(i).get(0);
                groupLastDate = this.dataTime.get(i).get(this.dataTime.get(i).size() - 1);
                this.TimeToString.add(groupFirstDate + " " + groupLastDate);
            }
        }
    }

    // Convert both time and result to string
    public void processMetricAndTime() {
        convertResultToString();
        convertGroupOfTimeToArray();
    }

    // Announcement invalid function
    public void annInvalid() {
        System.out.println("INVALID. Please, input again!");
    }
}
