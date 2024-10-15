#include <SFML/Graphics.hpp>
#include <vector>
#include <cmath>
#include <cstdlib>
#include <ctime>

class Particle {
public:
    sf::CircleShape shape;
    sf::Vector2f velocity;

    Particle(float x, float y) {
        shape.setRadius(3.0f);
        shape.setFillColor(sf::Color::White);
        shape.setPosition(x, y);
        velocity = sf::Vector2f(0.0f, 0.0f);
    }

    void update() {
        shape.move(velocity);
    }
};

class ParticleSystem {
public:
    std::vector<Particle> particles;
    sf::Vector2f attractor;
    float K; // Attraction constant

    ParticleSystem(float attractorX, float attractorY, float attractionStrength)
        : attractor(attractorX, attractorY), K(attractionStrength) {}

    void emit(float x, float y) {
        particles.emplace_back(x, y);
    }

    void update() {
        for (auto it = particles.begin(); it != particles.end(); ) {
            sf::Vector2f direction = attractor - it->shape.getPosition();
            float distance = std::sqrt(direction.x * direction.x + direction.y * direction.y);
            if (distance > 0.1f) { // Avoid division by zero
                float force = K / (distance * distance); // F = K/d^2
                direction /= distance; // Normalize
                it->velocity += direction * force; // Apply force
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

int main() {
    srand(static_cast<unsigned int>(time(nullptr))); // Seed for random number generation
    sf::RenderWindow window(sf::VideoMode(800, 600), "Particle Attraction");

    ParticleSystem particleSystem(300, 300, 50.0f); // Create a particle system with attractor at (400, 400)

    // Main loop
    while (window.isOpen()) {
        sf::Event event;
        while (window.pollEvent(event)) {
            if (event.type == sf::Event::Closed)
                window.close();
        }

        // Emit a new particle at a random position
        float randomX = static_cast<float>(rand() % 800);
        float randomY = static_cast<float>(rand() % 600);
        particleSystem.emit(randomX, randomY);

        particleSystem.update();

        window.clear();
        for (const auto& particle : particleSystem.particles) {
            window.draw(particle.shape);
        }
        window.display();
    }

    return 0;
}
