import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PracticeScene {
    private int sceneId;
    private String sceneName;
    private List<PracticeTarget> targets;
    private Map<Skill, Integer> proficiency; // 技能熟练度（技能→练习次数）

    public PracticeScene(int sceneId, String sceneName) {
        this.sceneId = sceneId;
        this.sceneName = sceneName;
        this.targets = new ArrayList<>();
        this.proficiency = new HashMap<>();
        // 初始化2个练习靶子
        targets.add(new PracticeTarget(1, "木人桩A"));
        targets.add(new PracticeTarget(2, "木人桩B"));
    }

    // 玩家进入场景
    public void enter(Role player) {
        System.out.println("\n" + player.getName() + "进入【" + sceneName + "】，可练习技能提升熟练度！");
        System.out.println("场景内靶子：" + targets.stream().map(PracticeTarget::getName).toList());
    }

    // 练习技能
    public void practiceSkill(Role player, Skill skill, int targetIndex) {
        // 校验参数
        if (targetIndex < 0 || targetIndex >= targets.size()) {
            System.out.println("目标靶子不存在！");
            return;
        }
        if (!player.getSkills().contains(skill)) {
            System.out.println(player.getName() + "未掌握技能" + skill.getName() + "！");
            return;
        }
        if (!skill.canCast(player)) {
            return;
        }

        PracticeTarget target = targets.get(targetIndex);
        int damage = skill.getDamage(player);
        target.takeDamage(damage);

        // 更新熟练度
        int count = proficiency.getOrDefault(skill, 0) + 1;
        proficiency.put(skill, count);
        System.out.println(skill.getName() + "练习次数：" + count + "，当前熟练度：" + count);

        // 检查技能是否升级
        checkUpgrade(skill);
    }


    private void checkUpgrade(Skill skill) {
        int count = proficiency.getOrDefault(skill, 0);
        if (count >= 5 && count % 5 == 0) {
            int oldDamage = skill.getSkillDamage();
            int newDamage = (int) (oldDamage * 1.2);
            skill.setSkillDamage(newDamage);
            System.out.println("恭喜！技能【" + skill.getName() + "】熟练度达标，伤害提升20%！");
            System.out.println("原伤害：" + oldDamage + " → 新伤害：" + newDamage);
        }
    }

    // Getter
    public List<PracticeTarget> getTargets() { return targets; }
}