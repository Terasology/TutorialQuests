/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.quests.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.events.RightMouseDownButtonEvent;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.tasks.components.QuestComponent;
import org.terasology.assets.management.AssetManager;
import org.terasology.registry.In;
import org.terasology.world.generator.plugin.RegisterPlugin;


/**
 * Handles quest attached to book entity.
 */
@RegisterSystem(value = RegisterMode.AUTHORITY)
public class BookQuestSystem {

    @In
    private AssetManager assetManager;

    private static final Logger logger = LoggerFactory.getLogger(BookQuestSystem.class);

    @ReceiveEvent
    public void onquestActivated(ActivateEvent event, QuestComponent quest) {
        logger.warn("oquestActivated called");
    }

}
