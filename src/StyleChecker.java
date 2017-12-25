import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This is the main class. This class includes the main, processInputFile,
 * optionalBraces, braceAlignment, indentation, operatorSpace, checkCase,
 * blankLine, multipleStatementsInLine, countSemiColon, lineLength, and
 * generateReport method
 *
 * @author Thien Ngo N. Le
 */
public class StyleChecker {

    private static final String INPUT_FILE_NAME = "input/Chris_indents.txt";
    private static final String OUTPUT_FILE_NAME = "output/Chris_indents.txt";
    private static final String FIXED_CODE_FILE_NAME = "fixedcode/Chris_indents.txt";
    private static ArrayList<String> inputData = new ArrayList<>();

    /**
     * This is the main method. This method calls processInputFile and generateReport
     * methods to process the program
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Call processInputFile method to process the input data
        processInputFile(INPUT_FILE_NAME);
        // Call the generateReport method to print the report to the output file
        generateReport();
        // Call the blankLineStyleFixer method to print out corrected code
        blankLineStyleFixer();
    }

    /***************************************************************************
     * This is processInputFile method. This method first checks to see weather 
     * the input file is available or not. Then reads the input file line by line 
     * and assigns them into an array list named inputData.
     *
     * @param fileName the name of the input file that need to be processed.
     */
    public static void processInputFile(String fileName) {
        File inputFile = null;
        Scanner scnr;
        try {
            inputFile = new File(INPUT_FILE_NAME);
            scnr = new Scanner(inputFile);

            while (scnr.hasNext()) {
                inputData.add(scnr.nextLine());
            }
        } catch (FileNotFoundException ex) {
            //Print out the message to the consolate when the file is not found
            System.err.println("\nERROR: The input file name " + inputFile
                    + " was not found.\nPlease check your input file again.");

            // Exit the program when the file is not found
            System.exit(0);
        }
    }

    /***************************************************************************
     * This is optionalBraces method. This method scans every line on the input 
     * file and reports an error if there is any single if, else, for, while, 
     * and doWhile statement that misses the curly braces.
     *
     * @param lines of the input file
     * @return an array of integer contains all line number that has error.
     */
    private static int[] optionalBraces(ArrayList<String> lines) {
        int[] errorLineNumber = new int[lines.size()];
        int j = 0;
        for (int i = 0; i < lines.size() - 1; i++) {
            String line = lines.get(i).trim();
            String nextLine = lines.get(i + 1).trim();
            boolean doesNotHasBraces = !line.contains("{") && !nextLine.contains("{");

            if (line.startsWith("if") && doesNotHasBraces
                    || line.startsWith("else") && doesNotHasBraces
                    || line.startsWith("for") && doesNotHasBraces
                    || line.startsWith("while") && doesNotHasBraces
                    || line.startsWith("do ") && doesNotHasBraces) {
                errorLineNumber[j] = i + 1;
                j++;
            }
        }
        return errorLineNumber;
    }

