# File Manager

A simple File Manager application. It is based on Java Swing and built with Intellij IDEA.

The operations include opening, editing, printing, previewing and searching for files. The preview is available only for text files and images.

## Required
This application requires:
* Java 1.8 or greater

## About
This application has three panels:
* System folders are displayed in the hierarchical tree based on their directory structure in the left panel
* The contents of the current directory are displayed in the center panel
* The contents of the current file (preview) are displayed in the right panel

## Building File Manager

Source code is available from https://github.com/Vivelapaix/FileManager by either cloning or downloading a zip file into `<SOURCE_HOME>`.
* Using IntelliJ IDEA
  #### Opening source code for build
  Using IntelliJ IDEA **File | Open**, select the `<SOURCE_HOME>/FileManager` directory.

  #### Building configuration
  JDK version 1.8 or newer is required for building and developing.

  #### Import Gradle project
  For the first time after cloning or downloading, choose **Event Log** from the main menu, click **Import Gradle project**.
  Check **Use auto-import**, specify **Gradle JVM** and click **OK**.

  #### Running File Manager
  To run application built from source, choose `./FileManager/src/main/java/filemanager/Main.java`, right click on this file,     choose **Run \'Main.main()\'**. 
* Using Gradle
  #### Opening source code for build
  Navigate to `<SOURCE_HOME>/FileManager` directory.
  
  #### Build and test 
  Run `./gradlew build` or run without test `./gradlew build -x test`.
  
  #### Running File Manager
  Run `java -jar build/libs/FileManager-1.0-SNAPSHOT.jar`
