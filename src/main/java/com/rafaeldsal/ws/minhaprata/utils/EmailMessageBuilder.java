package com.rafaeldsal.ws.minhaprata.utils;

public class EmailMessageBuilder {

  private EmailMessageBuilder() {}

  public static String buildRecoveryMessage(String name, String code, String siteUrl) {
    StringBuilder sb = new StringBuilder();
    sb.append("Olá, ").append(name).append("!\n");
    sb.append("Recebemos uma solicitação para redefinir a sua senha no Minha Prata.\n\n");
    sb.append("\uD83D\uDEE1\uFE0F Para continuar com o processo, utilize o código abaixo:\n\n");
    sb.append("\uD83D\uDD10 Código de recuperação: **").append(code).append("**\n\n");
    sb.append("Este código é válido por alguns minutos e deve ser inserido no campo solicitado para que você possa criar uma nova senha.\n");
    sb.append("Se você **não solicitou** essa recuperação, pode ignorar este e-mail com segurança — nenhuma alteração será feita na sua conta.\n\n");
    sb.append("Com carinho,\n");
    sb.append("Equipe Minha Prata\n");
    sb.append(siteUrl).append(" | @minhaprata");

    return sb.toString();
  }

  public static String buildMessagePasswordChangedSuccess(String name, String urlSiteMinhaPrata) {
    StringBuilder sb = new StringBuilder();
    sb.append("Olá, ").append(name).append("!\n");
    sb.append("Sua senha foi alterada com sucesso. \uD83C\uDF89 \n\n");
    sb.append("Caso não tenha sido você quem fez essa alteração, recomendamos que entre em contato com a nossa equipe imediatamente para garantir a segurança da sua conta.\n");
    sb.append("Estamos sempre aqui para te ajudar!\n\n");
    sb.append("Com carinho,\n");
    sb.append("Equipe Minha Prata\n");
    sb.append(urlSiteMinhaPrata).append(" | @minhaprata");

    return sb.toString();
  }
}
