Sudoku Generator

Liya Choi
AP Computer Science A  
Java

Description
A Java console application that generates a valid solved 9x9 Sudoku puzzle using constrained random selection. 
Includes an optional playable mode (Part 2) where numbers are removed and the user fills them in.

How To Run
1. Go to jdoodle.com/online-java-compiler
2. Paste SudokuGenerator.java into the editor
3. Click Execute

How the Board is Generated
1. For each row, a pool ArrayList starting with {1-9} is built for each cell
2. Numbers already used in the same row, column, or 3x3 box are removed
3. A random number is picked from what remains
4. If the pool is ever empty, the row is cleared
5. This guarantees a valid solved board every run

Files Included
- SudokuGenerator.java — main source code (Part 1 + Part 2)
- README.md — this file
- DesignDocument.docx — design document
