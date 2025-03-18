package packets.handler.version;

import config.Config;
import game.data.WorldManager;
import game.data.dimension.Dimension;
import game.protocol.Protocol;
import game.protocol.ProtocolVersionHandler;
import packets.builder.PacketBuilder;
import packets.handler.ClientBoundGamePacketHandler;
import packets.handler.PacketOperator;
import proxy.ConnectionManager;

import java.util.Map;

import static packets.builder.NetworkType.*;

public class ClientBoundGamePacketHandler_1_14 extends ClientBoundGamePacketHandler {
    public ClientBoundGamePacketHandler_1_14(ConnectionManager connectionManager) {
        super(connectionManager);

        Protocol protocol = Config.versionReporter().getProtocol();

        Map<String, PacketOperator> operators = getOperators();
        operators.put("Login", provider -> {
            PacketBuilder replacement = new PacketBuilder(protocol.clientBound("Login"));
            replacement.copy(provider, INT, BYTE);

            // current dimension
            int dimensionEnum = provider.readInt();
            WorldManager.getInstance().setDimension(Dimension.fromId(dimensionEnum));
            replacement.writeInt(dimensionEnum);

            replacement.copy(provider, BYTE, STRING);

            // view distance
            int viewDist = provider.readVarInt();
            replacement.writeVarInt(Math.max(viewDist, Config.getExtendedRenderDistance()));

            replacement.copy(provider, BOOL);

            getConnectionManager().getEncryptionManager().sendImmediately(replacement);
            return false;
        });

        operators.put("OpenScreen", provider -> {
            int windowId = provider.readNext();

            int windowType = provider.readVarInt();
            String windowTitle = provider.readChat();

            WorldManager.getInstance().getContainerManager().openWindow(windowId, windowType, windowTitle);
            return true;
        });
    }
}
