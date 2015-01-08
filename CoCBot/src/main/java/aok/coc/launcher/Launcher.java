package aok.coc.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import aok.coc.exception.BotConfigurationException;
import aok.coc.exception.BotException;
import aok.coc.state.Context;
import aok.coc.state.StateIdle;
import aok.coc.util.ConfigUtils;

public class Launcher {

	private static final Logger	logger	= Logger.getLogger(Launcher.class.getName());

	static {
		try (InputStream inputStream = Launcher.class.getResourceAsStream("/logging.properties")) {
			LogManager.getLogManager().readConfiguration(inputStream);
		} catch (final IOException e) {
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}

	public static void main(String[] args) {
		// setup the bot
		try {
			Setup.setup();
		} catch (BotConfigurationException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			System.exit(1);
		}

		// run the bot
		Launcher launcher = new Launcher();
		try {
			launcher.start();
		} catch (BotException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			System.exit(2);
		}
	}

	private void start() throws BotException {
		ConfigUtils.initialize();
		Context context = new Context();
		context.setState(StateIdle.instance());
		while (true) {
			if (Thread.interrupted()) {
				break;
			}
			context.handle();
		}
	}
}
