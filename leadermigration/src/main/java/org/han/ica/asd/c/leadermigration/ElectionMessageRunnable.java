package org.han.ica.asd.c.leadermigration;

import org.han.ica.asd.c.interfaces.leadermigration.IConnectorForLeaderElection;
import org.han.ica.asd.c.model.interface_models.ElectionModel;
import org.han.ica.asd.c.model.domain_objects.Player;

import javax.inject.Inject;
import java.util.List;

/**
 * Runnable for sending an election message to a player.
 */
public class ElectionMessageRunnable implements Runnable {
	private Thread t;
	private ElectionModel electionModel;
	private Player player;
	private List<Player> receivedPlayers;
	private Object lock;

	@Inject private IConnectorForLeaderElection communication;

	/**
	 * Empty constructor for dependency injection.
	 */
	ElectionMessageRunnable() {
		// default
	}


	/**
	 * Execute the runnable, which sends the election message and handles the response.
	 */
	@Override
	public void run() {
		synchronized(lock) {
			// This if statement ensures to not send the election message to yourself.
			if (!electionModel.getCurrentPlayer().equals(player)) {
				electionModel.setReceivingPlayer(player);
				ElectionModel model = communication.sendElectionMessage(electionModel, player);
				if (model != null && model.isAlive()) {
					receivedPlayers.add(electionModel.getReceivingPlayer());
				}
			}
		}
	}

	/**
	 * Create a new thread and start it with all required data.
	 * @param lock object used for synchronizing across threads.
	 * @param receivedPlayers players that have responded on the election message.
	 * @param electionModel object that should be sent in the election message.
	 * @param player player that should receive the election message.
	 */
	void start(Object lock, List<Player> receivedPlayers, ElectionModel electionModel, Player player) {
		this.electionModel = electionModel;
		this.player = player;
		this.receivedPlayers = receivedPlayers;
		this.lock = lock;
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	/**
	 * Wait for the thread to finish.
	 * @throws InterruptedException can be thrown if the thread is interrupted.
	 */
	void join() throws InterruptedException {
		t.join();
	}
}
