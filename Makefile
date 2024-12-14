
all: build run

build:
	mkdir classes/
	find src/ -name "*.java" | xargs javac -d classes/

run:
	java -cp classes/ Main
