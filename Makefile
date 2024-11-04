# Define paths
SRC_JAVA=src/main/java
SRC_CPP=src/main/c++
OUT_DIR=out
RESOURCES_DIR=src/resources
HEADERS_DIR=$(SRC_CPP)/headers
JAVA_INCLUDES=-I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux

# Targets

# Build step that generates JNI headers and compiles Java classes
.PHONY: build
build: $(RESOURCES_DIR)/libParticleSystem.so

# Generate JNI headers and compile all Java classes in one step
$(OUT_DIR)/Pack/Particle/Particle.class: $(SRC_JAVA)/Pack/Particle/Particle.java $(SRC_JAVA)/Pack/FieldPoint/FieldPoint.java $(SRC_JAVA)/Pack/Main.java
	mkdir -p $(HEADERS_DIR) $(OUT_DIR)
	javac -h $(HEADERS_DIR) -d $(OUT_DIR) -sourcepath $(SRC_JAVA) $(SRC_JAVA)/Pack/Main.java

# Compile C++ code into a shared library
$(RESOURCES_DIR)/libParticleSystem.so: $(SRC_CPP)/ParticleSystem.cpp $(OUT_DIR)/Pack/Particle/Particle.class $(OUT_DIR)/Pack/FieldPoint/FieldPoint.class
	mkdir -p $(RESOURCES_DIR)
	g++ -I$(HEADERS_DIR) -shared -fPIC -o $(RESOURCES_DIR)/libParticleSystem.so $(JAVA_INCLUDES) $(SRC_CPP)/ParticleSystem.cpp 

# Run the Java program with the native library
.PHONY: run
run: build
	java -cp $(OUT_DIR) -Djava.library.path=$(RESOURCES_DIR) Pack.Main

# Clean generated files
.PHONY: clean
clean:
	rm -rf $(OUT_DIR) $(HEADERS_DIR) $(RESOURCES_DIR)/libParticleSystem.so
	rm -rf target