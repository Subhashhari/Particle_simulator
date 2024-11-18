// #include <jni.h>
// #include <iostream>
// #include <cmath>
// #include <vector>
// #include "Pack_Particle_Particle.h"
// #include "Pack_ParticleSystem.h"

// using namespace std;

// constexpr float K = 100000000.0f;

// extern "C" {

// // JNI implementation for update method
// JNIEXPORT void JNICALL Java_Pack_Particle_Particle_update(JNIEnv* env, jobject obj) {
//     float dt = 1.0f;

//     jclass particleClass = env->GetObjectClass(obj);

//     // Retrieve mass
//     jmethodID getMassMethod = env->GetMethodID(particleClass, "getMass", "()F");
//     float mass = env->CallFloatMethod(obj, getMassMethod);

//     // Retrieve force as float[]
//     jmethodID getForceMethod = env->GetMethodID(particleClass, "getForce", "()[F");
//     jfloatArray forceArray = (jfloatArray)env->CallObjectMethod(obj, getForceMethod);
//     jfloat* force = env->GetFloatArrayElements(forceArray, nullptr);

//     // Retrieve position as float[]
//     jmethodID getPositionMethod = env->GetMethodID(particleClass, "getPosition", "()[F");
//     jfloatArray positionArray = (jfloatArray)env->CallObjectMethod(obj, getPositionMethod);
//     jfloat* position = env->GetFloatArrayElements(positionArray, nullptr);

//     // Retrieve velocity as float[]
//     jmethodID getVelocityMethod = env->GetMethodID(particleClass, "getVelocity", "()[F");
//     jfloatArray velocityArray = (jfloatArray)env->CallObjectMethod(obj, getVelocityMethod);
//     jfloat* velocity = env->GetFloatArrayElements(velocityArray, nullptr);

//     // Perform calculations
//     position[0] += velocity[0] * dt + (force[0] * dt * dt) / (2.0f * mass);
//     position[1] += velocity[1] * dt + (force[1] * dt * dt) / (2.0f * mass);

//     // Update arrays back in Java object
//     env->ReleaseFloatArrayElements(positionArray, position, 0);
//     env->ReleaseFloatArrayElements(velocityArray, velocity, 0);
//     env->ReleaseFloatArrayElements(forceArray, force, JNI_ABORT);

//     // Call setPosition and setVelocity to update the Java object
//     jmethodID setPositionMethod = env->GetMethodID(particleClass, "setPosition", "([F)V");
//     env->CallVoidMethod(obj, setPositionMethod, positionArray);

//     jmethodID setVelocityMethod = env->GetMethodID(particleClass, "setVelocity", "([F)V");
//     env->CallVoidMethod(obj, setVelocityMethod, velocityArray);

//     // Clean up local references
//     env->DeleteLocalRef(forceArray);
//     env->DeleteLocalRef(positionArray);
//     env->DeleteLocalRef(velocityArray);
// }

// JNIEXPORT void JNICALL Java_Pack_ParticleSystem_setForces(JNIEnv* env, jobject obj) {
//     jclass particleSystemClass = env->GetObjectClass(obj);

//     // Retrieve `getParticles` and `getFieldPoints` methods
//     jmethodID getParticlesMethod = env->GetMethodID(particleSystemClass, "getParticles", "()Ljava/util/Vector;");
//     jmethodID getFieldPointsMethod = env->GetMethodID(particleSystemClass, "getFieldPoints", "()Ljava/util/Vector;");
//     if (!getParticlesMethod || !getFieldPointsMethod) {
//         cerr << "Could not find methods for particles or field points retrieval." << endl;
//         return;
//     }

//     // Get particles and field points objects
//     jobject particles = env->CallObjectMethod(obj, getParticlesMethod);
//     jobject fieldPoints = env->CallObjectMethod(obj, getFieldPointsMethod);

//     // Vector class and methods
//     jclass vectorClass = env->FindClass("java/util/Vector");
//     jmethodID vectorSizeMethod = env->GetMethodID(vectorClass, "size", "()I");
//     jmethodID vectorGetMethod = env->GetMethodID(vectorClass, "get", "(I)Ljava/lang/Object;");

