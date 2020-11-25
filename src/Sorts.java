import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;

/**
 * Author: Ryan Lynch
 * Date: 11/20/2020
 * Section: CS 317-03
 *
 * CS 317-03 Java Honors Project portion
 *
 * This program implements mergesort and quicksort on an array of strings. This array of strings is obtained from an input text file.
 * The program first performs mergesort on the array of strings, then it performs quicksort on a copy of that original array of strings.
 * The sorted data from each sort is written to separate output text files.
 * Timing information is obtained from the mergesort and quicksort algorithms and is used to compare the performance of the two.
 * The constant MAX_LINES can be modified to test out the performance of sorting a certain number of lines of strings.
 */
public class Sorts {

    static final int MAX_LINES = 1000000; // Number of lines you want to read in

    public static void main(String[] args) {
        String linesReadFileName = ""; // Input file name
        String MSWriteFileName = ""; // Output file name for the sorted data obtained using mergesort
        String QSWriteFileName = ""; // Output file name for the sorted data obtained using quicksort
        File linesReadFile; // Input file of unsorted lines used for both sorts
        File MSWriteFile; // Mergesort write file
        File QSWriteFile; // Quicksort write file
        linesReadFile = new File(linesReadFileName);
        String[] mergeSortLines = new String[MAX_LINES]; // List of lines to be sorted using mergesort
        String[] quickSortLines = new String[MAX_LINES]; // List of lines to be sorted using quicksort
        String[] temp = new String[MAX_LINES]; // Temporary array used in mergesort

        // Read the input file name from the console
        System.out.println("What is the name of your input file?");
        Scanner consoleReader = new Scanner(System.in);
        linesReadFileName = consoleReader.nextLine();

        // Open the input file for reading and read in all of the lines
        try
        {
            linesReadFile = new File(linesReadFileName);
            Scanner linesScanner = new Scanner(linesReadFile);

            int i = 0; // Index used to add elements to the lines array in the loop
            while (linesScanner.hasNextLine() && i < MAX_LINES) // Either stop reading when the file runs out of lines or when we reach MAX_LINES lines
            {
                mergeSortLines[i] = linesScanner.nextLine();
                i++;
            }

            linesScanner.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error opening input file.");
            e.printStackTrace();
        }

        quickSortLines = mergeSortLines.clone(); // Copy the unsorted string list to be used for merge sort into the array to be used for quick sort

        // Read the output file names from the console
        System.out.println("What is the name of your output file for the data obtained using mergesort?");
        consoleReader = new Scanner(System.in);
        MSWriteFileName = consoleReader.nextLine();

        System.out.println("What is the name of your output file for the data obtained using quicksort?");
        consoleReader = new Scanner(System.in);
        QSWriteFileName = consoleReader.nextLine();

        // Perform the mergesort, obtain the start and end time of the algorithm
        long mergeSortBegin = System.nanoTime();
        mergeSort(mergeSortLines, 0, mergeSortLines.length - 1, temp);
        long mergeSortEnd = System.nanoTime();

        long mergeSortDuration = (mergeSortEnd - mergeSortBegin);
        System.out.println("Mergesort duration: " + mergeSortDuration + " nanoseconds");

        // Perform the quicksort, obtain the start and end time of the algorithm
        long quickSortBegin = System.nanoTime();
        quickSort(quickSortLines, 0, quickSortLines.length - 1);
        long quickSortEnd = System.nanoTime();

        long quickSortDuration = (quickSortEnd - quickSortBegin);
        System.out.println("Quicksort Duration: " + quickSortDuration + " nanoseconds");

        // Write the sorted data to separate text files
        writeSortedArrayToFile(mergeSortLines, mergeSortLines.length, MSWriteFileName);
        writeSortedArrayToFile(quickSortLines, quickSortLines.length, QSWriteFileName);

    }


    /**
     * The mergesort algorithm.
     * Recursively divides the string lines array into 2 arrays, then combines these arrays in the merge step.
     *
     * @param lines The lines array
     * @param first Index of the first element in the subarray
     * @param last Index of the last element in the subarray
     * @param temp The temporary array used to hold the sorted data throughout the mergesort operation
     */
    public static void mergeSort(String[] lines, int first, int last, String[] temp)
    {
        int middle;

        if (first < last)
        {
            middle = (first + last) / 2;

            mergeSort(lines, first, middle, temp);
            mergeSort(lines, middle + 1, last, temp);
            merge(lines, first, last, temp);
        }
    }


