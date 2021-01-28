package UltimateGrapher;

import java.util.Scanner;

public class MatrixStorage {

    //Note: Least Square formula takes the form of A^TAx=A^Tb;
    //(0, 1), (1, 2), (2, 2), (3, 3), (4, 8), (5, 12)
    //(9, 5), (4, 6), (2, 7), (5, 7), (6, 3), (10, 5)

    private static int numOfCoord; //the numbers of coordinates inputted by the user
    private Scanner scan;
    private Coordinates[] coords; //Coordinate array (intermediate transition step that isn't really important now)

    private double[][] matrixA; //Matrix A from the formula
    private double[][] matrixB; //Matrix b from the formula

    public static int nValue; //represents the column count of the matrix
    public static int mValue; //represents the row count of the matrix

    private int threshold; // max error value that the program use to determine whether a rerun is needed
    private double maxError; // max error value that the user input

    /**
     * (Matrix Storage constructor)
     * Initialize all field objects
     */
    public MatrixStorage() throws IncorrectInputException {
        threshold = 3;

        scan = new Scanner(System.in);
        String input = userInput();
        mValue = getNumOfCoords(); //sets up the row value for the matrix
        nValue = threshold + 1; //sets up the column value for the matrix

        this.coords = convertToCoord(input);
        inputCheck(coords);
        sortCoordinates(coords);
        this.matrixA = convertToMatrix(coords);
    }

    /**
     * (Matrix Storage Constructor for reruns only)
     * I'll explain this to you in person
     */
    public MatrixStorage(Coordinates[] coordParam, int thresholdParam, double maxErrorParam){
        mValue = getNumOfCoords(); //sets up the row value for the matrix
        nValue = thresholdParam; //sets up the column value for the matrix
        threshold = thresholdParam;
        maxError = maxErrorParam;
        coords = coordParam;

        this.matrixA = convertToMatrix(coordParam);
    }

    /**
     * (User Input method)
     * Scans user input
     */
    private String userInput() {
        System.out.println("Remainder: Correct input takes the form of (9, 5), (4, 6), (2, 7), (5, 7)");
        System.out.println("Please input the coordinates below: ");
        String input = scan.nextLine();

        //allows the user to define the max error of the function in relation to the coordinates
        System.out.println("Please input the maximum accepted error below: ");
        try{
            maxError = scan.nextDouble();
        }
        catch(NumberFormatException ex){
            throw new RuntimeException("Please input a number for max error!");
        }

        // (auto input correction)
        //checks to see if the max error is correct
        if(maxError < 0){
            throw new RuntimeException("Max error value needs to be > 0!");
        }

        //removes the space(s) at the very start that causes potential exceptions
        StringBuilder augInput = new StringBuilder();
        System.out.println("Auto-correction step running...");

        int emptySpace = 0;
        for(int start = 0; start < input.length(); start++){
            if(input.charAt(start) == ' '){
                emptySpace++;
            }
            else if(input.charAt(start) != ' '){
                augInput.append(input.trim());

                System.out.println("Auto-removed " + emptySpace + " front empty spaces");
                break;
            }
        }

        // 1.add a space behind a comma if there doesn't exit one already
        // 2.remove a space before a comma if there exist one
        int insertSpace = 0;
        for(int b = 0; b < augInput.toString().length() - 1; b++){
            if(augInput.charAt(b) == ',' && augInput.charAt(b + 1) != ' '){
                augInput.insert(b + 1, ' ');
                insertSpace++;
            }
            else if(augInput.charAt(b) == ' ' && augInput.charAt(b + 1) == ','){
                augInput.deleteCharAt(b);
                b--;
            }
        }

        //last fail safe to ensure that there are no extra spaces
        for(int c = 0; c < augInput.toString().length() - 1; c++){
            if(augInput.charAt(c) != ',' && augInput.charAt(c + 1) == ' '){
                augInput.deleteCharAt(c + 1);
                c--;
            }
        }

        System.out.println("Auto-inserted " + insertSpace + " in-between empty spaces");

        input = augInput.toString();
        System.out.println("Augmented input: " + input + "\n");
        //calculates the number of coordinates (tested)
        for (int i = 0; i < input.length(); i++) {
            if (input.substring(i, i + 1).equals(")")) {
                numOfCoord++;
            }
        }
        return input;
    }

