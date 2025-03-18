package game.data.entity.metadata;

import packets.DataTypeProvider;
import se.llbit.nbt.ByteTag;
import se.llbit.nbt.CompoundTag;
import se.llbit.nbt.IntTag;
import se.llbit.nbt.StringTag;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MetaData_1_13 extends MetaData {
    private static Map<Integer, Consumer<DataTypeProvider>> typeHandlers;
    static {
        // network.syncher.EntityDataSerializers
        typeHandlers = new HashMap<>();
        typeHandlers.put(0, DataTypeProvider::readNext);
        typeHandlers.put(1, DataTypeProvider::readVarInt);
        typeHandlers.put(2, DataTypeProvider::readFloat);
        typeHandlers.put(3, DataTypeProvider::readString);
        typeHandlers.put(4, DataTypeProvider::readChat);
        typeHandlers.put(5, DataTypeProvider::readOptChat);
        typeHandlers.put(6, DataTypeProvider::readSlot);
        typeHandlers.put(7, DataTypeProvider::readBoolean);
        typeHandlers.put(8, DataTypeProvider::readBoolean);
        typeHandlers.put(9, DataTypeProvider::readLong);
        typeHandlers.put(10, provider -> {
            if (provider.readBoolean()) {
                provider.readLong();
            }
        });
        typeHandlers.put(11, DataTypeProvider::readVarInt);
        // typeHandlers.put(12, DataTypeProvider::read);
        typeHandlers.put(13, DataTypeProvider::readVarInt);
        typeHandlers.put(14, DataTypeProvider::readNbtTag);
        // typeHandlers.put(15, DataTypeProvider::read);
        typeHandlers.put(16, (provider -> {
            provider.readVarInt();
            provider.readVarInt();
            provider.readVarInt();
        }));
        typeHandlers.put(17, DataTypeProvider::readOptVarInt);
        typeHandlers.put(18, DataTypeProvider::readVarInt);
    }

    private int air;
    private String customName;
    private boolean customNameVisible;
    private boolean isInvisible;
    private boolean isSilent;
    private boolean hasNoGravity;


    @Override
    public Consumer<DataTypeProvider> getTypeHandler(int i) {
        return typeHandlers.getOrDefault(i, null);
    }

    @Override
    public Consumer<DataTypeProvider> getIndexHandler(int i) {
        switch (i) {
            case 0: return provider -> isInvisible = (provider.readNext() & 0x20) > 0;
            case 1: return provider -> this.air = provider.readVarInt();
            case 2: return provider -> this.customName = provider.readOptChat();
            case 3: return provider -> this.customNameVisible = provider.readBoolean();
            case 4: return provider -> this.isSilent = provider.readBoolean();
            case 5: return provider -> this.hasNoGravity = provider.readBoolean();
        }
        return null;
    }

    @Override
    public void addNbtTags(CompoundTag nbt) {
        nbt.add("Silent", new ByteTag(isSilent ? 1 : 0));
        nbt.add("NoGravity", new ByteTag(hasNoGravity ? 1 : 0));
        nbt.add("Invisible", new ByteTag(isInvisible ? 1 : 0));
        nbt.add("CustomNameVisible", new ByteTag(customNameVisible ? 1 : 0));
        nbt.add("Air", new IntTag(air));
        if (customName != null && customName.length() > 0) {
            nbt.add("CustomName", new StringTag(customName));
        }
    }
}
