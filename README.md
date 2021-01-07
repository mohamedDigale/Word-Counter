# Word-Counter
Description: This is Java program that counts word occurrences in a given input file and outputs an HTML document with a table of the words and counts listed in alphabetical order.

Instructions:

The program shall ask the user for the name of an input file and for the name of an output file.

The input file can be an arbitrary text file. No special requirements are imposed.

The output shall be a single well-formed HTML file displaying the name of the input file in a heading followed by a table listing the words and their corresponding counts. The words should appear in alphabetical order.

Words contain no whitespace characters. Beyond that, it is up to you to come up with a reasonable definition of what a word is and what characters (in addition to whitespace characters) are considered separators.

In the first item above, "name of an input file" and "name of an output file" are to be understood as follows. Each includes the notion of a path to the terminal name. The path may either be relative to a current folder or be absolute from the top of the file system. It would be bad form for the program to insert an implied sub-path or folder prior to or after what the user supplies as input. Similarly, it would be bad form for the program to supply an implied filename extension such as ".txt" or ".html" after what the user supplies as input. Therefore, the program shall respect the user input as being the complete relative or absolute path as the name of the input file, or the name of the output file, and will not augment the given path in any way, e.g., it will not supply its own filename extension. For example, a reasonable user response for the name of the input file could directly result in the String value "data/gettysburg.txt"; similarly, a reasonable user response for the name of the output file could directly result in the String value "data/gettysburg.html".
