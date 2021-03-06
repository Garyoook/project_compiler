# Sample Makefile for the WACC Compiler lab: edit this to build your own comiler
# Locations

ANTLR_DIR	:= antlr_config
SOURCE_DIR	:= src/main/
OUTPUT_DIR	:= bin

# Tools

ANTLR	:= antlrBuild
FIND	:= find
RM	:= rm -rf
MKDIR	:= mkdir -p
JAVA	:= java
JAVAC	:= javac

JFLAGS	:= -sourcepath $(SOURCE_DIR) -d $(OUTPUT_DIR) -cp lib/antlr-4.7-complete.jar 

# the make rules

all: rules

# runs the antlr build script then attempts to compile all .java files within src
rules:
	mvn compile

clean:
	mvn clean
	$(RM) rules $(OUTPUT_DIR) $(SOURCE_DIR)/antlr

.PHONY: all rules clean


