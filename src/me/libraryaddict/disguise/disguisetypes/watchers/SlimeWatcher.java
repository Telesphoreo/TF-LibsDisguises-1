package me.libraryaddict.disguise.disguisetypes.watchers;

import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.MetaIndex;
import me.libraryaddict.disguise.utilities.DisguiseUtilities;

public class SlimeWatcher extends InsentientWatcher {

    public SlimeWatcher(Disguise disguise) {
        super(disguise);
        setSize(DisguiseUtilities.random.nextInt(4) + 1);
    }

    public int getSize() {
        return (int) getData(MetaIndex.SLIME_SIZE);
    }

    public void setSize(int size) {
        if (size < 1) {
            size = 1;
        } else if (size > 50) {
            size = 50;
        }

        setData(MetaIndex.SLIME_SIZE, size);
        sendData(MetaIndex.SLIME_SIZE);
    }
}
