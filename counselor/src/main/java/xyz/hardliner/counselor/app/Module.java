package xyz.hardliner.counselor.app;

public interface Module {

	Module initModule();

	boolean isOnline();

	Module tryToReinit();

}
