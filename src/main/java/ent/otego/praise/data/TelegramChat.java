package ent.otego.praise.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class TelegramChat implements Serializable {

    private static final long serialVersionUID = 15374321L;

    private Long chatId;
}
