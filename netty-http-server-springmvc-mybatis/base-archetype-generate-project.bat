@echo off
@echo 1.Create netty-http-server-springmvc-mybatis-archetype
call mvn clean archetype:create-from-project

@echo 2.Install netty-http-server-springmvc-mybatis-archetype
set PROJECT_CONTEXT_PATH=%cd%
echo %PROJECT_CONTEXT_PATH%
cd %PROJECT_CONTEXT_PATH%/target/generated-sources/archetype
call mvn install
pause

cd %PROJECT_CONTEXT_PATH%
cd ../../
echo %cd%

@echo 3.Generate your's project by netty-http-server-springmvc-mybatis-archetype
call mvn -B archetype:generate -DarchetypeCatalog=local -DarchetypeGroupId=com.soho.web.server -DarchetypeArtifactId=netty-http-server-springmvc-mybatis-archetype -DarchetypeVersion=0.1.0-Final -DgroupId=com.platform.common -DartifactId=game-server -Dversion=0.1.0-SNAPSHOT
pause