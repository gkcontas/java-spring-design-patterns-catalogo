package com.gkcontas.patterns.mediator;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the rules that relate the components to each other.
 *
 * <p>The trade is explicit: the components become simple and mutually ignorant, and the
 * complexity they used to share is concentrated here. That is a win while the rules are
 * few and a loss once the mediator becomes the class nobody wants to open.
 */
public class FormMediator {

    private final Map<String, FormField> fields = new HashMap<>();

    void register(FormField field) {
        fields.put(field.name(), field);
    }

    public void changed(FormField source) {
        switch (source.name()) {
            case "country" -> {
                // Choosing a country enables the state field and clears whatever was in it.
                FormField state = fields.get("state");
                if (state != null) {
                    state.setEnabled(!source.value().isBlank());
                    state.setValue("");
                }
            }
            case "deliveryType" -> {
                FormField address = fields.get("address");
                if (address != null) {
                    address.setEnabled("SHIPPING".equals(source.value()));
                }
            }
            default -> {
                // Fields with no rule attached need no special case, and adding one later
                // touches this class alone.
            }
        }
    }

    public FormField field(String name) {
        return fields.get(name);
    }
}
