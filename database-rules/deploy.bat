@echo off
echo Reading token from ${projectRootDir}/token.key
set /p token=<../token.key
echo Deploying database rules from cli
firebase deploy --only database --token %token%
