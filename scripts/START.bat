@echo off
"c:\_P\_tools\jdk1.8.0_60\win64\bin\java.exe" "-Djava.library.path=%cd%" -jar pressure-measuring-cells-1.0-SNAPSHOT-jar-with-dependencies.jar
:set /p id=Enter ID:
:echo %id%
set /p exit=Press any key to exit...