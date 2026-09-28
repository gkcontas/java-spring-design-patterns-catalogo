package com.gkcontas.patterns.decorator;

/**
 * Base for the wrappers. A decorator both implements the interface and holds one — that
 * pair is what lets decorators stack to any depth without anything knowing how deep.
 */
public abstract class PriceDecorator implements PriceQuote {

    protected final PriceQuote wrapped;

    protected PriceDecorator(PriceQuote wrapped) {
        this.wrapped = wrapped;
    }
}
