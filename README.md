
# Particle Simulator


## About the Project

The **Particle System Simulator** is an interactive, real-time application designed to explore the dynamics of particle systems. It provides users with extensive customization options for particle and emitter behaviors, including emission rates, velocities, masses, charges, and interactions with fields. The simulator is tailored for both educational and creative purposes, offering a platform for students to learn about particle dynamics and for designers to experiment with complex visual effects.

### Key Features:
- **Emitters**: Add, configure, and position emitters with customizable properties such as particle spread, direction, and special behaviors (oscillator, pulse, etc.).
- **Field Points**: Create and manipulate attractive or repulsive field points to influence particle movement dynamically.
- **Particles**: Define properties like mass, charge, and velocity for individual particles, with real-time adjustments.
- **Environment Controls**: Adjust gravity, simulation speed, and boundary conditions, or observe particle interactions frame by frame.
- **Preset Management**: Load predefined system setups or save custom configurations for reuse or sharing.
  
### Built with  
Built with **JavaFX** for an intuitive UI and **C++** (using SFML) for efficient physics calculations, the simulator bridges the frontend and backend using **JNI** to deliver high-performance real-time visualizations.

---



## Getting Started

This is an example of how you may give instructions on setting up your project locally.
To get a local copy up and running follow these simple example steps.

### Prerequisites

Install JavaFX from gluon using this link 
https://download2.gluonhq.com/openjfx/21.0.5/openjfx-21.0.5_linux-x64_bin-sdk.zip

Unzip and extract the folder
Also java, g++ and JNI needs to be installed

### Installation

_Below is an example of how you can instruct your audience on installing and setting up your app. This template doesn't rely on any external dependencies or services._

1. Clone the repo
   ```sh
   git clone https://github.com/github_username/repo_name.git
   ```
2. Change the path to JAVAFX_LIB in the Makefile based on your system

3. Run the program
    ```sh
   make clean
   make 
   make run
   ```

## Usage

Here's a **Usage** section for the Particle System Simulator's README file:

---

## Usage

The Particle System Simulator is a powerful tool designed for visualizing and experimenting with particle systems. Follow these steps to get started:

### 1. Launch the Application
- Run the program or the provided script to start the simulator.
- Ensure all necessary libraries (JavaFX, JNI bindings, SFML) are installed and configured.

### 2. Adding Emitters
- Click **"Add Emitter"** to create a new particle emitter.
- Configure emitter properties such as:
  - **Position**: Drag the emitter to the desired location.
  - **Emission Rate**: Control how many particles are emitted per second.
  - **Spread**: Adjust the direction and randomness of emitted particles.
  - **Speed**: Adjusts the speed of emission of particles.

### 3. Adding Fields
- Click **"Add Field Point"** to create attractive or repulsive fields.
- Modify properties like:
  - **Field Strength**: Define how strongly the field influences nearby particles.
  - **Field Type**: Switch between attraction and repulsion.

### 4. Configuring Particles
- Customize particle properties for each emitter, such as:
  - **Mass**: Affects how particles respond to forces.
  - **Charge**: Alters interactions with fields.
  - **Velocity**: Adjust initial speed and direction.
 

### 5. Managing Environment Settings
- Modify global settings to influence the entire simulation:
  - **Gravity**: Toggle gravity on/off and set its strength.
  - **Maximum Number of particles**: Can set the maximum number of particles that can be on the screen at a given time.
  - **Simulation Speed**: Slow down or speed up time for detailed observation.

### 6. Saving and Loading Presets
- Use **"Save Preset"** to store your current simulation setup.
- Load previously saved presets to revisit experiments or share configurations with others.
- Recently loaded presets appear at the bottom on a separate panel for quick access.

### 7. Advanced Features
- **Step-by-Step Simulation**: Pause the simulation and step through it frame by frame to analyze particle interactions.
- **Custom Behaviors**: Define oscillating emitters, pulsing particles, or other unique behaviors using the property editor.

---


## Roadmap

### Phase 1: Core Features
1. **Basic Particle System**  
   - Implement particle attributes: mass, velocity, charge, and color.  
   - Create a particle rendering system using JavaFX.  
   - Add basic interactions with field points and gravity.  

2. **Emitters**  
   - Develop standard emitters with configurable properties: position, speed, spread, and angle.  
   - Enable emitters to produce particles continuously.  

3. **Field Points**  
   - Implement attractor and repulsor field points with adjustable field strength.  
   - Add drag-and-drop functionality for positioning field points on the canvas.  

