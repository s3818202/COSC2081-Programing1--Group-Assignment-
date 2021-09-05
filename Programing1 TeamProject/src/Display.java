import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Stream;

public class Display {
    // Set up
    Summary suma = new Summary();
    Scanner scan = new Scanner(System.in);
    //
    public void Type() throws Exception {
        suma.outlineData();
        String option;
        System.out.println("""
                Choose the display type of your results you want to present to:
                1. By Table
                2. By Chart""");
        option = scan.nextLine();
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
        boolean rowLeft = true;
        int MaxWidthOfTable = 30;

        String[][] tabular = new String[(suma.OutcomeInString.size()) + 1][2];
        tabular[0][0] = "Range of Time";
        tabular[0][1] = "Value";

        for (int i = 1; i < tabular.length; i++) {
            tabular[i][0] = suma.TimeToString.get(i - 1);
        }

        for (int i = 1; i < tabular.length; i++) {
            tabular[i][1] = suma.OutcomeInString.get(i - 1);
        }

        if (suma.TimeToString.size() < 1 && suma.OutcomeInString.size() < 1) {
            System.out.println("The input not have information!");
        }

        ArrayList<String[]> table = new ArrayList<>(Arrays.asList(tabular));
        ArrayList<String[]> tableResult = new ArrayList<>();
        for (String[] row : table) {
            boolean extraRow = false;
            int split = 0;
            do {
                extraRow = false;
                String[] addRow = new String[row.length];
                for (int i = 0; i < row.length; i++) {
                    if (row[i].length() < MaxWidthOfTable) {
                        addRow[i] = split == 0 ? row[i] : "";
                    } else if ((row[i].length() > (split * MaxWidthOfTable))) {
                        int end = row[i].length() > ((split * MaxWidthOfTable) + MaxWidthOfTable)
                                ? (split * MaxWidthOfTable) + MaxWidthOfTable
                                : row[i].length();
                        addRow[i] = row[i].substring((split * MaxWidthOfTable), end);
                        extraRow = true;
                    } else {
                        addRow[i] = "";
                    }
                }
                tableResult.add(addRow);
                if (extraRow) {
                    split++;
                }
            } while (extraRow);
        }

        String[][] takeTable = new String[tableResult.size()][tableResult.get(0).length];
        for (int i = 0; i < takeTable.length; i++) {
            takeTable[i] = tableResult.get(i);
        }

        HashMap<Integer, Integer> columLength = new HashMap<>();
        Arrays.stream(takeTable)
                .forEach(thisTable -> Stream.iterate(0, (i -> i < thisTable.length), (i -> ++i)).forEach(i -> {
                    if (columLength.get(i) == null) {
                        columLength.put(i, 0);
                    }
                    if (columLength.get(i) < thisTable[i].length()) {
                        columLength.put(i, thisTable[i].length());
                    }
                }));

        final StringBuilder getStringFormatter = new StringBuilder("");

        String flag = rowLeft ? "-" : "";

        columLength.entrySet().stream().forEach(e -> getStringFormatter.append("| %" + flag + e.getValue() + "s "));

        getStringFormatter.append("|\n");

        String Horizonline = columLength.entrySet().stream().reduce("", (line, b) -> {
            String lineMaterial = "--";
            lineMaterial = lineMaterial + Stream.iterate(0, (i -> i < b.getValue()), (i -> ++i)).reduce("",
                    (line1, b1) -> line1 + "-", (a1, b1) -> a1 + b1);
            lineMaterial = lineMaterial + "-";
            return line + lineMaterial;
        }, (a, b) -> a + b);

        Horizonline = Horizonline + "\n";

        String horizonline = Horizonline;

        System.out.print(horizonline);

        Arrays.stream(takeTable).limit(1).forEach(temp -> System.out.printf(getStringFormatter.toString(), temp));

        System.out.print(horizonline);

        Stream.iterate(1, (i -> i < takeTable.length), (i -> ++i)).forEach(temp -> {
            System.out.printf(getStringFormatter.toString(), takeTable[temp]);
            System.out.print(horizonline);
        });
    }

    public void takeValuePosition(ArrayList<Integer> result, int rows, ArrayList<Integer> valueOfgroup) {
        int temp = Collections.min(result);
        double minimumDistance = (Collections.max(result) - Collections.min(result)) / 22.0;

        for (int k : result) {
            double newResultx2 = k;
            double minResult = temp;
            for (int i = rows - 2; i >= 0; i--) {
                minResult += minimumDistance;
                if (minResult > newResultx2) {
                    valueOfgroup.add(i);
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
        int YLength = labelY.length() + 1;

        ArrayList<Integer> GroupTime = new ArrayList<>();
        ArrayList<Integer> GroupValue = new ArrayList<>();
        String[][] chart = new String[rows][columns + YLength];

        // initialize an empty chart
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns + YLength; j++) {
                chart[i][j] = " ";
            }
        }

        int value = (columns - 1) / (suma.ValueOfGroup + 1);
        int colValue;
        for (int i = 1; i <= suma.ValueOfGroup; i++) {
            colValue = value * i;
            GroupTime.add(colValue);
        }

        if (suma.TypeOfResult == "Newtotal") {
            takeValuePosition(suma.NewTotal, rows, GroupValue);
        } else {
            takeValuePosition(suma.UpTo, rows, GroupValue);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = YLength - 1; j < columns + YLength; j++) {
                chart[i][j] = " ";
                if (i != rows - 1) {
                    for (int k = 0; k < GroupTime.size(); k++) {
                        chart[GroupValue.get(k)][GroupTime.get(k) + YLength] = "*" + suma.OutcomeInString.get(k) + " "
                                + "(" + suma.TimeToString.get(k) + ")";
                    }
                } else {
                    chart[rows - 1][YLength - 1] = "|";
                    break;
                }
                chart[i][YLength - 1] = "|";
                if (i == 12) {
                    chart[i][YLength - 1] = " ";
                }
                chart[rows - 1][j] = "_";
            }
        }

        // to add title to y-axis
        chart[rows / 2][0] = labelY + "|";

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns + YLength; j++) {
                System.out.print(chart[i][j]);
            }
            System.out.println();
        }
        System.out.printf("%70s", "Range\n\n");

        if (suma.TimeToString.size() < 1 && suma.OutcomeInString.size() < 1) {
            System.out.println("Your data input does not have information!");
        }
    }
}