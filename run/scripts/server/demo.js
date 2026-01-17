// ScriptBridge Demo Script
// 这是一个演示脚本，展示了如何使用 ScriptBridge API 与游戏进行交互

// 1. 基础日志输出
game.log("加载演示脚本: demo.js");

// 2. 全服广播
// 当脚本加载时，向所有玩家广播一条消息
game.broadcast("§a[ScriptBridge] §f演示脚本已加载！");

// 3. 定义一个函数来奖励玩家
// 你可以在游戏中或其他脚本中调用这个逻辑（如果暴露给全局）
function rewardPlayer(playerName) {
    game.log("正在奖励玩家: " + playerName);
    
    // 设置玩家生命值为 20 (满血)
    game.modifyPlayer(playerName, "health", 20.0);
    
    // 设置玩家饱食度为 20 (满腹)
    game.modifyPlayer(playerName, "food", 20.0);
    
    game.broadcast("§6" + playerName + " §f已被脚本治愈！");
}

// 4. 生成一些方块结构
// 在坐标 (0, 100, 0) 附近生成一个简单的 3x3 钻石平台
function buildPlatform() {
    game.log("正在生成钻石平台...");
    var startX = 0;
    var startY = 100;
    var startZ = 0;
    
    for (var x = 0; x < 3; x++) {
        for (var z = 0; z < 3; z++) {
            game.spawnBlock(startX + x, startY, startZ + z, "minecraft:diamond_block");
        }
    }
    game.log("平台生成完毕！");
}

// 执行生成平台的操作
buildPlatform();

// 注意：modifyPlayer 需要玩家在线才能生效。
// 如果你在服务器启动时直接运行此脚本，且没有任何玩家在线，下面的调用将提示找不到玩家。
// rewardPlayer("Dev");
