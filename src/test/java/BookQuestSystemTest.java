import com.google.api.client.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.common.ActivateEvent;
import org.terasology.logic.inventory.InventoryManager;
import org.terasology.logic.players.PlayerCharacterComponent;
import org.terasology.moduletestingenvironment.ModuleTestingEnvironment;
import org.terasology.tasks.Quest;
import org.terasology.tasks.components.QuestComponent;
import org.terasology.tasks.systems.QuestSystem;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.BlockManager;

import java.util.Set;
import static org.junit.Assert.*;


public class BookQuestSystemTest extends ModuleTestingEnvironment {
    private InventoryManager inventoryManager;
    private EntityManager entityManager;
    private Time time;
    private QuestSystem questSystem;

    @Override
    public Set<String> getDependencies() {
        Set<String> modules = Sets.newHashSet();
        modules.add("Tasks");
        modules.add("Books");
        return modules;
    }

    @Before
    public void initialize() {
        inventoryManager = getHostContext().get(InventoryManager.class);
        entityManager = getHostContext().get(EntityManager.class);
        time = getHostContext().get(Time.class);
        questSystem = getHostContext().get(QuestSystem.class);
    }

    @Test
    public void questAdditionTest() {
        final EntityRef book = entityManager.create("QuestExamples:BookQuest");
        final EntityRef player = entityManager.create();
        player.addComponent(new PlayerCharacterComponent());
        inventoryManager.giveItem(player, EntityRef.NULL, book);
        String questId = book.getComponent(QuestComponent.class).shortName;

        book.send(new ActivateEvent(
                book,                    // target
                player,  // instigator
                null,                    // origin
                null,                    // direction
                null,                    // hit position
                null,                    // hit normal
                0                        // activation id
        ));
        assertTrue(questActivated(questId));

    }

    public boolean questActivated(String questId) {
        for (Quest quest : questSystem.getActiveQuests()) {
            if (quest.getShortName().equals(questId)) {
                return true;
            }
        }
        return false;
    }


}
