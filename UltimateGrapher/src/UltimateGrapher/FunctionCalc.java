package UltimateGrapher;

import java.util.Scanner;

public class FunctionCalc{

    private MatrixStorage storage; //obj
    private MatrixOperation operation; //obj

    private static double upperBounds; //upper bound of the function
    private static double lowerBounds; //lower bound of the function

    /**
     * Constructor
     * @throws IncorrectInputException
     */
    public FunctionCalc() throws IncorrectInputException {
        storage = new MatrixStorage();
        operation = new MatrixOperation();
        Scanner scan = new Scanner(System.in);
        boolean gate = true;

        //reruns the program with incremented nValue for more accuracy
        while(storage.getMaxError() < errorCalc() && errorCalc() > 0.001){
            // asks if the user still wants to continue running the program if nValue > 10 (takes forever to calculate)
            if(storage.getThreshold() == 10 && gate){
                System.out.println("CALCULATION AUTO-STOPPED. There are no simple solutions to the given error value and coordinates. Try increasing the max allowed error value on the next run.");
                System.out.println("NOTE: If you want to the program to continue the evaluation for possible complex results (not recommended), enter \"yes\" below. Otherwise, enter \"no\".");
                String input = scan.nextLine();
                if(input.equalsIgnoreCase("yes")){
                    gate = false;
                    continue;
                }
                else if(input.equalsIgnoreCase("no")){
                    System.out.println("Program Terminated.");
                    System.out.println("Try increasing the max allowed error value on the next run.");
                    System.exit(0);
                }
                else{
                    System.out.println("You input is invalid. Program terminated.");
                    System.exit(0);
                }
            }
            storage = new MatrixStorage(storage.getCoordsArray(), storage.getThreshold() + 1, storage.getMaxError());
            System.out.println("Program re-running with incremented nValue of: " + storage.nValue);
            System.out.println("Error Value: " + errorCalc());
            if(storage.getMaxError() > errorCalc()){
                System.out.println("this ran");
                break;
            }
        }
        finalOutput(); // calls finalOutput in the constructor
    }

    /**
     * Method responsible for printing out the final output
     */
    private void finalOutput(){
        System.out.println("=========================================");
        System.out.println("FINAL EQUATION");
        double[][] temp = leastSquareMatrix();
        for(int i = 0; i < temp.length; i++){
            if(i == temp.length - 1){
                System.out.print("(" + temp[i][0] + "x^" + i + ") ");
                break;
            }
            System.out.print("(" + temp[i][0] + "x^" + i + ") + ");
        }
        System.out.println("= y");

        System.out.println("\nFUNCTION DOMAIN \n{" + lowerBounds + " <= x <= " + upperBounds + "}");
        System.out.println("\nERROR VALUE: " + errorCalc());
        System.out.println("=========================================");
    }

    /**
     * Method that calculates the least square matrix
     */
    private double[][] leastSquareMatrix(){
        double[][] tempMatrixA = storage.getMatrixA();
        double[][] tempMatrixB = storage.getMatrixB();

        operation.print(tempMatrixA);

        double[][] tempLeft = operation.multiplyByMat(operation.transpose(tempMatrixA), tempMatrixA);
        double[][] tempRight = operation.multiplyByMat(operation.transpose(tempMatrixA), tempMatrixB);
        double[][] result = operation.multiplyByMat(operation.inverse(tempLeft), tempRight);

        return result;
    }

    /**
     * Method that calculates the error value
     */
    private double errorCalc(){
        double[][] temp = operation.multiplyByMat(storage.getMatrixA(), leastSquareMatrix());

        double error = 0;
        double[][] tempMatrixB = storage.getMatrixB();
        for(int i = 0; i < tempMatrixB.length; i++){
            double errorTemp = (tempMatrixB[i][0] - temp[i][0]);
            error += Math.pow(errorTemp, 2);
        }
        return error;
    }

    /**
     * (setter for upperBounds)
     */
    public static void setUpperBounds(double upperValue){
        upperBounds = upperValue;
    }

    /**
     * (setter for lowerBounds)
     */
    public static void setLowerBounds(double lowerValue){
        lowerBounds = lowerValue;
    }

}
