
all: build run

build:
	mkdir -p classes/
	find src/ -name "*.java" | xargs javac -d classes/ -cp ./include/postgresql-42.7.4.jar

run:
	java -cp classes/:./include/postgresql-42.7.4.jar Main 
