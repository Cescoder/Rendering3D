public class Matrix {
    private double[][] matrix;
    private int rows;
    private int cols;

    public Matrix(double[][] matrix) {
        this.matrix = matrix;
        this.rows = matrix.length;
        this.cols = matrix[0].length;
    }

    public Matrix multiply(Matrix other) {
        double[][] result = new double[this.rows][other.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                for (int k = 0; k < this.cols; k++) {
                    result[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        return new Matrix(result);
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result += this.matrix[i][j] + " ";
            }
            result += "\n";
        }
        return result;
    }

    public Matrix clone() {
        double[][] matrix = new double[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                matrix[i][j] = this.matrix[i][j];
            }
        }
        return new Matrix(matrix);
    }

    public double[][] getMatrix() {
        return this.matrix;
    }

}