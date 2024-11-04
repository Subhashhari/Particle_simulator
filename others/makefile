# Define variables
JAVA_FILES = ParticleSimulator.java
HEADER = ParticleSystem.h
SHARED_LIB = libParticleSystem.so

# Get the JAVA_HOME path
JAVA_HOME := $(shell readlink -f $(shell which javac) | sed "s:/bin/javac::")

# Compiler and flags for C++
CXX = g++
CXXFLAGS = -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux -fPIC -shared -lsfml-graphics -lsfml-window -lsfml-system

# Default target
all: compile_java generate_header compile_cpp run

# Compile Java code
compile_java:
	@echo "Compiling Java code..."
	javac $(JAVA_FILES)

# Generate JNI header file
generate_header: compile_java`
	@echo "Generating JNI header file..."
	javac -h . $(JAVA_FILES)
	
# Compile C++ shared library
compile_cpp: generate_header
	@echo "Compiling C++ shared library..."
	$(CXX) -o $(SHARED_LIB) ParticleSystem.cpp $(CXXFLAGS)

# Run the Java program with the shared library
run: compile_cpp
	@echo "Running Java program..."
	LIBGL_ALWAYS_SOFTWARE=1 java -Djava.library.path=. ParticleSimulator

# Clean up compiled files
clean:
	@echo "Cleaning up..."
	rm -f $(HEADER) $(SHARED_LIB) *.class

# PHONY targets to avoid conflicts with files of the same name
.PHONY: all compile_java generate_header compile_cpp run clean
