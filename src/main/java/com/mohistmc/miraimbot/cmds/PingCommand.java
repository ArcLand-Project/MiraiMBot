package com.mohistmc.miraimbot.cmds;

import com.mohistmc.miraimbot.cmds.manager.CommandExecutor;
import com.mohistmc.miraimbot.cmds.manager.CommandResult;
import com.mohistmc.miraimbot.cmds.manager.annotations.Command;
import com.mohistmc.miraimbot.console.log4j.MiraiMBotLog;
import com.mohistmc.miraimbot.utils.PingUtils;
import studio.trc.minecraft.serverpinglib.API.MCServerModInfo;
import studio.trc.minecraft.serverpinglib.API.MCServerSocket;
import studio.trc.minecraft.serverpinglib.API.MCServerStatus;
import studio.trc.minecraft.serverpinglib.Protocol.ProtocolVersion;

@Command(name = "ping", usage = "#ping 地址:端口")
public class PingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandResult result) {
        if (result.getArgs().size() <= 0) {
            result.sendMessage("======使用检测======\n请输入IP, 格式> ip:端口");
            return true;
        } else {
            String msg = result.getArgs().get(0);
            String[] ip = msg.split(":");
            result.sendMessage("======使用检测======\n正在检测，请稍后...");
            MCServerSocket socket = null;
            switch (ip.length) {
                case 1: {
                    socket = MCServerSocket.getInstance(ip[0], 25565);
                    break;
                }
                case 2: {
                    try {
                        socket = MCServerSocket.getInstance(ip[0], Integer.valueOf(ip[1]));
                    } catch (NumberFormatException ex) {
                        break;
                    }
                    break;
                }
                default: {
                    break;
                }
            }
            if (socket == null) return false;
            MCServerStatus status = socket.getStatus(ProtocolVersion.v1_12_2);
            if (!status.isMCServer()) {
                return false;
            }
            StringBuilder sb = new StringBuilder();
            StringBuilder mods = new StringBuilder();
            sb.append("======Mohist使用检测======").append("\n");
            sb.append("检测通过: 你正在使用Mohist, 感谢你的使用!!!").append("\n");
            sb.append("在线: " + status.getOnlinePlayers() + "/" + status.getMaxPlayers()).append("\n");
            boolean mohist = false;
            for (MCServerModInfo.MCMod mod : status.getModInfo().getModList()) {
                mods.append(mod.getModId() + ":" + mod.getVersion()).append("\n");
                String ver = mod.getVersion().replace(" ", "");
                if (mod.getModId().equals("mohist")) {
                    mohist = true;
                    sb.append("Mohist版本: " + (PingUtils.hasLatestVersion().equals(ver) ? ver + "(已是最新版)" : ver + "(你该更新了)")).append("\n");
                }
            }
            if (mods != null && mods.toString().contains("mohist:") && mohist) {
                sb.append("模组数量: " + (PingUtils.modsize(mods.toString(), ":") - 5)).append("\n");
                result.sendMessage(sb.toString());
                return true;
            } else {
                MiraiMBotLog.LOGGER.info(status.getVersion());
                result.sendMessage("======Mohist使用检测======\n此服务器不是Mohist, 可能原因：1.BC, 2.旧版Mohist, 3.其他核心");
            }
        }
        return true;
    }
}
