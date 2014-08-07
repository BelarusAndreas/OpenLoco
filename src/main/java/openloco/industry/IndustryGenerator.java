package openloco.industry;

import openloco.Assets;
import openloco.entities.IndustryAsset;
import openloco.graphics.CartCoord;
import openloco.graphics.Tile;

import java.util.*;
import java.util.stream.Collectors;

public class IndustryGenerator {

    private final Assets assets;
    private final Random random;

    public IndustryGenerator(Assets assets) {
        this.assets = assets;
        this.random = new Random();
    }

    public IndustryInstance generate(String industryType, int xTile, int yTile) {
        IndustryAsset industry = assets.getIndustry(industryType);
        long[] buildingIntanceTypes = industry.getBuildingInstances();

        List<BuildingInstance> buildingInstances = new ArrayList<>();

        Set<CartCoord> occupiedPositions = new HashSet<>();

        CartCoord position = new CartCoord(xTile, yTile, 0);

        for (int i=0; i<industry.getIndustryVars().getNumBuildingInstances(); i++) {
            buildingInstances.add(new BuildingInstance((int)buildingIntanceTypes[i], Tile.WIDTH*position.getX(), Tile.WIDTH*position.getY(), random.nextInt(4)));
            occupiedPositions.add(position);
            position = updatePosition(position, occupiedPositions);
        }

        return new IndustryInstance(industryType, buildingInstances);
    }

    private CartCoord updatePosition(CartCoord position, Set<CartCoord> occupiedPositions) {
        Set<CartCoord> invalidPositions = occupiedPositions.stream().flatMap(p -> {
            List<CartCoord> result = new LinkedList();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    result.add(new CartCoord(p.getX() + i, p.getY() + j, p.getZ()));
                }
            }
            return result.stream();
        }).collect(Collectors.toSet());

        List<CartCoord> potentialPositions = new ArrayList<>();
        for (int i=-2; i<=2; i++) {
            for (int j=-2; j<=2; j++) {
                CartCoord newPos = new CartCoord(position.getX()+i, position.getY()+j, position.getZ());
                if (!invalidPositions.contains(newPos)) {
                    potentialPositions.add(newPos);
                }
            }
        }

        return potentialPositions.get(random.nextInt(potentialPositions.size()));
    }

}
