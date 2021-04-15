JAVAC=/usr/bin/javac
.SUFFIXES: .java .class
SRCDIR=src
BINDIR=bin
DOCDIR=docs

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES=  ReadThread.class Client.class Server.class GUI.class
        
CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)
default: $(CLASS_FILES)

clean:
	rm $(BINDIR)/*.class

docs:
	javadoc -cp  ${BINDIR} -d ${DOCDIR} ${SRCDIR}/*.java

cleandocs:
	rm -r docs/*

runClient:
	java -cp bin GUI

runServer:
	java -cp bin Server


