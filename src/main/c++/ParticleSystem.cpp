#include <jni.h>
#include <iostream>
#include <cmath>
#include <vector>
#include "Pack_Particle_Particle.h"
#include "Pack_ParticleSystem.h"

using namespace std;

constexpr float K = 1.0f;

extern "C" {

// JNI implementation for update method
JNIEXPORT void JNICALL Java_Pack_Particle_Particle_update(JNIEnv* env, jobject obj) {
    jclass particleClass = env->GetObjectClass(obj);
    jmethodID getMassMethod = env->GetMethodID(particleClass, "getMass", "()F");
    if (getMassMethod == nullptr) {
        cerr << "Could not find getMass() method" << endl;
        return;
    }

    float mass = env->CallFloatMethod(obj, getMassMethod);
    cout << "Mass: " << mass << endl;
}

JNIEXPORT void JNICALL Java_Pack_ParticleSystem_setForces(JNIEnv* env, jobject obj) {
    jclass particleSystemClass = env->GetObjectClass(obj);

    // Retrieve `getParticles` and `getFieldPoints` methods
    jmethodID getParticlesMethod = env->GetMethodID(particleSystemClass, "getParticles", "()Ljava/util/Vector;");
    jmethodID getFieldPointsMethod = env->GetMethodID(particleSystemClass, "getFieldPoints", "()Ljava/util/Vector;");
    if (!getParticlesMethod || !getFieldPointsMethod) {
        cerr << "Could not find methods for particles or field points retrieval." << endl;
        return;
    }

    // Get particles and field points objects
    jobject particles = env->CallObjectMethod(obj, getParticlesMethod);
    jobject fieldPoints = env->CallObjectMethod(obj, getFieldPointsMethod);

    // Vector class and methods
    jclass vectorClass = env->FindClass("java/util/Vector");
    jmethodID vectorSizeMethod = env->GetMethodID(vectorClass, "size", "()I");
    jmethodID vectorGetMethod = env->GetMethodID(vectorClass, "get", "(I)Ljava/lang/Object;");

    // Get sizes
    jint numParticles = env->CallIntMethod(particles, vectorSizeMethod);
    jint numFieldPoints = env->CallIntMethod(fieldPoints, vectorSizeMethod);

    // Loop through each particle
    for (int i = 0; i < numParticles; i++) {
        jobject particle = env->CallObjectMethod(particles, vectorGetMethod, i);
        jclass particleClass = env->GetObjectClass(particle);

        jmethodID getPositionMethod = env->GetMethodID(particleClass, "getPosition", "()Ljava/util/Vector;");
        jmethodID getChargeMethod = env->GetMethodID(particleClass, "getCharge", "()F");
        jmethodID setForceMethod = env->GetMethodID(particleClass, "setForce", "(Ljava/util/Vector;)V");

        if (!getPositionMethod || !getChargeMethod || !setForceMethod) {
            cerr << "Could not find particle methods." << endl;
            return;
        }

        // Get particle's position and charge
        jobject particlePosition = env->CallObjectMethod(particle, getPositionMethod);
        float charge = env->CallFloatMethod(particle, getChargeMethod);

        // Retrieve x and y components of particle position
        float px = env->CallFloatMethod(particlePosition, vectorGetMethod, 0);
        float py = env->CallFloatMethod(particlePosition, vectorGetMethod, 1);

        // Initialize force
        float forceX = 0.0f;
        float forceY = 0.0f;

        // Loop through each field point
        for (int j = 0; j < numFieldPoints; j++) {
            jobject fieldPoint = env->CallObjectMethod(fieldPoints, vectorGetMethod, j);
            jclass fieldPointClass = env->GetObjectClass(fieldPoint);

            jmethodID getFieldPointPosition = env->GetMethodID(fieldPointClass, "getPosition", "()Ljava/util/Vector;");
            jmethodID getFieldStrengthMethod = env->GetMethodID(fieldPointClass, "getFieldStrength", "()F");
            
            if (!getFieldPointPosition || !getFieldStrengthMethod) {
                cerr << "Could not find field point methods." << endl;
                return;
            }

            jobject fieldPointPosition = env->CallObjectMethod(fieldPoint, getFieldPointPosition);
            float fieldStrength = env->CallFloatMethod(fieldPoint, getFieldStrengthMethod);

            float fx = env->CallFloatMethod(fieldPointPosition, vectorGetMethod, 0);
            float fy = env->CallFloatMethod(fieldPointPosition, vectorGetMethod, 1);

            // Calculate distance and direction
            float dx = fx - px;
            float dy = fy - py;
            float distanceSquared = dx * dx + dy * dy;
            float distance = sqrt(distanceSquared);

            if (distance < 0.1f) continue;

            float forceMagnitude = (charge * fieldStrength * K) / distanceSquared;
            forceX += forceMagnitude * (dx / distance);
            forceY += forceMagnitude * (dy / distance);
        }

        // Create a Java float array for force
        jfloat force[2] = {forceX, forceY};
        jfloatArray jForceArray = env->NewFloatArray(2);
        env->SetFloatArrayRegion(jForceArray, 0, 2, force);

        // Set the calculated force on the particle
        env->CallVoidMethod(particle, setForceMethod, jForceArray);

        // Clean up local references
        env->DeleteLocalRef(jForceArray);
    }
}
}
