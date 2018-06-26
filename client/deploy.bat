@echo off
echo Reading token from ${projectRootDir}/token.key
set /p token=<../token.key
echo Deploying hosting from cli
firebase deploy --only hosting --token %token%
