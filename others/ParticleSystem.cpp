#include <SFML/Graphics.hpp>
#include <vector>
#include <cstdlib>
#include <cmath>
#include <jni.h>  // Include JNI header

class Particle {
public:
    sf::CircleShape shape;
    sf::Vector2f velocity;

    Particle(float x, float y, const sf::Vector2f& vel) {
        shape.setRadius(1.0f);
        shape.setFillColor(sf::Color::White);
        shape.setPosition(x, y);
        velocity = vel;
    }

    void update() {
        shape.move(velocity);
    }
};

class ParticleSystem {
public:
    std::vector<Particle> particles;
    std::vector<sf::Vector2f> attractors; // Store multiple attractors (fields)
    std::vector<float> fieldStrengths;    // Store the strength of each attractor

    ParticleSystem() {}

    void addAttractor(float x, float y, float strength) {
        attractors.emplace_back(x, y);
        fieldStrengths.push_back(strength);
    }

    void emit(float x, float y, const sf::Vector2f& velocity) {
        particles.emplace_back(x, y, velocity);
    }

    void update() {
        for (auto it = particles.begin(); it != particles.end(); ) {
            for (size_t i = 0; i < attractors.size(); ++i) {
                sf::Vector2f direction = attractors[i] - it->shape.getPosition();
                float distance = std::sqrt(direction.x * direction.x + direction.y * direction.y);
                if (distance > 0.1f) { // Avoid division by zero
                    float force = fieldStrengths[i] / (distance * distance); // F = K/d^2
                    direction /= distance; // Normalize
                    it->velocity += direction * force; // Apply force from each field
                }
            }

            it->update();

            // Check if the particle is outside the window
            if (it->shape.getPosition().x < 0 || it->shape.getPosition().x > 800 ||
                it->shape.getPosition().y < 0 || it->shape.getPosition().y > 600) {
                it = particles.erase(it); // Remove the particle
            } else {
                ++it; // Move to the next particle
            }
        }
    }
};

// JNI function definition
extern "C" {
    JNIEXPORT void JNICALL Java_ParticleSimulator_runParticleSystem(JNIEnv *, jobject) {
        srand(static_cast<unsigned int>(time(nullptr))); // Seed for random number generation
        sf::RenderWindow window(sf::VideoMode(800, 600), "Draggable Attractors");

        ParticleSystem particleSystem;

        // Add multiple attractors (fields)
        particleSystem.addAttractor(400, 300, 50.0f); // Add one attractor at (400, 300) with strength 50.0f
        particleSystem.addAttractor(200, 150, -50.0f); // Add another attractor at (200, 150) with strength -50.0f

        // Store the attractor dots visually
        std::vector<sf::CircleShape> attractorDots;
        for (size_t i = 0; i < particleSystem.attractors.size(); ++i) {
            sf::CircleShape attractorDot(10.0f); // Create a visual dot for the attractor
            attractorDot.setFillColor(sf::Color::Red);
            attractorDot.setOrigin(10.0f, 10.0f); // Center the dot
            attractorDot.setPosition(particleSystem.attractors[i]); // Set position to attractor location
            attractorDots.push_back(attractorDot);
        }

        // Store multiple emitters
        std::vector<sf::CircleShape> emitters;
        std::vector<sf::Vector2f> emitterDirections; // Store directions for each emitter
        std::vector<float> emitterSpreads;           // Store spread angles for each emitter

        // Add an emitter
        sf::CircleShape emitterDot(8.0f); // Radius of 8.0f for the dot
        emitterDot.setFillColor(sf::Color::Green);
        emitterDot.setOrigin(8.0f, 8.0f); // Center the dot
        emitterDot.setPosition(100.0f, 100.0f); // Initial position of the emitter
        emitters.push_back(emitterDot);
        emitterDirections.push_back(sf::Vector2f(1.0f, 0.0f)); // Emit particles to the right
        emitterSpreads.push_back(20.0f); // Spread angle of 20 degrees

        bool isDraggingEmitter = false;  // To track dragging state of emitter
        bool isDraggingAttractor = false; // To track dragging state of attractor
        int draggedEmitter = -1;         // To track which emitter is being dragged
        int draggedAttractor = -1;       // To track which attractor is being dragged

        while (window.isOpen()) {
            sf::Event event;
            while (window.pollEvent(event)) {
                if (event.type == sf::Event::Closed)
                    window.close();

                // Check if the mouse is pressed over an emitter or attractor
                if (event.type == sf::Event::MouseButtonPressed) {
                    for (size_t i = 0; i < emitters.size(); ++i) {
                        if (emitters[i].getGlobalBounds().contains(event.mouseButton.x, event.mouseButton.y)) {
                            isDraggingEmitter = true;
                            draggedEmitter = i; // Set the current dragged emitter
                            break;
                        }
                    }

                    for (size_t i = 0; i < attractorDots.size(); ++i) {
                        if (attractorDots[i].getGlobalBounds().contains(event.mouseButton.x, event.mouseButton.y)) {
                            isDraggingAttractor = true;
                            draggedAttractor = i; // Set the current dragged attractor
                            break;
                        }
                    }
                }

                // Check if the mouse button is released
                if (event.type == sf::Event::MouseButtonReleased) {
                    isDraggingEmitter = false;
                    isDraggingAttractor = false;
                    draggedEmitter = -1;
                    draggedAttractor = -1;
                }

                // If the mouse is moving while dragging an emitter, update its position
                if (event.type == sf::Event::MouseMoved) {
                    if (isDraggingEmitter && draggedEmitter != -1) {
                        emitters[draggedEmitter].setPosition(event.mouseMove.x, event.mouseMove.y);
                    }

                    // If the mouse is moving while dragging an attractor, update its position
                    if (isDraggingAttractor && draggedAttractor != -1) {
                        attractorDots[draggedAttractor].setPosition(event.mouseMove.x, event.mouseMove.y);
                        particleSystem.attractors[draggedAttractor] = attractorDots[draggedAttractor].getPosition();
                    }
                }
            }

            // Emit particles from each emitter in a spread angle
            for (size_t i = 0; i < emitters.size(); ++i) {
                float spread = emitterSpreads[i] * (rand() / static_cast<float>(RAND_MAX)) - emitterSpreads[i] / 2.0f; // Random angle within the spread
                float angle = std::atan2(emitterDirections[i].y, emitterDirections[i].x); // Base direction angle
                float newAngle = angle + spread * 3.14159f / 180.0f; // Convert spread to radians

                // Calculate velocity based on angle and speed
                sf::Vector2f velocity(std::cos(newAngle), std::sin(newAngle));
                velocity *= 1.5f; // Adjust speed of emitted particles
                particleSystem.emit(emitters[i].getPosition().x, emitters[i].getPosition().y, velocity);
            }

            particleSystem.update();

            window.clear();

            // Draw attractor dots
            for (const auto& attractorDot : attractorDots) {
                window.draw(attractorDot);
            }

            // Draw emitters
            for (const auto& emitter : emitters) {
                window.draw(emitter);
            }

            // Draw particles
            for (const auto& particle : particleSystem.particles) {
                window.draw(particle.shape);
            }

            window.display();
        }
    }
}
