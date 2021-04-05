package me.skids.margeleisgay.auth.impl;

import me.skids.margeleisgay.auth.AuthModule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CheckVMProcess implements AuthModule {
	private ArrayList<String> targetProcess;
	private Process process = null;
	@Override
	public void onEnable() {
		targetProcess = new ArrayList<>();
		targetProcess.add("vmware.exe");
		targetProcess.add("VBoxService.exe");
		try {
			process = Runtime.getRuntime().exec("cmd.exe /c tasklist");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		process = null;
	}

	public boolean method1(){
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				for (String target : targetProcess) {
					if(line.contains(target)) {
						return false;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean run() {
		return method1();
	}

}
