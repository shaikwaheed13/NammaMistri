package com.nammamistri.app.calculation;

public final class MaterialCalculator {
    private static final double BRICKS_PER_CFT = 14.22;
    private static final double BRICK_WASTAGE = 0.08;
    private static final double MORTAR_FACTOR = 0.25;
    private static final double DRY_FACTOR = 1.54;
    private static final double CEMENT_BAG_CFT = 1.23;

    private MaterialCalculator() {
    }

    public static WallCalculationResult calculateWall(
            double lengthFeet,
            double heightFeet,
            double thicknessInches,
            double brickRatePerThousand,
            double cementRatePerBag,
            double sandRatePerCft
    ) {
        if (lengthFeet <= 0 || heightFeet <= 0 || thicknessInches <= 0) {
            throw new IllegalArgumentException("Wall dimensions must be greater than zero.");
        }

        double thicknessFeet = thicknessInches / 12.0;
        double volumeCft = lengthFeet * heightFeet * thicknessFeet;
        int bricks = (int) Math.ceil(volumeCft * BRICKS_PER_CFT * (1.0 + BRICK_WASTAGE));

        double mortarVolume = volumeCft * MORTAR_FACTOR;
        double cementVolume = mortarVolume * (1.0 / 7.0) * DRY_FACTOR;
        double cementBags = cementVolume / CEMENT_BAG_CFT;
        double sandCft = mortarVolume * (6.0 / 7.0) * DRY_FACTOR;

        double cost = (bricks / 1000.0 * brickRatePerThousand)
                + (cementBags * cementRatePerBag)
                + (sandCft * sandRatePerCft);

        return new WallCalculationResult(volumeCft, bricks, cementBags, sandCft, cost);
    }
}
