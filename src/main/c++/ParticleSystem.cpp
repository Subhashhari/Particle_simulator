// File path: src/main/cpp/ParticleSystem.cpp

#include <jni.h>
#include <iostream>
#include <cmath>
#include "Pack_Particle_Particle.h"

using namespace std;

// JNI method implementations

extern "C" {

// JNI implementation for update method
JNIEXPORT void JNICALL Java_Pack_Particle_Particle_update(JNIEnv* env, jobject obj) {
    //get particle from java
    jclass particleClass = env->GetObjectClass(obj);

    // Get the method ID for the getMass() method
    jmethodID getMassMethod = env->GetMethodID(particleClass, "getMass", "()F");
    if (getMassMethod == nullptr) {
        cerr << "Could not find getMass() method" << endl;
        return;
    }

    // Call getMass() and retrieve the mass
    float mass = env->CallFloatMethod(obj, getMassMethod);

    // Print the mass
    cout << "Mass: " << mass << endl;
}

// JNI implementation for applyForce method
// JNIEXPORT void JNICALL Particle_applyForce(JNIEnv* env, jobject obj, jfloat fx, jfloat fy, jfloat fz) {
//     particle.applyForce(fx, fy, fz);
// }

// // JNI implementation for setProperties method
// JNIEXPORT void JNICALL Java_com_project_simulation_Particle_setProperties(JNIEnv* env, jobject obj, jfloat mass, jfloat charge, jfloat size, jint lifespan) {
//     particle.setProperties(mass, charge, size, lifespan);
// }

}