4. **Environment Controls**  
   - Integrate gravity toggle and basic boundary conditions (e.g., particles disappearing at edges).  
   - Provide frame-by-frame control for detailed observation.  

5. **Preset Management**  
   - Enable loading and saving of particle system configurations.  
   - Add predefined presets for quick simulation setups.  

---

### Phase 2: Enhanced Features
1. **Special Emitters**  
   - Develop advanced emitter types like Oscillating and Pulse emitters with unique behaviors.  
   - Allow real-time modification of emitter properties.

2. **Dynamic Particle Properties**  
   - Add dynamic color changes based on particle velocity or acceleration.  
   - Introduce sliders for fine-tuning particle attributes.  

3. **Environment Enhancements**  
   - Expand boundary conditions: particles that leave the screen are deleted from memory to enable efficient rendering.  

4. **UI/UX Improvements**  
   - Create an intuitive and visually appealing interface with JavaFX.  
   - Provide real-time feedback, changing any setting will immediately reflect in change of particle behavior.  

---

### Phase 3: Scalability and Optimization
1. **Performance Enhancements**  
   - Optimize the C++ backend for handling large particle counts while maintaining high FPS.  
   - Utilize JNI efficiently for smooth communication between JavaFX and C++.  

2. **Advanced Preset Management**  
   - Enable exporting and sharing of presets for collaborative experimentation.  
   - Add the ability to load presets dynamically from external files.  

---

### Future Scope (Post-MVP)
1. **Advanced Physics**  
   - Add support for inter-particle interactions and advanced collision detection.  

2. **Visual Effects**  
   - Introduce particle trails, gradients, and enhanced visual effects for artistic simulations.  

3. **Extended Compatibility**  
   - Explore cross-platform support for web-based or mobile versions (if feasible).  

## Acknowledgements

We would like to express our gratitude to the following individuals and resources for their support and inspiration during the development of this project:



- **Libraries and Tools**  
  - **JavaFX**: For providing a robust framework for building the user interface and rendering the simulation.  
  - **C++ with SFML**: For enabling high-performance physics calculations and backend support.  
  - **JNI (Java Native Interface)**: For seamless communication between the Java frontend and C++ backend.  

- **Open Source Community**  
  - For the wealth of resources, documentation, and tutorials available, which helped us overcome technical challenges.  

- **Team Members**  
  - A special thanks to each team member for their dedication and collaboration in bringing this project to life:  
    - **Rakshith Srinivasan** (IMT2023544)  
    - **Subhash H** (IMT2023104)  
    - **R. Ricky Roger** (IMT2023098)  
    - **Sathish Adithiyaa SV** (IMT2023030)  
    - **Bharat Dhulgond** (IMT2023504)  
    - **Pranav Sandeep** (IMT2023058)  

- **Community Support**  
  - A huge thanks to the programming forums and developer communities, such as Stack Overflow, for their invaluable assistance and discussions.

This project wouldn’t have been possible without the collective efforts and contributions of everyone mentioned above. Thank you!


## Contributors

Thanks to all the amazing contributors who helped build this project!

| ![Rakshith Srinivasan](https://avatars.githubusercontent.com/u/144807403?s=400&u=b89bc20547a3b237aaf6fb44a1afddf7594ea498&v=4) | ![Subhash H](https://github.com/placeholder.png?size=100) | ![R. Ricky Roger](https://avatars.githubusercontent.com/u/144564986?s=400&u=5e7dd95f4733264c338cafe5223421f147fbfe7d&v=4) | ![Sathish Adithiyaa SV](https://avatars.githubusercontent.com/u/164901276?v=4) | ![Pranav Sandeep](https://avatars.githubusercontent.com/u/98758049?v=4) |  ![Bharat Dhulgond](https://avatars.githubusercontent.com/u/166221993?v=4)|
|:-------------------------------------------------------------------:|:----------------------------------------------------------:|:--------------------------------------------------------------:|:------------------------------------------------------------------:|:---------------------------------------------------------------:|:------------------------------------------------------------:|
| [Rakshith Srinivasan](https://github.com/rakshithsrinivasan)        | [Subhash H](https://github.com/subhashh)                   | [R. Ricky Roger](https://github.com/rrickyroger)               | [Sathish Adithiyaa SV](https://github.com/sathishadithiyaa)        | [Pranav Sandeep](https://github.com/pranavsandeep)             | [Bharat Dhulgond](https://github.com/bharatdhulgond)            |