    /***************************************************************************
     * This is braceAlignment method. This method scans every line on the input 
     * file and reports an error if there is any braces alignment error
     *
     * @param lines the input file lines
     * @return an array of integer contains all line number that has error.
     */
    private static int[] braceAlignment(ArrayList<String> lines) {
        int[] errorLineNumber = new int[lines.size()];
        int j = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            boolean hasBraces = line.contains("{") || line.contains("}");
            if (hasBraces && line.length() > 1) {
                errorLineNumber[j] = i + 1;
                j++;
            }
        }
        return errorLineNumber;
    }

    /***************************************************************************
     * This is indentation method. This method scans every line on the input 
     * file and reports an error if there is any indentation error.
     *
     * @param lines the input file lines
     * @return an array of integer contains all line number that has error.
     */
    private static int[] indentation(ArrayList<String> lines) {
        int[] errorLineNumber = new int[lines.size()];
        int j = 0;
        int numOfOpenBrace = 0;
        int numOfCloseBrace = 0;
        int spaceNeed = 0;
        int i = 0;

        while (i < lines.size() - 1) {
            String currentLine = lines.get(i).trim();
            String nextLine = lines.get(i + 1);

            if (currentLine.contains("{")) {
                numOfOpenBrace++;
                spaceNeed = spaceNeed + 3;

                if (nextLine.length() != 0) {
                    int spaceCount = 0;
                    for (char c : nextLine.toCharArray()) {
                        if (c == ' ') {
                            spaceCount++;
                        } else {
                            break;
                        }
                    }

                    if (spaceNeed != spaceCount) {
                        errorLineNumber[j] = i + 2;
                        j++;
                    }
                }
            } else if (currentLine.contains("}") && nextLine.contains("}")) {
                numOfCloseBrace++;
                spaceNeed = spaceNeed - 3;

                if (nextLine.length() != 0) {
                    int spaceCount = 0;
                    for (char c : nextLine.toCharArray()) {
                        if (c == ' ') {
                            spaceCount++;
                        } else {
                            break;
                        }
                    }

                    if (spaceNeed != (spaceCount + 3)) {
                        errorLineNumber[j] = i + 2;
                        j++;
                    }
                }
            } else if (currentLine.contains("}")) {
                numOfCloseBrace++;
                spaceNeed = spaceNeed - 3;

                if (nextLine.length() != 0) {
                    int spaceCount = 0;
                    for (char c : nextLine.toCharArray()) {
                        if (c == ' ') {
                            spaceCount++;
                        } else {
                            break;
                        }
                    }

                    if (spaceNeed != spaceCount) {
                        errorLineNumber[j] = i + 2;
                        j++;
                    }
                }
            } else {
                if (nextLine.length() != 0 && nextLine.contains("}")) {
                    int spaceCount = 0;
                    for (char c : nextLine.toCharArray()) {
                        if (c == ' ') {
                            spaceCount++;
                        } else {
                            break;
                        }
                    }

                    if (spaceNeed != (spaceCount + 3)) {
                        errorLineNumber[j] = i + 2;
                        j++;
                    }
                } else if (nextLine.length() != 0) {
                    int spaceCount = 0;
                    for (char c : nextLine.toCharArray()) {

                        if (c == ' ') {
                            spaceCount++;
                        } else {
                            break;
                        }
                    }
                    if (spaceNeed != spaceCount) {
                        errorLineNumber[j] = i + 2;
                        j++;
                    }
                }
            }
            i++;
        }
        return errorLineNumber;
    }

    /***************************************************************************
     * This is operatorSpace method. This method scans every line on the input 
     * file and reports an error if there is any operator that misses space 
     * around it.
     *
     * @param lines the input file lines
     * @return an array of integer contains all line number that has error.
     */
    private static int[] operatorSpace(ArrayList<String> lines) {
        int[] errorLineNumber = new int[lines.size()];
        int j = 0;

        for (int i = 0; i < lines.size(); i++) {
            String currentLine = lines.get(i);

            if (!currentLine.contains("import") && (currentLine.length() >= 3)) {
                char[] lineArray = currentLine.toCharArray();

                for (int k = 1; k < lineArray.length - 1; k++) {
                    if (lineArray[k - 1] != '+' && lineArray[k] == '+'
                            && lineArray[k + 1] != '+'
                            && (lineArray[k - 1] != ' ' || lineArray[k + 1] != ' ')) {
                        errorLineNumber[j] = i + 1;
                        j++;
                        break;
                    } else if (lineArray[k - 1] != '-' && lineArray[k] == '-'
                            && lineArray[k + 1] != '-'
                            && (lineArray[k - 1] != ' ' || lineArray[k + 1] != ' ')) {
                        errorLineNumber[j] = i + 1;
                        j++;
                        break;
                    } else if (lineArray[k - 1] != '*' && lineArray[k] == '*'
                            && lineArray[k + 1] != '*'
                            && (lineArray[k - 1] != ' ' || lineArray[k + 1] != ' ')) {
                        errorLineNumber[j] = i + 1;
                        j++;
                        break;
                    } else if (lineArray[k - 1] != '/' && lineArray[k] == '/'
                            && lineArray[k + 1] != '/'
                            && (lineArray[k - 1] != ' ' || lineArray[k + 1] != ' ')) {
                        errorLineNumber[j] = i + 1;
                        j++;
                        break;
                    } else if (lineArray[k - 1] != '%' && lineArray[k] == '%'
                            && lineArray[k + 1] != '%'
                            && (lineArray[k - 1] != ' ' || lineArray[k + 1] != ' ')) {
                        errorLineNumber[j] = i + 1;
                        j++;
                        break;
                    }
                }
            }
        }
        return errorLineNumber;
    }

    /***************************************************************************
     * This is blankLine method. This method scans every line on the input 
     * file and reports an error if the code misses a blank line between methods,
     * between the class header and declarations, and between the end of the 
     * declaration and the first method header.
     *
     * @param lines of the input file
     * @return an array of integer contains all line number that has error.
     */
    private static int[] blankLine(ArrayList<String> lines) {
        int[] errorLineNumber = new int[lines.size()];
        int j = 0;

        for (int i = 0; i < lines.size() - 2; i++) {
            String currentLine = lines.get(i).trim();
            String nextLine = lines.get(i + 1).trim();
            String nextNextLine = lines.get(i + 2).trim();

            if (isClassHeader(currentLine) && currentLine.contains("{")
                    && nextLine.length() > 0) {
                errorLineNumber[j] = i + 2;
                j++;
            } else if (isClassHeader(currentLine) && nextLine.contains("{")
                    && nextNextLine.length() > 0) {
                errorLineNumber[j] = i + 3;
                j++;
            } else if (isMethodHeader(nextLine) && currentLine.length() > 0) {
                if (errorLineNumber[j - 1] != (i + 2)) {
                    errorLineNumber[j] = i + 2;
                    j++;
                }
            }
        }
        return errorLineNumber;
    }

    /***************************************************************************
     * This is isMethodHeader method. This method check a line and return true 
     * if the line is a method header.
     *
     * @param line a line to check weather it is a method header or not.
     * @return true if the line is a method header, otherwise return false.
     */
    private static boolean isMethodHeader(String line) {
        boolean isMethod = false;
        boolean keyWord = line.contains("public ") || line.contains("private ");

        if (keyWord && line.contains("(") && !line.contains(";")) {
            isMethod = true;
        }
        return isMethod;
    }

    /***************************************************************************
     * This is isClassHeader method. This method check a line and return true 
     * if the line is a class header. 
     *
     * @param line to check weather it is a class header or not.
     * @return true if the line is a class header, otherwise return false.
     */
    private static boolean isClassHeader(String line) {
        boolean isClass = false;

        if (line.contains("class ")) {
            isClass = true;
        }
        return isClass;
    }

    /***************************************************************************
     * This is multipleStatementsInLine method. This method scans every line on 
     * the input file and reports an error if there is any line that has more 
     * than one statement.
     *
     * @param lines the input file lines
     * @return an array of integer contains all line number that has error.
     */
    private static int[] multipleStatementsInLine(ArrayList<String> lines) {
        int[] errorLineNumber = new int[lines.size()];
        int j = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            int numberOfSemiColon = countSemiColon(line);
            if (numberOfSemiColon > 2 && line.startsWith("for")) {
                errorLineNumber[j] = i + 1;
                j++;
            } else if ((line.startsWith("if")
                    || line.startsWith("else")
                    || line.startsWith("while")
                    || line.startsWith("do ")) && numberOfSemiColon >= 1) {
                errorLineNumber[j] = i + 1;
                j++;
            } else if (numberOfSemiColon > 1 && !line.startsWith("for")) {
                errorLineNumber[j] = i + 1;
                j++;
            }
        }
        return errorLineNumber;
    }

    /***************************************************************************
     * This is countSemiColon method. This method receives a line then count the
     * number of semi-colon that the line contains.
     *
     * @param line a line to count the number of semi-colon.
     * @return the number of semi-colon on the line.
     */
    private static int countSemiColon(String line) {
        int counter = 0;
        char[] lineArray = line.toCharArray();
        for (char c : lineArray) //for each loop
        {
            if (c == ';') {
                counter++;
            }
        }
        return counter;
    }

    /***************************************************************************
     * This is lineLength method. This method scans every line on the input file
     * then report errors if there is any line that has the length more than 80 
     * characters. 
     *
     * @param lines of the input file
     * @return an array of integer contains all line number that has error.
     */
    private static int[] lineLength(ArrayList<String> lines) {
        int[] errorLineNumber = new int[lines.size()];
        int j = 0;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.length() > 80) {
                errorLineNumber[j] = i + 1;
                j++;
            }
        }
        return errorLineNumber;
    }

    /***************************************************************************
     * This is generateReport method. This method calls others methods to check 
     * for the errors then generates a report and writes it to an output file.
     */
    private static void generateReport() {
        File file = null;
        PrintWriter writer = null;
        final String myName = "ThienNgo N. Le";
        final String date = "Friday October 20, 2017";
        String authorName = "";
        String errorTypes = "";

        try {
            file = new File(OUTPUT_FILE_NAME);
            writer = new PrintWriter(file);
        } catch (FileNotFoundException ex) {
            System.err.println("Error: File " + OUTPUT_FILE_NAME
                    + " was not found.");

            System.exit(0);
        }
        for (int i = 0; i < inputData.size(); i++) {
            String line = inputData.get(i).trim();
            if (line.equals("/*")) {
                authorName = inputData.get(i + 1);
                errorTypes = inputData.get(i + 2);
                break;
            }
        }
        writer.println("Style report by: " + myName + "\nReport date: " + date
                + "\nTest program author: " + authorName
                + "\nError(s) checked: " + errorTypes + "\n\nStyle errors found:");
        writer.println("----------------------------------------------------"
                + "----------------------------\n");

        //errorLineNumber
        int[] tooLongLineNum = lineLength(inputData);
        int[] multiCodesLineNum = multipleStatementsInLine(inputData);
        int[] blankLineNum = blankLine(inputData);
        int[] optionalBraceLineNum = optionalBraces(inputData);
        int[] operatorSpaceLineNum = operatorSpace(inputData);
        int[] braceAligLineNum = braceAlignment(inputData);
        int[] indentLineNum = indentation(inputData);

        int numOfOptinalBracesError = 0;

        //Output the line number that is too long
        for (int i = 0; i < tooLongLineNum.length; i++) {
            if (tooLongLineNum[i] == 0) {
                break;
            }
            writer.println("LINE LENGTH ERROR: Line number " + tooLongLineNum[i]
                    + " is too long.");
        }

        //Output the line number that has multiple statements in one line
        for (int i = 0; i < multiCodesLineNum.length; i++) {
            if (multiCodesLineNum[i] == 0) {
                break;
            }
            writer.println("MULTIPLE STATEMENTS ERROR: Line number "
                    + multiCodesLineNum[i] + " has more than one statement.");
        }

        //Output the line number that misses a blank line
        for (int i = 0; i < blankLineNum.length; i++) {
            if (blankLineNum[i] == 0) {
                break;
            }
            writer.println("BLANK LINE ERROR: Missing a blank line above line number "
                    + blankLineNum[i]);
        }

        //Output the line number that misses optional curly braces
        for (int i = 0; i < optionalBraceLineNum.length; i++) {
            if (optionalBraceLineNum[i] == 0) {
                break;
            }
            writer.println("OPTIONAL CURLY BRACES ERROR: Line number " + optionalBraceLineNum[i]
                    + " misses optional curly braces");
            numOfOptinalBracesError++;
        }

        //Output the line number that misses spces around operator
        for (int i = 0; i < operatorSpaceLineNum.length; i++) {
            if (operatorSpaceLineNum[i] == 0) {
                break;
            }
            writer.println("OPERATOR SPACES ERROR: Line number " + operatorSpaceLineNum[i]
                    + " misses spaces around the operator(s).");
        }

        //Output the line number that has brace alignment error
        for (int i = 0; i < braceAligLineNum.length; i++) {
            if (braceAligLineNum[i] == 0) {
                break;
            }
            writer.println("BRACE ALIGNMENT ERROR: Line number " + braceAligLineNum[i]
                    + " has brace alignment error.");
        }

        //Output the line number that has indentation error
        if (numOfOptinalBracesError > 0) {

            //System.out.println("# optinal braces error " + numOfOptinalBracesError);
            writer.println("ATTENTION: FIX YOUR OPTIONAL CURLY BRACES ERROR(S) "
                    + "TO CHECK YOUR INDENTATION STYLE ERROR(S).");
        } else {
            for (int i = 0; i < indentLineNum.length; i++) {
                if (indentLineNum[i] == 0) {
                    break;
                }
                writer.println("INDENTATION ERROR: Line number " + indentLineNum[i]
                        + " has indentation error.");
            }
        }

        writer.println("Program is written by ThienNgo Le");

        writer.close();
    }

    /**
     * This is blankLineStyleFixer method, this method fix all blank line error(s)
     * and print out the correct code.
     */
    private static void blankLineStyleFixer() {
        File file = null;
        PrintWriter writer = null;
        final String myName = "ThienNgo N. Le";
        final String date = "Friday October 20, 2017";
        String authorName = "";
        String errorTypes = "";

        try {
            file = new File(FIXED_CODE_FILE_NAME);
            writer = new PrintWriter(file);
        } catch (FileNotFoundException ex) {
            System.err.println("Error: File " + FIXED_CODE_FILE_NAME
                    + " was not found.");

            System.exit(0);
        }
        for (int i = 0; i < inputData.size(); i++) {
            String line = inputData.get(i).trim();
            if (line.equals("/*")) {
                authorName = inputData.get(i + 1);
                errorTypes = inputData.get(i + 2);
                break;
            }
        }
        writer.println("Style error(s) fix by: " + myName + "\nFix date: " + date
                + "\nSource program author: " + authorName
                + "\nError(s) fixed: " + errorTypes + "\n\nFixed code:");
        writer.println("----------------------------------------------------"
                + "----------------------------\n");

        //array of error line numbers    
        int[] blankLineNum = blankLine(inputData);

        int offset = -1;

        for (int j = 0; j < blankLineNum.length; j++) {
            if (blankLineNum[j] == 0) {
                break;
            } else {
                inputData.add(blankLineNum[j] + offset, "");
                offset++;
            }
        }

        for (int i = 0; i < inputData.size(); i++) {
            String currentLine = inputData.get(i);
            writer.println(currentLine);
        }

        writer.println("Program is written by ThienNgo Le");

        writer.close();
    }
}
