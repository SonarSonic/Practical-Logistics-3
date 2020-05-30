package sonar.logistics.client.gsi.interactions.triggers;

import sonar.logistics.client.gsi.interactions.GSIInteractionHandler;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Trigger<O extends Object> implements ITrigger<O> {

    public BiConsumer<O, GSIInteractionHandler> trigger;
    public BiFunction<O, GSIInteractionHandler, Boolean> isActive;

    public Trigger(BiConsumer<O, GSIInteractionHandler> trigger, BiFunction<O, GSIInteractionHandler, Boolean> isActive){
        this.trigger = trigger;
        this.isActive = isActive;
    }

    @Override
    public void trigger(O source, GSIInteractionHandler handler) {
        trigger.accept(source, handler);
    }

    @Override
    public boolean isActive(O source, GSIInteractionHandler handler) {
        return isActive.apply(source, handler);
    }
}
