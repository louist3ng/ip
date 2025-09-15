package jason.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Utility class to add zooming (font size adjustment) to a JavaFX scene.
 * Zooming is done by changing the root node's font size via CSS.
 * Supported gestures:
 * - Ctrl/Cmd + '+' or '=' to zoom in
 * - Ctrl/Cmd + '-' to zoom out
 * - Ctrl/Cmd + '0' to reset zoom
 * - Ctrl/Cmd + mouse wheel up/down to zoom in/out
 * Zoom levels are clamped between 11px and 28px, with a default of 15px.
 */
public final class Zoom {
    private static final double BASE_PX = 15;   
    private static final double STEP_PX = 2;    // zoom increment
    private static final double MIN_PX  = 11;   // min font size
    private static final double MAX_PX  = 28;   // max font size

    private double currentPx = BASE_PX;

    /** Installs zoom handlers on the given scene. */
    public void install(Scene scene) {
        apply(scene);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            boolean accel = e.isControlDown() || e.isMetaDown(); // Windows/Linux: Ctrl, macOS: Cmd
            if (!accel) {
                return;
            }
            if (e.getCode() == KeyCode.EQUALS || e.getCode() == KeyCode.PLUS) {
                adjust(scene, +STEP_PX); 
                e.consume();
            } else if (e.getCode() == KeyCode.MINUS) {
                adjust(scene, -STEP_PX);
                e.consume();
            } else if (e.getCode() == KeyCode.DIGIT0) {
                reset(scene);
                e.consume();
            }
        });

        // Ctrl/Cmd + mouse wheel to zoom
        scene.addEventFilter(ScrollEvent.SCROLL, e -> {
            if (!(e.isControlDown() || e.isMetaDown())) {
                return;
            } 
            double delta = e.getDeltaY() > 0 ? STEP_PX : -STEP_PX;
            adjust(scene, delta);
            e.consume();
        });
    }

    private void adjust(Scene scene, double delta) {
        currentPx = clamp(currentPx + delta, MIN_PX, MAX_PX);
        apply(scene);
    }

    private void reset(Scene scene) {
        currentPx = BASE_PX;
        apply(scene);
    }

    private void apply(Scene scene) {
        Parent root = scene.getRoot();
        root.setStyle("-fx-font-size: " + (int) currentPx + "px;");
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
