import edu.princeton.cs.algs4.Picture;

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
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
        energyArray = new double[height][width];
        constructEnergyOfPixels(this.picture);
    }
    private int toIndex(int col, int row, int width) {
        return (row-1) * width + col;
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

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
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
        return Math.sqrt(Rx) + Math.sqrt(Gx) + Math.sqrt(Bx);
    }
    private double computeGradientY2(int x, int y) {
        double Rx = Math.abs((new Color(picture.getRGB(x,y - 1))).getRed() - (new Color(picture.getRGB(x,y + 1))).getRed());
        double Gx = Math.abs((new Color(picture.getRGB(x,y - 1))).getGreen() - (new Color(picture.getRGB(x,y + 1))).getGreen());
        double Bx = Math.abs((new Color(picture.getRGB(x,y - 1))).getBlue() - (new Color(picture.getRGB(x,y + 1))).getBlue());
        return Math.sqrt(Rx) + Math.sqrt(Gx) + Math.sqrt(Bx);
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

    private class SP{
        private int[] edgeTo;
        private double[] distTo;
        private double[] energy;
        private int width, height;
        public SP(double[][] energyArray, int width, int height){
            this.width = width;
            this.height = height;
            double[] energy = construct1Dfrom2DArray(energyArray);
            int arrayLength = energy.length;
            edgeTo = new int[arrayLength];
            distTo = new double[arrayLength];
            for (int i = 0; i < arrayLength; i++) {
                distTo[i] = Double.POSITIVE_INFINITY;
            }
            for (int i = 0; i < arrayLength; i++) {
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
                for (int j = 0; i < width; i++) {
                    a.add(j);
                }
                return a;
            } else {
                return null;
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
    }
    private int toCol(int index) {
        return index % width;
    }
    private int toRow(int index) {
        return Math.floorDiv(index, width) + 1;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        Picture newPic = new Picture(width, height - 1);
        for (int i = 0; i < width; i++) {//col
            for (int m = 0; m < seam[i]; m++) {//row
                newPic.setRGB(i, m, picture.getRGB(i, m));
            }
            for (int n = width - 2; n >= seam[i]; n-- ) {
                newPic.setRGB(n, i, picture.getRGB(n+1, i));
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
        changeEnergyValue(seam, true);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam){
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
        changeEnergyValue(seam, true);
    }
    private void changeEnergyValue(int[] seam, boolean isVertical) {
        if (isVertical) {
            for (int i = 0; i < seam.length; i++) {
                energyArray[i][seam[i] - 1] = energy(seam[i] - 1, i);
                energyArray[i][seam[i]] = energy(seam[i], i);
            }
        } else {
            for (int i = 0; i < seam.length; i++) {
                energyArray[seam[i] - 1][i] = energy(i, seam[i] - 1);
                energyArray[seam[i]][i] = energy(i, seam[i]);
        }
    }
    }

    //  unit testing (optional)


}
