package com.gkcontas.patterns.iterator;

import java.util.List;

public record Page<T>(List<T> items, boolean hasNext) {
}
