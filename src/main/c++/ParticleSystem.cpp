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

//     // Retrieve getParticles and getFieldPoints methods
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
#include <random>
#include "Pack_Particle_Particle.h"
#include "Pack_ParticleSystem.h"

using namespace std;

float K = 100000000.0f;

const double PI = 3.14159265358979323846;

double randomDouble(double a, double b) {
    // Create a random number generator
    random_device rd;  // Initialize random device
    mt19937 generator(rd());  // Use the random device to seed the generator
    uniform_real_distribution<double> distribution(a, b);  // Define the range

    return distribution(generator);  // Generate a random double in the range [a, b]
}

extern "C" {


vector<float> getCppVectorFromJavaVector(JNIEnv* env, jobject javaVector) {
    std::vector<float> cppVector;

    // Find the Java Vector class and required methods
    jclass vectorClass = env->FindClass("java/util/Vector");
    jmethodID sizeMethod = env->GetMethodID(vectorClass, "size", "()I");
    jmethodID getMethod = env->GetMethodID(vectorClass, "get", "(I)Ljava/lang/Object;");

    // Get the size of the Java Vector
    jint vectorSize = env->CallIntMethod(javaVector, sizeMethod);

    // Iterate through the Java Vector and extract each float element
    for (int i = 0; i < vectorSize; ++i) {
        jobject floatObj = env->CallObjectMethod(javaVector, getMethod, i);

        // Cast the Object to Float and get the float value
        jclass floatClass = env->FindClass("java/lang/Float");
        jmethodID floatValueMethod = env->GetMethodID(floatClass, "floatValue", "()F");
        jfloat value = env->CallFloatMethod(floatObj, floatValueMethod);

        // Add the float value to the C++ vector
        cppVector.push_back(value);

        // Clean up local references
        env->DeleteLocalRef(floatObj);
    }

    // Return the populated C++ vector
    return cppVector;
}



jobject getJavaVectorFromCppVector(JNIEnv* env, const std::vector<float>& cppVector) {
    // Find the Java Vector and Float classes
    jclass vectorClass = env->FindClass("java/util/Vector");
    jclass floatClass = env->FindClass("java/lang/Float");

    // Get the constructors and method IDs
    jmethodID vectorConstructor = env->GetMethodID(vectorClass, "<init>", "()V");
    jmethodID addMethod = env->GetMethodID(vectorClass, "add", "(Ljava/lang/Object;)Z");
    jmethodID floatConstructor = env->GetMethodID(floatClass, "<init>", "(F)V");

    // Create a new Java Vector instance
    jobject javaVector = env->NewObject(vectorClass, vectorConstructor);

    // Populate the Java Vector with Float objects
    for (float value : cppVector) {
        // Create a new Float object for each value in the C++ vector
        jobject floatObj = env->NewObject(floatClass, floatConstructor, value);
        
        // Add the Float object to the Java Vector
        env->CallBooleanMethod(javaVector, addMethod, floatObj);

        // Clean up the local reference to the Float object
        env->DeleteLocalRef(floatObj);
    }

    // Return the populated Java Vector
    return javaVector;
}


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

    // Retrieve getParticles and getFieldPoints methods
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


JNIEXPORT jobjectArray JNICALL Java_Pack_Emitter_Emitter_getVelocities(JNIEnv* env, jobject obj) {
    jclass emitterClass = env->GetObjectClass(obj);
    jmethodID getAngleMethod = env->GetMethodID(emitterClass, "getAngle", "()F");
    if (getAngleMethod == nullptr) {
        cerr << "Could not find getAngle() method" << endl;
        return nullptr;
    }

    float angle = env->CallFloatMethod(obj, getAngleMethod);
    cout << "Angle: " << angle << endl;

    jmethodID getSpeedMethod = env->GetMethodID(emitterClass, "getSpeed", "()F");
    if (getSpeedMethod == nullptr) {
        cerr << "Could not find getSpeed() method" << endl;
        return nullptr;
    }

    float speed = env->CallFloatMethod(obj, getSpeedMethod);
    cout << "Speed: " << speed << endl;

    jmethodID getSpreadMethod = env->GetMethodID(emitterClass, "getSpread", "()F");
    if (getSpreadMethod == nullptr) {
        cerr << "Could not find getSpread() method" << endl;
        return nullptr;
    }

    float spread = env->CallFloatMethod(obj, getSpreadMethod);
    cout << "Spread: " << spread << endl;

    vector<vector<float>> velocities;

    for (int i=0 ; i<100; i++) 
    {
        float angU = angle + spread/2;
        float angL = angle - spread/2;
        float ang = randomDouble(angL, angU);
        float vx = cos(ang) * speed;
        float vy = sin(ang) * speed;
        velocities.push_back({vx, vy});
    }

    // Determine the number of coordinates
    jsize numVelocities = velocities.size();

    // Create a 2D Java array (jobjectArray of jfloatArray)
    jobjectArray jVelocitiesArray = env->NewObjectArray(numVelocities, env->FindClass("[F"), nullptr);

    // Populate the jobjectArray with jfloatArrays
    for (jsize i = 0; i < numVelocities; ++i) {
        // Convert each C++ vector to a jfloatArray
        jfloatArray jInnerArray = env->NewFloatArray(2);
        env->SetFloatArrayRegion(jInnerArray, 0, 2, velocities[i].data());

        // Set the jfloatArray in the main jobjectArray
        env->SetObjectArrayElement(jVelocitiesArray, i, jInnerArray);

        // Delete the local reference to the inner array to prevent memory leaks
        env->DeleteLocalRef(jInnerArray);
    }

    // Return the 2D Java array
    return jVelocitiesArray;
}


JNIEXPORT void JNICALL Java_Pack_Emitter_OscillatingEmitter_updateEmitter(JNIEnv* env, jobject obj) {
    jclass emitterClass = env->GetObjectClass(obj);
    jmethodID getAmplitudeMethod = env->GetMethodID(emitterClass, "getAmplitude", "()F");
    if (getAmplitudeMethod == nullptr) {
        cerr << "Could not find getMass() method" << endl;
        return;
    }

    float A = env->CallFloatMethod(obj, getAmplitudeMethod);
    cout << "Amplitude: " << A << endl;


    jmethodID getFrequencyMethod = env->GetMethodID(emitterClass, "getFrequecy", "()F");
    if (getFrequencyMethod == nullptr) {
        cerr << "Could not find getMass() method" << endl;
        return;
    }

    float f = env->CallFloatMethod(obj, getFrequencyMethod);
    cout << "Freq: " << f << endl;


/*    jmethodID getVelocityMethod = env->GetMethodID(emitterClass, "getVelocity", "()F");
    if (getVelocityMethod == nullptr) {
        cerr << "Could not find getMass() method" << endl;
        return;
    }

    vector<float> velocity = env->CallFloatMethod(obj, getVelocityMethod);*/


    jmethodID getThetaMethod = env->GetMethodID(emitterClass, "getTheta", "()F");
    if (getThetaMethod == nullptr) {
        cerr << "Could not find getMass() method" << endl;
        return;
    }

    float theta = env->CallFloatMethod(obj, getThetaMethod);
    cout << "Theta: " << theta << endl;

    // Get the getPosition() method ID
    jmethodID getPositionMethod = env->GetMethodID(emitterClass, "getPosition", "()Ljava/util/Vector;");
    if (getPositionMethod == nullptr) {
        std::cerr << "Could not find getPosition() method" << std::endl;
        return;
    }

    // Call getPosition() to get the Java Vector<Float>
    jobject positionVector = env->CallObjectMethod(obj, getPositionMethod);

    // Convert the Java Vector<Float> to a C++ std::vector<float>
    vector<float> cppPositionVector = getCppVectorFromJavaVector(env, positionVector);


    float vy = A*2*PI*f*sin(theta);
    cppPositionVector[1] += vy;

    float newTheta = theta + 2*PI*f;

    jmethodID setThetaMethod = env->GetMethodID(emitterClass, "setTheta", "()F");
    if (getThetaMethod == nullptr) {
        cerr << "Could not find getMass() method" << endl;
        return;
    }

    env->CallVoidMethod(obj, setThetaMethod, newTheta);


    // Convert the C++ vector to a Java Vector<Float>
    jobject javaPositionVector = getJavaVectorFromCppVector(env, cppPositionVector);

    // Call setPosition(Vector<Float>) on the Emitter object
    jclass oscillatingemitterClass = env->GetObjectClass(obj);
    jmethodID setPositionMethod = env->GetMethodID(oscillatingemitterClass, "setPosition", "(Ljava/util/Vector;)V");

    if (setPositionMethod != nullptr) {
        env->CallVoidMethod(obj, setPositionMethod, javaPositionVector);
    } else {
        std::cerr << "Could not find setPosition() method" << std::endl;
    }




    
}
}