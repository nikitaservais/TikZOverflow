## Configure Intellij to java-8:
1. Download sdk8 and install it.

2. Intellij->File->Project->Project SDK->New: Select JDK8

3. Intellij->File->Project->Project Languaga level: Default

4. Intellij->Run->Edit configurations...->Delete VM options

5. Intellij->Run->Edit configurations...->JRE->Default(1.8 - SDK of'group6' module)

## To execute JAR file in terminal set up java 1.8 as default:

=> On arch: sudo archlinux-java set java-8-jdk/jre

=> Other linux: sudo update-alternatives --config java  -> select java-8

=> On mac: export JAVA_HOME=\`/usr/libexec/java_home -v 1.8\`

=> On Windows:

        To set the PATH variable permanently, add the full path of the jdk1.8.0\bin directory to the PATH variable. Typically, this full path looks something like C:\Program Files\Java\jdk1.8.0\bin. Set the PATH variable as follows on Microsoft Windows:
        
           1. Click Start, then Control Panel, then System.
        
           2. Click Advanced, then Environment Variables.
        
           3. Add the location of the bin folder of the JDK installation to the PATH variable in System Variables. The following is a typical value for the PATH variable:
        
           4. C:\WINDOWS\system32;C:\WINDOWS;C:\Program Files\Java\jdk1.8.0\bin
- Help: https://docs.oracle.com/javase/8/docs/technotes/guides/install/windows_jdk_install.html

#### To check if default in mac/linux:
 => type in Terminal "java -version", should print: java version "1.8.0_241"

#### To check if default on windows:

![alt text](https://media.giphy.com/media/ma7VlDSlty3EA/giphy.gif)