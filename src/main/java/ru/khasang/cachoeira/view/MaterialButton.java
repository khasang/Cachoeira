package ru.khasang.cachoeira.view;

import com.sun.javafx.scene.control.skin.ButtonSkin;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class MaterialButton extends Button {
    private final static double DEFAULT_DROP_SHADOW_RADIUS = 4;
    private final static double HOVER_DROP_SHADOW_RADIUS = 12;

    private DropShadow dropShadow;

    private Circle circleRipple;
    private Rectangle rippleClip = new Rectangle();
    private Duration rippleDuration = Duration.millis(250);
    private double lastRippleHeight = 0;
    private double lastRippleWidth = 0;
    private Color rippleColor = new Color(0, 0, 0, 0.11);

    public MaterialButton(String text) {
        super(text);
        this.setFont(Font.loadFont(getClass().getResource("/font/Roboto-Medium.ttf").toExternalForm(), 10.5));
        dropShadow = createDropShadow();
        this.setEffect(dropShadow);
        this.enableDropShadowAnimationOnHover();
        this.createRippleEffect();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        final ButtonSkin buttonSkin = new ButtonSkin(this);
        // Adding circleRipple as fist node of button nodes to be on the bottom
        this.getChildren().add(0, circleRipple);
        return buttonSkin;
    }

    private void createRippleEffect() {
        circleRipple = new Circle(0.1, rippleColor);
        circleRipple.setOpacity(0.0);
        // Optional box blur on ripple - smoother ripple effect
//        circleRipple.setEffect(new BoxBlur(3, 3, 2));

        // Fade effect bit longer to show edges on the end
        final FadeTransition fadeTransition = new FadeTransition(rippleDuration, circleRipple);
        fadeTransition.setInterpolator(Interpolator.EASE_OUT);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        final Timeline scaleRippleTimeline = new Timeline();

        final SequentialTransition parallelTransition = new SequentialTransition();
        parallelTransition.getChildren().addAll(
                scaleRippleTimeline,
                fadeTransition
        );

        parallelTransition.setOnFinished(event1 -> {
            circleRipple.setOpacity(0.0);
            circleRipple.setRadius(0.1);
        });

        this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            parallelTransition.stop();
            parallelTransition.getOnFinished().handle(null);

            circleRipple.setCenterX(event.getX());
            circleRipple.setCenterY(event.getY());
            // Recalculate ripple size if size of button from last time was changed
            if (getWidth() != lastRippleWidth || getHeight() != lastRippleHeight) {
                lastRippleWidth = getWidth();
                lastRippleHeight = getHeight();

                rippleClip.setWidth(lastRippleWidth);
                rippleClip.setHeight(lastRippleHeight);

                rippleClip.setArcHeight(this.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius());
                rippleClip.setArcWidth(this.getBackground().getFills().get(0).getRadii().getTopLeftHorizontalRadius());
                circleRipple.setClip(rippleClip);

                // Getting 45% of longest button's length, because we want edge of ripple effect always visible
                double circleRippleRadius = Math.max(getHeight(), getWidth()) * 0.45;
                final KeyValue keyValue = new KeyValue(circleRipple.radiusProperty(), circleRippleRadius, Interpolator.EASE_OUT);
                final KeyFrame keyFrame = new KeyFrame(rippleDuration, keyValue);
                scaleRippleTimeline.getKeyFrames().clear();
                scaleRippleTimeline.getKeyFrames().add(keyFrame);
            }
            parallelTransition.playFromStart();
        });
    }

    private DropShadow createDropShadow() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.8));
        dropShadow.setBlurType(BlurType.TWO_PASS_BOX);
        dropShadow.setOffsetY(2);
        dropShadow.setRadius(DEFAULT_DROP_SHADOW_RADIUS);
        return dropShadow;
    }

    private void enableDropShadowAnimationOnHover() {
        Timeline dropShadowOut = createAnimation(dropShadow.radiusProperty(), HOVER_DROP_SHADOW_RADIUS, Duration.millis(500));
        Timeline dropShadowIn = createAnimation(dropShadow.radiusProperty(), DEFAULT_DROP_SHADOW_RADIUS, Duration.millis(300));
        this.setOnMouseEntered(event -> {
            dropShadowIn.stop();
            dropShadowOut.play();
        });
        this.setOnMouseExited(event -> {
            dropShadowOut.stop();
            dropShadowIn.play();
        });
    }

    private Timeline createAnimation(DoubleProperty property, double endValue, Duration millis) {
        KeyValue endKeyValue = new KeyValue(property, endValue, Interpolator.SPLINE(0.4, 0, 0.2, 1));
        KeyFrame endKeyFrame = new KeyFrame(millis, endKeyValue);
        return new Timeline(endKeyFrame);
    }
}
