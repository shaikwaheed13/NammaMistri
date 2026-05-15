package com.nammamistri.app.calculation;

import java.util.Locale;

public final class WallCalculationResult {
    private final double wallVolumeCft;
    private final int bricks;
    private final double cementBags;
    private final double sandCft;
    private final double estimatedCost;

    public WallCalculationResult(
            double wallVolumeCft,
            int bricks,
            double cementBags,
            double sandCft,
            double estimatedCost
    ) {
        this.wallVolumeCft = wallVolumeCft;
        this.bricks = bricks;
        this.cementBags = cementBags;
        this.sandCft = sandCft;
        this.estimatedCost = estimatedCost;
    }

    public double getWallVolumeCft() {
        return wallVolumeCft;
    }

    public int getBricks() {
        return bricks;
    }

    public double getCementBags() {
        return cementBags;
    }

    public double getSandCft() {
        return sandCft;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public String asSummary() {
        return String.format(
                Locale.US,
                "Wall volume: %.2f cft\nBricks: %d nos\nCement: %.2f bags\nSand: %.2f cft\nEstimated cost: Rs %.0f",
                wallVolumeCft,
                bricks,
                cementBags,
                sandCft,
                estimatedCost
        );
    }
}
