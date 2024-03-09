package flashcard.animations;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class FaderV2 {
    private FadeTransition fadeTransition;

    public FaderV2(Node node, Integer milliSeconds, Boolean autoReverse) {
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
        fadeTransition.setFromValue(0.3f);
        fadeTransition.setToValue(1f);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(defaultAutoReverse);

    }

    public void fade() {
        fadeTransition.playFromStart();
    }
}
