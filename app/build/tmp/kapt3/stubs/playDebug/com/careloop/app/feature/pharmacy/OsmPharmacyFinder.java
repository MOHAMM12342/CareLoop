package com.careloop.app.feature.pharmacy;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J(\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\nH\u0002J$\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u0011\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\nH\u0082@\u00a2\u0006\u0002\u0010\u0013J6\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f2\u0006\u0010\u0015\u001a\u00020\n2\u0006\u0010\u0016\u001a\u00020\n2\b\b\u0002\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u001aR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/careloop/app/feature/pharmacy/OsmPharmacyFinder;", "", "pharmacyDao", "Lcom/careloop/app/data/local/dao/PharmacyDao;", "(Lcom/careloop/app/data/local/dao/PharmacyDao;)V", "httpClient", "Lio/ktor/client/HttpClient;", "calculateDistanceMeters", "", "lat1", "", "lon1", "lat2", "lon2", "getCachedPharmaciesWithDistance", "", "Lcom/careloop/app/domain/model/Pharmacy;", "currentLat", "currentLon", "(DDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getNearbyPharmacies", "latitude", "longitude", "radiusMeters", "isOnline", "", "(DDIZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_playDebug"})
public final class OsmPharmacyFinder {
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.data.local.dao.PharmacyDao pharmacyDao = null;
    @org.jetbrains.annotations.NotNull()
    private final io.ktor.client.HttpClient httpClient = null;
    
    public OsmPharmacyFinder(@org.jetbrains.annotations.NotNull()
    com.careloop.app.data.local.dao.PharmacyDao pharmacyDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getNearbyPharmacies(double latitude, double longitude, int radiusMeters, boolean isOnline, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.careloop.app.domain.model.Pharmacy>> $completion) {
        return null;
    }
    
    private final java.lang.Object getCachedPharmaciesWithDistance(double currentLat, double currentLon, kotlin.coroutines.Continuation<? super java.util.List<com.careloop.app.domain.model.Pharmacy>> $completion) {
        return null;
    }
    
    private final int calculateDistanceMeters(double lat1, double lon1, double lat2, double lon2) {
        return 0;
    }
}