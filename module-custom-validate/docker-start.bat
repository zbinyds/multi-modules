@echo off
setlocal

REM 设置镜像名称
set IMAGE_NAME=com.zbinyds/validate
set CONTAINER_NAME=validate

REM 检查容器是否正在运行
for /f "tokens=*" %%i in ('docker ps -aq --filter "name=%CONTAINER_NAME%"') do (
    echo Container already exists, stopping and deleting container...
    docker stop %%i && docker rm %%i
)

REM 检查镜像是否存在
for /f "tokens=*" %%i in ('docker images -q %IMAGE_NAME%') do (
    echo The image already exists and is being deleted...
    docker rmi -f %%i
)

REM 构建新的镜像
echo Building image now...
docker build -t %IMAGE_NAME% -f Dockerfile .

REM 运行新的容器
echo Running a new container...
docker run -d -p 7500:7500 --name %CONTAINER_NAME% %IMAGE_NAME%

endlocal