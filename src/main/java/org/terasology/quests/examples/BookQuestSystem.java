// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.quests.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.module.inventory.events.BeforeItemRemovedFromInventory;
import org.terasology.tasks.Quest;
import org.terasology.tasks.components.QuestComponent;
import org.terasology.tasks.events.BeforeQuestEvent;
import org.terasology.tasks.systems.QuestSystem;

/**
 * Handles quest attached to book entity.
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class BookQuestSystem extends BaseComponentSystem {

    private static final Logger logger = LoggerFactory.getLogger(BookQuestSystem.class);

    @In
    private QuestSystem questSystem;

    /**
     * This method prevents addition of quest if quest with same shortName is active.
     *
     * @param event  Event triggered by QuestSystem due to interaction with Quest Book
     * @param entity Entity on which quest is attached
     */
    @ReceiveEvent
    public void onQuestActivated(BeforeQuestEvent event, EntityRef entity) {
        String name = event.getQuestName();
        for (Quest activeQuest : questSystem.getActiveQuests()) {
            if (name.equals(activeQuest.getShortName())) {
                event.consume();
                logger.warn("Duplicate Quest " + name + " cancelled");
                break;
            }
        }
    }

    @ReceiveEvent
    public void onBookThrown(BeforeItemRemovedFromInventory event, EntityRef entity) {
        EntityRef entityThrown = event.getItem();
        if (entityThrown.hasComponent(QuestComponent.class)) {
            QuestComponent itemQuest = entityThrown.getComponent(QuestComponent.class);
            for (Quest quest : questSystem.getActiveQuests()) {
                if (quest.getShortName().equals(itemQuest.shortName)) {
                    questSystem.removeQuest(quest, true);
                    break;
                }
            }

        }
    }
}
