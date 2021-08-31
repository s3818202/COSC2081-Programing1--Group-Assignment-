import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

public class Display {
    // Set up
    Summary su = new Summary();
    Scanner sc = new Scanner(System.in);
    //
    public void Type() throws Exception {
        su.sumData();
        String option;
        System.out.println("""
                Choose the display type of your results you want to present to:
                1. By Table
                2. By Chart""");
        option = sc.nextLine();
        switch (option) {
            case "1":
                System.out.println("The table of Data is: ");
                Tabular();
                break;
            case "2":
                System.out.println("The chart of Data is: ");
                Chart();
                break;
            default:
                System.out.println("Invalid input, Please try again");
                Type();
                break;
        }
    }
    // Present by table
    public void Tabular() throws Exception {
        boolean rowJustifyLeft = true;
        int tableMaxWidth = 30;

        String[][] tabular = new String[(su.ResultInString.size()) + 1][2];
        tabular[0][0] = "Range of Time";
        tabular[0][1] = "Value";

        for (int i = 1; i < tabular.length; i++) {
            tabular[i][0] = su.TimeToString.get(i - 1);
        }

        for (int i = 1; i < tabular.length; i++) {
            tabular[i][1] = su.ResultInString.get(i - 1);
        }

        if (su.TimeToString.size() < 1 && su.ResultInString.size() < 1) {
            System.out.println("The input not have information!");
        }

        ArrayList<String[]> table = new ArrayList<>(Arrays.asList(tabular));
        ArrayList<String[]> tableFinale = new ArrayList<>();
        for (String[] row : table) {
            boolean extraRow = false;
            int split = 0;
            do {
                extraRow = false;
                String[] addRow = new String[row.length];
                for (int i = 0; i < row.length; i++) {
                    if (row[i].length() < tableMaxWidth) {
                        addRow[i] = split == 0 ? row[i] : "";
                    } else if ((row[i].length() > (split * tableMaxWidth))) {
                        int end = row[i].length() > ((split * tableMaxWidth) + tableMaxWidth)
                                ? (split * tableMaxWidth) + tableMaxWidth
                                : row[i].length();
                        addRow[i] = row[i].substring((split * tableMaxWidth), end);
                        extraRow = true;
                    } else {
                        addRow[i] = "";
                    }
                }
                tableFinale.add(addRow);
                if (extraRow) {
                    split++;
                }
            } while (extraRow);
        }

        String[][] getTable = new String[tableFinale.size()][tableFinale.get(0).length];
        for (int i = 0; i < getTable.length; i++) {
            getTable[i] = tableFinale.get(i);
        }

        HashMap<Integer, Integer> colLength = new HashMap<>();
        Arrays.stream(getTable)
                .forEach(thisTable -> Stream.iterate(0, (i -> i < thisTable.length), (i -> ++i)).forEach(i -> {
                    if (colLength.get(i) == null) {
                        colLength.put(i, 0);
                    }
                    if (colLength.get(i) < thisTable[i].length()) {
                        colLength.put(i, thisTable[i].length());
                    }
                }));

        final StringBuilder getStringFormatter = new StringBuilder("");

        String flag = rowJustifyLeft ? "-" : "";

        colLength.entrySet().stream().forEach(e -> getStringFormatter.append("| %" + flag + e.getValue() + "s "));

        getStringFormatter.append("|\n");

        String getHoriz = colLength.entrySet().stream().reduce("", (line, b) -> {
            String lineMaterial = "--";
            lineMaterial = lineMaterial + Stream.iterate(0, (i -> i < b.getValue()), (i -> ++i)).reduce("",
                    (line1, b1) -> line1 + "-", (a1, b1) -> a1 + b1);
            lineMaterial = lineMaterial + "-";
            return line + lineMaterial;
        }, (a, b) -> a + b);

        getHoriz = getHoriz + "\n";

        String horizLine = getHoriz;

        System.out.print(horizLine);

        Arrays.stream(getTable).limit(1).forEach(temp -> System.out.printf(getStringFormatter.toString(), temp));

        System.out.print(horizLine);

        Stream.iterate(1, (i -> i < getTable.length), (i -> ++i)).forEach(temp -> {
            System.out.printf(getStringFormatter.toString(), getTable[temp]);
            System.out.print(horizLine);
        });
    }

    public void getValuePos(ArrayList<Integer> result, int rows, ArrayList<Integer> valuegroup) {
        int temp = Collections.min(result);
        double minDisBetweenTwoValue = (Collections.max(result) - Collections.min(result)) / 22.0;

        for (int k : result) {
            double newResultDouble = k;
            double minResult = temp;
            for (int i = rows - 2; i >= 0; i--) {
                minResult += minDisBetweenTwoValue;
                if (minResult > newResultDouble) {
                    valuegroup.add(i);
                    break;
                }
            }
        }
    }
    // Present by chart
    public void Chart() throws FileNotFoundException, Exception {
        int rows = 24;
        int columns = 80;
        String labelY = "Value ";
        int labelYLength = labelY.length() + 1;

        ArrayList<Integer> timegroup = new ArrayList<>();
        ArrayList<Integer> valuegroup = new ArrayList<>();
        String[][] chart = new String[rows][columns + labelYLength];

        // initialize an empty chart
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns + labelYLength; j++) {
                chart[i][j] = " ";
            }
        }

        int value = (columns - 1) / (su.NumberOfGroup + 1);
        int colValue;
        for (int i = 1; i <= su.NumberOfGroup; i++) {
            colValue = value * i;
            timegroup.add(colValue);
        }

        if (su.TypeOfResult == "newtotal") {
            getValuePos(su.NewTotal, rows, valuegroup);
        } else {
            getValuePos(su.UpTo, rows, valuegroup);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = labelYLength - 1; j < columns + labelYLength; j++) {
                chart[i][j] = " ";
                if (i != rows - 1) {
                    for (int k = 0; k < timegroup.size(); k++) {
                        // chart[valuegroup.get(k)][timegroup.get(k) + titleLength] = "*" +
                        // s.resultString.get(k);
                        chart[valuegroup.get(k)][timegroup.get(k) + labelYLength] = "*" + su.ResultInString.get(k) + " "
                                + "(" + su.TimeToString.get(k) + ")";
                    }
                } else {
                    chart[rows - 1][labelYLength - 1] = "|";
                    break;
                }
                chart[i][labelYLength - 1] = "|";
                if (i == 12) {
                    chart[i][labelYLength - 1] = " ";
                }
                chart[rows - 1][j] = "_";
            }
        }

        // to add title to y-axis
        chart[rows / 2][0] = labelY + "|";

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns + labelYLength; j++) {
                System.out.print(chart[i][j]);
            }
            System.out.println();
        }
        System.out.printf("%70s", "Range\n\n");

        if (su.TimeToString.size() < 1 && su.ResultInString.size() < 1) {
            System.out.println("Your data input does not have information!");
        }
    }
}