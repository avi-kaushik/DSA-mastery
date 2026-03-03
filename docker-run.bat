@echo off

echo Building Docker image...
docker build -t dsa-mastery .

echo Removing old container if exists...
docker rm -f dsa-mastery 2>nul

echo Starting container...
docker run -dit ^
  --name dsa-mastery ^
  -w /code ^
  -v "%cd%":/code ^
  dsa-mastery ^
  sleep infinity

echo Done! Container is running.
pause
