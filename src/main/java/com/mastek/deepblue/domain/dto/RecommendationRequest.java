package com.mastek.deepblue.domain.dto;

import com.mastek.deepblue.domain.VehicleRoutePlan;

public record RecommendationRequest(VehicleRoutePlan solution, String visitId) {
}
