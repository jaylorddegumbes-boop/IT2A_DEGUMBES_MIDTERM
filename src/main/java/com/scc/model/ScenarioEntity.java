package com.scc.model;

public record ScenarioEntity(
        int id,
        String attendeeName,
        String ticketTier,
        double price,
        String payment
) {
}