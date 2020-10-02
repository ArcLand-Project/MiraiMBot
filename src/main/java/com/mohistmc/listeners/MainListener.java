package com.mohistmc.listeners;

import com.alibaba.fastjson.JSON;
import com.mohistmc.MiraiMBot;
import com.mohistmc.cmds.manager.CommandManager;
import com.mohistmc.utils.HasteUtils;
import com.mohistmc.utils.LogUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.RichMessage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MainListener extends SimpleListenerHost {
    @EventHandler
    public ListeningStatus onGroupMessage(GroupMessageEvent event) throws IOException {
        String content = event.getMessage().contentToString();
        if ((content.length() >= 300) && (event.getSender().getPermission() == MemberPermission.MEMBER)) {
            if (!JSON.isValidObject(content) && !content.startsWith("<?xml")){
                event.getGroup().sendMessage(new At(event.getSender()).plus(" 您的消息过长，正在转移～"));
                MiraiMBot.bot.recall(event.getMessage());
                event.getGroup().sendMessage("您的消息已经被转移到此：" + HasteUtils.pasteUbuntu(event.getSenderName(), content));
            }
        } else {
            if (content.startsWith(LogUtil.command)) {
                CommandManager.call(event.getMessage(), event.getSender());
            }
        }
        return ListeningStatus.LISTENING;
    }

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        throw new RuntimeException("在事件处理中发生异常", exception);
    }

}