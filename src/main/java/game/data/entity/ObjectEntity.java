package game.data.entity;

import config.Config;
import config.Version;
import packets.DataTypeProvider;
import se.llbit.nbt.CompoundTag;

public class ObjectEntity extends Entity {
    protected ObjectEntity() { }

    @Override
    protected void addNbtData(CompoundTag root) { }

    protected void setData(int data) { }

    public static Entity parse(DataTypeProvider provider) {
        PrimitiveEntity primitive = PrimitiveEntity.parse(provider);
        Entity ent = primitive.getEntity(ObjectEntity::new);

        if (ent == null) { return null; }

        ent.readPosition(provider);
        ent.pitch = provider.readNext();
        ent.yaw = provider.readNext();
        int data;
        if (Config.versionReporter().isAtLeast(Version.V1_19)) {
            provider.readNext(); // head rotation
            data = provider.readVarInt();
        } else {
            data = provider.readInt();
        }
        parseVelocity(provider);

        // only if it's an ObjectEntity do we actually set the data bit
        if (ent instanceof ObjectEntity) {
            ((ObjectEntity) ent).setData(data);
        }

        return ent;
    }
}
