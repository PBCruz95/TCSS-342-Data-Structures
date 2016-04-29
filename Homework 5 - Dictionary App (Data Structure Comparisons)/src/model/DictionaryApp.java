package model;

import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * Program to read in strings from a file and insert them into a dictionary.
 * 
 * @author Donald Chinn
 * @version October 29, 2002, modified for Java built-in classes February 4,
 *          2010
 * 
 *          Flags: -b binary search tree -v AVL tree -s Splay tree -h hash
 *          function (quadratic probing) -j Java built-in class TreeMap -k Java
 *          built-in class HashMap -x do nothing
 */
public class DictionaryApp {

	// static variables used to identify the
	// data structure/algorithm to use
	// (based on the command line argument)
	private static final int NOALG = 0;
	private static final int useBST = 1;
	private static final int useAVL = 2;
	private static final int useSplay = 3;
	private static final int useHashTable = 4;
	private static final int useJavaTreeMap = 5;
	private static final int useJavaHashMap = 6;
	private static final int useNothing = 7;

	/**
	 * Return the next word (Unicode, big endian) in a file.
	 * 
	 * @param file
	 *            the file to read from
	 * @return the next word in the file, or null if there is no next word A
	 *         word is defined here as a consecutive sequence of alphanumeric
	 *         characters.
	 */
	private static MyString getWord(DataInputStream file) {
		String currentString = null;
		char currentChar;
		try {
			// get rid of leading non-alphanumeric characters
			currentChar = file.readChar();
			
			while (!Character.isLetterOrDigit(currentChar)) {
				currentChar = file.readChar();
			}
		} catch (EOFException eof) {
			return null;
		} catch (IOException io) {
			return null;
		}

		currentString = new String(currentChar + "");

		try {
			// read in the next word
			currentChar = file.readChar();
			while (Character.isLetterOrDigit(currentChar)) {
				currentString += currentChar;
				currentChar = file.readChar();
			}
		} catch (EOFException eof) {
			return new MyString(currentString.toLowerCase());
		} catch (IOException io) {
			return new MyString(currentString.toLowerCase());
		}
		return new MyString(currentString.toLowerCase()); //Edited this so that cases like "The" and "the" count as the same word.
	}

	/**
	 * The driver method for the word counting application.
	 */
	public static void main(String[] args) {
		boolean error = false;

		// timer variables
		long totalTime = 0;
		long startTime = 0;
		long finishTime = 0;
		int numInsertions = 0;

		int whichAlgorithm = NOALG;

		DataInputStream infile = null;

		BinarySearchTree bst = new BinarySearchTree();
		AvlTree avlTree = new AvlTree();
		SplayTree splayTree = new SplayTree();
		QuadraticProbingHashTable hashTable = new QuadraticProbingHashTable();
		TreeMap<MyString, Integer> javaTreeMap = new TreeMap<MyString, Integer>();
		HashMap<MyString, Integer> javaHashMap = new HashMap<MyString, Integer>();

		// Handle command line arguments.
		// Usage: args[0] -[bvshjkx] input_filename
		// Options:
		// -b use a standard binary search tree
		// -v use the recursive implementation of an AVL tree
		// -s splay tree
		// -h hash function (quadratic probing)
		// -j Java built-in class TreeMap
		// -k Java built-in class HashMap
		// -x no data structure (just read in the file)

		if ((args.length < 2) || (args.length > 2)) {
			System.out.println("Argument usage: -[bvshx] infile");
			error = true;
		} else {
			// figure out which option was chosen
			if (args[0].charAt(0) == '-') {
				switch (args[0].charAt(1)) {
				case 'b':
					whichAlgorithm = useBST;
					break;

				case 'v':
					whichAlgorithm = useAVL;
					break;

				case 's':
					whichAlgorithm = useSplay;
					break;

				case 'h':
					whichAlgorithm = useHashTable;
					break;

				case 'j':
					whichAlgorithm = useJavaTreeMap;
					break;

				case 'k':
					whichAlgorithm = useJavaHashMap;
					break;

				case 'x':
					whichAlgorithm = useNothing;
					break;

				default:
					System.out.print("Usage: ");
					System.out.println("-" + args[0].charAt(1) + " is not a valid option.");
					error = true;
					break;
				}

				// Get the input filename
				try {
					infile = new DataInputStream(new FileInputStream(args[1]));
				} catch (IOException ioexception) {
					System.out.println("Error: Could not open " + args[1] + ".");
					error = true;
				}
			} else {
				System.out.println("Argument usage: -[bvshjkx] filename");
				error = true;
			}
		}

		if (!error) {

			MyString currentWord;
			Integer numTimes; // the number of times a key has been encountered
								// so far

			// start the timer
			Date startDate = new Date();
			startTime = startDate.getTime();

			while ((currentWord = DictionaryApp.getWord(infile)) != null) {
				switch (whichAlgorithm) {
				case useBST:
					bst.insert(currentWord);
					numInsertions++;
					break;

				case useAVL:
					avlTree.insert(currentWord);
					numInsertions++;
					break;

				case useSplay:
					splayTree.insert(currentWord);
					numInsertions++;
					break;

				case useHashTable:
					hashTable.insert(currentWord);
					numInsertions++;
					break;

				case useJavaTreeMap:
					numTimes = javaTreeMap.get(currentWord);
					// Increment the word count
					/**
					 * YOUR CODE GOES HERE
					 */
					javaTreeMap.put(currentWord, numTimes);
					numInsertions++;
					break;

				case useJavaHashMap:
					numTimes = javaHashMap.get(currentWord);
					// Increment the word count
					/**
					 * YOUR CODE GOES HERE
					 */
					javaHashMap.put(currentWord, numTimes);
					numInsertions++;
					break;

				case useNothing:
					// do nothing
					numInsertions++;
					break;
				}
			}

			// stop the timer
			Date finishDate = new Date();
			finishTime = finishDate.getTime();
			totalTime += (finishTime - startTime);

			// if the dictionary was the AVL tree, then
			// print out the most frequently occuring words
			if (whichAlgorithm == useAVL) {
				avlTree.PrintMostFrequent();
			}

			System.out.println("** Results for " + args[0] + " option on file ");
			System.out.println("    " + args[1]);
			System.out.println("Time to do insertions: ");
			System.out.println(totalTime + " ms.");
			System.out.println("Number of insertions: " + numInsertions);

		}

		// Close the file stream.
		if (!error) {
			try {
				infile.close();
			} catch (IOException ioexception) {
				System.out.println("Error: Could not close " + args[1] + ".");
				error = true;
			}
		}

		System.out.println("DictionaryApp is done. **");
	}

}
