# A simple and lightweight program to play sound effects on streams

## How to add new sound effects?
1. Download the project
2. Paste your .mp3 into src/main/resources/sounds
3. Rename your file to match the program pattern. Example: 14- Pedro pe.mp3

** The program will display everything between - and .mp3 as the button name.

## How to add an icon to my effect?
1. Download the project
2. Paste your 50x50 icon into src/main/resources/images
3. Rename your file to match the sound effect. Example: 14- Pedro pe.png

## How to convert this program into an executable .jar?
1. Download an IDE like Eclipse or IntelliJ
2. For Eclipse:
   - File > Export > Runnable JAR file > set Launch configuration to main - AudioEffects and Library handling to Extract
   - Requires libraries into generated JAR > Finish.
3. To run the program:
   3.1 Open a terminal into the folder that contains the .jar file.
   3.2 Write:
       ```
       java -jar AudioEffect.jar
       ```
       ** AudioEffect must be replaced with the name of the .jar file that you have exported in your IDE.
   3.3 To close the program, just press alt + f4 on the window or close your terminal.
