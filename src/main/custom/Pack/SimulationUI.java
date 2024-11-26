package Pack;

    import Pack.Emitter.*;
    import Pack.FieldPoint.*;
    import Pack.Particle.*;
    import Pack.ParticleSystem;
    import java.util.Vector;
    import javafx.application.Application;
    import javafx.scene.Scene;
    import javafx.scene.canvas.Canvas;
    import javafx.scene.canvas.GraphicsContext;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.Slider;
    import javafx.scene.layout.HBox;
    import javafx.scene.layout.VBox;
    import javafx.scene.layout.BorderPane;
    import javafx.scene.paint.Color;
    import javafx.stage.Stage;
    import javafx.animation.AnimationTimer;
    import javafx.scene.input.MouseEvent;
    import javafx.stage.FileChooser; // Correctly import FileChooser
    import javafx.geometry.Insets;
    import java.io.File;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;


    public class SimulationUI extends Application {
        private static final int WIDTH = 1200; // Total screen width
        private static final int HEIGHT = 800; // Total screen height
        private static final int CONTROL_BOX_WIDTH = 300; // Width of the control box

        private ParticleSystem particleSystem; // Backend system
        private Canvas canvas;
        private Map<Button, File> presetMap = new HashMap<>();


        // Sliders for controlling parameters
        private Slider velocitySlider;
        private Slider spreadSlider;
        private Slider angleSlider;
        private Slider fieldForceSlider;

        private Emitter selectedEmitter; // Currently selected emitter
        private FieldPoint selectedFieldPoint; // Currently selected field point

        private boolean draggingEmitter = false; // Is an emitter being dragged?
        private boolean draggingField = false;  // Is a field being dragged?
        private boolean isPaused = false;
        private boolean isStepMode = false;
        private AnimationTimer timer;
        private VBox sliderContainer;
        private VBox fieldContainer;

        // // @Override
        // public class SimulationUI extends Application {
        //     // Other fields as before...
        
            private HBox presetButtonBox; // Box to hold preset buttons
        
            @Override
            public void start(Stage stage) {
                // Initialize particleSystem and other components
                particleSystem = new ParticleSystem();
        
                BorderPane root = new BorderPane();
                canvas = new Canvas(WIDTH - CONTROL_BOX_WIDTH, HEIGHT);
                canvas.setOnMousePressed(this::handleMousePressed);
                canvas.setOnMouseDragged(this::handleMouseDragged);
                canvas.setOnMouseReleased(this::handleMouseReleased);
                root.setCenter(canvas);
        
                VBox controlBox = createControls();
                controlBox.setPrefWidth(CONTROL_BOX_WIDTH);
                root.setRight(controlBox);
        
                // Initialize animation timer as before...
                timer = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if (!isPaused || isStepMode) {
                            update();
                            render();
                            isStepMode = false;
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                timer.start();
        
                Scene scene = new Scene(root, WIDTH, HEIGHT);
                stage.setScene(scene);
                stage.setTitle("Interactive Particle Simulation");
                stage.show();
            }

        
    private VBox createControls() {
        System.out.println("createControls");
        VBox controls = new VBox(10);
        controls.setStyle("-fx-background-color: #333333; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 2;");

        Button addEmitterButton = new Button("Add Emitter");
        Button addFieldAButton = new Button("Add FieldA");
        Button addFieldBButton = new Button("Add FieldB");
        Button resetButton = new Button("Reset");
        Button toggleGravityButton = new Button("Toggle Gravity");
        Button pauseButton = new Button("Pause/Resume");
        Button stepButton = new Button("Step");
        Button savePreset = new Button("Save Preset");
        Button loadPreset = new Button("Load Preset");
        Button deleteEmitter = new Button("Delete Emitter");
        Button deleteField = new Button("Delete Field");
        //Button presetButton = new Button("Preset Button");
        Button clear = new Button("Clear");
            // Sliders
            velocitySlider = createSlider(1, 10, 3, "Particle Velocity");
            spreadSlider = createSlider(0, 2 * 3.16, 1, "Emitter Spread Angle");
            angleSlider = createSlider(0, 2 * 3.16, 0, "Emission Angle");
            fieldForceSlider = createSlider(5, 15, 10, "Field Force");

            // Slider container that will be added when emitter is selected and not being dragged
            sliderContainer = new VBox(10);
            sliderContainer.setStyle("-fx-background-color: #555555; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 1;");
            sliderContainer.getChildren().addAll(
                    labeledSlider("Velocity:", velocitySlider),
                    labeledSlider("Spread Angle:", spreadSlider),
                    labeledSlider("Emission Angle:", angleSlider),
                    deleteEmitter
            );
            sliderContainer.setVisible(false); // Hide initially

            fieldContainer = new VBox(10);
            fieldContainer.setStyle("-fx-background-color: #555555; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 1;");
            fieldContainer.getChildren().addAll(
                    labeledSlider("Field Strength:", fieldForceSlider),
                    deleteField
            );
            fieldContainer.setVisible(false); // Hide initially

            // fieldContainer = new VBox(10);
            // fieldContainer.setStyle("-fx-background-color: #555555; -fx-padding: 10; -fx-border-color: white; -fx-border-width: 1;");
            // fieldContainer.getChildren().addAll(
            //         deleteField
            // );
        // HBox to hold preset buttons
        presetButtonBox = new HBox(5); // Set spacing between buttons
        presetButtonBox.setPadding(new Insets(0, 0, 0, 0)); // Padding at the top
        presetButtonBox.setStyle("-fx-background-color: #444444; -fx-border-color: white; -fx-border-width: 1;");

        
            addEmitterButton.setOnAction(e -> {
                // Add an emitter at a random position
                particleSystem.addEmitter(randomPosition(), 3.0f, 1.0f, 0.0f, 1.0f);
            });
        
            addFieldAButton.setOnAction(e -> {
                // Add a field point at a random position
                particleSystem.addFieldPoint(randomPosition(), 10.0f, "A");
            });

            addFieldBButton.setOnAction(e -> {
                // Add a field point at a random position
                particleSystem.addFieldPoint(randomPosition(), 10.0f, "B");
            });
        
            resetButton.setOnAction(e -> {
                // Clear all particles, emitters, and fields
                particleSystem.getParticles().clear();
            });
            clear.setOnAction(e -> {
                // Clear all particles, emitters, and fields
                particleSystem.getParticles().clear();
                particleSystem.getEmitters().clear();
                particleSystem.getFieldPoints().clear();
            });
        
            toggleGravityButton.setOnAction(e -> {
                // Toggle gravity for the particle system
                particleSystem.setGravityEnabled(particleSystem.isGravityEnabled() ^ 1);
            });
        
            pauseButton.setOnAction(e -> {
                isPaused = !isPaused;
            });
        
            stepButton.setOnAction(e -> {
                if (isPaused) {
                    isStepMode = true;
                }
            });
        
            savePreset.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Preset");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Preset Files", "*.txt"));

                // Show save dialog
                File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());
                if (file != null) {
                    SystemPreset preset = new SystemPreset(particleSystem);
                    preset.savePreset(file.getAbsolutePath()); // Save to the chosen file
                }
            });

            loadPreset.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Load Preset");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Preset Files", "*.txt"));
                File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
            
                if (file != null) {
                    // Create a new ParticleSystem and load the preset file
                    particleSystem = new ParticleSystem();
                    SystemPreset preset = new SystemPreset(particleSystem);
                    preset.loadPreset(file.getAbsolutePath());
            
                    // Create a new button for this specific preset
                    Button presetButton = new Button(file.getName());
                    presetMap.put(presetButton, file); // Store the button-preset association in the map
                    //Button presetButton = new Button("Preset Button");
                    // Set an action for the preset button using the HashMa
                    presetButton.setOnAction(event -> {
                        // Clear the current particle system
                        particleSystem = new ParticleSystem();
                        
                        // Retrieve and load the preset associated with this button
                        SystemPreset preset2=new SystemPreset(particleSystem);
                        File currentFile = presetMap.get(presetButton); // Retrieve the preset from the map
                        if (currentFile!=null) {
                            // Load the preset into the new particle system
                            preset2.loadPreset(currentFile.getAbsolutePath());
        
                            // Update the UI controls to match the loaded preset's settings
                            //updateUIWithPresetData();
                        }
                    });
                    // Add button to presetButtonBox
                    presetButtonBox.getChildren().add(presetButton);
                }
            });


            


            deleteEmitter.setOnAction(e -> {
                if(selectedEmitter != null)
                {
                    particleSystem.getEmitters().remove(selectedEmitter);
                    selectedEmitter = null;
                    sliderContainer.setVisible(false);
                    //updateControlBox();
                }
            });

            deleteField.setOnAction(e -> {
                if(selectedFieldPoint != null)
                {
                    particleSystem.getFieldPoints().remove(selectedFieldPoint);
                    selectedFieldPoint = null;
                    fieldContainer.setVisible(false);
                    //updateControlBox();
                }
            });
        
            // Add controls and conditionally add sliders
            controls.getChildren().addAll(
                    new Label("Controls:"),
                    addEmitterButton, addFieldAButton, addFieldBButton, resetButton,
                    toggleGravityButton, pauseButton, stepButton, savePreset, loadPreset, clear
            );
            controls.getChildren().add(sliderContainer);
            controls.getChildren().add(fieldContainer);
        
        
        controls.getChildren().add(presetButtonBox); // Add the HBox at the bottom of the control panel

        return controls;
            // Only add the slider container if selectedEmitter != null and draggingEmitter == false
            // if (selectedEmitter != null) {
            //     controls.getChildren().add(sliderContainer);
            // }      
        }

        private HBox labeledSlider(String labelText, Slider slider) {
            Label label = new Label(labelText);
            label.setTextFill(Color.WHITE);
            HBox hbox = new HBox(10, label, slider);
            return hbox;
        }

        private Slider createSlider(double min, double max, double value, String tooltip) {
            Slider slider = new Slider(min, max, value);
            slider.setShowTickMarks(true);
            slider.setShowTickLabels(true);
            slider.setBlockIncrement(1);
            slider.setTooltip(new javafx.scene.control.Tooltip(tooltip));
            return slider;
        }

        private Vector<Float> randomPosition() {
            Vector<Float> position = new Vector<>();
            position.add((float) (Math.random() * (WIDTH - CONTROL_BOX_WIDTH)));  // X-coordinate
            position.add((float) (Math.random() * HEIGHT)); // Y-coordinate
            return position;
        }

        private void handleMousePressed(MouseEvent e) {
            float x = (float) e.getX();
            float y = (float) e.getY();
        
            if (x > WIDTH - CONTROL_BOX_WIDTH) return; // Ignore clicks in the control box
        
            // Check for emitters near the click
            for (Emitter emitter : particleSystem.getEmitters()) {
                Vector<Float> position = emitter.getPosition();
                if (isNear(x, y, position)) {
                    selectedEmitter = emitter;
                    draggingEmitter = true;
                    // Update control panel to show sliders
                    //set slider values
                    velocitySlider.setValue(emitter.getSpeed());
                    spreadSlider.setValue(emitter.getSpread());
                    angleSlider.setValue(emitter.getAngle());
                    updateControlBox();
                    return;
                }
            }
        
            // Check for field points near the click
            for (FieldPoint fieldPoint : particleSystem.getFieldPoints()) {
                Vector<Float> position = fieldPoint.getPosition();
                if (isNear(x, y, position)) {
                    selectedFieldPoint = fieldPoint;
                    draggingField = true;
                    fieldForceSlider.setValue(fieldPoint.getFieldStrength());
                    updateControlBox();
                    return;
                }
            }
        }

        private void handleMouseDragged(MouseEvent e) {
            float x = (float) e.getX();
            float y = (float) e.getY();

            if (draggingEmitter && selectedEmitter != null) {
                selectedEmitter.getPosition().set(0, x);
                selectedEmitter.getPosition().set(1, y);
            }

            if (draggingField && selectedFieldPoint != null) {
                selectedFieldPoint.getPosition().set(0, x);
                selectedFieldPoint.getPosition().set(1, y);
            }
        }

        private void handleMouseReleased(MouseEvent e) {
            draggingEmitter = false;
            draggingField = false;
            // selectedEmitter = null;
            // selectedFieldPoint = null;
            //updateControlBox(); // Update UI after releasing the mouse
        }
        
        // Update the control box UI when emitter is selected or dragged
        private void updateControlBox() {
            System.out.println("Update control box");
            VBox controlBox = (VBox) ((BorderPane) canvas.getScene().getRoot()).getRight();
            if (selectedEmitter != null) {
                // if (!controlBox.getChildren().contains(sliderContainer)) {
                //     controlBox.getChildren().add(sliderContainer);
                //     sliderContainer.setVisible(true);
                // }
                sliderContainer.setVisible(true);
            } else {
                sliderContainer.setVisible(false);
            }

            if(selectedFieldPoint != null)
            {
                fieldContainer.setVisible(true);
            }
            else
            {
                fieldContainer.setVisible(false);
            }
        }

        // private void updateUIWithPresetData() {
        //     // Assuming that particleSystem has a currently loaded preset
            
        //     // Update the UI sliders with preset values if there's a selected emitter
        //     if (selectedEmitter != null) {
        //         velocitySlider.setValue(selectedEmitter.getSpeed());
        //         spreadSlider.setValue(selectedEmitter.getSpread());
        //         angleSlider.setValue(selectedEmitter.getAngle());
        //         sliderContainer.setVisible(true); // Ensure the slider UI is visible
        //     } else {
        //         sliderContainer.setVisible(false); // Hide if no emitter is selected
        //     }
        
        //     // Update field strength if there's a selected field point
        //     if (selectedFieldPoint != null) {
        //         fieldForceSlider.setValue(selectedFieldPoint.getFieldStrength());
        //         fieldContainer.setVisible(true); // Ensure the field UI is visible
        //     } else {
        //         fieldContainer.setVisible(false); // Hide if no field is selected
        //     }
        
        //     // Refresh the canvas to render the particle system state
        //     render();
        // }
        
        

        private boolean isNear(float x, float y, Vector<Float> position) {
            float dx = x - position.get(0);
            float dy = y - position.get(1);
            return (dx * dx + dy * dy) <= 100; // Within 10px radius
        }

        private void update() {
            // Update particle positions in the backend
            particleSystem.removeParticlesOutOfScreen(WIDTH, HEIGHT);
            float angle = (float) angleSlider.getValue();
            float spread = (float) spreadSlider.getValue();
            float speed = (float)  velocitySlider.getValue();
            if(selectedEmitter != null)
            {
                selectedEmitter.setAngle(angle);
                selectedEmitter.setSpread(spread);
                selectedEmitter.setSpeed(speed);
            }
            float forceField = (float) fieldForceSlider.getValue();
            if(selectedFieldPoint != null)
            {
                selectedFieldPoint.setFieldStrength(forceField);
            }
            particleSystem.setForces();
            particleSystem.updateAll();
        }

        private void render() {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.BLACK); // Black background
            gc.fillRect(0, 0, WIDTH - CONTROL_BOX_WIDTH, HEIGHT);

            // Draw particles
            List<Particle> particles = particleSystem.getParticles();
            gc.setFill(Color.BLUE);
            for (Particle particle : particles) {
                gc.fillOval(particle.getPosition().get(0), particle.getPosition().get(1), 3, 3);
            }

            // Draw emitters
            gc.setFill(Color.RED);
            for (Emitter emitter : particleSystem.getEmitters()) {
                Vector<Float> position = emitter.getPosition();
                if(selectedEmitter!=null && selectedEmitter.equals(emitter))
                {
                    gc.fillOval(position.get(0), position.get(1), 10, 10);
                    gc.setStroke(Color.YELLOW); // Set the boundary color
                    gc.setLineWidth(2); // Set the thickness of the boundary
                    gc.strokeOval(position.get(0) - 2, position.get(1) - 2, 14, 14); 
                }
                else
                {
                    gc.fillOval(position.get(0), position.get(1), 10, 10);
                } 
            }

            // Draw field points
            gc.setFill(Color.GREEN);
            for (FieldPoint fieldPoint : particleSystem.getFieldPoints()) {
                Vector<Float> position = fieldPoint.getPosition();
                if(selectedFieldPoint!=null && selectedFieldPoint.equals(fieldPoint))
                {
                    gc.fillOval(position.get(0), position.get(1), 10, 10);
                    gc.setStroke(Color.YELLOW); // Set the boundary color
                    gc.setLineWidth(2); // Set the thickness of the boundary
                    gc.strokeOval(position.get(0) - 2, position.get(1) - 2, 14, 14); 
                }
                else
                {
                    gc.fillOval(position.get(0), position.get(1), 10, 10);
                } 
            }
        }

        public static void main(String[] args) {
            launch(args);
        }
        }