//     // Get sizes
//     jint numParticles = env->CallIntMethod(particles, vectorSizeMethod);
//     jint numFieldPoints = env->CallIntMethod(fieldPoints, vectorSizeMethod);

//     // Loop through each particle
//     for (int i = 0; i < numParticles; i++) {
//         jobject particle = env->CallObjectMethod(particles, vectorGetMethod, i);
//         jclass particleClass = env->GetObjectClass(particle);

//         jmethodID getPositionMethod = env->GetMethodID(particleClass, "getPosition", "()Ljava/util/Vector;");
//         jmethodID getChargeMethod = env->GetMethodID(particleClass, "getCharge", "()F");
//         jmethodID setForceMethod = env->GetMethodID(particleClass, "setForce", "([F)V");

//         if (!getPositionMethod || !getChargeMethod || !setForceMethod) {
//             cerr << "Could not find particle methods." << endl;
//             return;
//         }

//         // Get particle's position and charge
//         jobject particlePosition = env->CallObjectMethod(particle, getPositionMethod);
//         float charge = env->CallFloatMethod(particle, getChargeMethod);

//         // Retrieve x and y components of particle position
//         float px = env->CallFloatMethod(particlePosition, vectorGetMethod, 0);
//         float py = env->CallFloatMethod(particlePosition, vectorGetMethod, 1);

//         // Initialize force
//         float forceX = 0.0f;
//         float forceY = 0.0f;

//         // Loop through each field point
//         for (int j = 0; j < numFieldPoints; j++) {
//             //cout<<"inside loop"<<endl;
//             jobject fieldPoint = env->CallObjectMethod(fieldPoints, vectorGetMethod, j);
//             jclass fieldPointClass = env->GetObjectClass(fieldPoint);

//             jmethodID getFieldPointPosition = env->GetMethodID(fieldPointClass, "getPosition", "()Ljava/util/Vector;");
//             jmethodID getFieldStrengthMethod = env->GetMethodID(fieldPointClass, "getFieldStrength", "()F");
            
//             if (!getFieldPointPosition || !getFieldStrengthMethod) {
//                 cerr << "Could not find field point methods." << endl;
//                 return;
//             }

//             jobject fieldPointPosition = env->CallObjectMethod(fieldPoint, getFieldPointPosition);
//             float fieldStrength = env->CallFloatMethod(fieldPoint, getFieldStrengthMethod);

//             float fx = env->CallFloatMethod(fieldPointPosition, vectorGetMethod, 0);
//             float fy = env->CallFloatMethod(fieldPointPosition, vectorGetMethod, 1);

//             // Calculate distance and direction
//             float dx = fx - px;
//             float dy = fy - py;
//             float distanceSquared = dx * dx + dy * dy;
//             float distance = sqrt(distanceSquared);

//             //if (distance < 0.1f) continue;

//             float forceMagnitude = (charge * fieldStrength * K) / distanceSquared;
//             forceX += forceMagnitude * (dx / distance);
//             forceY += forceMagnitude * (dy / distance);
//         }

//         // Create a Java float array for force
//         jfloat force[2] = {forceX, forceY};
//         jfloatArray jForceArray = env->NewFloatArray(2);
//         env->SetFloatArrayRegion(jForceArray, 0, 2, force);

//         // Set the calculated force on the particle
//         env->CallVoidMethod(particle, setForceMethod, jForceArray);

//         // Clean up local references
//         env->DeleteLocalRef(jForceArray);
//     }
// }

// }


#include <jni.h>
#include <iostream>
#include <cmath>
#include <vector>
#include "Pack_Particle_Particle.h"
#include "Pack_ParticleSystem.h"

using namespace std;

float K = 100000000.0f;

