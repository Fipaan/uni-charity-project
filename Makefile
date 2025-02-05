
all: build run

build:
	mkdir -p classes/
	javac -cp "include/*" src/* -d classes/

run:
	java -cp classes/:./include/* Main 
