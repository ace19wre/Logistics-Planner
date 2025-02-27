package com.mastek.deepblue.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import com.mastek.deepblue.domain.Vehicle;
import com.mastek.deepblue.domain.Visit;
import org.jspecify.annotations.NonNull;

public class VehicleRoutingConstraintProvider implements ConstraintProvider {

    public static final String VEHICLE_CAPACITY = "vehicleCapacity";
    public static final String SERVICE_FINISHED_AFTER_MAX_END_TIME = "serviceFinishedAfterMaxEndTime";
    public static final String MINIMIZE_TRAVEL_TIME = "minimizeTravelTime";

    @Override
    public Constraint @NonNull [] defineConstraints(@NonNull ConstraintFactory constraintFactory) {
        return new Constraint[]{
                vehicleCapacity(constraintFactory),
                serviceFinishedAfterMaxEndTime(constraintFactory),
                minimizeTravelTime(constraintFactory)
        };
    }

    protected Constraint vehicleCapacity(@NonNull ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Vehicle.class)
                .filter(vehicle -> vehicle.getTotalDemand()> vehicle.getCapacity())
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        vehicle -> vehicle.getTotalDemand() - vehicle.getCapacity())
                .asConstraint(VEHICLE_CAPACITY);
    }

    protected Constraint serviceFinishedAfterMaxEndTime(@NonNull ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Visit.class)
                .filter(Visit::isServiceFinishedAfterMaxEndTime)
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        Visit::getServiceFinishedDelayInMinutes)
                .asConstraint(SERVICE_FINISHED_AFTER_MAX_END_TIME);
    }

    protected Constraint minimizeTravelTime(@NonNull ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Vehicle.class)
                .penalizeLong(HardSoftLongScore.ONE_SOFT,
                        Vehicle::getTotalDrivingTimeSeconds)
                .asConstraint(MINIMIZE_TRAVEL_TIME);
    }
}
