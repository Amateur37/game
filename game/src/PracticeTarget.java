public class PracticeTarget {
    private int id;
    private String name;
    private int hp;

    public PracticeTarget(int id, String name) {
        this.id = id;
        this.name = name;
        this.hp = 100; // 靶子初始生命值
    }

    public void takeDamage(int damage) {
        this.hp = Math.max(0, this.hp - damage);
        System.out.println("靶子【" + name + "】承受" + damage + "点伤害，剩余生命值：" + hp);
        if (hp <= 0) {
            System.out.println("靶子【" + name + "】被摧毁！重置中...");
            this.hp = 100; // 靶子摧毁后重置
        }
    }

    // Getter
    public String getName() { return name; }
}