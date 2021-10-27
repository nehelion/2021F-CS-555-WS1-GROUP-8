@ECHO OFF

ECHO.
ECHO Compiling...
javac -classpath junit-jupiter-api-5.8.1.jar "main_project.java" "Family.java" "Individual.java" "Utils.java" "tests.java"
ECHO done!
ECHO.

ECHO Executing:
ECHO.
java main_project

set /p DUMMY=Hit ENTER to continue...

:end
ECHO ON