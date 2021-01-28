package UltimateGrapher;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SystemOperator extends MatrixOperation{
    /**
     * (Matrix Storage constructor)
     * Initialize all field objects
     */

    //(9, 5), (4, 6), (2, 7), (5, 7)
    public SystemOperator() throws IncorrectInputException {
    }

    public static void main(String[] args) throws IncorrectInputException {
        FunctionCalc fc = new FunctionCalc();
    }

    /**
     * Method responsible for printing matrices
     */
    public static void printMatrix(double M[][],
                                   int rowSize,
                                   int colSize)
    {
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++)
                System.out.print("(" + M[i][j] + ") ");

            System.out.println();
        }
    }
}
