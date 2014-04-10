package com.freakz.hokan_ng.common.entity;

/**
 * User: petria
 * Date: 11/7/13
 * Time: 5:59 PM
 *
 * @author Petri Airio <petri.j.airio@gmail.com>
 */
public enum PropertyName {

  PROP_SYS_ACCESS_CONTROL("sys.AccessControl"),
  PROP_SYS_BOT_NICK("sys.BotNick"),
  PROP_SYS_CORE_ENGINE_UPTIME("sys.CoreEngineUptime"),
  PROP_SYS_CORE_ENGINE_RUNTIME("sys.CoreEngineRuntime"),
  PROP_SYS_CORE_IO_UPTIME("sys.CoreIoUptime"),
  PROP_SYS_CORE_IO_RUNTIME("sys.CoreIoRuntime"),
  PROP_SYS_EXEC("sys.Exec"),
  PROP_SYS_EXEC_USERS("sys.ExecUsers"),
  PROP_SYS_HTTP_PROXY_HOST("sys.HttpProxyHost"),
  PROP_SYS_HTTP_PROXY_PORT("sys.HttpProxyPort"),
  PROP_SYS_HTTP_USERAGENT("sys.UserAgent"),
  PROP_SYS_IGNORE("sys.Ignore"),
  PROP_SYS_MAX_CONNECTION_RETRY("sys.MaxConnectionRetry"),
  PROP_SYS_SUPER_USERS("sys.SuperUsers"),
  PROP_SYS_RAWLOG("sys.RawLog"),

  PROP_CHANNEL_DO_JOIN_MESSAGE("channel.DoJoinMessage"),
  PROP_CHANNEL_DO_KICK_REJOIN("channel.DoKickRejoin"),
  PROP_CHANNEL_DO_TVNOTIFY("channel.DoTvNotify"),
  PROP_CHANNEL_DO_STATS("channel.DoStats"),
  PROP_CHANNEL_DO_URL_TITLES("channel.DoUrlTitles"),
  PROP_CHANNEL_DO_WHOLELINE_TRICKERS("channel.DoWholeLineTrickers");


  /**
   * @param text
   */
  private PropertyName(String text) {
    this.text = text;
  }

  private final String text;

  /* (non-Javadoc)
   * @see java.lang.Enum#toString()
   */
  @Override
  public String toString() {
    return text;
  }
/*
  public static final String PROP_SYS_IGNORE_TITLES = "sys.IgnoreTitles";
  public static final String
  public static final String
  public static final String PROP_SYS_REPORT_TO = "sys.ReportTo";
  public static final String ;

  public static final String PROP_CHANNEL_DO_CMDS = "channel.DoCmds";
  public static final String PROP_CHANNEL_DO_JOIN_MSGS = "channel.DoJoinMsgs";
  public static final String PROP_CHANNEL_DO_LOGIN_OPS = "channel.DoLoginOps";
  public static final String PROP_CHANNEL_DO_SR = "channel.DoSR";
  public static final String PROP_CHANNEL_DO_TVNOTIFY = "channel.DoTvNotify";
  public static final String PROP_CHANNEL_DO_URL_TITLES = "channel.DoUrlTitles";
  public static final String PROP_CHANNEL_STATS_TIME = "channel.StatsTime";
  public static final String PROP_CHANNEL_TVNOTIFY_MAX_IDLE = "channel.TvNotifyMaxIdle";
  public static final String PROP_CHANNEL_WEB_CHAT_RESOLVE = "channel.WebChatResolve";
  public static final String PROP_CHANNEL_WHOLE_LINE_TRIGGERS = "channel.WholeLineTriggers";

 */

}
