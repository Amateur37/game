import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameDataIO {
    private static final String DATA_FILE_PATH = "game_data/player_data.txt";

    // 保存角色数据到文件（覆盖写入）
    public static void savePlayerData(Role player) throws IOException {
        // 确保目录存在
        File dir = new File("game_data");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 写入文件（使用BufferedWriter，格式：id|name|level|hp|maxHp|exp|damage|技能ID列表）
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE_PATH))) { // 使用try-with-resources自动关闭
            StringBuilder skillIds = new StringBuilder();
            for (Skill skill : player.getSkills()) {
                skillIds.append(skill.getId()).append(",");
            }
            String skillStr = skillIds.length() > 0 ? skillIds.substring(0, skillIds.length() - 1) : "";

            writer.write(String.format("%d|%s|%d|%d|%d|%d|%d|%s",
                    player.getId(),
                    player.getName(),
                    player.getLevel(),
                    player.getHp(),
                    player.getMaxHp(),
                    player.getExp(),
                    player.getDamage(),
                    skillStr));
        }
        System.out.println("\n角色数据已保存到：" + DATA_FILE_PATH);
    }

    // 从文件读取角色数据
    public static Role loadPlayerData() throws IOException {
        File file = new File(DATA_FILE_PATH);
        if (!file.exists()) {
            System.out.println("未找到角色数据文件，返回默认角色！");
            return new Role(1, "默认侠客");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE_PATH))) { // 使用try-with-resources自动关闭
            String line = reader.readLine();
            if (line == null || line.isEmpty()) {
                System.out.println("角色数据文件为空，返回默认角色！");
                return new Role(1, "默认侠客");
            }

            // 解析数据（按|分割）
            String[] parts = line.split("\\|");
            if (parts.length < 8) { // 增加数组长度校验
                System.out.println("角色数据格式错误，返回默认角色！");
                return new Role(1, "默认侠客");
            }
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            int level = Integer.parseInt(parts[2]);
            int hp = Integer.parseInt(parts[3]);
            int maxHp = Integer.parseInt(parts[4]);
            int exp = Integer.parseInt(parts[5]);
            int damage = Integer.parseInt(parts[6]);
            String skillIdStr = parts[7];

            // 创建角色并恢复属性
            Role player = new Role(id, name);
            player.setHp(hp);
            // 手动恢复等级、maxHp、exp、damage（因为默认构造方法是1级）
            try {
                // 通过反射修改私有属性（简化方案，实际项目建议加setter）
                java.lang.reflect.Field levelField = Role.class.getDeclaredField("level");
                levelField.setAccessible(true);
                levelField.set(player, level);

                java.lang.reflect.Field maxHpField = Role.class.getDeclaredField("maxHp");
                maxHpField.setAccessible(true);
                maxHpField.set(player, maxHp);

                java.lang.reflect.Field expField = Role.class.getDeclaredField("exp");
                expField.setAccessible(true);
                expField.set(player, exp);

                java.lang.reflect.Field damageField = Role.class.getDeclaredField("damage");
                damageField.setAccessible(true);
                damageField.set(player, damage);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 恢复技能（假设技能ID对应固定技能，实际项目需维护技能字典）
            if (skillIdStr != null && !skillIdStr.isEmpty()) { // 增加非空判断
                String[] skillIds = skillIdStr.split(",");
                List<Skill> defaultSkills = getDefaultSkills();
                for (String skillIdStrPart : skillIds) {
                    try {
                        int skillId = Integer.parseInt(skillIdStrPart);
                        for (Skill skill : defaultSkills) {
                            if (skill.getId() == skillId) {
                                player.getSkills().add(skill);
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("技能ID格式错误：" + skillIdStrPart);
                    }
                }
            }

            System.out.println("角色数据加载成功！");
            System.out.println("角色信息：" + name + "（等级" + level + "，HP：" + hp + "，经验：" + exp + "）");
            return player;
        }
    }

    private static List<Skill> getDefaultSkills() {
        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill(1, "剑斩", 1, 20, 2));
        skills.add(new Skill(2, "气波", 3, 35, 5));
        skills.add(new Skill(3, "绝杀", 5, 60, 10));
        skills.add(new Skill(999, "普通攻击", 1, 10, 0));
        return skills;
    }
}