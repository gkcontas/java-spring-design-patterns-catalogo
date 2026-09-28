package com.gkcontas.patterns.mediator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FormMediatorTest {

    private final FormMediator mediator = new FormMediator();
    private final FormField country = new FormField("country", mediator);
    private final FormField state = new FormField("state", mediator);
    private final FormField deliveryType = new FormField("deliveryType", mediator);
    private final FormField address = new FormField("address", mediator);

    @Test
    void shouldCoordinateFieldsThatKnowNothingAboutEachOther() {
        state.type("SP");
        country.type("BR");

        // country has no reference to state, and state has none to country. The rule lives
        // in one place and the fields stay ignorant of one another.
        assertThat(state.enabled()).isTrue();
        assertThat(state.value()).isEmpty();
    }

    @Test
    void shouldApplyADifferentRuleForADifferentSource() {
        deliveryType.type("PICKUP");
        assertThat(address.enabled()).isFalse();

        deliveryType.type("SHIPPING");
        assertThat(address.enabled()).isTrue();
    }

    @Test
    void shouldIgnoreAFieldWithNoRuleAttached() {
        FormField notes = new FormField("notes", mediator);

        notes.type("anything");

        // No rule, no special case, nothing breaks — and adding one later touches the
        // mediator alone.
        assertThat(notes.value()).isEqualTo("anything");
        assertThat(mediator.field("notes")).isSameAs(notes);
    }
}
