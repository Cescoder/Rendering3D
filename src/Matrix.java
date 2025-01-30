public class Matrix {
    private double[][] matrix;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        matrix = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

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

    public int getRowCount() {
        return rows;
    }

    public int getColCount() {
        return cols;
    }

    public double get(int row, int col) {
        return matrix[row][col];
    }

    public void set(int i, int j, double d) {
        matrix[i][j] = d;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Matrix add(Matrix m2) {
        double[][] result = new double[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result[i][j] = this.matrix[i][j] + m2.matrix[i][j];
            }
        }
        return new Matrix(result);
    }

    public Matrix subtract(Matrix m2) {
        double[][] result = new double[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result[i][j] = this.matrix[i][j] - m2.matrix[i][j];
            }
        }
        return new Matrix(result);
    }

    public Matrix deepCopy() {
        double[][] newMatrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, cols);
        }
        return new Matrix(newMatrix);
    }

}