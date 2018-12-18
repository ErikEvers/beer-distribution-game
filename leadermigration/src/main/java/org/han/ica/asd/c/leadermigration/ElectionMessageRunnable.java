package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.leadermigration.componentInterfaces.IConnectorForLeaderElection;
import org.han.ica.asd.c.model.Player;

import javax.inject.Inject;
import java.util.List;

public class ElectionMessageRunnable implements Runnable {
	private Thread t;
	private ElectionModel electionModel;
	private Player player;
	private List<Player> receivedPlayers;
	private Object lock;

	@Inject private IConnectorForLeaderElection communication;

	ElectionMessageRunnable() {
	}

	@Override
	public void run() {
		synchronized(lock) {
			// This if statement ensures to not send this to yourself.
			if (!electionModel.getCurrentPlayer().equals(player)) {
				electionModel.setReceivingPlayer(player);
				ElectionModel model = communication.sendElectionMessage(electionModel, player);
				if (model != null && model.isAlive()) {
					receivedPlayers.add(electionModel.getReceivingPlayer());
				}
			}
		}
	}

	public void start(Object lock, List<Player> receivedPlayers, ElectionModel electionModel, Player player) {
		this.electionModel = electionModel;
		this.player = player;
		this.receivedPlayers = receivedPlayers;
		this.lock = lock;
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	public void join() throws InterruptedException {
		t.join();
	}
}
