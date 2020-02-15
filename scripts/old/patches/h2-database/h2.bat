@ECHO off
@rem java -cp "h2-1.4.199.jar;%H2DRIVERS%;%CLASSPATH%" org.h2.tools.Console -webPort 9090 %*
@SET ENV=D3
@IF /I "%1" NEQ "" (SET ENV=%1)
@ECHO ENV: %ENV%
@java -cp "h2-1.4.199.jar;%H2DRIVERS%;%CLASSPATH%" org.h2.tools.Console -tcp -tcpPort 65123 -tcpAllowOthers -web -webPort 9090 -webAllowOthers -baseDir C:/Users/Administrator/apps/h2-billprod-merge-layer/database/%ENV%/  %*
@if errorlevel 1 pause