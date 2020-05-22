package sonar.logistics.client.gsi.interactions.triggers;

import sonar.logistics.client.gsi.context.DisplayInteractionHandler;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Trigger implements ITrigger {

    public BiConsumer<Object, DisplayInteractionHandler> trigger;
    public BiFunction<Object, DisplayInteractionHandler, Boolean> isActive;

    public Trigger(BiConsumer<Object, DisplayInteractionHandler> trigger, BiFunction<Object, DisplayInteractionHandler, Boolean> isActive){
        this.trigger = trigger;
        this.isActive = isActive;
    }

    @Override
    public void trigger(Object source, DisplayInteractionHandler handler) {
        trigger.accept(source, handler);
    }

    @Override
    public boolean isActive(Object source, DisplayInteractionHandler handler) {
        return isActive.apply(source, handler);
    }
}
