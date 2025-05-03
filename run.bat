@echo off
javac -cp ".;sqlite-jdbc-3.43.2.0.jar" Main.java TaskManager.java Task.java && java -cp ".;sqlite-jdbc-3.43.2.0.jar" Main