    /**
     * (Convert to Coordinate array)
     * Converts string input to an array of type coordinates
     *
     * Inputs strings take the form of:
     * (1, 2), (2, 3), (5, 9)
     */
    private Coordinates[] convertToCoord(String input) throws IncorrectInputException {
        int currPos = 0;
        double xCoord;
        double yCoord;
        Coordinates[] coords = new Coordinates[numOfCoord];
        String[] coorArrOne = input.split("\\), ");
        String[] coorArrTwo = new String[2]; //contains only x and y

        try {
            for (int i = 0; i < coorArrOne.length; i++) {
                coorArrOne[i] = coorArrOne[i].substring(1); //removes the front (
                coorArrOne[i] = coorArrOne[i].replaceAll("\\)", ""); //if int digit >= 2, remove )
                coorArrTwo = coorArrOne[i].split(", ");

                xCoord = Double.parseDouble(coorArrTwo[0]);
                yCoord = Double.parseDouble(coorArrTwo[1]);
                Coordinates newCoord = new Coordinates(xCoord, yCoord);
                coords[currPos] = newCoord;
                currPos++;
            }
        }
        catch(Exception ex){
            if(ex != null){
                throw new IncorrectInputException();
            }
        }
        return coords;
    }

    /**
     * (Convert to Coordinate Matrix)
     * Converts an array of type coordinates to a matrix
     */
    private double[][] convertToMatrix(Coordinates[] coordParam){
        double[][] coordMatrix = new double[mValue][nValue - 1];
        matrixB = new double[mValue][1];

        for(int m = 0; m < mValue; m++){
            for(int n = 0; n < nValue; n++){
                if(n < nValue - 1) {
                    double x = coordParam[m].getX();
                    double xNext = Math.pow(x, n);
                    coordMatrix[m][n] = xNext;
                }
                else if(n == nValue - 1){
                    matrixB[m][0] = coordParam[m].getY();
                }
            }
        }
        return coordMatrix;
    }

    /**
     * (Checks if user input is valid)
     * checks to make sure that coordinates inputted by the user can be used for later calculations
     */
    private void inputCheck(Coordinates[] coordArr) throws IncorrectInputException {
        System.out.println("Input Check Activated...");
        boolean gate = false;

        // ensure the input has more than one coordinates
        if(coordArr.length <= 1){
            System.out.println("Error: You can't plot a function with only one coordinate!");
            throw new IncorrectInputException();
        }

        // ensure input coordinates can form a function
        for(int i = 0; i < coordArr.length - 1; i++) {
            if (coordArr[i].getX() == coordArr[i + 1].getX() && coordArr[i].getY() != coordArr[i + 1].getY()) {
                System.out.println("Error: Coordinates must be able to form a function!");
                throw new IncorrectInputException();
            }
            else if(coordArr[i].equals(coordArr[i + 1])){
                coords = removeCoord(coordArr, i + 1);
                mValue--;
                gate = true;
            }
        }
        if(gate){
            System.out.println("Duplicate coordinate has been identified and removed\n");
        }
        else{
            System.out.println("Input correction scan complete. No errors found.\n");
        }
    }

    /**
     * (removes a specific coordinate from the coordinate array)
     */
    private Coordinates[] removeCoord(Coordinates[] arr, int index){
        if (arr == null || index < 0 || index >= arr.length) {
            return arr;
        }
        Coordinates[] anotherArray = new Coordinates[arr.length - 1];
        for (int i = 0, k = 0; i < arr.length; i++) {
            if (i == index) {
                continue;
            }
            anotherArray[k++] = arr[i];
        }
        return anotherArray;
    }

    /**
     * (Sorting coordinates with the x coordinate)
     */
    private void sortCoordinates(Coordinates[] coordsParam) {
        if(coordsParam == null){
            throw new RuntimeException("Paramter for the sorting method is null");
        }
        int n = coordsParam.length;
        for (int j = 1; j < n; j++) {
            Coordinates key = coordsParam[j];
            int i = j - 1;
            while ((i > -1) && (coordsParam[i].getX() > key.getX())) {
                coordsParam[i + 1] = coordsParam[i];
                i--;
            }
            coordsParam[i + 1] = key;
        }
        //sets the upper and lower bound after sorting all coordinates
        FunctionCalc.setUpperBounds(coordsParam[coordsParam.length - 1].getX());
        FunctionCalc.setLowerBounds(coordsParam[0].getX());

        coords = coordsParam;
    }


    /**
     * (Get Number of Coordinates)
     */
    public int getNumOfCoords(){
        return numOfCoord;
    }

    /**
     * (Get the Coordinates array)
     */
    public Coordinates[] getCoordsArray(){
        return coords;
    }

    /**
     * (Get Matrix A)
     */
    public double[][] getMatrixA(){
        return matrixA;
    }

    /**
     * (Get Matrix B)
     */
    public double[][] getMatrixB(){
        return matrixB;
    }

    /**
     * (Get Threshold Value)
     */
    public int getThreshold(){
        return threshold;
    }

    /**
     * (Get Max Allowed Error Value)
     */
    public double getMaxError(){
        return maxError;
    }

} //end of class
