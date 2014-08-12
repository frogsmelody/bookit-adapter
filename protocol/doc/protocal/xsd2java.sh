#!/bin/sh
xjc -npa -extension -no-header -d ../../src/main/java -p com.derbysoft.bookit.dto All.xsd
echo -e  '\n'
read -n 1 -p "Press any key to continue..."
