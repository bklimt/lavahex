
default: all

clean:
	rm build/*.class || true

all:
	mkdir -p build
	javac -d build *.java

run: all
	java -cp build LavaHexClient

lint:
	clang-format -i *.java