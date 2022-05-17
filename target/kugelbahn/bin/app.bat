@echo off
set JLINK_VM_OPTIONS=
set DIR=%~dp0
"%DIR%\java" %JLINK_VM_OPTIONS% -m de.celineevelyn.kugelbahn/de.celineevelyn.kugelbahn.Main %*
