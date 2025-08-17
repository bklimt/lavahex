
default: all

clean:
	rm build/*.class || true

all:
	mkdir -p build
	javac -d build *.java

run: all
	java -cp build Client

lint:
	clang-format -i *.java