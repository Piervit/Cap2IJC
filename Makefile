CAP2IJC_java=$(wildcard net/sourceforge/javacardtools/Cap2IJC/*.java)
CAP2IJC_class=$(subst .java,.class, $(CAP2IJC_java))

all: Cap2IJC.jar

%.class: %.java
	javac $^


Cap2IJC.jar: $(CAP2IJC_class)
	jar cfm $@ manifest.txt $^


clean:
	rm -f Cap2IJC.jar
	rm -f $(CAP2IJC_class)
