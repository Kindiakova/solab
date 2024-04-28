all: run

clean:
	rm -f out/CountPalindromes.jar

out/CountPalindromes.jar: out/parcs.jar src/CountPalindromes.java
	@mkdir -p temp
	@javac -cp out/parcs.jar -d temp src/CountPalindromes.java
	@jar cf out/CountPalindromes.jar -C temp .
	@rm -rf temp/

build: out/CountPalindromes.jar

run: out/CountPalindromes.jar
	@cd out && java -cp 'parcs.jar:CountPalindromes.jar' CountPalindromes $(WORKERS)
