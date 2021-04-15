
# Makefile: SCTMIC015

JAVAC = /usr/bin/javac
.SUFFIXES: .java .class

SRCDIR=src
BINDIR=bin
DOCDIR=doc

$(BINDIR)/%.class:$(SRCDIR)/%.java
	$(JAVAC) -d $(BINDIR)/ -cp $(BINDIR) $<

CLASSES= Client.class ReceiveThread.class GUI.class User.class Server.class

CLASS_FILES=$(CLASSES:%.class=$(BINDIR)/%.class)

default: $(CLASS_FILES)
	
docs:
	javadoc -d $(DOCDIR) $(SRCDIR)/*.java
clean:
	rm $(BINDIR)/*.class
run:
	java -cp bin ReceiveThread
run1:
	java -cp bin Client
run2: 
	java -cp bin GUI
run3:
	java -cp bin User
run4:
	java -cp bin Server
cleandocs:
	rm -r docs/*
runClient:
	java -cp bin GUI
runServer:
	java -cp bin Server
