package ru.nekit.androidsamplelist.controller;

import org.puremvc.java.interfaces.ICommand;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import ru.nekit.androidsamplelist.service.AccelerometerService;

public class AccelerometerStopCommand extends SimpleCommand implements ICommand {

	@Override
	public void execute(INotification note) 
	{
		AccelerometerService service = (AccelerometerService)facade.retrieveProxy(AccelerometerService.NAME);
		service.stop();
	}
}