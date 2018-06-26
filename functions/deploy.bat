@echo off
echo Reading token from ${projectRootDir}/token.key
set /p token=<../token.key
echo Deploying functions from cli
firebase deploy --only functions --token %token%
