package com.hijab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HijabApplication {

	public static void main(String[] args) {
		SpringApplication.run(HijabApplication.class, args);

		// 스레드 테스트
		System.out.println(Thread.currentThread().getName() + "Main thread Start");
		DaemonThread daemonThread = new DaemonThread();
		daemonThread.setDaemon(false); //데몬 스레드 여부
		daemonThread.start();
		System.out.println(Thread.currentThread().getName() + "Main thread End");
	}

	static class DaemonThread extends Thread {

		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + "Daemon thread running");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
