package UltimateGrapher;

import java.text.DecimalFormat;

public class MatrixOperation{

    public MatrixOperation() throws IncorrectInputException {
        super();
    }

    /**
     * Matrix-matrix multiplication (y = A * B)
     *
     * @param a the first input matrix
     * @param b the second input matrix
     * @return the resultant matrix y
     */
    public static double[][] multiplyByMat(double[][] a, double[][] b) {
        int m1 = a.length; // input matrix1 row number
        int n1 = a[0].length; // input matrix1 column number
        int m2 = b.length; // input matrix2 row number
        int n2 = b[0].length; // input matrix2 column number
        if (n1 != m2) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] y = new double[m1][n2];
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n2; j++)
                for (int k = 0; k < n1; k++)
                    y[i][j] += a[i][k] * b[k][j];
        return y;
    }


    /**
     * Matrix-vector multiplication (y = A * x)
     *
     * @param a the input matrix
     * @param x the input vector
     * @return the multiplication vector y
     */
    public static double[] multiplyMV(double[][] a, double[] x) {
        int m = a.length;
        int n = a[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += a[i][j] * x[j];
        return y;
    }

    /**
     * Matrix-number multiplication (y = A * x)
     *
     * @param a the input matrix
     * @param x the input number
     * @return the resultant matrix y
     */
    public static double[][] multiplybyNum(double[][] a, double x) {
        int m = a.length; // input matrix1 row number
        int n = a[0].length; // input matrix1 column number
        double[][] y = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i][j] += a[i][j] * x;
        return y;
    }


    /**
     * Matrix-matrix addition (y = A + B)
     *
     * @param a the first input matrix
     * @param b the second input matrix
     * @return the addition of two matrices
     */
    public static double[][] add(double[][] a, double[][] b) {
        int m1 = a.length; // input matrix1 row number
        int n1 = a[0].length; // input matrix1 column number
        int m2 = b.length; // input matrix2 row number
        int n2 = b[0].length; // input matrix2 column number
        double[][] y = new double[m1][n1];
        if (m1 != m2 || n1 != n2) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n1; j++)
                y[i][j] = a[i][j] + b[i][j];
        return y;
    }


    /**
     * Matrix-matrix subtraction (y = A - B)
     *
     * @param a the first input matrix
     * @param b the second input matrix
     * @return the subtraction of two matrices
     */
    public static double[][] subtract(double[][] a, double[][] b) {
        int m1 = a.length; // input matrix1 row number
        int n1 = a[0].length; // input matrix1 column number
        int m2 = b.length; // input matrix2 row number
        int n2 = b[0].length; // input matrix2 column number
        double[][] y = new double[m1][n1];
        if (m1 != m2 || n1 != n2) throw new RuntimeException("Illegal matrix dimensions.");
        for (int i = 0; i < m1; i++)
            for (int j = 0; j < n1; j++)
                y[i][j] = a[i][j] - b[i][j];
        return y;
    }

    /**
     * Inverse the matrix
     * <p>
     * Reference website:
     * https://www.mathsisfun.com/algebra/matrix-inverse-minors-cofactors-adjugate.html
     *
     * @param mat the input matrix
     * @return the inverse of the matrix
     */
    public static double[][] inverse(double[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        double determinant = findDeterminant(mat);
        if (m != n)
            throw new RuntimeException("Not a square matrix.");
        // Step 1: Calculate the "Matrix of Minors"
        double[][] matrixOfMinors = matrixOfMinors(mat);
        // Step 2: Convert into "Matrix of Cofactors"
        double[][] matrixOfCofactors = matrixOfCofactors(matrixOfMinors);
        // Step 3: Transpose all the elements inside
        double[][] matrixTranspose = transpose(matrixOfCofactors);
        // Step 4: Multiply by 1/Determinant
        double[][] result = multiplybyNum(matrixTranspose, 1 / determinant);
        return result;
    }

    /**
     * Find the determinant of a square matrix
     *
     * @param mat the input square matrix
     * @return the determinant of the matrix
     */
    public static double findDeterminant(double[][] mat) {
        int len = mat.length;
        double det = 0;
        if (mat.length != mat[0].length)
            throw new RuntimeException("Not a square matrix.");
        if (len == 2)
            return mat[0][0] * mat[1][1] - mat[1][0] * mat[0][1];
        else {
            for (int i = 0; i < len; i++) {
                double[][] subMatrix = smallerMatrix(mat, 0, i);
                det += mat[0][i] * findDeterminant(subMatrix) *
                        (double) Math.pow(-1, i);
            }
        }
        return det;
    }


    /**
     * Break down a matrix into one with a smaller dimension.
     * <p>
     * Input matrix:{{ 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 }}
     * m = 0, n = 1 (removing all elements in row 0 and column 1)
     * <p>
     * Output matrix: {{ 2.0, 3.0 }, { 8.0, 9.0 }}
     *
     * @param mat input matrix to break down
     * @param m   row index
     * @param n   columm index
     * @return the breakdown matrix
     */
    private static double[][] smallerMatrix(double[][] mat, int m, int n) {
        int len = mat.length;
        if (mat == null || m >= len || n >= len || m < 0 || n < 0) {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        return removeCol(removeRow(mat, m), n);
    }

    /**
     * Remove certain row of a matrix
     * <p>
     * Helper method for smallerMatrix
     *
     * @param mat       input matrix
     * @param rowRemove row index to remove
     * @return a new matrix with one less row
     */
    private static double[][] removeRow(double[][] mat, int rowRemove) {
        int row = mat.length;
        int col = mat[0].length;
        double[][] result = new double[row - 1][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                if (i != rowRemove)
                    result[i > rowRemove ? i - 1 : i][j] = mat[i][j];
        return result;
    }

    /**
     * Remove certain column of a matrix
     * <p>
     * Helper method for smallerMatrix
     *
     * @param mat       input matrix
     * @param colRemove column index to remove
     * @return a new matrix with one less column
     */
    private static double[][] removeCol(double[][] mat, int colRemove) {
        int row = mat.length;
        int col = mat[0].length;
        double[][] result = new double[row][col - 1];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                if (j != colRemove)
                    result[i][j > colRemove ? j - 1 : j] = mat[i][j];
        return result;
    }

    /**
     * Calculate the "Matrix of Minors"
     * Get the determinant of each element and replace each of them
     *
     * @param mat input matrix
     * @return Matrix of Minors
     */
    private static double[][] matrixOfMinors(double[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        double[][] newMat = new double[m][n];
        if (m != n)
            throw new RuntimeException("Not a square matrix.");
        // It works only when the dimension is larger than 2.
        if (m > 2) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    newMat[i][j] = findDeterminant(smallerMatrix(mat, i, j));
                }
            }
        }
        return newMat;
    }

    /**
     * Calculate the "Matrix of Cofactors"
     * Change the sign of alternate cells by applying a "checkerboard" of minuses
     *
     * @param mat input matrix
     * @return Matrix of Cofactors
     */
    private static double[][] matrixOfCofactors(double[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                mat[i][j] *= mat[i][j] == 0 ? 0 : (double) Math.pow(-1, i + j);
        return mat;
    }

    /**
     * Transpose the matrix
     *
     * @param mat the input matrix
     * @return the transposed matrix
     */
    public static double[][] transpose(double[][] mat) {
        int m = mat.length;
        int n = mat[0].length;
        double[][] result = new double[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                result[j][i] = mat[i][j];
        return result;
    }

    /**
     * Print a matrix in a much more readable way
     * <p>
     * [9  19  21]
     * [24  52  57]
     * [39  85  93]
     *
     * @param mat input matrix
     * @return formatted matrix
     */
    public static String print(double[][] mat) {
        StringBuilder builder = new StringBuilder();
        int m = mat.length;
        int n = mat[0].length;
        DecimalFormat dF = new DecimalFormat("0.###");
        // If the element is an integer, the element will remain the same;
        // while when it comes to decimals, it will keep at most thousandths place.
        // However, "-0.0000001" will turn into "-0"
        //
        // Using "0.00#" here will leave all integers and decimals to the thousandths.

        for (int i = 0; i < m; i++) {
            builder.append("[");
            for (int j = 0; j < n; j++) {
                builder.append(dF.format(mat[i][j]));
                if (j != n - 1)
                    builder.append("  ");
                else
                    builder.append("]\n");
            }
        }
        return builder.toString();
    }

    /**
     * Print a vector in a much more readable way
     * <p>
     * [7  16  25]
     *
     * @param vector input vector
     * @return formatted vector
     */
    public static String print(double[] vector) {
        StringBuilder builder = new StringBuilder();
        int len = vector.length;
        DecimalFormat dF = new DecimalFormat("0.###");
        builder.append("[");
        for (int i = 0; i < len; i++) {
            builder.append(dF.format(vector[i]));
            if (i != len - 1)
                builder.append("  ");
            else
                builder.append("]\n");
        }
        return builder.toString();
    }

    /**
     * Return a string representation of the matrix
     *
     * @param mat the input matrix
     * @return a string representing the matrix
     */
    public static String toString(double[][] mat) {
        StringBuilder builder = new StringBuilder("{");
        int m = mat.length;
        int n = mat[0].length;
        DecimalFormat dF = new DecimalFormat("0.###");
        for (int i = 0; i < m; i++) {
            builder.append("{ ");
            for (int j = 0; j < n; j++) {
                builder.append(dF.format(mat[i][j]));
                if (j != n - 1) {
                    builder.append(", ");
                }
            }
            builder.append(" }");
            if (i != m - 1) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     * Return a string representation of the vector
     * Add convenience by using DecimalFormat
     * Available to substitute with "System.out.println(Arrays.toString(array))"
     *
     * @param vector the input vector
     * @return a string representing the vector
     */
    public static String toString(double[] vector) {
        DecimalFormat dF = new DecimalFormat("0.###");
        StringBuilder builder = new StringBuilder("[");
        int len = vector.length;
        for (int i = 0; i < len; i++) {
            builder.append(dF.format(vector[i]));
            if (i != len - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}