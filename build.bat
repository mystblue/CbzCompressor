@echo off
rem jar cvmf manifest.mf CbzCompressor.jar src\*.class org META-INF
cd bin
jar cvmf manifest.mf ..\CbzCompressor.jar *.class org META-INF
cd ..
