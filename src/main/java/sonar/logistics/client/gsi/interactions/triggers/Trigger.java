package sonar.logistics.client.gsi.interactions.triggers;

import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Trigger implements ITrigger {

    public BiConsumer<Object, GSIInteractionHandler> trigger;
    public BiFunction<Object, GSIInteractionHandler, Boolean> isActive;

    public Trigger(BiConsumer<Object, GSIInteractionHandler> trigger, BiFunction<Object, GSIInteractionHandler, Boolean> isActive){
        this.trigger = trigger;
        this.isActive = isActive;
    }

    @Override
    public void trigger(Object source, GSIInteractionHandler handler) {
        trigger.accept(source, handler);
    }

    @Override
    public boolean isActive(Object source, GSIInteractionHandler handler) {
        return isActive.apply(source, handler);
    }
}
