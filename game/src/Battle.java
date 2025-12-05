import java.util.List;
import java.util.Scanner;

public class Battle {
    private int id;
    private Role attacker;
    private List<Role> defenders;
    private boolean isActive;
    private int round;

    public Battle(int id, Role attacker, List<Role> defenders) {
        this.id = id;
        this.attacker = attacker;
        this.defenders = defenders;
        this.isActive = false;
        this.round = 0;
    }

    // 开始战斗
    public void start() {
        System.out.println("\n===== 战斗开始！=====");
        System.out.println("攻击者：" + attacker.getName() + "（等级" + attacker.getLevel() + "，HP：" + attacker.getHp() + "）");
        for (Role defender : defenders) {
            System.out.println("防御者：" + defender.getName() + "（等级" + defender.getLevel() + "，HP：" + defender.getHp() + "）");
        }
        this.isActive = true;
        this.round = 1;
        Scanner scanner = new Scanner(System.in);

        while (isActive) {
            System.out.println("\n===== 第" + round + "回合 =====");
            // 玩家回合（攻击者是玩家）
            if (attacker.getNpcDifficulty() == null) {
                playerTurn(scanner);
            } else {
                // NPC攻击者回合（简化：随机选技能）
                npcTurn();
            }

            // 检查战斗结果
            if (checkResult()) {
                end();
                break;
            }

            for (Role defender : defenders) {
                if (defender.getHp() > 0) {
                    npcAttack(defender, attacker);
                    if (checkResult()) {
                        end();

                        return;
                    }
                }
            }

            round++;
        }

    }

    // 玩家回合（选择技能攻击）
    private void playerTurn(Scanner scanner) {
        System.out.println("\n" + attacker.getName() + "的回合，请选择技能：");
        List<Skill> skills = attacker.getSkills();
        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            System.out.println(i + 1 + ". " + skill.getName() + "（伤害：" + skill.getSkillDamage() + "，冷却：" );
        }

        System.out.print("输入技能序号：");
        int skillIndex = scanner.nextInt() - 1;
        if (skillIndex < 0 || skillIndex >= skills.size()) {
            System.out.println("技能选择错误！");
            playerTurn(scanner);
            return;
        }

        Skill selectedSkill = skills.get(skillIndex);
        if (selectedSkill.canCast(attacker)) {
            executeAttack(selectedSkill);
        } else {
            playerTurn(scanner); // 技能不可释放，重新选择
        }
    }

    // NPC回合（随机选技能）
    private void npcTurn() {
        List<Skill> skills = attacker.getSkills();
        Skill randomSkill = skills.get((int) (Math.random() * skills.size()));
        System.out.println("\n" + attacker.getName() + "使用了" + randomSkill.getName() + "！");
        executeAttack(randomSkill);
    }

    // NPC攻击玩家
    private void npcAttack(Role npc, Role player) {
        List<Skill> skills = npc.getSkills();
        Skill skill = skills.get(0); // NPC默认用第一个技能
        System.out.println("\n" + npc.getName() + "攻击" + player.getName() + "！");
        int damage = skill.getDamage(npc);
        player.takeDamage(damage);
    }

    // 执行攻击
    public void executeAttack(Skill skill) {
        int damage = skill.getDamage(attacker);
        for (Role defender : defenders) {
            if (defender.getHp() > 0) {
                defender.takeDamage(damage);
                break; // 只攻击第一个存活的防御者
            }
        }
    }

    // 检查战斗结果
    public boolean checkResult() {
        // 攻击者死亡
        if (attacker.getHp() <= 0) {
            System.out.println("\n" + attacker.getName() + "战败！");
            return true;
        }
        // 所有防御者死亡
        boolean allDefenderDead = defenders.stream().allMatch(defender -> defender.getHp() <= 0);
        if (allDefenderDead) {
            System.out.println("\n所有敌人被击败！" + attacker.getName() + "获胜！");
            return true;
        }
        return false;
    }

    // 结束战斗（结算奖励）
    public void end() {
        this.isActive = false;
        System.out.println("\n===== 战斗结束 =====");
        if (attacker.getHp() > 0) {
            // 玩家获胜，根据NPC难度发放经验
            int expReward = 0;
            for (Role defender : defenders) {
                switch (defender.getNpcDifficulty()) {
                    case "easy": expReward += 50; break;
                    case "normal": expReward += 150; break;
                    case "hard": expReward += 300; break;
                }
            }
            attacker.addExp(expReward);
        } else {

            attacker.addExp(-20);
        }
    }
}