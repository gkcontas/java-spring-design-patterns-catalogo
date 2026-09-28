package com.gkcontas.patterns.mediator;

/**
 * A component that reports changes to the mediator and never to another component.
 *
 * <p>That restriction is the pattern. Without it every field needs a reference to every
 * other field it affects, and the wiring grows as the square of the number of components.
 */
public class FormField {

    private final String name;
    private final FormMediator mediator;
    private String value = "";
    private boolean enabled = true;

    public FormField(String name, FormMediator mediator) {
        this.name = name;
        this.mediator = mediator;
        mediator.register(this);
    }

    public void type(String value) {
        this.value = value;
        // The field announces what happened. It does not decide what that means for
        // anything else, and it does not know what else exists.
        mediator.changed(this);
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    public boolean enabled() {
        return enabled;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    void setValue(String value) {
        this.value = value;
    }
}
