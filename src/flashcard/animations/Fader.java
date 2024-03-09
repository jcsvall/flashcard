package flashcard.animations;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Fader {
    private FadeTransition fadeTransition;

    public Fader(Node node, Integer milliSeconds, Boolean autoReverse) {
        int defaultMilliSeconds = 2000;
        if(milliSeconds !=null){
            defaultMilliSeconds = milliSeconds;
        }
        boolean defaultAutoReverse = false;
        if(autoReverse != null){
            defaultAutoReverse = autoReverse;
        }

        fadeTransition =
                new FadeTransition(Duration.millis(defaultMilliSeconds), node);
        fadeTransition.setFromValue(1f);
        fadeTransition.setToValue(0f);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(defaultAutoReverse);

    }

    public void fade() {
        fadeTransition.playFromStart();
    }
}
