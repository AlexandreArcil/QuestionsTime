package fr.canardnocturne.questionstime.config.upgrade;

public class ConfigurationUpgradeException extends Exception {

  public ConfigurationUpgradeException(final Throwable cause) {
        super(cause);
  }

  public ConfigurationUpgradeException(final String message) {
        super(message);
  }

}
