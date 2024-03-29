// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.quests.examples;

import com.google.common.collect.Lists;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3f;
import org.terasology.engine.entitySystem.entity.EntityStore;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.logic.nameTags.NameTagComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.utilities.Assets;
import org.terasology.engine.world.generation.EntityBuffer;
import org.terasology.engine.world.generation.EntityProviderPlugin;
import org.terasology.engine.world.generation.Region;
import org.terasology.engine.world.generation.facets.ElevationFacet;
import org.terasology.engine.world.generation.facets.SurfacesFacet;
import org.terasology.engine.world.generator.plugin.RegisterPlugin;
import org.terasology.gestalt.assets.management.AssetManager;
import org.terasology.nui.Color;
import org.terasology.tasks.components.QuestListComponent;

/**
 * Adds a quest point to any world generator
 */
@RegisterPlugin
public class QuestPointEntityProvider implements EntityProviderPlugin {

    @In
    private AssetManager assetManager;

    @Override
    public void process(Region region, EntityBuffer buffer) {
        ElevationFacet elevationFacet = region.getFacet(ElevationFacet.class);
        SurfacesFacet surfacesFacet = region.getFacet(SurfacesFacet.class);

        Vector2i questLoc = new Vector2i(16, 16);
        Vector2i beaconLoc = new Vector2i(-16, 16);

        if (contains(region, elevationFacet, questLoc, 2)) {
            float y = surfacesFacet.getPrimarySurface(elevationFacet, questLoc.x, questLoc.y)
                    .orElse(elevationFacet.getWorld(questLoc)) + 2;
            Vector3f pos3d = new Vector3f(questLoc.x(), y, questLoc.y());
            EntityStore questPoint = createQuestPoint(pos3d);

            buffer.enqueue(questPoint);
        }

        if (contains(region, elevationFacet, beaconLoc, 2)) {
            float y = surfacesFacet.getPrimarySurface(elevationFacet, beaconLoc.x, beaconLoc.y)
                    .orElse(elevationFacet.getWorld(beaconLoc)) + 2;
            Vector3f pos3d = new Vector3f(beaconLoc.x(), y, beaconLoc.y());
            EntityStore questPoint = createTargetBeacon(pos3d);

            buffer.enqueue(questPoint);
        }
    }

    private boolean contains(Region region, ElevationFacet elevationFacet, Vector2ic pos, float heightOff) {
        if (elevationFacet.getWorldArea().contains(pos)) {
            float y = elevationFacet.getWorld(pos) + heightOff;
            return region.getRegion().minY() <= y && region.getRegion().maxY() >= y;
        }
        return false;
    }

    private EntityStore createQuestPoint(Vector3f pos3d) {
        Prefab questPoint = Assets.getPrefab("QuestPoint").get();
        EntityStore entityStore = new EntityStore(questPoint);

        QuestListComponent questList = new QuestListComponent();
        questList.questItems = Lists.newArrayList("QuestCard");
        entityStore.addComponent(questList);

        NameTagComponent nameTagComponent = createNameTag(questList.questItems.toString());
        entityStore.addComponent(nameTagComponent);

        LocationComponent locationComponent = questPoint.getComponent(LocationComponent.class);
        if (locationComponent == null) {
            locationComponent = new LocationComponent(pos3d);
        } else {
            locationComponent.setWorldPosition(pos3d);
        }
        entityStore.addOrSaveComponent(locationComponent);
        return entityStore;
    }

    private EntityStore createTargetBeacon(Vector3f pos3d) {
        Prefab beaconMark = Assets.getPrefab("BeaconMark").get();
        EntityStore entityStore = new EntityStore(beaconMark);

        NameTagComponent nameTagComponent = createNameTag("Target");
        entityStore.addComponent(nameTagComponent);

        LocationComponent locationComponent = beaconMark.getComponent(LocationComponent.class);
        if (locationComponent == null) {
            locationComponent = new LocationComponent(pos3d);
        } else {
            locationComponent.setWorldPosition(pos3d);
        }
        entityStore.addOrSaveComponent(locationComponent);
        return entityStore;
    }

    private NameTagComponent createNameTag(String s) {
        NameTagComponent nameTagComponent = new NameTagComponent();
        nameTagComponent.text = s;
        nameTagComponent.textColor = Color.WHITE;
        nameTagComponent.yOffset = -1;
        nameTagComponent.scale = 2;
        return nameTagComponent;
    }
}
