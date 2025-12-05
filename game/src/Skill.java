public class Skill {
    private int id;
    private String name;
    private int levelLimit;
    private int skillDamage;
    private int coolDown;
    private long lastCastTime; // 上次释放时间（用于冷却判断）

    // 构造方法
    public Skill(int id, String name, int levelLimit, int skillDamage, int coolDown) {
        this.id = id;
        this.name = name;
        this.levelLimit = levelLimit;
        this.skillDamage = skillDamage;
        this.coolDown = coolDown;
        this.lastCastTime = 0;
    }

    // 检查是否能释放（等级+冷却）
    public boolean canCast(Role caster) {
        // 等级判断
        if (caster.getLevel() < this.levelLimit) {
            System.out.println(caster.getName() + "等级不足" + levelLimit + "级，无法释放" + name + "！");
            return false;
        }
        // 冷却判断（当前时间 - 上次释放时间 >= 冷却时间）
        long currentTime = System.currentTimeMillis() / 1000; // 转秒
        long remainingCoolDown = coolDown - (currentTime - lastCastTime);
        if (remainingCoolDown > 0) {
            System.out.println(name + "冷却中，剩余" + remainingCoolDown + "秒！");
            return false;
        }
        return true;
    }

    // 计算最终伤害（技能基础伤害 + 角色等级*0.5）
    public int getDamage(Role caster) {
        this.lastCastTime = System.currentTimeMillis() / 1000; // 更新上次释放时间
        return skillDamage + (int) (caster.getLevel() * 0.5);
    }

    // Getter/Setter
    public int getId() { return id; }
    public String getName() { return name; }
    public int getLevelLimit() { return levelLimit; }
    public int getSkillDamage() { return skillDamage; }
    public void setSkillDamage(int skillDamage) { this.skillDamage = skillDamage; }
}