package mirai;

import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.GlobalScope;
import net.mamoe.mirai.console.command.CommandManager;

public class RunMirai {
   
    // 执行 gradle task: runMiraiConsole 来自动编译, shadow, 复制, 并启动 pure console.

    public static void main(String[] args) throws InterruptedException {
        // 默认在 /test 目录下运行

        // MiraiConsolePureLoader.load("1.1-EA", "0.5.2"); // 启动 console

        // 阻止主线程退出
        // BuildersKt.runBlocking(GlobalScope.INSTANCE.getCoroutineContext(), (coroutineScope, continuation) -> CommandManager.INSTANCE.join(continuation));
    }
}