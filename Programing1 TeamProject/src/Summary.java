import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Summary {
	// Attribute
	int ValueOfGroup, InsertDays;
	String csv = "src/data.csv";
	String ChoiceMetric = "";
	String TypeOfResult = "";
	String line = "";
	String option = "";
	Scanner sc = new Scanner(System.in);
	/*
	 * Because processing and manipulating the csv file and other related data is
	 * mandatory but we've encountered so many adversities about that. To resolve
	 * this issue we introduce the ArrayList Driven Approach in order to handle data
	 * more easily
	 */

	// Filtered by Area and Time from Beginning to Start Date
	ArrayList<String> ListofDataBeforeUserCheck = new ArrayList<>();
	ArrayList<String> AreaMetricBeforeUserCheck = new ArrayList<>();
	ArrayList<String> TimeRangeBeforeUserCheck = new ArrayList<>();

	// Fetched and Filtered by Area
	ArrayList<String> listofArray = new ArrayList<>();
	ArrayList<String> ByAreaArray = new ArrayList<>();

	// Group of metric data and time data
	List<List<String>> dataCheckList = new ArrayList<>();
	List<List<String>> dataCheckTime = new ArrayList<>();

	// Metric and Time Range User Input
	ArrayList<String> TimeRange = new ArrayList<>();
	ArrayList<String> ValueCheckMetric = new ArrayList<>();
	ArrayList<String> ValueCheckTimeRange = new ArrayList<>();
	
	// Convert result to string to display (Result and Time)
	ArrayList<String> OutcomeInString = new ArrayList<>();
	ArrayList<String> TimeToString = new ArrayList<>();

	// Result of newtotal type and upto
	ArrayList<Integer> NewTotal = new ArrayList<>();
	ArrayList<Integer> UpTo = new ArrayList<>();

	// Create a User
	Data user = new Data();

	// Constructor
	public Summary() {
	}

	// Main function for summary
	public void outlineData() throws Exception, FileNotFoundException {
		// User input data by area and time range
		user.InsertArea();
		user.timeFieldSet();

		// receives the list of fetched data
		getData();

		// allows users to process data in order to obtain result
		GroupOfData();

		// convert result from int to string
		processMetricAndTime();
	}

	// Get and filter data from csv
	public void getData() throws IOException {
		// fetch data from csv
		BufferedReader br = new BufferedReader(new FileReader(csv));
		while ((line = br.readLine()) != null) {
			this.listofArray.add(line.toLowerCase(Locale.ROOT));
		}

		// filter data by area user inputs
		// input by country
		if (user.PickArea.equals("1")) {
			for (String e : this.listofArray) {
				if (isCarried(e, user.Area)) {
					this.ByAreaArray.add(e);
				}
			}
		}
		// input by continent
		else {
			for (String e : this.listofArray) {
				// check all the continent area in the data
				if (isCarried(e, user.Area) && ((isCarried(e, "owid_asi")) ||

						(isCarried(e, "owid_eur")) ||

						(isCarried(e, "owid_oce")) ||

						(isCarried(e, "owid_afr")) ||

						(isCarried(e, "owid_nam")) ||

						(isCarried(e, "owid_eun")) ||

						(isCarried(e, "owid_sam")) ||

						(isCarried(e, "owid_wrl")))) {
					this.ByAreaArray.add(e);
				}
			}
		}

		// filter data from beginning of the area to start date user inputs
		for (String e : this.ByAreaArray) {
			if (isCarried(e, user.RangeOfTime[0]) == false) {
				this.ListofDataBeforeUserCheck.add(e);
			} else {
				break;
			}
		}

		// get data by area and time by user inputs
		for (String e : this.ByAreaArray) {
			for (int i = 0; i < user.RangeOfTime.length; i++) {
				if (isCarried(e, user.RangeOfTime[i]) != false) {
					this.TimeRange.add(e);
				}
			}
		}
		br.close();
	}

	// function Nongroup
	public void NonGroup() {
		this.dataCheckList = createData(this.dataCheckList, TimeRange.size());
		this.dataCheckTime = createData(this.dataCheckTime, TimeRange.size());
		this.ValueOfGroup = TimeRange.size();
		for (int i = 0; i < TimeRange.size(); i++) {
			for (int j = 1; j <= 1; j++) {
				(this.dataCheckList.get(i)).add(this.ValueCheckMetric.get((j - 1) + i));
				(this.dataCheckTime.get(i)).add(this.ValueCheckTimeRange.get((j - 1) + i));
			}
		}
	}

	// function grouping by input values of day
	public void ValueOfDays() {
		// Number of groups in case it is divisible
		this.ValueOfGroup = TimeRange.size() / this.InsertDays;
		// Check
		this.dataCheckList = createData(this.dataCheckList, this.ValueOfGroup);
		this.dataCheckTime = createData(this.dataCheckTime, this.ValueOfGroup);

		int flag = 0;
		for (int i = 0; i < this.dataCheckList.size(); i++) {
			for (int j = 0; j < this.InsertDays; j++) {
				(this.dataCheckList.get(i)).add(this.ValueCheckMetric.get(flag));
				(this.dataCheckTime.get(i)).add(this.ValueCheckTimeRange.get(flag));
				flag++;
			}
		}
	}

	// function group by input the number of group
	public void ValueOfGroups() {
		int qty_days = TimeRange.size() / this.ValueOfGroup;
		int checkdays = TimeRange.size() % this.ValueOfGroup;
		if (checkdays <= TimeRange.size()) {
			this.dataCheckList = createData(this.dataCheckList, this.ValueOfGroup);
			this.dataCheckTime = createData(this.dataCheckTime, this.ValueOfGroup);
			int count = 0;
			int pos_datalist = checkdays;
			for (int i = 0; i < this.ValueOfGroup; i++) {
				int value = i * qty_days;
				if (i == this.ValueOfGroup - pos_datalist) {
					for (int j = 1; j <= qty_days + 1; j++) {
						(this.dataCheckList.get(i)).add(this.ValueCheckMetric.get((j - 1) + value + count));
						(this.dataCheckTime.get(i)).add(this.ValueCheckTimeRange.get((j - 1) + value + count));
					}
					count++;
					pos_datalist--;
				} else {
					for (int j = 1; j <= qty_days; j++) {
						(this.dataCheckList.get(i)).add(this.ValueCheckMetric.get((j - 1) + value));
						(this.dataCheckTime.get(i)).add(this.ValueCheckTimeRange.get((j - 1) + value + count));
					}
				}
			}
		}
	}

	// runMetric function will proccess to get exact metric data and then group
	// users data choices
	// choose result type
	public void RunForMetric(String num) {
		// Get Metric Data from beginning of the area to start date in order to
		// calculate the upTo function
		GetMetricDataNTimeRange(this.ListofDataBeforeUserCheck, this.AreaMetricBeforeUserCheck,
				this.TimeRangeBeforeUserCheck);
		// Get Metric Data and Time Range Data as user choice
		GetMetricDataNTimeRange(this.TimeRange, this.ValueCheckMetric, this.ValueCheckTimeRange);
		switch (num) {
		case "1":
			// Run no grouping and run function to choose result type to calculate
			NonGroup();
			selectResult();
			break;
		case "2":
			// Run number of groups grouping and run function to choose result type to
			// calculate
			ValueOfGroups();
			selectResult();
			break;
		case "3":
			// Run number of days in a group grouping and run function to choose result type
			// to calculate
			ValueOfDays();
			selectResult();
			break;
		default:
			// Input value is invalid, input again announcement
			Invalid();
			RunForMetric(num);
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
			this.ChoiceMetric = "cases";
			RunForMetric(num);
			break;
		case "2":
			// Metric is people deaths
			this.ChoiceMetric = "deaths";
			RunForMetric(num);
			break;
		case "3":
			// Metric is people who got vaccinated
			this.ChoiceMetric = "vaccinations";
			RunForMetric(num);
			break;
		default:
			// Input again because user input is invalid
			Invalid();
			InputforMetric(num);
			break;
		}
	}

	// User Choose how to group data
	public void GroupOfData() {
		System.out.println("""
				Choose the type to grouping your data to:
				1. Non Group
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
			this.ValueOfGroup = sc.nextInt();
			// Input again if user input is invalid
			while (check) {
				if ((this.ValueOfGroup >= 2 && this.ValueOfGroup < 80)
						&& (this.ValueOfGroup <= this.TimeRange.size())) {
					check = false;
				} else {
					Invalid();
					System.out.println("Choose number of group again: ");
					this.ValueOfGroup = sc.nextInt();
					check = true;
				}
			}
			sc.nextLine();
			InputforMetric(option);
			break;
		case "3":
			// Number of days in a group
			System.out.println("Enter your number of days: ");
			this.InsertDays = sc.nextInt();
			// Input again if user input is invalid
			while (check) {
				if (IsDivisible(this.InsertDays) != true) {
					Invalid();
					System.out.println("Enter your number of days again: ");
					this.InsertDays = sc.nextInt();
				} else {
					check = false;
				}
			}
			sc.nextLine();
			InputforMetric(option);
			break;
		default:
			// Input again if user input is invalid
			Invalid();
			GroupOfData();
		}
	}

	// User Chooses result type function
	public void selectResult() {
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
			Invalid();
			selectResult();
		}
	}

	public ArrayList<Integer> UpTo() {
		int amountValueBeforeCheck = 0;
		int amountEveryInGroup = 0;
		ArrayList<Integer> MetricInInt = new ArrayList<>();

		// Calculate all the value of each group then plus with the sumBefore Range to
		// get
		// upTo of each group in list
		for (int i = 0; i < this.dataCheckList.size(); i++) {
			// Only calculate new covid cases and deaths
			if (this.ChoiceMetric != "vaccinations") {
				// Calculate all the value from beginning upto start date for covid new cases
				// and deaths
				for (String str : this.AreaMetricBeforeUserCheck) {
					if (str == "") {
						str = "0";
					}
					int num = Integer.parseInt(str);
					amountValueBeforeCheck += num;
				}
				for (String str : this.dataCheckList.get(i)) {
					if (str == "") {
						str = "0";
					}
					int num = Integer.parseInt(str);
					amountEveryInGroup += num;
				}
				this.UpTo.add(amountValueBeforeCheck + amountEveryInGroup);
			} else {
				// Only calculate people got vaccinations
				// Calculate all the value from beginning upto start date for vaccinations
				for (String str : this.AreaMetricBeforeUserCheck) {
					if (str == "") {
						str = "0";
					}
					int num = Integer.parseInt(str);
					MetricInInt.add(num);
				}
				for (String str : this.dataCheckList.get(i)) {
					if (str == "") {
						str = "0";
					}
					int num = Integer.parseInt(str);
					MetricInInt.add(num);
				}
				this.UpTo.add(Collections.max(MetricInInt));
			}
		}

		return this.UpTo;
	}

	public ArrayList<Integer> NewofTotal() {
		ArrayList<Integer> Max = new ArrayList<>();
		ArrayList<Integer> maxValueBeforeUserDataCheck = new ArrayList<>();
		// Go through each group in a dataList to calculate data
		if (this.ChoiceMetric != "vaccinations") {
			for (int i = 0; i < this.dataCheckList.size(); i++) {
				int sum = 0;

				// Only calculate new covid cases and deaths
				for (String str : this.dataCheckList.get(i)) {
					if (str == "") {
						str = "0";
					}
					int num = Integer.parseInt(str); // Convert to Int in order to sum up
					sum += num;
				}

				this.NewTotal.add(sum);
			}
		} else {
			ArrayList<Integer> maxeveryGroup = new ArrayList<>();
			for (String str : this.AreaMetricBeforeUserCheck) {
				if (str == "") {
					str = "0";
				}
				int num = Integer.parseInt(str);
				maxValueBeforeUserDataCheck.add(num);
			}
			int maxfirst = Collections.max(maxValueBeforeUserDataCheck);

			for (int i = 0; i < this.dataCheckList.size(); i++) {
				int maxBigger = 0;
				for (String str : this.dataCheckList.get(i)) {
					if (str == "") {
						str = "0";
					}
					int num = Integer.parseInt(str); // Convert to Int in order to sum up
					Max.add(num);
				}
				maxBigger = Collections.max(Max);
				maxeveryGroup.add(maxBigger);
			}
			for (int i = 0; i < maxeveryGroup.size(); i++) {
				int sum = 0;
				if (i == 0) {
					sum = maxeveryGroup.get(i) - maxfirst;
				} else {
					sum = maxeveryGroup.get(i) - maxeveryGroup.get(i - 1);
				}
				this.NewTotal.add(sum);
			}
		}
		return this.NewTotal;
	}

	public void GetMetricDataNTimeRange(ArrayList<String> list, ArrayList<String> metric, ArrayList<String> rangetime) {
		// convert list to array
		String[] value;
		String[] Convert = new String[list.size()];
		Convert = list.toArray(Convert);

		// get each index
		for (String s : Convert) {
			value = s.split(",");
			switch (this.ChoiceMetric) {
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
	public List<List<String>> createData(List<List<String>> list, int num_of_group) {
		list = new ArrayList<>();
		for (int i = 1; i <= num_of_group; i++) {
			List<String> EveryGroupInList = new ArrayList<>();
			list.add(EveryGroupInList);
		}
		return list;
	}

	public boolean isCarried(String s, String str) {
		Pattern P = Pattern.compile("\\b" + str + "\\b");
		Matcher found = P.matcher(s);
		return found.find();
	}

	// This function is originally the engine which has private modifier
	// but we may use those function in the future so we decided to make it public
	public boolean IsDivisible(int num) {
		return TimeRange.size() % num != 0 ? false : true; // Binary Operator
	}

	// 3 functions below are responsible for processing result and time to string
	// harmonically to
	// display
	private void convertnewTotalToString() { // This is the engine
		if (this.TypeOfResult == "newtotal") {
			for (int num : this.NewTotal) {
				String s = Integer.toString(num);
				this.OutcomeInString.add(s);
			}
		} else {
			for (int num : this.UpTo) {
				String s = Integer.toString(num);
				this.OutcomeInString.add(s);
			}
		}
	}

	private void convertTimeToArray() { // This is the engine
		String groupFirstDate;
		String groupLastDate;
		if (this.option.equals("1")) {
			for (int i = 0; i < this.dataCheckTime.size(); i++) {
				groupFirstDate = this.dataCheckTime.get(i).get(0);
				this.TimeToString.add(groupFirstDate);
			}
		} else {
			if (TimeRange.size() == this.ValueOfGroup) {
				for (int i = 0; i < this.dataCheckTime.size(); i++) {
					groupFirstDate = this.dataCheckTime.get(i).get(0);
					this.TimeToString.add(groupFirstDate);
				}
			}
			for (int i = 0; i < this.dataCheckTime.size(); i++) {
				groupFirstDate = this.dataCheckTime.get(i).get(0);
				groupLastDate = this.dataCheckTime.get(i).get(this.dataCheckTime.get(i).size() - 1);
				this.TimeToString.add(groupFirstDate + " " + groupLastDate);
			}
		}
	}

	// Convert both time and result to string
	public void processMetricAndTime() { // This is the Stand-alone function
		convertnewTotalToString();
		convertTimeToArray();
	}

	// To inform about the invalid case
	public void Invalid() {
		System.out.println("Cannot confirm the data. Could you check and input again!");
	}
}
