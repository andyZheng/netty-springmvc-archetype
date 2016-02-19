@set PATH=%JAVA_HOME%\bin;%PATH%;
@java -cp "testlibs\*;%H2DRIVERS%;%CLASSPATH%" org.h2.tools.Console %*
@if errorlevel 1 pause