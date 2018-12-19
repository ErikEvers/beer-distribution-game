package communicationcomponent.messagehandler;

import org.han.ica.asd.c.socketrpc.SocketClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.Socket;


@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class SocketClientTest {

    private SocketClient socketClient;

    @Mock
    private Socket socket;

    @BeforeEach
    void init() {
        socketClient = new SocketClient();
        socket = new Socket();
    }
    
}