extern "C" {

// Helper function to get a float value from Java Vector<Float>
float getVectorElement(JNIEnv* env, jobject vectorObj, int index) {
    jclass vectorClass = env->GetObjectClass(vectorObj);
    jmethodID getMethod = env->GetMethodID(vectorClass, "get", "(I)Ljava/lang/Object;");
    jobject floatObj = env->CallObjectMethod(vectorObj, getMethod, index);
    jclass floatClass = env->FindClass("java/lang/Float");
    jmethodID floatValueMethod = env->GetMethodID(floatClass, "floatValue", "()F");
    float value = env->CallFloatMethod(floatObj, floatValueMethod);
    env->DeleteLocalRef(floatObj);
    return value;
}

// Helper function to set values in Java Vector<Float>
void setVectorElement(JNIEnv* env, jobject vectorObj, int index, float value) {
    jclass vectorClass = env->GetObjectClass(vectorObj);
    jmethodID setMethod = env->GetMethodID(vectorClass, "set", "(ILjava/lang/Object;)Ljava/lang/Object;");
    jclass floatClass = env->FindClass("java/lang/Float");
    jobject floatObj = env->NewObject(floatClass, env->GetMethodID(floatClass, "<init>", "(F)V"), value);
    env->CallObjectMethod(vectorObj, setMethod, index, floatObj);
    env->DeleteLocalRef(floatObj);
}

// JNI implementation for update method
JNIEXPORT void JNICALL Java_Pack_Particle_Particle_update(JNIEnv* env, jobject obj) {
    float dt = 1.0f;

    jclass particleClass = env->GetObjectClass(obj);

    // Retrieve mass
    jmethodID getMassMethod = env->GetMethodID(particleClass, "getMass", "()F");
    float mass = env->CallFloatMethod(obj, getMassMethod);

    // Retrieve force as Vector<Float>
    jmethodID getForceMethod = env->GetMethodID(particleClass, "getForce", "()Ljava/util/Vector;");
    jobject forceVector = env->CallObjectMethod(obj, getForceMethod);

    // Retrieve position as Vector<Float>
    jmethodID getPositionMethod = env->GetMethodID(particleClass, "getPosition", "()Ljava/util/Vector;");
    jobject positionVector = env->CallObjectMethod(obj, getPositionMethod);

    // Retrieve velocity as Vector<Float>
    jmethodID getVelocityMethod = env->GetMethodID(particleClass, "getVelocity", "()Ljava/util/Vector;");
    jobject velocityVector = env->CallObjectMethod(obj, getVelocityMethod);

    jmethodID setPositionMethod = env->GetMethodID(particleClass, "setPosition", "([F)V");
    jmethodID setVelocityMethod = env->GetMethodID(particleClass, "setVelocity", "([F)V");

    // Perform calculations
    float positionX = getVectorElement(env, positionVector, 0);
    float positionY = getVectorElement(env, positionVector, 1);
    float velocityX = getVectorElement(env, velocityVector, 0);
    float velocityY = getVectorElement(env, velocityVector, 1);
    float forceX = getVectorElement(env, forceVector, 0);
    float forceY = getVectorElement(env, forceVector, 1);

    // Update position based on force and velocity
    positionX += velocityX * dt + (forceX * dt * dt) / (2.0f * mass);
    positionY += velocityY * dt + (forceY * dt * dt) / (2.0f * mass);

    // Set updated position back in Java object
    // setVectorElement(env, positionVector, 0, positionX);
    // setVectorElement(env, positionVector, 1, positionY);

    float finalPos[2] = { positionX, positionY };
    jfloatArray jPositionArray = env->NewFloatArray(2);
    env->SetFloatArrayRegion(jPositionArray, 0, 2, finalPos);
    env->CallVoidMethod(obj, setPositionMethod, jPositionArray);
    env->DeleteLocalRef(jPositionArray);

    velocityX += forceX * dt;
    velocityY += forceY * dt;

    float finalVel[2] = { velocityX, velocityY };
    jfloatArray jVelocityArray = env->NewFloatArray(2);
    env->SetFloatArrayRegion(jVelocityArray, 0, 2, finalVel);
    env->CallVoidMethod(obj, setVelocityMethod, jVelocityArray);
    env->DeleteLocalRef(jVelocityArray);

    // Clean up local references
    env->DeleteLocalRef(forceVector);
    env->DeleteLocalRef(positionVector);
    env->DeleteLocalRef(velocityVector);

    // Update velocity based on force

}

JNIEXPORT void JNICALL Java_Pack_ParticleSystem_setForces(JNIEnv* env, jobject obj) {
    jclass particleSystemClass = env->GetObjectClass(obj);

    // Retrieve `getParticles` and `getFieldPoints` methods
    jmethodID getParticlesMethod = env->GetMethodID(particleSystemClass, "getParticles", "()Ljava/util/Vector;");
    jmethodID getFieldPointsMethod = env->GetMethodID(particleSystemClass, "getFieldPoints", "()Ljava/util/Vector;");

    jobject particles = env->CallObjectMethod(obj, getParticlesMethod);
    jobject fieldPoints = env->CallObjectMethod(obj, getFieldPointsMethod);

    jclass vectorClass = env->FindClass("java/util/Vector");
    jmethodID vectorSizeMethod = env->GetMethodID(vectorClass, "size", "()I");
    jmethodID vectorGetMethod = env->GetMethodID(vectorClass, "get", "(I)Ljava/lang/Object;");

    jint numParticles = env->CallIntMethod(particles, vectorSizeMethod);
    jint numFieldPoints = env->CallIntMethod(fieldPoints, vectorSizeMethod);

    for (int i = 0; i < numParticles; i++) {
        jobject particle = env->CallObjectMethod(particles, vectorGetMethod, i);
        jclass particleClass = env->GetObjectClass(particle);

        jmethodID getPositionMethod = env->GetMethodID(particleClass, "getPosition", "()Ljava/util/Vector;");
        jmethodID getChargeMethod = env->GetMethodID(particleClass, "getCharge", "()F");
        jmethodID setForceMethod = env->GetMethodID(particleClass, "setForce", "([F)V");

        jobject particlePosition = env->CallObjectMethod(particle, getPositionMethod);
        float charge = env->CallFloatMethod(particle, getChargeMethod);

        float px = getVectorElement(env, particlePosition, 0);
        float py = getVectorElement(env, particlePosition, 1);

        float forceX = 0.0f;
        float forceY = 0.0f;

        for (int j = 0; j < numFieldPoints; j++) {
            jobject fieldPoint = env->CallObjectMethod(fieldPoints, vectorGetMethod, j);
            jclass fieldPointClass = env->GetObjectClass(fieldPoint);

            jmethodID getFieldPointPosition = env->GetMethodID(fieldPointClass, "getPosition", "()Ljava/util/Vector;");
            jmethodID getFieldStrengthMethod = env->GetMethodID(fieldPointClass, "getFieldStrength", "()F");

            jobject fieldPointPosition = env->CallObjectMethod(fieldPoint, getFieldPointPosition);
            float fieldStrength = env->CallFloatMethod(fieldPoint, getFieldStrengthMethod);

            float fx = getVectorElement(env, fieldPointPosition, 0);
            float fy = getVectorElement(env, fieldPointPosition, 1);

            float dx = fx - px;
            float dy = fy - py;
            float distanceSquared = dx * dx + dy * dy;
            float distance = sqrt(distanceSquared);

            float forceMagnitude = (charge * fieldStrength * K) / distanceSquared;
            forceX += forceMagnitude * (dx / distance);
            forceY += forceMagnitude * (dy / distance);

            env->DeleteLocalRef(fieldPointPosition);
            env->DeleteLocalRef(fieldPoint);
        }

        float force[2] = { forceX, forceY };
        jfloatArray jForceArray = env->NewFloatArray(2);
        env->SetFloatArrayRegion(jForceArray, 0, 2, force);

        env->CallVoidMethod(particle, setForceMethod, jForceArray);

        env->DeleteLocalRef(jForceArray);
        env->DeleteLocalRef(particlePosition);
        env->DeleteLocalRef(particle);
    }
    env->DeleteLocalRef(particles);
    env->DeleteLocalRef(fieldPoints);
}

}