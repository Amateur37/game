import java.util.ArrayList;
import java.util.List;

public class Role {
    private int id;
    private String name;
    private int level;
    private int hp;
    private int maxHp;
    private int exp;
    private int damage;
    private List<Skill> skills;
    private String npcDifficulty; // 仅NPC使用（easy/normal/hard）

    // 玩家构造方法
    public Role(int id, String name) {
        this.id = id;
        this.name = name;
        this.level = 1; // 初始等级1
        this.maxHp = 100; // 初始最大生命值
        this.hp = maxHp; // 初始生命值满
        this.exp = 0; // 初始经验0
        this.damage = 10; // 初始基础伤害
        this.skills = new ArrayList<>();
        this.npcDifficulty = null;
    }

    // NPC构造方法
    public Role(int id, String name, String npcDifficulty) {
        this.id = id;
        this.name = name;
        this.npcDifficulty = npcDifficulty;
        // 根据难度设置NPC属性
        switch (npcDifficulty) {
            case "easy":
                this.level = 2;
                this.maxHp = 80;
                this.damage = 8;
                break;
            case "normal":
                this.level = 5;
                this.maxHp = 150;
                this.damage = 15;
                break;
            case "hard":
                this.level = 10;
                this.maxHp = 300;
                this.damage = 30;
                break;
        }
        this.hp = maxHp;
        this.exp = 0;
        this.skills = new ArrayList<>();
        // NPC默认技能
        this.skills.add(new Skill(999, "普通攻击", 1, this.damage, 0));
    }

    // 承受伤害
    public void takeDamage(int damage) {
        this.hp = Math.max(0, this.hp - damage);
        System.out.println(name + "承受" + damage + "点伤害，剩余生命值：" + hp);
    }

    // 学习技能（等级限制）
    public boolean learnSkill(Skill skill) {
        if (this.level >= skill.getLevelLimit()) {
            this.skills.add(skill);
            System.out.println(name + "学会技能：" + skill.getName() + "！");
            return true;
        } else {
            System.out.println(name + "等级不足" + skill.getLevelLimit() + "级，无法学习" + skill.getName() + "！");
            return false;
        }
    }

    // 升级逻辑
    private void levelUp() {
        int nextLevelExp = level * 100; // 升级所需经验：等级*100
        while (exp >= nextLevelExp) {
            exp -= nextLevelExp;
            level++;
            maxHp += 50; // 每级增加50最大生命值
            hp = maxHp; // 升级回满状态
            damage += 5; // 每级增加5基础伤害
            System.out.println("恭喜" + name + "升级到" + level + "级！生命值、攻击力提升！");
            nextLevelExp = level * 100;
        }
    }

    // 添加经验（自动触发升级）
    public void addExp(int exp) {
        this.exp += exp;
        System.out.println(name + "获得" + exp + "点经验，当前经验：" + this.exp);
        levelUp();
    }

    // Getter/Setter
    public int getId() { return id; }
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getMaxHp() { return maxHp; }
    public int getExp() { return exp; }
    public int getDamage() { return damage; }
    public List<Skill> getSkills() { return skills; }
    public String getNpcDifficulty() { return npcDifficulty; }
}
