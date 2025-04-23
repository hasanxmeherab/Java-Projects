@echo off
set CLASSPATH=.;lib/mysql-connector-j-9.3.0.jar

echo Compiling Java files...
javac -cp "%CLASSPATH%" *.java

if %errorlevel% neq 0 (
    echo.
    echo ❌ Compilation failed.
    pause
    exit /b
)

echo.
echo ✅ Compilation successful. Running the program...
echo.
java -cp "%CLASSPATH%" Main

pause
