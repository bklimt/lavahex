
default: all

clean:
	rm build/*.class || true

all:
	mkdir -p build
	javac -d build *.java

run: all
	java -cp build LavaHexClient

serve: all
	java -cp build GameServer

format:
	clang-format -i *.java