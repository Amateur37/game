import java.util.ArrayList;
import java.util.List;

public class NPCBattleScene {
    private int sceneId;
    private String sceneName;
    private Role player;
    private Role npc;
    private Battle battle;

    public NPCBattleScene(int sceneId, String sceneName, String npcDifficulty) {
        this.sceneId = sceneId;
        this.sceneName = sceneName;
        this.npc = new Role(100, "荒野强盗", npcDifficulty); // 创建NPC
    }

    // 玩家进入场景
    public void enter(Role player) {
        this.player = player;
        System.out.println("\n" + player.getName() + "进入【" + sceneName + "】！");
        System.out.println("遭遇NPC：" + npc.getName() + "（难度：" + npc.getNpcDifficulty() + "）");
        System.out.println("是否发起战斗？（1-是，2-否）");

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int choice = scanner.nextInt();
        if (choice == 1) {
            startBattle();
        } else {
            System.out.println(player.getName() + "逃离了场景！");
        }

    }

    // 开始战斗
    private void startBattle() {
        List<Role> defenders = new ArrayList<>();
        defenders.add(npc);
        this.battle = new Battle(1, player, defenders);
        battle.start();
    }
}