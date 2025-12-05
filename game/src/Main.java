import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Role player = null;

        // 第一步：加载/创建角色
        try {
            System.out.println("===== 侠客行游戏 =====");
            System.out.println("1. 加载已有角色");
            System.out.println("2. 创建新角色");
            System.out.print("请选择：");
            int choice = scanner.nextInt();
            scanner.nextLine(); // 吸收换行符

            if (choice == 1) {
                player = GameDataIO.loadPlayerData();
            } else {
                System.out.print("请输入角色名称：");
                String name = scanner.nextLine();
                player = new Role(1, name);
                System.out.println("新角色创建成功：" + name);
                // 初始技能（普通攻击）
                player.learnSkill(new Skill(999, "普通攻击", 1, 10, 0));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("角色数据读写失败，创建默认角色！");
            player = new Role(1, "默认侠客");
            player.learnSkill(new Skill(999, "普通攻击", 1, 10, 0));
        }

        // 第二步：游戏主菜单
        while (true) {
            System.out.println("\n===== 主菜单 =====");
            System.out.println("1. 学习技能");
            System.out.println("2. 进入练习场景");
            System.out.println("3. 进入NPC战斗场景（简单）");
            System.out.println("4. 进入NPC战斗场景（普通）");
            System.out.println("5. 进入NPC战斗场景（困难）");
            System.out.println("6. 保存角色数据");
            System.out.println("7. 退出游戏");
            System.out.print("请选择：");
            int menuChoice = scanner.nextInt();

            switch (menuChoice) {
                case 1:
                    learnSkillMenu(player);
                    break;
                case 2:
                    enterPracticeScene(player, scanner);
                    break;
                case 3:
                    enterNPCBattleScene(player, "easy");
                    break;
                case 4:
                    enterNPCBattleScene(player, "normal");
                    break;
                case 5:
                    enterNPCBattleScene(player, "hard");
                    break;
                case 6:
                    savePlayerData(player);
                    break;
                case 7:
                    System.out.println("感谢游玩，再见！");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("选择错误，请重新输入！");
            }
        }
    }

    // 学习技能菜单
    private static void learnSkillMenu(Role player) {
        System.out.println("\n===== 学习技能 =====");
        Skill skill1 = new Skill(1, "剑斩", 1, 20, 2);
        Skill skill2 = new Skill(2, "气波", 3, 35, 5);
        Skill skill3 = new Skill(3, "绝杀", 5, 60, 10);

        player.learnSkill(skill1);
        player.learnSkill(skill2);
        player.learnSkill(skill3);
    }

    // 进入练习场景
    private static void enterPracticeScene(Role player, Scanner scanner) {
        PracticeScene practiceScene = new PracticeScene(1, "练功房");
        practiceScene.enter(player);

        while (true) {
            System.out.println("\n===== 练习场景 =====");
            System.out.println("1. 练习技能");
            System.out.println("2. 退出场景");
            System.out.print("请选择：");
            int choice = scanner.nextInt();

            if (choice == 1) {
                System.out.println("选择靶子：");
                for (int i = 0; i < practiceScene.getTargets().size(); i++) {
                    System.out.println(i + 1 + ". " + practiceScene.getTargets().get(i).getName());
                }
                System.out.print("输入靶子序号：");
                int targetIndex = scanner.nextInt() - 1;

                System.out.println("选择技能：");
                for (int i = 0; i < player.getSkills().size(); i++) {
                    Skill skill = player.getSkills().get(i);
                    System.out.println(i + 1 + ". " + skill.getName());
                }
                System.out.print("输入技能序号：");
                int skillIndex = scanner.nextInt() - 1;

                if (skillIndex >= 0 && skillIndex < player.getSkills().size()) {
                    Skill selectedSkill = player.getSkills().get(skillIndex);
                    practiceScene.practiceSkill(player, selectedSkill, targetIndex);
                } else {
                    System.out.println("技能选择错误！");
                }
            } else if (choice == 2) {
                System.out.println(player.getName() + "退出了练习场景！");
                break;
            } else {
                System.out.println("选择错误！");
            }
        }
    }

    // 进入NPC战斗场景
    private static void enterNPCBattleScene(Role player, String difficulty) {
        NPCBattleScene battleScene = new NPCBattleScene(2, "荒野", difficulty);
        battleScene.enter(player);
    }

    // 保存角色数据
    private static void savePlayerData(Role player) {
        try {
            GameDataIO.savePlayerData(player);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("数据保存失败！");
        }
    }
}