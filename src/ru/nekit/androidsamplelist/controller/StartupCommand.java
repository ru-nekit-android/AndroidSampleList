package ru.nekit.androidsamplelist.controller;

import org.puremvc.java.interfaces.ICommand;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import ru.nekit.androidsamplelist.model.ForcePoint;
import ru.nekit.androidsamplelist.service.AccelerometerService;

public class StartupCommand extends SimpleCommand implements ICommand
{

	@Override
	public void execute(INotification notification) 
	{
		facade.registerProxy(new AccelerometerService());
		facade.registerProxy(new ForcePoint());
	};

}
