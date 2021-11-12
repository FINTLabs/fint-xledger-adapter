package no.fint.xledger.okonomi;

import lombok.Getter;

public class CachedHandlerServiceMock  extends CachedHandlerService{

    @Getter
    private int refreshDataCount;

    public CachedHandlerServiceMock(ConfigFintCache cacheConfig) {
        super(cacheConfig);
    }

    @Override
    protected void refreshData() {
        refreshDataCount++;
    }

    public boolean hasRefreshed() {
        return refreshDataCount > 0;
    }

    public boolean hasNotRefreshed() {
        return refreshDataCount == 0;
    }
}