    /**
     * The merge operation used in mergeSort.
     * Merges two sorted arrays into one sorted array by taking a pointer to the left half of the array and a pointer to the right half of the array
     * and comparing elements until one pointer reaches the end of its half of the array. The elements are put into the temp array as the comparisons happen.
     * Then, it copies the remaining elements in the array with unread elements to the temp array. The temp array is then copied into the lines array.
     * This step of mergesort takes O(n) time.
     * The string comparison works by viewing the strings in their lowercase form (using strToLower()) and then comparing the strings.
     * @param lines The lines array
     * @param first Index of the first element in the subarray
     * @param last Index of the last element in the subarray
     * @param temp The temporary array used to hold the sorted data throughout the mergesort operation
     */
    public static void merge(String[] lines, int first, int last, String[] temp)
    {
        int middle = (first + last) / 2;
        int leftArrIndex = first; // Pointer to the left array that gets updated throughout the comparisons
        int rightArrIndex = middle + 1; // Pointer to the right array that gets updated throughout the comparisons
        int rightLast = last; // The last index in the right array
        int leftLast = middle; // The last index in the left array
        int index = first; // Used to store strings into the temp array and copy the temp array to the lines array. Gets updated after every comparison

        while (leftArrIndex <= middle && rightArrIndex <= last)
        {
            if (lines[leftArrIndex].toLowerCase().compareTo(lines[rightArrIndex].toLowerCase()) < 0) // left string < right string (case-insensitive)
                temp[index] = lines[leftArrIndex++];
            else
                temp[index] = lines[rightArrIndex++];

            index++;
        }

        // Copy the rest of the left subarray if necessary
        while (leftArrIndex <= leftLast)
            temp[index++] = lines[leftArrIndex++];

        // Copy the rest of the right subarray if necessary
        while (rightArrIndex <= rightLast)
            temp[index++] = lines[rightArrIndex++];

        // Copy the temp array to the lines array for this particular range of indices
        for (index = first; index <= rightLast; index++)
            lines[index] = temp[index];
    }


    public static void quickSort(String[] lines, int left, int right)
    {
        int split; // The position the array is split at

        if (left < right)
        {
            split = hoarePartition(lines, left, right);
            quickSort(lines, left, split - 1);
            quickSort(lines, split + 1, right);
        }
    }


    public static int hoarePartition(String[] lines, int left, int right)
    {
        int pivot = left; // Using the left index (median) as the pivot

        int i = left; // i starts at the left index (pivot)
        int j = right + 1; // j starts one after the right index

        String temp = ""; // Temporary string used when swapping

        do {
            do {
                if (i == right) // Prevent i from going out of bounds
                    break;
                else
                    i++;
            } while (lines[i].toLowerCase().compareTo(lines[pivot].toLowerCase()) < 0); // Stops when a value >= the pivot is encountered
            do {
                j--;
            } while (lines[j].toLowerCase().compareTo(lines[pivot].toLowerCase()) > 0); // Stops when a value <= the pivot is encountered

            // Swap lines[i] with lines[j]
            temp = lines[i];
            lines[i] = lines[j];
            lines[j] = temp;
        } while (i < j);

        // Undo the last swap that occurred when i >= j
        temp = lines[i];
        lines[i] = lines[j];
        lines[j] = temp;

        // Swap the pivot when the element at index j (the split position)
        temp = lines[pivot];
        lines[pivot] = lines[j];
        lines[j] = temp;


        return j; // Return the split position j
    }


    /**
     * Writes a sorted array to an output file.
     * @param lines The sorted array of strings
     * @param linesLength The length of the array of strings
     * @param fileName The output file to write the sorted strings
     */
    public static void writeSortedArrayToFile(String[] lines, int linesLength, String fileName) {
        File outFile = new File(fileName);
        FileWriter outFileWriter = null;

        try {
            outFileWriter = new FileWriter(outFile);

            for (int i = 0; i < linesLength; i++)
                outFileWriter.append(lines[i] + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
