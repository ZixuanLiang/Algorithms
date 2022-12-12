import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SeamCarver {
    private Picture picture;
    private int width, height;
    private double[][] energyArray;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        nullArgumentCheck(picture);
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
        energyArray = new double[height][width];
        constructEnergyOfPixels(this.picture);
    }
    private void nullArgumentCheck(Object o) {
        if (o == null) {
            throw new IllegalArgumentException("argument is null");
        }
    }
    private void constructEnergyOfPixels(Picture picture){
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++){
                energyArray[row][col] = energy(col, row);
            }
        }
    }
    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }
    private void argumentRangeCheck(int col, int row) {
        if (col<0 || col>=width) {
            throw new IllegalArgumentException("col is outside the range");
        } else if (row<0 || row>=height) {
            throw new IllegalArgumentException("row is outside the range");
        }
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        argumentRangeCheck(x, y);
        if (isBorder(x, y)) {
            return 1000;
        } else {
            double squareOfGradientX = computeGradientX2(x, y);
            double squareOfGradientY = computeGradientY2(x, y);
            return Math.sqrt(squareOfGradientX + squareOfGradientY);
        }
    }
    private double computeGradientX2(int x, int y){
        double Rx = Math.abs((new Color(picture.getRGB(x-1,y))).getRed() - (new Color(picture.getRGB(x+1,y))).getRed());
        double Gx = Math.abs((new Color(picture.getRGB(x-1,y))).getGreen() - (new Color(picture.getRGB(x+1,y))).getGreen());
        double Bx = Math.abs((new Color(picture.getRGB(x-1,y))).getBlue() - (new Color(picture.getRGB(x+1,y))).getBlue());
        return Math.pow(Rx, 2) + Math.pow(Gx, 2) + Math.pow(Bx, 2);
    }
    private double computeGradientY2(int x, int y) {
        double Ry = Math.abs((new Color(picture.getRGB(x,y - 1))).getRed() - (new Color(picture.getRGB(x,y + 1))).getRed());
        double Gy = Math.abs((new Color(picture.getRGB(x,y - 1))).getGreen() - (new Color(picture.getRGB(x,y + 1))).getGreen());
        double By = Math.abs((new Color(picture.getRGB(x,y - 1))).getBlue() - (new Color(picture.getRGB(x,y + 1))).getBlue());
        return Math.pow(Ry, 2) + Math.pow(Gy, 2) + Math.pow(By, 2);
    }
    private boolean isBorder(int x, int y){
        if (x == 0 || x == width - 1){
            return true;
        } else if (y == 0 || y == height - 1){
            return true;
        } else {
            return false;
        }
    }
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        SP sp = new SP(transpose(energyArray), height, width);
        return sp.findSeam();
    }
    private double[][] transpose(double[][] orin){
        double[][] transpose = new double[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                transpose[j][i] = orin[i][j];
            }
        }
        return transpose;
    }
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        SP sp = new SP(energyArray, width, height);
        return sp.findSeam();
    }



    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        seamArgumentCheck(seam, false);
        Picture newPic = new Picture(width, height - 1);
        for (int i = 0; i < width; i++) {//col
            for (int m = 0; m < seam[i]; m++) {//row
                newPic.setRGB(i, m, picture.getRGB(i, m));
            }
            for (int n = height - 2; n >= seam[i]; n-- ) {//row
                newPic.setRGB(i, n, picture.getRGB(i, n + 1));
                energyArray[n][i] = energyArray[n+1][i];
            }
        }
        // change energy array
        double[][] newEnergyArray = new double[height - 1][];
        for (int i = 0; i < height - 1; i++) {
            newEnergyArray[i] = energyArray[i];
        }
        energyArray = newEnergyArray;
        picture = newPic;
        height--;
        changeEnergyValue(seam, false);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam){
        seamArgumentCheck(seam, true);
        Picture newPic = new Picture(picture.width() - 1, picture.height());
        for (int i = 0; i < height; i++) {
            double[] newEnergyArray = new double[width - 1];
            for (int m = 0; m < seam[i]; m++) {
                newPic.setRGB(m, i, picture.getRGB(m, i));
                newEnergyArray[m] = energyArray[i][m];
            }
            for (int n = width - 2; n >= seam[i]; n-- ) {
                newPic.setRGB(n, i, picture.getRGB(n+1, i));
                newEnergyArray[n] = energyArray[i][n+1];
            }
            energyArray[i] = newEnergyArray;
        }
        picture = newPic;
        width--;
        changeEnergyValue(seam, true);
    }

    private void changeEnergyValue(int[] seam, boolean isVertical) {
        if (isVertical) {
            for (int i = 0; i < seam.length; i++) {
                if (seam[i] > 0) {
                    energyArray[i][seam[i] - 1] = energy(seam[i] - 1, i);
                } else if (seam[i] < width) {
                    energyArray[i][seam[i]] = energy(seam[i], i);
                }
            }
        } else {
            for (int i = 0; i < seam.length; i++) {// i is col
                if (seam[i] > 0) {
                    energyArray[seam[i] - 1][i] = energy(i, seam[i] - 1);
                } else if (seam[i] < width) {
                    energyArray[seam[i]][i] = energy(i, seam[i]);
                }
        }
    }
    }
    private void seamArgumentCheck(int[] seam, boolean isVertical) {
        nullArgumentCheck(seam);
        if (isVertical) {
            if (seam.length != height) {
                throw new IllegalArgumentException("seam array's length is not equal to height");
            } else if (width <= 1) {
                throw new IllegalArgumentException("width of the picture is smaller than 1");
            }
        } else {
            if (seam.length != width) {
                throw new IllegalArgumentException("seam array's length is not equal to width");
            } else if (height <= 1) {
                throw new IllegalArgumentException("height of the picture is smaller than 1");
            }
        }
        int last = seam[0];
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i]-last) > 1) {
                throw new IllegalArgumentException("two entries' difference is larger than 1");
            }
            last = seam[i];
        }
    }
    private class SP{
        private int[] edgeTo;
        private double[] distTo;
        private double[] energy;
        private int width, height;
        public SP(double[][] energyArray, int width, int height){
            this.width = width;
            this.height = height;
            energy = construct1Dfrom2DArray(energyArray);
            int arrayLength = energy.length;
            edgeTo = new int[arrayLength];
            distTo = new double[arrayLength];
            for (int i = 0; i < arrayLength; i++) {
                distTo[i] = Double.POSITIVE_INFINITY;
            }
            distTo[width*height] = 0;
            List<Integer> topologicalOrder = new ArrayList<>();
            topologicalOrder.add(width*height);
            for (int i = 0; i < width*height; i++) {
                topologicalOrder.add(i);
            }
            topologicalOrder.add(width*height + 1);
            for (int i : topologicalOrder) {
                for (int j : adj(i)) {
                    relax(i,j);
                }
            }
        }
        private double[] construct1Dfrom2DArray(double[][] twoD){
            double[] oneD = new double[width*height + 2];
            for (int i = 0; i < height; i++) {
                System.arraycopy(twoD[i], 0, oneD, width*i, width);
            }
            oneD[width*height] = 0;
            return oneD;
        }
        private List<Integer> adj(int i) {
            if (i < width * (height-1)) {
                if (i % width == 0) {
                    return Arrays.asList(i+width,i+width+1);
                } else if ((i + 1) % width == 0) {
                    return Arrays.asList(i+width-1, i+width);
                } else {
                    return Arrays.asList(i+width-1, i+width, i+width+1);
                }
            } else if (i < width * height) {
                return Arrays.asList(width*height+1);
            } else if (i == width * height) {
                List<Integer> a = new ArrayList<>();
                for (int j = 0; j < width; j++) {
                    a.add(j);
                }
                return a;
            } else {
                return Arrays.asList();
            }
        }
        private void relax(int v, int w) {
            if (distTo[w] > distTo[v] + energy[v]) {
                distTo[w] = distTo[v] + energy[v];
                edgeTo[w] = v;
            }
        }
        private int[] findSeam() {
            int indexOfTop = width * height;
            int indexOfBottom = width * height + 1;
            LinkedList<Integer> list = new LinkedList<Integer>();
            int w = indexOfBottom;
            while (edgeTo[w] != indexOfTop) {
                list.push(toCol(edgeTo[w]));
                w = edgeTo[w];
            }
            int[] a = new int[height];
            Object[] l = list.toArray();
            for (int i = 0; i < l.length; i++) {
                a[i] = (int) l[i];
            }
            return a;
        }
        private int toCol(int index) {
            return index % width;
        }
    }
    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        int[] verticalSeam = new int[]{ 3, 4, 5, 5, 4 };
        sc.removeVerticalSeam(verticalSeam);
    }
}
