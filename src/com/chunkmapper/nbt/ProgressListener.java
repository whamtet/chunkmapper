package com.chunkmapper.nbt;


public interface ProgressListener {
    public void progressStartNoAbort(String string);

    public void progressStart(String string);

    public void progressStage(String string);

    public void progressStagePercentage(int i);

}
