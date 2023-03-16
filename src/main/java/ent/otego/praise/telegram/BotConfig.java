package ent.otego.praise.telegram;

import ent.otego.praise.data.BotDataRepository;
import ent.otego.praise.schedule.PraiseScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.commands.DeleteMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.GetMyCommands;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.List;

@Configuration
public class BotConfig {

    @Bean
    @Autowired
    public PraiseTheSunBot praiseTheSunBot(@Value("${bot.token}") String token,
                                           @Value("${bot.username}") String botUsername,
                                           BotDataRepository botDataRepository,
                                           @Lazy PraiseScheduler praiseScheduler) throws TelegramApiException {
        PraiseTheSunBot bot = new PraiseTheSunBot(token, botUsername, botDataRepository,
                praiseScheduler);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        updateBotCommands(bot);
        updateBotDescription(bot);
        return bot;
    }

    private void updateBotCommands(PraiseTheSunBot bot) throws TelegramApiException {
        BotCommand startCommand = BotCommand.builder()
                .command("start")
                .description("Alias for /location")
                .build();
        BotCommand locationCommand = BotCommand.builder()
                .command("location")
                .description("Set your location to calculate sun position. Command accepts "
                        + "arguments in following format: "
                        + "/location 17.945000 -90.166139 where 17.945000 is latitude and "
                        + "-90.166139 is longitude. Or send location in next message")
                .build();
        List<BotCommand> botCommands = List.of(startCommand, locationCommand);
        List<BotCommand> currentBotCommands = bot.execute(new GetMyCommands());
        if (currentBotCommands.size() != botCommands.size()
                || !currentBotCommands.containsAll(botCommands)) {
            SetMyCommands setMyCommands = SetMyCommands.builder()
                    .commands(botCommands)
                    .build();

            bot.execute(new DeleteMyCommands());
            bot.execute(setMyCommands);
        }
    }

    private void updateBotDescription(PraiseTheSunBot bot) {
        //похоже в данной версии апи все еще нет команды для получения и обновления описания бота
    }
}
