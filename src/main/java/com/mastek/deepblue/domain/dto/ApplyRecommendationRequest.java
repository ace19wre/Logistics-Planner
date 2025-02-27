package com.mastek.deepblue.domain.dto;

import com.mastek.deepblue.domain.VehicleRoutePlan;

public record ApplyRecommendationRequest(VehicleRoutePlan solution, String visitId, String vehicleId, int index) {
}
